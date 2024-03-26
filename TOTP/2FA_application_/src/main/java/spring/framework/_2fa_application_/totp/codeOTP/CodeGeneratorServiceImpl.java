package spring.framework._2fa_application_.totp.codeOTP;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j // logger
@Service
@RequiredArgsConstructor
public final class CodeGeneratorServiceImpl implements CodeGeneratorService {

    private final CodeRepository codeRepository;
    private final CodeMapper codeMapper;
    private static final Logger logger = LoggerFactory.getLogger(CodeGeneratorServiceImpl.class);
    private final int[] DIGITS_POWER = { 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000 };

    // function inspired by RFC6238
    private byte[] hmacFunction(String algorithm, byte[] keyValue, byte[] text) {
        try {
            Mac hmac = Mac.getInstance(algorithm);
            SecretKeySpec macKey = new SecretKeySpec(keyValue, "RAW");
            hmac.init(macKey);
            return hmac.doFinal(text);
        } catch (NoSuchAlgorithmException | InvalidKeyException exp) {
            System.out.println(exp.getMessage());
        }

        return null;
    }

    // function taken from RFC6238
    private byte[] hexStr2Bytes(String hex) {

        // We add 10 to the beginning of the string to ensure that the value is
        // interpreted as positive because BigInteger expects a value
        // positive for the hex value
        byte[] bArray = new BigInteger("10" + hex, 16).toByteArray();
        // remove the sign byte
        byte[] ret = new byte[bArray.length - 1];
        for (int i = 0; i < ret.length; i++)
            ret[i] = bArray[i + 1];
        return ret;

    }

    // function inspired by RFC6238
    @Override
    public Mono<String> generateCode(String keyHex, final String time) {

        return codeRepository.findAll()
                .map(codeMapper::codeDataToCodeDataDto)
                .map(codeDataDTO -> {
                    String algorithm = codeDataDTO.getAlgorithm();
                    int numberOfDigitsCode = codeDataDTO.getNrDigits();

                    String modifierTime = time;
                    while (modifierTime.length() < 16) {
                        modifierTime = "0" + modifierTime;
                    }

                    byte[] key = hexStr2Bytes(keyHex);
                    byte[] timeText = hexStr2Bytes(modifierTime);
                    byte[] hash = hmacFunction(algorithm, key, timeText);

                    // choice of bytes that we will put in TOTP code
                    int offset = hash[hash.length - 1] & 0xF;
                    int binary = ((hash[offset] & 0x7f) << 24) |
                            ((hash[offset + 1] & 0xff) << 16) |
                            ((hash[offset + 2] & 0xff) << 8) |
                            (hash[offset + 3] & 0xff);

                    int otp = binary % DIGITS_POWER[numberOfDigitsCode];

                    String result = Integer.toString(otp);
                    while (result.length() < numberOfDigitsCode) {
                        result = "0" + result;
                    }

                    return result;
                })
                .next()
                .onErrorMap(error -> new Exception(error.getMessage()));
    }
}

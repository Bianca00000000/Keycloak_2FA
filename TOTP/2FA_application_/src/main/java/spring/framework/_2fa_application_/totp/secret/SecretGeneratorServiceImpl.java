package spring.framework._2fa_application_.totp.secret;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import spring.framework._2fa_application_.totp.codeOTP.CodeMapper;
import spring.framework._2fa_application_.totp.codeOTP.CodeRepository;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecretGeneratorServiceImpl implements SecretGeneratorService {

    private final CodeRepository codeRepository;
    private final CodeMapper codeMapper;
    private final SecretRepository secretRepository;
    private static final Logger logger = LoggerFactory.getLogger(SecretGeneratorServiceImpl.class);
    private final int NR_ITERATIONS = 600000; // this number of iterations is recommended by OWASP for SHA-256

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexStringBuilder = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            hexStringBuilder.append(String.format("%02x", b));
        }
        return hexStringBuilder.toString();
    }

    // I jump the current time
    private byte[] getSalt() {
        try {
            long timestamp = Instant.now().toEpochMilli();
            ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
            buffer.putLong(timestamp);
            byte[] salt = buffer.array();

            return salt;
        } catch (Exception exp) {
            System.out.println(exp.getMessage());
        }
        return null;
    }

    private String getRandom(int lengthSecret) throws InvalidKeyException {
        try {
            if (lengthSecret < 0) {
                return null;
            }
            SecureRandom secureRandom = new SecureRandom();
            byte[] randomBytes = new byte[lengthSecret];
            secureRandom.nextBytes(randomBytes);

            return bytesToHex(randomBytes);
        } catch (Exception exp) {
            System.out.println(exp.getMessage());
        }
        return null;
    }

    @Override
    public Mono<String> generate() {
        return codeRepository.findAll()
                .map(codeMapper::codeDataToCodeDataDto)
                .flatMap(codeDataDTO -> {
                    String algorithm = codeDataDTO.getAlgorithm();

                    int lengthSecret;
                    if (algorithm.equals("HMacSHA256")) {
                        lengthSecret = 32;
                    } else if (algorithm.equals("HMacSHA512")) {
                        lengthSecret = 64;
                    } else {
                        lengthSecret = -1;
                    }

                    try {
                        byte[] salt = getSalt();

                        // log.info( "Salt: " + Hex.encodeHexString(salt));

                        String randomSecret = getRandom(lengthSecret);

                        // log.info("Random: " + randomSecret);

                        if (randomSecret != null && salt != null && lengthSecret > -1) {
                            KeySpec spec = new PBEKeySpec(
                                    randomSecret.toCharArray(),
                                    salt,
                                    NR_ITERATIONS,
                                    lengthSecret * 8 // length in bits
                            );

                            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2With" + algorithm);
                            byte[] secretBytes = skf.generateSecret(spec).getEncoded();

                            // log.info("Secret: " + Hex.encodeHexString(secretBytes));

                            return Mono.just(Hex.encodeHexString(secretBytes));
                        } else {
                            return Mono.error(new RuntimeException("Error generating secret!\n"));
                        }
                    } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException e) {
                        return Mono.error(e);
                    }
                })
                .next();
    }

    // function for saving the secret in the database
}

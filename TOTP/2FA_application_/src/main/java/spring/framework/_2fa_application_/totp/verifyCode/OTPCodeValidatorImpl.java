package spring.framework._2fa_application_.totp.verifyCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import spring.framework._2fa_application_.totp.codeOTP.CodeGeneratorService;
import spring.framework._2fa_application_.totp.codeOTP.CodeMapper;
import spring.framework._2fa_application_.totp.codeOTP.CodeRepository;
import spring.framework._2fa_application_.totp.timeProvider.TimeProviderService;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Service
@RequiredArgsConstructor
public class OTPCodeValidatorImpl implements OTPCodeValidator {

    private final TimeProviderService ntpProviderTimeService;
    private final CodeRepository codeRepository;
    private final CodeMapper codeMapper;
    private final CodeGeneratorService codeGeneratorService;
    private static final Logger logger = LoggerFactory.getLogger(OTPCodeValidatorImpl.class);

    // I have to think a little about how I count the number of wrong attempts for
    // each user!!!!
    @Override
    public Mono<Boolean> isValidOtp(String code, String key, String username) {
        return ntpProviderTimeService.getTime()
                .flatMap(timeNtp -> codeRepository.findAll()
                        .map(codeMapper::codeDataToCodeDataDto)
                        .single()
                        .flatMap(codeDataDto -> {
                            int expirationTime = codeDataDto.getTime();
                            long syncTime = Long.parseLong(timeNtp) - Long.parseLong(timeNtp) % expirationTime;

                            try {
                                return codeGeneratorService.generateCode(key, Long.toString(syncTime))
                                        .flatMap(codeVerifier -> {
                                            if (codeVerifier.equals(code)) {
                                                return Mono.just(true);
                                            } else {
                                                long resyncTime = syncTime - 30;

                                                try {
                                                    return codeGeneratorService
                                                            .generateCode(key, Long.toString(resyncTime))
                                                            .map(codeVerifierResync -> codeVerifierResync.equals(code));
                                                } catch (InvalidKeyException | NoSuchAlgorithmException e) {
                                                    logger.error("Error generating OTP code" + e.getMessage());
                                                    return Mono.just(false);
                                                }
                                            }
                                        });
                            } catch (InvalidKeyException | NoSuchAlgorithmException e) {
                                logger.error("Error generating OTP code" + e.getMessage());
                                return Mono.just(false);
                            }
                        }))
                .onErrorResume(e -> {
                    logger.error("Error getting the time from the NTP server: " + e.getMessage());
                    return Mono.just(false);
                });
    }
}

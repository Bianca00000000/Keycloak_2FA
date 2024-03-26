package spring.framework._2fa_application_.totp.codeOTP;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@SpringBootTest
class CodeGeneratorServiceImplTest {

        @Autowired
        CodeGeneratorServiceImpl code;

        @Autowired
        CodeRepository codeRepository;

        @BeforeTestClass
        public static void setUpClass() {
                System.setProperty("GOOGLE_APPLICATION_CREDENTIALS",
                                "here you must add the path to the json file with the credentials from the service-account");
        }

        @Test
        void testCodeRepository() {
                codeRepository.findAll()
                                // with doOnNext I do the action for each element in the source stream at the
                                // moment it is issued
                                .doOnNext(codeData -> System.out.println(codeData))
                                .blockLast(); // Blocks execution until the flow ends
        }

        @Test
        void generateCode() {

                String seed32 = "3132333435363738393031323334353637383930" +
                                "3132333435363738393031323334353637383930" +
                                "3132333435363738393031323334353637383930" +
                                "31323334";
                long T0 = 0;
                long X = 30;
                long testTime[] = { 59L, 1111111109L, 1111111111L, 1234567890L, 2000000000L, 20000000000L };

                String steps = "0";
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                df.setTimeZone(TimeZone.getTimeZone("UTC"));
                try {
                        System.out.println(
                                        "+---------------+-----------------------+" +
                                                        "------------------+--------+--------+");
                        System.out.println(
                                        "|  Time(sec)    |   Time (UTC format)   " +
                                                        "| Value of T(Hex)  |  TOTP  | Mode   |");
                        System.out.println(
                                        "+---------------+-----------------------+" +
                                                        "------------------+--------+--------+");

                        for (int i = 0; i < testTime.length; i++) {
                                long T = (testTime[i] - T0) / X;
                                steps = Long.toHexString(T).toUpperCase();
                                while (steps.length() < 16)
                                        steps = "0" + steps;
                                String fmtTime = String.format("%1$-11s", testTime[i]);
                                String utcTime = df.format(new Date(testTime[i] * 1000));
                                System.out.print("|  " + fmtTime + "  |  " + utcTime +
                                                "  | " + steps + " |");

                                code.generateCode(seed32, steps)
                                                .doOnNext(codeResult -> {
                                                        System.out.println(codeResult + "| SHA256 |");
                                                })
                                                .block();
                                System.out.println(
                                                "+---------------+-----------------------+" +
                                                                "------------------+--------+--------+");
                        }
                } catch (final Exception e) {
                        System.out.println("Error : " + e);
                }
        }
}

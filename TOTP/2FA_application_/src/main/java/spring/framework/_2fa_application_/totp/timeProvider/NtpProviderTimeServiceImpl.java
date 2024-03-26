package spring.framework._2fa_application_.totp.timeProvider;

import java.io.IOException;
import java.net.InetAddress;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.NtpV3Packet;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.ntp.TimeStamp;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public final class NtpProviderTimeServiceImpl implements TimeProviderService {
    private final NTPUDPClient ntpudpClient;
    private final String server_ntp;

    public NtpProviderTimeServiceImpl() {
        this("pool.ntp.org");
    }

    public NtpProviderTimeServiceImpl(String server_ntp) {
        this(server_ntp, 5000);
    }

    public NtpProviderTimeServiceImpl(String server_ntp, int timeout) {

        this.ntpudpClient = new NTPUDPClient();
        ntpudpClient.setDefaultTimeout(timeout); // We set a period of time in which we waited for the answer
        this.server_ntp = server_ntp;
    }

    private Mono<Long> requestServerNtp() {
        // we use Mono.create because we want to start the process of getting the NTP
        // time immediately after someone subscribes to the stream
        return Mono.create(monoSinc -> {
            try {
                ntpudpClient.open();
                InetAddress hostAddr = InetAddress.getByName(server_ntp);
                final TimeInfo info = ntpudpClient.getTime(hostAddr);
                final NtpV3Packet message = info.getMessage();
                ntpudpClient.close();

                TimeStamp t1 = message.getOriginateTimeStamp(); // originate time => when the request is sent by the
                                                                // client
                long destTimeMillis = info.getReturnTime();
                TimeStamp t2 = message.getReceiveTimeStamp(); // receive time => when the request is received by the
                                                              // server
                TimeStamp t3 = message.getTransmitTimeStamp(); // transmit time => when the server sends the response
                TimeStamp t4 = TimeStamp.getNtpTime(destTimeMillis); // destination time => when the answer is received
                                                                     // by the client

                long localClockOffset = ((t2.getSeconds() - t1.getSeconds()) + (t3.getSeconds() - t4.getSeconds())) / 2;

                long currentTimeMillis = System.currentTimeMillis() / 1000 + localClockOffset;
                monoSinc.success(currentTimeMillis);
            } catch (IOException e) {
                monoSinc.error(e);
            }
        });
    }

    @Override
    public Mono<String> getTime() {
        return requestServerNtp().map(time -> Long.toString(time));
    }

}

package spring.framework._2fa_application_.totp.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Service;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import reactor.core.publisher.Mono;
import spring.framework._2fa_application_.totp.exceptions.QrGenerationException;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class QRCodeGeneratorServiceImpl implements QRCodeGeneratorService {

    private String uriEncode(String data){
        if (data == null) {
            return "";
        }
        try {
            return URLEncoder.encode(data, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Could not URI encode QrData.");
        }
    }
    @Override
    public String getUri(QrData data) {
        String uri = "otpauth://" +
                uriEncode(data.getType()) + "/" +
                uriEncode(data.getLabel()) + "?" +
                "secret=" + uriEncode(data.getShared_secret()) +
                "&digits=" + data.getNr_digits() +
                "&algorithm=" + uriEncode(data.getAlgorithm()) +
                "&time=" + data.getTime();
        return uri;
    }

    @Override
    public byte[] generateQrCode(QrData data){
        try {
            String uri = getUri(data);
            // genereaza matricea pentru codul QR
            BitMatrix bitMatrix = new MultiFormatWriter().encode(uri, BarcodeFormat.QR_CODE, data.getImageSize(), data.getImageSize());

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

            return pngOutputStream.toByteArray();
        } catch (Exception e) {
            QrGenerationException exp = new QrGenerationException("Failed to generate QR code. See nested exception.", e);
            System.out.println(exp.getMessage());

        }
        return null;
    }
}

package utilities.image;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.imgscalr.Scalr;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ImageUtilities {

    public static byte[] resizeBytesImage(byte[] image) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(image);
        BufferedImage bufferedImage;
        ByteArrayOutputStream byteArrayOutputStream = null;

        try {
            bufferedImage = ImageIO.read(byteArrayInputStream);
            BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_BGR);
            newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
            newBufferedImage = Scalr.resize(newBufferedImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.AUTOMATIC ,newBufferedImage.getWidth() / 2, newBufferedImage.getHeight() / 2);
            byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(newBufferedImage, "JPG", byteArrayOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (byteArrayOutputStream != null) ? byteArrayOutputStream.toByteArray() : null;
    }

    @SneakyThrows
    public static void captureIndividualElementScreenShots(WebElement WebElement) {
        File file = WebElement.getScreenshotAs(OutputType.FILE);
        String str = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM_dd_yyyy_HH_mm_ss"));
        FileUtils.copyFile(file, new File(System.getProperty("user.dir") + File.separator + "ScreenShots" + File.separator + str + File.separator + str + ".png"), true);
    }
}

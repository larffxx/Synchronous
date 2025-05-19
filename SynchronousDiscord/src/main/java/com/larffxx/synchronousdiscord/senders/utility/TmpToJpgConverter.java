package com.larffxx.synchronousdiscord.senders.utility;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
public class TmpToJpgConverter {
    public File convertToJpg(String filePath) {File inputFile = new File(filePath);
        File outputFile = new File("image.jpg");

        try {
            BufferedImage bufferedImage = ImageIO.read(inputFile);
            ImageIO.write(bufferedImage, "jpg", outputFile);
        } catch (IOException e) {
            //TODO custom exc
            throw new RuntimeException(e);
        }

        return outputFile;
    }

}

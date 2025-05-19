package com.larffxx.synchronoustelegram.handler.utility;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

@Component
public class TmpToJpgConverter {
    private java.io.File pngOutput;

    public File tmpConvertToJpg(File inputFile, Long fileID) {
        try {
            BufferedImage bufferedImage = ImageIO.read(inputFile);

            pngOutput = new java.io.File("C:/Users/offic/Desktop/tempphotos/photo" + fileID + ".jpg");
            ImageIO.write(bufferedImage, "jpg", pngOutput);
        } catch (IOException e) {
            //TODO: custom exc
            throw new RuntimeException(e);
        }

        return pngOutput;
    }
}


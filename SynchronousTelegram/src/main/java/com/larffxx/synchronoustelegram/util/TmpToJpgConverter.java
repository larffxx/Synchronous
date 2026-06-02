package com.larffxx.synchronoustelegram.util;

import com.larffxx.synchronoustelegram.domain.exception.data.mutation.PhotoConversionException;
import com.larffxx.synchronoustelegram.domain.exception.infexc.InfExcMessage;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Component
public class TmpToJpgConverter {
    public File tmpConvertToJpg(File inputFile, Long fileID) {
        File pngOutput;

        try {
            BufferedImage bufferedImage = ImageIO.read(inputFile);

            pngOutput = new java.io.File(Paths.get(
                    System.getProperty("user.home"), "Documents", "tempphotos","photo" + fileID +".jpg").toUri());

            ImageIO.write(bufferedImage, "jpg", pngOutput);
        } catch (IOException e) {
            throw new PhotoConversionException(String.format(InfExcMessage.CONVERSION_PHOTO_EXCEPTION, e.getMessage()));
        }

        return pngOutput;
    }
}


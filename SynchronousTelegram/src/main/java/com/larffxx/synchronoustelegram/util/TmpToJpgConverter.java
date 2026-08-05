package com.larffxx.synchronoustelegram.util;

import com.larffxx.synchronoustelegram.domain.exception.data.mutation.PhotoConversionException;
import com.larffxx.synchronoustelegram.domain.constant.infexc.InfExcMessage;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;

@Component
public class TmpToJpgConverter {

    public File tmpConvertToJpg(File inputFile, Long fileID) {
        File pngOutput;
        URI PATH_TO_PACKAGE = Paths.get(System.getProperty("user.home"), "Documents", "tempphotos","photo" + fileID +".jpg").toUri();

        try {
            BufferedImage bufferedImage = ImageIO.read(inputFile);

            pngOutput = new java.io.File(PATH_TO_PACKAGE);

            ImageIO.write(bufferedImage, "jpg", pngOutput);
        } catch (IOException e) {
            throw new PhotoConversionException(String.format(InfExcMessage.CONVERSION_PHOTO_EXCEPTION, e.getMessage()));
        }

        return pngOutput;
    }
}


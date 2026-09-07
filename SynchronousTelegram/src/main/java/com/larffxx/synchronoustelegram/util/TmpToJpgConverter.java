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

/**
 * Converts downloaded temporary photo files to JPEG format.
 */
@Component
public class TmpToJpgConverter {

    /**
     * Reads a temporary photo file and writes it as a JPEG file.
     *
     * @param inputFile temporary photo file to convert
     * @param fileID identifier used to name the converted file
     * @return converted JPEG file
     * @throws PhotoConversionException if the image cannot be read or written
     */
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


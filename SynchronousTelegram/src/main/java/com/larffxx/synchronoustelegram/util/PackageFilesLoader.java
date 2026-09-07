package com.larffxx.synchronoustelegram.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Builds local files from URI strings.
 */
public class PackageFilesLoader {
    /**
     * Creates a file object for each given URI string.
     *
     * @param uris URI strings pointing to local files
     * @return list of files built from the URIs
     */
    public List<File> getFilesFromURIs(List<String> uris){
        List<File> files = new ArrayList<>();

        for(String string : uris){
            files.add(new File(string));
        }

        return files;
    }
}

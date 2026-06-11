package com.larffxx.synchronoustelegram.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PackageFilesLoader {
    public List<File> getFilesFromURIs(List<String> uris){
        List<File> files = new ArrayList<>();

        for(String string : uris){
            files.add(new File(string));
        }

        return files;
    }
}

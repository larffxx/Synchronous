package com.larffxx.synchronoustelegram.sender.utility;

import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class PackageFilesLoader {
    public List<File> getFilesFromURIs(List<String> uris){
        List<File> files = new ArrayList<>();

        for(String string : uris){
            files.add(new File(string));
        }

        return files;
    }
}

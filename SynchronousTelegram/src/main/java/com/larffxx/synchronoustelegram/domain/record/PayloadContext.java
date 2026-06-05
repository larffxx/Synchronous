package com.larffxx.synchronoustelegram.domain.record;


import java.io.File;
import java.util.List;

public record PayloadContext (
    String chatID,
    String caption,
    List<File> files
){}
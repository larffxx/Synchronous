package com.larffxx.synchronoustelegram.util;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.*;

@Component
public class PhotoAlbumHolder {
    private final Map<String, List<List<PhotoSize>>> albumPhotos = new HashMap<>();

    public Optional<PhotoSize> getPhoto(String mediaGroupId, List<PhotoSize> photos) {
        collectAlbumPhotos(mediaGroupId, photos);
        return getLastPhotoFromAlbum(mediaGroupId);
    }

    private void collectAlbumPhotos(String mediaGroupId, List<PhotoSize> photos){
        if(mediaGroupId != null && !mediaGroupId.isEmpty()){
            albumPhotos.putIfAbsent(mediaGroupId, new ArrayList<>());
            albumPhotos.get(mediaGroupId).add(photos);
        }
    }

    private Optional<PhotoSize> getLastPhotoFromAlbum(String mediaGroupId) {
        List<List<PhotoSize>> photosList = albumPhotos.get(mediaGroupId);

        if (photosList != null && !photosList.isEmpty()) {
            List<PhotoSize> lastGroup = photosList.get(photosList.size() - 1);
            if (lastGroup != null && !lastGroup.isEmpty()) {
                return Optional.of(lastGroup.get(lastGroup.size() - 1));
            }
        }
        return Optional.empty();
    }

    public void clearAllAlbumPhotos(){
        albumPhotos.clear();
    }
}

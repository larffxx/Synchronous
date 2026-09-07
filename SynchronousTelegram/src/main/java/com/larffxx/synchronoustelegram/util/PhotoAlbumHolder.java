package com.larffxx.synchronoustelegram.util;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Accumulates photo sizes of Telegram media groups in memory.
 */
@Component
public class PhotoAlbumHolder {
    /**
     * Buffered photo sizes keyed by media group identifier.
     */
    private final Map<String, List<List<PhotoSize>>> albumPhotos = new ConcurrentHashMap<>();

    /**
     * Buffers the given photos and returns the latest one of the group.
     *
     * @param mediaGroupId media group identifier or null for single photos
     * @param photos photo sizes from the current update
     * @return latest photo of the group or empty if none is buffered
     */
    public Optional<PhotoSize> getPhoto(String mediaGroupId, List<PhotoSize> photos) {
        collectAlbumPhotos(mediaGroupId, photos);
        return getLastPhotoFromAlbum(mediaGroupId);
    }

    /**
     * Appends the given photos to the buffer of the media group.
     *
     * @param mediaGroupId media group identifier or null for single photos
     * @param photos photo sizes to buffer
     */
    private void collectAlbumPhotos(String mediaGroupId, List<PhotoSize> photos){
        if(mediaGroupId != null && !mediaGroupId.isEmpty()){
            albumPhotos.computeIfAbsent(mediaGroupId, id -> new ArrayList<>()).add(photos);
        }
    }

    /**
     * Returns the last buffered photo of the given media group.
     *
     * @param mediaGroupId media group identifier to read
     * @return last photo of the group or empty if none is buffered
     */
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

    /**
     * Drops the buffered photos of the given media group.
     * @param mediaGroupId media group identifier to evict
     */
    public void removeAlbumPhotos(String mediaGroupId){
        if (mediaGroupId != null) {
            albumPhotos.remove(mediaGroupId);
        }
    }
}

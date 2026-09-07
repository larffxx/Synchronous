package com.larffxx.synchronoustelegram.domain.mapper;

import com.larffxx.synchronoustelegram.domain.dto.ServersConnectDTO;
import com.larffxx.synchronoustelegram.domain.model.ServersConnect;
import org.springframework.stereotype.Component;

/**
 * Converts between server link entities and server link transfer objects.
 * Copies the Discord guild and Telegram channel identifiers in both directions.
 */
@Component
public class ServersConnectMapper implements Mapper<ServersConnect, ServersConnectDTO> {
    /**
     * Converts a server link entity to its transfer object.
     * @param serversConnect the server link entity to convert
     * @return the converted transfer object
     */
    public ServersConnectDTO toDTO(ServersConnect serversConnect) {
        ServersConnectDTO dto = new ServersConnectDTO();

        dto.setDiscordGuild(serversConnect.getDiscordGuild());
        dto.setTelegramChannel(serversConnect.getTelegramChannel());

        return dto;
    }

    /**
     * Converts a server link transfer object to its entity.
     * @param dto the transfer object to convert
     * @return the converted server link entity
     */
    public ServersConnect toEntity(ServersConnectDTO dto) {
        ServersConnect entity = new ServersConnect();

        entity.setDiscordGuild(dto.getDiscordGuild());
        entity.setTelegramChannel(dto.getTelegramChannel());

        return entity;
    }
}


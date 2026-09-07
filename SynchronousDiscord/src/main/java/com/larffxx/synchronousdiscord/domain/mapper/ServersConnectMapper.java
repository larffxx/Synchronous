package com.larffxx.synchronousdiscord.domain.mapper;

import com.larffxx.synchronousdiscord.domain.model.ServersConnect;
import com.larffxx.synchronousdiscord.domain.dto.ServersConnectDTO;
import org.springframework.stereotype.Component;

/**
 * Servers Connect Mapper class.
 */
@Component
public class ServersConnectMapper implements Mapper<ServersConnect,ServersConnectDTO> {
    /**
     * Converts entity to DTO.
     * @param serversConnect the servers connect.
     * @return the resulting servers connect dto.
     */
    public ServersConnectDTO toDTO(ServersConnect serversConnect) {
        ServersConnectDTO dto = new ServersConnectDTO();

        dto.setDiscordGuild(serversConnect.getDiscordGuild());
        dto.setTelegramChannel(serversConnect.getTelegramChannel());

        return dto;
    }

    /**
     * Converts DTO to entity.
     * @param dto the dto.
     * @return the resulting servers connect.
     */
    public ServersConnect toEntity(ServersConnectDTO dto) {
        ServersConnect entity = new ServersConnect();

        entity.setDiscordGuild(dto.getDiscordGuild());
        entity.setTelegramChannel(dto.getTelegramChannel());

        return entity;
    }
}

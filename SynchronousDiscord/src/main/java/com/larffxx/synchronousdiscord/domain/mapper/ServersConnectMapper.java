package com.larffxx.synchronousdiscord.domain.mapper;

import com.larffxx.synchronousdiscord.domain.model.ServersConnect;
import com.larffxx.synchronousdiscord.domain.dto.ServersConnectDTO;
import org.springframework.stereotype.Component;

@Component
public class ServersConnectMapper implements Mapper<ServersConnect,ServersConnectDTO> {
    public ServersConnectDTO toDTO(ServersConnect serversConnect) {
        ServersConnectDTO dto = new ServersConnectDTO();

        dto.setDiscordGuild(serversConnect.getDiscordGuild());
        dto.setTelegramChannel(serversConnect.getTelegramChannel());

        return dto;
    }

    public ServersConnect toEntity(ServersConnectDTO dto) {
        ServersConnect entity = new ServersConnect();

        entity.setDiscordGuild(dto.getDiscordGuild());
        entity.setTelegramChannel(dto.getTelegramChannel());

        return entity;
    }
}

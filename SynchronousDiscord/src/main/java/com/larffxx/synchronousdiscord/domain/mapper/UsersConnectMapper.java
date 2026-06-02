package com.larffxx.synchronousdiscord.domain.mapper;

import com.larffxx.synchronousdiscord.domain.model.UsersConnect;
import com.larffxx.synchronousdiscord.domain.dto.UsersConnectDTO;
import org.springframework.stereotype.Component;

@Component
public class UsersConnectMapper implements Mapper<UsersConnect,UsersConnectDTO> {
    public UsersConnectDTO toDTO(UsersConnect usersConnect) {
        UsersConnectDTO dto = new UsersConnectDTO();

        dto.setDiscordName(usersConnect.getDiscordName());
        dto.setTelegramName(usersConnect.getTelegramName());

        return dto;
    }

    public UsersConnect toEntity(UsersConnectDTO dto) {
        UsersConnect entity = new UsersConnect();

        entity.setDiscordName(dto.getDiscordName());
        entity.setTelegramName(dto.getTelegramName());

        return entity;
    }
}

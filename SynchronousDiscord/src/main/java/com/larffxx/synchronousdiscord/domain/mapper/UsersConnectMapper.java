package com.larffxx.synchronousdiscord.domain.mapper;

import com.larffxx.synchronousdiscord.domain.model.ServersConnect;
import com.larffxx.synchronousdiscord.domain.model.UsersConnect;
import com.larffxx.synchronousdiscord.domain.dto.UsersConnectDTO;
import org.springframework.stereotype.Component;

/**
 * Users Connect Mapper class.
 */
@Component
public class UsersConnectMapper implements Mapper<UsersConnect,UsersConnectDTO> {
    /**
     * Converts entity to DTO.
     * @param usersConnect the users connect.
     * @return the resulting users connect dto.
     */
    public UsersConnectDTO toDTO(UsersConnect usersConnect) {
        UsersConnectDTO dto = new UsersConnectDTO();

        dto.setDiscordName(usersConnect.getDiscordName());
        dto.setTelegramName(usersConnect.getTelegramName());
        dto.setDiscordUserId(usersConnect.getDiscordUserId());
        dto.setServersConnectId(usersConnect.getId());

        return dto;
    }

    /**
     * Converts DTO to entity.
     * @param dto the dto.
     * @return the resulting users connect.
     */
    public UsersConnect toEntity(UsersConnectDTO dto) {
        UsersConnect entity = new UsersConnect();

        entity.setDiscordName(dto.getDiscordName());
        entity.setTelegramName(dto.getTelegramName());
        entity.setDiscordUserId(dto.getDiscordUserId());
        entity.setServersConnect(new ServersConnect(dto.getServersConnectId()));

        return entity;
    }
}

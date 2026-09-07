package com.larffxx.synchronoustelegram.domain.mapper;

import com.larffxx.synchronoustelegram.domain.dto.UsersConnectDTO;
import com.larffxx.synchronoustelegram.domain.model.ServersConnect;
import com.larffxx.synchronoustelegram.domain.model.UsersConnect;

/**
 * Converts between user link entities and user link transfer objects.
 * Resolves the server link by identifier when building entities.
 */
public class UsersConnectMapper implements Mapper<UsersConnect, UsersConnectDTO> {
    /**
     * Converts a user link entity to its transfer object.
     * @param usersConnect the user link entity to convert
     * @return the converted transfer object
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
     * Converts a user link transfer object to its entity.
     * @param dto the transfer object to convert
     * @return the converted user link entity
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

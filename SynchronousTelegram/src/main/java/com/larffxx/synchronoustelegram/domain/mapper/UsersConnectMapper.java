package com.larffxx.synchronoustelegram.domain.mapper;

import com.larffxx.synchronoustelegram.domain.dto.UsersConnectDTO;
import com.larffxx.synchronoustelegram.domain.model.ServersConnect;
import com.larffxx.synchronoustelegram.domain.model.UsersConnect;

public class UsersConnectMapper implements Mapper<UsersConnect, UsersConnectDTO> {
    public UsersConnectDTO toDTO(UsersConnect usersConnect) {
        UsersConnectDTO dto = new UsersConnectDTO();

        dto.setDiscordName(usersConnect.getDiscordName());
        dto.setTelegramName(usersConnect.getTelegramName());
        dto.setDiscordUserId(usersConnect.getDiscordUserId());
        dto.setServersConnectId(usersConnect.getId());

        return dto;
    }

    public UsersConnect toEntity(UsersConnectDTO dto) {
        UsersConnect entity = new UsersConnect();

        entity.setDiscordName(dto.getDiscordName());
        entity.setTelegramName(dto.getTelegramName());
        entity.setDiscordUserId(dto.getDiscordUserId());
        entity.setServersConnect(new ServersConnect(dto.getServersConnectId()));

        return entity;
    }
}

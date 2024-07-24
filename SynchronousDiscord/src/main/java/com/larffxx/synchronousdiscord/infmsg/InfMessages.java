package com.larffxx.synchronousdiscord.infmsg;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class InfMessages {
    public static final String TELEGRAM_CHANNEL_NAME_FROM_OPTIONS = "connect";
    public static final String CONNECT_SUCCESS_MESSAGE = "Servers connected successfully!";
    public static final String TELEGRAM_CHANNEL = "telegram";
    public static final String GUILD_ID_FROM_TELEGRAM = "guildId";
    public static final String CREATE_PROFILE_SUCCESS_MESSAGE = "Profile was created successfully";
    public static final String CREATE_PROFILE_UNSUCCESSFUL_MESSAGE = "You have created a profile already, can edit with /edit";
    public static final String DESCRIPTION_OPTION = "description";
    public static final String PHOTO_OPTION = "photo";
    public static final String URL_OPTION = "url";
    public static final String NAME_FROM_TELEGRAM = "name";
    public static final String EDIT_PROFILE_SUCCESS_MESSAGE = "Profile was edited";
    public static final String LOOP_SUCCESS_MESSAGE = "Loop";
    public static final String LOOP_UNSUCCESSFUL_MESSAGE = "No music";
    public static final String PLAY_LINK_FROM_DISCORD = "link";
    public static final String PLAY_LINK_FROM_TELEGRAM = "options";
    public static final String PLAY_SUCCESS_MESSAGE = "Track was added";
    public static final String PROFILE_SUCCESS_MESSAGE = "Your profile";
    public static final String PROFILE_UNSUCCESSFUL_MESSAGE = "Create profile with /create command";
    public static final String QUEUE_SUCCESS_MESSAGE = "Current Queue:";
    public static final String QUEUE_UNSUCCESSFUL_MESSAGE = "Queue is empty";
    public static final String USER_REGISTER_SUCCESS_MESSAGE = "Successfully registered";
    public static final String USER_REGISTER_UNSUCCESSFUL_MESSAGE = "You have been registered before";
    public static final String SKIP_SUCCESS_MESSAGE = "Skipped";
    public static final String STOP_SUCCESS_MESSAGE = "Stopped";
    public static final String DELETE_SUCCESS_MESSAGE = "Successfully deleted %s messages";
}

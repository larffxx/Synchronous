package com.larffxx.synchronoustelegram.domain.constant.infmsg;

/**
 * User facing texts sent after command execution.
 * Holds the replies for connect, register, and start commands.
 */
public class CommandConstant {
    /**
     * Reply sent when servers are connected successfully.
     */
    public final static String SUCCESSFULLY_CONNECTED = "Servers connected";
    /**
     * Reply sent when the servers were already connected.
     */
    public final static String UNSUCCESSFULLY_CONNECTED = "Servers connected before";
    /**
     * Reply sent when a user is registered successfully.
     */
    public final static String USER_SUCCESSFULLY_REGISTERED = "User successfully registered";
    /**
     * Reply sent when the user was already registered.
     */
    public final static String USER_UNSUCCESSFULLY_REGISTERED = "User was registered before";
    /**
     * Reply sent when a coolness rating is forwarded to Discord.
     */
    public final static String COOLNESS_SENT = "Coolness will be applied in Discord";
    /**
     * Coolness total reply template.
     */
    public final static String COOLNESS_SHOW = "%s coolness: %d";
    /**
     * Reply sent when the user has no link yet.
     */
    public final static String USER_NOT_REGISTERED = "User is not registered";
    /**
     * Reply sent by the start command with the action choices.
     */
    public final static String START_MESSAGE = "Choose an action:";
}

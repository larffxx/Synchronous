package com.larffxx.synchronousdiscord.infrastructure.handler;

import com.larffxx.synchronousdiscord.domain.exception.consume.CommandConsumingException;
import com.larffxx.synchronousdiscord.domain.exception.consume.ConsumingException;
import com.larffxx.synchronousdiscord.domain.exception.consume.MessageConsumingException;
import com.larffxx.synchronousdiscord.domain.exception.service.GuildNotFoundException;
import com.larffxx.synchronousdiscord.domain.exception.service.NoConnectionBetweenServersException;
import com.larffxx.synchronousdiscord.domain.exception.service.TextChannelNotFoundException;
import com.larffxx.synchronousdiscord.domain.context.ErrorContext;
import com.larffxx.synchronousdiscord.domain.exception.DiscordSynchronousException;
import com.larffxx.synchronousdiscord.domain.exception.DownloadAttachmentException;
import com.larffxx.synchronousdiscord.domain.exception.RouteServiceException;
import com.larffxx.synchronousdiscord.domain.exception.VerifyException;
import com.larffxx.synchronousdiscord.domain.exception.command.CommandException;
import com.larffxx.synchronousdiscord.domain.exception.command.DeleteMessageException;
import com.larffxx.synchronousdiscord.domain.exception.interaction.DiscordSlashInteractionException;
import com.larffxx.synchronousdiscord.domain.exception.interaction.TelegramSlashInteractionException;
import com.larffxx.synchronousdiscord.domain.constant.infexc.InfExcMessages;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global Exception Handler class.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles discord synchronous exception.
     * @param exception the exception.
     */
    @ExceptionHandler(value = DiscordSynchronousException.class)
    public ResponseEntity<?> handleGeneric(DiscordSynchronousException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorContext(
                                InfExcMessages.SYNCHRONOUS_DISCORD_EXCEPTION,
                                exception.getMessage())
                );
    }

    /**
     * Handles download attachment exception.
     * @param exception the exception.
     */
    @ExceptionHandler(value = DownloadAttachmentException.class)
    public ResponseEntity<?> handleDownloadAttachmentException(DownloadAttachmentException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorContext(
                                InfExcMessages.DOWNLOAD_ATTACHMENT_EXCEPTION,
                                exception.getMessage())
                );
    }

    /**
     * Handles route service exception.
     * @param exception the exception.
     */
    @ExceptionHandler(value = RouteServiceException.class)
    public ResponseEntity<?> handleRouteServiceException(RouteServiceException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorContext(InfExcMessages.ROUTE_SERVICE_EXCEPTION,
                                exception.getMessage())
                );
    }

    /**
     * Handles verify exception.
     * @param exception the exception.
     */
    @ExceptionHandler(value = VerifyException.class)
    public ResponseEntity<?> handleVerifyException(VerifyException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorContext(
                                InfExcMessages.VERIFY_EXCEPTION,
                                exception.getMessage())
                );
    }

    /**
     * Handles discord slash interaction exception.
     * @param exception the exception.
     */
    @ExceptionHandler(DiscordSlashInteractionException.class)
    public ResponseEntity<?> handleDiscordSlashInteractionException(DiscordSlashInteractionException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorContext(
                                InfExcMessages.DISCORD_SLASH_INTERACTION_EXCEPTION,
                                exception.getMessage()
                        )
                );
    }

    /**
     * Handles telegram slash interaction exception.
     * @param exception the exception.
     */
    @ExceptionHandler(TelegramSlashInteractionException.class)
    public ResponseEntity<?> handleTelegramSlashInteractionException(TelegramSlashInteractionException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorContext(
                                InfExcMessages.TELEGRAM_SLASH_INTERACTION_EXCEPTION,
                                exception.getMessage()
                        )
                );
    }
    /**
     * Handles command exception.
     * @param exception the exception.
     */
    @ExceptionHandler(value = CommandException.class)
    public ResponseEntity<?> handleCommandException(CommandException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorContext(
                                InfExcMessages.COMMAND_EXCEPTION,
                                exception.getMessage()
                        )
                );
    }

    /**
     * Handles delete message exception.
     * @param exception the exception.
     */
    @ExceptionHandler(value = DeleteMessageException.class)
    public ResponseEntity<?> handleDeleteMessageException(DeleteMessageException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorContext(
                                InfExcMessages.DELETE_MESSAGE_EXCEPTION,
                                exception.getMessage()
                        )
                );
    }
    /**
     * Handles consuming exception.
     * @param exception the exception.
     */
    @ExceptionHandler(value = ConsumingException.class)
    public ResponseEntity<?> handleConsumingException(ConsumingException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorContext(
                                InfExcMessages.CONSUMING_EXCEPTION,
                                exception.getMessage()
                        )
                );
    }

    /**
     * Handles command consuming exception.
     * @param exception the exception.
     */
    @ExceptionHandler(value = CommandConsumingException.class)
    public ResponseEntity<?> handleCommandConsumingException(CommandConsumingException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorContext(
                                InfExcMessages.COMMAND_CONSUMING_EXCEPTION,
                                exception.getMessage()
                        )
                );
    }

    /**
     * Handles message consuming exception.
     * @param exception the exception.
     */
    @ExceptionHandler(value = MessageConsumingException.class)
    public ResponseEntity<?> handleMessageConsumingException(MessageConsumingException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorContext(
                                InfExcMessages.MESSAGE_CONSUMING_EXCEPTION,
                                exception.getMessage()
                        )
                );
    }

    /**
     * Handles guild not found exception.
     * @param exception the exception.
     */
    @ExceptionHandler(value = GuildNotFoundException.class)
    public ResponseEntity<?> handleGuildNotFoundException(GuildNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorContext(
                                InfExcMessages.GUILD_NOT_FOUND_EXCEPTION,
                                exception.getMessage()
                        )
                );
    }

    /**
     * Handles no connection between servers exception.
     * @param exception the exception.
     */
    @ExceptionHandler(value = NoConnectionBetweenServersException.class)
    public ResponseEntity<?> handleNoConnectionBetweenServersException(NoConnectionBetweenServersException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorContext(
                                InfExcMessages.NO_CONNECTION_BETWEEN_SERVERS,
                                exception.getMessage()
                        )
                );
    }

    /**
     * Handles text channel not found exception.
     * @param exception the exception.
     */
    @ExceptionHandler(value = TextChannelNotFoundException.class)
    public ResponseEntity<?> handleTextChannelNotFoundException(TextChannelNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorContext(
                                InfExcMessages.TEXT_CHANNEL_NOT_FOUND_EXCEPTION,
                                exception.getMessage()
                        )
                );
    }
}

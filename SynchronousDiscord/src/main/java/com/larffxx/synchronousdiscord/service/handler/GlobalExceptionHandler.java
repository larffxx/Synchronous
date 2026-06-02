package com.larffxx.synchronousdiscord.service.handler;

import com.larffxx.synchronousdiscord.domain.exception.consume.CommandConsumingException;
import com.larffxx.synchronousdiscord.domain.exception.consume.ConsumingException;
import com.larffxx.synchronousdiscord.domain.exception.consume.MessageConsumingException;
import com.larffxx.synchronousdiscord.domain.exception.service.GuildNotFoundException;
import com.larffxx.synchronousdiscord.domain.exception.service.NoConnectionBetweenServersException;
import com.larffxx.synchronousdiscord.domain.exception.service.TextChannelNotFoundException;
import com.larffxx.synchronousdiscord.domain.record.ErrorResponse;
import com.larffxx.synchronousdiscord.domain.exception.DiscordSynchronousException;
import com.larffxx.synchronousdiscord.domain.exception.DownloadAttachmentException;
import com.larffxx.synchronousdiscord.domain.exception.RouteServiceException;
import com.larffxx.synchronousdiscord.domain.exception.VerifyException;
import com.larffxx.synchronousdiscord.domain.exception.command.CommandException;
import com.larffxx.synchronousdiscord.domain.exception.command.DeleteMessageException;
import com.larffxx.synchronousdiscord.domain.exception.command.ShuffleException;
import com.larffxx.synchronousdiscord.domain.exception.interaction.DiscordSlashInteractionException;
import com.larffxx.synchronousdiscord.domain.exception.interaction.TelegramSlashInteractionException;
import com.larffxx.synchronousdiscord.domain.constants.infexc.InfExcMessages;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = DiscordSynchronousException.class)
    public ResponseEntity<?> handleGeneric(DiscordSynchronousException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorResponse(
                                InfExcMessages.SYNCHRONOUS_DISCORD_EXCEPTION,
                                exception.getMessage())
                );
    }

    @ExceptionHandler(value = DownloadAttachmentException.class)
    public ResponseEntity<?> handleDownloadAttachmentException(DownloadAttachmentException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorResponse(
                                InfExcMessages.DOWNLOAD_ATTACHMENT_EXCEPTION,
                                exception.getMessage())
                );
    }

    @ExceptionHandler(value = RouteServiceException.class)
    public ResponseEntity<?> handleRouteServiceException(RouteServiceException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorResponse(InfExcMessages.ROUTE_SERVICE_EXCEPTION,
                                exception.getMessage())
                );
    }

    @ExceptionHandler(value = VerifyException.class)
    public ResponseEntity<?> handleVerifyException(VerifyException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorResponse(
                                InfExcMessages.VERIFY_EXCEPTION,
                                exception.getMessage())
                );
    }

    @ExceptionHandler(DiscordSlashInteractionException.class)
    public ResponseEntity<?> handleDiscordSlashInteractionException(DiscordSlashInteractionException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorResponse(
                                InfExcMessages.DISCORD_SLASH_INTERACTION_EXCEPTION,
                                exception.getMessage()
                        )
                );
    }

    @ExceptionHandler(TelegramSlashInteractionException.class)
    public ResponseEntity<?> handleTelegramSlashInteractionException(TelegramSlashInteractionException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorResponse(
                                InfExcMessages.TELEGRAM_SLASH_INTERACTION_EXCEPTION,
                                exception.getMessage()
                        )
                );
    }
    @ExceptionHandler(value = CommandException.class)
    public ResponseEntity<?> handleCommandException(CommandException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorResponse(
                                InfExcMessages.COMMAND_EXCEPTION,
                                exception.getMessage()
                        )
                );
    }

    @ExceptionHandler(value = DeleteMessageException.class)
    public ResponseEntity<?> handleDeleteMessageException(DeleteMessageException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorResponse(
                                InfExcMessages.DELETE_MESSAGE_EXCEPTION,
                                exception.getMessage()
                        )
                );
    }
    @ExceptionHandler(value = ShuffleException.class)
    public ResponseEntity<?> handleShuffleException(ShuffleException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorResponse(
                                InfExcMessages.SHUFFLE_EXCEPTION,
                                exception.getMessage()
                        )
                );
    }
    @ExceptionHandler(value = ConsumingException.class)
    public ResponseEntity<?> handleConsumingException(ConsumingException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorResponse(
                                InfExcMessages.CONSUMING_EXCEPTION,
                                exception.getMessage()
                        )
                );
    }

    @ExceptionHandler(value = CommandConsumingException.class)
    public ResponseEntity<?> handleCommandConsumingException(CommandConsumingException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorResponse(
                                InfExcMessages.COMMAND_CONSUMING_EXCEPTION,
                                exception.getMessage()
                        )
                );
    }

    @ExceptionHandler(value = MessageConsumingException.class)
    public ResponseEntity<?> handleMessageConsumingException(MessageConsumingException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorResponse(
                                InfExcMessages.MESSAGE_CONSUMING_EXCEPTION,
                                exception.getMessage()
                        )
                );
    }

    @ExceptionHandler(value = GuildNotFoundException.class)
    public ResponseEntity<?> handleGuildNotFoundException(GuildNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorResponse(
                                InfExcMessages.GUILD_NOT_FOUND_EXCEPTION,
                                exception.getMessage()
                        )
                );
    }

    @ExceptionHandler(value = NoConnectionBetweenServersException.class)
    public ResponseEntity<?> handleNoConnectionBetweenServersException(NoConnectionBetweenServersException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorResponse(
                                InfExcMessages.NO_CONNECTION_BETWEEN_SERVERS,
                                exception.getMessage()
                        )
                );
    }

    @ExceptionHandler(value = TextChannelNotFoundException.class)
    public ResponseEntity<?> handleTextChannelNotFoundException(TextChannelNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorResponse(
                                InfExcMessages.TEXT_CHANNEL_NOT_FOUND_EXCEPTION,
                                exception.getMessage()
                        )
                );
    }
}

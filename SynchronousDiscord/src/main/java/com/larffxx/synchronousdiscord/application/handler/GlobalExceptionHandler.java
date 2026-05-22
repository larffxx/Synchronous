package com.larffxx.synchronousdiscord.application.handler;

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
import com.larffxx.synchronousdiscord.infexc.InfExcMessages;
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
}

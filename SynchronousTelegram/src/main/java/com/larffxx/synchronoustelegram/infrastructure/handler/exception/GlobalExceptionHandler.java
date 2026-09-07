package com.larffxx.synchronoustelegram.infrastructure.handler.exception;

import com.larffxx.synchronoustelegram.domain.constant.infexc.InfExcMessage;
import com.larffxx.synchronoustelegram.domain.exception.TelegramException;
import com.larffxx.synchronoustelegram.domain.exception.command.CommandException;
import com.larffxx.synchronoustelegram.domain.exception.command.ConnectCommandException;
import com.larffxx.synchronoustelegram.domain.exception.data.DataException;
import com.larffxx.synchronoustelegram.domain.exception.execution.ExecutionException;
import com.larffxx.synchronoustelegram.domain.record.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global REST exception handler for the Telegram module.
 * Translates domain exceptions into error responses with HTTP 500 status.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Handles generic Telegram failures.
     * @param e the thrown exception
     * @return error response with HTTP 500 status
     */
    @ExceptionHandler
    public ResponseEntity<?> handleGenericException(TelegramException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        InfExcMessage.TELEGRAM_EXCEPTION, e.getMessage())
                );
    }

    /**
     * Handles command parsing and handling failures.
     * @param e the thrown exception
     * @return error response with HTTP 500 status
     */
    @ExceptionHandler
    public ResponseEntity<?> handleCommandException(CommandException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        InfExcMessage.COMMAND_EXCEPTION, e.getMessage())
                );
    }

    /**
     * Handles data loading and conversion failures.
     * @param e the thrown exception
     * @return error response with HTTP 500 status
     */
    @ExceptionHandler
    public ResponseEntity<?> handleDataException(DataException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        InfExcMessage.DATA_EXCEPTION, e.getMessage())
                );
    }

    /**
     * Handles failures while executing Telegram operations.
     * @param e the thrown exception
     * @return error response with HTTP 500 status
     */
    @ExceptionHandler
    public ResponseEntity<?> handleExecutionException(ExecutionException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        InfExcMessage.EXECUTION_EXCEPTION, e.getMessage())
                );
    }
}

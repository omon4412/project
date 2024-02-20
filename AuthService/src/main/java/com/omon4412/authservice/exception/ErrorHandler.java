package com.omon4412.authservice.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;

/**
 * Обработчик всех исключений.
 */
@RestControllerAdvice
public class ErrorHandler {

    /**
     * Обработчик исключения {@link NotFoundException}.
     * Возникает, когда искомый объект не найден.
     *
     * @param ex Исключение {@link NotFoundException}
     * @return Объект {@link ApiError} с информацией об ошибках
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException ex) {
        return ApiError.builder()
                .message(ex.getMessage())
                .reason("Запрашиваемый объект не найден")
                .status(HttpStatus.NOT_FOUND.name())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNoResourceFoundException(final NoResourceFoundException ex) {
        return ApiError.builder()
                .message(ex.getMessage())
                .reason("Запрашиваемый объект не найден")
                .status(HttpStatus.NOT_FOUND.name())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Обработчик исключения {@link BadRequestException}.
     * Возникает, когда приходят неверные данные.
     *
     * @param ex Исключение {@link BadRequestException}
     * @return Объект {@link ApiError} с информацией об ошибках
     */
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(final BadRequestException ex) {
        return ApiError.builder()
                .message(ex.getMessage())
                .reason("Неверные данные")
                .status(HttpStatus.BAD_REQUEST.name())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Обработчик исключения {@link ConstraintViolationException}.
     * Возникает, когда действие нарушает ограничение на структуру модели.
     *
     * @param ex Исключение {@link ConstraintViolationException}
     * @return Объект {@link ApiError} с информацией об ошибках
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError onConstraintValidationException(ConstraintViolationException ex) {
        return ApiError.builder()
                .message(ex.getMessage())
                .reason("Нарушение ограничений на структуру модели")
                .status(HttpStatus.BAD_REQUEST.name())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Обработчик исключения {@link MethodArgumentNotValidException}.
     * Возникает когда проверка аргумента с аннотацией @Valid не удалась
     *
     * @param ex Исключение {@link MethodArgumentNotValidException}
     * @return Объект {@link ApiError} с информацией об ошибках
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError onMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ApiError.builder()
                .message(ex.getMessage())
                .reason("Нарушение ограничений на структуру модели")
                .status(HttpStatus.BAD_REQUEST.name())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Обработчик исключения {@link HttpRequestMethodNotSupportedException}.
     * Возникает когда обработчик запросов не поддерживает определенный метод запроса
     *
     * @param ex Исключение {@link HttpRequestMethodNotSupportedException}
     * @return Объект {@link ApiError} с информацией об ошибке
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ApiError handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException ex) {
        return ApiError.builder()
                .message(ex.getMessage())
                .reason("Method not supported")
                .status(HttpStatus.METHOD_NOT_ALLOWED.name())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError handleBadCredentialsExceptionException(final BadCredentialsException ex) {
        return ApiError.builder()
                .message(ex.getMessage())
                .reason("Неправильный логин или пароль")
                .status(HttpStatus.UNAUTHORIZED.name())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError handleUnauthorizedException(final UnauthorizedException ex) {
        return ApiError.builder()
                .message(ex.getMessage())
                .reason("Authorization failed")
                .status(HttpStatus.UNAUTHORIZED.toString())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleHttpMessageNotReadableException(final HttpMessageNotReadableException ex) {
        return ApiError.builder()
                .message(ex.getMessage())
                .reason("Required request body is wrong")
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Обработчик исключения {@link DataIntegrityViolationException}.
     * Возникает, когда нарушается целостность данных (например, уникальное ограничение).
     *
     * @param ex Исключение {@link DataIntegrityViolationException}
     * @return Объект {@link ApiError} с информацией об ошибке
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(final DataIntegrityViolationException ex) {
        return ApiError.builder()
                .message(ex.getMessage())
                .reason("Ошибка целостности данных")
                .status(HttpStatus.CONFLICT.toString())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Обработчик исключения {@link MissingServletRequestParameterException}.
     * Возникает, когда отсутствует обязательный параметр.
     *
     * @param ex Исключение {@link MissingServletRequestParameterException}
     * @return Объект {@link ApiError} с информацией об ошибке
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMissingParams(MissingServletRequestParameterException ex) {
        return ApiError.builder()
                .message("Отсутствует обязательный параметр -" + ex.getParameterName())
                .reason("Отсутствует обязательный параметр")
                .status(HttpStatus.CONFLICT.toString())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(KafkaMessageException.class)
    @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
    public ApiError handleKafkaMessageException(KafkaMessageException ex) {
        return ApiError.builder()
                .message(ex.getMessage())
                .reason("Ошибка сервера, повторите ещё раз")
                .status(HttpStatus.GATEWAY_TIMEOUT.toString())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Обработчик всевозможных исключений во время работы программы.
     *
     * @param ex Исключение {@link Throwable}
     * @return Объект {@link ApiError} с информацией об ошибке
     */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleThrowable(final Throwable ex) {
        ex.printStackTrace();
        return ApiError.builder()
                .message(ex.getMessage())
                .reason("Внутрення ошибка сервера")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .timestamp(LocalDateTime.now())
                .build();
    }
}

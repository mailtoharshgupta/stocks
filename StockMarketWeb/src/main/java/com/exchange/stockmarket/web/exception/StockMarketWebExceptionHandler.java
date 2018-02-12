package com.exchange.stockmarket.web.exception;

import com.exchange.stockmarket.base.exception.InvalidParamException;
import com.exchange.stockmarket.base.exception.ResourceNotFoundException;
import com.exchange.stockmarket.base.model.ErrorResponse;
import com.exchange.stockmarket.base.model.ValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Harsh Gupta on {2/10/18}
 */

@ControllerAdvice
public class StockMarketWebExceptionHandler {

    /**
     * Handler for the request that fail due to @Validated annotation
     *
     * @param exception
     * @return {@link ErrorResponse}
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArguementsNotValidException(MethodArgumentNotValidException exception) {

        // Converting field errors to in house validation error objects
        Function<FieldError, ValidationError> convertToValidationError = (fe) -> new ValidationError(fe.getField(), fe.getDefaultMessage());

        List<ValidationError> validationErrors = exception.getBindingResult().getFieldErrors().stream()
                .map(convertToValidationError)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .validationErrors(validationErrors)
                        .build());
    }

    /**
     * Handler for requests that fail due to invalid resource access.
     *
     * @param ex
     * @return {@link ErrorResponse}
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message(StringUtils.isEmpty(ex.getMessage()) ? HttpStatus.NOT_FOUND.getReasonPhrase() : ex.getMessage())
                        .build());
    }

    /**
     * Handler for requests that fail due to invalid custom request params.
     *
     * @param ex
     * @return {@link ErrorResponse}
     */
    @ExceptionHandler(InvalidParamException.class)
    public ResponseEntity<ErrorResponse> handleInvalidParamException(InvalidParamException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(StringUtils.isEmpty(ex.getMessage()) ? HttpStatus.BAD_REQUEST.getReasonPhrase() : ex.getMessage())
                        .build());
    }

    /**
     * Handler for all internal server errors.
     *
     * @param ex
     * @return {@link ErrorResponse}
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> handleInternalServerError(Throwable ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(StringUtils.isEmpty(ex.getMessage()) ? HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase() : ex.getMessage())
                        .build());
    }

}

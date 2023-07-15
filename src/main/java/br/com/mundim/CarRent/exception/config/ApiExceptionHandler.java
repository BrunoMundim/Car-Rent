package br.com.mundim.CarRent.exception.config;

import br.com.mundim.CarRent.exception.BadRequestException;
import br.com.mundim.CarRent.exception.NullFieldException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {NullFieldException.class})
    public ResponseEntity<Object> handleNullFieldException(NullFieldException e){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                e.getMessage(),
                badRequest,
                new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime())
        );

        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<Object> handleBadRequestException(BadRequestException e){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime())
        );

        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiException> handleValidationException(ConstraintViolationException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        String errorMessage = e.getConstraintViolations()
                .stream()
                .map(cv -> cv.getMessage())
                .findFirst()
                .orElse("Erro de validação do campo 'name'");

        ApiException apiException = new ApiException(
                errorMessage,
                HttpStatus.BAD_REQUEST,
                new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime())
        );

        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiException> handlePSQLException(DataIntegrityViolationException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                "Email already in use.",
                HttpStatus.BAD_REQUEST,
                new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime())
        );

        return new ResponseEntity<>(apiException, badRequest);
    }

}

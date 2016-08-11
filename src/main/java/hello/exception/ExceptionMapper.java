package hello.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.CompletionException;

/**
 * Created by pavel on 09.08.16.
 */
@ControllerAdvice
public class ExceptionMapper {

    @ExceptionHandler(Exception.class)
    public
    @ResponseBody
    ResponseEntity<ErrorMessage> handle(Exception exception) {
        if (exception instanceof CompletionException) {
            return handleInternally(exception.getCause());
        } else {
            return handleInternally(exception);
        }
    }

    private ResponseEntity<ErrorMessage> handleInternally(Throwable throwable) {
        if (throwable instanceof ResourceNotFoundException) {
            return new ResponseEntity<>(new ErrorMessage(204, "Resource not found", throwable.getLocalizedMessage()), HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(new ErrorMessage(500, "Internal error", throwable.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Created by pavel on 09.08.16.
     */
    public static class ErrorMessage {

        private int code;

        private String message;

        private String cause;

        public ErrorMessage() {
        }

        public ErrorMessage(int code, String message, String cause) {
            this.code = code;
            this.message = message;
            this.cause = cause;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getCause() {
            return cause;
        }

        public void setCause(String cause) {
            this.cause = cause;
        }
    }
}

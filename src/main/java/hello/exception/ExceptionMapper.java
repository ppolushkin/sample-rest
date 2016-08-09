package hello.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by pavel on 09.08.16.
 */
@ControllerAdvice
public class ExceptionMapper {

    @ExceptionHandler(Exception.class)
    public
    @ResponseBody
    ResponseEntity<ErrorMessage> handle(Exception exception) {
        if (exception instanceof ResourceNotFoundException) {
            return new ResponseEntity<>(new ErrorMessage(204, "Resource not found "), HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(new ErrorMessage(500, "Internal error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Created by pavel on 09.08.16.
     */
    public static class ErrorMessage {

        private int code;
        private String message;

        public ErrorMessage() {
        }

        public ErrorMessage(int code, String message) {
            this.code = code;
            this.message = message;
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

    }
}

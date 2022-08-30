package qwerty268.ShareIt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice("qwerty268.ShareIt")
public class ExceptionController {


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidArgsException(final InvalidArgsException e) {
        return e.toString();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,String> handelIllegalStateException(final IllegalStateException e) {
        return Map.of("Error message", String.format(e.getMessage()));
    }
}

//@ExceptionHandler
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public String handleFilmDoesNotExistException(final FilmDoesNotExistException e) {
//        return e.getMessage();
//    }

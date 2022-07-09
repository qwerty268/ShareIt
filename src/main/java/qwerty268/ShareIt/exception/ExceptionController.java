package qwerty268.ShareIt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("qwerty268.ShareIt")
public class ExceptionController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleUserAlreadyExistException(final UserAlreadyExistException e) {
        return e.toString();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidArgsException(final InvalidArgsException e) {
        return e.toString();
    }

}

//@ExceptionHandler
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public String handleFilmDoesNotExistException(final FilmDoesNotExistException e) {
//        return e.getMessage();
//    }
package qwerty268.ShareIt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import qwerty268.ShareIt.item.exceptions.InvalidOwnerOfItemException;
import qwerty268.ShareIt.item.exceptions.ItemNotFoundException;
import qwerty268.ShareIt.user.exceptions.UserAlreadyExistException;
import qwerty268.ShareIt.user.exceptions.UserDoesNotExistException;

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

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUserDoesNotExistException(final UserDoesNotExistException e) {
        return e.toString();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleInvalidOwnerOfItemException(final InvalidOwnerOfItemException e) {
        return e.toString();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleItemNotFoundException(final ItemNotFoundException e) {
        return e.toString();
    }
}

//@ExceptionHandler
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public String handleFilmDoesNotExistException(final FilmDoesNotExistException e) {
//        return e.getMessage();
//    }
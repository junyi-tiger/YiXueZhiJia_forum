package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class NotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(NotFoundResourceException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String NotFoundHandler(NotFoundResourceException ex){
        return ex.getMessage();
    }

}

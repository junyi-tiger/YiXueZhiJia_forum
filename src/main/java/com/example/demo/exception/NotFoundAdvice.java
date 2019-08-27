package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @ControllerAdvice 异常出错后提供修复建议
 */
@ControllerAdvice
public class NotFoundAdvice {

    /**
     * @ReponseBody signals that this advice is rendered straight into the response body.
     * @ExceptionHandler configures the advice to only respond if an NotFoundResourceException is thrown.
     * @ResponseStatus syas to issue an HttpStatus.NOT_FOUND, i,e. an HTTP 404.
     * the body of the advice generates the content. In this case, it gives the message of the exception.
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(NotFoundResourceException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String NotFoundHandler(NotFoundResourceException ex){
        return ex.getMessage();
    }

}

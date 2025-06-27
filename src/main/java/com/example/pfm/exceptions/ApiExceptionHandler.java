package com.example.pfm.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ApiExceptionHandler {

    record Err(String message) {}

    @ExceptionHandler(BadRequestException.class)   @ResponseStatus(HttpStatus.BAD_REQUEST)      Err e1(BadRequestException e){return new Err(e.getMessage());}
    @ExceptionHandler(ConflictException.class)     @ResponseStatus(HttpStatus.CONFLICT)         Err e2(ConflictException e){return new Err(e.getMessage());}
    @ExceptionHandler(NotFoundException.class)     @ResponseStatus(HttpStatus.NOT_FOUND)        Err e3(NotFoundException e){return new Err(e.getMessage());}
    @ExceptionHandler(ForbiddenException.class)    @ResponseStatus(HttpStatus.FORBIDDEN)        Err e4(ForbiddenException e){return new Err(e.getMessage());}
}

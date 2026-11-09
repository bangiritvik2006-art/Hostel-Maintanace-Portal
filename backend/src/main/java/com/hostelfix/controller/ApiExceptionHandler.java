package com.hostelfix.controller;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestControllerAdvice
public class ApiExceptionHandler {
  @ExceptionHandler(NoSuchElementException.class) @ResponseStatus(HttpStatus.NOT_FOUND)
  public Map<String,String> notFound(Exception e){return Map.of("error",e.getMessage());}
  @ExceptionHandler(MethodArgumentNotValidException.class) @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String,String> invalid(MethodArgumentNotValidException e){return Map.of("error","Please provide all required fields with valid values.");}
  @ExceptionHandler(IllegalArgumentException.class) @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String,String> bad(Exception e){return Map.of("error",e.getMessage());}
}

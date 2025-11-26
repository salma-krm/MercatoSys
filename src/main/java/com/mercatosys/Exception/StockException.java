package com.mercatosys.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StockException extends RuntimeException {
    public StockException(String message) {
        super(message);
    }
}

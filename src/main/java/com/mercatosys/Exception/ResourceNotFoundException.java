package com.mercatosys.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private static final String resourceId = null;

    public ResourceNotFoundException(String resourceName) {
        super(resourceName + " introuvable avec l’identifiant : " + resourceId);
    }
}

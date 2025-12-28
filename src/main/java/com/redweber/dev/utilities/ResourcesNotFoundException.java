package com.redweber.dev.utilities;
import org.springframework.stereotype.Component;

@Component
public class ResourcesNotFoundException extends RuntimeException {
    public ResourcesNotFoundException(){}

    public ResourcesNotFoundException(String message){
        super(message);
    }
}

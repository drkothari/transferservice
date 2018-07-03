package com.ingenico.transferservice.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages="com.ingenico.transferservice")
public class TransferExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(TransferExceptionHandler.class);

    @ExceptionHandler(TransferServiceException.class)
    public ResponseEntity processApiError(final TransferServiceException tsexception) {
        logger.error("APIException ::"+ tsexception.getMessage());
        return new ResponseEntity(tsexception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}

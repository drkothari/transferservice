package com.ingenico.transferservice.context;

/**
 * TransferServiceException Custom/Functional exception
 */
public class TransferServiceException extends Exception {

    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

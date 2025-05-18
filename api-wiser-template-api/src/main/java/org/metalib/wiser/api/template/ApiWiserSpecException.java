package org.metalib.wiser.api.template;

public class ApiWiserSpecException extends RuntimeException {
    public ApiWiserSpecException(String message) { super(message); }
    public ApiWiserSpecException(String message, Exception e) { super(message, e); }
}

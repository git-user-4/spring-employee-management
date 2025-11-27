package com.tu.course.employee_management.exception;

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

public class CloudinaryException extends RuntimeException implements ErrorResponse {

    private final ProblemDetail body;
    private final HttpStatus status;

    public enum Operation {UPLOAD, DELETE}

    public CloudinaryException(Operation operation, Throwable cause) {
        super("Cloudinary file " + operation + " failed");
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, getMessage());
        problemDetail.setTitle("Cloudinary Operation Failed");
        problemDetail.setProperty("operation", operation);
        problemDetail.setProperty("cause", cause.getMessage());
        this.body = problemDetail;
    }

    @Override
    @NonNull
    public HttpStatusCode getStatusCode() {
        return status;
    }

    @Override
    @NonNull
    public ProblemDetail getBody() {
        return body;
    }

}

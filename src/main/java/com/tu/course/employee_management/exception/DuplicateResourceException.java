package com.tu.course.employee_management.exception;

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

public class DuplicateResourceException extends RuntimeException implements ErrorResponse {

    private final ProblemDetail body;
    private final HttpStatus status;

    public DuplicateResourceException(Class<?> resource, String fieldName, Object value) {
        super(extractResourceName(resource) + " with " + fieldName + "=" + value + " already exists");
        this.status = HttpStatus.CONFLICT; // 409

        ProblemDetail pd = ProblemDetail.forStatus(status);
        pd.setTitle("Duplicate Resource");
        pd.setDetail(getMessage());
        pd.setProperty("resource", extractResourceName(resource));
        pd.setProperty("field", fieldName);
        pd.setProperty("value", value);

        this.body = pd;
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

    private static String extractResourceName(Class<?> resource) {
        return resource == null ? "Resource" : resource.getSimpleName();
    }
}

package com.tu.course.employee_management.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Catches the Custom Not Found Exception
    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(ResourceNotFoundException ex,
                                        HttpServletRequest req) {
        ProblemDetail pd = ex.getBody();
        pd.setInstance(URI.create(req.getRequestURI()));
        return pd;
    }

    // Catches the Custom Cloudinary Exception
    @ExceptionHandler(CloudinaryException.class)
    public ProblemDetail handleCloudinary(CloudinaryException ex,
                                          HttpServletRequest req){
        ProblemDetail problemDetail = ex.getBody();
        problemDetail.setInstance(URI.create(req.getRequestURI()));
        return problemDetail;
    }

    // Catches the Custom Duplicate Resource Exception
    @ExceptionHandler(DuplicateResourceException.class)
    public ProblemDetail handleDuplicateResource(DuplicateResourceException ex,
                                                 HttpServletRequest req) {

        ProblemDetail pd = ex.getBody();
        pd.setInstance(URI.create(req.getRequestURI()));
        return pd;
    }

    // Catches DB Constraint Violations
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException ex,
                                                      HttpServletRequest req) {

        ProblemDetail pd = ProblemDetail.forStatus(409);
        pd.setTitle("Data Integrity Violation");
        pd.setDetail("Data key constraint violated");
        pd.setInstance(URI.create(req.getRequestURI()));
        pd.setProperty("reason", ex.getMostSpecificCause().getMessage());

        return pd;
    }

    // For @Valid (@RequestBody requestDTO)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleRequestObjectValidation(MethodArgumentNotValidException ex,
                                                       HttpServletRequest req) {

        ProblemDetail pd = ProblemDetail.forStatus(400);
        pd.setTitle("Validation Failed");
        pd.setDetail("One or more fields are invalid");
        pd.setInstance(URI.create(req.getRequestURI()));

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        pd.setProperty("errors", errors);
        return pd;
    }

    // For @Validate (@PathVariable and @RequestParam)
    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleRequestVariableValidation(ConstraintViolationException ex,
                                                         HttpServletRequest req) {

        ProblemDetail pd = ProblemDetail.forStatus(400);
        pd.setTitle("Input Constraint Violation");
        pd.setDetail("Invalid request parameters");
        pd.setInstance(URI.create(req.getRequestURI()));

        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(
                v -> errors.put(v.getPropertyPath().toString(), v.getMessage())
        );

        pd.setProperty("errors", errors);
        return pd;
    }

    // Catches Illegal State Violations
    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleIllegalState(IllegalStateException ex,
                                            HttpServletRequest req) {

        ProblemDetail pd = ProblemDetail.forStatus(403);
        pd.setTitle("Operation Not Allowed");
        pd.setDetail(ex.getMessage());
        pd.setInstance(URI.create(req.getRequestURI()));

        return pd;
    }

}

package csd.backend.Account.MS.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.*;

public class ResponseUtil {

    // Helper method to create a success response with a message
    public static ResponseEntity<Map<String, Object>> createSuccessResponse(String message) {
        Map<String, Object> response = Map.of("message", message);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Helper method to create a success response with a message + Data Object
    public static ResponseEntity<Map<String, Object>> createSuccessResponse(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        if (data != null) response.put("data", data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Helper method to create an Internal Server Error response with a message
    public static ResponseEntity<Map<String, Object>> createInternalServerErrResponse(String message) {
        Map<String, Object> response = Map.of("message", message);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Helper method to create a Bad Request response with a message (400)
    public static ResponseEntity<Map<String, Object>> createBadRequestResponse(String message) {
        Map<String, Object> response = Map.of("message", message);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Helper method to create a Forbidden response with a message (403)
    public static ResponseEntity<Map<String, Object>> createForbiddenResponse(String message) {
        Map<String, Object> response = Map.of("message", message);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    // Helper method to create a Not Found response with a message (404)
    public static ResponseEntity<Map<String, Object>> createNotFoundResponse(String message) {
        Map<String, Object> response = Map.of("message", message);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Helper method to create a Request Timeout response with a message (408)
    public static ResponseEntity<Map<String, Object>> createRequestTimeoutResponse(String message) {
        Map<String, Object> response = Map.of("message", message);
        return new ResponseEntity<>(response, HttpStatus.REQUEST_TIMEOUT);
    }

    // Helper method for an empty OK response (no content)
    public static ResponseEntity<Map<String, Object>> createEmptyOkResponse() {
        return new ResponseEntity<>(Collections.emptyMap(), HttpStatus.OK);
    }
}

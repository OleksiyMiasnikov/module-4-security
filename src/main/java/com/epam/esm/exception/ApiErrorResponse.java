package com.epam.esm.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ApiErrorResponse {

    private String errorMessage;
    private String errorCode;
    private HttpStatusCode statusCode;

    public void send(HttpServletResponse response) throws IOException {

        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(this.statusCode.value());

        Map<String, String> error = new LinkedHashMap<>();
        error.put("message", this.errorMessage);
        error.put("errorCode", this.errorCode);

        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }
}

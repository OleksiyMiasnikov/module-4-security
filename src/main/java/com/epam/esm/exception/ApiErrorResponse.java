package com.epam.esm.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import org.springframework.http.HttpStatusCode;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorResponse {

    private String message;
    private String errorCode;
    @JsonIgnore
    private HttpStatusCode status;

    public void send(HttpServletResponse response) throws IOException {

        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(this.status.value());

        Map<String, String> error = new LinkedHashMap<>();
        error.put("message", this.message);
        error.put("errorCode", this.errorCode);

        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }
}

package com.gopal.cab_booking.security;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

final class JsonErrorWriter {

    private JsonErrorWriter() {
    }

    static void write(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String safeMessage = message.replace("\"", "'");
        String json = "{\"success\":false,"
                + "\"message\":\"" + safeMessage + "\","
                + "\"data\":null,"
                + "\"errors\":null,"
                + "\"timestamp\":\"" + LocalDateTime.now() + "\"}";
        response.getWriter().write(json);
    }
}
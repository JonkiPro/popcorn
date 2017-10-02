package com.web.web.security.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.common.dto.error.ErrorMessageDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Utility class for returning messages.
 */
public class SecurityUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Returns the error message to the customer.
     */
    public static void sendError(HttpServletResponse response, int status, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(status);
        PrintWriter writer = response.getWriter();
        writer.write(mapper.writeValueAsString(new ErrorMessageDTO(status, status, message)));
        writer.flush();
        writer.close();
    }
}

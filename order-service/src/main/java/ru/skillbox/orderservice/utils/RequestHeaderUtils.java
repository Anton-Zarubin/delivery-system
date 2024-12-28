package ru.skillbox.orderservice.utils;

import lombok.experimental.UtilityClass;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@UtilityClass
public class RequestHeaderUtils {

    public Long getActiveUserId(HttpServletRequest request) {
        return Long.valueOf(request.getHeader("id"));
    }

    public boolean isAdmin(HttpServletRequest request) {
        String rolesHeader = request.getHeader("roles");
        return (rolesHeader != null) && Arrays.asList(rolesHeader.split(", ")).contains("ROLE_ADMIN");
    }
}

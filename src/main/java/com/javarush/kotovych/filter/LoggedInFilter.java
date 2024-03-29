package com.javarush.kotovych.filter;


import com.javarush.kotovych.constants.Constants;
import com.javarush.kotovych.service.UserService;
import com.javarush.kotovych.util.FilterUrlChecker;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.servlet.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
@WebFilter(urlPatterns = "/*")
@Slf4j
public class LoggedInFilter implements Filter {

    @Autowired
    UserService userService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        String uri = req.getRequestURI();
        log.info("{} request to {}", req.getMethod(), uri);

        if (FilterUrlChecker.isAllowed(uri)) {

            Cookie[] cookies = req.getCookies();
            String cookieName = Constants.ID;

            Optional<String> idCookieValue = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals(cookieName))
                    .map(Cookie::getValue)
                    .findFirst();

            String id = idCookieValue.orElse(Constants.DEFAULT_ID);

            if (!userService.checkIfExists(id)) {
                resp.sendRedirect("/");
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}

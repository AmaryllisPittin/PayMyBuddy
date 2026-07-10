package com.PayMyBuddy.pay_my_buddy.ConfigTests;

import com.PayMyBuddy.pay_my_buddy.Config.LoggingFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@ExtendWith(MockitoExtension.class)
public class LoggingFilterTests {

    private LoggingFilter loggingFilter;

    @BeforeEach
    void setUp() {

        loggingFilter = new LoggingFilter();

    }

    @Test
    void doFilter_shouldCallFilterChainAndCopyResponseBody() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/transfert");

        request.setCharacterEncoding("UTF-8");
        request.setContentType("application/json");
        request.setContent("""
                    {
                        "amount": 50
                    }
                """.getBytes(StandardCharsets.UTF_8));

        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain filterChain = (servletRequest, servletResponse) -> {
            HttpServletRequest wrappedRequest = (HttpServletRequest) servletRequest;

            HttpServletResponse wrappedResponse = (HttpServletResponse) servletResponse;

            // Lecture obligatoire pour que ContentCatchingRequestWrapper mette le contenu
            // en cache
            wrappedRequest.getInputStream().readAllBytes();

            wrappedResponse.setStatus(HttpServletResponse.SC_OK);
            wrappedResponse.setCharacterEncoding("UTF-8");
            wrappedResponse.getWriter().write("Transaction effectuée");

        };

        loggingFilter.doFilter(request, response, filterChain);

        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        assertEquals("Transaction effectuée", response.getContentAsString());

    }

    @Test
    void doFilter_shouldPreserveErrorStatusAndResponseBody() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/transfert");

        request.setCharacterEncoding("UTF-8");

        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain filterChain = (servletRequest, servletResponse) -> {

            HttpServletResponse wrappedResponse = (HttpServletResponse) servletResponse;

            wrappedResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            wrappedResponse.setCharacterEncoding("UTF-8");
            wrappedResponse.getWriter().write("Solde insuffisant.");

        };

        loggingFilter.doFilter(request, response, filterChain);

        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
        assertEquals("Solde insuffisant.", response.getContentAsString());

    }

    @Test
    void doFilter_shouldPropagateExceptionThrownByFilterChain() {

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/transfert");

        request.setCharacterEncoding("UTF-8");

        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain filterChain = (servletRequest, servletResponse) -> {
            throw new ServletException("Erreur simulée");
        };

        ServletException exception = assertThrows(ServletException.class,
                () -> loggingFilter.doFilter(request, response, filterChain));

        assertEquals("Erreur simulée", exception.getMessage());

    }

    @Test
    void doFilter_shouldInvokeFilterChainOnce() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/profile");

        request.setCharacterEncoding("UTF-8");

        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain filterChain = mock(FilterChain.class);

        loggingFilter.doFilter(request, response, filterChain);

        verify(filterChain, times(1))
                .doFilter(any(), any());

    }

}

package com.PayMyBuddy.pay_my_buddy.Config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger requestLogger = LoggerFactory.getLogger(LoggingFilter.class);

    // Recuperation des requêtes HTTP
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Chronometre sur le traitement
        long startTime = System.currentTimeMillis();

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        requestLogger.info("=> {} {}", request.getMethod(), request.getRequestURI());

        try {
            requestLogger.debug("Début traitement {}", request.getRequestURI());

            filterChain.doFilter(wrappedRequest, wrappedResponse); // Requetes récupérées

            requestLogger.debug("Fin du traitement {}", request.getRequestURI());

        } catch (Exception e) {
            requestLogger.error("Erreur lors du traitement de {}", request.getRequestURI(), e); // Signale les erreurs
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - startTime; // Mesure du temps de traitement
            int status = wrappedResponse.getStatus();

            String requestBody = new String(wrappedRequest.getContentAsByteArray(), request.getCharacterEncoding());
            String responseBody = new String(wrappedResponse.getContentAsByteArray(), request.getCharacterEncoding());

            requestLogger.debug("Request body: {}", requestBody);
            requestLogger.debug("Response body: {}", responseBody);

            if (status >= 400) {
                requestLogger.error("<= {} {} -> status {} in {} ms", request.getMethod(), request.getRequestURI(),
                        status, duration);
            } else {
                requestLogger.info("<= {} {} -> status {} in {} ms", request.getMethod(), request.getRequestURI(),
                        status, duration); // Log sur la requete
            }

            wrappedResponse.copyBodyToResponse();
        }
    }

}

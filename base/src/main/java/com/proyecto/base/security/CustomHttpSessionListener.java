package com.proyecto.base.security;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomHttpSessionListener implements HttpSessionListener {


    @Override
    public void sessionCreated(HttpSessionEvent se) {
        // Log when a new session is created
        log.info("Session created with ID: " + se.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        // Optionally, log when a session is destroyed
        log.info("Session destroyed with ID: " + se.getSession().getId());
    }
}

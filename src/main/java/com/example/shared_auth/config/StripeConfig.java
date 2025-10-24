package com.example.shared_auth.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;

@Configuration
public class StripeConfig {

    private static final Logger log = LoggerFactory.getLogger(StripeConfig.class);

    @Value("${stripe.secret.key:}")
    private String stripeSecretKey;

    @PostConstruct
    public void init() {
        if (stripeSecretKey == null || stripeSecretKey.isBlank()) {
            log.warn("Stripe secret key not provided; Stripe will not be initialized.");
            return;
        }

        try {
            Class<?> stripeClass = Class.forName("com.stripe.Stripe");
            Field apiKeyField = stripeClass.getDeclaredField("apiKey");
            apiKeyField.setAccessible(true);
            apiKeyField.set(null, stripeSecretKey);
            log.info("Stripe SDK found and API key initialized.");
        } catch (ClassNotFoundException e) {
            log.warn("Stripe SDK not found on classpath; skipping Stripe initialization.", e);
        } catch (ReflectiveOperationException e) {
            log.error("Failed to initialize Stripe API key via reflection.", e);
        }
    }
}

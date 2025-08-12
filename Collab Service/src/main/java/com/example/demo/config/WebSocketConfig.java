/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.context.annotation.Configuration
 *  org.springframework.messaging.converter.ContentTypeResolver
 *  org.springframework.messaging.converter.DefaultContentTypeResolver
 *  org.springframework.messaging.converter.MappingJackson2MessageConverter
 *  org.springframework.messaging.converter.MessageConverter
 *  org.springframework.messaging.simp.config.MessageBrokerRegistry
 *  org.springframework.util.MimeTypeUtils
 *  org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
 *  org.springframework.web.socket.config.annotation.StompEndpointRegistry
 *  org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
 */
package com.example.demo.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.ContentTypeResolver;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig
implements WebSocketMessageBrokerConfigurer {
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(new String[]{"/collab/ws"}).setAllowedOriginPatterns(new String[]{"*"}).withSockJS();
        registry.addEndpoint(new String[]{"/collab/ws"}).setAllowedOriginPatterns(new String[]{"*"});
    }

    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker(new String[]{"/collab-topic", "/collab-queue", "/user"});
        config.setApplicationDestinationPrefixes(new String[]{"/collab-app"});
        config.setUserDestinationPrefix("/user");
    }

    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
        resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setContentTypeResolver((ContentTypeResolver)resolver);
        messageConverters.add((MessageConverter)converter);
        return false;
    }
}

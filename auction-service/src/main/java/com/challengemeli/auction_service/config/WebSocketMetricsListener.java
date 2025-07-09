package com.challengemeli.auction_service.config;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.*;

@Slf4j
@Component
public class WebSocketMetricsListener implements ApplicationListener<AbstractSubProtocolEvent> {

    private final MeterRegistry meterRegistry;

    public WebSocketMetricsListener(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void onApplicationEvent(AbstractSubProtocolEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();

        switch (event) {
            case SessionConnectEvent e -> {
                log.info("WebSocket connected: {}", sessionId);
                meterRegistry.counter("websocket.sessions.connected").increment();
            }
            case SessionDisconnectEvent e -> {
                log.info("WebSocket disconnected: {}", sessionId);
                meterRegistry.counter("websocket.sessions.disconnected").increment();
            }
            case SessionSubscribeEvent e -> {
                log.info("WebSocket subscribed: {}", sessionId);
                meterRegistry.counter("websocket.sessions.subscribed").increment();
            }
            case SessionUnsubscribeEvent e -> {
                log.info("WebSocket unsubscribed: {}", sessionId);
                meterRegistry.counter("websocket.sessions.unsubscribed").increment();
            }
            default -> log.debug("Unhandled WebSocket event type: {}", event.getClass().getSimpleName());
        }
    }
}

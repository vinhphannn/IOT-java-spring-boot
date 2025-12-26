package com.phanvanvinh.doan.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Endpoint để Flutter connect vào: ws://localhost:8080/ws
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // Cho phép mọi nguồn kết nối
                .withSockJS(); // Hỗ trợ fallback cho trình duyệt cũ (Flutter vẫn dùng được)
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Tiền tố cho các tin nhắn từ Client gửi lên (nếu có)
        registry.setApplicationDestinationPrefixes("/app");

        // Tiền tố cho các topic mà Client đăng ký lắng nghe (Subscribe)
        // Ví dụ: Client sub vào /topic/room1
        registry.enableSimpleBroker("/topic", "/queue");
    }
}
package com.phanvanvinh.doan.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class MqttConfig {

    @Value("${mqtt.broker.url}")
    private String brokerUrl;

    @Value("${mqtt.client.id}")
    private String clientId;

    @Value("${mqtt.topic.sub}")
    private String defaultTopic;

    // 1. Tạo Factory kết nối
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[] { brokerUrl });
        options.setCleanSession(true);
        // options.setUserName("admin"); // Nếu có user
        // options.setPassword("123456".toCharArray()); // Nếu có pass
        factory.setConnectionOptions(options);
        return factory;
    }

    // --- PHẦN GỬI TIN (OUTBOUND) ---
    // Channel để code Java đẩy tin nhắn vào
    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    // Handler lấy tin từ Channel đẩy lên Broker
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(clientId + "_pub", mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic("home/general");
        return messageHandler;
    }

    // --- PHẦN NHẬN TIN (INBOUND) ---
    // Channel để nhận tin từ Broker về Java
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    // Adapter lắng nghe topic từ Broker
    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientId + "_sub",
                mqttClientFactory(), defaultTopic);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    // Hàm xử lý tin nhắn nhận được (Test logs)
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return message -> {
            String topic = (String) message.getHeaders().get("mqtt_receivedTopic");
            String payload = (String) message.getPayload();
            System.out.println("[MQTT RECEIVED] Topic: " + topic + " | Payload: " + payload);

            // TODO: Tại đây bạn sẽ gọi WebSocketService để bắn tin này về Frontend
        };
    }
}
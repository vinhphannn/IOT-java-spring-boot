package com.phanvanvinh.doan.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "devices")
@Data
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Ví dụ: Đèn trần, Quạt

    // Loại thiết bị: RELAY (bật tắt), SENSOR (cảm biến)
    private String type;

    // Topic MQTT để gửi lệnh xuống (VD: home/room1/light/set)
    private String mqttTopicPub;

    // Topic MQTT để nhận dữ liệu lên (VD: home/room1/sensor)
    private String mqttTopicSub;

    // Trạng thái hiện tại (ON/OFF hoặc giá trị nhiệt độ)
    private String lastValue;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;
}

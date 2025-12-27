package com.phanvanvinh.doan.model;

import com.phanvanvinh.doan.model.DeviceType;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "devices")
@Data
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

<<<<<<< HEAD
    @Column(unique = true, nullable = false)
    private String macAddress; // QUAN TRỌNG: Ví dụ "AA:BB:CC:11:22:33"

    private String name; // Ví dụ: Đèn trần

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeviceType type;
    // Topic để Server gửi lệnh xuống (Subscribe trên ESP)
    // VD: home/device/AA:BB:CC:11:22:33/set
    private String mqttTopicPub;

    // Topic để Server nhận dữ liệu (Publish từ ESP)
    // VD: home/device/AA:BB:CC:11:22:33/status
    private String mqttTopicSub;

    private String lastValue;

    // --- ĐÂY LÀ DÒNG VỢ ĐANG THIẾU ---
    @Column(columnDefinition = "boolean default false")
    private Boolean status = false; // Mặc định là Tắt (false)
    // ---------------------------------

=======
    private String name; // Tên thiết bị (vd: Đèn trần)
    private String type; // Loại: LIGHT, FAN, AC...

    @Column(unique = true)
    private String macAddress; // ĐỊNH DANH DUY NHẤT CỦA ESP32

    private boolean status; // true = ON, false = OFF

    // Liên kết: Nhiều Device thuộc 1 Room
>>>>>>> 316b47d3d0bc6209281e72b4d01ca960fb5aeba0
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    private LocalDateTime createdAt = LocalDateTime.now();
}
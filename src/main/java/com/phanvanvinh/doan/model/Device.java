package com.phanvanvinh.doan.model;

import com.phanvanvinh.doan.model.DeviceType;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "devices")
@Data
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;
}

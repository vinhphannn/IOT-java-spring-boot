package com.phanvanvinh.doan.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "device_logs")
@Data
public class DeviceLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action; // "TURN_ON", "TURN_OFF", "ADJUST_TEMP"

    private String details; // VD: "Bật bằng App", "Tắt bằng hẹn giờ"

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    private LocalDateTime timestamp = LocalDateTime.now();
}
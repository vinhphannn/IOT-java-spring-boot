package com.phanvanvinh.doan.model;

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

    private String name; // Tên thiết bị (vd: Đèn trần)
    private String type; // Loại: LIGHT, FAN, AC...

    @Column(unique = true)
    private String macAddress; // ĐỊNH DANH DUY NHẤT CỦA ESP32

    private boolean status; // true = ON, false = OFF

    // Liên kết: Nhiều Device thuộc 1 Room
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    private LocalDateTime createdAt = LocalDateTime.now();
}
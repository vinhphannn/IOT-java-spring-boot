package com.phanvanvinh.doan.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "rooms")
@Data
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Ví dụ: Phòng khách, Phòng ngủ

    private String imageUrl; // Hình ảnh đại diện phòng

    // Nhiều phòng thuộc về 1 user
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Một phòng có nhiều thiết bị
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Device> devices;
}
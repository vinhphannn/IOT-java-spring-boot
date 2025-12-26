package com.phanvanvinh.doan.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "rooms")
@Data
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Ví dụ: Phòng khách, Phòng ngủ

    private String imageUrl; // Hình ảnh đại diện phòng

    @ManyToOne
    @JoinColumn(name = "house_id")
    @JsonIgnore
    private House house;

    // Một phòng có nhiều thiết bị
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Device> devices;
}
package com.phanvanvinh.doan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
@Table(name = "houses")
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Tên nhà (VD: My Castle)
    private String address; // Địa chỉ
    private String nationality; // Quốc gia

    // Quan hệ: Nhiều nhà thuộc về 1 User
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore // Tránh vòng lặp vô tận khi convert JSON
    private User user;

    // Quan hệ: 1 nhà có nhiều phòng
    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL)
    private List<Room> rooms;
}
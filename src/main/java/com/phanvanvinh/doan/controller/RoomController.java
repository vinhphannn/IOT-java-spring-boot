package com.phanvanvinh.doan.controller;

import com.phanvanvinh.doan.model.Room;
import com.phanvanvinh.doan.model.User;
import com.phanvanvinh.doan.repository.RoomRepository;
import com.phanvanvinh.doan.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Room> getMyRooms() {
        // 1. Lấy email người dùng từ Token đang đăng nhập
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // 2. Tìm User trong DB
        User user = userRepository.findByEmail(email).orElseThrow();

        // 3. Trả về danh sách phòng của User đó
        return roomRepository.findByUserId(user.getId());
    }
}
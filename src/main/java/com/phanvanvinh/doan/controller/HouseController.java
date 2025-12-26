package com.phanvanvinh.doan.controller;

import com.phanvanvinh.doan.model.House;
import com.phanvanvinh.doan.model.User;
import com.phanvanvinh.doan.repository.HouseRepository;
import com.phanvanvinh.doan.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/houses")
public class HouseController {

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<House> getMyHouses() {
        // 1. Lấy email từ Token
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // 2. Tìm User
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3. Trả về danh sách nhà của User này
        return houseRepository.findByUserId(user.getId());
    }
}
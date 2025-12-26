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
import com.phanvanvinh.doan.model.House;
import com.phanvanvinh.doan.repository.HouseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HouseRepository houseRepository;

    @GetMapping
    public List<Room> getMyRooms() {
        // 1. Lấy email người dùng từ Token
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // 2. Tìm User trong DB
        User user = userRepository.findByEmail(email).orElseThrow();

        // 3. SỬA DÒNG NÀY (Dòng 34 cũ)
        // Thay vì findByUserId -> dùng findByHouseUserId
        return roomRepository.findByHouseUserId(user.getId());
    }

    // --- API MỚI: LẤY PHÒNG THEO NHÀ ---
    // URL: /api/rooms/house/1 (Lấy phòng của nhà số 1)
    @GetMapping("/house/{houseId}")
    public ResponseEntity<?> getRoomsByHouseId(@PathVariable Long houseId) {

        // 1. Lấy User hiện tại
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        // 2. Kiểm tra xem cái nhà này có đúng là của User không? (Bảo mật)
        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new RuntimeException("House not found"));

        if (!house.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Bạn không có quyền xem phòng của nhà này!");
        }

        // 3. Nếu đúng chủ nhà -> Trả về danh sách phòng
        List<Room> rooms = roomRepository.findByHouseId(houseId);
        return ResponseEntity.ok(rooms);
    }
}
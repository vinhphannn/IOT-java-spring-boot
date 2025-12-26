package com.phanvanvinh.doan.controller;

import com.phanvanvinh.doan.dto.request.SetupProfileRequest;
import com.phanvanvinh.doan.model.Room;
import com.phanvanvinh.doan.model.User;
import com.phanvanvinh.doan.repository.RoomRepository; // Nhớ tạo RoomRepository nhé
import com.phanvanvinh.doan.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoomRepository roomRepository; // Interface RoomRepository extends JpaRepository<Room, Long> {}

    @PostMapping("/setup")
    public ResponseEntity<?> setupProfile(@RequestBody SetupProfileRequest request) {
        // 1. Lấy user hiện tại đang login từ Token
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        // 2. Cập nhật thông tin User
        user.setNationality(request.getNationality());
        user.setHouseName(request.getHouseName());
        user.setAddress(request.getAddress());
        userRepository.save(user);

        // 3. Tạo nhanh các phòng (nếu có gửi lên)
        if (request.getRoomNames() != null) {
            for (String roomName : request.getRoomNames()) {
                Room room = new Room();
                room.setName(roomName);
                room.setUser(user);
                room.setImageUrl("default_room.png"); // Ảnh mặc định
                roomRepository.save(room);
            }
        }

        return ResponseEntity.ok("Profile setup completed!");
    }
}
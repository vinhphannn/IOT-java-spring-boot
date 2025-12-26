package com.phanvanvinh.doan.controller;

import com.phanvanvinh.doan.dto.request.SetupProfileRequest;
import com.phanvanvinh.doan.dto.response.*;
import com.phanvanvinh.doan.model.House; // Import Model House mới
import com.phanvanvinh.doan.model.Room;
import com.phanvanvinh.doan.model.User;
import com.phanvanvinh.doan.repository.HouseRepository; // Import Repo mới
import com.phanvanvinh.doan.repository.RoomRepository;
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
    HouseRepository houseRepository; // <--- Cần thêm cái này

    @Autowired
    RoomRepository roomRepository;

    @PostMapping("/setup")
    public ResponseEntity<?> setupProfile(@RequestBody SetupProfileRequest request) {
        // 1. Lấy user hiện tại đang login từ Token
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. LOGIC MỚI: TẠO CĂN NHÀ (Thay vì update User)
        House newHouse = new House();
        newHouse.setName(request.getHouseName()); // VD: "Nhà phố cổ"
        newHouse.setAddress(request.getAddress()); // VD: "Hà Nội"
        newHouse.setNationality(request.getNationality());
        newHouse.setUser(user); // Gán nhà này cho User hiện tại

        // Lưu nhà vào DB để lấy ID
        House savedHouse = houseRepository.save(newHouse);

        // 3. LOGIC MỚI: TẠO PHÒNG VÀ GÁN VÀO NHÀ
        if (request.getRoomNames() != null) {
            for (String roomName : request.getRoomNames()) {
                Room room = new Room();
                room.setName(roomName);

                // Set ảnh mặc định (Vợ có thể viết hàm switch-case chọn ảnh đẹp hơn ở đây)
                room.setImageUrl("default_room.png");

                // QUAN TRỌNG: Gán phòng vào Nhà (chứ không phải User nữa)
                room.setHouse(savedHouse);

                roomRepository.save(room);
            }
        }

        return ResponseEntity.ok(new SetupResponse(
                "Setup thành công!",
                savedHouse.getId(),
                savedHouse.getName()));
    }
}
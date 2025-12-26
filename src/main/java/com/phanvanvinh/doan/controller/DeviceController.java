package com.phanvanvinh.doan.controller;

import com.phanvanvinh.doan.model.*;
import com.phanvanvinh.doan.repository.*;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.Data;
import com.phanvanvinh.doan.dto.request.BindDeviceRequest;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    DeviceLogRepository deviceLogRepository;

    // 1. API Thêm thiết bị mới (Gọi sau khi Provisioning thành công)
    @PostMapping("/bind")
    public ResponseEntity<?> bindDevice(@Valid @RequestBody BindDeviceRequest request) {
        // Tìm phòng
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // Tạo device mới
        Device device = new Device();
        device.setName(request.getName());
        device.setType(request.getType()); // Light, Fan...
        device.setMacAddress(request.getMacAddress());
        device.setRoom(room);
        device.setStatus(false);

        deviceRepository.save(device);
        return ResponseEntity.ok("Device added successfully!");
    }

    // 2. API Lấy danh sách thiết bị theo Phòng
    @GetMapping("/room/{roomId}")
    public List<Device> getDevicesByRoom(@PathVariable Long roomId) {
        return deviceRepository.findByRoomId(roomId); // Nhớ thêm hàm này vào Repo
    }

    // 3. API Lưu Log (Gọi khi bật tắt)
    // ... Vợ tự viết thêm logic lưu log khi điều khiển MQTT nhé
}

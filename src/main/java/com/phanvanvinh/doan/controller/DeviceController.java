package com.phanvanvinh.doan.controller;

<<<<<<< HEAD
// --- CÁC IMPORT CẦN THIẾT ---
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// --- IMPORT CÁC CLASS CỦA DỰ ÁN ---
import com.phanvanvinh.doan.dto.request.BindDeviceRequest;
import com.phanvanvinh.doan.dto.response.MessageResponse; // Đảm bảo file này có trong dto/response
import com.phanvanvinh.doan.model.Device;
import com.phanvanvinh.doan.service.DeviceService;
// Thêm import này ở đầu file
import org.springframework.web.bind.annotation.GetMapping;
=======
import com.phanvanvinh.doan.model.*;
import com.phanvanvinh.doan.repository.*;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.Data;
import com.phanvanvinh.doan.dto.request.BindDeviceRequest;

>>>>>>> 316b47d3d0bc6209281e72b4d01ca960fb5aeba0
import java.util.List;

@RestController
@RequestMapping("/api/devices")
<<<<<<< HEAD
@CrossOrigin(origins = "*", maxAge = 3600)
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    // API: POST /api/devices/bind
    @PostMapping("/bind")
    public ResponseEntity<?> bindDevice(@RequestBody BindDeviceRequest request) {
        try {
            Device newDevice = deviceService.bindDevice(request);
            // Trả về OK kèm thông báo
            return ResponseEntity.ok(new MessageResponse("Thêm thiết bị thành công! ID: " + newDevice.getId()));
        } catch (RuntimeException e) {
            // Trả về lỗi 400 Bad Request
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Lỗi: " + e.getMessage()));
        }
    }

    // API: GET /api/devices
    @GetMapping
    public ResponseEntity<List<Device>> getAllDevices() {
        List<Device> devices = deviceService.getAllDevices();
        return ResponseEntity.ok(devices);
    }
}
=======
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
>>>>>>> 316b47d3d0bc6209281e72b4d01ca960fb5aeba0

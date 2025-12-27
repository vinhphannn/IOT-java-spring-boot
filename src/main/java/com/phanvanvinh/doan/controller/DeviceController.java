package com.phanvanvinh.doan.controller;

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
import java.util.List;

@RestController
@RequestMapping("/api/devices")
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
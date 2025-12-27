package com.phanvanvinh.doan.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phanvanvinh.doan.dto.request.BindDeviceRequest;
import com.phanvanvinh.doan.model.Device;
import com.phanvanvinh.doan.model.Room;
import com.phanvanvinh.doan.repository.DeviceRepository;
import com.phanvanvinh.doan.repository.RoomRepository;
import com.phanvanvinh.doan.service.DeviceService;
import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public Device bindDevice(BindDeviceRequest request) {
        // 1. Kiểm tra phòng
        Long roomId = request.getRoom().getId();
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng với ID: " + roomId));

        // 2. Kiểm tra thiết bị cũ/mới
        Device device = deviceRepository.findByMacAddress(request.getMacAddress())
                .orElse(new Device());

        // 3. Cập nhật thông tin
        device.setName(request.getName());
        device.setMacAddress(request.getMacAddress());
        device.setType(request.getType());
        device.setRoom(room);

        if (device.getId() == null) {
            device.setStatus(false);
        }

        return deviceRepository.save(device);
    }

    @Override
    public List<Device> getAllDevices() {
        // Lấy tất cả thiết bị từ Repository
        return deviceRepository.findAll();
    }
}
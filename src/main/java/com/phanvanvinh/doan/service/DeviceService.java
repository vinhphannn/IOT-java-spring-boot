package com.phanvanvinh.doan.service;

import com.phanvanvinh.doan.dto.request.BindDeviceRequest;
import com.phanvanvinh.doan.model.Device;
import java.util.List;

public interface DeviceService {
    // Khai báo hàm bindDevice
    Device bindDevice(BindDeviceRequest request);

    List<Device> getAllDevices(); // Thêm dòng này
}
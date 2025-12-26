package com.phanvanvinh.doan.repository;

import com.phanvanvinh.doan.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findByRoomId(Long roomId);
}

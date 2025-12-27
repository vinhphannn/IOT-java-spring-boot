package com.phanvanvinh.doan.repository;

import com.phanvanvinh.doan.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    // Tìm thiết bị theo địa chỉ MAC
    Optional<Device> findByMacAddress(String macAddress);
}
=======

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findByRoomId(Long roomId);
}
>>>>>>> 316b47d3d0bc6209281e72b4d01ca960fb5aeba0

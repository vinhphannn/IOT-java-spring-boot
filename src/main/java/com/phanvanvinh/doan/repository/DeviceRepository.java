package com.phanvanvinh.doan.repository;

import com.phanvanvinh.doan.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    // Tìm thiết bị theo địa chỉ MAC
    Optional<Device> findByMacAddress(String macAddress);
}
package com.phanvanvinh.doan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.phanvanvinh.doan.model.DeviceLog;
import java.util.List;

public interface DeviceLogRepository extends JpaRepository<DeviceLog, Long> {
    List<DeviceLog> findByDeviceId(Long deviceId);
}
package com.phanvanvinh.doan.dto.request;

import com.phanvanvinh.doan.model.DeviceType; // Nhớ import Enum của vợ
import lombok.Data;

@Data
public class BindDeviceRequest {
    private String name;
    private String macAddress;
    private DeviceType type; // Tự động map chuỗi "RELAY" -> Enum.RELAY
    private RoomRequest room; // Hứng cái object { "id": 1 }

    // Class con để hứng cái id phòng
    @Data
    public static class RoomRequest {
        private Long id;
    }
}
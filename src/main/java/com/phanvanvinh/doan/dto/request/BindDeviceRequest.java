package com.phanvanvinh.doan.dto.request;

<<<<<<< HEAD
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
=======
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BindDeviceRequest {

    @NotBlank(message = "Tên thiết bị không được để trống")
    private String name; // VD: "Đèn ngủ"

    @NotBlank(message = "Loại thiết bị không được để trống")
    private String type; // VD: "LIGHT", "FAN"

    @NotBlank(message = "MAC Address không được để trống")
    private String macAddress; // VD: "A1:B2:C3:D4:E5"

    @NotNull(message = "ID phòng không được để trống")
    private Long roomId; // ID của phòng mà thiết bị này thuộc về
>>>>>>> 316b47d3d0bc6209281e72b4d01ca960fb5aeba0
}
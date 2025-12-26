package com.phanvanvinh.doan.dto.request;

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
}
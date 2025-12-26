package com.phanvanvinh.doan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SetupResponse {
    private String message;
    private Long houseId; // ID nhà vừa tạo
    private String houseName;
}
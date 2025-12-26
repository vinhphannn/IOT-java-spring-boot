package com.phanvanvinh.doan.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class SetupProfileRequest {
    private String nationality;
    private String houseName;
    private String address;
    private List<String> roomNames; // Ví dụ: ["Phòng Khách", "Phòng Ngủ"] để tạo nhanh
}
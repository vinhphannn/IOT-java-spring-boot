package com.phanvanvinh.doan.model;

public enum DeviceType {
    RELAY, // Thiết bị điều khiển bật/tắt (Đèn, Quạt, Máy bơm)
    SENSOR, // Thiết bị chỉ đọc dữ liệu (Nhiệt độ, Độ ẩm, Dòng điện)
    HYBRID // (Tùy chọn) Thiết bị vừa đo vừa điều khiển
}
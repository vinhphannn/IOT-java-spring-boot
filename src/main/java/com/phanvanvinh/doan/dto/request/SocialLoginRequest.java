package com.phanvanvinh.doan.dto.request;

import lombok.Data;

@Data
public class SocialLoginRequest {
    private String token; // Token nhận từ Google/Facebook
    private String provider; // "GOOGLE" hoặc "FACEBOOK"
}
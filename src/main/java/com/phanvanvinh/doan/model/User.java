package com.phanvanvinh.doan.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User implements UserDetails { // Implement UserDetails để Spring hiểu
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String fullName;
    private String avatarUrl;

    // --- Thông tin cấu hình sau khi đăng ký ---
    private String nationality; // Quốc tịch
    private String houseName; // Tên nhà (VD: Vinh's Smart Home)
    private String address; // Địa chỉ

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;
    private String providerId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Room> rooms;

    // --- Các hàm Override của UserDetails ---

    // Quan trọng: Spring Security hỏi username đâu? Ta trả lời: Là email!
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // Tạm thời chưa phân quyền
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
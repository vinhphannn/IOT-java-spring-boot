package com.phanvanvinh.doan.repository;

import com.phanvanvinh.doan.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Chỉ cần tìm theo email thôi
    Optional<User> findByEmail(String email);

    // Check tồn tại
    Boolean existsByEmail(String email);
}
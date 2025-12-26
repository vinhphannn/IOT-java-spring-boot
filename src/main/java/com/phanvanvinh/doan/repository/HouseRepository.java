package com.phanvanvinh.doan.repository;

import com.phanvanvinh.doan.model.House;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HouseRepository extends JpaRepository<House, Long> {
    List<House> findByUserId(Long userId);
}
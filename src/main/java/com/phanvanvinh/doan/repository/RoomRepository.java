package com.phanvanvinh.doan.repository;

import com.phanvanvinh.doan.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    // Tìm tất cả phòng của một User cụ thể (dựa vào user_id)
    // Spring Data JPA sẽ tự động hiểu và gen ra câu lệnh SQL: SELECT * FROM rooms
    // WHERE user_id = ?
    List<Room> findByHouseUserId(Long userId);

    List<Room> findByHouseId(Long houseId);
}
package com.example.Tutorials_Eom.repository;

import com.example.Tutorials_Eom.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Integer> {

    // สามารถเพิ่ม Query เพื่อค้นหาข้อมูลเฉพาะ
    @Query("SELECT o FROM Orders o WHERE o.user.userId = :userId")
    List<Orders> findOrdersByUserId(@Param("userId") Integer userId);

}

package com.example.Tutorials_Eom.repository;

import com.example.Tutorials_Eom.dto.UserLoginHomeDto;
import com.example.Tutorials_Eom.entity.UserProducts;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface UserProductsRepository extends JpaRepository<UserProducts, Integer> {

    @Query("SELECT up FROM UserProducts up JOIN FETCH up.product WHERE up.user.userId = :userId")
    List<UserProducts> findByUserId(@Param("userId") Integer userId);

    @Query("SELECT COUNT(up) > 0 FROM UserProducts up WHERE up.user.userId = :userId AND up.product.productId = :productId")
    boolean existsByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    @Query("SELECT new com.example.Tutorials_Eom.dto.UserLoginHomeDto(u.userId, u.username, u.wallet) FROM Users u WHERE u.userId = :userId")
    UserLoginHomeDto findUserDTOByUserId(@Param("userId") Integer userId);

    // เรียงลำดับ โปรแกรมที่เช่าล่าสุดขึ้นก่อน
    @Query(value = "SELECT up.user_products_id, p.product_id, u.username, u.wallet, p.name, p.file_program, up.start_date, up.end_date, up.status " +
            "FROM user_products up " +
            "JOIN users u ON up.user_id = u.user_id " +
            "JOIN products p ON up.product_id = p.product_id " +
            "WHERE up.user_id = :userId " +
            "ORDER BY up.start_date DESC", nativeQuery = true)
    List<Object[]> findUserProductDetailsNative(@Param("userId") Integer userId);


    @Query("SELECT COUNT(up) > 0 FROM UserProducts up WHERE up.user.userId = :userId AND up.product.productId = :productId AND up.status = 'ACTIVATE'")
    boolean existsByUserIdAndProductIdAndStatus(@Param("userId") Integer userId, @Param("productId") Integer productId,
                                                @Param("status") String status);

    @Transactional
    @Modifying
    @Query("UPDATE UserProducts up SET up.status = 'DEACTIVATE' WHERE up.endDate < :currentTime AND up.status = 'ACTIVATE'")
    void updateExpiredStatus(LocalDateTime currentTime);


    @Query("SELECT p.name, COUNT(DISTINCT up.user.userId), COUNT(up.userProductsId) " +
            "FROM UserProducts up JOIN up.product p " +
            "GROUP BY p.name " +
            "ORDER BY COUNT(up.userProductsId) DESC")
    List<Object[]> getRentalStatistics();

}

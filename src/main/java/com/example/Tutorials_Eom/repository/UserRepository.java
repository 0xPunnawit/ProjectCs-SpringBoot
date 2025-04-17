package com.example.Tutorials_Eom.repository;

import com.example.Tutorials_Eom.dto.UserLoginHomeDto;
import com.example.Tutorials_Eom.dto.UserWalletDto;
import com.example.Tutorials_Eom.entity.UserRole;
import com.example.Tutorials_Eom.entity.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByTel(String tel);

    long count();

    Optional<Users> findByUsername(String username); // ค้นหาผู้ใช้ตาม username

    // ============== COUNT Users ==============
    @Query("SELECT COUNT(u.userId) FROM Users u")
    Integer countUserIds(); // นับจำนวน userId

    @Query("SELECT new com.example.Tutorials_Eom.dto.UserLoginHomeDto(u.userId, u.username, u.wallet) FROM Users u WHERE u.userId = :userId")
    UserLoginHomeDto findUserDTOByUserId(@Param("userId") Integer userId);

    @Query("SELECT COUNT(u) > 0 FROM Users u WHERE u.username = :username AND u.passwordHash = :passwordHash")
    boolean existsByUsernameAndPassword(@Param("username") String username, @Param("passwordHash") String passwordHash);

    @Modifying
    @Transactional
    @Query("UPDATE Users u SET u.wallet = :newWallet, u.walletLastUpdated = :lastUpdated WHERE u.userId = :userId")
    void updateUserWallet(@Param("userId") Integer userId,
                          @Param("newWallet") BigDecimal newWallet,
                          @Param("lastUpdated") LocalDateTime lastUpdated);


    @Query("SELECT new com.example.Tutorials_Eom.dto.UserWalletDto(u.userId, u.username, u.wallet, u.walletLastUpdated) "
            +
            "FROM Users u WHERE u.userId = :userId")
    UserWalletDto findUserWalletDtoByUserId(@Param("userId") Integer userId);


}

package com.example;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    public User getByUsername(String username);

    public User findByEmail(String email);

    public User findByUsername(String username);

    @Query("UPDATE User u SET u.failedAttempt = ?1 WHERE u.username = ?2")
    @Modifying
    @Transactional
    public void updateFailedAttempts(int failAttempts, String username);

}

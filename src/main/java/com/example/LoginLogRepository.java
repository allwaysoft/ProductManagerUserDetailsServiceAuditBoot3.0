package com.example;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {

    Page<LoginLog> findAllByOrderByIdDesc(Pageable pageable);

    @Query("SELECT l FROM LoginLog l WHERE CONCAT(l.username, ' ', l.description, ' ', l.ip, ' ', l.sessionid) LIKE %?1%")
    public Page<LoginLog> search(String keyword, Pageable pageable);

}

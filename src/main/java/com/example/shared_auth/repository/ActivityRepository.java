package com.example.shared_auth.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shared_auth.entity.Activity;


import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Optional<Activity> findById(Long id);

}

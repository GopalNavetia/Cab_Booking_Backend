package com.gopal.cab_booking.repository;

import com.gopal.cab_booking.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {
}
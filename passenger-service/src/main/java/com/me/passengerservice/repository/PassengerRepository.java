package com.me.passengerservice.repository;

import com.me.passengerservice.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}

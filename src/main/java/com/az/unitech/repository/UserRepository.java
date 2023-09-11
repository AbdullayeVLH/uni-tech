package com.az.unitech.repository;

import com.az.unitech.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByPin(String pin);

    User findUserByPinAndPassword (String pin, String password);
}

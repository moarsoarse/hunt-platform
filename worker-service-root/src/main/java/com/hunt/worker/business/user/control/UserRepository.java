package com.hunt.worker-service-root.business.user.control;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.hunt.worker-service-root.business.user.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsernameAndPassword(String username, String hashedPassword);

    Optional<User> findByUsername(String username);
}

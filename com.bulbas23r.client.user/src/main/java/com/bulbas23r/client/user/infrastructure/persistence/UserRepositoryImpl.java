package com.bulbas23r.client.user.infrastructure.persistence;

import com.bulbas23r.client.user.domain.model.User;
import com.bulbas23r.client.user.domain.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositoryImpl extends UserRepository, JpaRepository<User, Long> {

}

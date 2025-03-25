package com.bulbas23r.client.user.domain.repository;

import com.bulbas23r.client.user.domain.model.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepository{

  Optional<User> findByUsername(String username);

  User save(User user);

  Optional<User> findById(Long userId);

  Page<User> findAll(Pageable pageable);

  Optional<User> findBySlackId(String slackId);
}

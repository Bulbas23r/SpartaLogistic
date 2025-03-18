package com.bulbas23r.client.auth.application.service;

import com.bulbas23r.client.auth.application.dto.CustomUserDetails;
import com.bulbas23r.client.auth.application.dto.UserDetailsDto;
import com.bulbas23r.client.auth.client.UserClient;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  private UserClient userClient;

//    //public CustomUserDetailsService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }


  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserDetailsDto user = userClient.getUserDetails(username);

    return new CustomUserDetails(
        user.getUserId(),
        user.getUsername(),
        user.getPassword(),
        user.getRole()
    );
  }
}

package com.university.coursework.security.roles;

import com.university.coursework.entity.UserEntity;
import com.university.coursework.repository.UserRepository;
import com.university.coursework.security.roles.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> userOptional = Optional.ofNullable(userRepository.findByEmail(email));
        UserEntity user = userOptional.orElseThrow(() -> new UsernameNotFoundException("Could not find user: " + email));

        return new UserDetailsImpl(user);
    }

}

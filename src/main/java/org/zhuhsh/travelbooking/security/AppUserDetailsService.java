package org.zhuhsh.travelbooking.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.List;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final org.zhuhsh.travelbooking.repository.UserRepository userRepository;

    public AppUserDetailsService(org.zhuhsh.travelbooking.repository.UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        org.zhuhsh.travelbooking.model.User u = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + u.getRole());
        return User.withUsername(u.getUsername())
                .password(u.getPassword())
                .authorities(List.of(authority))
                .build();
    }
}


package com.example;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public UserDetails loadUserByUsername(String userName)
            throws UsernameNotFoundException {
        User domainUser = userRepository.findByUsername(userName);

        if (domainUser != null) {
            return new org.springframework.security.core.userdetails.User(
                    domainUser.getUsername(),
                    domainUser.getPassword(),
                    domainUser.getEnabled(),
                    domainUser.getAccountNonExpired(),
                    domainUser.getCredentialsNonExpired(),
                    domainUser.getAccountNonLocked(),
                    getAuthorities(domainUser.getRoles())
            );
        }
//        return null;
        throw new UsernameNotFoundException("User " + userName + " does not exist");
    }

    private Collection<? extends GrantedAuthority> getAuthorities(
            Collection<Role> roles) {
        List<GrantedAuthority> authorities
                = new ArrayList<>();
        for (Role role : roles) {
//            authorities.add(new SimpleGrantedAuthority(role.getName()));
            role.getPermissions().stream()
                    .map(p -> new SimpleGrantedAuthority(p.getName()))
                    .forEach(authorities::add);
        }

        return authorities;
    }

}

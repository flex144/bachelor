package com.example.bachelor.services;

import com.example.bachelor.data.dto.UserDetailsPrincipal;
import com.example.bachelor.data.entities.UserEntity;
import com.example.bachelor.utility.exceptions.UserNotActiveException;
import com.example.bachelor.utility.exceptions.UserNotConfirmedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userService.findUserByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserDetailsPrincipal(user);
    }
}

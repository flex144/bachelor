package com.example.bachelor.services;

import com.example.bachelor.data.dto.UserDetailsPrincipal;
import com.example.bachelor.data.dto.UserDto;
import com.example.bachelor.data.entities.UserEntity;
import com.example.bachelor.data.enums.UserRoles;
import com.example.bachelor.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserEntity readUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public UserDto readUserDtoById(Long id) {
        UserEntity userEntity = readUserById(id);

        return userEntity != null ? modelMapper.map(userEntity, UserDto.class) : null;

    }

    public List<UserEntity> readAllUsers() {
        List<UserEntity> result = new ArrayList<>();
        userRepository.findAll().forEach(result::add);
        return result;
    }

    public UserEntity createUser(String email, String password) throws IllegalStateException{
        UserEntity user = findUserByEmail(email);
        if (user != null) {
            throw new IllegalStateException("User with Email already present!");
        }

        UserEntity newUserEntity = new UserEntity();
        newUserEntity.setEmail(email);
        newUserEntity.setPassword(encode(password));
        newUserEntity.setRole(UserRoles.ROLE_USER);
        newUserEntity.setActive(true);
        userRepository.save(newUserEntity);
        return newUserEntity;
    }

    public UserEntity findUserByEmail(String email) {
        Iterable<UserEntity> usersByEmail = userRepository.findUserByEmail(email);
        return usersByEmail == null || !usersByEmail.iterator().hasNext() ? null : usersByEmail.iterator().next();
    }

    public UserDto findUserDtoByEmail(String email) {
        UserEntity userEntity = findUserByEmail(email);

        return userEntity != null ? modelMapper.map(userEntity, UserDto.class) : null;
    }

    public String encode(String pw) {
        return passwordEncoder.encode(pw);
    }
}

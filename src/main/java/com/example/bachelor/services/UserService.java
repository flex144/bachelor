package com.example.bachelor.services;

import com.example.bachelor.data.dto.UserDto;
import com.example.bachelor.data.dto.UserGuardingRelationDto;
import com.example.bachelor.data.dto.UserStatisticsDto;
import com.example.bachelor.data.entities.UserEntity;
import com.example.bachelor.data.enums.UserRoles;
import com.example.bachelor.repositories.UserRepository;
import com.example.bachelor.utility.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserEntity readUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public UserDto readUserDtoById(Long id) {
        UserEntity userEntity = readUserById(id);

        return userMapper.mapUserEntityToDto(userEntity);

    }

    public List<UserDto> readAllUserDtos() {
        List<UserDto> allUsers = new ArrayList<>();
        readAllUsers().forEach(n -> allUsers.add(userMapper.mapUserEntityToDto(n)));

        return allUsers;
    }

    public List<UserEntity> readAllUsers() {
        List<UserEntity> result = new ArrayList<>();
        userRepository.findAll().forEach(result::add);
        return result;
    }

    public void saveUser(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    public void saveUserDto(UserDto userDto) {
        saveUser(userMapper.mapUserDtoToEntity(userDto));
    }

    public UserEntity createUser(UserDto userDto) throws IllegalStateException{
        UserEntity user = findUserByEmail(userDto.getEmail());
        if (user != null) {
            throw new IllegalStateException("Nutzer mit Email '" + user.getEmail() + "' existiert bereits!");
        }

        UserEntity newUserEntity = new UserEntity();
        newUserEntity.setEmail(userDto.getEmail());
        newUserEntity.setPassword(encode(userDto.getPassword()));
        newUserEntity.setFirstName(userDto.getFirstName());
        newUserEntity.setLastName(userDto.getLastName());
        newUserEntity.setLocalBranch(userDto.getLocalbranch());
        newUserEntity.setRole(UserRoles.ROLE_USER);
        newUserEntity.setActive(false); //TODO: erstmal inaktiv setzen, erst bei Freischaltung durch ADMIN aktiv setzen
        userRepository.save(newUserEntity);
        return newUserEntity;
    }

    public UserEntity findUserByEmail(String email) {
        Iterable<UserEntity> usersByEmail = userRepository.findUserByEmail(email);
        return usersByEmail == null || !usersByEmail.iterator().hasNext() ? null : usersByEmail.iterator().next();
    }

    public UserDto findUserDtoByEmail(String email) {
        UserEntity userEntity = findUserByEmail(email);

        return userMapper.mapUserEntityToDto(userEntity);
    }

    public String encode(String pw) {
        return passwordEncoder.encode(pw);
    }
}

package com.example.bachelor.services;

import com.example.bachelor.data.dto.ConfirmationTokenDto;
import com.example.bachelor.data.dto.UserDto;
import com.example.bachelor.data.dto.UserGuardingRelationDto;
import com.example.bachelor.data.dto.UserStatisticsDto;
import com.example.bachelor.data.entities.ConfirmationTokenEntity;
import com.example.bachelor.data.entities.UserEntity;
import com.example.bachelor.data.enums.UserRoles;
import com.example.bachelor.repositories.ConfirmationTokenRepository;
import com.example.bachelor.repositories.UserRepository;
import com.example.bachelor.utility.exceptions.UserAlreadyExistsException;
import com.example.bachelor.utility.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

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

    public UserEntity createUser(UserDto userDto) throws UserAlreadyExistsException{
        UserEntity user = findUserByEmail(userDto.getEmail());
        if (user != null) {
            throw new UserAlreadyExistsException("Nutzer mit Email '" + user.getEmail() + "' existiert bereits!");
        }

        UserEntity newUserEntity = new UserEntity();
        newUserEntity.setEmail(userDto.getEmail());
        newUserEntity.setPassword(encode(userDto.getPassword()));
        newUserEntity.setFirstName(userDto.getFirstName());
        newUserEntity.setLastName(userDto.getLastName());
        newUserEntity.setLocalBranch(userDto.getLocalbranch());
        newUserEntity.setRole(UserRoles.ROLE_USER);
        newUserEntity.setActive(false);
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

    public void confirmUser(String email) {
        UserDto userToConfirm = findUserDtoByEmail(email);
        userToConfirm.setConfirmed(true);
        saveUserDto(userToConfirm);
    }

    public String encode(String pw) {
        return passwordEncoder.encode(pw);
    }

    public ConfirmationTokenEntity createNewConfirmationToken(String userEmail) {
        ConfirmationTokenEntity confirmationToken = new ConfirmationTokenEntity();

        confirmationToken.setCreatedDate(new Date());
        confirmationToken.setEmailUser(userEmail);
        confirmationToken.setConfirmationToken(UUID.randomUUID().toString());

        confirmationToken = confirmationTokenRepository.save(confirmationToken);

        return confirmationToken;
    }

    public ConfirmationTokenEntity findConfirmationToken(String token) {
        Iterable<ConfirmationTokenEntity> tokens = confirmationTokenRepository.findUserByConfirmationToken(token);

        List<ConfirmationTokenEntity> result = new ArrayList<>();
        tokens.forEach(result::add);

        if (result.size() != 1) {
            throw new IllegalStateException("More or less than one confirmation token found!");
        }

        return result.get(0);
    }

    public void deleteConfirmationTokenById(Long tokenId) {
        confirmationTokenRepository.deleteById(tokenId);
    }

    public String setRandomUserPassword(String email) {
        String newPassword = "";
        UserDto toChange = findUserDtoByEmail(email);
        if (toChange != null) {
            newPassword = UUID.randomUUID().toString().substring(1,12);
            toChange.setPassword(encode(newPassword));
            saveUserDto(toChange);
        }
        return newPassword;
    }
}

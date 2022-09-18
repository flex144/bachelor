package com.example.bachelor.utility.mapper;

import com.example.bachelor.data.dto.GuardDayDto;
import com.example.bachelor.data.dto.UserDto;
import com.example.bachelor.data.dto.UserGuardingRelationDto;
import com.example.bachelor.data.entities.GuardDayEntity;
import com.example.bachelor.data.entities.UserEntity;
import com.example.bachelor.data.entities.UserGuardingRelationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserMapper extends BaseMapper {

    public UserDto mapUserEntityToDto(UserEntity userEntity) {

        UserDto userDto = null;

        if (userEntity != null) {
            userDto = modelMapper.map(userEntity, UserDto.class);
        }

        return userDto;
    }

    public UserEntity mapUserDtoToEntity(UserDto userDto) {

        UserEntity userEntity = null;

        if (userDto != null) {
            userEntity = modelMapper.map(userDto, UserEntity.class);
        }

        return userEntity;
    }
}

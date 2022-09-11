package com.example.bachelor.utility.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.DestinationSetter;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseMapper {

    @Autowired
    ModelMapper modelMapper;


    public <S, D, V> void setPropertyMapper(Class<S> sourceType, Class<D> destinationType, DestinationSetter<D, V> fieldToSkip) {
        TypeMap<S, D> propertyMapper = modelMapper.getTypeMap(sourceType, destinationType);

        if (propertyMapper == null) {
            propertyMapper = modelMapper.createTypeMap(sourceType, destinationType);
            propertyMapper.addMappings(mapper -> mapper.skip(fieldToSkip));
        }
    }



}

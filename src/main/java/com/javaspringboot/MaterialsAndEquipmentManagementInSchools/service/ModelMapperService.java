package com.javaspringboot.MaterialsAndEquipmentManagementInSchools.service;

import com.javaspringboot.MaterialsAndEquipmentManagementInSchools.advice.CustomMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ModelMapperService {
    @Autowired
    private static ModelMapper modelMapper;

    public static <S, D> List<D> mapList(List<S> sourceList, CustomMapper<S, D> customMapper) {
        return sourceList.stream()
                .map(customMapper::map)
                .collect(Collectors.toList());
    }

    public static <S, D> List<D> mapSet(Set<S> sourceSet, CustomMapper<S, D> customMapper) {
        return sourceSet.stream()
                .map(customMapper::map)
                .collect(Collectors.toList());
    }

    public static  <S, D> D mapObject(S source, CustomMapper<S, D> customMapper) {
        return customMapper.map(source);
    }

    public static <S, T> List<T> mapListWithDefault(List<S> source, Class<T> targetClass) {
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }
}

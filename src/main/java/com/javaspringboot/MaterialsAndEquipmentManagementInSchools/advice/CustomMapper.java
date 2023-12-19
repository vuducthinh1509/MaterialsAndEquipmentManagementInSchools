package com.javaspringboot.MaterialsAndEquipmentManagementInSchools.advice;

public interface CustomMapper<S,D> {
    D map(S source);
}

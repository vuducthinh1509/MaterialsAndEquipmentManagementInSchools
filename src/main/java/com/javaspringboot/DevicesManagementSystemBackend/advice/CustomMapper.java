package com.javaspringboot.DevicesManagementSystemBackend.advice;

public interface CustomMapper<S,D> {
    D map(S source);
}

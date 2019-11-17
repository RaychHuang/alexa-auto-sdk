package com.amazon.sampleapp.climate.bean;

public interface Modification<T> {
    T update(T t);
}
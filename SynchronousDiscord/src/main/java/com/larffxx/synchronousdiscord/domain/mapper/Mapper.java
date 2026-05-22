package com.larffxx.synchronousdiscord.domain.mapper;

public interface Mapper<T,V> {
    V toDTO(T t);
    T toEntity(V v);
}

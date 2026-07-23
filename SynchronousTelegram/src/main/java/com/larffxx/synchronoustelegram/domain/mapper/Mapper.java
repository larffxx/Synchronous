package com.larffxx.synchronoustelegram.domain.mapper;

public interface Mapper<T, V> {
   V toDTO(T t);
   T toEntity(V dto) ;
}

package com.larffxx.synchronousdiscord.domain.mapper;

/**
 * Mapper contract.
 * @param <T> the T type parameter.
 * @param <V> the V type parameter.
 */
public interface Mapper<T,V> {
    /**
     * Converts entity to DTO.
     * @param t the item.
     * @return the resulting v.
     */
    V toDTO(T t);
    /**
     * Converts DTO to entity.
     * @param v the item.
     * @return the resulting t.
     */
    T toEntity(V v);
}

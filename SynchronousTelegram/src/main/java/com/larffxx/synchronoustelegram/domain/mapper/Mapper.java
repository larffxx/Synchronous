package com.larffxx.synchronoustelegram.domain.mapper;

/**
 * Contract for converters between entities and data transfer objects.
 * Implementations cover profiles and server and user links.
 */
public interface Mapper<T, V> {
   /**
    * Converts an entity to its data transfer object.
    * @param t the entity to convert
    * @return the converted transfer object
    */
   V toDTO(T t);
   /**
    * Converts a data transfer object to its entity.
    * @param dto the transfer object to convert
    * @return the converted entity
    */
   T toEntity(V dto) ;
}

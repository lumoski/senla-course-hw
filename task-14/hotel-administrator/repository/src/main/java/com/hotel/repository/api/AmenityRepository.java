package com.hotel.repository.api;

import java.util.List;

import com.hotel.database.entity.AmenityEntity;

public interface AmenityRepository extends BaseRepository<AmenityEntity, Long> {

    List<AmenityEntity> findAllSortedByPrice();
}
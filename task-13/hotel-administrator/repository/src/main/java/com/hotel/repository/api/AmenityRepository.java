package com.hotel.repository.api;

import java.util.List;

public interface AmenityRepository<T, ID> extends BaseRepository<T, ID> {

    List<T> findAllSortedByPrice();
}
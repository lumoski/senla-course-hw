package com.hotel.repository.impl.hibernate;

import org.springframework.stereotype.Repository;

import com.hotel.database.entity.GuestEntity;
import com.hotel.repository.api.GuestRepository;

@Repository
public class GuestRepositoryImpl extends AbstractJpaRepository<GuestEntity, Long> implements GuestRepository {

    public GuestRepositoryImpl() {
        super(GuestEntity.class);
    }
}
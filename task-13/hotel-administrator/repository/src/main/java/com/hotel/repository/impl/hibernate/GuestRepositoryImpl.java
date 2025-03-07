package com.hotel.repository.impl.hibernate;

import com.hotel.database.entity.GuestEntity;
import com.hotel.repository.api.GuestRepository;

public class GuestRepositoryImpl extends AbstractJpaRepository<GuestEntity, Long> implements GuestRepository<GuestEntity, Long> {

    public GuestRepositoryImpl() {
        super(GuestEntity.class);
    }
}
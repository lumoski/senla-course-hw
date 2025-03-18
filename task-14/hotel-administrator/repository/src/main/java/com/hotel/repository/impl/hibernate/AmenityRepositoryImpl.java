package com.hotel.repository.impl.hibernate;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Repository;

import com.hotel.database.EntityManagerProvider;
import com.hotel.database.entity.AmenityEntity;
import com.hotel.repository.api.AmenityRepository;

@Slf4j
@Repository
public class AmenityRepositoryImpl extends AbstractJpaRepository<AmenityEntity, Long> implements AmenityRepository {
    
    public AmenityRepositoryImpl() {
        super(AmenityEntity.class);
    }

    @Override
    public List<AmenityEntity> findAllSortedByPrice() {
        try {
            log.debug("Retrieving all amenities sorted by price");
            
            List<AmenityEntity> amenities = EntityManagerProvider.getEntityManager()
                .createQuery("FROM AmenityEntity ORDER BY price", AmenityEntity.class)
                .getResultList();
                
            log.debug("Found {} amenities sorted by price", amenities.size());
            return amenities;
            
        } catch (Exception e) {
            log.error("Error retrieving amenities sorted by price: {}", e.getMessage(), e);
            throw new RuntimeException("Error retrieving amenities sorted by price", e);
        } finally {
            EntityManagerProvider.closeEntityManager();
        }
    }
}

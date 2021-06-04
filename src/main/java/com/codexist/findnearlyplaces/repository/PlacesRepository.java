package com.codexist.findnearlyplaces.repository;

import com.codexist.findnearlyplaces.dto.Places;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface PlacesRepository extends MongoRepository<Places,Long> {
    List<Places> getPlacesByRequestId(UUID requestId);
}

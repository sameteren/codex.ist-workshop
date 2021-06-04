package com.codexist.findnearlyplaces.repository;

import com.codexist.findnearlyplaces.dto.Requests;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RequestsRepository extends MongoRepository<Requests,Long> {

     Requests findByLongitudeAndLatitudeAndRadius(String longitute,String Latitude,String Radius);
}

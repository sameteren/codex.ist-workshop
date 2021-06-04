package com.codexist.findnearlyplaces.Service;

import com.codexist.findnearlyplaces.dto.Places;
import com.codexist.findnearlyplaces.dto.Requests;
import com.codexist.findnearlyplaces.model.response.Example;
import com.codexist.findnearlyplaces.model.response.FindPlacesResponse;
import com.codexist.findnearlyplaces.model.response.Location;
import com.codexist.findnearlyplaces.model.response.Result;
import com.codexist.findnearlyplaces.repository.PlacesRepository;
import com.codexist.findnearlyplaces.repository.RequestsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;


@Service
public class PlacesServiceImp implements PlacesService {
    @Autowired
    RequestsRepository requestsRepository;

    @Autowired
    PlacesRepository placesRepository;

    @Value("${google.api.key}")
    private String GOOGLE_API_KEY;
    @Value("${google.api.url}")
    private String GOOGLE_API_URL;

    @Override
    public FindPlacesResponse findNearlyPlaces(String longitude, String latitude, String radius) {

        Requests savedRequest = requestsRepository.findByLongitudeAndLatitudeAndRadius(longitude, latitude, radius);
        if (savedRequest == null) {
            String uri = GOOGLE_API_URL;
            RestTemplate restTemplate = new RestTemplate();
            Example allNearlyPlaces = restTemplate.getForObject(
                    uri, Example.class, longitude, latitude, radius, GOOGLE_API_KEY);
            Requests requests = new Requests();
            requests.setLatitude(latitude);
            requests.setLongitude(longitude);
            requests.setRadius(radius);
            requests.setRequestId(UUID.randomUUID());
            requestsRepository.save(requests);
            for (Result results : allNearlyPlaces.getResults()) {
                Places places = new Places();
                places.setName(results.getName());
                places.setLatitude(results.getGeometry().getLocation().getLat().toString());
                places.setLongitude(results.getGeometry().getLocation().getLng().toString());
                places.setVicinity(results.getVicinity());
                places.setRequestId(requests.getRequestId());
                places.setPlacesId(UUID.randomUUID());
                placesRepository.save(places);
            }
        }

        List<Places> placesList = placesRepository.getPlacesByRequestId(savedRequest.getRequestId());
        FindPlacesResponse findPlacesResponse = new FindPlacesResponse();
        findPlacesResponse.setPlacesList(placesList);
        return findPlacesResponse;


    }
}

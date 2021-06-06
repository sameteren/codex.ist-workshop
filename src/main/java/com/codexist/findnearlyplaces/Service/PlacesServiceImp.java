package com.codexist.findnearlyplaces.Service;

import com.codexist.findnearlyplaces.dto.Places;
import com.codexist.findnearlyplaces.dto.Requests;
import com.codexist.findnearlyplaces.model.response.Example;
import com.codexist.findnearlyplaces.model.response.FindPlacesResponse;
import com.codexist.findnearlyplaces.model.response.Result;
import com.codexist.findnearlyplaces.repository.PlacesRepository;
import com.codexist.findnearlyplaces.repository.RequestsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;


@Service
public class PlacesServiceImp implements PlacesService {
    Logger log = LoggerFactory.getLogger(PlacesServiceImp.class);
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
        Requests requests = new Requests();
        requests.setRequestId(UUID.randomUUID());
        Example allNearlyPlaces = new Example();
        String nextPageToken = "1";
        FindPlacesResponse findPlacesResponse = new FindPlacesResponse();
        Requests savedRequest = requestsRepository.findByLongitudeAndLatitudeAndRadius(longitude, latitude, radius);
        if (savedRequest == null) {
            String uri = GOOGLE_API_URL;
            RestTemplate restTemplate = new RestTemplate();
            try {
                while (nextPageToken !=null)
                {
                    allNearlyPlaces = restTemplate.getForObject(
                            uri, Example.class, longitude, latitude, radius, GOOGLE_API_KEY);
                    savePlaces(allNearlyPlaces,requests.getRequestId());
                    if(allNearlyPlaces.getNextPageToken()!=null)
                    {
                        nextPageToken=allNearlyPlaces.getNextPageToken();
                        uri = GOOGLE_API_URL + "&pagetoken=" + nextPageToken;
                    }
                    else{
                        nextPageToken=null;
                    }
                }
            }
            catch (Exception e)
            {
                log.error("Google Places API Error");
            }
            saveRequest(requests,latitude,longitude,radius);
        }
        findPlacesResponse.setPlacesList(getPlacesList(savedRequest, requests));
        return findPlacesResponse;

    }
    public void savePlaces(Example allNearlyPlaces, UUID requestID ){
        for (Result results : allNearlyPlaces.getResults()) {
            Places places = new Places();
            places.setName(results.getName());
            places.setLatitude(results.getGeometry().getLocation().getLat().toString());
            places.setLongitude(results.getGeometry().getLocation().getLng().toString());
            places.setVicinity(results.getVicinity());
            places.setRequestId(requestID);
            places.setPlacesId(UUID.randomUUID());
            placesRepository.save(places);
        }
    }
    public void saveRequest(Requests requests,String latitude, String longitude, String radius){
        requests.setLatitude(latitude);
        requests.setLongitude(longitude);
        requests.setRadius(radius);
        requests.setRequestId(requests.getRequestId());
        requestsRepository.save(requests);
    }
    public List<Places> getPlacesList( Requests savedRequest,Requests requests){
        List<Places> placesList;
        //placesList= (savedRequest != null ? placesRepository.getPlacesByRequestId(savedRequest.getRequestId()) : placesRepository.getPlacesByRequestId(requests.getRequestId()));
        if(savedRequest !=null)
        {
            placesList = placesRepository.getPlacesByRequestId(savedRequest.getRequestId());
        }
        else{
            placesList = placesRepository.getPlacesByRequestId(requests.getRequestId());
        }
        return placesList;
    }
}

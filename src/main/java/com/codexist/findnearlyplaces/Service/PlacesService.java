package com.codexist.findnearlyplaces.Service;


import com.codexist.findnearlyplaces.model.response.FindPlacesResponse;

public interface PlacesService {

    public FindPlacesResponse findNearlyPlaces(String longitude, String latitude, String radius );
}

package com.codexist.findnearlyplaces.controller;

import com.codexist.findnearlyplaces.Service.PlacesService;
import com.codexist.findnearlyplaces.model.response.FindPlacesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class PlacesController {
    @Autowired
    PlacesService placesService;

    @RequestMapping(value="findPlaces/{longitude}/{latitude}/{radius}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<FindPlacesResponse> getPlaces(@PathVariable("longitude") String longitude, @PathVariable("latitude") String latitude, @PathVariable("radius") String radius ){
        FindPlacesResponse findPlacesResponse = placesService.findNearlyPlaces(longitude,latitude,radius);
        return ResponseEntity.ok(findPlacesResponse);
    }

}

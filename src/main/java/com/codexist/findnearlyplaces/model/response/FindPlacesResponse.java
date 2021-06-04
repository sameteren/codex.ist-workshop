package com.codexist.findnearlyplaces.model.response;

import com.codexist.findnearlyplaces.dto.Places;
import lombok.Data;

import java.util.List;

@Data
public class FindPlacesResponse {
    private List<Places> placesList;
}

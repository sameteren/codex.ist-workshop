package com.codexist.findnearlyplaces.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;


@Data
@Document("Places")
public class Places {

    @Id
    private UUID placesId;
    private UUID requestId;
    private String name;
    private String vicinity;
    private String longitude;
    private String latitude;

}

package com.codexist.findnearlyplaces.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;


@Data
@Document("Requests")
public class Requests {



    @Id
    private UUID requestId;
    private String longitude;
    private String latitude;
    private String radius;

}

package com.insha.World.Atlas.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Country {
    @Id
    private String code; // ISO Alpha-3 code (e.g., IND, USA)
    private String name;
    private long population;
    private String region;
    private String subRegion;
    private String capital;
    private String topLevelDomain;
    private String currency;
    private String language;
    private String borders;
    private String flagUrl;
}

package ru.alexeyva.todoback.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
public class GeoIPService {

    private final RestTemplate restTemplate;
    private final Environment env;

    public GeoLocation getLocation(String ip){
        String key = env.getProperty("GEOIP_KEY");
        if (key == null) return null;

        String url = "https://suggestions.dadata.ru/suggestions/api/4_1/rs/iplocate/address?ip={ip}";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        headers.set("Authorization", "Token "+key);
        ResponseEntity<GeoLocation> response = restTemplate.exchange(url, HttpMethod.GET, entity, GeoLocation.class, ip);
        if(response.getStatusCode() != HttpStatus.OK) return null;
        return response.getBody();
    }


    @Data
    @NoArgsConstructor @AllArgsConstructor
    public static class GeoLocation {
        @JsonProperty("location")
        private Location location;
    }

    @Data
    @NoArgsConstructor @AllArgsConstructor
    public static class Location {
        @JsonProperty("value")
        private String value;
        @JsonProperty("unrestricted_value")
        private String unrestrictedValue;
        @JsonProperty("data")
        private LocationData data;
    }

    @Data
    @NoArgsConstructor @AllArgsConstructor
    public static class LocationData {
        @JsonProperty("postal_code")
        private String postalCode;
        @JsonProperty("country")
        private String country;
        @JsonProperty("country_iso_code")
        private String countryIsoCode;
        @JsonProperty("federal_district")
        private String federalDistrict;
        @JsonProperty("region_fias_id")
        private String regionFiasId;
        @JsonProperty("region_kladr_id")
        private String regionKladrId;
        @JsonProperty("region_iso_code")
        private String regionIsoCode;
        @JsonProperty("region_with_type")
        private String regionWithType;
        @JsonProperty("region_type")
        private String regionType;
        @JsonProperty("region_type_full")
        private String regionTypeFull;
        @JsonProperty("region")
        private String region;

    }
}

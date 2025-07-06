package com.insha.World.Atlas.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insha.World.Atlas.entity.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class CountryImportService {

    @Autowired
    private CountryService countryService;

    private final String URL_ALL = "https://restcountries.com/v3.1/all?fields=name,population,region,capital,flags,cca3";
    private final String URL_DETAILS = "https://restcountries.com/v3.1/name/";

    public void importCountries() {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Country> countries = new ArrayList<>();

        try {
            String response = restTemplate.getForObject(URL_ALL, String.class);
            JsonNode root = objectMapper.readTree(response);

            for (JsonNode node : root) {
                String code = node.path("cca3").asText();
                String name = node.path("name").path("common").asText();
                long population = node.path("population").asLong();
                String region = node.path("region").asText();

                // ✅ Safely extract capital
                JsonNode capitalArray = node.path("capital");
                String capital = (capitalArray.isArray() && capitalArray.size() > 0)
                        ? capitalArray.get(0).asText()
                        : "";

                String flagUrl = node.path("flags").path("png").asText();

                // Fetch full country details
                String detailUrl = URL_DETAILS + name + "?fullText=true&fields=name,population,region,subregion,capital,tld,currencies,languages,borders,flags,cca3";
                String detailResponse = restTemplate.getForObject(detailUrl, String.class);
                JsonNode detailRoot = objectMapper.readTree(detailResponse);

                if (detailRoot.isArray() && detailRoot.size() > 0) {
                    JsonNode detailNode = detailRoot.get(0);

                    String subRegion = detailNode.path("subregion").asText();

                    // ✅ Safely extract TLD
                    JsonNode tldArray = detailNode.path("tld");
                    String tld = (tldArray.isArray() && tldArray.size() > 0)
                            ? tldArray.get(0).asText()
                            : "";

                    // ✅ Extract currency code (like USD)
                    JsonNode currenciesNode = detailNode.path("currencies");
                    String currency = currenciesNode.isObject() && currenciesNode.fieldNames().hasNext()
                            ? currenciesNode.fieldNames().next()
                            : "";

                    // ✅ Extract languages
                    JsonNode languageNode = detailNode.path("languages");
                    Iterator<String> langKeys = languageNode.fieldNames();
                    StringBuilder langBuilder = new StringBuilder();
                    while (langKeys.hasNext()) {
                        String langKey = langKeys.next();
                        langBuilder.append(languageNode.path(langKey).asText());
                        if (langKeys.hasNext()) {
                            langBuilder.append(", ");
                        }
                    }

                    // ✅ Extract borders cleanly (without trailing comma)
                    JsonNode bordersNode = detailNode.path("borders");
                    StringBuilder bordersBuilder = new StringBuilder();
                    if (bordersNode.isArray()) {
                        for (int i = 0; i < bordersNode.size(); i++) {
                            bordersBuilder.append(bordersNode.get(i).asText());
                            if (i < bordersNode.size() - 1) {
                                bordersBuilder.append(", ");
                            }
                        }
                    }

                    Country country = new Country(
                            code,
                            name,
                            population,
                            region,
                            subRegion,
                            capital,
                            tld,
                            currency,
                            langBuilder.toString(),
                            bordersBuilder.toString(),
                            flagUrl
                    );

                    countries.add(country);
                }
            }

            countryService.addAll(countries);
            System.out.println("Countries imported successfully: " + countries.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

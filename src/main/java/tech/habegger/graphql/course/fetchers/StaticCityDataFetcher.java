package tech.habegger.graphql.course.fetchers;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import tech.habegger.graphql.course.model.City;
import tech.habegger.graphql.course.model.Country;
import tech.habegger.graphql.course.model.GeoCoord;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class StaticCityDataFetcher  implements DataFetcher<City> {
    private final static Map<String, City> CITIES = Map.of(
            "Paris", new City("Paris", 1000, new GeoCoord(1000, 1000), 100, "ite-de-france"),
            "Madrid", new City("Madrid", 1000, new GeoCoord(1000, 1000), 100, "Madrid"),
            "Bern", new City("Bern", 1000, new GeoCoord(1000, 1000), 100, "Bern"),
            "Washington", new City("Washington", 1000, new GeoCoord(1000, 1000), 100, "district of columbia")
    );
    @Override
    public City get(DataFetchingEnvironment environment)  throws IOException {
        Country sourceCountry = environment.getSource();
        var capitalName = sourceCountry.capital();
        return CITIES.get(capitalName);
    }
}











package tech.habegger.graphql.course.fetchers;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import tech.habegger.graphql.course.model.Country;


import java.util.List;


public class StaticCountriesDataFetcher implements DataFetcher<List<Country>> {
    private final static List<Country> COUNTRIES = List.of(
            new Country("CH", "Switzerland", 1000, 1000, "Bern"),
            new Country("US", "United States Of America", 2000, 2000, "Washington"),
            new Country("FR", "France", 3000, 3000, "Paris"),
            new Country("ES", "Spain", 4000, 4000, "Barcelona")
    );
    @Override
    public List<Country> get(DataFetchingEnvironment environment) {
    //    List<String> wantedIsoCodes = environment.getArgument("isoCodes");
    //    if(wantedIsoCodes == null) {
            return COUNTRIES;
    //    } else {
    //        return COUNTRIES.stream().filter(country -> wantedIsoCodes.contains(country.isoCode())).toList();
    //    }
    }
}

package tech.habegger.graphql.example;


import graphql.ExecutionResult;
import graphql.validation.ValidationError;
import org.junit.jupiter.api.Test;
import tech.habegger.graphql.course.GraphQLRuntime;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GraphQLExampleRuntimeTest {

    GraphQLRuntime runtime;

    GraphQLExampleRuntimeTest() throws IOException {
        runtime = new GraphQLRuntime();
    }

    @Test
    public void validQuery() {
        // Given
        String query = """
            {
                countries {
                    name
                    population
                    capital {
                        name
                        population
                    }
                }
            }
        """;

        // When
        ExecutionResult result = runtime.execute(query);

        // Then
        Map<String, Object> data = result.getData();
        assertThat(data).isNotNull();
        assertThat(data).containsKey("countries").extracting("countries").isInstanceOf(List.class);
        var countries = (List<Map<String, Object>>) data.get("countries");
        assertThat(countries).hasSize(4);
        assertThat(countries).contains(Map.of(
                "name", "Switzerland",
                "population", 1000,
                "capital", Map.of(
                        "name", "Bern",
                        "population", 1000
                )
        ));

        assertThat(result.getErrors()).isEmpty();
    }
    @Test
    public void invalidQuery() {
        // Given
        String query = """
            {
                countries {
                    latitude # does not exist in schema
                    name
                    population
                    capital {
                        name
                        population
                    }
                }
            }
        """;

        // When
        ExecutionResult result = runtime.execute(query);

        // Then
        Map<String, Object> data = result.getData();
        assertThat(data).isNotNull();
        assertThat(result.getErrors()).isNotEmpty();
        assertThat(result.getErrors().get(0)).isInstanceOf(ValidationError.class);

    }
}






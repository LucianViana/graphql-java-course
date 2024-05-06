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






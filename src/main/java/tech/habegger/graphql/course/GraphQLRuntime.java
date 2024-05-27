package tech.habegger.graphql.course;
import graphql.GraphQL;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import tech.habegger.graphql.course.fetchers.StaticCityDataFetcher;
import tech.habegger.graphql.course.fetchers.StaticCountriesDataFetcher;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class GraphQLRuntime {
    private final GraphQL graphql;

    public GraphQLRuntime() throws IOException {
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        TypeDefinitionRegistry typeRegistry = buildRegistry();
        RuntimeWiring runtimeWiring = buildWiring();
        GraphQLSchema schema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
        graphql = GraphQL.newGraphQL(schema).build();

        //graphql = null;
    }
    static TypeDefinitionRegistry buildRegistry() throws IOException {
        SchemaParser schemaParser = new SchemaParser();
        try(InputStream is = GraphQLRuntime.class.getResourceAsStream("/schema.graphqls")) {
            assert is != null;
            Reader schemaReader =  new InputStreamReader(is, StandardCharsets.UTF_8);
            return schemaParser.parse(schemaReader);
        }
    }
    public ExecutionResult execute(String query) {
        return execute(query, null, null);
    }
    static RuntimeWiring buildWiring() {
        RuntimeWiring.Builder runtimeWiring = RuntimeWiring.newRuntimeWiring()
                .type("Query", builder ->
                        builder.dataFetcher("countries", new StaticCountriesDataFetcher())
                )
                .type("Country", builder ->
                        builder.dataFetcher("capital", new StaticCityDataFetcher())
                );
//                .type("Country", builder -> builder
//                        .dataFetcher("area", new WFBFieldDataFetcher("/Geography/Area/total/text"))
//                        .dataFetcher("population", new WFBFieldDataFetcher("/People and Society/Population/text"))
//                        .dataFetcher("name", new WFBFieldDataFetcher("/Government/Country name/conventional short form/text"))
//                        .dataFetcher("capital", new WFBFieldDataFetcher("/Government/Capital/name/text"))
//                );
        return runtimeWiring.build();
    }
//}


    public ExecutionResult execute(String query, Map<String, Object> variables, String operation) {
        ExecutionInput.Builder executionInput = ExecutionInput.newExecutionInput()
                .query(query);
        if(variables != null) {
            executionInput.variables(variables);
        }
        if(operation != null) {
            executionInput.operationName(operation);
        }

        return graphql.execute(executionInput.build());

    }
}


package tech.habegger.graphql.course;
import graphql.GraphQL;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
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
            Reader reader =  new InputStreamReader(is, StandardCharsets.UTF_8);
            return schemaParser.parse(reader);
        }
    }

    static RuntimeWiring buildWiring() {
        RuntimeWiring.Builder runtimeWiring = RuntimeWiring.newRuntimeWiring()
                .type("Query", builder ->
                        builder.dataFetcher("countries", new StaticCountriesDataFetcher())
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
    public ExecutionResult execute(String query) {
        ExecutionInput executionInput = ExecutionInput.newExecutionInput()
                .query(query)
                //.variables(variables)
                .build();
        return graphql.execute(executionInput);
    }
}


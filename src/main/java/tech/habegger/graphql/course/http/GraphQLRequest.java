package tech.habegger.graphql.course.http;

import java.util.Map;

public record GraphQLRequest(String query, Map<String, Object> variables, String operationNames) {

}

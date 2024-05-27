package tech.habegger.graphql.course.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.ExecutionResult;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.io.Content;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.util.Callback;
import tech.habegger.graphql.course.GraphQLRuntime;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class GraphQLHandler extends Handler.Abstract {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final GraphQLRuntime graphqlRuntime;

    public GraphQLHandler(GraphQLRuntime graphqlRuntime) {
        this.graphqlRuntime = graphqlRuntime;
    }

    @Override
    public boolean handle(Request httpRequest, Response response, Callback callback) throws Exception {
        GraphQLRequest graphQLRequest = graphqlRequestFromHttp(httpRequest);
        ExecutionResult result = graphqlRuntime.execute(graphQLRequest.query(), graphQLRequest.variables(), graphQLRequest.operationNames());
        Content.Sink.write(response, true, MAPPER.writeValueAsString(result.toSpecification()), callback);
        return true;
    }

    private static GraphQLRequest graphqlRequestFromHttp(Request httpRequest) throws IOException {
        var contentType = httpRequest.getHeaders().get(HttpHeader.CONTENT_TYPE);
        var mimeType = MimeTypes.getBaseType(contentType);
        var charset = Optional.ofNullable(MimeTypes.getCharsetFromContentType(contentType)).orElse(StandardCharsets.UTF_8.name());

        if (mimeType == null) {
            throw new UnsupportedOperationException("Don't know how to handle %s".formatted(contentType));
        }
        InputStream is = Content.Source.asInputStream(httpRequest);
        Reader httpRequestReader = new InputStreamReader(is, charset);
        return switch (mimeType.getBaseType().asString()) {
            // Handle application/graphql
            case "application/graphql" -> unmarshalGraphQLRequest(httpRequestReader);
            // Handle application/json
            case "application/json" -> unmarshalJsonReguest(httpRequestReader);
            // Anything else -> Bad Request
            default -> throw new UnsupportedOperationException("Don't know two handle %s".formatted(contentType));
        };
    }

    private static GraphQLRequest unmarshalJsonReguest(Reader jsonContent) throws IOException {
        return MAPPER.readValue(jsonContent, GraphQLRequest.class);
    }

    private static GraphQLRequest unmarshalGraphQLRequest(Reader graphqlContent) throws IOException {
        StringWriter writer = new StringWriter();
        IOUtils.copy(graphqlContent, writer);
        return new GraphQLRequest(writer.toString(), null, null);
    }
}

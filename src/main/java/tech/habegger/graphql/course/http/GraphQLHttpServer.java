package tech.habegger.graphql.course.http;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import tech.habegger.graphql.course.GraphQLRuntime;

public class GraphQLHttpServer {
    private static final int HTTP_PORT = 8080;

    public static void main(String[] args) throws Exception {
        Server server = new Server(HTTP_PORT);
        GraphQLRuntime graphQLRuntime = new GraphQLRuntime();
        //Se tup the contexts
        ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();
        contextHandlerCollection.addHandler(new ContextHandler(new GraphiQLHandler(), "/"));
        contextHandlerCollection.addHandler(new ContextHandler(new GraphQLHandler(graphQLRuntime), "/graphql"));
        server.setHandler(contextHandlerCollection);

        // Start the HTTP server
        server.start();
        server.join();
    }
}
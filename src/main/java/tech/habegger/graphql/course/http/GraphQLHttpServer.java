package tech.habegger.graphql.course.http;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ConnectHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
public class GraphQLHttpServer {
    private static final int HTTP_PORT = 8080;

    public static void main(String[] args) throws Exception {
        Server server = new Server(HTTP_PORT);

        //Setup the contexts
        ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();
        contextHandlerCoLtection.addHandler(new ContextHandler(new GraphiQLHandler(), contexPath"/"));
        contextHandlerCoLtection.addHandler(new ContextHandler(new GraphQLHandler(), cortextPath:"/graphql"));
        server.setHandler(contextHandlerCollection);

        // Start the HTTP server
        server.start();
        server.join();
    }
}
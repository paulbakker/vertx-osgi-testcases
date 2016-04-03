package vertx.rxjava.itest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.rx.java.ObservableHandler;
import io.vertx.rx.java.RxHelper;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.http.HttpClient;
import io.vertx.rxjava.core.http.HttpClientResponse;
import io.vertx.rxjava.core.http.HttpServer;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.ext.web.RoutingContext;

@RunWith(VertxUnitRunner.class)
public class RxTest {
	private Vertx vertx;
	private Router router;

	@Before
	public void setup(TestContext context) {
		vertx = Vertx.vertx();
		HttpServer server = vertx.createHttpServer();

		router = Router.router(vertx);

		ObservableHandler<RoutingContext> observable = RxHelper.observableHandler();
		observable.subscribe(routingContext -> {
			routingContext.response().end("success!");
		});
		
		router.route("/test").handler(observable.toHandler());

		server.requestHandler(router::accept).listen(8080);
	}

	@After
	public void after(TestContext context) {
		vertx.close(context.asyncAssertSuccess());
	}
	
	@Test
	public void testVertxRxWebServer(TestContext context) {
		
		HttpClient client = vertx.createHttpClient();
	    Async async = context.async();
	    
	    ObservableHandler<HttpClientResponse> observable = RxHelper.observableHandler();
	    observable.subscribe(resp -> {
	    	resp.bodyHandler(body -> {
		        context.assertEquals("success!", body.toString());
		        client.close();
		        async.complete();
		      });
	    });
	    
	    client.getNow(8080, "localhost", "/test", observable.toHandler());
	}
}

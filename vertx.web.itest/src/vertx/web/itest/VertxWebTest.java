package vertx.web.itest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.TemplateHandler;
import io.vertx.ext.web.templ.FreeMarkerTemplateEngine;
import io.vertx.ext.web.templ.HandlebarsTemplateEngine;
import io.vertx.ext.web.templ.JadeTemplateEngine;
import io.vertx.ext.web.templ.MVELTemplateEngine;
import io.vertx.ext.web.templ.PebbleTemplateEngine;
import io.vertx.ext.web.templ.TemplateEngine;
import io.vertx.ext.web.templ.ThymeleafTemplateEngine;

@RunWith(VertxUnitRunner.class)
public class VertxWebTest {

	private Vertx vertx;
	private HttpServer server;
	private Router router;
	
	@Before
	public void setup(TestContext context) {
		vertx = Vertx.vertx();
		HttpServer server = vertx.createHttpServer();

		router = Router.router(vertx);

		router.route("/test").handler(routingContext -> {
		  HttpServerResponse response = routingContext.response();
		  response.end("success!");
		});

		server.requestHandler(router::accept).listen(8080);
	}
	
	 @After
	  public void after(TestContext context) {
	    vertx.close(context.asyncAssertSuccess());
	  }
	
	@Test
	public void testVertxWebServer(TestContext context) {
		
		HttpClient client = vertx.createHttpClient();
	    Async async = context.async();
	    client.getNow(8080, "localhost", "/test", resp -> {
	      resp.bodyHandler(body -> {
	        context.assertEquals("success!", body.toString());
	        client.close();
	        async.complete();
	      });
	    });
	}
	
	@Test
	public void testHandleBars(TestContext context) {
		TemplateEngine engine = HandlebarsTemplateEngine.create();
		router.get("/handlebars").handler(ctx -> {
			ctx.put("name","world");
			
			engine.render(ctx, "templates/test.hbs", res -> {
				
		        if (res.succeeded()) {
		          ctx.response().end(res.result());
		        } else {
		          ctx.fail(res.cause());
		        }
		      });
		});

		HttpClient client = vertx.createHttpClient();
	    Async async = context.async();
	    client.getNow(8080, "localhost", "/handlebars", resp -> {
	      resp.bodyHandler(body -> {
	        context.assertEquals("Hello world", body.toString().trim());
	        client.close();
	        async.complete();
	      });
	    });
	}
	
	@Test
	public void testFreeMarker(TestContext context) {
		TemplateEngine engine = FreeMarkerTemplateEngine.create();
		router.get("/freemarker").handler(ctx -> {
			ctx.put("name","world");
			
			engine.render(ctx, "templates/test.ftl", res -> {
				
		        if (res.succeeded()) {
		          ctx.response().end(res.result());
		        } else {
		          ctx.fail(res.cause());
		        }
		      });
		});

		HttpClient client = vertx.createHttpClient();
	    Async async = context.async();
	    client.getNow(8080, "localhost", "/freemarker", resp -> {
	      resp.bodyHandler(body -> {
	        context.assertEquals("Hello world", body.toString().trim());
	        client.close();
	        async.complete();
	      });
	    });
	}
	
	@Test
	public void testJade(TestContext context) {
		TemplateEngine engine = JadeTemplateEngine.create();
		router.get("/jade").handler(ctx -> {
			ctx.put("name","world");
			
			engine.render(ctx, "templates/test.jade", res -> {
				
		        if (res.succeeded()) {
		          ctx.response().end(res.result());
		        } else {
		          ctx.fail(res.cause());
		        }
		      });
		});

		HttpClient client = vertx.createHttpClient();
	    Async async = context.async();
	    client.getNow(8080, "localhost", "/jade", resp -> {
	      resp.bodyHandler(body -> {
	        context.assertEquals("<body>Hello world</body>", body.toString().trim());
	        client.close();
	        async.complete();
	      });
	    });
	}
	
	@Test
	public void testMvel(TestContext context) {
		TemplateEngine engine = MVELTemplateEngine.create();
		router.get("/mvel").handler(ctx -> {
			ctx.put("name","world");
			
			engine.render(ctx, "templates/test.templ", res -> {
				
		        if (res.succeeded()) {
		          ctx.response().end(res.result());
		        } else {
		          ctx.fail(res.cause());
		        }
		      });
		});

		HttpClient client = vertx.createHttpClient();
	    Async async = context.async();
	    client.getNow(8080, "localhost", "/mvel", resp -> {
	      resp.bodyHandler(body -> {
	        context.assertEquals("Hello /mvel", body.toString().trim());
	        client.close();
	        async.complete();
	      });
	    });
	}
	
	@Test
	public void testPebble(TestContext context) {
		TemplateEngine engine = PebbleTemplateEngine.create();
		router.get("/pebble").handler(ctx -> {
			ctx.put("name","world");
			
			engine.render(ctx, "templates/test", res -> {
				
		        if (res.succeeded()) {
		          ctx.response().end(res.result());
		        } else {
		          ctx.fail(res.cause());
		        }
		      });
		});

		HttpClient client = vertx.createHttpClient();
	    Async async = context.async();
	    client.getNow(8080, "localhost", "/pebble", resp -> {
	      resp.bodyHandler(body -> {
	        context.assertEquals("Hello /pebble", body.toString().trim());
	        client.close();
	        async.complete();
	      });
	    });
	}
	
	@Test
	public void testThymeleaf(TestContext context) {
		TemplateEngine engine = ThymeleafTemplateEngine.create();
		router.get("/thymeleaf").handler(ctx -> {
			ctx.put("name","world");
			
			engine.render(ctx, "templates/test.thymeleaf", res -> {
				
		        if (res.succeeded()) {
		          ctx.response().end(res.result());
		        } else {
		          ctx.fail(res.cause());
		        }
		      });
		});

		HttpClient client = vertx.createHttpClient();
	    Async async = context.async();
	    client.getNow(8080, "localhost", "/thymeleaf", resp -> {
	      resp.bodyHandler(body -> {
	        context.assertEquals("Hello <span>world</span>", body.toString().trim());
	        client.close();
	        async.complete();
	      });
	    });
	}
}

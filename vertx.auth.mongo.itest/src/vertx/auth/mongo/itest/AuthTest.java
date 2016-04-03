package vertx.auth.mongo.itest;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.mongo.MongoAuth;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class AuthTest {
	private Vertx vertx;

	protected MongoAuth authProvider;
	
	@Before
	public void setup(TestContext context) {
		vertx = Vertx.vertx();
		
		JsonObject mongoconfig = new JsonObject()
		        .put("db_name", "vertx-test");		
		MongoClient client = MongoClient.createShared(vertx, mongoconfig);
		JsonObject authProperties = new JsonObject();
		authProvider = MongoAuth.create(client, authProperties);
	}

	protected JsonObject getConfig() {
		return new JsonObject().put("keyStore",
				new JsonObject().put("path", "keystore.jceks").put("type", "jceks").put("password", "secret"));
	}

	@After
	public void after(TestContext context) {
		vertx.close(context.asyncAssertSuccess());
	}
	
	@Test
	public void testAuth(TestContext context) {
		Async async = context.async();
		
		JsonObject authInfo = new JsonObject()
			    .put("username", "tim")
			    .put("password", "sausages");
			authProvider.authenticate(authInfo, res -> {
			  if (res.succeeded()) {
				  fail("No users should be available");
				  async.complete();
			  } else {
				  async.complete();
			  }
			});
			
			async.await();
	}
}

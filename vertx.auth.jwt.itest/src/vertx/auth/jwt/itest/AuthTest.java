package vertx.auth.jwt.itest;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class AuthTest {

	private Vertx vertx;

	protected JWTAuth authProvider;

	// {"sub":"Paulo","iat":1431695313,"exp":1747055313,"roles":["admin","developer","user"],"permissions":["read","write","execute"]}
	private static final String JWT_VALID = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJQYXVsbyIsImlhdCI6MTQzMTY5NTMxMywiZXhwIjoxNzQ3MDU1MzEzLCJyb2xlcyI6WyJhZG1pbiIsImRldmVsb3BlciIsInVzZXIiXSwicGVybWlzc2lvbnMiOlsicmVhZCIsIndyaXRlIiwiZXhlY3V0ZSJdfQ==.D6FLewkLz4lmCsUYLQS82x6QMjgSaMg0ROYXiKXorgo=";

	@Before
	public void setup(TestContext context) {
		vertx = Vertx.vertx();
		authProvider = JWTAuth.create(vertx, getConfig());
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
	public void testValidJWT(TestContext context) {
		Async async = context.async();
		JsonObject authInfo = new JsonObject().put("jwt", JWT_VALID);
		authProvider.authenticate(authInfo, res -> {
			assertNotNull(res);
			async.complete();
		});
		
		async.await();
	}
}

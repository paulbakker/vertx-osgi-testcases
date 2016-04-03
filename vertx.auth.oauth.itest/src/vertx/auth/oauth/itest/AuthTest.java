package vertx.auth.oauth.itest;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.oauth2.AccessToken;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.auth.oauth2.OAuth2ClientOptions;
import io.vertx.ext.auth.oauth2.OAuth2FlowType;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class AuthTest {
	private Vertx vertx;

	@Before
	public void setup(TestContext context) {
		vertx = Vertx.vertx();

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

		OAuth2ClientOptions oAuth2ClientOptions = new OAuth2ClientOptions();
		oAuth2ClientOptions.setClientID("client_id");
		oAuth2ClientOptions.setClientSecret("client_secret");
		oAuth2ClientOptions.setSite("https://github.com/login");
		oAuth2ClientOptions.setTokenPath("/oauth/access_token");
		oAuth2ClientOptions.setAuthorizationPath("/oauth/authorize");

		OAuth2Auth oauth2 = OAuth2Auth.create(vertx, OAuth2FlowType.PASSWORD, oAuth2ClientOptions);
		JsonObject tokenConfig = new JsonObject().put("username", "user").put("password", "pass");

		oauth2.getToken(tokenConfig, res -> {
			//This is an invalid login, but it's enough to test the OSGi integration
			async.complete();			
		});

		async.await();

	}
}

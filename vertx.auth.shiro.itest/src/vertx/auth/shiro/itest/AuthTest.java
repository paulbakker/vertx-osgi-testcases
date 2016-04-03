package vertx.auth.shiro.itest;

import static org.junit.Assert.*;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.shiro.ShiroAuth;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class AuthTest {

	private Vertx vertx;

	protected ShiroAuth authProvider;

	@Before
	public void setup(TestContext context) {
		vertx = Vertx.vertx();
		Realm realm = new MyShiroRealm();
		authProvider = ShiroAuth.create(vertx, realm);
		
	}

	@Test
	public void testShiro(TestContext context) {
		JsonObject authInfo = new JsonObject().put("username", "tim").put("password", "sausages");
		Async async = context.async();

		authProvider.authenticate(authInfo, res -> {
			if (res.succeeded()) {
				User user = res.result();
				assertEquals("tim", user.principal().getString("username"));
			} else {
				fail("Could not login");
			}

			async.complete();
		});

		async.await();
	}

	class MyShiroRealm implements Realm {

		@Override
		public String getName() {
			return getClass().getName();
		}

		@Override
		public boolean supports(AuthenticationToken token) {
			return true;
		}

		@Override
		public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

			return new AuthenticationInfo() {
				@Override
				public PrincipalCollection getPrincipals() {
					return new SimplePrincipalCollection(token.getPrincipal(), getClass().getName());
				}

				@Override
				public Object getCredentials() {
					return token.getCredentials();
				}
			};
		}

	}
}

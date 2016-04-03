package vertx.redis.itest;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;

@RunWith(VertxUnitRunner.class)
public class RedisTest {

	private Vertx vertx;

	@Before
	public void setup(TestContext context) {
		vertx = Vertx.vertx();
	}

	@After
	public void after(TestContext context) {
		vertx.close(context.asyncAssertSuccess());
	}
	
	
	@Test
	public void testRedis(TestContext context) {
		Async async = context.async();
		RedisOptions config = new RedisOptions().setHost("192.168.99.100");
		RedisClient redis = RedisClient.create(vertx, config);
		
		redis.set("mykey", "test", r -> {
			redis.get("mykey", r2 -> {
				assertEquals("test", r2.result());
				async.complete();
			});
		});
		
		async.await();
	}
}

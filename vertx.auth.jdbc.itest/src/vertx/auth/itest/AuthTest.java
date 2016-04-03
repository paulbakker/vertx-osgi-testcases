package vertx.auth.itest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jdbc.JDBCAuth;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class AuthTest  {

	private Vertx vertx;

	static final List<String> SQL = new ArrayList<>();

	static {
		SQL.add("drop table if exists user;");
		SQL.add("drop table if exists user_roles;");
		SQL.add("drop table if exists roles_perms;");
		SQL.add("create table user (username varchar(255), password varchar(255), password_salt varchar(255) );");
		SQL.add("create table user_roles (username varchar(255), role varchar(255));");
		SQL.add("create table roles_perms (role varchar(255), perm varchar(255));");

		SQL.add("insert into user values ('tim', 'EC0D6302E35B7E792DF9DA4A5FE0DB3B90FCAB65A6215215771BF96D498A01DA8234769E1CE8269A105E9112F374FDAB2158E7DA58CDC1348A732351C38E12A0', 'C59EB438D1E24CACA2B1A48BC129348589D49303858E493FBE906A9158B7D5DC');");
		SQL.add("insert into user_roles values ('tim', 'dev');");
		SQL.add("insert into user_roles values ('tim', 'admin');");
		SQL.add("insert into roles_perms values ('dev', 'commit_code');");
		SQL.add("insert into roles_perms values ('dev', 'eat_pizza');");
		SQL.add("insert into roles_perms values ('admin', 'merge_pr');");

		// and a second set of tables with slight differences

		SQL.add("drop table if exists user2;");
		SQL.add("drop table if exists user_roles2;");
		SQL.add("drop table if exists roles_perms2;");
		SQL.add("create table user2 (user_name varchar(255), pwd varchar(255), pwd_salt varchar(255) );");
		SQL.add("create table user_roles2 (user_name varchar(255), role varchar(255));");
		SQL.add("create table roles_perms2 (role varchar(255), perm varchar(255));");

		SQL.add("insert into user2 values ('tim', 'EC0D6302E35B7E792DF9DA4A5FE0DB3B90FCAB65A6215215771BF96D498A01DA8234769E1CE8269A105E9112F374FDAB2158E7DA58CDC1348A732351C38E12A0', 'C59EB438D1E24CACA2B1A48BC129348589D49303858E493FBE906A9158B7D5DC');");
		SQL.add("insert into user_roles2 values ('tim', 'dev');");
		SQL.add("insert into user_roles2 values ('tim', 'admin');");
		SQL.add("insert into roles_perms2 values ('dev', 'commit_code');");
		SQL.add("insert into roles_perms2 values ('dev', 'eat_pizza');");
		SQL.add("insert into roles_perms2 values ('admin', 'merge_pr');");

	}

	@BeforeClass
	public static void createDb() throws Exception {
		Class.forName("org.hsqldb.jdbcDriver");
		
		Connection conn = DriverManager.getConnection(config().getString("url"));
		for (String sql : SQL) {
			System.out.println("Executing: " + sql);
			conn.createStatement().execute(sql);
		}
	}
	
	@Before
	public void setup(TestContext context) {
		vertx = Vertx.vertx();
		
		
	}

	@After
	public void after(TestContext context) {
		vertx.close(context.asyncAssertSuccess());
	}

	@Test
	public void testJdbcAuth(TestContext context) {
		Async async = context.async();
		
		JDBCClient client = JDBCClient.createNonShared(vertx, config());
		JDBCAuth authProvider = JDBCAuth.create(client);
		
		JsonObject authInfo = new JsonObject();
	    authInfo.put("username", "tim").put("password", "sausages");
    	authProvider.authenticate(authInfo, res -> {
    		async.complete();
    	});
    	
    	async.await();
	}

	protected static JsonObject config() {
		return new JsonObject().put("url", "jdbc:hsqldb:mem:test?shutdown=true").put("driver_class",
				"org.hsqldb.jdbcDriver");
	}
}

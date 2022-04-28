/**
MIT License

Copyright (c) 2021 Surati

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package io.surati.gap.payment.module.server;

import com.baudoliver7.jdbc.toolset.lockable.LocalLockedDataSource;
import com.minlessika.utils.ConsoleArgs;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.surati.gap.admin.module.AdminModule;
import io.surati.gap.payment.base.db.PaymentBaseDemoDatabase;
import io.surati.gap.payment.base.module.PaymentBaseModule;
import io.surati.gap.payment.module.PaymentModule;
import io.surati.gap.web.base.FkMimes;
import io.surati.gap.web.base.TkSafe;
import io.surati.gap.web.base.TkSafeUserAlert;
import io.surati.gap.web.base.TkTransaction;
import io.surati.gap.web.base.auth.SCodec;
import io.surati.gap.web.base.auth.TkAuth;
import java.sql.Connection;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.takes.facets.auth.Pass;
import org.takes.facets.auth.PsByFlag;
import org.takes.facets.auth.PsChain;
import org.takes.facets.auth.PsCookie;
import org.takes.facets.auth.PsLogout;
import org.takes.facets.flash.TkFlash;
import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.TkFork;
import org.takes.facets.forward.TkForward;
import org.takes.http.Exit;
import org.takes.http.FtCli;
import org.takes.tk.TkSlf4j;

/**
 * Entry of application.
 * 
 * @since 0.1
 */
public final class Main {

	/**
	 * Local connection.
	 */
	private static final ThreadLocal<Connection> localconn = new ThreadLocal<>();

	/**
	 * App entry
	 * @param args Arguments
	 * @throws Exception If some problems in
	 */
	public static void main(String[] args) throws Exception {
		final Map<String, String> map = new ConsoleArgs("--", args).asMap();
		final HikariConfig config = new HikariConfig();
		config.setDriverClassName(map.get("db-driver"));
		config.setJdbcUrl(map.get("db-url"));
		config.setUsername(map.get("db-user"));
		config.setPassword(map.get("db-password"));
		int psize = 5;
		if(StringUtils.isNotBlank(map.get("db-pool-size"))) {
			psize = Integer.parseInt(map.get("db-pool-size"));
		}
		config.setMaximumPoolSize(psize);
		final DataSource src = new PaymentBaseDemoDatabase(
			new HikariDataSource(config)
		);
		final DataSource lcksrc = new LocalLockedDataSource(src, Main.localconn);
		AdminModule.setup();
		PaymentBaseModule.setup();
		PaymentModule.setup();
		final Pass pass = new PsChain(
			new PsByFlag(
				new PsByFlag.Pair(
					PsLogout.class.getSimpleName(),
					new PsLogout()
				)
			),
			new PsCookie(
				new SCodec()
			)
		);
		new FtCli(
			new TkSlf4j(
				new TkSafe(
					new TkForward(
						new TkFlash(
							new TkAuth(
								new TkSafeUserAlert(
									src,
									new TkTransaction(
										new TkFork(
											new FkMimes(),
											new FkRegex("/robots\\.txt", ""),
											new FkActions(lcksrc),
											new FkPages(lcksrc),
											new FkApi(lcksrc),
											new io.surati.gap.payment.base.module.server.FkActions(lcksrc),
											new io.surati.gap.payment.base.module.server.FkPages(lcksrc),
											new io.surati.gap.payment.base.module.server.FkApi(lcksrc),
											new io.surati.gap.admin.module.web.server.FkActions(lcksrc, pass),
											new io.surati.gap.admin.module.web.server.FkPages(lcksrc),
											new io.surati.gap.admin.module.web.server.FkApi(lcksrc)
										),
										Main.localconn
									)
								),
								pass
							)
						)
					)
				)
			),
			args
		).start(Exit.NEVER);	
	}
    
}

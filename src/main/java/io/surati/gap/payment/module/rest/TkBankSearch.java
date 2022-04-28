package io.surati.gap.payment.module.rest;

import io.surati.gap.payment.base.api.Banks;
import io.surati.gap.payment.base.db.DbPaginedBanks;
import io.surati.gap.payment.module.xe.XeBanksJson;
import javax.sql.DataSource;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rq.RqHref.Smart;
import org.takes.rs.RsJson;

public final class TkBankSearch implements Take {

	/**
	 * Database
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkBankSearch(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public Response act(Request req) throws Exception {	
		final Smart params = new Smart(req);
		final String filter = params.single("filter", "");					
		final Long page = Long.parseLong(params.single("page"));
		final Long nbperpage = Long.parseLong(params.single("nbperpage"));
		final Banks banks = new DbPaginedBanks(this.source, nbperpage, page, filter);
		return new RsJson(
			new XeBanksJson(banks.iterate(), banks.count())
		);
	}
}

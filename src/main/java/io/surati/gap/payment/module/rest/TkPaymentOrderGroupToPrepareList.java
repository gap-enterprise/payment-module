package io.surati.gap.payment.module.rest;

import io.surati.gap.admin.base.api.User;
import io.surati.gap.payment.base.api.Workspace;
import io.surati.gap.payment.base.db.DbWorkspace;
import io.surati.gap.payment.module.xe.XePaymentOrderGroupsJson;
import io.surati.gap.web.base.rq.RqUser;
import javax.sql.DataSource;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rs.RsJson;

public final class TkPaymentOrderGroupToPrepareList implements Take {

	/**
	 * Database
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkPaymentOrderGroupToPrepareList(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public Response act(Request req) throws Exception {	
		final User user = new RqUser(this.source, req);
		final Workspace workspace = new DbWorkspace(this.source, user);
		return new RsJson(
			new XePaymentOrderGroupsJson(
				workspace.ordersToPrepare().iterate(),
				workspace.ordersToPrepare().totalAmount()
			)
		);
	}

}

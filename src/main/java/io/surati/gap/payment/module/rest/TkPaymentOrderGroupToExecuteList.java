package io.surati.gap.payment.module.rest;

import io.surati.gap.admin.base.api.User;
import io.surati.gap.payment.base.api.PaymentBatches;
import io.surati.gap.payment.base.api.Workspace;
import io.surati.gap.payment.base.db.DbWorkspace;
import io.surati.gap.payment.base.impl.PaymentBatchesImpl;
import io.surati.gap.payment.module.xe.XePaymentBatchesJson;
import io.surati.gap.web.base.rq.RqUser;
import javax.sql.DataSource;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rs.RsJson;

public final class TkPaymentOrderGroupToExecuteList implements Take {

	/**
	 * Database
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkPaymentOrderGroupToExecuteList(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public Response act(Request req) throws Exception {	
		final User user = new RqUser(this.source, req);
		final Workspace workspace = new DbWorkspace(this.source, user);
		final PaymentBatches batches = new PaymentBatchesImpl(workspace.ordersToExecute());
		return new RsJson(
			new XePaymentBatchesJson(
				batches.iterate(),
				batches.totalAmount()
			)
		);
	}

}

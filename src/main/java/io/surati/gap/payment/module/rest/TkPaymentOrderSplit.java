package io.surati.gap.payment.module.rest;

import io.surati.gap.admin.base.api.Log;
import io.surati.gap.admin.base.api.User;
import io.surati.gap.payment.base.api.PaymentOrder;
import io.surati.gap.payment.base.api.PaymentOrderGroup;
import io.surati.gap.payment.base.api.Workspace;
import io.surati.gap.payment.base.db.DbWorkspace;
import io.surati.gap.payment.module.xe.XePaymentOrderGroupJson;
import io.surati.gap.web.base.log.RqLog;
import io.surati.gap.web.base.rq.RqUser;
import java.net.HttpURLConnection;
import javax.json.Json;
import javax.sql.DataSource;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rq.RqGreedy;
import org.takes.rq.form.RqFormSmart;
import org.takes.rs.RsJson;
import org.takes.rs.RsWithStatus;

public final class TkPaymentOrderSplit implements Take {

	/**
	 * Database
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkPaymentOrderSplit(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public Response act(Request req) throws Exception {	
		try {
			final Log log = new RqLog(this.source, req);
			final RqFormSmart form = new RqFormSmart(new RqGreedy(req));
			final Long id = Long.parseLong(form.single("id"));
			final Long groupid = Long.parseLong(form.single("group_id"));
			final double firstamount = Double.parseDouble(form.single("first_amount"));			
			final User user = new RqUser(this.source, req);
			final Workspace workspace = new DbWorkspace(this.source, user);
			final PaymentOrderGroup group = workspace.ordersToPrepare().get(groupid);
			final PaymentOrder order = group.get(id);
			final PaymentOrder secondorder = order.split(firstamount, user);
			workspace.ordersToPrepare().merge(group, secondorder);
			log.info("Scission de l'ordre de paiement (Référence=%s)", order.reference());
			return new RsJson(
					new XePaymentOrderGroupJson(group)
				);
		} catch(IllegalArgumentException ex) {
			return new RsWithStatus(
				new RsJson(
					Json.createObjectBuilder()
						.add("message", ex.getLocalizedMessage())
						.build()
					),
				HttpURLConnection.HTTP_BAD_REQUEST
			);
		} catch(Exception ex) {
			return new RsWithStatus(
				new RsJson(
					Json.createObjectBuilder()
					.add("message", ex.getLocalizedMessage())
					.build()
				),
				HttpURLConnection.HTTP_INTERNAL_ERROR
			);
		}
	}

}

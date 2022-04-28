package io.surati.gap.payment.module.rest;

import io.surati.gap.admin.base.api.Log;
import io.surati.gap.admin.base.api.User;
import io.surati.gap.payment.base.api.PaymentOrder;
import io.surati.gap.payment.base.api.PaymentOrderGroup;
import io.surati.gap.payment.base.api.PaymentOrderGroupsToPrepare;
import io.surati.gap.payment.base.api.PaymentOrders;
import io.surati.gap.payment.base.api.ThirdParty;
import io.surati.gap.payment.base.api.Workspace;
import io.surati.gap.payment.base.db.DbPaginedPaymentOrders;
import io.surati.gap.payment.base.db.DbThirdParties;
import io.surati.gap.payment.base.db.DbWorkspace;
import io.surati.gap.payment.module.xe.XePaymentOrderGroupsJson;
import io.surati.gap.web.base.log.RqLog;
import io.surati.gap.web.base.rq.RqUser;
import java.net.HttpURLConnection;
import java.util.LinkedList;
import java.util.List;
import javax.json.Json;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.cactoos.list.ListOf;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rq.RqGreedy;
import org.takes.rq.form.RqFormSmart;
import org.takes.rs.RsJson;
import org.takes.rs.RsWithStatus;

public final class TkPaymentOrderMergeAcross implements Take {

	/**
	 * Database
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkPaymentOrderMergeAcross(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public Response act(Request req) throws Exception {	
		try {
			final Log log = new RqLog(this.source, req);
			final RqFormSmart form = new RqFormSmart(new RqGreedy(req));
			final Long tpid = Long.parseLong(form.single("beneficiary_id"));
			final ThirdParty beneficiary = new DbThirdParties(this.source).get(tpid);
			final List<String> poids = new ListOf<>(form.param("poids[]"));
			if(poids.isEmpty()) {
				throw new IllegalArgumentException("Il n'y a aucun ordre de paiement à regrouper !");
			}
			final PaymentOrders allorders = new DbPaginedPaymentOrders(this.source);
			final List<PaymentOrder> orderstomerge = new LinkedList<>();
			StringBuilder builder = new StringBuilder();
			for (String idstr : poids) {
				final Long id = Long.parseLong(idstr);
				final PaymentOrder order = allorders.get(id);
				orderstomerge.add(order);
				builder.append(order.reference()).append(",");
			}
			final User user = new RqUser(this.source, req);
			final Workspace workspace = new DbWorkspace(this.source, user);
			final PaymentOrderGroupsToPrepare groups = workspace.ordersToPrepare();
			final PaymentOrderGroup newgroup = groups.mergeAcross(beneficiary, orderstomerge);
			log.info(
				StringUtils.abbreviate(
					String.format("Regroupement hétérogène d'ordres de paiement (%s)", builder.toString()),
					175
				)
			);
			return new RsJson(
				Json.createObjectBuilder(
						new XePaymentOrderGroupsJson(
							groups.iterate(),
							groups.totalAmount()
						).toJson().asJsonObject()
					)
				    .add("target_id", newgroup.id())
				    .build()
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

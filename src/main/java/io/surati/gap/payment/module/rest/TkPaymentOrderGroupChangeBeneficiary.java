package io.surati.gap.payment.module.rest;

import io.surati.gap.admin.base.api.Log;
import io.surati.gap.admin.base.api.User;
import io.surati.gap.payment.base.api.PaymentOrderGroup;
import io.surati.gap.payment.base.api.PaymentOrderGroupsToPrepare;
import io.surati.gap.payment.base.api.ThirdParty;
import io.surati.gap.payment.base.api.Workspace;
import io.surati.gap.payment.base.db.DbThirdParties;
import io.surati.gap.payment.base.db.DbWorkspace;
import io.surati.gap.payment.module.xe.XePaymentOrderGroupsJson;
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

public final class TkPaymentOrderGroupChangeBeneficiary implements Take {

	/**
	 * Database
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkPaymentOrderGroupChangeBeneficiary(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public Response act(Request req) throws Exception {	
		try {
			final Log log = new RqLog(this.source, req);
			final RqFormSmart form = new RqFormSmart(new RqGreedy(req));
			final Long groupid = Long.parseLong(form.single("group_id"));
			final User user = new RqUser(this.source, req);
			final Workspace workspace = new DbWorkspace(this.source, user);
			final PaymentOrderGroupsToPrepare groups = workspace.ordersToPrepare();
			final PaymentOrderGroup group = groups.get(groupid);
			final Long tpid = Long.parseLong(form.single("beneficiary_id"));
			final ThirdParty beneficiary = new DbThirdParties(this.source).get(tpid);
			group.changeBeneficiary(beneficiary);
			log.info(
				"Changement du bénéficiaire du groupe d'ordres par %s.",
				beneficiary.abbreviated()
			);
			return new RsJson(
				new XePaymentOrderGroupsJson(
					groups.iterate(),
					groups.totalAmount()
				)
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

package io.surati.gap.payment.module.rest;

import io.surati.gap.admin.base.api.Log;
import io.surati.gap.admin.base.api.User;
import io.surati.gap.commons.utils.amount.FrAmountInXof;
import io.surati.gap.payment.base.api.PaymentOrderGroup;
import io.surati.gap.payment.base.api.PaymentOrderGroupsToPrepare;
import io.surati.gap.payment.base.api.Workspace;
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

public final class TkPaymentOrderGroupValidate implements Take {

	/**
	 * Database
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkPaymentOrderGroupValidate(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public Response act(Request req) throws Exception {	
		try {
			final Log log = new RqLog(this.source, req);
			final RqFormSmart form = new RqFormSmart(new RqGreedy(req));
			final Long id = Long.parseLong(form.single("id", "O"));
			final User user = new RqUser(this.source, req);
			final Workspace workspace = new DbWorkspace(this.source, user);
			final PaymentOrderGroupsToPrepare groups = workspace.ordersToPrepare();
			if(id.equals(0L)) {
				groups.validate();
				for (PaymentOrderGroup group : groups.iterate()) {

					log.info("Validation du groupe (ID=%s, Bénéficiaire=%s, Montant=%s)", group.id(), group.beneficiary().abbreviated(), new FrAmountInXof(group.totalAmount()));
				}
			} else {
				final PaymentOrderGroup group = groups.get(id);
				group.validate(user);
				log.info("Validation du groupe (ID=%s, Bénéficiaire=%s, Montant=%s)", group.id(), group.beneficiary().abbreviated(), new FrAmountInXof(group.totalAmount()));
			}	
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

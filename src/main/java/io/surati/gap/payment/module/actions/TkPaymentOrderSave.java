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
package io.surati.gap.payment.module.actions;

import io.surati.gap.admin.base.api.Log;
import io.surati.gap.admin.base.api.User;
import io.surati.gap.payment.base.api.PaymentOrder;
import io.surati.gap.payment.base.api.ThirdParty;
import io.surati.gap.payment.base.api.ThirdPartyPaymentOrders;
import io.surati.gap.payment.base.api.Workspace;
import io.surati.gap.payment.base.db.DbPaginedPaymentOrders;
import io.surati.gap.payment.base.db.DbThirdParties;
import io.surati.gap.payment.base.db.DbThirdPartyPaymentOrders;
import io.surati.gap.payment.base.db.DbWorkspace;
import io.surati.gap.web.base.log.RqLog;
import io.surati.gap.web.base.rq.RootPageFull;
import io.surati.gap.web.base.rq.RqUser;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.cactoos.list.ListOf;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.flash.RsFlash;
import org.takes.facets.forward.RsForward;
import org.takes.rq.RqGreedy;
import org.takes.rq.form.RqFormSmart;

/**
 * Take that creates or modifies a payment order.
 *
 * <p>The class is immutable and thread-safe.</p>
 *
 * @since 0.1.0
 */


public final class TkPaymentOrderSave implements Take {

	/**
	 * Database
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkPaymentOrderSave(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public Response act(Request req) throws Exception {
		final Log log = new RqLog(this.source, req);
		final RqGreedy rqg = new RqGreedy(req);
		final RqFormSmart form = new RqFormSmart(rqg);
		final Long id = Long.parseLong(form.single("id", "0"));
		final Long beneficiaryid = Long.parseLong(form.single("beneficiary_id"));
		final double amounttopay = Double.parseDouble(form.single("amount_to_pay"));
		final String reason = form.single("reason");
		final String description = form.single("description", "");
		final ThirdParty beneficiary = new DbThirdParties(this.source).get(beneficiaryid);
		final String msg;
		final PaymentOrder item;
		if(id.equals(0L)) {
			final ThirdPartyPaymentOrders items = new DbThirdPartyPaymentOrders(this.source, beneficiary);
			final User user = new RqUser(this.source, req);
			final Workspace workspace = new DbWorkspace(this.source, user);
			item = items.add(amounttopay, reason, description, user);
			workspace.ordersToPrepare().merge(new ListOf<>(item));
			msg = "L'ordre de paiement a été créé avec succès !";
			log.info("Ajout de l'ordre de paiement (%s)", item.reference());
		} else {
			item = new DbPaginedPaymentOrders(this.source).get(id);
			msg = "L'ordre de paiement été modifié avec succès !";
			log.info("Mise à jour de l'ordre de paiement (%s)", item.reference());
		}
		item.update(item.date(), beneficiary, amounttopay, reason, description);
		return new RsForward(
			new RsFlash(
				msg,
				Level.INFO
			),
			String.format(
				"/payment-order/view?id=%s&%s",
				item.id(),
				new RootPageFull(req)
			)
		);	
	}
}

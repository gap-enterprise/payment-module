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
import io.surati.gap.payment.base.api.PaymentMeanType;
import io.surati.gap.payment.base.api.ThirdParties;
import io.surati.gap.payment.base.api.ThirdParty;
import io.surati.gap.payment.base.db.DbThirdParties;
import io.surati.gap.web.base.log.RqLog;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.flash.RsFlash;
import org.takes.facets.forward.RsForward;
import org.takes.rq.RqGreedy;
import org.takes.rq.form.RqFormSmart;

/**
 * Take that creates or modifies a payment condition of a third party.
 *
 * <p>The class is immutable and thread-safe.</p>
 *
 * @since 0.3
 */


public final class TkThirdPartyPaymentMeanTypeAllowedAdd implements Take {

	/**
	 * Database
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkThirdPartyPaymentMeanTypeAllowedAdd(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public Response act(Request req) throws Exception {		
		final Log log = new RqLog(this.source, req);
		final RqFormSmart form = new RqFormSmart(new RqGreedy(req));
		final String meantypeid = form.single("mean_type_id");
		final PaymentMeanType meantype = PaymentMeanType.valueOf(meantypeid);
		final Long tpid = Long.parseLong(form.single("tp_id"));
		final ThirdParties tps = new DbThirdParties(this.source);
		final ThirdParty tp = tps.get(tpid);
		tp.paymentCondition().add(meantype);
		final String msg = "Le moyen de paiement a été autorisé au tiers avec succès !";
		log.info("Autorisation du moyen de paiement (%s) au tiers (%s).", meantype.name(), tp.toString());
		return new RsForward(
			new RsFlash(
				msg,
				Level.INFO
			),
			String.format(
				"/third-party/view?id=%s",
				tp.id()
			)
		);	
	}
}

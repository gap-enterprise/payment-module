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
import org.takes.rq.RqHref;

/**
 * Take that deletes a payment condition of a third party.
 *
 * <p>The class is immutable and thread-safe.</p>
 *
 * @since 0.3
 */


public final class TkThirdPartyPaymentMeanTypeAllowedDelete implements Take {
	
	/**
	 * Database
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkThirdPartyPaymentMeanTypeAllowedDelete(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public Response act(Request req) throws Exception {
		final Log log = new RqLog(this.source, req);
		final String meantypeid = new RqHref.Smart(req).single("meantype");
		final PaymentMeanType meantype = PaymentMeanType.valueOf(meantypeid);
		final Long tpid = Long.parseLong(new RqHref.Smart(req).single("tp"));
		final ThirdParty tp = new DbThirdParties(this.source).get(tpid);
		tp.paymentCondition().remove(meantype);
		log.info("Retrait moyen de paiement (%s) du tiers (%s).", meantype.toString(), tp.toString());
		return new RsForward(
			new RsFlash(
				"Le moyen de paiement a été retiré du tiers avec succès !",
				Level.INFO
			),
			String.format("/third-party/view?id=%s", tp.id())
		);	
	}
}

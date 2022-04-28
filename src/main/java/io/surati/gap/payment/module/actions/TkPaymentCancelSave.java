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
import io.surati.gap.payment.base.api.BankNote;
import io.surati.gap.payment.base.api.PaymentCancelReason;
import io.surati.gap.payment.base.db.DbBankNotes;
import io.surati.gap.web.base.log.RqLog;
import io.surati.gap.web.base.rq.RootPageFull;
import io.surati.gap.web.base.rq.RqUser;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
 * Take that cancels a payment.
 *
 * <p>The class is immutable and thread-safe.</p>
 *
 * @since 0.1.0
 */


public final class TkPaymentCancelSave implements Take {

	/**
	 * Database
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkPaymentCancelSave(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public Response act(Request req) throws Exception {
		final Log log = new RqLog(this.source, req);
		final RqFormSmart form = new RqFormSmart(new RqGreedy(req));
		final Long id = Long.parseLong(form.single("id"));
		final PaymentCancelReason reason = PaymentCancelReason.valueOf(form.single("reason_id"));
		final String description = form.single("description");
		final BankNote item = new DbBankNotes(this.source).get(id);
		final boolean sendbackinpayment = Boolean.parseBoolean(form.single("sendbackinpayment"));
		final LocalDate canceldate = LocalDate.parse(form.single("cancel_date"),
			DateTimeFormatter.ISO_DATE
		);
		final LocalDateTime canceldatetime;
		if (canceldate.isEqual(LocalDate.now())) {
			canceldatetime = canceldate.atTime(LocalTime.now());
		} else {
			canceldatetime = canceldate.atStartOfDay();
		}
	    item.cancel(canceldatetime, reason, description, sendbackinpayment, new RqUser(this.source, req));
	    log.info("Annulation du paiement (REF=%s)", item.internalReference());
		return new RsForward(
			new RsFlash(
				"Le paiement a été annulé avec succès !",
				Level.INFO
			),
			String.format("/payment/view?id=%s&%s", item.id(), new RootPageFull(req))
		);	
	}
}

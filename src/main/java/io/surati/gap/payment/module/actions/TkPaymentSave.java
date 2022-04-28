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
import io.surati.gap.payment.base.api.BankNote;
import io.surati.gap.payment.base.api.BankNoteBook;
import io.surati.gap.payment.base.api.PaymentMeanType;
import io.surati.gap.payment.base.api.PaymentOrderGroup;
import io.surati.gap.payment.base.api.Workspace;
import io.surati.gap.payment.base.db.DbBankNoteBooks;
import io.surati.gap.payment.base.db.DbBankNotePen;
import io.surati.gap.payment.base.db.DbWorkspace;
import io.surati.gap.web.base.log.RqLog;
import io.surati.gap.web.base.rq.RootPageFull;
import io.surati.gap.web.base.rq.RqUser;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.flash.RsFlash;
import org.takes.facets.forward.RsForward;
import org.takes.rq.RqGreedy;
import org.takes.rq.form.RqFormSmart;

/**
 * Take that saves a payment.
 *
 * <p>The class is immutable and thread-safe.</p>
 *
 * @since 0.1.0
 */


public final class TkPaymentSave implements Take {

	/**
	 * Database
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkPaymentSave(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public synchronized Response act(Request req) throws Exception {
		final Log log = new RqLog(this.source, req);
		final RqFormSmart form = new RqFormSmart(new RqGreedy(req));
		final Long groupid = Long.parseLong(form.single("group_id"));
		final User user = new RqUser(this.source, req);
		final Workspace workspace = new DbWorkspace(this.source, user);
		final PaymentOrderGroup group = workspace.ordersToExecute().get(groupid);
		final Long bookid = Long.parseLong(form.single("book_id"));
		final BankNoteBook book = new DbBankNoteBooks(this.source).get(bookid);
		final LocalDate date = LocalDate.parse(form.single("date"), DateTimeFormatter.ISO_DATE);
		LocalDate echeancedate = null;
		if(book.meanType() != PaymentMeanType.CHQ) {
			final String duedatestr = form.single("echeance_date", "");
			if(StringUtils.isBlank(duedatestr)) {
				throw new IllegalArgumentException("Vous devez renseigner le date d'échéance !");
			}
			echeancedate = LocalDate.parse(duedatestr, DateTimeFormatter.ISO_DATE);
		}
		final BankNote item = new DbBankNotePen(
			this.source,
			book,
			group,
			date,
			StringUtils.EMPTY,
			StringUtils.EMPTY,
			echeancedate,
			new RqUser(this.source, req)
		).write();
		log.info("Paiement (REF=%s) par %s (%s)", item.internalReference(), item.name(), item.book().name());
		return new RsForward(
			new RsFlash(
				"Le paiement a été effectué avec succès !",
				Level.INFO
			),
			String.format("/payment/view?id=%s&%s", item.id(), new RootPageFull(req))
		);	
	}
}

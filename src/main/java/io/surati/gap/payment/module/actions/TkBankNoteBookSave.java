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
import io.surati.gap.payment.base.api.BankAccount;
import io.surati.gap.payment.base.api.BankNoteBook;
import io.surati.gap.payment.base.api.BankNoteBookStatus;
import io.surati.gap.payment.base.api.PaymentMeanType;
import io.surati.gap.payment.base.db.DbBankNoteBooks;
import io.surati.gap.payment.base.db.DbCompanyBankAccounts;
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
 * Take that creates or modifies a bank note book.
 *
 * <p>The class is immutable and thread-safe.</p>
 *
 * @since 0.1.0
 */


public final class TkBankNoteBookSave implements Take {

	/**
	 * Database
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkBankNoteBookSave(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public Response act(Request req) throws Exception {
		final Log log = new RqLog(this.source, req);
		final RqFormSmart form = new RqFormSmart(new RqGreedy(req));
		final Long id = Long.parseLong(form.single("id", "0"));
		final String startnumber = form.single("start_number");
		final String endnumber = form.single("end_number");
		final String prefixnumber = form.single("prefix_number", "");
		final String msg;
		final BankNoteBook item;
		if(id.equals(0L)) {
			final Long accountid = Long.parseLong(form.single("account_id"));
			final PaymentMeanType type = PaymentMeanType.valueOf(form.single("mean_type_id"));
			final BankAccount account = new DbCompanyBankAccounts(this.source).get(accountid);
			item = account.addBook(type, startnumber, endnumber, prefixnumber);
			msg = "Le carnet de formules a été créé avec succès !";
			log.info("Ajout du carnet de formule (%s)", item.name());
		} else {
			item = new DbBankNoteBooks(this.source).get(id);
			if(item.status() == BankNoteBookStatus.REGISTERED) {
				item.update(startnumber, endnumber);
			}
			item.prefixNumber(prefixnumber);
			msg = "Le carnet de formules a été modifié avec succès !";
			log.info("Mise à jour du carnet de formule (%s)", item.name());
		}
		return new RsForward(
			new RsFlash(
				msg,
				Level.INFO
			),
			String.format("/bank-note-book/view?id=%s", item.id())
		);	
	}
}

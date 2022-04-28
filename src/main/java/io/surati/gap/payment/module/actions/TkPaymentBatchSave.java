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
import io.surati.gap.payment.base.api.BankAccount;
import io.surati.gap.payment.base.api.BankNote;
import io.surati.gap.payment.base.api.BankNoteBook;
import io.surati.gap.payment.base.api.BankNoteBookStatus;
import io.surati.gap.payment.base.api.BankNoteBooks;
import io.surati.gap.payment.base.api.PaymentBatch;
import io.surati.gap.payment.base.api.PaymentBatches;
import io.surati.gap.payment.base.api.PaymentMeanType;
import io.surati.gap.payment.base.api.PaymentOrderGroup;
import io.surati.gap.payment.base.api.PaymentOrderGroupsToExecute;
import io.surati.gap.payment.base.api.Workspace;
import io.surati.gap.payment.base.db.DbCompanyBankAccounts;
import io.surati.gap.payment.base.db.DbPaginedBankNoteBooks;
import io.surati.gap.payment.base.db.DbPaymentBatches;
import io.surati.gap.payment.base.db.DbWorkspace;
import io.surati.gap.web.base.log.RqLog;
import io.surati.gap.web.base.rq.RootPageFull;
import io.surati.gap.web.base.rq.RqUser;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.cactoos.list.ListOf;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.flash.RsFlash;
import org.takes.facets.forward.RsForward;
import org.takes.rq.form.RqFormSmart;

/**
 * Take that save a batch payment.
 *
 * <p>The class is immutable and thread-safe.</p>
 *
 * @since 3.0
 */
public final class TkPaymentBatchSave implements Take {

	/**
	 * DataSource
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkPaymentBatchSave(final DataSource source) {
		this.source = source;
	}

	@Override
	public Response act(Request req) throws Exception {
		final Log log = new RqLog(this.source, req);
		final RqFormSmart form = new RqFormSmart(req);
		final Long accountid = Long.parseLong(form.single("account_id"));
		final BankAccount account = new DbCompanyBankAccounts(this.source).get(accountid);
		final PaymentMeanType meantype = PaymentMeanType.valueOf(form.single("mean_type_id"));
		final LocalDate date = LocalDate.parse(form.single("date"), DateTimeFormatter.ISO_DATE);
		final Iterable<String> groupids = form.param("group_id");
		final Iterable<String> bookids = form.param("book_id");
		final Iterable<String> notenumbers = form.param("note_number");
		final User user = new RqUser(this.source, req);
		final Workspace workspace = new DbWorkspace(this.source, user);
		final PaymentBatches batches = new DbPaymentBatches(this.source);
		final PaymentBatch batch = batches.add(date, account, meantype);
		final PaymentOrderGroupsToExecute groups = workspace.ordersToExecute();
		final BankNoteBooks books = new DbPaginedBankNoteBooks(this.source, BankNoteBookStatus.IN_USE);
		final Iterator<String> itgrp = groupids.iterator();
		final Iterator<String> itbook = bookids.iterator();
		final Iterator<String> itnum = notenumbers.iterator();
		while(itgrp.hasNext()) {
			final Long groupid = Long.parseLong(itgrp.next());
			final PaymentOrderGroup group = groups.get(groupid);
			final Long bookid = Long.parseLong(itbook.next());
			final BankNoteBook book = books.get(bookid);
			final BankNote note = batch.pay(group, new ListOf<>(book), user);
			final String numgen = itnum.next();
			if(!note.issuerReference().equals(numgen)) {
				throw new IllegalArgumentException(
					String.format(
						"La position de la formule a changé (%s -> %s) !",
						numgen,
						note.issuerReference()
					)
				);
			}
		}
		log.info("Paiement du lot (ID=%s, Compte=%s, Moyen de paiement=%s)", batch.id(), account.rib(), batch.meanType().name());
		return new RsForward(
			new RsFlash(
				"Le paiement a été effectué avec succès !",
				Level.INFO
			),
			String.format("/payment/batch/view?id=%s&%s", batch.id(), new RootPageFull(req))
		);
	}
}

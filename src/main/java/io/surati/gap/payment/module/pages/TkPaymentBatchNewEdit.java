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
package io.surati.gap.payment.module.pages;

import io.surati.gap.admin.base.api.User;
import io.surati.gap.payment.base.api.BankAccount;
import io.surati.gap.payment.base.api.BankNoteBook;
import io.surati.gap.payment.base.api.BankNoteBookStatus;
import io.surati.gap.payment.base.api.BankNoteBooks;
import io.surati.gap.payment.base.api.PaymentBatch;
import io.surati.gap.payment.base.api.PaymentBatches;
import io.surati.gap.payment.base.api.PaymentMeanType;
import io.surati.gap.payment.base.api.PaymentOrderGroup;
import io.surati.gap.payment.base.api.Workspace;
import io.surati.gap.payment.base.db.DbCompanyBankAccounts;
import io.surati.gap.payment.base.db.DbPaginedBankNoteBooks;
import io.surati.gap.payment.base.db.DbWorkspace;
import io.surati.gap.payment.base.impl.PaymentBatchesImpl;
import io.surati.gap.payment.module.xe.XeBankAccount;
import io.surati.gap.payment.module.xe.XeBankNotes;
import io.surati.gap.payment.module.xe.XePaymentMeanType;
import io.surati.gap.web.base.RsPage;
import io.surati.gap.web.base.rq.RqUser;
import io.surati.gap.web.base.xe.XeRootPage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.LinkedList;
import javax.sql.DataSource;
import org.cactoos.collection.Sticky;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.misc.Href;
import org.takes.rq.RqHref;
import org.takes.rs.xe.XeAppend;
import org.takes.rs.xe.XeChain;
import org.takes.rs.xe.XeSource;

/**
 * Take that shows a pay windows for a batch payment.
 *
 * <p>The class is immutable and thread-safe.</p>
 *
 * @since 3.0
 */
public final class TkPaymentBatchNewEdit implements Take {

	/**
	 * DataSource
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkPaymentBatchNewEdit(final DataSource source) {
		this.source = source;
	}

	@Override
	public Response act(Request req) throws Exception {
		final RqHref.Smart rqhref = new RqHref.Smart(req);
		final Href href = rqhref.href();
		final Long accountid = Long.parseLong(rqhref.single("account"));
		final BankAccount account = new DbCompanyBankAccounts(this.source).get(accountid);
		final PaymentMeanType meantype = PaymentMeanType.valueOf(rqhref.single("meantype"));
		final User user = new RqUser(this.source, req);
		final Workspace workspace = new DbWorkspace(this.source, user);
		final PaymentBatches batches = new PaymentBatchesImpl(workspace.ordersToExecute());
		final PaymentBatch batch = batches.get(account, meantype);
		final Iterable<String> bookids = href.param("book");
		final BankNoteBooks books = new DbPaginedBankNoteBooks(this.source, BankNoteBookStatus.IN_USE);
		final Collection<BankNoteBook> booktouses = new LinkedList<>();
		for (String idstr : bookids) {
			final Long id = Long.parseLong(idstr);
			booktouses.add(books.get(id));
		}
		for (PaymentOrderGroup group : batch.groups()) {
			batch.pay(group, booktouses, user);
		}
		final XeSource src = new XeChain(
			new XeBankAccount("account", account),
			new XePaymentMeanType("mean_type", meantype),
			new XeAppend("today", LocalDate.now().format(DateTimeFormatter.ISO_DATE)),
			new XeBankNotes(batch.notes())
		);
		return new RsPage(
			"/io/surati/gap/payment/module/xsl/payment/batch_new.xsl",
			req,
			this.source,
			() -> new Sticky<>(
				new XeAppend("menu", "payment"),
				src,
				new XeRootPage(req)
			)
		);
	}
}

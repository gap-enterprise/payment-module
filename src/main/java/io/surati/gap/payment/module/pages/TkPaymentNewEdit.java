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
import io.surati.gap.commons.utils.amount.FrAmountInXof;
import io.surati.gap.commons.utils.amount.XofAmountInLetters;
import io.surati.gap.payment.base.api.BankNoteBook;
import io.surati.gap.payment.base.api.PaymentMeanType;
import io.surati.gap.payment.base.api.PaymentOrderGroup;
import io.surati.gap.payment.base.api.ThirdParty;
import io.surati.gap.payment.base.api.Workspace;
import io.surati.gap.payment.base.db.DbBankNoteBooks;
import io.surati.gap.payment.base.db.DbWorkspace;
import io.surati.gap.payment.base.module.xe.XePaymentOrders;
import io.surati.gap.payment.base.module.xe.XeThirdParty;
import io.surati.gap.payment.module.xe.XeBankNoteBook;
import io.surati.gap.web.base.RsPage;
import io.surati.gap.web.base.rq.RqUser;
import io.surati.gap.web.base.xe.XeRootPage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.sql.DataSource;
import org.cactoos.collection.Sticky;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rq.RqHref;
import org.takes.rs.xe.XeAppend;
import org.takes.rs.xe.XeChain;
import org.takes.rs.xe.XeSource;

/**
 * Take that edits a pay action.
 *
 * <p>The class is immutable and thread-safe.</p>
 *
 * @since 3.0
 */
public final class TkPaymentNewEdit implements Take {

	/**
	 * DataSource
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkPaymentNewEdit(final DataSource source) {
		this.source = source;
	}

	@Override
	public Response act(Request req) throws Exception {
		final Long groupid = Long.parseLong(new RqHref.Smart(req).single("group"));
		final Long bookid = Long.parseLong(new RqHref.Smart(req).single("book"));
		final User user = new RqUser(this.source, req);
		final Workspace workspace = new DbWorkspace(this.source, user);
		final PaymentOrderGroup group = workspace.ordersToExecute().get(groupid);
		final BankNoteBook book = new DbBankNoteBooks(this.source).get(bookid);
		final ThirdParty beneficiary = group.beneficiary();
		final LocalDate date = LocalDate.now();
		XeSource xecommon = new XeChain(
		    new XeThirdParty("beneficiary", beneficiary),
		    new XeBankNoteBook("bank_note_book", book),
			new XePaymentOrders(group.iterate()),
			new XeAppend("group_id", group.id().toString()),
			new XeAppend("is_hetero", Boolean.toString(group.isHetero())),
			new XeAppend(
				"total_amount_to_pay",
				new FrAmountInXof(group.totalAmount()).toString()
			),
			new XeAppend(
				"total_amount_to_pay_in_letters",
				new XofAmountInLetters(group.totalAmount()).toString()
			),
			new XeAppend("date", date.format(DateTimeFormatter.ISO_DATE))
		);
		final XeSource src;
		final PaymentMeanType meantype = book.meanType();
		if(meantype == PaymentMeanType.LC || meantype == PaymentMeanType.BO) {
			src = new XeChain(
				xecommon,
				new XeAppend("echeance_date", group.dueDate().format(DateTimeFormatter.ISO_DATE))
			);
		} else {
			src = xecommon;
		}
		return new RsPage(
			"/io/surati/gap/payment/module/xsl/payment/new.xsl",
			req,
			this.source,
			() -> new Sticky<>(
				new XeAppend("menu", "payment"),
				new XeRootPage(req),
				src
			)
		);
	}
}

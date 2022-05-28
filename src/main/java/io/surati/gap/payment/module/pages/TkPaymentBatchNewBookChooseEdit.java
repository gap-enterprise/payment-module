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

import io.surati.gap.payment.base.api.BankAccount;
import io.surati.gap.payment.base.api.BankNoteBook;
import io.surati.gap.payment.base.api.BankNoteBookStatus;
import io.surati.gap.payment.base.api.PaymentMeanType;
import io.surati.gap.payment.base.db.DbBankNoteBooks;
import io.surati.gap.payment.base.db.DbCompanyBankAccounts;
import io.surati.gap.payment.module.xe.XeBankAccount;
import io.surati.gap.payment.module.xe.XeBankNoteBooks;
import io.surati.gap.payment.module.xe.XePaymentMeanType;
import io.surati.gap.payment.module.server.RsPage;
import io.surati.gap.web.base.xe.XeRootPage;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.stream.Collectors;
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
 * Take that choose a book for a new batch payment.
 *
 * <p>The class is immutable and thread-safe.</p>
 *
 * @since 3.0
 */

public class TkPaymentBatchNewBookChooseEdit implements Take  {

	/**
	 * DataSource
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkPaymentBatchNewBookChooseEdit(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public Response act(Request req) throws Exception {		
		final RqHref.Smart hqref = new RqHref.Smart(req);
		final Long accountid = Long.parseLong(hqref.single("account"));
		final BankAccount account = new DbCompanyBankAccounts(this.source).get(accountid);
		final PaymentMeanType meantype = PaymentMeanType.valueOf(hqref.single("meantype"));
		final Iterable<BankNoteBook> books = new DbBankNoteBooks(this.source).iterate(BankNoteBookStatus.IN_USE);
		final Collection<BankNoteBook> bookstouse = new LinkedList<>();
		for (BankNoteBook bk : books) {
			if(!bk.account().id().equals(account.id())) {
				continue;
			}
			if(bk.meanType() != meantype) {
				continue;
			}
			bookstouse.add(bk);
		}
		final XeSource src = new XeChain(
			new XeBankAccount("account", account),
			new XePaymentMeanType("mean_type", meantype),
			new XeBankNoteBooks(
				bookstouse.stream()
					.sorted(
						Comparator.comparing(BankNoteBook::id)
					).collect(
						Collectors.toList())
					)
		);
		return new RsPage(
			"/io/surati/gap/payment/module/xsl/payment/batch_book_choice_edit.xsl",
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

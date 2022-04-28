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
import io.surati.gap.payment.base.api.PaymentOrderGroup;
import io.surati.gap.payment.base.api.ThirdParty;
import io.surati.gap.payment.base.api.Workspace;
import io.surati.gap.payment.base.db.DbBankNoteBooks;
import io.surati.gap.payment.base.db.DbWorkspace;
import io.surati.gap.payment.base.module.xe.XeThirdParty;
import io.surati.gap.payment.module.xe.XeBankNoteBooks;
import io.surati.gap.web.base.RsPage;
import io.surati.gap.web.base.rq.RqUser;
import io.surati.gap.web.base.xe.XeRootPage;
import java.util.Collection;
import java.util.LinkedList;
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
 * Take that choose a book for a new payment.
 *
 * <p>The class is immutable and thread-safe.</p>
 *
 * @since 3.0
 */

public class TkPaymentNewBookChooseEdit implements Take  {

	/**
	 * DataSource
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkPaymentNewBookChooseEdit(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public Response act(Request req) throws Exception {		
		final Long groupid = Long.parseLong(new RqHref.Smart(req).single("group"));
		final User user = new RqUser(this.source, req);
		final Workspace workspace = new DbWorkspace(this.source, user);
		final PaymentOrderGroup group = workspace.ordersToExecute().get(groupid);
		final Iterable<BankNoteBook> books = new DbBankNoteBooks(this.source).iterate(BankNoteBookStatus.IN_USE);
		final Collection<BankNoteBook> bookstouse = new LinkedList<>();
		final ThirdParty beneficiary = group.beneficiary();
		final BankAccount account = group.accountToUse();
		for (BankNoteBook bk : books) {
			if(!bk.account().id().equals(account.id())) {
				continue;
			}
			if(bk.meanType() != group.meanType()) {
				continue;
			}
			bookstouse.add(bk);
		}
		final XeSource src = new XeChain(
			new XeThirdParty("beneficiary", beneficiary),
			new XeBankNoteBooks(bookstouse),
			new XeAppend("group_id", groupid.toString())
		);
		return new RsPage(
			"/io/surati/gap/payment/module/xsl/payment/book_choice_edit.xsl",
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

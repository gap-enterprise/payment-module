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

import io.surati.gap.payment.base.api.BankNoteBook;
import io.surati.gap.payment.base.api.PaymentMeanType;
import io.surati.gap.payment.base.db.DbBankNoteBooks;
import io.surati.gap.payment.base.db.DbCompanyBankAccounts;
import io.surati.gap.payment.module.xe.XeBankAccounts;
import io.surati.gap.payment.module.xe.XeBankNoteBook;
import io.surati.gap.payment.module.xe.XePaymentMeanTypes;
import io.surati.gap.payment.module.server.RsPage;
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
 * Take that edits a bank note book.
 *
 * <p>The class is immutable and thread-safe.</p>
 *
 * @since 3.0
 */
public final class TkBankNoteBookEdit implements Take {

	/**
	 * DataSource
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkBankNoteBookEdit(final DataSource source) {
		this.source = source;
	}

	@Override
	public Response act(Request req) throws Exception {
		final Long id = Long.parseLong(new RqHref.Smart(req).single("id", "0"));
		final Collection<PaymentMeanType> meantypes = new LinkedList<>();
		for (PaymentMeanType types : PaymentMeanType.values()) {
			if(!types.equals(PaymentMeanType.NONE)) {
				meantypes.add(types);
			}
		}
		final XeSource src;
		if(id.equals(0L)) {
			src = new XeChain(
				new XePaymentMeanTypes(meantypes),
				new XeBankAccounts(new DbCompanyBankAccounts(this.source))
			);
		} else {
			final BankNoteBook book = new DbBankNoteBooks(this.source).get(id);
			src = new XeBankNoteBook(book);
		}
		return new RsPage(
			"/io/surati/gap/payment/module/xsl/bank_note_book/edit.xsl",
			req,
			this.source,
			() -> new Sticky<>(
				new XeAppend("menu", "bank-note-book"),
				src
			)
		);
	}
}

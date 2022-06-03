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
import io.surati.gap.payment.base.api.BankNoteBookStatus;
import io.surati.gap.payment.base.db.DbBankNoteBooks;
import io.surati.gap.payment.module.PaymentDashboardMenu;
import io.surati.gap.payment.module.xe.XeBankNoteBooks;
import io.surati.gap.web.base.AbstractTkDashboard;
import io.surati.gap.web.base.InClasspath;
import io.surati.gap.web.base.menu.DashboardMenu;
import javax.sql.DataSource;
import org.takes.Request;
import org.takes.Response;
import org.takes.rs.xe.XeAppend;
import org.takes.rs.xe.XeChain;

/**
 * Take that displays bank accounts dashboard.
 *
 * <p>The class is immutable and thread-safe.</p>
 *
 * @since 0.2
 */
public final class TkDashboardBankNoteBook extends AbstractTkDashboard {

	/**
	 * Data source.
	 */
	private final DataSource src;

	/**
	 * Ctor.
	 *
	 * @param src Data source
	 */
	public TkDashboardBankNoteBook(final DataSource src) {
		super(
			src, PaymentDashboardMenu.BANK_NOTE_BOOK_DASHBOARD_MENU,
			"/io/surati/gap/payment/module/xsl/dashboard/bank_note_book.xsl",
			new InClasspath(TkDashboardBankNoteBook.class)
		);
		this.src = src;
	}

	@Override
	public Response act(final Request req) throws Exception {
		Integer nbregistered = 0;
		Integer nbinuse = 0;
		Integer nbblocked = 0;
		Integer nbfinished = 0;
		for (BankNoteBook item : new DbBankNoteBooks(this.src).iterate()) {
			switch (item.status()) {
				case REGISTERED:
					nbregistered++;
					break;
				case IN_USE:
					nbinuse++;
					break;
				case BLOCKED:
					nbblocked++;
					break;
				case TERMINATED:
					nbfinished++;
					break;
				default:
					break;
			}
		}
		return this.act(
			req,
			new XeChain(
				new XeAppend("nb_registered", nbregistered.toString()),
				new XeAppend("nb_in_use", nbinuse.toString()),
				new XeAppend("nb_blocked", nbblocked.toString()),
				new XeAppend("nb_finished", nbfinished.toString()),
				new XeBankNoteBooks(
					new DbBankNoteBooks(this.src).iterate(BankNoteBookStatus.IN_USE)
				)
			)
		);
	}

}

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
import io.surati.gap.payment.base.api.BankAccounts;
import io.surati.gap.payment.base.api.ThirdParty;
import io.surati.gap.payment.base.db.DbCompanyBankAccounts;
import io.surati.gap.payment.base.db.DbThirdParties;
import io.surati.gap.payment.base.db.DbThirdPartyBankAccounts;
import io.surati.gap.payment.module.xe.XeBankAccount;
import io.surati.gap.payment.module.xe.XeBankAccountAccountingSettings;
import io.surati.gap.payment.module.server.RsPage;
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
 * Take that shows a bank account in read-only mode.
 *
 * <p>The class is immutable and thread-safe.</p>
 *
 * @since 3.0
 */
public final class TkBankAccountView implements Take {

	/**
	 * DataSource
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkBankAccountView(final DataSource source) {
		this.source = source;
	}

	@Override
	public Response act(Request req) throws Exception {
		final Long id = Long.parseLong(new RqHref.Smart(req).single("id"));
		final Long holderid = Long.parseLong(new RqHref.Smart(req).single("holder", "0"));
		final BankAccounts items;
		final XeSource hdsrc;
		final String menu;
		if(holderid.equals(0L)) {
			items = new DbCompanyBankAccounts(this.source);
			hdsrc = XeSource.EMPTY;
			menu = "bank-account";
		} else {
			final ThirdParty holder = new DbThirdParties(this.source).get(holderid);
			items = new DbThirdPartyBankAccounts(this.source, holder);
			hdsrc = new XeChain(
				new XeAppend("holder", holderid.toString()),
				new XeAppend("holder_name", holder.abbreviated())
			);
			menu = "third-party";
		}
		final BankAccount item = items.get(id);
		final XeSource src = new XeChain(
			hdsrc,
			new XeBankAccount(item),
			new XeBankAccountAccountingSettings(item.accountingSettings().iterate())
		);
		return new RsPage(
			"/io/surati/gap/payment/module/xsl/bank_account/view.xsl",
			req,
			this.source,
			() -> new Sticky<>(
				new XeAppend("menu", menu),
				src
			)
		);
	}
}

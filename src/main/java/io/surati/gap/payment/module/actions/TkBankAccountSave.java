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

import io.surati.gap.payment.base.api.Bank;
import io.surati.gap.payment.base.api.BankAccount;
import io.surati.gap.payment.base.api.BankAccounts;
import io.surati.gap.payment.base.api.ThirdParty;
import io.surati.gap.payment.base.db.DbBanks;
import io.surati.gap.payment.base.db.DbCompanyBankAccounts;
import io.surati.gap.payment.base.db.DbThirdParties;
import io.surati.gap.payment.base.db.DbThirdPartyBankAccounts;
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
 * Take that creates or modifies a bank account.
 *
 * <p>The class is immutable and thread-safe.</p>
 *
 * @since 0.1.0
 */


public final class TkBankAccountSave implements Take {

	/**
	 * Database
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkBankAccountSave(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public Response act(Request req) throws Exception {		
		final RqFormSmart form = new RqFormSmart(new RqGreedy(req));
		final Long id = Long.parseLong(form.single("id", "0"));
		final Long holderid = Long.parseLong(form.single("holder_id", "0"));
		final String branchcode = form.single("branch_code");
		final String number = form.single("number");
		final String key = form.single("key");		
		final BankAccounts items;
		if(holderid.equals(0L)) {
			items = new DbCompanyBankAccounts(this.source);
		} else {
			final ThirdParty holder = new DbThirdParties(this.source).get(holderid);
			items = new DbThirdPartyBankAccounts(this.source, holder);
		}
		final String msg;
		final BankAccount item;
		if(id.equals(0L)) {
			final Long bankid = Long.parseLong(form.single("bank_id"));
			final Bank bank = new DbBanks(this.source).get(bankid);
			item = items.add(bank, branchcode, number, key);
			msg = String.format("Le compte bancaire %s a été créé avec succès !", item.rib());
		} else {
			item = items.get(id);
			item.update(branchcode, number, key);
			msg = String.format("Le compte bancaire %s a été modifié avec succès !", item.rib());
		}
		final String url;
		if(holderid.equals(0L)) {
			url = String.format("/bank-account/view?id=%s", item.id());
		} else {
			url = String.format("/third-party/view?id=%s", holderid);
		}
		return new RsForward(
			new RsFlash(
				msg,
				Level.INFO
			),
			url
		);	
	}
}

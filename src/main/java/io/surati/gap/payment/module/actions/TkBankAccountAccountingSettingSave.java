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
import io.surati.gap.payment.base.api.BankAccountAccountingSetting;
import io.surati.gap.payment.base.api.PaymentMeanType;
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
 * Take that creates or modifies an accounting setting of a bank account.
 *
 * <p>The class is immutable and thread-safe.</p>
 *
 * @since 0.3
 */


public final class TkBankAccountAccountingSettingSave implements Take {

	/**
	 * Database
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkBankAccountAccountingSettingSave(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public Response act(Request req) throws Exception {		
		final Log log = new RqLog(this.source, req);
		final RqFormSmart form = new RqFormSmart(new RqGreedy(req));
		final String meantypeid = form.single("mean_type_id");
		final PaymentMeanType meantype = PaymentMeanType.valueOf(meantypeid);
		final String journalcode = form.single("journal_code");
		final Long accountid = Long.parseLong(form.single("account_id"));
		final BankAccount account = new DbCompanyBankAccounts(this.source).get(accountid);
		final BankAccountAccountingSetting item = account.accountingSettings().get(meantype);
		item.update(journalcode);
		log.info("Mise à jour paramètre comptable (%s) du compte (%s).", meantype.name(), account.rib());
		return new RsForward(
			new RsFlash(
				"Le paramètre comptable a été modifié avec succès !",
				Level.INFO
			),
			String.format(
				"/bank-account/accounting-setting/view?meantype=%s&account=%s",
				meantype.name(),
				account.id()
			)
		);	
	}
}

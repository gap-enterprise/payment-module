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

import io.surati.gap.payment.base.db.DbCompanyBankAccounts;
import io.surati.gap.payment.module.PaymentDashboardMenu;
import io.surati.gap.payment.module.xe.XeBankAccounts;
import io.surati.gap.web.base.AbstractTkDashboard;
import io.surati.gap.web.base.InClasspath;
import io.surati.gap.web.base.menu.DashboardMenu;
import javax.sql.DataSource;
import org.takes.Request;
import org.takes.Response;

/**
 * Take that displays bank accounts dashboard.
 *
 * <p>The class is immutable and thread-safe.</p>
 *
 * @since 0.2
 */
public final class TkDashboardBankAccount extends AbstractTkDashboard {

	/**
	 * Data source.
	 */
	private final DataSource src;

	/**
	 * Ctor.
	 *
	 * @param src Data source
	 */
	public TkDashboardBankAccount(final DataSource src) {
		super(
			src, PaymentDashboardMenu.BANK_ACCOUNT_DASHBOARD_MENU,
			"/io/surati/gap/payment/module/xsl/dashboard/bank_account.xsl",
			new InClasspath(TkDashboardBankAccount.class)
		);
		this.src = src;
	}

	@Override
	public Response act(final Request req) throws Exception {
		return this.act(
			req,
			new XeBankAccounts(new DbCompanyBankAccounts(this.src).iterate())
		);
	}

}

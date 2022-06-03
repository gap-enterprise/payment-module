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

import io.surati.gap.commons.utils.amount.FrThousandSeparatorAmount;
import io.surati.gap.payment.base.api.PaymentOrder;
import io.surati.gap.payment.base.api.PaymentOrderStatus;
import io.surati.gap.payment.base.api.ReferenceDocumentStatus;
import io.surati.gap.payment.base.api.ReferenceDocuments;
import io.surati.gap.payment.base.db.DbPaginedPaymentOrders;
import io.surati.gap.payment.base.db.DbPaginedReferenceDocuments;
import io.surati.gap.payment.base.filter.ReferenceDocumentCriteria;
import io.surati.gap.payment.module.PaymentDashboardMenu;
import io.surati.gap.web.base.AbstractTkDashboard;
import io.surati.gap.web.base.InClasspath;
import java.util.Arrays;
import javax.sql.DataSource;
import org.takes.Request;
import org.takes.Response;
import org.takes.rs.xe.XeAppend;
import org.takes.rs.xe.XeChain;

/**
 * Take that displays payment dashboard.
 *
 * <p>The class is immutable and thread-safe.</p>
 *
 * @since 0.2
 */
public final class TkDashboardPayment extends AbstractTkDashboard {

	/**
	 * Data source.
	 */
	private final DataSource src;

	/**
	 * Ctor.
	 *
	 * @param src Data source
	 */
	public TkDashboardPayment(final DataSource src) {
		super(
			src, PaymentDashboardMenu.PAYMENT_DASHBOARD_MENU,
			"/io/surati/gap/payment/module/xsl/dashboard/payment.xsl",
			new InClasspath(TkDashboardPayment.class)
		);
		this.src = src;
	}

	@Override
	public Response act(final Request req) throws Exception {
		final ReferenceDocumentCriteria criteria = new ReferenceDocumentCriteria();
		criteria.add(
			Arrays.asList(
				ReferenceDocumentStatus.PAID_PARTIALLY,
				ReferenceDocumentStatus.WAITING_FOR_PAYMENT
			)
		);
		final ReferenceDocuments docs = new DbPaginedReferenceDocuments(this.src, criteria);
		Integer nborderstoexecute = 0;
		Double amounttoexecute = 0.0;
		for (PaymentOrder order : new DbPaginedPaymentOrders(this.src, PaymentOrderStatus.IN_WAITING_FOR_PAYMENT).iterate()) {
			nborderstoexecute++;
			amounttoexecute+=order.amountToPay();
		}
		return this.act(
			req,
			new XeChain(
				new XeAppend("nb_orders_to_authorize", docs.count().toString()),
				new XeAppend("nb_orders_to_execute", nborderstoexecute.toString()),
				new XeAppend("amount_to_authorize", new FrThousandSeparatorAmount(docs.totalAmount()).toString()),
				new XeAppend("amount_to_execute", new FrThousandSeparatorAmount(amounttoexecute).toString())
			)
		);
	}

}

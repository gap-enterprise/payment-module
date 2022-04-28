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
import io.surati.gap.payment.base.api.PaymentMean;
import io.surati.gap.payment.base.api.PaymentMeanField;
import io.surati.gap.payment.base.api.PaymentMeanFieldType;
import io.surati.gap.payment.base.db.DbBanks;
import io.surati.gap.payment.base.impl.PointImpl;
import java.util.Iterator;
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
 * Take that saves a manual setup of payment mean.
 *
 * <p>The class is immutable and thread-safe.</p>
 *
 * @since 0.1.0
 */


public final class TkPaymentMeanManualSetupSave implements Take {

	/**
	 * Database
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkPaymentMeanManualSetupSave(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public Response act(Request req) throws Exception {		
		final RqFormSmart form = new RqFormSmart(new RqGreedy(req));
		final Long id = Long.parseLong(form.single("id"));
		final Long bankid = Long.parseLong(form.single("bank_id"));
		final double width = Double.parseDouble(form.single("width"));
		final double height = Double.parseDouble(form.single("height"));
		final int dpi = Integer.parseInt(form.single("dpi"));
		final Bank bank = new DbBanks(this.source).get(bankid);
		final PaymentMean item = bank.paymentMeans().get(id);
		item.image().update(width, height, dpi);
		final Iterator<String> ftit = form.param("field_type_id").iterator();
		final Iterator<String> xit = form.param("x").iterator();
		final Iterator<String> yit = form.param("y").iterator();
		final Iterator<String> wit = form.param("fwidth").iterator();
		final Iterator<String> vit = form.param("visible").iterator();
		while(ftit.hasNext()) {
			final PaymentMeanFieldType type = PaymentMeanFieldType.valueOf(ftit.next());
			final double x = Double.parseDouble(xit.next());
			final double y = Double.parseDouble(yit.next());
			final double fwidth = Double.parseDouble(wit.next());
			final PaymentMeanField field = item.field(type);
			field.update(new PointImpl(x, y), fwidth);
			final boolean visible = Boolean.parseBoolean(vit.next());
			if(visible) {
				field.show();
			} else {
				field.disappear();
			}
		}
		return new RsForward(
			new RsFlash(
				"Configuration manuelle effectuée avec succès !",
				Level.INFO
			),
			String.format("/bank/payment-mean/view?id=%s&bank=%s", item.id(), bank.id())
		);	
	}
}

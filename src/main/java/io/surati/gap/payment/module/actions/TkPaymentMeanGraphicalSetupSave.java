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

import io.surati.gap.commons.utils.convert.PixelToCentimeter;
import io.surati.gap.payment.base.api.Bank;
import io.surati.gap.payment.base.api.Image;
import io.surati.gap.payment.base.api.PaymentMean;
import io.surati.gap.payment.base.api.PaymentMeanField;
import io.surati.gap.payment.base.db.DbBanks;
import io.surati.gap.payment.base.db.FileImage;
import io.surati.gap.payment.base.impl.PointImpl;
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
 * Take that saves a graphical setup of payment mean.
 *
 * <p>The class is immutable and thread-safe.</p>
 *
 * @since 0.1.0
 */


public final class TkPaymentMeanGraphicalSetupSave implements Take {

	/**
	 * Database
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkPaymentMeanGraphicalSetupSave(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public Response act(Request req) throws Exception {		
		final RqFormSmart form = new RqFormSmart(new RqGreedy(req));
		final Long id = Long.parseLong(form.single("id"));
		final Long bankid = Long.parseLong(form.single("bank_id"));
		final Bank bank = new DbBanks(this.source).get(bankid);
		final PaymentMean item = bank.paymentMeans().get(id);
		final Image image = new FileImage(item.image());
		final int dpi = image.dpi();
		item.image().update(image.width(), image.height(), dpi);
		for (PaymentMeanField field : item.fields()) {
			final double x = new PixelToCentimeter(
				dpi,
				Double.parseDouble(
					form.single(String.format("%s_x", field.type().name()))
				)
			).value();
			final double y = new PixelToCentimeter(
				dpi,
				Double.parseDouble(
					form.single(String.format("%s_y", field.type().name()))
				)
			).value();
			final double width = new PixelToCentimeter(
				dpi,
				Double.parseDouble(
					form.single(String.format("%s_width", field.type().name()))
				)
			).value();
			field.update(new PointImpl(x, y), width);
		}
		return new RsForward(
			new RsFlash(
				"Configuration graphique effectuée avec succès !",
				Level.INFO
			),
			String.format("/bank/payment-mean/view?id=%s&bank=%s", item.id(), bank.id())
		);	
	}
}

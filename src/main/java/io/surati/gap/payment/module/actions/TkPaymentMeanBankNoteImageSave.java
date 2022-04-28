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
import io.surati.gap.commons.utils.convert.RqFilename;
import io.surati.gap.payment.base.api.Bank;
import io.surati.gap.payment.base.api.PaymentMean;
import io.surati.gap.payment.base.db.DbBanks;
import io.surati.gap.web.base.log.RqLog;
import java.io.InputStream;
import java.util.Optional;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.cactoos.text.TextOf;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.flash.RsFlash;
import org.takes.facets.forward.RsForward;
import org.takes.rq.RqGreedy;
import org.takes.rq.form.RqFormSmart;
import org.takes.rq.multipart.RqMtSmart;

/**
 * Take that saves a bank note image of payment mean.
 *
 * <p>The class is immutable and thread-safe.</p>
 *
 * @since 0.1.0
 */


public final class TkPaymentMeanBankNoteImageSave implements Take {

	/**
	 * Database
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkPaymentMeanBankNoteImageSave(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public Response act(Request req) throws Exception {
		final Log log = new RqLog(this.source, req);
		final RqGreedy rqG = new RqGreedy(req);
		final RqMtSmart mt = new RqMtSmart(rqG);					        
		final Request rqimage = mt.single("bank_note_image");
		final Long id = Long.parseLong(new TextOf(new RqFormSmart(mt.single("id")).body()).asString().trim());
		final Long bankid = Long.parseLong(new TextOf(new RqFormSmart(mt.single("bank_id")).body()).asString().trim());
		final Bank bank = new DbBanks(this.source).get(bankid);
		final PaymentMean mean = bank.paymentMeans().get(id);
		try(InputStream file = rqimage.body()) {
			final String filename = new RqFilename(rqimage).value().toLowerCase();
			final String ext = getExtensionByStringHandling(filename).get();
			mean.image().update(file, ext);
		}
		log.info("Configuration de l'image du moyen de paiement (%s)", mean);
		return new RsForward(
			new RsFlash(
				"Configuration de l'image effectuée avec succès !",
				Level.INFO
			),
			String.format("/bank/payment-mean/view?id=%s&bank=%s", mean.id(), bank.id())
		);	
	}
	
	private static Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
          .filter(f -> f.contains("."))
          .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }
}

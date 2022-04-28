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
import io.surati.gap.payment.base.api.PaymentBatch;
import io.surati.gap.payment.base.db.DbPaymentBatches;
import io.surati.gap.web.base.log.RqLog;
import io.surati.gap.web.base.rq.RootPageUri;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.flash.RsFlash;
import org.takes.facets.forward.RsForward;
import org.takes.rq.RqHref;

/**
 * Take that finalizes a payment batch.
 *
 * <p>The class is immutable and thread-safe.</p>
 *
 * @since 0.1.0
 */


public final class TkBatchFinalize implements Take {

	/**
	 * Database
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkBatchFinalize(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public Response act(Request req) throws Exception {
		final Log log = new RqLog(this.source, req);
		final Long batchid = Long.parseLong(new RqHref.Smart(req).single("batchid"));
		final PaymentBatch batch = new DbPaymentBatches(this.source).get(batchid);
		batch.terminate();
		log.info("Finalisation du lot (ID=%s, Compte=%s, Moyen de paiement=%s)", batch.id(), batch.account().rib(), batch.meanType().name());
		return new RsForward(
			new RsFlash(
				"Le lot de paiement a été finalisé avec succès !",
				Level.INFO
			),
			new RootPageUri(req).toString()
		);	
	}
}

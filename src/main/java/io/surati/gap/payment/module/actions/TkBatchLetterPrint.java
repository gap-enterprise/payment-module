package io.surati.gap.payment.module.actions;

import io.surati.gap.admin.base.api.Log;
import io.surati.gap.payment.base.api.BankNote;
import io.surati.gap.payment.base.api.PaymentBatch;
import io.surati.gap.payment.base.api.Printer;
import io.surati.gap.payment.base.db.DbPaymentBatches;
import io.surati.gap.payment.base.report.BirtLetterPrinter;
import io.surati.gap.web.base.log.RqLog;
import java.io.ByteArrayOutputStream;
import javax.sql.DataSource;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rq.RqHref;
import org.takes.rs.RsWithBody;

/**
 * @see https://www.unitconverters.net/typography/centimeter-to-pixel-x.htm
 * for pixel convertion
 * @author baudoliver7
 *
 */
public final class TkBatchLetterPrint implements Take {

	private final DataSource source;

	public TkBatchLetterPrint(final DataSource source) {
		this.source = source;
	}

	@Override
	public Response act(Request req) throws Exception {
		final Log log = new RqLog(this.source, req);
		final Long batchid = Long.parseLong(new RqHref.Smart(req).single("batch"));
		final PaymentBatch batch = new DbPaymentBatches(this.source).get(batchid);
		final Iterable<BankNote> notes = batch.notes();
	    final ByteArrayOutputStream output = new ByteArrayOutputStream();
	    final Printer printer = new BirtLetterPrinter(notes);
	    printer.print(output);
	    log.info("Impression lettre du lot (ID=%s, Compte=%s, Moyen de paiement=%s)", batch.id(), batch.account().rib(), batch.meanType().name());
		return new RsWithBody(output.toByteArray());
		
	}
}

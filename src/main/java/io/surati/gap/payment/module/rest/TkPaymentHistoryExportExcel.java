package io.surati.gap.payment.module.rest;

import io.surati.gap.admin.base.api.Log;
import io.surati.gap.payment.base.api.BankNotes;
import io.surati.gap.payment.base.api.Printer;
import io.surati.gap.payment.base.report.PaymentHistoryPrinter;
import io.surati.gap.payment.module.rq.RqBankNotes;
import io.surati.gap.web.base.log.RqLog;
import java.io.ByteArrayOutputStream;
import javax.sql.DataSource;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rs.RsWithBody;

/**
 * @see https://www.unitconverters.net/typography/centimeter-to-pixel-x.htm
 * for pixel convertion
 * @author baudoliver7
 *
 */
public final class TkPaymentHistoryExportExcel implements Take {

	private final DataSource source;

	public TkPaymentHistoryExportExcel(final DataSource source) {
		this.source = source;
	}

	@Override
	public Response act(Request req) throws Exception {
		final Log log = new RqLog(this.source, req);
		final BankNotes notes = new RqBankNotes(this.source, req);
	    final ByteArrayOutputStream output = new ByteArrayOutputStream();
	    final Printer printer = new PaymentHistoryPrinter(notes.iterate());
	    printer.print(output);
	    log.info("Exportation de l'historique des paiements.");
		return new RsWithBody(output.toByteArray());
		
	}
}

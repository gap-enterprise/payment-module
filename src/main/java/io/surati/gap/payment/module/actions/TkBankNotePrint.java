package io.surati.gap.payment.module.actions;

import io.surati.gap.admin.base.api.Log;
import io.surati.gap.payment.base.api.BankNote;
import io.surati.gap.payment.base.api.PaymentMean;
import io.surati.gap.payment.base.api.Printer;
import io.surati.gap.payment.base.db.DbBankNotes;
import io.surati.gap.payment.base.report.BirtBankNotePrinter;
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
 */
public final class TkBankNotePrint implements Take {

	private final DataSource source;

	public TkBankNotePrint(final DataSource source) {
		this.source = source;
	}

	@Override
	public Response act(Request req) throws Exception {
		final Log log = new RqLog(this.source, req);
		final Long id = Long.parseLong(new RqHref.Smart(req).single("id"));
		final BankNote note = new DbBankNotes(this.source).get(id);
		final PaymentMean mean = note.book().account().bank().paymentMeans().get(note.book().meanType());
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final Printer printer = new BirtBankNotePrinter(note, mean);
		printer.print(output);
		log.info("Impression de %s (%s)", note.name(), note.book().name());
		return new RsWithBody(output.toByteArray());
	}
}

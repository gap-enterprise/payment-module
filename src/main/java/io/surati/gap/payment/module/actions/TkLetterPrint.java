package io.surati.gap.payment.module.actions;

import io.surati.gap.admin.base.api.Log;
import io.surati.gap.payment.base.api.BankNote;
import io.surati.gap.payment.base.api.Printer;
import io.surati.gap.payment.base.db.DbBankNotes;
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
public final class TkLetterPrint implements Take {

	private final DataSource source;

	public TkLetterPrint(final DataSource source) {
		this.source = source;
	}

	@Override
	public Response act(Request req) throws Exception {
		final Log log = new RqLog(this.source, req);
		final Long id = Long.parseLong(new RqHref.Smart(req).single("id"));
		final BankNote note = new DbBankNotes(this.source).get(id);
	    final ByteArrayOutputStream output = new ByteArrayOutputStream();
	    final Printer printer = new BirtLetterPrinter(note);
	    printer.print(output);
	    log.info("Impression de la lettre de l'effet %s (%s)", note.name(), note.book().name());
		return new RsWithBody(output.toByteArray());
		
	}
}

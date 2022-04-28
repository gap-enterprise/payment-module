package io.surati.gap.payment.module.actions;

import io.surati.gap.admin.base.api.Log;
import io.surati.gap.payment.base.api.BankNote;
import io.surati.gap.payment.base.api.PaymentBatch;
import io.surati.gap.payment.base.api.Printer;
import io.surati.gap.payment.base.db.DbPaymentBatches;
import io.surati.gap.payment.base.report.BirtPaymentResumePrinter;
import io.surati.gap.web.base.log.RqLog;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import javax.sql.DataSource;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rq.RqHref;
import org.takes.rs.RsWithBody;

/**
 * @see https://www.unitconverters.net/typography/centimeter-to-pixel-x.htm
 * for pixel convertion
 */
public final class TkBatchPaymentResumePrint implements Take {

	private final DataSource source;

	public TkBatchPaymentResumePrint(final DataSource source) {
		this.source = source;
	}

	@Override
	public Response act(Request req) throws Exception {
		final Log log = new RqLog(this.source, req);
		final Long batchid = Long.parseLong(new RqHref.Smart(req).single("batch"));
		final PaymentBatch batch = new DbPaymentBatches(this.source).get(batchid);
		final List<InputStream> pdfs = new LinkedList<>();
		for (BankNote note : batch.notes()) {
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			final Printer printer = new BirtPaymentResumePrinter(note);
			printer.print(out);
			byte[] bytes = out.toByteArray();
			InputStream in = new ByteArrayInputStream(bytes);
			pdfs.add(in);
		}
		final PDFMergerUtility pdfmerger = new PDFMergerUtility();
		pdfmerger.addSources(pdfs);
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		pdfmerger.setDestinationStream(output);
		pdfmerger.mergeDocuments(MemoryUsageSetting.setupTempFileOnly());
		log.info("Impression bordereaux de paiement du lot (ID=%s, Compte=%s, Moyen de paiement=%s)", batch.id(), batch.account().rib(), batch.meanType().name());
		return new RsWithBody(output.toByteArray());
	}
}

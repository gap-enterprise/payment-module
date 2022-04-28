package io.surati.gap.payment.module.actions;

import io.surati.gap.payment.base.api.Bank;
import io.surati.gap.payment.base.api.PaymentMean;
import io.surati.gap.payment.base.api.Printer;
import io.surati.gap.payment.base.db.DbBanks;
import io.surati.gap.payment.base.impl.LettreChangeSample;
import io.surati.gap.payment.base.report.BirtBankNotePrinter;
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
public final class TkBankNoteSamplePrint implements Take {

	private final DataSource source;

	public TkBankNoteSamplePrint(final DataSource source) {
		this.source = source;
	}

	@Override
	public Response act(Request req) throws Exception {
		final Long meanid = Long.parseLong(new RqHref.Smart(req).single("mean"));
		final Long bankid = Long.parseLong(new RqHref.Smart(req).single("bank"));
		final Bank bank = new DbBanks(this.source).get(bankid);
		final PaymentMean mean = bank.paymentMeans().get(meanid);
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final Printer printer = new BirtBankNotePrinter(new LettreChangeSample(), mean);
		printer.print(output);
		return new RsWithBody(output.toByteArray());
	}
}

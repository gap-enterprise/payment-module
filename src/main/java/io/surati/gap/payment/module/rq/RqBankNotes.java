package io.surati.gap.payment.module.rq;

import io.surati.gap.commons.utils.time.Period;
import io.surati.gap.commons.utils.time.SafePeriodFromText;
import io.surati.gap.payment.base.api.BankNote;
import io.surati.gap.payment.base.api.BankNotes;
import io.surati.gap.payment.base.api.PaymentStatus;
import io.surati.gap.payment.base.db.DbPaginedBankNotes;
import java.io.IOException;
import javax.sql.DataSource;
import org.takes.Request;
import org.takes.rq.RqHref.Smart;

public final class RqBankNotes implements BankNotes {

	private final BankNotes origin;
	
	public RqBankNotes(final DataSource source, final Request req) throws IOException {
		final Smart href = new Smart(req);
		final String filter = href.single("filter", "");					
		final Long page = Long.parseLong(href.single("page"));
		final Long nbperpage = Long.parseLong(href.single("nbperpage"));
		final Period payperiod = new SafePeriodFromText(
			href.single("begindate", ""),
			href.single("enddate", "")
		);
		final PaymentStatus status = PaymentStatus.valueOf(href.single("statusid", "NONE"));
		this.origin = new DbPaginedBankNotes(
		    source,
			nbperpage,
			page,
			filter,
			payperiod,
			status
		);
	}

	@Override
	public Iterable<BankNote> iterate() {
		return this.origin.iterate();
	}

	@Override
	public Iterable<BankNote> iterate(PaymentStatus status) {
		return this.origin.iterate(status);
	}

	@Override
	public BankNote get(Long id) {
		return this.origin.get(id);
	}

	@Override
	public Long count() {
		return this.origin.count();
	}

}

package io.surati.gap.payment.module.rest;

import io.surati.gap.admin.base.api.User;
import io.surati.gap.commons.utils.convert.filter.Comparator;
import io.surati.gap.commons.utils.convert.filter.Field;
import io.surati.gap.commons.utils.convert.filter.Filter;
import io.surati.gap.commons.utils.convert.filter.SorterDirection;
import io.surati.gap.commons.utils.time.Period;
import io.surati.gap.commons.utils.time.SafePeriodFromText;
import io.surati.gap.payment.base.api.ReferenceDocument;
import io.surati.gap.payment.base.api.ReferenceDocumentStatus;
import io.surati.gap.payment.base.api.ReferenceDocumentStep;
import io.surati.gap.payment.base.api.ReferenceDocuments;
import io.surati.gap.payment.base.api.Workspace;
import io.surati.gap.payment.base.db.DbPaginedReferenceDocuments;
import io.surati.gap.payment.base.db.DbWorkspace;
import io.surati.gap.payment.base.filter.ReferenceDocumentCriteria;
import io.surati.gap.payment.base.module.xe.XeReferenceDocumentsJson;
import io.surati.gap.web.base.rq.RqUser;
import java.util.Arrays;
import javax.sql.DataSource;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rq.RqHref.Smart;
import org.takes.rs.RsJson;

public final class TkReferenceDocumentToTreatSearch implements Take {

	/**
	 * Database
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkReferenceDocumentToTreatSearch(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public Response act(Request req) throws Exception {	
		final Smart href = new Smart(req);
		final String filtercontains = href.single("filtercontains", "");	
		final String filternotcontains = href.single("filternotcontains", "");
		final Long page = Long.parseLong(href.single("page"));
		final String sorterfieldid = href.single("sorterfieldid");
		final Field sorterfield = ReferenceDocument.valueOf(sorterfieldid);
		final String sorterdirectionid = href.single("sorterdirectionid");
		final SorterDirection sorterdirection = SorterDirection.valueOf(sorterdirectionid);
		final Long nbperpage = Long.parseLong(href.single("nbperpage"));
		final Period period = new SafePeriodFromText(
			href.single("editbegindate", ""),
			href.single("editenddate", "")
		);
		final ReferenceDocumentStatus status = ReferenceDocumentStatus.valueOf(href.single("statusid", "NONE"));
		final ReferenceDocumentCriteria criteria = new ReferenceDocumentCriteria();
		if(status == ReferenceDocumentStatus.NONE) {
			criteria.add(
				Arrays.asList(
					ReferenceDocumentStatus.WAITING_FOR_PAYMENT,
					ReferenceDocumentStatus.PAID_PARTIALLY
				)
			);
		} else {
			criteria.add(status);
		}
		criteria.step(ReferenceDocumentStep.TO_TREAT);
		criteria.addInterval(ReferenceDocument.DATE, period);
		criteria.addFilter(new Filter(Comparator.CONTAINS, filtercontains));
		criteria.addFilter(new Filter(Comparator.NOT_CONTAINS, filternotcontains));
		criteria.addSorter(sorterfield, sorterdirection);
		final ReferenceDocuments refdocs = new DbPaginedReferenceDocuments(
			this.source,
			nbperpage,
			page,
			criteria
		);
		final User user = new RqUser(this.source, req);
		final Workspace workspace = new DbWorkspace(this.source, user);
		return new RsJson(
			new XeReferenceDocumentsJson(
				refdocs.iterate(),
				refdocs.count(),
				refdocs.totalAmount(),
				workspace.documentsToPay().amountToPay(),
				refdocs.amountLeft()
			)
		);
	}

}

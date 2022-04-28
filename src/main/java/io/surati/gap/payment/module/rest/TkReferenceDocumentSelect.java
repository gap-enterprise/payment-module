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
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;
import javax.json.Json;
import javax.sql.DataSource;
import org.cactoos.list.ListOf;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rq.RqGreedy;
import org.takes.rq.form.RqFormSmart;
import org.takes.rs.RsJson;
import org.takes.rs.RsWithStatus;

public final class TkReferenceDocumentSelect implements Take {

	/**
	 * Database
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkReferenceDocumentSelect(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public Response act(Request req) throws Exception {	
		try {
			final RqFormSmart form = new RqFormSmart(new RqGreedy(req));
			final String filtercontains = form.single("filtercontains", "");	
			final String filternotcontains = form.single("filternotcontains", "");
			final Long page = Long.parseLong(form.single("page"));
			final String sorterfieldid = form.single("sorterfieldid");
			final Field sorterfield = ReferenceDocument.valueOf(sorterfieldid);
			final String sorterdirectionid = form.single("sorterdirectionid");
			final SorterDirection sorterdirection = SorterDirection.valueOf(sorterdirectionid);
			final Long nbperpage = Long.parseLong(form.single("nbperpage"));
			final Period period = new SafePeriodFromText(
				form.single("editbegindate", ""),
				form.single("editenddate", "")
			);
			final ReferenceDocumentStatus status = ReferenceDocumentStatus.valueOf(form.single("statusid", "NONE"));
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
			final List<String> docidstrs = new ListOf<>(form.param("refdocids[]"));
			if(docidstrs.isEmpty()) {
				workspace.documentsToPay().select(refdocs.iterate());
			} else {
				for (String docidstr : docidstrs) {
					final Long docid = Long.parseLong(docidstr);
					final ReferenceDocument document = refdocs.get(docid);
					workspace.documentsToPay().select(document);
				}
			}
			return new RsJson(
				new XeReferenceDocumentsJson(
					refdocs.iterate(),
					refdocs.count(),
					refdocs.totalAmount(),
					workspace.documentsToPay().amountToPay(),
					refdocs.amountLeft()
				)
			);
		} catch(IllegalArgumentException ex) {
			return new RsWithStatus(
				new RsJson(
					Json.createObjectBuilder()
						.add("message", ex.getLocalizedMessage())
						.build()
					),
				HttpURLConnection.HTTP_BAD_REQUEST
			);
		} catch(Exception e) {
			return new RsWithStatus(
				new RsJson(
					Json.createObjectBuilder()
					.add("message", e.getLocalizedMessage())
					.build()
				),
				HttpURLConnection.HTTP_INTERNAL_ERROR
			);
		}
	}

}

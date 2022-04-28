package io.surati.gap.payment.module.actions;

import io.surati.gap.admin.base.api.User;
import io.surati.gap.payment.base.api.ReferenceDocument;
import io.surati.gap.payment.base.api.ReferenceDocumentsToPay;
import io.surati.gap.payment.base.api.Workspace;
import io.surati.gap.payment.base.db.DbWorkspace;
import io.surati.gap.web.base.rq.RqUser;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.flash.RsFlash;
import org.takes.facets.forward.RsForward;
import org.takes.rq.RqGreedy;
import org.takes.rq.RqHref;

public final class TkReferenceDocumentWsSelect implements Take {

	/**
	 * Database
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkReferenceDocumentWsSelect(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public Response act(Request req) throws Exception {
		final RqHref.Smart href = new RqHref.Smart(new RqGreedy(req));
		final Long id = Long.parseLong(href.single("id"));
		final User user = new RqUser(this.source, req);
		final Workspace workspace = new DbWorkspace(this.source, user);
		final ReferenceDocumentsToPay documents = workspace.documentsToPay();
		final ReferenceDocument item = workspace.documentsToTreat().get(id);
		documents.select(item);
		return new RsForward(
			new RsFlash(
				"Document sélectionné avec succès !",
				Level.INFO
			),
			"/reference-document/list"
		);	
	}
}

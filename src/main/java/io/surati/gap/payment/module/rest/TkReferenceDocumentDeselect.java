package io.surati.gap.payment.module.rest;

import io.surati.gap.admin.base.api.Log;
import io.surati.gap.admin.base.api.User;
import io.surati.gap.payment.base.api.ReferenceDocument;
import io.surati.gap.payment.base.api.ReferenceDocumentsToPay;
import io.surati.gap.payment.base.api.Workspace;
import io.surati.gap.payment.base.db.DbWorkspace;
import io.surati.gap.web.base.log.RqLog;
import io.surati.gap.web.base.rq.RootPageFull;
import io.surati.gap.web.base.rq.RqUser;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.flash.RsFlash;
import org.takes.facets.forward.RsForward;
import org.takes.rq.RqHref;

public final class TkReferenceDocumentDeselect implements Take {
	
	/**
	 * Database
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkReferenceDocumentDeselect(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public Response act(Request req) throws Exception {
		final Log log = new RqLog(this.source, req);
		final Long id = Long.parseLong(new RqHref.Smart(req).single("id"));
		final User user = new RqUser(this.source, req);
		final Workspace workspace = new DbWorkspace(this.source, user);
		final ReferenceDocumentsToPay documents = workspace.documentsToPay();
		final ReferenceDocument document = documents.get(id);
		documents.deselect(document);
		log.info("Déselection du document de référence (N°=%s) du Tiers (Code=%s)", document.reference(), document.issuer().code());
		return new RsForward(
			new RsFlash(
				"Le document de référence a été retiré avec succès !",
				Level.INFO
			),
			String.format("/reference-document/selected?%s", new RootPageFull(req))
		);	
	}
}

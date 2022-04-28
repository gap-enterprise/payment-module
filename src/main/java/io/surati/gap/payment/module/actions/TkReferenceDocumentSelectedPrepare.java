package io.surati.gap.payment.module.actions;

import io.surati.gap.admin.base.api.Log;
import io.surati.gap.admin.base.api.User;
import io.surati.gap.payment.base.api.Workspace;
import io.surati.gap.payment.base.db.DbWorkspace;
import io.surati.gap.web.base.log.RqLog;
import io.surati.gap.web.base.rq.RqUser;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.flash.RsFlash;
import org.takes.facets.forward.RsForward;

public final class TkReferenceDocumentSelectedPrepare implements Take {
	
	/**
	 * Database
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkReferenceDocumentSelectedPrepare(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public Response act(Request req) throws Exception {
		final Log log = new RqLog(this.source, req);
		final User user = new RqUser(this.source, req);
		final Workspace workspace = new DbWorkspace(this.source, user);
		workspace.sendToPreparation();
		log.info("Envoi en préparation de la sélection");
		return new RsForward(
			new RsFlash(
				"La sélection a été envoyée en préparation avec succès !",
				Level.INFO
			),
			"/payment-order/to-prepare"
		);	
	}
}

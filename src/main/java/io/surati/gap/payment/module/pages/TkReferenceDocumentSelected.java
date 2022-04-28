package io.surati.gap.payment.module.pages;

import io.surati.gap.admin.base.api.User;
import io.surati.gap.commons.utils.amount.FrAmountInXof;
import io.surati.gap.payment.base.api.ReferenceDocumentsToPay;
import io.surati.gap.payment.base.api.Workspace;
import io.surati.gap.payment.base.db.DbWorkspace;
import io.surati.gap.payment.base.module.xe.XeReferenceDocuments;
import io.surati.gap.web.base.RsPage;
import io.surati.gap.web.base.rq.RqUser;
import io.surati.gap.web.base.xe.XeRootPage;
import javax.sql.DataSource;
import org.cactoos.collection.Sticky;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rs.xe.XeAppend;
import org.takes.rs.xe.XeChain;
import org.takes.rs.xe.XeSource;

public final class TkReferenceDocumentSelected implements Take {
	
	private final DataSource source;

	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkReferenceDocumentSelected(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public Response act(Request req) throws Exception {
		final User user = new RqUser(this.source, req);
		final Workspace workspace = new DbWorkspace(this.source, user);
		final ReferenceDocumentsToPay documents = workspace.documentsToPay();
		final XeSource src = new XeChain(
			new XeReferenceDocuments(documents.iterate()),
			new XeAppend("total_amount", documents.amountToPay().toString()),
			new XeAppend("total_amount_in_human", new FrAmountInXof(documents.amountToPay()).toString())
		);
		return new RsPage(
			"/io/surati/gap/payment/module/xsl/reference_document/selected_list.xsl",
			req,
			this.source,
			() -> new Sticky<>(
				new XeAppend("menu", "reference-document"),
				new XeRootPage(req),
				src
			)
		);
	}

}

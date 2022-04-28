package io.surati.gap.payment.module.pages;

import io.surati.gap.payment.base.api.ReferenceDocument;
import io.surati.gap.payment.base.api.ReferenceDocumentStatus;
import io.surati.gap.payment.base.module.xe.XeReferenceDocumentStatuss;
import io.surati.gap.payment.module.xe.XeFields;
import io.surati.gap.web.base.RsPage;
import io.surati.gap.web.base.xe.XeRootPage;
import java.util.Arrays;
import javax.sql.DataSource;
import org.cactoos.collection.Sticky;
import org.cactoos.iterable.IterableOf;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rs.xe.XeAppend;
import org.takes.rs.xe.XeChain;
import org.takes.rs.xe.XeSource;

public final class TkReferenceDocumentList implements Take {
	
	private final DataSource source;

	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkReferenceDocumentList(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public Response act(Request req) throws Exception {
		final Iterable<ReferenceDocumentStatus> status = new IterableOf<>(
			ReferenceDocumentStatus.WAITING_FOR_PAYMENT,
			ReferenceDocumentStatus.PAID_PARTIALLY
		);
		final XeSource src = new XeChain(
			new XeReferenceDocumentStatuss(status),
			new XeFields(Arrays.asList(ReferenceDocument.DATE, ReferenceDocument.BENEFICIARY, ReferenceDocument.AMOUNT))
		);
		return new RsPage(
			"/io/surati/gap/payment/module/xsl/reference_document/list.xsl",
			req,
			this.source,
			() -> new Sticky<>(
				new XeAppend("menu", "reference-document"),
				new XeRootPage(
					"Documents de référence",
					"Documents de référence à traiter",
					req
				),
				src
			)
		);
	}

}

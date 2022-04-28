package io.surati.gap.payment.module.xe;

import io.surati.gap.payment.base.api.PaymentCancelReason;
import org.takes.rs.xe.XeDirectives;
import org.takes.rs.xe.XeWrap;
import org.xembly.Directives;

public final class XePaymentCancelReason extends XeWrap {

	public XePaymentCancelReason(final PaymentCancelReason reason) {
		this("bank_note_cancel_reason", reason);
	}

	public XePaymentCancelReason(final String name, final PaymentCancelReason reason) {
		super(
			new XeDirectives(
				new Directives()
				.add(name)
					.add("id").set(reason.name()).up()
					.add("name").set(reason.toString()).up()
				.up()
			)
		);
	}
}

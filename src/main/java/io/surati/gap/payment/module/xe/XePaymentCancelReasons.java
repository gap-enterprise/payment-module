package io.surati.gap.payment.module.xe;

import io.surati.gap.payment.base.api.PaymentCancelReason;
import java.util.Collection;
import java.util.LinkedList;
import org.cactoos.collection.Mapped;
import org.cactoos.iterable.Joined;
import org.takes.rs.xe.XeDirectives;
import org.takes.rs.xe.XeWrap;
import org.xembly.Directive;
import org.xembly.Directives;

public final class XePaymentCancelReasons extends XeWrap {

	public XePaymentCancelReasons() {
		this(withoutNone());
	}
	public XePaymentCancelReasons(final Iterable<PaymentCancelReason> items) {
		super(
			new XeDirectives(
				new Directives()
					.add("bank_note_cancel_reasons")
					.append(
						new Joined<>(
							new Mapped<PaymentCancelReason, Iterable<Directive>>(
								item -> new XePaymentCancelReason("bank_note_cancel_reason", item).toXembly(),
								items
							)
						)
					)
			)
		);
	}
	
	private final static Iterable<PaymentCancelReason> withoutNone() {
		final Collection<PaymentCancelReason> status = new LinkedList<>();
		for (PaymentCancelReason st : PaymentCancelReason.values()) {
			if(st != PaymentCancelReason.NONE) {
				status.add(st);
			}
		}
		return status;
	}
}

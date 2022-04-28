package io.surati.gap.payment.module.xe;

import io.surati.gap.payment.base.api.PaymentMeanType;
import org.cactoos.collection.Mapped;
import org.cactoos.iterable.Joined;
import org.takes.rs.xe.XeDirectives;
import org.takes.rs.xe.XeWrap;
import org.xembly.Directive;
import org.xembly.Directives;

public final class XePaymentMeanTypes extends XeWrap {

	public XePaymentMeanTypes(final Iterable<PaymentMeanType> items) {
		super(
			new XeDirectives(
				new Directives()
					.add("payment_mean_types")
					.append(
						new Joined<>(
							new Mapped<PaymentMeanType, Iterable<Directive>>(
								item -> new XePaymentMeanType("payment_mean_type", item).toXembly(),
								items
							)
						)
					)
			)
		);
	}
}

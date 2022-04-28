package io.surati.gap.payment.module.xe;

import io.surati.gap.payment.base.api.PaymentMean;
import org.cactoos.collection.Mapped;
import org.cactoos.iterable.Joined;
import org.takes.rs.xe.XeDirectives;
import org.takes.rs.xe.XeWrap;
import org.xembly.Directives;

public final class XePaymentMeans extends XeWrap {

	public XePaymentMeans(final Iterable<PaymentMean> items) {
		super(
			new XeDirectives(
				new Directives()
					.add("payment_means")
					.append(
						new Joined<>(
							new Mapped<>(
								item -> new XePaymentMean("payment_mean", item).toXembly(),
								items
							)
						)
					)
			)
		);
	}
}

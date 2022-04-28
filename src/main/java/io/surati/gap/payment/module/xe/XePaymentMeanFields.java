package io.surati.gap.payment.module.xe;

import io.surati.gap.payment.base.api.PaymentMeanField;
import org.cactoos.collection.Mapped;
import org.cactoos.iterable.Joined;
import org.takes.rs.xe.XeDirectives;
import org.takes.rs.xe.XeWrap;
import org.xembly.Directives;

public final class XePaymentMeanFields extends XeWrap {

	public XePaymentMeanFields(final Iterable<PaymentMeanField> items) {
		super(
			new XeDirectives(
				new Directives()
					.add("payment_mean_fields")
					.append(
						new Joined<>(
							new Mapped<>(
								item -> new XePaymentMeanField("payment_mean_field", item).toXembly(),
								items
							)
						)
					)
			)
		);
	}
}

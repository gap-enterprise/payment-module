package io.surati.gap.payment.module.xe;

import io.surati.gap.commons.utils.amount.FrAmountInXof;
import io.surati.gap.payment.base.api.PaymentOrderGroup;
import org.cactoos.collection.Mapped;
import org.cactoos.iterable.Joined;
import org.takes.rs.xe.XeDirectives;
import org.takes.rs.xe.XeWrap;
import org.xembly.Directives;

public final class XePaymentOrderGroups extends XeWrap {

	public XePaymentOrderGroups(final Iterable<PaymentOrderGroup> items, final Double totalamount) {
		super(
			new XeDirectives(
				new Directives()
				    .add("total_amount").set(new FrAmountInXof(totalamount)).up()
					.add("payment_order_groups")
					.append(
						new Joined<>(
							new Mapped<>(
								item -> new XePaymentOrderGroup("payment_order_group", item).toXembly(),
								items
							)
						)
					)
			)
		);
	}
}

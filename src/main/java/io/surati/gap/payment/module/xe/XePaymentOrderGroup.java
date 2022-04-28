/**
MIT License

Copyright (c) 2021 Surati

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package io.surati.gap.payment.module.xe;

import io.surati.gap.commons.utils.amount.FrAmountInXof;
import io.surati.gap.payment.base.api.BankAccount;
import io.surati.gap.payment.base.api.PaymentOrderGroup;
import io.surati.gap.payment.base.module.xe.XePaymentOrder;
import org.cactoos.collection.Mapped;
import org.cactoos.iterable.Joined;
import org.takes.rs.xe.XeDirectives;
import org.takes.rs.xe.XeWrap;
import org.xembly.Directives;

/**
 * Xml Payment order group.
 *
 * @since 3.0
 */
public final class XePaymentOrderGroup extends XeWrap {

	public XePaymentOrderGroup(final PaymentOrderGroup group) {
		this("item", group);
	}

	public XePaymentOrderGroup(final String name, final PaymentOrderGroup group) {
		super(XePaymentOrderGroup.convert(name, group));
	}
	
	private static XeDirectives convert(
		final String name,
		final PaymentOrderGroup group
	) {
		final String accountname;
		final BankAccount account = group.accountToUse();
		if(account == BankAccount.EMPTY) {
			accountname = "Aucun compte sélectionné";
		} else {
			accountname = String.format(
				"%s - %s",
				group.accountToUse().bank().abbreviated(),
				group.accountToUse().rib()
			);
		}
		return new XeDirectives(
			new Directives()
				.add(name)
					.add("id").set(group.id()).up()
					.add("is_hetero").set(group.isHetero()).up()
					.add("beneficiary").set(group.beneficiary().abbreviated()).up()
					.add("beneficiary_id").set(group.beneficiary().id()).up()
					.add("account_id").set(group.accountToUse().id()).up()
					.add("account").set(accountname).up()
					.add("total_amount").set(group.totalAmount()).up()
					.add("total_amount_in_human").set(new FrAmountInXof(group.totalAmount())).up()
					.add("payment_orders")
					.append(
							new Joined<>(
								new Mapped<>(
									item -> new XePaymentOrder("payment_order", item).toXembly(),
									group.iterate()
								)
							)
				     ).up()
				.up()
			);
	}
}

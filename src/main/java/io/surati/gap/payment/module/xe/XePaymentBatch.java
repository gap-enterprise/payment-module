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

import io.surati.gap.payment.base.api.BankAccount;
import io.surati.gap.payment.base.api.PaymentBatch;
import io.surati.gap.payment.base.module.xe.XePayment;
import java.time.format.DateTimeFormatter;
import org.cactoos.collection.Mapped;
import org.cactoos.iterable.Joined;
import org.takes.rs.xe.XeDirectives;
import org.takes.rs.xe.XeWrap;
import org.xembly.Directives;

/**
 * Xml Payment batch.
 *
 * @since 3.0
 */
public final class XePaymentBatch extends XeWrap {

	public XePaymentBatch(final PaymentBatch batch) {
		this("item", batch);
	}

	public XePaymentBatch(final String name, final PaymentBatch batch) {
		super(XePaymentBatch.convert(name, batch));
	}
	
	private static XeDirectives convert(
		final String name,
		final PaymentBatch batch
	) {
		final BankAccount account = batch.account();
		return new XeDirectives(
			new Directives()
				.add(name)
					.add("id").set(batch.id()).up()
					.add("date").set(batch.date().format(DateTimeFormatter.ISO_DATE)).up()
					.add("date_view").set(batch.date().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).up()
					.add("account").set(
						String.format(
							"%s - %s",
							account.bank().abbreviated(),
							account.rib()
						)
					).up()
					.add("account_id").set(account.id()).up()
					.add("mean_type_id").set(batch.meanType().name()).up()
					.add("mean_type").set(batch.meanType()).up()
					.add("status_id").set(batch.status().name()).up()
					.add("status").set(batch.status()).up()
					.add("notes")
					.append(
							new Joined<>(
								new Mapped<>(
									item -> new XePayment("note", item).toXembly(),
									batch.notes()
								)
							)
				     ).up()
				.up()
			);
	}
}

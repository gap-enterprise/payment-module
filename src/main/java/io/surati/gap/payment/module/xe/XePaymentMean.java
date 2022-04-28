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

import io.surati.gap.payment.base.api.PaymentMean;
import org.takes.rs.xe.XeDirectives;
import org.takes.rs.xe.XeWrap;
import org.xembly.Directives;

/**
 * Xml Bank account.
 *
 * @since 3.0
 */
public final class XePaymentMean extends XeWrap {

	public XePaymentMean(final PaymentMean mean) {
		this("item", mean);
	}

	public XePaymentMean(final String name, final PaymentMean mean) {
		super(
			new XeDirectives(
				new Directives()
				.add(name)
					.add("id").set(mean.id()).up()
					.add("name").set(mean.name()).up()
					.add("type_id").set(mean.type().name()).up()
					.add("bank").set(mean.bank().abbreviated()).up()
					.add("bank_id").set(mean.bank().id()).up()
					.add("image_file_name").set("").up()
					.add("width").set(mean.image().width()).up()
					.add("height").set(mean.image().height()).up()
					.add("width_px").set(mean.image().widthpx()).up()
					.add("height_px").set(mean.image().heightpx()).up()
					.add("dpi").set(mean.image().dpi()).up()
				.up()
			)
		);
	}
}

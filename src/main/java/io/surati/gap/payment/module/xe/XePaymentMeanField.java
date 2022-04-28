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

import io.surati.gap.commons.utils.convert.CentimeterToPixel;
import io.surati.gap.payment.base.api.PaymentMeanField;
import org.takes.rs.xe.XeDirectives;
import org.takes.rs.xe.XeWrap;
import org.xembly.Directives;

/**
 * Xml Bank account.
 *
 * @since 3.0
 */
public final class XePaymentMeanField extends XeWrap {

	public XePaymentMeanField(final PaymentMeanField field) {
		this("item", field);
	}

	public XePaymentMeanField(final String name, final PaymentMeanField field) {
		super(
			new XeDirectives(
				new Directives()
				.add(name)
					.add("mean_id").set(field.mean().id()).up()
					.add("mean").set(field.mean().name()).up()
					.add("name").set(field.name()).up()
					.add("type_id").set(field.type().name()).up()
					.add("bank").set(field.mean().bank().abbreviated()).up()
					.add("bank_id").set(field.mean().bank().id()).up()
					.add("x").set(field.location().x()).up()
					.add("y").set(field.location().y()).up()
					.add("width").set(field.width()).up()
					.add("x_px").set(new CentimeterToPixel(field.mean().image().dpi(), field.location().x()).value()).up()
					.add("y_px").set(new CentimeterToPixel(field.mean().image().dpi(), field.location().y()).value()).up()
					.add("width_px").set(new CentimeterToPixel(field.mean().image().dpi(), field.width()).value()).up()
					.add("visible").set(field.visible()).up()
				.up()
			)
		);
	}
}

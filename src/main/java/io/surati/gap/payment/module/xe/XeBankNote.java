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

import com.minlessika.map.CleanMap;
import io.surati.gap.commons.utils.convert.FrShortDateFormat;
import io.surati.gap.payment.base.api.BankNote;
import java.time.format.DateTimeFormatter;
import org.takes.rs.xe.XeDirectives;
import org.takes.rs.xe.XeWrap;
import org.xembly.Directives;

/**
 * Xml Bank note.
 *
 * @since 3.0
 */
public final class XeBankNote extends XeWrap {

	public XeBankNote(final BankNote note) {
		this("item", note);
	}

	public XeBankNote(final String name, final BankNote note) {
		super(
			new XeDirectives(
				new Directives()
				.add(name)
				.add(
					new CleanMap<>()
						.add("id", note.id())
						.add("date", note.date().format(DateTimeFormatter.ISO_DATE))
						.add("date_view", new FrShortDateFormat().convert(note.date()))
						.add("duedate", note.dueDate() == null ? null : note.dueDate().format(DateTimeFormatter.ISO_DATE))
						.add("duedate_view", note.dueDate() == null ? null : new FrShortDateFormat().convert(note.dueDate()))
						.add("note_id", note.id())
						.add("note", note.name())
						.add("reference", note.internalReference())
						.add("number", note.issuerReference())
						.add("book_id", note.book().id())
						.add("book", note.book().name())
						.add("amount", note.amount().toString())
						.add("amount_in_human", note.amountInHuman())
						.add("amount_in_letters", note.amountInLetters())
						.add("issuer_id", note.issuer().id())
						.add("issuer", note.issuer().name())
						.add("status_id", note.status().name())
						.add("status", note.status().toString())
						.add("beneficiary", note.beneficiary().name())
						.add("beneficiary_id", note.beneficiary().id())
						.add("place", note.place())
						.add("cancel_reason", note.reasonOfCancel())
						.add("mean_type", note.book().meanType().toString())
						.add("mean_type_id", note.book().meanType().name())
						.add("cancel_date", note.cancelDate() == null ? null : note.cancelDate().format(DateTimeFormatter.ISO_DATE_TIME))
						.add("cancel_date_view", note.cancelDate() == null ? null : new FrShortDateFormat().convert(note.cancelDate()))
				)
				.up()
			)
		);
	}
}

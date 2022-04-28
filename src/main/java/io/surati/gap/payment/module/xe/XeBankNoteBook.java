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

import io.surati.gap.payment.base.api.BankNoteBook;
import org.takes.rs.xe.XeDirectives;
import org.takes.rs.xe.XeWrap;
import org.xembly.Directives;

/**
 * Xml Bank note book.
 *
 * @since 3.0
 */
public final class XeBankNoteBook extends XeWrap {

	public XeBankNoteBook(final BankNoteBook book) {
		this("item", book);
	}

	public XeBankNoteBook(final String name, final BankNoteBook book) {
		super(
			new XeDirectives(
				new Directives()
				.add(name)
					.add("id").set(book.id()).up()
					.add("name").set(book.name()).up()
					.add("name_with_current_note").set(
						String.format(
							"%s - %s - %s N°%s",
							book.account().bank().abbreviated(),
							book.account().rib(),
							book.meanType().toString(),
							book.currentNumber()
						)
					).up()
					.add("bank").set(book.account().bank().abbreviated()).up()
					.add("bank_id").set(book.account().bank().id()).up()
					.add("account_id").set(book.account().id()).up()
					.add("rib").set(book.account().rib()).up()
					.add("prefix_number").set(book.prefixNumber()).up()
					.add("start_number").set(book.startNumber()).up()
					.add("end_number").set(book.endNumber()).up()
					.add("start_number_view").set(String.format("%s%s", book.prefixNumber(), book.startNumber())).up()
					.add("end_number_view").set(String.format("%s%s", book.prefixNumber(), book.endNumber())).up()
					.add("current_number").set(book.currentNumber()).up()
					.add("number_of_leaves").set(book.totalNumberOfNotes()).up()
					.add("number_of_leaves_used").set(book.numberOfNotesUsed()).up()
					.add("number_of_leaves_left").set(book.totalNumberOfNotes() - book.numberOfNotesUsed()).up()
					.add("mean_type").set(book.meanType().toString()).up()
					.add("mean_type_id").set(book.meanType().name()).up()
					.add("status").set(book.status().toString()).up()
					.add("status_id").set(book.status().name()).up()
				.up()
			)
		);
	}
}

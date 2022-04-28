package io.surati.gap.payment.module.xe;

import io.surati.gap.payment.base.api.BankNoteBook;
import io.surati.gap.payment.base.api.BankNoteBooks;
import org.cactoos.collection.Mapped;
import org.cactoos.iterable.Joined;
import org.takes.rs.xe.XeDirectives;
import org.takes.rs.xe.XeWrap;
import org.xembly.Directive;
import org.xembly.Directives;

public final class XeBankNoteBooks extends XeWrap {
	
	public XeBankNoteBooks(final BankNoteBooks books) {
		this(books.iterate());
	}
	
	public XeBankNoteBooks(final Iterable<BankNoteBook> items) {
		super(
			new XeDirectives(
				new Directives()
					.add("bank_note_books")
					.append(
						new Joined<>(
							new Mapped<BankNoteBook, Iterable<Directive>>(
								item -> new XeBankNoteBook("bank_note_book", item).toXembly(),
								items
							)
						)
					)
			)
		);
	}
}

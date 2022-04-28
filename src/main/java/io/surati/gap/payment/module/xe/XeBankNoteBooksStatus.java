package io.surati.gap.payment.module.xe;

import io.surati.gap.payment.base.api.BankNoteBookStatus;
import org.takes.rs.xe.XeDirectives;
import org.takes.rs.xe.XeWrap;
import org.xembly.Directives;

public final class XeBankNoteBooksStatus extends XeWrap {

	public XeBankNoteBooksStatus(final BankNoteBookStatus status) {
		this("bank_note_books_status", status);
	}

	public XeBankNoteBooksStatus(final String name, final BankNoteBookStatus status) {
		super(
			new XeDirectives(
				new Directives()
				.add(name)
					.add("id").set(status.name()).up()
					.add("name").set(status.toString()).up()
				.up()
			)
		);
	}
}

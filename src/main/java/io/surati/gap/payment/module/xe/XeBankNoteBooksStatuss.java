package io.surati.gap.payment.module.xe;

import io.surati.gap.payment.base.api.BankNoteBookStatus;
import java.util.Collection;
import java.util.LinkedList;
import org.cactoos.collection.Mapped;
import org.cactoos.iterable.Joined;
import org.takes.rs.xe.XeDirectives;
import org.takes.rs.xe.XeWrap;
import org.xembly.Directives;

public final class XeBankNoteBooksStatuss extends XeWrap {

	public XeBankNoteBooksStatuss() {
		this(withoutNone());
	}
	public XeBankNoteBooksStatuss(final Iterable<BankNoteBookStatus> items) {
		super(
			new XeDirectives(
				new Directives()
					.add("bank_note_books_statuss")
					.append(
						new Joined<>(
							new Mapped<>(
								item -> new XeBankNoteBooksStatus("bank_note_books_status", item).toXembly(),
								items
							)
						)
					)
			)
		);
	}
	
	private final static Iterable<BankNoteBookStatus> withoutNone() {
		final Collection<BankNoteBookStatus> status = new LinkedList<>();
		for (BankNoteBookStatus st : BankNoteBookStatus.values()) {
			if(st != BankNoteBookStatus.NONE) {
				status.add(st);
			}
		}
		return status;
	}
}

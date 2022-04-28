package io.surati.gap.payment.module.xe;

import io.surati.gap.payment.base.api.BankNote;
import org.cactoos.collection.Mapped;
import org.cactoos.iterable.Joined;
import org.takes.rs.xe.XeDirectives;
import org.takes.rs.xe.XeWrap;
import org.xembly.Directives;

public final class XeBankNotes extends XeWrap {
	
	public XeBankNotes(final Iterable<BankNote> items) {
		super(
			new XeDirectives(
				new Directives()
					.add("bank_notes")
					.append(
						new Joined<>(
							new Mapped<>(
								item -> new XeBankNote("bank_note", item).toXembly(),
								items
							)
						)
					)
			)
		);
	}
}

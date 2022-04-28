package io.surati.gap.payment.module.xe;

import io.surati.gap.payment.base.api.BankAccount;
import io.surati.gap.payment.base.api.BankAccounts;
import org.cactoos.collection.Mapped;
import org.cactoos.iterable.Joined;
import org.takes.rs.xe.XeDirectives;
import org.takes.rs.xe.XeWrap;
import org.xembly.Directive;
import org.xembly.Directives;

public final class XeBankAccounts extends XeWrap {
	
	public XeBankAccounts(final BankAccounts accounts) {
		this(accounts.iterate());
	}
	
	public XeBankAccounts(final Iterable<BankAccount> items) {
		super(
			new XeDirectives(
				new Directives()
					.add("bank_accounts")
					.append(
						new Joined<>(
							new Mapped<BankAccount, Iterable<Directive>>(
								item -> new XeBankAccount("bank_account", item).toXembly(),
								items
							)
						)
					)
			)
		);
	}
}

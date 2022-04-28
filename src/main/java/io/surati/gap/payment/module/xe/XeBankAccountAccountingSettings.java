package io.surati.gap.payment.module.xe;

import io.surati.gap.payment.base.api.BankAccountAccountingSetting;
import org.cactoos.collection.Mapped;
import org.cactoos.iterable.Joined;
import org.takes.rs.xe.XeDirectives;
import org.takes.rs.xe.XeWrap;
import org.xembly.Directive;
import org.xembly.Directives;

public final class XeBankAccountAccountingSettings extends XeWrap {
	
	public XeBankAccountAccountingSettings(final Iterable<BankAccountAccountingSetting> items) {
		super(
			new XeDirectives(
				new Directives()
					.add("bank_account_accounting_settings")
					.append(
						new Joined<>(
							new Mapped<BankAccountAccountingSetting, Iterable<Directive>>(
								item -> new XeBankAccountAccountingSetting("bank_account_accounting_setting", item).toXembly(),
								items
							)
						)
					)
			)
		);
	}
}

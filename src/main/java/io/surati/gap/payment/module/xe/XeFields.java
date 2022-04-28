package io.surati.gap.payment.module.xe;

import io.surati.gap.commons.utils.convert.filter.Field;
import org.cactoos.collection.Mapped;
import org.cactoos.iterable.Joined;
import org.takes.rs.xe.XeDirectives;
import org.takes.rs.xe.XeWrap;
import org.xembly.Directives;

public final class XeFields extends XeWrap {

	public XeFields(final Iterable<Field> items) {
		super(
			new XeDirectives(
				new Directives()
					.add("fields")
					.append(
						new Joined<>(
							new Mapped<>(
								item -> new XeField("field", item).toXembly(),
								items
							)
						)
					)
			)
		);
	}
}

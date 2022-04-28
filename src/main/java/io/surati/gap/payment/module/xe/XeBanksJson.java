package io.surati.gap.payment.module.xe;

import io.surati.gap.payment.base.api.Bank;
import java.io.IOException;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonStructure;
import org.takes.rs.RsJson;

public final class XeBanksJson implements RsJson.Source {

	private final Long count;
	private final Iterable<Bank> items;
	
	public XeBanksJson(final Iterable<Bank> items, final Long count) {
		this.count = count;
		this.items = items;
	}
	
	@Override
	public JsonStructure toJson() throws IOException {
		return Json.createObjectBuilder()
		   .add("items", toJson(this.items))
           .add("count", this.count)
		   .build();
	}
	
	private static JsonArray toJson(final Iterable<Bank> items) throws IOException {
		JsonArrayBuilder builder = Json.createArrayBuilder();
		for (Bank item : items) {
			builder.add(Json.createObjectBuilder()
				.add("id", item.id())
				.add("code", item.code())
                .add("name", item.name())
                .add("abbreviated", item.abbreviated())
           );
		}
		return builder.build();
	}
}

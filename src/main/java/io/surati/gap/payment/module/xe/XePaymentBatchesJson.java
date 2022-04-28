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

import io.surati.gap.commons.utils.amount.FrAmountInXof;
import io.surati.gap.payment.base.api.BankAccount;
import io.surati.gap.payment.base.api.PaymentBatch;
import io.surati.gap.payment.base.api.PaymentOrderGroup;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonStructure;
import org.takes.rs.RsJson;

public final class XePaymentBatchesJson implements RsJson.Source {

	private final Iterable<PaymentBatch> items;
	
	private final Double totamount;
	
	public XePaymentBatchesJson(final Iterable<PaymentBatch> items, final Double totamount) {
		this.items = items;
		this.totamount = totamount;
	}
	
	@Override
	public JsonStructure toJson() throws IOException {
		final JsonArrayBuilder builder = Json.createArrayBuilder();
		for (PaymentBatch item : this.items) {
			final BankAccount account = item.account();
			builder.add(Json.createObjectBuilder()
				.add("id", item.id())
				.add("date", item.date().format(DateTimeFormatter.ISO_DATE))
				.add("date_view", item.date().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
				.add(
					"account",
					String.format(
						"%s - %s",
						account.bank().abbreviated(),
						account.rib()
					)
				)
				.add("account_id", account.id())
                .add("mean_type", item.meanType().toString())
                .add("mean_type_id", item.meanType().name())
                .add("groups", toJson(item.groups()))
           );
		}
		return Json.createObjectBuilder()
		   .add("items", builder)
		   .add("total_amount", totamount)
			.add(
				"total_amount_in_human",
				new FrAmountInXof(totamount).toString()
			)
		   .build();
	}
	
	private static JsonArray toJson(final Iterable<PaymentOrderGroup> items) throws IOException {
		JsonArrayBuilder builder = Json.createArrayBuilder();
		for (PaymentOrderGroup item : items) {
			builder.add(
				new XePaymentOrderGroupJson(item).toJson()
			);
		}
		return builder.build();
	}
}

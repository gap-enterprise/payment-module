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
import io.surati.gap.commons.utils.convert.FrShortDateFormat;
import io.surati.gap.payment.base.api.BankAccount;
import io.surati.gap.payment.base.api.PaymentMeanType;
import io.surati.gap.payment.base.api.PaymentOrder;
import io.surati.gap.payment.base.api.PaymentOrderGroup;
import io.surati.gap.payment.base.api.ReferenceDocument;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonStructure;
import org.apache.commons.lang.StringUtils;
import org.takes.rs.RsJson;

public final class XePaymentOrderGroupJson implements RsJson.Source {

	private final PaymentOrderGroup item;
	
	public XePaymentOrderGroupJson(final PaymentOrderGroup item) {
		this.item = item;
	}
	
	@Override
	public JsonStructure toJson() throws IOException {
		final String accountname;
		final BankAccount account = item.accountToUse();
		if(account == BankAccount.EMPTY) {
			accountname = "Aucun compte sélectionné";
		} else {
			accountname = String.format(
				"%s - %s",
				item.accountToUse().bank().abbreviated(),
				item.accountToUse().rib()
			);
		}
		final int deadline = item.beneficiary().paymentCondition().deadline();
		final LocalDate duedateexpected = LocalDate.now().plusDays(deadline);
		final String duedateexpectedstr = String.format(
			"Date prévue le %s (à %s jours)",
			duedateexpected.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
			deadline
		);
		return Json.createObjectBuilder()
			.add("id", item.id())
			.add("beneficiary", item.beneficiary().abbreviated())
			.add("is_hetero", item.isHetero())
            .add("beneficiary_id", item.beneficiary().id())
            .add("account_id", item.accountToUse().id())
            .add("account", accountname)
            .add("total_amount_in_human", new FrAmountInXof(item.totalAmount()).toString())
            .add("total_amount", item.totalAmount())
            .add("mean_type", item.meanType().toString())
            .add("mean_type_id", item.meanType().name())
            .add("due_date", item.dueDate() == LocalDate.MIN ? StringUtils.EMPTY : item.dueDate().format(DateTimeFormatter.ISO_DATE))
            .add("due_date_view", item.dueDate() == LocalDate.MIN ? StringUtils.EMPTY : new FrShortDateFormat().convert(item.dueDate()))
            .add("orders", ordersToJson(item.iterate()))
            .add("due_date_expected", duedateexpectedstr)
            .add(
        		"payment_mean_types_accepted",
        		paymentMeansToJson(item.beneficiary().paymentCondition().meanTypesAllowed())	
        	)
            .build();
	}
	
	private static JsonArray ordersToJson(final Iterable<PaymentOrder> items) throws IOException {
		JsonArrayBuilder builder = Json.createArrayBuilder();
		for (PaymentOrder item : items) {
			final String refdocdateview;
			final String refdocname;
			if(item.document() == ReferenceDocument.EMPTY) {
				refdocdateview = "Aucune";
				refdocname = "Aucun";
			} else {
				refdocdateview = new FrShortDateFormat().convert(item.document().date());
				refdocname = String.format("%s N°%s", item.document().type().toString(), item.document().reference());
			}
			builder.add(Json.createObjectBuilder()
				.add("id", item.id())
				.add("date_view", new FrShortDateFormat().convert(item.date()))
				.add("ref_doc_date_view", refdocdateview)
				.add("reference", item.reference())
				.add("beneficiary", item.beneficiary().abbreviated())
				.add("amount_to_pay", item.amountToPay())
                .add("amount_to_pay_in_human", item.amountToPayInHuman())
                .add("ref_doc_name", refdocname)
                .add("status", item.status().toString())
           );
		}
		return builder.build();
	}
	
	private static JsonArray paymentMeansToJson(final Iterable<PaymentMeanType> items) throws IOException {
		JsonArrayBuilder builder = Json.createArrayBuilder();
		for (PaymentMeanType item : items) {
			builder.add(Json.createObjectBuilder()
				.add("id", item.name())
				.add("name", item.toString())
           );
		}
		return builder.build();
	}
}

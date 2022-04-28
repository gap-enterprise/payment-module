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
package io.surati.gap.payment.module.actions;

import io.surati.gap.web.base.rq.RootPageFull;
import javax.sql.DataSource;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.forward.RsForward;
import org.takes.rq.RqGreedy;
import org.takes.rq.form.RqFormSmart;

/**
 * Take that chooses a book for a new batch payment.
 *
 * <p>The class is immutable and thread-safe.</p>
 *
 * @since 3.0
 */

public class TkPaymentBatchNewBookChoose implements Take  {

	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkPaymentBatchNewBookChoose(final DataSource source) {
		
	}
	
	@Override
	public Response act(Request req) throws Exception {
		final RqFormSmart form = new RqFormSmart(new RqGreedy(req));
		final StringBuilder queries = new StringBuilder();
		queries.append(
			String.format(
				"account=%s&meantype=%s",
				form.single("account_id"),
				form.single("mean_type_id")
			)
		);
		boolean hasbook = false;
		for (String name : form.names()) {
			if(name.startsWith("book-")) {
				final boolean ischecked = Boolean.parseBoolean(form.single(name));
				if(ischecked) {
					final String bookid = name.split("-")[1];
					queries.append(String.format("&book=%s", bookid));
					hasbook = true;
				}
			}
		}
		if(!hasbook) {
			throw new IllegalArgumentException("Vous devez choisir au moins un carnet !");
		}
		return new RsForward(
			String.format("/payment/batch/new/edit?%s&%s", queries, new RootPageFull(req))
		);
	}
}

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

import io.surati.gap.payment.base.api.Bank;
import io.surati.gap.payment.base.api.Banks;
import io.surati.gap.payment.base.db.DbBanks;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.flash.RsFlash;
import org.takes.facets.forward.RsForward;
import org.takes.rq.RqGreedy;
import org.takes.rq.form.RqFormSmart;

/**
 * Take that creates or modifies a bank.
 *
 * <p>The class is immutable and thread-safe.</p>
 *
 * @since 0.1.0
 */


public final class TkBankSave implements Take {

	/**
	 * Database
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkBankSave(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public Response act(Request req) throws Exception {		
		final RqFormSmart form = new RqFormSmart(new RqGreedy(req));
		final Long id = Long.parseLong(form.single("id", "0"));		
		final String code = form.single("code");
		final String name = form.single("name");
		final String abbreviated = form.single("abbreviated");
		final String representative = form.single("representative","");
		final String headquarters = form.single("headquarters","");
		final String representativePosition = form.single("representative_position","");
		final String representativeCivility = form.single("representative_civility","");
		final Banks items = new DbBanks(this.source);
		final String msg;
		final Bank item;
		if(id.equals(0L)) {
			item = items.add(code, name, abbreviated);
			msg = String.format("La banque %s a été créée avec succès !", name);
		} else {
			item = items.get(id);
			item.update(code, name, abbreviated);
			msg = String.format("La banque %s a été modifiée avec succès !", name);
		}
		item.headquarters(headquarters);
		item.representative(representative, representativePosition, representativeCivility);
		return new RsForward(
			new RsFlash(
				msg,
				Level.INFO
			),
			String.format("/bank/view?id=%s", item.id())
		);	
	}
}

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
package io.surati.gap.payment.module.pages;

import io.surati.gap.admin.base.api.User;
import io.surati.gap.payment.base.api.BankAccount;
import io.surati.gap.payment.base.api.BankNoteBook;
import io.surati.gap.payment.base.api.BankNoteBookStatus;
import io.surati.gap.payment.base.api.PaymentBatch;
import io.surati.gap.payment.base.api.PaymentBatches;
import io.surati.gap.payment.base.api.PaymentMeanType;
import io.surati.gap.payment.base.api.PaymentOrderGroup;
import io.surati.gap.payment.base.api.ThirdParty;
import io.surati.gap.payment.base.api.Workspace;
import io.surati.gap.payment.base.db.DbBankNoteBooks;
import io.surati.gap.payment.base.db.DbCompanyBankAccounts;
import io.surati.gap.payment.base.db.DbWorkspace;
import io.surati.gap.payment.base.impl.PaymentBatchesImpl;
import io.surati.gap.web.base.rq.RootPageFull;
import io.surati.gap.web.base.rq.RqUser;
import java.util.Collection;
import java.util.LinkedList;
import javax.sql.DataSource;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.forward.RsForward;
import org.takes.rq.RqHref;

/**
 * Take that edits a pay action for a batch payment.
 *
 * <p>The class is immutable and thread-safe.</p>
 *
 * @since 3.0
 */
public final class TkPaymentBatchNew implements Take {

	/**
	 * DataSource
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkPaymentBatchNew(final DataSource source) {
		this.source = source;
	}

	@Override
	public Response act(Request req) throws Exception {
		final RqHref.Smart hqref = new RqHref.Smart(req);
		final Long accountid = Long.parseLong(hqref.single("account"));
		final BankAccount account = new DbCompanyBankAccounts(this.source).get(accountid);
		final PaymentMeanType meantype = PaymentMeanType.valueOf(hqref.single("meantype"));
		final User user = new RqUser(this.source, req);
		final Workspace workspace = new DbWorkspace(this.source, user);
		final PaymentBatches batches = new PaymentBatchesImpl(workspace.ordersToExecute());
		final PaymentBatch batch = batches.get(account, meantype);
		for (PaymentOrderGroup group : batch.groups()) {
			final ThirdParty beneficiary = group.beneficiary();
			if(!beneficiary.paymentCondition().has(meantype)) {
				throw new IllegalArgumentException(
					String.format(
						"Le bénéficiaire %s n'est pas autorisé à être payé par %s !",
						beneficiary.abbreviated(),
						meantype
					)
				);
			}
		}
		final Iterable<BankNoteBook> books = new DbBankNoteBooks(this.source).iterate(BankNoteBookStatus.IN_USE);
		final Collection<BankNoteBook> bookstouse = new LinkedList<>();
		for (BankNoteBook bk : books) {
			if(!bk.account().id().equals(account.id())) {
				continue;
			}
			if(bk.meanType() != meantype) {
				continue;
			}
			bookstouse.add(bk);
		}
		final int size = bookstouse.size();
		if(size == 0) {
			throw new IllegalArgumentException(
				String.format(
					"Vous ne disposez pas de carnet de formule %s en utilisation pour la banque %s !",
					meantype,
					account.bank().abbreviated()
				)
			);
		} else if(size == 1) {
			final BankNoteBook book = bookstouse.iterator().next();
			return new RsForward(
				String.format("/payment/batch/new/edit?account=%s&meantype=%s&book=%s&%s", accountid, meantype.name(), book.id(), new RootPageFull(req))
			);
		} else {
			return new RsForward(
				String.format("/payment/batch/new/book/edit?account=%s&meantype=%s&%s", accountid, meantype.name(), new RootPageFull(req))
			);
		}
	}
}

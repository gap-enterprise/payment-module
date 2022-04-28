package io.surati.gap.payment.module.rest;

import io.surati.gap.admin.base.api.Log;
import io.surati.gap.admin.base.api.User;
import io.surati.gap.commons.utils.amount.FrAmountInXof;
import io.surati.gap.payment.base.api.BankAccount;
import io.surati.gap.payment.base.api.BankAccounts;
import io.surati.gap.payment.base.api.PaymentMeanType;
import io.surati.gap.payment.base.api.PaymentOrderGroup;
import io.surati.gap.payment.base.api.Workspace;
import io.surati.gap.payment.base.db.DbCompanyBankAccounts;
import io.surati.gap.payment.base.db.DbWorkspace;
import io.surati.gap.payment.module.xe.XePaymentOrderGroupJson;
import io.surati.gap.web.base.log.RqLog;
import io.surati.gap.web.base.rq.RqUser;
import java.net.HttpURLConnection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.json.Json;
import javax.sql.DataSource;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rq.RqGreedy;
import org.takes.rq.form.RqFormSmart;
import org.takes.rs.RsJson;
import org.takes.rs.RsWithStatus;

public final class TkPaymentOrderGroupChooseBankAccount implements Take {

	/**
	 * Database
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkPaymentOrderGroupChooseBankAccount(final DataSource source) {
		this.source = source;
	}
	
	@Override
	public Response act(Request req) throws Exception {	
		try {
			final Log log = new RqLog(this.source, req);
			final RqFormSmart form = new RqFormSmart(new RqGreedy(req));
			final Long id = Long.parseLong(form.single("id"));
			final Long accountid = Long.parseLong(form.single("account_id"));
			final PaymentMeanType meantype = PaymentMeanType.valueOf(form.single("mean_type_id"));
			LocalDate duedate;
			try {
				duedate = LocalDateTime.parse(
					form.single("due_date"),
					DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
				).toLocalDate();
			} catch (Exception e) {
				duedate = LocalDate.MIN;
			}
			final User user = new RqUser(this.source, req);
			final Workspace workspace = new DbWorkspace(this.source, user);
			final PaymentOrderGroup group = workspace.ordersToPrepare().get(id);
			final BankAccounts accounts = new DbCompanyBankAccounts(this.source);
			final BankAccount account;
			if(accountid.equals(0L)) {
				account = BankAccount.EMPTY;
			} else {
				account = accounts.get(accountid);
			}
			group.useAccount(account);
			group.update(meantype, duedate);
			log.info("Choix du mode de règlement pour le groupe (ID=%s, Bénéficiaire=%s, Montant=%s)", group.id(), group.beneficiary().abbreviated(), new FrAmountInXof(group.totalAmount()));
			return new RsJson(
					new XePaymentOrderGroupJson(group)
				);
		} catch(IllegalArgumentException ex) {
			return new RsWithStatus(
				new RsJson(
					Json.createObjectBuilder()
						.add("message", ex.getLocalizedMessage())
						.build()
					),
				HttpURLConnection.HTTP_BAD_REQUEST
			);
		} catch(Exception ex) {
			return new RsWithStatus(
				new RsJson(
					Json.createObjectBuilder()
					.add("message", ex.getLocalizedMessage())
					.build()
				),
				HttpURLConnection.HTTP_INTERNAL_ERROR
			);
		}
	}

}

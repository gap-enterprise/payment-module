package io.surati.gap.payment.module.server;

import io.surati.gap.payment.module.actions.TkBankNotePrint;
import io.surati.gap.payment.module.actions.TkBankNoteSamplePrint;
import io.surati.gap.payment.module.actions.TkBatchBankNotePrint;
import io.surati.gap.payment.module.actions.TkBatchLetterPrint;
import io.surati.gap.payment.module.actions.TkBatchPaymentResumePrint;
import io.surati.gap.payment.module.actions.TkLetterPrint;
import io.surati.gap.payment.module.actions.TkPaymentResumePrint;
import io.surati.gap.payment.module.rest.TkBankNoteBookSearch;
import io.surati.gap.payment.module.rest.TkBankNoteSearch;
import io.surati.gap.payment.module.rest.TkBankSearch;
import io.surati.gap.payment.module.rest.TkPaymentExportSearch;
import io.surati.gap.payment.module.rest.TkPaymentHistoryExportExcel;
import io.surati.gap.payment.module.rest.TkPaymentOrderGroupChangeBeneficiary;
import io.surati.gap.payment.module.rest.TkPaymentOrderGroupChooseBankAccount;
import io.surati.gap.payment.module.rest.TkPaymentOrderGroupToExecuteList;
import io.surati.gap.payment.module.rest.TkPaymentOrderGroupToPrepareList;
import io.surati.gap.payment.module.rest.TkPaymentOrderGroupValidate;
import io.surati.gap.payment.module.rest.TkPaymentOrderMerge;
import io.surati.gap.payment.module.rest.TkPaymentOrderMergeAcross;
import io.surati.gap.payment.module.rest.TkPaymentOrderSplit;
import io.surati.gap.payment.module.rest.TkReferenceDocumentDeselect;
import io.surati.gap.payment.module.rest.TkReferenceDocumentSelect;
import io.surati.gap.payment.module.rest.TkReferenceDocumentToTreatSearch;
import io.surati.gap.web.base.TkSecure;
import javax.sql.DataSource;
import org.takes.facets.fork.FkChain;
import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.FkWrap;

/**
 * Front for API.
 *
 * @since 0.1
 */
public final class FkApi extends FkWrap {

	public FkApi(final DataSource source) {
		super(
			new FkChain(
				new FkRegex(
					"/payment/search",
					new TkSecure(
						new TkBankNoteSearch(source),
						source
					)
				),
				new FkRegex(
					"/payment/export/search",
					new TkSecure(
						new TkPaymentExportSearch(source),
						source
					)
				),
				new FkRegex(
					"/bank/search",
					new TkSecure(
						new TkBankSearch(source),
						source
					)
				),
				new FkRegex(
					"/bank-note-book/search",
					new TkSecure(
						new TkBankNoteBookSearch(source),
						source
					)
				),
				new FkRegex(
					"/bank/payment-mean/print/test",
					new TkSecure(
						new TkBankNoteSamplePrint(source),
						source
					)
				),
				new FkRegex(
					"/bank-note/print",
					new TkSecure(
						new TkBankNotePrint(source),
						source
					)
				),
				new FkRegex(
					"/api/batch/bank-note/print",
					new TkSecure(
						new TkBatchBankNotePrint(source),
						source
					)
				),
				new FkRegex(
					"/payment-resume/print",
					new TkSecure(
						new TkPaymentResumePrint(source),
						source
					)
				),
				new FkRegex(
					"/api/batch/payment-resume/print",
					new TkSecure(
						new TkBatchPaymentResumePrint(source),
						source
					)
				),
				new FkRegex(
					"/letter/print",
					new TkSecure(
						new TkLetterPrint(source),
						source
					)
				),
				new FkRegex(
					"/api/batch/letter/print",
					new TkSecure(
						new TkBatchLetterPrint(source),
						source
					)
				),
				new FkRegex(
					"/reference-document/to-pay/search",
					new TkSecure(
						new TkReferenceDocumentToTreatSearch(source),
						source
					)
				),
				new FkRegex(
					"/reference-document/select",
					new TkSecure(
						new TkReferenceDocumentSelect(source),
						source
					)
				),
				new FkRegex(
					"/reference-document/deselect",
					new TkSecure(
						new TkReferenceDocumentDeselect(source),
						source
					)
				),
				new FkRegex(
					"/api/payment-order-group/to-prepare",
					new TkSecure(
						new TkPaymentOrderGroupToPrepareList(source),
						source
					)
				),
				new FkRegex(
					"/api/payment-order-group/bank-account/choose",
					new TkSecure(
						new TkPaymentOrderGroupChooseBankAccount(source),
						source
					)
				),
				new FkRegex(
					"/api/payment-order/to-prepare/merge",
					new TkSecure(
						new TkPaymentOrderMerge(source),
						source
					)
				),
				new FkRegex(
					"/api/payment-order-group/to-prepare/validate",
					new TkSecure(
						new TkPaymentOrderGroupValidate(source),
						source
					)
				),
				new FkRegex(
					"/api/payment-order-group/to-execute",
					new TkSecure(
						new TkPaymentOrderGroupToExecuteList(source),
						source
					)
				),
				new FkRegex(
					"/api/payment-order/split",
					new TkSecure(
						new TkPaymentOrderSplit(source),
						source
					)
				),
				new FkRegex(
					"/api/payment-order/merge-across",
					new TkSecure(
						new TkPaymentOrderMergeAcross(source),
						source
					)
				),
				new FkRegex(
					"/api/payment-order-group/change-beneficiary",
					new TkSecure(
						new TkPaymentOrderGroupChangeBeneficiary(source),
						source
					)
				),
				new FkRegex(
					"/api/payment/history/export-excel",
					new TkSecure(
						new TkPaymentHistoryExportExcel(source),
						source
					)
				)
			)
		);
	}
}

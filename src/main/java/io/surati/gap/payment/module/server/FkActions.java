package io.surati.gap.payment.module.server;

import io.surati.gap.payment.module.actions.TkBankAccountAccountingSettingSave;
import io.surati.gap.payment.module.actions.TkBankAccountDelete;
import io.surati.gap.payment.module.actions.TkBankAccountSave;
import io.surati.gap.payment.module.actions.TkBankDelete;
import io.surati.gap.payment.module.actions.TkBankNoteBookBlock;
import io.surati.gap.payment.module.actions.TkBankNoteBookDelete;
import io.surati.gap.payment.module.actions.TkBankNoteBookSave;
import io.surati.gap.payment.module.actions.TkBankNoteBookUse;
import io.surati.gap.payment.module.actions.TkBankSave;
import io.surati.gap.payment.module.actions.TkBatchFinalize;
import io.surati.gap.payment.module.actions.TkPaymentBatchNewBookChoose;
import io.surati.gap.payment.module.actions.TkPaymentBatchSave;
import io.surati.gap.payment.module.actions.TkPaymentCancelSave;
import io.surati.gap.payment.module.actions.TkPaymentExport;
import io.surati.gap.payment.module.actions.TkPaymentFinalize;
import io.surati.gap.payment.module.actions.TkPaymentMeanBankNoteImageSave;
import io.surati.gap.payment.module.actions.TkPaymentMeanGraphicalSetupSave;
import io.surati.gap.payment.module.actions.TkPaymentMeanManualSetupSave;
import io.surati.gap.payment.module.actions.TkPaymentOrderDelete;
import io.surati.gap.payment.module.actions.TkPaymentOrderGroupSendBackInPreparation;
import io.surati.gap.payment.module.actions.TkPaymentOrderSave;
import io.surati.gap.payment.module.actions.TkPaymentSave;
import io.surati.gap.payment.module.actions.TkReferenceDocumentCancelSelection;
import io.surati.gap.payment.module.actions.TkReferenceDocumentImportSave;
import io.surati.gap.payment.module.actions.TkReferenceDocumentSelectedPrepare;
import io.surati.gap.payment.module.actions.TkReferenceDocumentWsSelect;
import io.surati.gap.payment.module.actions.TkThirdPartyPaymentMeanTypeAllowedAdd;
import io.surati.gap.payment.module.actions.TkThirdPartyPaymentMeanTypeAllowedDelete;
import io.surati.gap.payment.module.pages.TkPaymentBatchNew;
import io.surati.gap.web.base.TkSecure;
import javax.sql.DataSource;
import org.takes.facets.fork.FkChain;
import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.FkWrap;

/**
 * Front for actions.
 *
 * @since 0.1
 */
public final class FkActions extends FkWrap {

	public FkActions(final DataSource source) {
		super(
			new FkChain(
				new FkRegex(
					"/payment/save",
					new TkSecure(
						new TkPaymentSave(source),
						source
					)
				),
				new FkRegex(
					"/payment/finalize",
					new TkSecure(
						new TkPaymentFinalize(source),
						source
					)
				),
				new FkRegex(
					"/batch/finalize",
					new TkSecure(
						new TkBatchFinalize(source),
						source
					)
				),
				new FkRegex(
					"/payment/cancel/save",
					new TkSecure(
						new TkPaymentCancelSave(source),
						source
					)
				),
				new FkRegex(
					"/bank/save",
					new TkSecure(
						new TkBankSave(source),
						source
					)
				),
				new FkRegex(
					"/bank/delete",
					new TkSecure(
						new TkBankDelete(source),
						source
					)
				),
				new FkRegex(
					"/bank-note-book/save",
					new TkSecure(
						new TkBankNoteBookSave(source),
						source
					)
				),
				new FkRegex(
					"/bank-note-book/delete",
					new TkSecure(
						new TkBankNoteBookDelete(source),
						source
					)
				),
				new FkRegex(
					"/bank-note-book/use",
					new TkSecure(
						new TkBankNoteBookUse(source),
						source
					)
				),
				new FkRegex(
					"/bank-note-book/block",
					new TkSecure(
						new TkBankNoteBookBlock(source),
						source
					)
				),
				new FkRegex(
					"/payment-order/save",
					new TkSecure(
						new TkPaymentOrderSave(source),
						source
					)
				),
				new FkRegex(
					"/payment-order/delete",
					new TkSecure(
						new TkPaymentOrderDelete(source),
						source
					)
				),
				new FkRegex(
					"/bank/payment-mean/manual-setup/save",
					new TkSecure(
						new TkPaymentMeanManualSetupSave(source),
						source
					)
				),
				new FkRegex(
					"/bank/payment-mean/graphical-setup/save",
					new TkSecure(
						new TkPaymentMeanGraphicalSetupSave(source),
						source
					)
				),
				new FkRegex(
					"/bank-account/save",
					new TkSecure(
						new TkBankAccountSave(source),
						source
					)
				),
				new FkRegex(
					"/bank-account/delete",
					new TkSecure(
						new TkBankAccountDelete(source),
						source
					)
				),
				new FkRegex(
					"/third-party/payment-mean-allowed/add",
					new TkSecure(
						new TkThirdPartyPaymentMeanTypeAllowedAdd(source),
						source
					)
				),
				new FkRegex(
					"/third-party/payment-mean-allowed/delete",
					new TkSecure(
						new TkThirdPartyPaymentMeanTypeAllowedDelete(source),
						source
					)
				),
				new FkRegex(
					"/bank-account/accounting-setting/save",
					new TkSecure(
						new TkBankAccountAccountingSettingSave(source),
						source
					)
				),
				new FkRegex(
					"/bank/payment-mean/bank-note-image/save",
					new TkSecure(
						new TkPaymentMeanBankNoteImageSave(source),
						source
					)
				),
				new FkRegex(
					"/reference-document/import/save",
					new TkSecure(
						new TkReferenceDocumentImportSave(source),
						source
					)
				),
				new FkRegex(
					"/reference-document/selected/cancel",
					new TkSecure(
						new TkReferenceDocumentCancelSelection(source),
						source
					)
				),
				new FkRegex(
					"/reference-document/ws-select",
					new TkSecure(
						new TkReferenceDocumentWsSelect(source),
						source
					)
				),
				new FkRegex(
					"/reference-document/selected/prepare",
					new TkSecure(
						new TkReferenceDocumentSelectedPrepare(source),
						source
					)
				),
				new FkRegex(
					"/payment/export",
					new TkSecure(
						new TkPaymentExport(source),
						source
					)
				),
				new FkRegex(
					"/payment/batch/new",
					new TkSecure(
						new TkPaymentBatchNew(source),
						source
					)
				),
				new FkRegex(
					"/payment/batch/new/book/choose",
					new TkSecure(
						new TkPaymentBatchNewBookChoose(source),
						source
					)
				),
				new FkRegex(
					"/payment/batch/save",
					new TkSecure(
						new TkPaymentBatchSave(source),
						source
					)
				),
				new FkRegex(
					"/payment-order-group/send-back-in-preparation",
					new TkSecure(
						new TkPaymentOrderGroupSendBackInPreparation(source),
						source
					)
				)
			)
		);
	}
}

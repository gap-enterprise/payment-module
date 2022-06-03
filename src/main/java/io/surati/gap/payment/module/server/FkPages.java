package io.surati.gap.payment.module.server;

import io.surati.gap.payment.module.pages.TkDashboardBankAccount;
import io.surati.gap.payment.module.pages.TkDashboardBankNoteBook;
import io.surati.gap.payment.module.pages.TkDashboardPayment;
import io.surati.gap.payment.module.pages.TkPaymentExportList;
import io.surati.gap.payment.module.actions.TkPaymentNewBookChoose;
import io.surati.gap.payment.module.pages.TkBankAccountAccountingSettingEdit;
import io.surati.gap.payment.module.pages.TkBankAccountAccountingSettingView;
import io.surati.gap.payment.module.pages.TkBankAccountEdit;
import io.surati.gap.payment.module.pages.TkBankAccountList;
import io.surati.gap.payment.module.pages.TkBankAccountView;
import io.surati.gap.payment.module.pages.TkBankEdit;
import io.surati.gap.payment.module.pages.TkBankList;
import io.surati.gap.payment.module.pages.TkBankNoteBookEdit;
import io.surati.gap.payment.module.pages.TkBankNoteBookList;
import io.surati.gap.payment.module.pages.TkBankNoteBookView;
import io.surati.gap.payment.module.pages.TkBankView;
import io.surati.gap.payment.module.pages.TkPaymentBatchNewBookChooseEdit;
import io.surati.gap.payment.module.pages.TkPaymentBatchNewEdit;
import io.surati.gap.payment.module.pages.TkPaymentBatchView;
import io.surati.gap.payment.module.pages.TkPaymentCancelEdit;
import io.surati.gap.payment.module.pages.TkPaymentHome;
import io.surati.gap.payment.module.pages.TkPaymentMeanBankNoteImageEdit;
import io.surati.gap.payment.module.pages.TkPaymentMeanBankNoteImageView;
import io.surati.gap.payment.module.pages.TkPaymentMeanGraphicalSetupEdit;
import io.surati.gap.payment.module.pages.TkPaymentMeanManualSetupEdit;
import io.surati.gap.payment.module.pages.TkPaymentMeanView;
import io.surati.gap.payment.module.pages.TkPaymentNew;
import io.surati.gap.payment.module.pages.TkPaymentNewBookChooseEdit;
import io.surati.gap.payment.module.pages.TkPaymentNewEdit;
import io.surati.gap.payment.module.pages.TkPaymentOrderToPrepareList;
import io.surati.gap.payment.module.pages.TkReferenceDocumentImportEdit;
import io.surati.gap.payment.module.pages.TkReferenceDocumentList;
import io.surati.gap.payment.module.pages.TkReferenceDocumentSelected;
import io.surati.gap.payment.module.pages.TkThirdPartyPaymentMeanTypeAllowedNew;
import io.surati.gap.web.base.TkSecure;
import javax.sql.DataSource;
import org.takes.facets.fork.FkChain;
import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.FkWrap;

/**
 * Front for pages.
 *
 * @since 0.1
 */
public final class FkPages extends FkWrap {

	public FkPages(final DataSource source) {
		super(
			new FkChain(
				new FkRegex(
					"/dashboard/payment",
					new TkSecure(
						new TkDashboardPayment(source),
						source
					)
				),
				new FkRegex(
					"/dashboard/bank-account",
					new TkSecure(
						new TkDashboardBankAccount(source),
						source
					)
				),
				new FkRegex(
					"/dashboard/bank-note-book",
					new TkSecure(
						new TkDashboardBankNoteBook(source),
						source
					)
				),
				new FkRegex(
					"/payment/home",
					new TkSecure(
						new TkPaymentHome(source),
						source
					)
				),
				new FkRegex(
					"/payment/new",
					new TkSecure(
						new TkPaymentNew(source),
						source
					)
				),
				new FkRegex(
					"/payment/new/edit",
					new TkSecure(
						new TkPaymentNewEdit(source),
						source
					)
				),
				new FkRegex(
					"/payment/new/book/edit",
					new TkSecure(
						new TkPaymentNewBookChooseEdit(source),
						source
					)
				),
				new FkRegex(
					"/payment/new/book/choose",
					new TkSecure(
						new TkPaymentNewBookChoose(source),
						source
					)
				),
				new FkRegex(
					"/payment/cancel/edit",
					new TkSecure(
						new TkPaymentCancelEdit(source),
						source
					)
				),
				new FkRegex(
					"/payment/export/list",
					new TkSecure(
						new TkPaymentExportList(source),
						source
					)
				),
				new FkRegex(
					"/bank",
					new TkSecure(
						new TkBankList(source),
						source
					)
				),
				new FkRegex(
					"/bank/view",
					new TkSecure(
						new TkBankView(source),
						source
					)
				),
				new FkRegex(
					"/bank/edit",
					new TkSecure(
						new TkBankEdit(source),
						source
					)
				),
				new FkRegex(
					"/bank-note-book",
					new TkSecure(
						new TkBankNoteBookList(source),
						source
					)
				),
				new FkRegex(
					"/bank-note-book/view",
					new TkSecure(
						new TkBankNoteBookView(source),
						source
					)
				),
				new FkRegex(
					"/bank-note-book/edit",
					new TkSecure(
						new TkBankNoteBookEdit(source),
						source
					)
				),
				new FkRegex(
					"/payment-order/to-prepare",
					new TkSecure(
						new TkPaymentOrderToPrepareList(source),
						source
					)
				),
				new FkRegex(
					"/bank/payment-mean/view",
					new TkSecure(
						new TkPaymentMeanView(source),
						source
					)
				),
				new FkRegex(
					"/bank/payment-mean/manual-setup/edit",
					new TkSecure(
						new TkPaymentMeanManualSetupEdit(source),
						source
					)
				),
				new FkRegex(
					"/bank/payment-mean/graphical-setup/edit",
					new TkSecure(
						new TkPaymentMeanGraphicalSetupEdit(source),
						source
					)
				),
				new FkRegex(
					"/bank-account",
					new TkSecure(
						new TkBankAccountList(source),
						source
					)
				),
				new FkRegex(
					"/bank-account/view",
					new TkSecure(
						new TkBankAccountView(source),
						source
					)
				),
				new FkRegex(
					"/bank-account/edit",
					new TkSecure(
						new TkBankAccountEdit(source),
						source
					)
				),
				new FkRegex(
					"/third-party/payment-mean-allowed/new",
					new TkSecure(
						new TkThirdPartyPaymentMeanTypeAllowedNew(source),
						source
					)
				),
				new FkRegex(
					"/bank-account/accounting-setting/edit",
					new TkSecure(
						new TkBankAccountAccountingSettingEdit(source),
						source
					)
				),
				new FkRegex(
					"/bank-account/accounting-setting/view",
					new TkSecure(
						new TkBankAccountAccountingSettingView(source),
						source
					)
				),
				new FkRegex(
					"/bank/payment-mean/bank-note-image/edit",
					new TkSecure(
						new TkPaymentMeanBankNoteImageEdit(source),
						source
					)
				),
				new FkRegex(
					"/bank/payment-mean/bank-note-image/view",
					new TkSecure(
						new TkPaymentMeanBankNoteImageView(source),
						source
					)
				),
				new FkRegex(
					"/reference-document/import/edit",
					new TkSecure(
						new TkReferenceDocumentImportEdit(source),
						source
					)
				),
				new FkRegex(
					"/reference-document/list",
					new TkSecure(
						new TkReferenceDocumentList(source),
						source
					)
				),
				new FkRegex(
					"/reference-document/selected",
					new TkSecure(
						new TkReferenceDocumentSelected(source),
						source
					)
				),
				new FkRegex(
					"/payment/batch/new/book/edit",
					new TkSecure(
						new TkPaymentBatchNewBookChooseEdit(source),
						source
					)
				),
				new FkRegex(
					"/payment/batch/new/edit",
					new TkSecure(
						new TkPaymentBatchNewEdit(source),
						source
					)
				),
				new FkRegex(
					"/payment/batch/view",
					new TkSecure(
						new TkPaymentBatchView(source),
						source
					)
				)
			)
		);
	}
}

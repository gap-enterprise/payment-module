package io.surati.gap.payment.module.actions;

import io.surati.gap.admin.base.api.Log;
import io.surati.gap.admin.base.prop.PropCompany;
import io.surati.gap.commons.utils.amount.FrWYSIWYGAmount;
import io.surati.gap.commons.utils.convert.FrShortDateFormat;
import io.surati.gap.payment.base.api.Bank;
import io.surati.gap.payment.base.api.BankAccountAccountingSetting;
import io.surati.gap.payment.base.api.BankNote;
import io.surati.gap.payment.base.api.BankNoteBook;
import io.surati.gap.payment.base.api.Payment;
import io.surati.gap.payment.base.api.PaymentExport;
import io.surati.gap.payment.base.api.PaymentMeanType;
import io.surati.gap.payment.base.api.PaymentOrder;
import io.surati.gap.payment.base.api.PaymentStatus;
import io.surati.gap.payment.base.api.ReferenceDocument;
import io.surati.gap.payment.base.db.DbBankNote;
import io.surati.gap.payment.base.db.DbPaymentExport;
import io.surati.gap.web.base.log.RqLog;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.cactoos.list.ListOf;
import org.cactoos.scalar.LengthOf;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.flash.RsFlash;
import org.takes.facets.forward.RsForward;

/**
 * @see <a href="https://www.unitconverters.net/typography/centimeter-to-pixel-x.htm">...</a>
 * for pixel convertion
 * @author baudoliver7
 */
@SuppressWarnings("serial")
public final class TkPaymentExport implements Take {

	private static String[][] COMPTES_EFFETS = {
		{ "40201010", "40201020", "40201031"}, // Janvier
		{ "40202010", "40202020", "40202029"}, // Février
		{ "40203010", "40203020", "40202031"}, // Mars
		{ "40204010", "40204020", "40204030"}, // Avril
		{ "40205010", "40205020", "40205031"}, // Mai
		{ "40206010", "40206020", "40206030"}, // Juin
		{ "40207010", "40207020", "40207031"}, // Juillet
		{ "40208010", "40208020", "40208031"}, // Aout
		{ "40209010", "40209020", "40209030"}, // Septembre
		{ "40210010", "40210020", "40210031"}, // Octobre
		{ "40211010", "40211020", "40211030"}, // Novembre
		{ "40212010", "40212020", "40212031"}  // Décembre
	};
	
	private static Map<String, String> COMPTES_BANCAIRES = new HashMap<String, String>() {
	    {
	        put("SGCI", "52120000");
	        put("BDU", "52150000");
	        put("BOA", "52110000");
	        put("BNI", "52130000");
	        put("SIB", "52140000");
	        put("NSIA", "52170000");
	        put("BRIDGE BANK", "52160000");
	        put("CORIS", "52175000");
			put("ECOBANK", "52180000");
			put("BP", "52190000");
			put("ORABANK", "52185000");
			put("BACI", "52195000");
	    }
	};

	private final DataSource source;

	static {
		File file = new File("exports");
		if(!file.exists()) {
			file.mkdir();
		}
	}
	public TkPaymentExport(final DataSource source) {
		this.source = source;
	}

	@Override
	public Response act(Request req) throws Exception {
		final Log log = new RqLog(this.source, req);
		final String exportlocation = new PropCompany().parameter("export_location");
		if(StringUtils.isBlank(exportlocation)) {
			throw new IllegalArgumentException("Le répertoire des exportations n'est pas configuré! Veuillez contacter SVP votre administrateur.");
		}
		if(!new File(exportlocation).exists()) {
			throw new IllegalArgumentException(String.format("Le répertoire des exportations (%s) n'existe pas. Veuillez contacter SVP votre administrateur afin de le créer.", exportlocation));
		}
		final PaymentExport export = new DbPaymentExport(this.source);
		final List<String[]> lines = new LinkedList<>();
		Integer numpiece = 0;
		for (Payment payment : export.iterate()) {
			// For the moment, we only manage bank note.
			final BankNote note = new DbBankNote(this.source, payment.id());
			numpiece++;
			Integer numecriture = 0;
			final String numnote;
			if(note.meanType() == PaymentMeanType.CHQ) {
				numnote = note.issuerReference();
			} else {
				numnote = String.format("EAP°%s", note.issuerReference());
			}
			final String tiers = "SSI";
			String libecriture = StringUtils.EMPTY;
			for (PaymentOrder po : note.orders()) {
				numecriture++;
				String[] line = new String[33];
				line[0] = tiers;
				line[1] = numpiece.toString();
				line[2] = numecriture.toString();
				line[3] = String.format("%s", new LengthOf(note.orders()).intValue() + 1);
				line[4] = note.date().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
				final BankNoteBook book = note.book();
				final BankAccountAccountingSetting setting = book.account().accountingSettings().get(book.meanType());
				line[5] = setting.journalCode();
				line[6] = po.beneficiary().code();
				line[7] = "";
				line[8] = "";
				if(StringUtils.isBlank(libecriture)) {
					String label;
					final Bank bank = book.account().bank();
					if(book.meanType() == PaymentMeanType.CHQ) {
						label = String.format("%s°%s %s FACT°%s", bank.abbreviated(), note.issuerReference(), note.beneficiary().abbreviated(), format(new ListOf<>(note.orders())));
					} else {
						label = String.format("%s EAP°%s au %s sur %s", note.beneficiary().abbreviated(), note.issuerReference(), new FrShortDateFormat().convert(note.dueDate()), bank.abbreviated());
					}
					libecriture = label;
				}
				line[9] = libecriture;
				line[10] = "1";
				if(note.status() == PaymentStatus.CANCELLED) {
					line[11] = new FrWYSIWYGAmount(-po.amountToPay()).toString();
				} else {
					line[11] = new FrWYSIWYGAmount(po.amountToPay()).toString();
				}
				line[12] = "";
				line[13] = note.internalReference();
				line[14] = po.document().otherReference();
				for (int i = 15; i < 33; i++) {
					line[i] = "";
				}
				line[21] = numnote;
				lines.add(line);
			}
			numecriture++;
			String[] line = new String[33];
			line[0] = tiers;
			line[1] = numpiece.toString();
			line[2] = numecriture.toString();
			line[3] = String.format("%s", new LengthOf(note.orders()).intValue() + 1);
			line[4] = note.date().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			final BankNoteBook book = note.book();
			final BankAccountAccountingSetting setting = book.account().accountingSettings().get(book.meanType());
			line[5] = setting.journalCode();
			line[6] = accountNumberOf(note);
			line[7] = "";
			line[8] = "";
			line[9] = libecriture;
			line[10] = "2";
			if(note.status() == PaymentStatus.CANCELLED) {
				line[11] = new FrWYSIWYGAmount(-note.amount()).toString();
			} else {
				line[11] = new FrWYSIWYGAmount(note.amount()).toString();
			}
			line[12] = "";
			line[13] = note.internalReference();
			line[14] = "";
			for (int i = 15; i < 33; i++) {
				line[i] = "";
			}
			line[21] = numnote;
			lines.add(line);
		}
		final String filename = String.format("EXPORT_PAIEMENT_%s.txt", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm")));
		final Path customerpath = Paths.get(exportlocation,  filename);
		File csvOutputFile = customerpath.toFile();
	    try (PrintWriter pw = new PrintWriter(csvOutputFile, "ISO-8859-1")) {
	        lines.stream()
	          .map(this::convertToCSV)
	          .forEach(pw::println);
	    }
	    final Path localpath = Paths.get(System.getProperty("user.dir"), "exports",  filename);
	    Files.copy(customerpath, localpath, StandardCopyOption.REPLACE_EXISTING);
	    export.finish();
	    log.info("Export de paiements");
	    return new RsForward(
			new RsFlash(
				"L'exportation des paiements a été effectuée avec succès !",
				Level.INFO
			),
			String.format("/payment/export/list")
		);
	}
	
	private static String format(final List<PaymentOrder> orders) {
		if(orders.isEmpty() || orders.stream().noneMatch(o -> o.document() != ReferenceDocument.EMPTY)) {
			return "";
		}
		int minlength = 3;
		int length = orders.stream().filter(
			o -> o.document() != ReferenceDocument.EMPTY
		).findFirst().get().document().reference().length();
		if(length < minlength) {
			minlength = length;
		}
		String refs;
		do {
			refs = "";
			for (PaymentOrder order : orders) {
				if(order.document() == ReferenceDocument.EMPTY) {
					continue;
				}
				if(!refs.isEmpty()) {
					refs += "-";
				}
				final String reference = order.document().reference();
				refs += reference.substring(Math.max(reference.length() - length, 0));
			}
			if(refs.length() > 75) {
				length -= 1;
				if(length < minlength) {
					refs = refs.substring(0, 72) + "...";
				}
			}
		} while(refs.length() > 75);
		return refs;
	}
	
	private static String accountNumberOf(BankNote note) {
		final String number;
		if(note.book().meanType() == PaymentMeanType.CHQ) {
			number = COMPTES_BANCAIRES.get(note.book().account().bank().abbreviated());
		} else {
			final LocalDate date = note.dueDate();
			final int i = date.getMonthValue() - 1;
			final int j;
			if(date.getDayOfMonth() <= 10) {
				j = 0;
			} else if(date.getDayOfMonth() >= 11 && date.getDayOfMonth() <=20) {
				j = 1;
			} else {
				j = 2;
			}
			number = COMPTES_EFFETS[i][j];
		}
		return number;
	}
	
	public String convertToCSV(String[] data) {
	    return Stream.of(data)
	      .map(this::escapeSpecialCharacters)
	      .collect(Collectors.joining(";"));
	}
	
	public String escapeSpecialCharacters(String data) {
		if(StringUtils.isBlank(data)) {
			return "";
		}
	    String escapedData = data.replaceAll("\\R", " ");
	    if (data.contains(";") || data.contains("\"") || data.contains("'")) {
	        data = data.replace("\"", "\"\"");
	        escapedData = "\"" + data + "\"";
	    }
	    return escapedData.replace("\"", "").replace("&", "").replace("#", "");
	}
}

package io.surati.gap.payment.module.actions;

import io.surati.gap.admin.base.api.Log;
import io.surati.gap.commons.utils.convert.FrShortDateFormat;
import io.surati.gap.payment.base.api.BankNote;
import io.surati.gap.payment.base.api.Payment;
import io.surati.gap.payment.base.api.PaymentExport;
import io.surati.gap.payment.base.api.PaymentMeanType;
import io.surati.gap.payment.base.api.PaymentOrder;
import io.surati.gap.payment.base.api.ThirdParty;
import io.surati.gap.payment.base.db.DbPaymentExport;
import io.surati.gap.web.base.log.RqLog;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.sql.DataSource;
import org.cactoos.list.ListOf;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EXCELRenderOption;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.core.internal.registry.RegistryProviderFactory;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rs.RsWithBody;

/**
 * @see <a href="https://www.unitconverters.net/typography/centimeter-to-pixel-x.htm">...</a>
 * for pixel convertion
 * @author baudoliver7
 */
public final class TkPaymentExportDefault implements Take {

	private final DataSource source;

	public TkPaymentExportDefault(final DataSource source) {
		this.source = source;
	}

	@Override
	public Response act(Request req) throws Exception {
		final Log log = new RqLog(this.source, req);
		final PaymentExport export = new DbPaymentExport(this.source);
		final Map<String, Object> context = new HashMap<>();
		final Iterable<Payment> payments = export.iterate();
	    context.put("lines", new ListOf<>(payments));
	    final List<String> labels = new LinkedList<>();
	    final List<String> amounts = new LinkedList<>();
	    final List<String> orderamounts = new LinkedList<>();
	    for (Payment payment : payments) {
	    	final ThirdParty bank = payment.issuer();
	    	String label;
			if(payment.meanType() == PaymentMeanType.CHQ) {
				label = String.format("%s°%s %s FACT°%s", bank.abbreviated(), payment.issuerReference(), payment.beneficiary().abbreviated(), format(new ListOf<>(payment.orders())));
			} else {
				label = String.format("%s EAP°%s au %s sur %s", payment.beneficiary().abbreviated(), payment.issuerReference(), new FrShortDateFormat().convert(((BankNote)payment).dueDate()), bank.abbreviated());
			}
			labels.add(label);
			amounts.add(payment.amount().toString());
			orderamounts.add(formatAmounts(payment.orders()));
		}
	    context.put("labels", labels);
	    context.put("amounts", amounts);
	    context.put("orderamounts", orderamounts);
	    context.put(Locale.class.getSimpleName(), Locale.FRENCH);
	    final ByteArrayOutputStream output = new ByteArrayOutputStream();
	    IReportEngine engine = null;
		try {
			final EngineConfig config = new EngineConfig();
			Platform.startup(config);
			final IReportEngineFactory fact = (IReportEngineFactory) Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
			engine = fact.createReportEngine(config);
			final InputStream reportResource = getClass().getClassLoader().getResourceAsStream("report/payment_export.rptdesign");
			final IReportRunnable runnable = engine.openReportDesign(reportResource);
			final IRunAndRenderTask task = engine.createRunAndRenderTask(runnable);
			final RenderOption pdfOptions = new EXCELRenderOption();
			pdfOptions.setOutputFormat("XLSX");
			pdfOptions.setOutputStream(output);
			task.setRenderOption(pdfOptions);
			task.setAppContext(context);		
			task.run();
			task.close();
			log.info("Export de paiements");
			export.finish();
			return new RsWithBody(output.toByteArray());
		} finally {
			try
			{
				engine.destroy();
				Platform.shutdown();
				//Bugzilla 351052
				RegistryProviderFactory.releaseDefault();
			}catch ( Exception e1 ){
			    e1.printStackTrace();
			}
		}	
	}
	
	private static String format(final List<PaymentOrder> orders) {
		if(orders.isEmpty()) {
			return "";
		}
		int minlength = 3;
		int length = orders.get(0).document().reference().length();
		if(length < minlength) {
			minlength = length;
		}
		String refs;
		do {
			refs = "";
			for (PaymentOrder order : orders) {
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
	
	private static String formatAmounts(final Iterable<PaymentOrder> orders) {
		String format = "";
		for (PaymentOrder order : orders) {
			if(!format.isEmpty()) {
				format += "|";
			}
			format += order.amountToPay().toString();
		}
		return format;
	}
}

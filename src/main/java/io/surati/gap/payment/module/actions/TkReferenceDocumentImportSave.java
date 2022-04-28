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

import io.surati.gap.admin.base.api.Log;
import io.surati.gap.admin.base.api.ReferenceDocumentType;
import io.surati.gap.admin.base.api.User;
import io.surati.gap.commons.utils.convert.RqFilename;
import io.surati.gap.payment.base.api.ReferenceDocument;
import io.surati.gap.payment.base.api.ThirdParties;
import io.surati.gap.payment.base.api.ThirdParty;
import io.surati.gap.payment.base.api.ThirdPartyReferenceDocuments;
import io.surati.gap.payment.base.db.DbThirdParties;
import io.surati.gap.payment.base.db.DbThirdPartyReferenceDocuments;
import io.surati.gap.web.base.log.RqLog;
import io.surati.gap.web.base.rq.RootPageUri;
import io.surati.gap.web.base.rq.RqUser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.facets.flash.RsFlash;
import org.takes.facets.forward.RsForward;
import org.takes.rq.RqGreedy;
import org.takes.rq.multipart.RqMtSmart;

/**
 * Take that imports payment orders.
 *
 * <p>The class is immutable and thread-safe.</p>
 *
 * @since 0.1.0
 */


public final class TkReferenceDocumentImportSave implements Take {

	/**
	 * Database
	 */
	private final DataSource source;
	
	/**
	 * Ctor.
	 * @param source DataSource
	 */
	public TkReferenceDocumentImportSave(final DataSource source) {
		this.source = source;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public Response act(Request req) throws Exception {
		final Log log = new RqLog(this.source, req);
		final RqGreedy rqg = new RqGreedy(req);
		final RqMtSmart mt = new RqMtSmart(rqg);
		final Request rqfile = mt.single("file");			
		int lineNumber = 0;
		final User user = new RqUser(this.source, req);
		try(InputStream file = rqfile.body()) {
			final String filename = new RqFilename(rqfile).value().toLowerCase();
			if(filename.endsWith(".xls") || filename.endsWith(".xlsx")) {
				final Workbook workbook = WorkbookFactory.create(file);		
				if(workbook.getNumberOfSheets() == 0) {
					throw new IllegalArgumentException("Le fichier ne contient pas de feuille !");
				}
				final Sheet sheet = workbook.getSheetAt(0);
	            final Iterator<Row> rowIterator = sheet.iterator();
	            rowIterator.next();
	            while (rowIterator.hasNext()) 
	            {     	
	            	final Row row = rowIterator.next();
	            	if(row.getCell(0) == null) {
	            		break;
	            	}
	            	final LocalDate date =
	        			Instant.ofEpochMilli(row.getCell(0).getDateCellValue().getTime())
	        		    	.atZone(ZoneOffset.UTC)
	        		    	.toLocalDate();
	            	final ReferenceDocumentType type = ReferenceDocumentType.valueOf(row.getCell(1).getStringCellValue());
	            	final String[] nums = row.getCell(2).getStringCellValue().split("°");
	            	final String num;
	            	if(nums.length == 1) {
	            		num = nums[0].trim();
	            	} else {
	            		num = nums[1].trim();
	            	}
	                final String othernum = row.getCell(3).getStringCellValue();
	                final String tpcode = row.getCell(4).getStringCellValue();
	                final String tpname = row.getCell(6).getStringCellValue();
	                final String tpabbreviated = row.getCell(5) == null ? tpname : row.getCell(5).getStringCellValue();
	                final double amount = row.getCell(7).getNumericCellValue();
	                final String object = row.getCell(8).getStringCellValue();
	                final String place = row.getCell(9).getStringCellValue();
	                final LocalDate depositdate = row.getCell(10).getDateCellValue() == null ? LocalDate.MIN :
		        			Instant.ofEpochMilli(row.getCell(10).getDateCellValue().getTime())
		        		    	.atZone(ZoneOffset.UTC)
		        		    	.toLocalDate();
	                final LocalDate entrydate = row.getCell(11).getDateCellValue() == null ? LocalDate.MIN :
	        			Instant.ofEpochMilli(row.getCell(11).getDateCellValue().getTime())
	        		    	.atZone(ZoneOffset.UTC)
	        		    	.toLocalDate();
	                final ThirdParties tps = new DbThirdParties(this.source);
	                final ThirdParty beneficiary;
	                if(tps.has(tpcode)) {
	                	beneficiary = tps.get(tpcode);
	                } else {
	                	beneficiary = tps.add(tpcode, tpname, tpabbreviated);
	                }
	                final ThirdPartyReferenceDocuments documents = new DbThirdPartyReferenceDocuments(this.source, beneficiary);
	                if(documents.has(num, type)) {
	                	continue;
	                }
	                if(documents.has(othernum)) {
	                	continue;
	                }
	                lineNumber++;
	                final ReferenceDocument refdoc = documents.add(date, type, num, amount, object, place, user);
	                refdoc.update(othernum);
	                refdoc.update(depositdate, entrydate);
	            }
			} else if(filename.endsWith(".csv")) {
				final Reader in = new InputStreamReader(file, "ISO-8859-1");
				final Iterable<CSVRecord> records = CSVFormat.EXCEL.withDelimiter(';').parse(in);
	            final Iterator<CSVRecord> iterator = records.iterator();
	            iterator.next();
				while (iterator.hasNext()) {
					final CSVRecord record = iterator.next();
	            	final LocalDate date = LocalDate.parse(record.get(0), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	            	final ReferenceDocumentType type = ReferenceDocumentType.valueOf(record.get(1));
	            	final String[] nums = record.get(2).split("°");
	            	final String num;
	            	if(nums.length == 1) {
	            		num = nums[0].trim();
	            	} else {
	            		num = nums[1].trim();
	            	}
	                if(StringUtils.isBlank(num)) {
	                	continue;
	                }
	                final String othernum = record.get(3);
	                final String tpcode = record.get(4);
	                final String tpname = record.get(6);
	                final String tpabbreviated = StringUtils.isBlank(record.get(5)) ? tpname : record.get(5);
	                final double amount = Double.parseDouble(record.get(7));
	                final String object = record.get(8);
	                final String place = record.get(9);
	                final LocalDate depositdate = StringUtils.isBlank(record.get(10)) ? LocalDate.MIN : LocalDate.parse(record.get(10), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	                final LocalDate entrydate = StringUtils.isBlank(record.get(11)) ? LocalDate.MIN : LocalDate.parse(record.get(11), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	                final ThirdParties tps = new DbThirdParties(this.source);
	                final ThirdParty beneficiary;
	                if(tps.has(tpcode)) {
	                	beneficiary = tps.get(tpcode);
	                } else {
	                	beneficiary = tps.add(tpcode, tpname, tpabbreviated);
	                }
	                final ThirdPartyReferenceDocuments documents = new DbThirdPartyReferenceDocuments(this.source, beneficiary);
	                if(documents.has(num, type)) {
	                	continue;
	                }
	                if(documents.has(othernum)) {
	                	continue;
	                }
	                lineNumber++;
	                final ReferenceDocument refdoc = documents.add(date, type, num, amount, object, place, user);
	                refdoc.update(othernum);
	                refdoc.update(depositdate, entrydate);
				}
			} else {
				throw new IllegalArgumentException("Le format du fichier importé n'est pas pris en charge !");
			}
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(String.format("Erreur à la ligne %s : %s", lineNumber, e.getLocalizedMessage()));
		}
		log.info("Importation des documents de référence (%s lignes traitées)", lineNumber);
		return new RsForward(
			new RsFlash(
				String.format("L'importation a été effectuée avec succès (%s lignes traitées) !", lineNumber),
				Level.INFO
			),
			new RootPageUri(req).toString()
		);
	}
}

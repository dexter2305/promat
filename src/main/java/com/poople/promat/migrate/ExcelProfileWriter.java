package com.poople.promat.migrate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.sl.usermodel.Notes;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.poople.promat.adapters.DTFormatter;
import com.poople.promat.models.Candidate;
import com.poople.promat.models.Dob;
import com.poople.promat.models.Education;
import com.poople.promat.models.Horoscope;
import com.poople.promat.models.Note;
import com.poople.promat.models.Occupation;

public class ExcelProfileWriter {
	private String profileSheetName;
	private String excelPrintTemplateFile;
	private String horoscopeImageDir;
	private String profileWriteDir;
	private CellReference nameCf;
	private CellReference htCf;
	private CellReference userIdCf;
	private CellReference phCf;
	private CellReference kulamCf;
	private CellReference starCf;
	private CellReference padhamCf;
	private CellReference date_dayCf;
	private CellReference timeCf;
	private CellReference birthplaceCf;
	private CellReference rasiCf;
	private CellReference lagnamCf;
	private CellReference rahukethuCf;
	private CellReference sevvaiCf;
	private CellReference dasaCf;
	private CellReference iruppuCf;
	private CellReference educationCf;
	private CellReference occupation_salaryCf;
	private CellReference workplaceCf;
	private CellReference noteCf;
	private Map<String, CellReference> cellRefMap = null;

	private static final Log logger = LogFactory.getLog(ExcelProfileWriter.class);
	private static final List<String> cellRefs = Arrays.asList("userid", "name", "ph", "kulam", "ht", "star", "padham",
			"date_day", "time", "birthplace", "rasi", "lagnam", "rahukethu", "sevvai", "dasa", "iruppu", "education",
			"occupation_salary", "workplace", "note");

	private ExcelManager xlMgr;

	public ExcelProfileWriter(String strConfigFileName) throws Exception {
		super();
		init(strConfigFileName);
	}

	private void init(String strConfigFileName) throws Exception {
		logger.info("ENTER - init() :" + strConfigFileName);

		Properties configProps = this.load(strConfigFileName);
		this.profileSheetName = configProps.getProperty("profileSheetName");
		this.excelPrintTemplateFile = configProps.getProperty("excelPrintTemplateFile");
		this.profileWriteDir = configProps.getProperty("profileWriteDir");
		this.horoscopeImageDir = configProps.getProperty("horoscopeImageDir");

		// Validate if all required cell references are present in input config
		List<String> notPresentList = cellRefs.stream().filter((propName) -> (!configProps.containsKey(propName)))
				.collect(Collectors.toList());
		if (notPresentList != null && notPresentList.size() > 0) {
			throw new Exception("One or more config values are not persent for following properties : "
					+ notPresentList.toString());
		}
		this.cellRefMap = cellRefs.stream().filter((propName) -> (configProps.containsKey(propName)))
				.collect(Collectors.toMap((propName) -> propName.toString(), (propName) -> {
					String value = configProps.getProperty(propName, null);
					if (value != null && value.trim().length() > 0) {
						CellReference cf = new CellReference(value.trim());
						return cf;
					} else {
						return null;
					}
				}));

		this.xlMgr = new ExcelManager();

		this.validate();

		logger.info("EXIT - init()");

	}

	private void validate() throws Exception {
		if (!xlMgr.fileExists(this.horoscopeImageDir)) {
			new Exception("Could not find horoscope image directory named '" + this.horoscopeImageDir + "'");
		}
		if (!xlMgr.fileExists(this.profileWriteDir)) {
			new Exception("Could not find profile write directory named '" + this.profileWriteDir + "'");
		}
		Workbook wb = xlMgr.openWorkBook(excelPrintTemplateFile);
		Sheet s = wb.getSheet(profileSheetName);
		if (s == null) {
			new Exception("Could not find sheet named '" + this.profileSheetName + "' in the template file '"
					+ this.excelPrintTemplateFile);
		}
		wb.close();
		// Validate if all the references valid
		List<String> invalidCellRefList = this.cellRefMap.entrySet().stream()
				.filter((entry) -> (entry.getValue() == null)).map((entry) -> entry.getKey())
				.collect(Collectors.toList());
		if (invalidCellRefList.size() > 0) {
			throw new Exception("One or more config values are invalid for following properties : "
					+ invalidCellRefList.toString());
		}
	}

	public void write(Collection<Candidate> candidates) {

		userIdCf = cellRefMap.get("userid");
		nameCf = cellRefMap.get("name");
		htCf = cellRefMap.get("ht");
		phCf = cellRefMap.get("ph");
		kulamCf = cellRefMap.get("kulam");
		starCf = cellRefMap.get("star");
		padhamCf = cellRefMap.get("padham");
		date_dayCf = cellRefMap.get("date_day");
		timeCf = cellRefMap.get("time");
		birthplaceCf = cellRefMap.get("birthplace");
		rasiCf = cellRefMap.get("rasi");
		lagnamCf = cellRefMap.get("lagnam");
		rahukethuCf = cellRefMap.get("rahukethu");
		sevvaiCf = cellRefMap.get("sevvai");
		dasaCf = cellRefMap.get("dasa");
		iruppuCf = cellRefMap.get("iruppu");
		educationCf = cellRefMap.get("education");
		occupation_salaryCf = cellRefMap.get("occupation_salary");
		workplaceCf = cellRefMap.get("workplace");
		noteCf = cellRefMap.get("note");

		StringBuilder errorMsg = new StringBuilder();
		boolean hasErrors = false;
		for (Candidate c : candidates) {
			long id = c.getId();
			String strFile = this.profileWriteDir + id + ".xls";
			try {
				File candidateFile = xlMgr.getFile(strFile);
				Workbook wb = null;
				Sheet s = null;
				if (candidateFile.exists()) {
					//FIXME not working 
					wb = xlMgr.openWorkBook(candidateFile);
					// rename if sheet with name already exists
					s = wb.getSheet(this.profileSheetName);
					if (s != null) {
						wb.setSheetName(wb.getSheetIndex(s), "this.profileSheetName" + "_backup"
								+ LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy")));
					}
					// now, create new sheet
					s = wb.createSheet(this.profileSheetName);

				} else {
					wb = xlMgr.openWorkBook(excelPrintTemplateFile);
					s = wb.getSheet(profileSheetName);

				}
				updateProfileData(wb, s, c);
				xlMgr.writeWorkBookToFile(wb, strFile);
				xlMgr.closeWorkBook(wb);
			} catch (Exception e) {
				e.printStackTrace();
				errorMsg.append("\nError while writing candidate with user id '" + id + "' :" + e.getMessage());
				hasErrors = true;
			}
		}
		if (hasErrors) {
			System.out.println(errorMsg.toString());
			// write to error file
		}
	}

	private void updateProfileData(Workbook wb, Sheet s, Candidate c) {

		xlMgr.retreiveAndSetCells(s, userIdCf.getRow(), userIdCf.getCol(), String.valueOf(c.getId()));
		xlMgr.retreiveAndSetCells(s, phCf.getRow(), phCf.getCol(), "PH - ");
		xlMgr.retreiveAndSetCells(s, nameCf.getRow(), nameCf.getCol(), c.getName());
		xlMgr.retreiveAndSetCells(s, htCf.getRow(), htCf.getCol(),
				formatAsString(c.getPhysique().getHeight(), 4) + "cm");
		xlMgr.retreiveAndSetCells(s, kulamCf.getRow(), kulamCf.getCol(), c.getKulam());
		// xlMgr.retreiveAndSetCells(s, date_dayCf.getRow(),
		// date_dayCf.getCol(),
		// (c.getDob().getBirthdate()!=null)?c.getDob().getBirthdate().format(DTFormatter.INSTANCE.getDateFormatter()):null);
		xlMgr.retreiveAndSetCells(s, date_dayCf.getRow(), date_dayCf.getCol(),
				datefmt.apply(c.getDob().getBirthdate()));

		// xlMgr.retreiveAndSetCells(s, timeCf.getRow(), timeCf.getCol(),
		// (c.getDob().getBirthtime() !=
		// null)?c.getDob().getBirthtime().format(DTFormatter.INSTANCE.getTimeFormatter()):null);
		xlMgr.retreiveAndSetCells(s, timeCf.getRow(), timeCf.getCol(), timefmt.apply(c.getDob().getBirthtime()));

		Horoscope h = c.getHoroscope();
		xlMgr.retreiveAndSetCells(s, starCf.getRow(), starCf.getCol(),
				strfmt.apply(h.getStar()) + ", Padham " + (h.getPaadham() != 0 ? String.valueOf(h.getPaadham()) : ""));
		// xlMgr.retreiveAndSetCells(s, padhamCf.getRow(), padhamCf.getCol(),
		// "Paadham " + strfmt.apply(h.getPaadham()));
		xlMgr.retreiveAndSetCells(s, birthplaceCf.getRow(), birthplaceCf.getCol(), h.getBirthPlace());
		xlMgr.retreiveAndSetCells(s, rasiCf.getRow(), rasiCf.getCol(), strfmt.apply(h.getRaasi()));
		xlMgr.retreiveAndSetCells(s, lagnamCf.getRow(), lagnamCf.getCol(), strfmt.apply(h.getLagnam()));
		xlMgr.retreiveAndSetCells(s, rahukethuCf.getRow(), rahukethuCf.getCol(), h.getRaahu_kethu());
		xlMgr.retreiveAndSetCells(s, sevvaiCf.getRow(), sevvaiCf.getCol(), h.getSevvai());
		xlMgr.retreiveAndSetCells(s, dasaCf.getRow(), dasaCf.getCol(), strfmt.apply(h.getDasa()));
		xlMgr.retreiveAndSetCells(s, iruppuCf.getRow(), iruppuCf.getCol(), h.getIruppu());
		Education e = null;
		if (!c.getEducations().isEmpty()) {
			e = c.getEducations().iterator().next();
		}
		xlMgr.retreiveAndSetCells(s, educationCf.getRow(), educationCf.getCol(),
				(e != null) ? strfmt.apply(e.getQualification()) : padString("", 20));

		Occupation o = null;
		if (!c.getOccupations().isEmpty()) {
			// as of now only one occupation is stored in excel
			o = c.getOccupations().iterator().next();
		}
		xlMgr.retreiveAndSetCells(s, occupation_salaryCf.getRow(), occupation_salaryCf.getCol(),
				strfmt.apply(o.getTitle()) + ", " + strfmt.apply(o.getSalary().longValue()));
		xlMgr.retreiveAndSetCells(s, workplaceCf.getRow(), workplaceCf.getCol(), o.getCompanyLocation());
		Note n = null;
		if (!c.getNotes().isEmpty()) {
			// as of now only one occupation is stored in excel
			Iterator<Note> i = c.getNotes().iterator();
			n = i.next();
			n = i.hasNext() ? i.next() : null;
		}

		xlMgr.retreiveAndSetCells(s, noteCf.getRow(), noteCf.getCol(), strfmt.apply(n.getNote()));

	}

	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm[:ss][a]");
	private static final Function<LocalDate, String> datefmt = (d) -> (d != null)
			? (d.format(dateFormatter) + "," + d.getDayOfWeek().toString()) : padString("", 20);
	private static final Function<LocalTime, String> timefmt = (d) -> (d != null) ? d.format(timeFormatter)
			: padString("", 20);
	private static final Function<Object, String> strfmt = (d) -> (d != null) ? d.toString() : padString("", 20);

	private static String formatAsString(Object s, int length) {
		if (s == null) {
			s = "";
		}
		return padString(s.toString(), length);
	}

	private static String padString(String s, int length) {
		if (s.trim().length() == 0) {
			return String.format("%-" + length + "s", s);
		}
		return s;
	}

	public static void main(String[] args) throws Exception {

//		if (args == null || args.length != 2) {
//			System.out.println("java com.poople.promat.ExcelProfileWriter <xls-to-import> <path-to-writer-conf-file>");
//			return;
//		}
//		final String fileName = args[0];
//		final String confFile = args[1];
//		List<String> sheetsToRead = null;
//        if (args.length > 1 && args[1] != null && args[1].length() > 0) {
//        	sheetsToRead = Arrays.asList((args[1].split(",")));
//    	}
		 final String fileName =
		 "D:/sandbox/promat2605/data/z_srinivasan_30052016.xls";
		 final String confFile =
		 "D:/sandbox/promat2605/promat/src/main/resources/a4print.conf";
		 final List<String> sheetsToRead = new ArrayList<String>();
		 sheetsToRead.add("Poornima300516");

		try {
			long startTime = System.currentTimeMillis();
			Collection<Candidate> cList = ExcelDataImport.importData(fileName, sheetsToRead);
			long timeTaken = System.currentTimeMillis() - startTime;
			logger.info("Import completed in " + (timeTaken / 1000) + "s");

			startTime = System.currentTimeMillis();
			ExcelProfileWriter xpw = new ExcelProfileWriter(confFile);
			xpw.write(cList);
			logger.info("Write completed in " + (timeTaken / 1000) + "s");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Loads the key and value pair from the given configuration file
	 * 
	 * @param strConfigFileName
	 * @throws Exception
	 */

	private Properties load(String strConfigFileName) throws Exception {
		Properties configProps = new Properties();
		try {
			configProps.load(new FileInputStream(strConfigFileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new Exception(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return configProps;
	}

}

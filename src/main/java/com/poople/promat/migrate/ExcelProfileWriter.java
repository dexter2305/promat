package com.poople.promat.migrate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.poople.promat.models.Candidate;
import com.poople.promat.models.Education;
import com.poople.promat.models.Horoscope;
import com.poople.promat.models.Note;
import com.poople.promat.models.Occupation;

public class ExcelProfileWriter {
	private String profileSheetName;
	private String excelPrintTemplateFile;
	private String horoscopeImageDir;
	private String profileWriteDir;
	private String photoImageDir;
	private String rasiTableBlank;

	private CellReference nameCf;
	private CellReference htCf;
	private CellReference userIdCf;
	private CellReference phCf;
	private CellReference kulamCf;
	private CellReference starCf;
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
	private CellReference noteCf; // not used as of now
	private CellReference navamsamtabCf;
	private CellReference rasitabCf;
	private Map<String, CellReference> cellRefMap = null;
	private File horoscopeImageDirFile;
	private File photoImageDirFile;
	private static final Log logger = LogFactory.getLog(ExcelProfileWriter.class);
	private static List<String> cellRefs = Arrays.asList("userid", "name", "ph", "kulam", "ht", "star", "date_day",
			"time", "birthplace", "rasi", "lagnam", "rahukethu", "sevvai", "dasa", "iruppu", "education",
			"occupation_salary", "workplace", "note", "rasitab", "navamsamtab");

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
		this.rasiTableBlank = configProps.getProperty("rasiTableBlank");
		this.photoImageDir = configProps.getProperty("photoImageDir");
		// Validate if all required cell references are present in input config
		List<String> notPresentList = cellRefs.stream().filter((propName) -> (!configProps.containsKey(propName)))
				.collect(Collectors.toList());
		if (notPresentList != null && notPresentList.size() > 0) {
			throw new Exception("One or more config values are not persent for following properties : "
					+ notPresentList.toString());
		}
		this.cellRefMap = cellRefs.stream().filter((propName) -> {
			if (configProps.containsKey(propName)) {
				String value = configProps.getProperty(propName, "");
				if (value != null && value.trim().length() <= 0) {
					// cell not specified so skip writing the cell
					logger.info(
							"No value given for property '" + propName + "' and it will NOT be written to template.");
					return false;
				} else {
					return true;
				}
			} else {
				return false;
			}
		}).collect(Collectors.toMap((propName) -> propName.toString(), (propName) -> {
			String value = configProps.getProperty(propName, "");
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

		if (!xlMgr.fileExists(this.rasiTableBlank)) {
			new Exception("Could not find blank rasi table image named '" + this.rasiTableBlank + "'");
		}
		try {
			photoImageDirFile = xlMgr.getFile(this.photoImageDir);
		} catch (Exception e) {
			new Exception("Could not find photo image directory named '" + this.photoImageDir + "'");
		}
		try {
			horoscopeImageDirFile = xlMgr.getFile(this.horoscopeImageDir);
		} catch (Exception e) {
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
		rasitabCf = cellRefMap.get("rasitab");
		navamsamtabCf = cellRefMap.get("navamsamtab");

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
					// FIXME not working
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

		xlMgr.retreiveAndSetCells(s, userIdCf, String.valueOf(c.getId()));
		String imgFileStatus = "";
		if (c.getExternalUserId() != null && !c.getExternalUserId().isEmpty()) {
			String[] fileNames = photoImageDirFile.list((File dirToFilter,
					String filename) -> ((filename.toUpperCase().startsWith("KK" + c.getExternalUserId()))
							&& (filename.toUpperCase().endsWith(".JPEG") || filename.toUpperCase().endsWith(".JPG")
									|| filename.toUpperCase().endsWith(".PNG")
									|| filename.toUpperCase().endsWith(".GIF"))));
			if (fileNames != null && fileNames.length > 0) {
				imgFileStatus = "Com"; // meaning present in C as in Computer

			}
		}

		xlMgr.retreiveAndSetCells(s, phCf, "Ph - " + imgFileStatus);
		xlMgr.retreiveAndSetCells(s, nameCf, c.getName());
		xlMgr.retreiveAndSetCells(s, htCf, formatAsString(c.getPhysique().getHeight(), 4) + "cm");
		xlMgr.retreiveAndSetCells(s, kulamCf, c.getKulam());
		// xlMgr.retreiveAndSetCells(s, date_dayCf.getRow(),
		// date_dayCf.getCol(),
		// (c.getDob().getBirthdate()!=null)?c.getDob().getBirthdate().format(DTFormatter.INSTANCE.getDateFormatter()):null);
		xlMgr.retreiveAndSetCells(s, date_dayCf, datefmt.apply(c.getDob().getBirthdate()));

		// xlMgr.retreiveAndSetCells(s, timeCf.getRow(), timeCf.getCol(),
		// (c.getDob().getBirthtime() !=
		// null)?c.getDob().getBirthtime().format(DTFormatter.INSTANCE.getTimeFormatter()):null);
		xlMgr.retreiveAndSetCells(s, timeCf, timefmt.apply(c.getDob().getBirthtime()));

		Horoscope h = c.getHoroscope();
		xlMgr.retreiveAndSetCells(s, starCf,
				strfmt.apply(h.getStar()) + ", Padham " + (h.getPaadham() != 0 ? String.valueOf(h.getPaadham()) : ""));
		// xlMgr.retreiveAndSetCells(s, padhamCf.getRow(), padhamCf.getCol(),
		// "Paadham " + strfmt.apply(h.getPaadham()));
		xlMgr.retreiveAndSetCells(s, birthplaceCf, h.getBirthPlace());
		xlMgr.retreiveAndSetCells(s, rasiCf, strfmt.apply(h.getRaasi()));
		xlMgr.retreiveAndSetCells(s, lagnamCf, strfmt.apply(h.getLagnam()));
		xlMgr.retreiveAndSetCells(s, rahukethuCf, h.getRaahu_kethu());
		xlMgr.retreiveAndSetCells(s, sevvaiCf, h.getSevvai());
		xlMgr.retreiveAndSetCells(s, dasaCf, strfmt.apply(h.getDasa()));
		xlMgr.retreiveAndSetCells(s, iruppuCf, h.getIruppu());
		String joinedQual = "";
		Set<Education> e = c.getEducations();
		if (e != null) {
			joinedQual = e.stream().map(Education::getQualification).collect(Collectors.joining(", "));
		}
		xlMgr.retreiveAndSetCells(s, educationCf, strfmt.apply(joinedQual));

		Occupation o = null;
		if (!c.getOccupations().isEmpty()) {
			// as of now only one occupation is stored in excel
			o = c.getOccupations().iterator().next();
		}
		Long salary = null;
		if (o.getSalary() != null) {
			salary = o.getSalary().longValue();
		}
		xlMgr.retreiveAndSetCells(s, occupation_salaryCf, strfmt.apply(o.getTitle()) + ", " + strfmt.apply(salary));
		xlMgr.retreiveAndSetCells(s, workplaceCf, o.getCompanyLocation());
		Note n = null;
		if (!c.getNotes().isEmpty()) {
			// as of now only one note is stored in excel
			Iterator<Note> i = c.getNotes().iterator();
			n = i.next();
			n = i.hasNext() ? i.next() : null;
		}
		xlMgr.retreiveAndSetCells(s, noteCf, strfmt.apply(n.getNote()));

		writeHoroTables(wb, s, c);
	}

	private void writeHoroTables(Workbook wb, Sheet s, Candidate c) {
		String rasiTab = this.rasiTableBlank;
		String navamsamTab = this.rasiTableBlank;

		if (c.getExternalUserId() != null && !c.getExternalUserId().trim().isEmpty()) {
			logger.debug("Rasi Table Name: " + "KK" + c.getExternalUserId());
			String[] fileNames = horoscopeImageDirFile.list((File dirToFilter,
					String filename) -> (filename.toUpperCase().startsWith("KK" + c.getExternalUserId())));
			if (fileNames != null && fileNames.length > 0) {
				for (String f : fileNames) {
					if (f.contains("NAVAMSATAM")) {
						navamsamTab = this.horoscopeImageDir + f;
					} else if (f.contains("RASITAM")) {
						rasiTab = this.horoscopeImageDir + f;
					}
				}

			}
		}
		xlMgr.setImage(wb, s, rasitabCf.getRow(), rasitabCf.getCol(), rasiTab);
		xlMgr.setImage(wb, s, navamsamtabCf.getRow(), navamsamtabCf.getCol(), navamsamTab);
	}

	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm[:ss][a]");
	private static final Function<String, String> toCamelCase = (s) -> (s.substring(0, 1) + s.substring(1).toLowerCase());
	private static final Function<LocalDate, String> datefmt = (d) -> (d != null)
			? (d.format(dateFormatter) + ", " + toCamelCase.apply(d.getDayOfWeek().toString())) : padString("", 20);
	
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
		ExcelProfileWriter.test(args);
		//ExcelProfileWriter.profileWriter(args);
	}
	public static void profileWriter(String[] args) throws Exception {

		 if (args == null || args.length < 2) {
			 System.out.println("java com.poople.promat.ExcelProfileWriter <path-to-writer-conf-file> <xls-to-import> [sheet1,sheet2]");
			 System.out.println("if argument #3 is not provided, all sheets in the excel will be read.");
			 return;
		 }
		 final String confFile = args[0];
		 final String fileName = args[1];
		 
		 List<String> sheetsToRead = null;
		 if (args.length > 2 && args[2] != null && args[2].length() > 0) {
			 sheetsToRead = Arrays.asList((args[2].split(",")));
		 }

		try {
			long startTime = System.currentTimeMillis();
			Collection<Candidate> cList = ExcelDataImport.importData(fileName, sheetsToRead);
			long timeTaken = System.currentTimeMillis() - startTime;
			logger.info("Import completed in " + (timeTaken / 1000) + "s");

			startTime = System.currentTimeMillis();
			ExcelProfileWriter xpw = new ExcelProfileWriter(confFile);
			xpw.write(cList);
			timeTaken = System.currentTimeMillis() - startTime;
			logger.info("Write completed in " + (timeTaken / 1000) + "s");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public static void test(String[] args) throws Exception {
		final String fileName = "D:/sandbox/promat2605/data/z_srinivasan_06062016.xls";
		final String confFile = "D:/sandbox/promat2605/promat/src/main/resources/a4print.conf";
		final String sheetsToRead = "sree";
		String[] newArgs = {confFile, fileName, sheetsToRead};
		ExcelProfileWriter.profileWriter(newArgs);
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

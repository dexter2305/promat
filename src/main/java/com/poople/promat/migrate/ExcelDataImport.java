package com.poople.promat.migrate;

import com.poople.promat.adapters.DTFormatter;
import com.poople.promat.models.*;
import com.poople.promat.models.HoroscopeConstants.Planet;
import com.poople.promat.models.HoroscopeConstants.Raasi;
import com.poople.promat.models.HoroscopeConstants.Star;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExcelDataImport {

    private static final Log logger = LogFactory.getLog(ExcelDataImport.class);
    //skip the header row and next row (2 rows) 
    public static int EXCEL_READ_ROW_STARTING_FROM_NUM = 2;
    public static Collection<Candidate> importData(String fileName) throws IOException {
        return importData(fileName, null);
    }
    public static Collection<Candidate> importData(Properties configProps, String fileName, List<String> sheetsToRead) throws Exception {
    	setExcelConstants(configProps);
    	return importData(fileName, sheetsToRead);
    }
    private static class Report {
        private int sheetNumber;
        private long numberOfRowsRead;
        private long numberOfBeansLoaded;
    }


    public static Collection<Candidate> importData(String fileName, List<String> sheetsToRead) throws IOException {
    	logger.info("Importing file :" + fileName + " sheets :"+ sheetsToRead);
        final Collection<Candidate> candidates = new LinkedList<>();
        Set<Report> reports = new LinkedHashSet<>();
        FileInputStream fileInputStream = new FileInputStream(fileName);
        //Use generix workbook class so we can read any excel format like .xls too
        Workbook workbook;
		try {
			workbook = WorkbookFactory.create(fileInputStream);
		} catch (EncryptedDocumentException | InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new IOException(e.getMessage());
		}
        final String csvFileName = fileName.substring(0, fileName.indexOf(".")) + ".csv";
        FileWriter csvWriter = new FileWriter(csvFileName);
        logger.info(fileName + " read completed.");
        logger.info("Number of sheets:" + workbook.getNumberOfSheets());
        boolean readAllSheets = false;
        if(sheetsToRead == null ) {
        	readAllSheets = true;
        }
        logger.info("readAllSheets:" + readAllSheets);
        logger.info("sheetsToRead:" + sheetsToRead);
        int sheetCounter = 0;
        long beanCounter;
        Candidate candidate = null;
        for (Sheet sheet : workbook) {
            Report report = new Report();
            beanCounter = 0;
            logger.info("Reading sheet # " + (sheetCounter + 1) + " with row count = " + sheet.getLastRowNum() + 1);
            for (Row row : sheet) {
            	if(!readAllSheets && !sheetsToRead.contains(row.getSheet().getSheetName())) {
            		continue;
            	}
                
                if (row.getRowNum() < EXCEL_READ_ROW_STARTING_FROM_NUM) continue;
                try {
					candidate = getCandidateBean(row);
					if (candidate != null) {
	                    candidates.add(candidate);
	                    beanCounter++;
	                    logger.info((sheetCounter + 1) + "->" + (row.getRowNum() + 1 ) + " -> " + candidate.toString());
	                } else {
	                    writeAsCSV(csvWriter, row, "Unknown error while reading row. Please check log file.");
	                    logger.error((sheetCounter + 1) + "->" + (row.getRowNum() + 1) + " -> Unknown error while reading row. Please check log file.");
	                }
				} catch (DataError e) {
					writeAsCSV(csvWriter, row, "Error while reading row : "+ e.getMessage());
                    logger.error((sheetCounter + 1) + "->" + (row.getRowNum() + 1) + " -> Error while reading row : "+ e.getMessage());
				}
                
            }
            report.sheetNumber = sheetCounter + 1;
            report.numberOfRowsRead = sheet.getLastRowNum();
            report.numberOfBeansLoaded = beanCounter;
            reports.add(report);
            sheetCounter++;
        }
        csvWriter.flush();
        csvWriter.close();
        logger.info("*** REPORT ***");
        long sumB = 0, sumR = 0;
        Iterator<Report> iterator = reports.iterator();
        while (iterator.hasNext()) {
            Report report = iterator.next();
            logger.info("Sheet # " + report.sheetNumber + " loaded " + report.numberOfBeansLoaded + " from " + report.numberOfRowsRead + " rows.");
            sumB += report.numberOfBeansLoaded;
            sumR += report.numberOfRowsRead;
        }
        logger.info("Total beans loaded " + sumB + " from " + sumR + " rows.");
        logger.info("All invalid records are recorded as CSV in " + csvFileName);
        return candidates;
    }

    private static void writeAsCSV(FileWriter writer, Row row, String errorMsg) {
        Iterator<Cell> cellIterator = row.cellIterator();
        final StringBuilder rowBuilder = new StringBuilder();
        rowBuilder.append(row.getSheet().getSheetName() + " - row :" + (row.getRowNum() + 1)).append("->").append("|");
        rowBuilder.append(errorMsg).append("|");
        int cellNum = 0;
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            String cellValue = getValueAsString(cell);
            rowBuilder.append(cellValue).append("|");
            cellNum++;
            if (cellNum >= 29) { // skip writing notes cells
            	break;
            }
        }
        String rowAsCSV = rowBuilder.toString();
        try {
            writer.write(rowAsCSV);
            writer.write("\n");
            writer.flush();
        } catch (IOException e) {
            logger.error("Exception while writing " + (row.getRowNum() + 1) + " to error file. ", e);
        }
    }

    private static Candidate getCandidateBean(Row row) throws DataError {
        if (row == null) return null;
        validateMandatoryFields(row);
        Candidate candidate = new Candidate();
    
        //Candidate::Id
        //candidate.setId(IDGenerator.INSTANCE.getUUID());
        //need to fetch the id from excel itself
        //mandatory field
        long id = getValueAsLong(row.getCell(ExcelColumns.USER_ID.asInt()));
        candidate.setId(id);
        //Candidate::Name;
        candidate.setName(getValueAsString(row.getCell(ExcelColumns.NAME.asInt())));

        //Candidate::DOB
        Dob dob = getDOB(getDateValueAsString(row.getCell(ExcelColumns.DATE_OF_BIRTH.asInt()), DTFormatter.INSTANCE.getDateFormatter()), getDateValueAsString(row.getCell(ExcelColumns.TIME_OF_BIRTH.asInt()),DTFormatter.INSTANCE.getTimeFormatter()));
        candidate.setDob(dob);
        
        //Candidate::Marital Staus
        candidate.setMaritalStatus(getValueAsString(row.getCell(ExcelColumns.MARITAL_STATUS.asInt())));
        
        //Candidate:kulam
        candidate.setKulam(getValueAsString(row.getCell(ExcelColumns.KULAM.asInt())));
        
        //Candidate::Horoscope
    	Star star = HoroscopeConstants.Star.fromString(getValueAsString(row.getCell(ExcelColumns.STAR.asInt())));
        String birthPlace = getValueAsString(row.getCell(ExcelColumns.NATIVE.asInt()));
        int paadham = getPaadham(row.getCell(ExcelColumns.PADHAM.asInt()));
		Raasi raasi = HoroscopeConstants.Raasi.fromString(getValueAsString(row.getCell(ExcelColumns.RASI.asInt())));
		Raasi lagnam = HoroscopeConstants.Raasi.fromString(getValueAsString(row.getCell(ExcelColumns.LAGNAM.asInt())));
		String raahu_kethu = getValueAsString(row.getCell(ExcelColumns.RAAHU_KETHU.asInt()));
		String sevvai = getValueAsString(row.getCell(ExcelColumns.SEVVAI.asInt()));
		Planet dasa = HoroscopeConstants.Planet.fromString(getValueAsString(row.getCell(ExcelColumns.DASA.asInt())));
		String iruppu = getValueAsString(row.getCell(ExcelColumns.IRUPPU.asInt()));
		Horoscope horoscope = new Horoscope(dob, birthPlace, star, paadham, raasi, lagnam, raahu_kethu, sevvai, dasa, iruppu);
		candidate.setHoroscope(horoscope);
		
        //Candidate::gender
        candidate.setGender(Candidate.Gender.fromString(getValueAsString(row.getCell(ExcelColumns.GENDER.asInt()))));
        //Physique::skinTone
        candidate.getPhysique().setSkinTone(Physique.SkinTone.fromString(getValueAsString(row.getCell(ExcelColumns.SKINTONE.asInt()))));
        //Physique::bloodGroup
        candidate.getPhysique().setBloodGroup(Physique.Bloodgroup.fromString(getValueAsString(row.getCell(ExcelColumns.BLOODGROUP.asInt()))));
        //Physique::height
        Long heightInCms = getHeightInCms(getValueAsString(row.getCell(ExcelColumns.HEIGHT.asInt())));
        candidate.getPhysique().setHeight(heightInCms);
        Collection<String> phoneNumbers = getPhoneNumbers(new Cell[]{row.getCell(ExcelColumns.PHONE_1.asInt()), row.getCell(ExcelColumns.PHONE_2.asInt()), row.getCell(ExcelColumns.PHONE_3.asInt())});
        //Contact::phoneNumbers
        candidate.getContact().addPhoneNumbers(phoneNumbers);
        //Candidate::Education
        Collection<Education> educations = getEducations(row.getCell(ExcelColumns.EDUCATION.asInt()));
        candidate.getEducations().addAll(educations);
        //Candidate::Occupation
        Occupation occupation = getOccupation(getValueAsString(row.getCell(ExcelColumns.OCCUPATION_TITLE.asInt())), getValueAsString(row.getCell(ExcelColumns.OCCUPATION_SALARY.asInt())), getValueAsString(row.getCell(ExcelColumns.OCCUPATION_PLACE.asInt())));
        occupation.setCurrent(true);
        candidate.getOccupations().add(occupation);
        //Candidate::ExternalUserId
        candidate.setExternalUserId(getValueAsString(row.getCell(ExcelColumns.EXTERNAL_USER_ID.asInt())));
        //Candidate::Note
        Collection<Note> notes = getNotes(new String[]{getValueAsString(row.getCell(ExcelColumns.NOTE_1.asInt())), getValueAsString(row.getCell(ExcelColumns.NOTE_2.asInt())), getValueAsString(row.getCell(ExcelColumns.NOTE_3.asInt()))});
        candidate.getNotes().addAll(notes);
    
        return candidate;
    }

    private static Dob getDOB(String birthdateAsString, String birthtimeAsString) throws DataError {
    	logger.debug("ENTER - getDOB() :"+ birthdateAsString + ", " + birthtimeAsString);
        Dob dob = new Dob();
        dob.setBirthdate(DTFormatter.INSTANCE.getLocalDateFrom(birthdateAsString));
        dob.setBirthtime(DTFormatter.INSTANCE.getLocalTimeFrom(birthtimeAsString));
        logger.debug("EXIT - getDOB() :"+ dob.toString());
        return dob;        
    }

    private static Collection<Note> getNotes(String[] notes) {
        if (notes == null) return Collections.emptySet();
        List<Note> noteList = Stream.of(notes)
                .parallel()
                .map(s -> {
                    Note n = new Note();
                    n.setNote(s);
                    return n;
                })
                .collect(Collectors.toList());
        return noteList;
    }

    private static Occupation getOccupation(String title, String salary, String place) {
        Occupation occupation = new Occupation();
        occupation.setTitle(title != null ? title.trim() : title);
        occupation.setCompanyLocation(place != null ? place.trim() : place);
        long multiples = 1;
        if (salary != null) {
            salary = salary.trim();
            salary = salary.replaceAll(" ", "");
            if (salary.indexOf("L") > 0 || salary.indexOf("l") > 0) {
                salary = salary.replaceAll("[Ll]", "");
                multiples = 100000;
            }else if (salary.indexOf("K") > 0 || salary.indexOf("k") > 0) {
                salary = salary.replaceAll("[Kk]", "");
                multiples = 1000;
            }  
            try {
                Double aDouble = Double.parseDouble(salary);
                occupation.setSalary(aDouble * multiples);
            } catch (NumberFormatException nfe) {
            }
        }
        return occupation;
    }


    private static Collection<Education> getEducations(Cell cell) {
        if (null == cell) return Collections.emptySet();
        Collection<Education> educations;
        String value = getValueAsString(cell);
        if (value != null && !value.isEmpty()) {
            educations = new LinkedHashSet<>();
            StringTokenizer tokenizer = new StringTokenizer(value, ",", false);
            while (tokenizer.hasMoreTokens()) {
                Education education = new Education();
                education.setQualification(tokenizer.nextToken());
                educations.add(education);
            }
        } else {
            educations = Collections.emptySet();
        }
        return educations;
    }

    private static Collection<String> getPhoneNumbers(Cell[] cells) {
        if (cells == null || cells.length == 0) return Collections.emptyList();
        Collection<String> phoneNumbers = Stream.of(cells)
                .map(cell -> getValueAsString(cell))
                .filter(s -> s != null && !s.isEmpty())
                .map(s -> s.trim())
                .filter(s -> s.matches("(\\d)+(-)?"))
                .collect(Collectors.toSet());
        return phoneNumbers;
    }

    private static Long getHeightInCms(String height) {
    	logger.debug("ENTER - getHeightInCms() :"+ height);
        if (height == null || height.trim().isEmpty()) return null;
        try {
        	height = height.replaceAll("'", "");
        	height = height.replaceAll("\"", "");
        	height = height.replaceAll("”", "");
        	Double heightInCms = 0d;
        	Double heightInInches = 0d;
        	int decimalPt = height.indexOf(".");
        	if(decimalPt != -1 && height.length() != decimalPt) {
        		logger.debug("EXIT - getHeightInCms() Feet p1:"+ height.substring(0,decimalPt));
        		logger.debug("EXIT - getHeightInCms() Feet p2:"+ height.substring(decimalPt+1));
        		heightInInches = Double.parseDouble(height.substring(0,decimalPt)) * 12 + Double.parseDouble(height.substring(decimalPt+1));
        	}else {
        		heightInInches = Double.parseDouble(height) * 12;
        	}
        	heightInCms = heightInInches * 2.54;
            logger.debug("EXIT - getHeightInCms() :"+ Math.round(heightInCms));
            return Math.round(heightInCms);
        } catch (NumberFormatException nfe) {
        	nfe.printStackTrace();
            return null;
        }        
    }

    public static String getDateValueAsString(Cell cell, DateTimeFormatter dtf) throws DataError {
    	logger.debug("ENTER - getDateValueAsString : " + cell);
        if (cell == null) return null;
        String dateValueAsString;
        try {

        	if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC && cell.getDateCellValue() != null) 
        	{
	        	LocalDate dateValueAsLocalDate = DTFormatter.INSTANCE.getLocalDateFrom(cell.getDateCellValue());
	        	logger.debug("dateValueAsLocalDate : " + dateValueAsLocalDate);
	            dateValueAsString = dateValueAsLocalDate.format(dtf);
        	}else {
        		//FIXME this may be needed until we use excel for profile matching 
        		String tempStr = cell.getStringCellValue(); 
        		if(tempStr == null || tempStr.trim().isEmpty() || tempStr.contains("*")) 
        		{
	            	dateValueAsString = null;
	            }else {
		            dateValueAsString = tempStr;
	        	}
        	}
        } catch (Exception ise) {
            dateValueAsString = null;
            throw new DataError("Invalid DOB :"+ ise.toString());
        }
        logger.debug("EXIT - getDateValueAsString : " + dateValueAsString);
        return dateValueAsString;
    }

    private static String getValueAsString(Cell cell) {
    	logger.debug("ENTER - getValueAsString");
        if (cell == null) return null;
        if (cell.getCellType() != Cell.CELL_TYPE_STRING) {
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }
        String stringCellValue = cell.getStringCellValue();
        if(stringCellValue.contains("Not Specified")) {
        	stringCellValue.replaceAll("Not Specified", "");
        }
        //FIXME this may be needed until we use excel for profile matching 
        if(stringCellValue.contains("*") || stringCellValue.trim().isEmpty()) {
        	stringCellValue = null;
        }
        
        logger.debug("EXIT - getValueAsString : " + stringCellValue);
        return stringCellValue;
    }

    private static Long getValueAsLong(Cell cell) {
        String value = getValueAsString(cell);
        if ( !(value == null || value.isEmpty())) {
        	long id = 0L;
	        try {
	        	id = Long.parseLong(value);
	        	return id;
	        }catch(NumberFormatException nfe) {
	        	//nfe.printStackTrace();
	        	return null;
	        }
        }
        return null;
    }
    
    private static int getPaadham(Cell cell) {
    	Long p = getValueAsLong(cell);
        int paadham = 0;
		if (p != null) {
			paadham = p.intValue();
		}
		return paadham;
    }
    private static void validateMandatoryFields(Row row) throws DataError {
        //Request nameRequest = new Request(row.getCell(ExcelColumns.NAME.asInt()), ExcelColumns.NAME);
        //Request genderRequest = new Request(row.getCell(ExcelColumns.GENDER.asInt()), ExcelColumns.GENDER);
        Request idRequest = new Request(row.getCell(ExcelColumns.USER_ID.asInt()), ExcelColumns.USER_ID);
        List<Request> mandatoryFields = Arrays.asList(new Request[]{idRequest});
        List<Response> responseList = mandatoryFields.stream().map(request -> isCellValid(request)).collect(Collectors.toList());
        Optional<Response> any = responseList.parallelStream().filter(response -> response.isValid == false).findAny();
        if (any.isPresent()) {
            final StringBuilder messagebuilder = new StringBuilder();
            messagebuilder.append("Row " + row.getRowNum() + " has following invalid fields : ");
            responseList.stream().filter(response -> response.isValid == false).forEach(response -> {
                messagebuilder.append("<" + response.getFieldType() + ">").append(" ");
            });
            throw new DataError("Invalid Mandatory Fields :"+ messagebuilder.toString());
        } 
    }


    private static Response isCellValid(Request request) {
        Response r = new Response(request);
        if(request.fieldType == ExcelColumns.USER_ID) {
        	r.isValid = isCellValidNumber(request.cell);
        } else {
        	r.isValid = isCellValid(request.cell);
        }
        return r;
    }

    private static boolean isCellValid(Cell cell) {
        if (cell == null) return false;
        if (cell.getCellType() != Cell.CELL_TYPE_STRING) {
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }
        String value = cell.getRichStringCellValue().getString();

        return !(value == null || value.isEmpty());
    }
    
    private static boolean isCellValidNumber(Cell cell) {
        Long value = getValueAsLong(cell);
        return (value != null);
    }

	public static void main(String[] args) throws Exception {
		//ExcelDataImport.test(args);
		ExcelDataImport.dataImporter(args);
	}
	public static void dataImporter(String[] args) throws Exception {
        if (args == null || args.length < 1) {
            System.out.println("java com.poople.promat.migrate.ExcelDataImport <path-to-xls-file> [sheetname1,sheetname2,sheetname3]");
            System.out.println("if argument #2 is not provided, all sheets in the excel will be read.");
            return;
        }
        final String fileName = args[0];
        List<String> sheetsToRead = null;
        if (args.length > 1 && args[1] != null && args[1].length() > 0) {
        	sheetsToRead = Arrays.asList((args[1].split(",")));
    	}
        try {
            long startTime = System.currentTimeMillis();
            ExcelDataImport.importData(fileName, sheetsToRead);
            long timeTaken = System.currentTimeMillis() - startTime;
            logger.info("Completed in " + (timeTaken / 1000) + "s");
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	public static void test(String[] args) throws Exception {
		final String confPropFileName = "D:/sandbox/promat2605/promat/src/main/resources/import.conf";
		//final String fileName = "D:/sandbox/promat2605/data/z_srinivasan_04072016.xls";
		//final String sheetsToRead = "sree";
		final String fileName = "D:/sandbox/promat2605/data/z_lakshmi_04072016.xls";
		final String sheetsToRead = "Sheet1";
		String[] newArgs = {confPropFileName, fileName, sheetsToRead};
		ExcelDataImport.dataImporterWithConfig(newArgs);
	}
	public static void dataImporterWithConfig(String[] args) throws Exception {
		if (args == null || args.length < 2) {
            System.out.println("java com.poople.promat.migrate.ExcelDataImport <config-props-file> <path-to-xls-file> [sheetname1,sheetname2,sheetname3]");
            System.out.println("if argument #3 is not provided, all sheets in the excel will be read.");
            return;
        }
		Properties configProps = new Properties();
		configProps.load(new FileInputStream(args[0]));
        final String fileName = args[1];
        List<String> sheetsToRead = null;
        if (args.length > 2 && args[2] != null && args[2].length() > 0) {
        	sheetsToRead = Arrays.asList((args[2].split(",")));
    	}
        try {
            long startTime = System.currentTimeMillis();
            ExcelDataImport.importData(configProps,fileName, sheetsToRead);
            long timeTaken = System.currentTimeMillis() - startTime;
            logger.info("Completed in " + (timeTaken / 1000) + "s");
        } catch (IOException e) {
            e.printStackTrace();
        }

		
	}
	private static void setExcelConstants(Properties configProps) throws Exception{
		List<String> cellRefs = Arrays.asList("userid.col", "extid.col", "sex.col", "name.col", "star.col", "age.col",
				"dob.col", "time.col", "birthplace.col", "kulam.col", "padham.col", "rasi.col", "lagnam.col", "rk.col",
				"sev.col", "dasa.col", "iruppu.col", "education.col", "work.place.col", "salary.col", "tech.qual.col",
				"complexion.col", "ht.col", "blood.group.col", "phone1.col", "phone2.col", "phone3.col", "late.col",
				"marital.status.col", "note1.col", "note2.col", "note3.col");
		// Validate if all required cell references are present in input config
		List<String> notPresentList = cellRefs.stream().filter((propName) -> (!configProps.containsKey(propName)))
				.collect(Collectors.toList());
		if (notPresentList != null && notPresentList.size() > 0) {
			throw new Exception("One or more config values are not persent for following properties : "
					+ notPresentList.toString());
		}
		ExcelColumns.USER_ID.setColumnIndex(Integer.parseInt(configProps.getProperty("userid.col")));
		ExcelColumns.EXTERNAL_USER_ID.setColumnIndex(Integer.parseInt(configProps.getProperty("extid.col")));
		ExcelColumns.GENDER.setColumnIndex(Integer.parseInt(configProps.getProperty("sex.col")));
		ExcelColumns.NAME.setColumnIndex(Integer.parseInt(configProps.getProperty("name.col")));
		ExcelColumns.STAR.setColumnIndex(Integer.parseInt(configProps.getProperty("star.col")));
		ExcelColumns.DATE_OF_BIRTH.setColumnIndex(Integer.parseInt(configProps.getProperty("dob.col")));
		ExcelColumns.TIME_OF_BIRTH.setColumnIndex(Integer.parseInt(configProps.getProperty("time.col")));
		ExcelColumns.NATIVE.setColumnIndex(Integer.parseInt(configProps.getProperty("birthplace.col")));
		ExcelColumns.KULAM.setColumnIndex(Integer.parseInt(configProps.getProperty("kulam.col")));
		ExcelColumns.PADHAM.setColumnIndex(Integer.parseInt(configProps.getProperty("padham.col")));
		ExcelColumns.RASI.setColumnIndex(Integer.parseInt(configProps.getProperty("rasi.col")));
		ExcelColumns.LAGNAM.setColumnIndex(Integer.parseInt(configProps.getProperty("lagnam.col")));
		ExcelColumns.RAAHU_KETHU.setColumnIndex(Integer.parseInt(configProps.getProperty("rk.col")));
		ExcelColumns.SEVVAI.setColumnIndex(Integer.parseInt(configProps.getProperty("sev.col")));
		ExcelColumns.DASA.setColumnIndex(Integer.parseInt(configProps.getProperty("dasa.col")));
		ExcelColumns.IRUPPU.setColumnIndex(Integer.parseInt(configProps.getProperty("iruppu.col")));
		ExcelColumns.EDUCATION.setColumnIndex(Integer.parseInt(configProps.getProperty("education.col")));
		ExcelColumns.OCCUPATION_PLACE.setColumnIndex(Integer.parseInt(configProps.getProperty("work.place.col")));
		ExcelColumns.OCCUPATION_SALARY.setColumnIndex(Integer.parseInt(configProps.getProperty("salary.col")));
		ExcelColumns.OCCUPATION_TITLE.setColumnIndex(Integer.parseInt(configProps.getProperty("tech.qual.col")));
		ExcelColumns.SKINTONE.setColumnIndex(Integer.parseInt(configProps.getProperty("complexion.col")));
		ExcelColumns.HEIGHT.setColumnIndex(Integer.parseInt(configProps.getProperty("ht.col")));
		ExcelColumns.BLOODGROUP.setColumnIndex(Integer.parseInt(configProps.getProperty("blood.group.col")));
		ExcelColumns.PHONE_1.setColumnIndex(Integer.parseInt(configProps.getProperty("phone1.col")));
		ExcelColumns.PHONE_2.setColumnIndex(Integer.parseInt(configProps.getProperty("phone2.col")));
		ExcelColumns.PHONE_3.setColumnIndex(Integer.parseInt(configProps.getProperty("phone3.col")));
		ExcelColumns.LATE.setColumnIndex(Integer.parseInt(configProps.getProperty("late.col")));
		ExcelColumns.MARITAL_STATUS.setColumnIndex(Integer.parseInt(configProps.getProperty("marital.status.col")));
		ExcelColumns.NOTE_1.setColumnIndex(Integer.parseInt(configProps.getProperty("note1.col")));
		ExcelColumns.NOTE_2.setColumnIndex(Integer.parseInt(configProps.getProperty("note2.col")));
		ExcelColumns.NOTE_3.setColumnIndex(Integer.parseInt(configProps.getProperty("note3.col")));
		EXCEL_READ_ROW_STARTING_FROM_NUM = Integer.parseInt(configProps.getProperty("row.number.starting"));
		
	    }

    enum ExcelColumns {
    	USER_ID(0),
        EXTERNAL_USER_ID(1),
        GENDER(2),
        NAME(3),
        STAR(4),
        DATE_OF_BIRTH(6),
        TIME_OF_BIRTH(7),
        NATIVE(8),
        KULAM(9),
        PADHAM(10),
        RASI(11),
        LAGNAM(12),
        RAAHU_KETHU(13),
        SEVVAI(14),
        DASA(15),
        IRUPPU(16),
        EDUCATION(17),
        OCCUPATION_PLACE(18),
        OCCUPATION_SALARY(19),
        OCCUPATION_TITLE(20),
        SKINTONE(21),
        HEIGHT(22),
        BLOODGROUP(23),
        PHONE_1(24),
        PHONE_2(25),
        PHONE_3(26),
        LATE(27),
        MARITAL_STATUS(28),
        NOTE_1(29),
        NOTE_2(30),
        NOTE_3(31);


        private int columnIndex;

        ExcelColumns(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        int asInt() {
            return columnIndex;
        }

		public int getColumnIndex() {
			return columnIndex;
		}

		public void setColumnIndex(int columnIndex) {
			this.columnIndex = columnIndex;
		}

    }

    private static class Request {
        private Cell cell;
        private ExcelColumns fieldType;

        Request(Cell cell, ExcelColumns fieldType) {
            this.cell = cell;
            this.fieldType = fieldType;
        }
    }

    private static class Response {
        private Request request;
        private boolean isValid;

        Response(Request request) {
            this.request = request;
        }

        Cell getCell() {
            return this.request.cell;
        }

        ExcelColumns getFieldType() {
            return this.request.fieldType;
        }
    }
}

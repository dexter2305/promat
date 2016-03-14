package com.poople.promat.migrate;

import com.poople.promat.models.*;
import com.poople.promat.persistence.IDGenerator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.util.SystemOutLogger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sun.util.resources.cldr.aa.CalendarData_aa_ER;

import javax.swing.text.DateFormatter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExcelDataImport {

    private static final Log logger = LogFactory.getLog(ExcelDataImport.class);

    private static class Report {
        private int sheetNumber;
        private long numberOfRowsRead;
        private long numberOfBeansLoaded;
    }

    public static Collection<Candidate> importData(String fileName) throws IOException {
        Set<Report> reports = new LinkedHashSet<>();
        FileInputStream fileInputStream = new FileInputStream(fileName);
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        final String csvFileName = fileName.substring(0, fileName.indexOf(".")) + ".csv";
        FileWriter csvWriter = new FileWriter(csvFileName);
        logger.info(fileName + " read completed.");
        logger.info("Number of sheets:" + workbook.getNumberOfSheets());
        Collection<Candidate> candidates = new ArrayList<>();
        int sheetCounter = 0;
        long beanCounter;
        Candidate candidate;
        for (Sheet sheet : workbook) {
            Report report = new Report();
            beanCounter = 0;
            logger.debug("Reading sheet # " + (sheetCounter + 1) + " with row count = " + sheet.getLastRowNum());
            for (Row row : sheet) {
                //skip the header row
                if (row.getRowNum() == 0) continue;
                candidate = getCandidateBean(row);
                if (candidate != null) {
                    candidates.add(candidate);
                    beanCounter++;
                    logger.debug((sheetCounter) + "->" + (row.getRowNum()) + " -> " + candidate.toString());
                } else {
                    writeAsCSV(csvWriter, row);
                    logger.error("Error while reading row @ " + (sheetCounter + 1) + "/" + (row.getRowNum()));
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

    private static void writeAsCSV(FileWriter writer, Row row) {
        Iterator<Cell> cellIterator = row.cellIterator();
        final StringBuilder rowBuilder = new StringBuilder();
        rowBuilder.append(row.getSheet().getSheetName() + " - row :" + (row.getRowNum() + 1)).append("->").append("|");
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            String cellValue = getValueAsString(cell);
            rowBuilder.append(cellValue).append("|");
        }
        String rowAsCSV = rowBuilder.toString();
        try {
            writer.write(rowAsCSV);
            writer.write("\n");
            writer.flush();
        } catch (IOException e) {
            logger.error("Exception while writing " + row.getRowNum() + " to error file. ", e);
        }
    }

    private static Candidate getCandidateBean(Row row) {
        if (row == null) return null;
        if (!areMandatoryFieldsPresent(row)) return null;
        Candidate candidate = new Candidate();
        //Candidate::Id
        candidate.setId(IDGenerator.INSTANCE.getUUID());
        //Candidate::Name;
        candidate.setName(getValueAsString(row.getCell(ExcelColumns.NAME.asInt())));
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
        candidate.getOccupations().add(occupation);
        //Candidate::ExternalUserId
        candidate.setExternalUserId(getValueAsString(row.getCell(ExcelColumns.EXTERNAL_USER_ID.asInt())));
        //Candidate::Note
        Collection<Note> notes = getNotes(new String[]{getValueAsString(row.getCell(ExcelColumns.NOTE_1.asInt())), getValueAsString(row.getCell(ExcelColumns.NOTE_2.asInt())), getValueAsString(row.getCell(ExcelColumns.NOTE_3.asInt())), getValueAsString(row.getCell(ExcelColumns.NOTE_4.asInt()))});
        candidate.getNotes().addAll(notes);
        //Candidate::DOB
        Dob dob = getDOB(getValueAsDate(row.getCell(ExcelColumns.DATE_OF_BIRTH.asInt())), getValueAsString(row.getCell(ExcelColumns.TIME_OF_BIRTH.asInt())));
        candidate.setDob(dob);
        return candidate;
    }

    private static Dob getDOB(Date birthdateAsDate, String birthtimeAsString) {
        Dob dob = new Dob();
        if (birthdateAsDate == null) return dob;
        Calendar cal = Calendar.getInstance();
        cal.setTime(birthdateAsDate);
        LocalDate birthdate = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE));
        dob.setBirthdate(birthdate);
        birthtimeAsString = birthtimeAsString.replaceAll(" ", "").toUpperCase();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh.mma");
        try {
            LocalTime birthTime = LocalTime.parse(birthtimeAsString, formatter);
            dob.setBirthtime(birthTime);
        }catch(DateTimeParseException dtpe){

        }

        return dob;
    }

    private static Collection<Note> getNotes(String[] notes) {
        if (notes == null) return Collections.EMPTY_SET;
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
        if (salary != null) {
            salary = salary.trim();
            salary = salary.replaceAll(" ", "");
            if (salary.indexOf("L") > 0 || salary.indexOf("l") > 0) {
                salary = salary.replaceAll("[Ll]", "");
                try {
                    Double aDouble = Double.parseDouble(salary);
                    occupation.setSalary(aDouble * 100000);
                } catch (NumberFormatException nfe) {
                }
            }
        }
        return occupation;
    }


    private static Collection<Education> getEducations(Cell cell) {
        if (null == cell) return Collections.EMPTY_SET;
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
            educations = Collections.EMPTY_SET;
        }
        return educations;
    }

    private static Collection<String> getPhoneNumbers(Cell[] cells) {
        if (cells == null || cells.length == 0) return Collections.EMPTY_LIST;
        Collection<String> phoneNumbers = Stream.of(cells)
                .map(cell -> getValueAsString(cell))
                .filter(s -> s != null && !s.isEmpty())
                .map(s -> s.trim())
                .filter(s -> s.matches("(\\d)+(-)?"))
                .collect(Collectors.toSet());
        return phoneNumbers;
    }

    private static Long getHeightInCms(String heightInFeet) {
        if (heightInFeet == null || heightInFeet.trim().isEmpty()) return null;

        try {
            Double h = Double.parseDouble(heightInFeet);
            h = h * 30;
            return h.longValue();
        } catch (NumberFormatException nfe) {
            return null;
        }

    }

    public static Date getValueAsDate(Cell cell){
        if (cell == null) return null;
        Date dateCellValue = null;
        try {
            dateCellValue = cell.getDateCellValue();
        }catch(IllegalStateException ise){
            dateCellValue = null;
        }
        return dateCellValue;
    }

    private static String getValueAsString(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() != Cell.CELL_TYPE_STRING) {
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }
        String stringCellValue = cell.getStringCellValue();
        return stringCellValue;
    }

    private static boolean areMandatoryFieldsPresent(Row row) {
        Request nameRequest = new Request(row.getCell(ExcelColumns.NAME.asInt()), ExcelColumns.NAME);
        Request genderRequest = new Request(row.getCell(ExcelColumns.GENDER.asInt()), ExcelColumns.GENDER);
        List<Request> mandatoryFields = Arrays.asList(new Request[]{nameRequest, genderRequest});
        List<Response> responseList = mandatoryFields.stream().map(request -> isCellValid(request)).collect(Collectors.toList());
        Optional<Response> any = responseList.parallelStream().filter(response -> response.isValid == false).findAny();
        if (any.isPresent()) {
            final StringBuilder messagebuilder = new StringBuilder();
            messagebuilder.append("Row " + row.getRowNum() + " has following invalid fields : ");
            responseList.stream().filter(response -> response.isValid == false).forEach(response -> {
                messagebuilder.append("<" + response.getFieldType() + ">").append(" ");
            });
            logger.error(messagebuilder.toString());
            return false;
        } else {
            return true;
        }
    }


    private static Response isCellValid(Request request) {
        Response r = new Response(request);
        r.isValid = isCellValid(request.cell);
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


    public static void main(String[] args) {
        if (args == null || args.length != 1) {
            System.out.println("java com.poople.promat.ExcelDataImport <path-to-xls-file>");
            return;
        }
        final String fileName = args[0];
        try {
            long startTime = System.currentTimeMillis();
            ExcelDataImport.importData(fileName);
            long timeTaken = System.currentTimeMillis() - startTime;
            logger.info("Completed in " + (timeTaken / 1000) + "s");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    enum ExcelColumns {
        EXTERNAL_USER_ID(1),
        GENDER(2),
        NAME(3),
        DATE_OF_BIRTH(6),
        TIME_OF_BIRTH(7),
        SKINTONE(20),
        HEIGHT(21),
        BLOODGROUP(22),
        PHONE_1(23),
        PHONE_2(24),
        PHONE_3(25),
        EDUCATION(16),
        OCCUPATION_TITLE(19),
        OCCUPATION_PLACE(17),
        OCCUPATION_SALARY(18),
        NOTE_1(28),
        NOTE_2(29),
        NOTE_3(30),
        NOTE_4(31);

        private int columnIndex;

        ExcelColumns(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        int asInt() {
            return columnIndex;
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

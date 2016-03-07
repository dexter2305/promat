package com.poople.promat.migrate;

import com.poople.promat.models.Candidate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
                    logger.debug((sheetCounter) + "->" + (row.getRowNum()) + " -> " + candidate.getName());
                } else {
                    logger.error("Error while reading row @ " + (sheetCounter + 1) + "/" + (row.getRowNum()));
                }
            }
            report.sheetNumber = sheetCounter + 1;
            report.numberOfRowsRead = sheet.getLastRowNum();
            report.numberOfBeansLoaded = beanCounter;
            reports.add(report);
            sheetCounter++;
        }
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
        return candidates;
    }

    private static Candidate getCandidateBean(Row row) {
        if (row == null) return null;
        if (!areMandatoryFieldsPresent(row)) return null;
        Candidate candidate = new Candidate();
        candidate.setName(getValueAsString(row.getCell(ExcelColumns.NAME.asInt())));
        candidate.setGender(Candidate.Gender.fromString(getValueAsString(row.getCell(ExcelColumns.GENDER.asInt()))));

        return candidate;
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
        boolean arePresent;

        Cell nameCell = row.getCell(ExcelColumns.NAME.asInt());
        Cell genderCell = row.getCell(ExcelColumns.GENDER.asInt());

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

        /*
        if (!isCellValid(nameCell)){
            arePresent = false;
            messagebuilder.append("<NAME>");
        }else{
            arePresent = true;
        }


        if (!isCellValid(genderCell)){
            arePresent = arePresent & false;
            messagebuilder.append("<GENDER>");
        }
        if (!arePresent)
        logger.error("Row " + row.getRowNum() + " has following invalid properties " + messagebuilder.toString());
        return arePresent;
        */
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
            ExcelDataImport.importData(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    enum ExcelColumns {
        GENDER(2),
        NAME(3);
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

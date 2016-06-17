package com.poople.promat.migrate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.IOUtils;

import com.poople.promat.adapters.DTFormatter;
import com.poople.promat.models.DataError;

public class ExcelManager {

	private static final Log logger = LogFactory.getLog(ExcelManager.class);

	protected void shutdown(Workbook wrkBk) throws Exception {
		this.closeWorkBook(wrkBk);
	}

	protected File getFile(String strFile) throws Exception {
		logger.info("ENTER - getFile(String) : " + strFile);
		File file = null;
		if (strFile != null) {
			file = new File(strFile);
		} else {
			throw new Exception("File name is empty");
		}
		logger.info("EXIT - getFile(String)");
		return file;
	}

	protected boolean fileExists(String strFile) {
		logger.info("ENTER - fileExists(String) : " + strFile);
		try {
			File file = getFile(strFile);
			if (file.exists()) {
				return true;
			}
		} catch (Exception e) {

		}
		logger.info("EXIT - fileExists(String) - false");
		return false;
	}

	/**
	 * @param strTemplateFile
	 * @throws IOException
	 */
	protected Workbook openWorkBook(String strFile) throws Exception {
		logger.info("ENTER - openWorkBook(String) : " + strFile);
		Workbook wrkBk = null;
		File file = getFile(strFile);
		if (file.exists()) {
			wrkBk = openWorkBook(file);
		}
		logger.info("EXIT - openWorkBook(String)");
		return wrkBk;
	}

	protected Workbook openWorkBook(File file) throws Exception {
		logger.info("ENTER - openWorkBook(File) : " + file);
		Workbook wrkBk = null;
		try {
			// POIFSFileSystem fs = new POIFSFileSystem(fis);
			wrkBk = WorkbookFactory.create(file);

		} catch (IOException | EncryptedDocumentException | InvalidFormatException e) {
			throw new Exception("Error in opening excel file :" + e.getMessage());
		}
		logger.info("EXIT - openWorkBook(File)");
		return wrkBk;
	}

	protected void closeWorkBook(Workbook wrkBk) throws Exception {
		try {
			wrkBk.close();
		} catch (IOException ioe) {
			throw new Exception(ioe.getMessage() + "Error in closing Excel File");
		}
	}

	protected Sheet[] getExistingSheets(Workbook wrkBk) throws Exception {
		logger.info("ENTER - getExistingSheet()");
		Sheet[] sheets = null;
		int sheetIndex = wrkBk.getNumberOfSheets();
		logger.debug("sheetIndex:" + sheetIndex);
		logger.debug("total sheets:" + wrkBk.getNumberOfSheets());
		if (sheetIndex != -1) {
			sheets = new Sheet[sheetIndex];
			for (int i = 0; i < sheetIndex; i++) {
				sheets[i] = wrkBk.getSheetAt(i);
			}
		} else {
			logger.debug("No sheets found to return");
			throw new Exception("No excel sheets found to fetch email ids");
		}
		logger.info("EXIT - getExistingSheet()");
		return sheets;
	}

	protected CellStyle createCellStyle(Workbook wrkBk, short border, short bgColor) {
		CellStyle cellStyle = wrkBk.createCellStyle();
		cellStyle.setWrapText(true);
		if (border != 0) {
			cellStyle.setBorderBottom(border);
			cellStyle.setBorderTop(border);
			cellStyle.setBorderLeft(border);
			cellStyle.setBorderRight(border);
		}
		if (bgColor != 0) {
			cellStyle.setFillForegroundColor(bgColor);
			cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		}

		return cellStyle;
	}

	protected CellStyle createDateCellStyle(Workbook wrkBk, String dateFormatStr) {
		CellStyle cellStyle = wrkBk.createCellStyle();
		CreationHelper ch = wrkBk.getCreationHelper();
		cellStyle.setDataFormat(ch.createDataFormat().getFormat(dateFormatStr));
		return cellStyle;
	}

	protected CellStyle createCellStyle(Workbook wrkBk, short border, short bgColor, int fontSize, short fontColor,
			short fontBold) {
		CellStyle cellStyle = createCellStyle(wrkBk, border, bgColor);
		if ((fontSize != 0) || (fontColor != 0) || (fontBold != 0)) {

			Font font = wrkBk.createFont();
			if (fontSize != 0) {
				font.setFontHeightInPoints((short) fontSize);
			}
			if (fontColor != 0) {
				font.setColor(fontColor);
			}
			if (fontBold != 0) {
				font.setBoldweight(fontBold);
			}
			cellStyle.setFont(font);
		}
		return cellStyle;
	}

	protected CellStyle createCellStyle(Workbook wrkBk, short border, short bgColor, int fontSize, short fontColor,
			short fontBold, short align) {
		CellStyle cellStyle = createCellStyle(wrkBk, border, bgColor, fontSize, fontColor, fontBold);
		if (align != 0) {
			cellStyle.setAlignment(align);
		}
		return cellStyle;
	}

	protected DataFormat createCellDataFormat(Workbook wrkBk) {
		DataFormat dFormat = wrkBk.createDataFormat();
		return dFormat;
	}

	protected CellStyle setDataFormatStyle(CellStyle cellStyle, short formatIndex) {
		if (cellStyle != null) {
			cellStyle.setDataFormat(formatIndex);

		}
		return cellStyle;
	}

	protected CellStyle setDataFormatStyle(CellStyle cellStyle, DataFormat dFormat, String strFormat) {
		if (cellStyle != null) {
			if ((dFormat != null) && (strFormat != null)) {
				if (strFormat.trim().length() >= 0) {
					cellStyle.setDataFormat(dFormat.getFormat(strFormat.trim()));
				} else {
					cellStyle.setDataFormat(dFormat.getFormat("General"));
				}
			}
		}
		return cellStyle;
	}

	protected void setStringCellValue(Row row, int colNum, String cellValue) {
		// Cell cell = row.getCell(colNum);
		Cell cell = row.createCell(colNum);
		if (cellValue != null) {
			cell.setCellValue(cellValue);
		} else {
			cell.setCellValue("");
		}

	}

	protected void setFormulaCell(Row row, int colNum, String cellFormulaValue, CellStyle csWithDf)
			throws ParseException {
		// Cell cell = row.getCell(colNum);
		// =ROUND(((TODAY()-G4)/365);0)
		Cell cell = row.createCell(colNum);
		if (cellFormulaValue != null) {
			if (csWithDf != null)
				cell.setCellStyle(csWithDf);
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cell.setCellFormula(cellFormulaValue);
		} else {
			cell.setCellValue("");
		}

	}

	protected void retreiveAndSetCells(Sheet sheet, CellReference cf, String cellValue) {

		logger.info("ENTER - retreiveAndSetCells() : " + sheet + " , " + cf + " , " + cellValue + " , " + null);
		if (cf != null) {
			retreiveAndSetCells(sheet, cf.getRow(), cf.getCol(), cellValue, null);
		}
	}

	protected void retreiveAndSetCells(Sheet sheet, int rownum, int colNum, String cellValue) {
		logger.info("ENTER - retreiveAndSetCells() : " + sheet + " , " + rownum + " , " + colNum + " , " + cellValue
				+ " , " + null);
		retreiveAndSetCells(sheet, rownum, colNum, cellValue, null);
	}

	protected void retreiveAndSetCells(Sheet sheet, int rownum, int colNum, String cellValue, CellStyle csWithDf) {
		Cell cell = sheet.getRow(rownum).getCell(colNum);
		if (cellValue != null && !cellValue.equals("null")) {
			if (csWithDf != null)
				cell.setCellStyle(csWithDf);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(cellValue);
		} else {
			cell.setCellValue("");
		}

	}

	protected void setImage(Workbook workbook, Sheet sheet, int rownum, int colNum, String imgFileName) {
		logger.info("ENTER - setImage() : " + sheet + " , " + rownum + " , " + colNum + " , " + imgFileName);
		if (imgFileName != null) {
			InputStream inputStream;
			try {
				inputStream = new FileInputStream(imgFileName);
				byte[] imageBytes = IOUtils.toByteArray(inputStream);
				int pictureureIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);
				inputStream.close();
				CreationHelper helper = workbook.getCreationHelper();
				Drawing drawing = sheet.createDrawingPatriarch();
				ClientAnchor anchor = helper.createClientAnchor();
				anchor.setAnchorType(2);
				anchor.setCol1(colNum);
				anchor.setRow1(rownum);
				anchor.setCol2(colNum + 12);
				anchor.setRow2(rownum + 7);
				drawing.createPicture(anchor, pictureureIdx);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	protected void retreiveAndSetCells(Sheet sheet, int rownum, int colNum, Date cellValue, CellStyle csWithDf) {
		Cell cell = sheet.getRow(rownum).getCell(colNum);
		if (cellValue != null) {
			if (csWithDf != null)
				cell.setCellStyle(csWithDf);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellValue(cellValue);
		} else {
			cell.setCellValue(Cell.CELL_TYPE_BLANK);
		}
	}

	protected void retreiveAndSetCells(Sheet sheet, int rownum, int colNum, Integer cellValue, CellStyle csWithDf) {
		Cell cell = sheet.getRow(rownum).getCell(colNum);
		if (cellValue != null) {
			if (csWithDf != null)
				cell.setCellStyle(csWithDf);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellValue(cellValue);
		} else {
			cell.setCellValue(Cell.CELL_TYPE_BLANK);
		}
	}

	protected void retreiveAndSetCells(Sheet sheet, int rownum, int colNum, Long cellValue, CellStyle csWithDf) {
		Cell cell = sheet.getRow(rownum).getCell(colNum);
		if (cellValue != null) {
			if (csWithDf != null)
				cell.setCellStyle(csWithDf);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellValue(cellValue);
		} else {
			cell.setCellValue(Cell.CELL_TYPE_BLANK);
		}
	}

	protected void writeWorkBookToFile(Workbook wrkBk, String reportName) throws Exception {
		FileOutputStream out = null;

		try {
			out = new FileOutputStream(reportName);
			wrkBk.write(out);
			// System.out.println("Written to a file");
		} catch (FileNotFoundException fnfe) {
			throw new Exception(fnfe.getMessage() + "Error while writing to Excel:" + reportName);
		} catch (IOException ioe) {
			throw new Exception(ioe.getMessage() + "Error while writing to Excel:" + reportName);
		} catch (Exception e) {
			throw new Exception(e.getMessage() + "Error while writing to Excel:" + reportName);
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (IOException ioe) {

				throw new Exception(ioe.getMessage() + "Error while closing IOStream");
			} catch (Exception e) {

				throw new Exception(e.getMessage());
			}
		}

	}

    public String getDateValueAsString(Cell cell, DateTimeFormatter dtf) throws DataError {
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

    public String getValueAsString(Cell cell) {
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

    public Long getValueAsLong(Cell cell) {
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
}

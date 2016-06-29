package com.poople.promat.migrate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;

import com.poople.promat.models.HoroscopeConstants;
import com.poople.promat.models.MatchingProfile;
import com.poople.promat.models.HoroscopeConstants.MatchingStar;
import com.poople.promat.models.HoroscopeConstants.MatchType;
import com.poople.promat.models.HoroscopeConstants.MatchingStars;
import com.poople.promat.models.HoroscopeConstants.Star;
import com.poople.promat.models.HoroscopeConstants.CompareMatchingStars;
import com.poople.promat.models.HoroscopeConstants.CompareStar;

public class MatchingStarsInitializer {
	private String excelPrintTemplateFile;
	private static final Log logger = LogFactory.getLog(MatchingStarsInitializer.class);
	private ExcelManager xlMgr;
	private Workbook wrkBk;
	public Map<Star, MatchingStars> boysMatching = new HashMap<Star, MatchingStars>();
    public Map<Star, MatchingStars> girlsMatching = new HashMap<Star, MatchingStars>();
 

	public MatchingStarsInitializer(String strConfigFileName) throws Exception {
		super();
		init(strConfigFileName);
	}

	private void init(String strConfigFileName) throws Exception {
		logger.info("ENTER - init() :" + strConfigFileName);

		this.excelPrintTemplateFile = strConfigFileName;
		this.xlMgr = new ExcelManager();
		wrkBk = xlMgr.openWorkBook(excelPrintTemplateFile);
		logger.info("EXIT - init()");
	}

	private Map<Star, MatchingStars> readBoyMatchingStars() throws Exception {
		logger.info("ENTER - readBoyMatchingStars()");
		Sheet sheet = wrkBk.getSheet("b2g");
		// read boy to girl matching stars
		Map<Star, MatchingStars> boysMatching = new TreeMap<Star, MatchingStars>(new CompareStar());
		System.out.println("Map<Star, MatchingStars> boysMatching = new HashMap<Star, MatchingStars>();");
		for (int i = 2; i <= 72; i = i + 2) {
			// System.out.println("");
			CellReference rasiCf = new CellReference("A" + String.valueOf(i));
			String rasiCell = getValue(sheet.getRow(rasiCf.getRow()).getCell(rasiCf.getCol()));
			String star = rasiCell.split("\n")[0].trim();
			String rasi = rasiCell.split("\n")[1].trim();
			 //System.out.println("Star :" + star);
			 //System.out.println("Rasi :" + rasi);

			

			CellReference bestCf = new CellReference("B" + String.valueOf(i));
			String bestCell = getValue(sheet.getRow(bestCf.getRow()).getCell(bestCf.getCol()));
			// System.out.println("Best Match type :" + bestCell);
			
			CellReference bestStarsCf = new CellReference("C" + String.valueOf(i));
			String bestStarsCell = getValue(sheet.getRow(bestStarsCf.getRow()).getCell(bestStarsCf.getCol()));
			// System.out.println(bestStarsCell);
			bestStarsCell = bestStarsCell.trim();
			bestStarsCell.replaceAll("[\n\r]", "");
			bestStarsCell.replaceAll(" ", "");
			
			// System.out.println(bestStarsCell.split(",").length);

			CellReference medCf = new CellReference("B" + String.valueOf(i + 1));
			String medCell = getValue(sheet.getRow(medCf.getRow()).getCell(medCf.getCol()));
			// System.out.println("Medium Match type :" + bestCell);
			
			CellReference medStarsCf = new CellReference("C" + String.valueOf(i + 1));
			String medStarsCell = getValue(sheet.getRow(medStarsCf.getRow()).getCell(medStarsCf.getCol()));
			// System.out.println(medStarsCell);
			
			
			// System.out.println(medStarsCell.split(",").length);
			//System.out.println("-----------------------");
			boysMatching.put(Star.valueOf(star), new MatchingStars(Star.valueOf(star), rasi, MatchingStar.getMatchingStars(Integer.parseInt(bestCell), MatchType.BEST, bestStarsCell),MatchingStar.getMatchingStars(Integer.parseInt(medCell), MatchType.MEDIUM, medStarsCell)));
			System.out.println("boysMatching.put(Star."+Star.valueOf(star).name()+",new MatchingStars(Star."+Star.valueOf(star).name()+",\""+rasi+"\",new Match("+bestCell+", MatchType.BEST, \""+bestStarsCell+"\"),new Match("+medCell+", MatchType.MEDIUM, \""+medStarsCell+"\")));");

		}
		logger.info("EXIT - readBoyMatchingStars()");
		return boysMatching;
	}

	private Map<Star, MatchingStars> readGirlMatchingStars() throws Exception {
		logger.info("ENTER - readGirlMatchingStars()");
		Sheet sheet = wrkBk.getSheet("g2b");
		Map<Star, MatchingStars> girlsMatching = new TreeMap<Star, MatchingStars>(new CompareStar());
		System.out.println("Map<Star, MatchingStars> girlsMatching = new HashMap<Star, MatchingStars>();");
		// read girl to boy matching stars
		for (int i = 2; i <= 72; i = i + 2) {
			// System.out.println("");
			CellReference rasiCf = new CellReference("A" + String.valueOf(i));
			String rasiCell = getValue(sheet.getRow(rasiCf.getRow()).getCell(rasiCf.getCol()));
			String star = rasiCell.split("\n")[0].trim();
			String rasi = rasiCell.split("\n")[1].trim();
			 //System.out.println("Star :" + star);
			 //System.out.println("Rasi :" + rasi);

			

			CellReference bestCf = new CellReference("B" + String.valueOf(i));
			String bestCell = getValue(sheet.getRow(bestCf.getRow()).getCell(bestCf.getCol()));
			// System.out.println("Best Match type :" + bestCell);
			
			CellReference bestStarsCf = new CellReference("C" + String.valueOf(i));
			String bestStarsCell = getValue(sheet.getRow(bestStarsCf.getRow()).getCell(bestStarsCf.getCol()));
			// System.out.println(bestStarsCell);
			bestStarsCell = bestStarsCell.trim();
			bestStarsCell.replaceAll("[\n\r]", "");
			bestStarsCell.replaceAll(" ", "");
			
			// System.out.println(bestStarsCell.split(",").length);

			CellReference medCf = new CellReference("B" + String.valueOf(i + 1));
			String medCell = getValue(sheet.getRow(medCf.getRow()).getCell(medCf.getCol()));
			// System.out.println("Medium Match type :" + bestCell);
			
			CellReference medStarsCf = new CellReference("C" + String.valueOf(i + 1));
			String medStarsCell = getValue(sheet.getRow(medStarsCf.getRow()).getCell(medStarsCf.getCol()));
			// System.out.println(medStarsCell);
			
			
			// System.out.println(medStarsCell.split(",").length);
			
			
			girlsMatching.put(Star.valueOf(star), new MatchingStars(Star.valueOf(star), rasi, MatchingStar.getMatchingStars(Integer.parseInt(bestCell), MatchType.BEST, bestStarsCell),MatchingStar.getMatchingStars(Integer.parseInt(medCell), MatchType.MEDIUM, medStarsCell)));
			System.out.println("girlsMatching.put(Star."+Star.valueOf(star).name()+",new MatchingStars(Star."+Star.valueOf(star).name()+",\""+rasi+"\",new Match("+bestCell+", MatchType.BEST, \""+bestStarsCell+"\"),new Match("+medCell+", MatchType.MEDIUM, \""+medStarsCell+"\")));");
		}
		logger.info("EXIT - readGirlMatchingStars()");
		return girlsMatching;
	}

	private String getValue(Cell c) {
		return xlMgr.getValueAsString(c);
	}

	public static void main(String[] args) throws Exception {
		MatchingStarsInitializer.test(args);
	}

	public static void test(String[] args) throws Exception {
		final String fileName = "D:/sandbox/promat2605/promat/src/main/resources/star_matching.xls";
		MatchingStarsInitializer xpw = new MatchingStarsInitializer(fileName);
		
		
		xpw.writeToExcel("b2g.xls", xpw.readBoyMatchingStars());
		xpw.writeToExcel("g2b.xls", xpw.readGirlMatchingStars());
	}
	public void writeToExcel(String outFileName, Map<Star,MatchingStars> resultMap) throws Exception {
		
        System.out.println("writeToExcel - ENTER : Writing output to csv with file name: " +outFileName);
		ExcelManager xl = new ExcelManager();
		Workbook w = xl.createWorkBook();
		
		Sheet s = w.createSheet(outFileName.substring(0, outFileName.indexOf(".")));

		Row row = s.createRow(s.getLastRowNum());
		List<String> header =Arrays.asList("Star" , "Match Type", "Match Strength" , "Matching Stars");
		xl.writeStringArraytoRow(row, header);

		CellReference fCf = new CellReference(row.getRowNum(), row.getFirstCellNum());
		CellReference lCf = new CellReference(row.getRowNum(), row.getLastCellNum());
		
		s.setAutoFilter(CellRangeAddress.valueOf(fCf.formatAsString() + ":" + lCf.formatAsString()));
		resultMap.forEach((star,matchedStar) -> {
			Row rowdata1 = s.createRow(s.getLastRowNum()+1);
			rowdata1.createCell(0).setCellValue(String.valueOf(star));
			rowdata1.createCell(1).setCellValue("BEST");
			rowdata1.createCell(2).setCellValue(matchedStar.getBestMatchStars().iterator().next().getStrength());
			rowdata1.createCell(3).setCellValue(matchedStar.getBestMatchStars().stream().map(bs -> bs.getStar().toWholeString()).sorted().collect(Collectors.joining(", ")));
			
			Row rowdata2 = s.createRow(s.getLastRowNum()+1);
			rowdata2.createCell(0).setCellValue("");
			s.addMergedRegion(new CellRangeAddress(rowdata1.getRowNum(),rowdata2.getRowNum(),0,0));
			rowdata2.createCell(1).setCellValue("MEDIUM");
			rowdata2.createCell(2).setCellValue(matchedStar.getMediumMatchStars().iterator().next().getStrength());
			rowdata2.createCell(3).setCellValue(matchedStar.getMediumMatchStars().stream().map(bs -> bs.getStar().toWholeString()).sorted().collect(Collectors.joining(", ")));
		});
		// Auto size the column widths
		for(int columnIndex = 0; columnIndex < 10; columnIndex++) {
		     s.autoSizeColumn(columnIndex);
		}
		xl.writeWorkBookToFile(w, outFileName);
		xl.closeWorkBook(w);
	}
}

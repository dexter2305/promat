package com.poople.promat.migrate;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;

import com.poople.promat.models.HoroscopeConstants;
import com.poople.promat.models.HoroscopeConstants.MatchingStar;
import com.poople.promat.models.HoroscopeConstants.MatchType;
import com.poople.promat.models.HoroscopeConstants.MatchingStars;
import com.poople.promat.models.HoroscopeConstants.Star;

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
		Map<Star, MatchingStars> boysMatching = new HashMap<Star, MatchingStars>();
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
		Map<Star, MatchingStars> girlsMatching = new HashMap<Star, MatchingStars>();
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
		xpw.readBoyMatchingStars();
		xpw.readGirlMatchingStars();
	}

}

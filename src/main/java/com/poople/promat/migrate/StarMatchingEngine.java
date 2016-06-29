package com.poople.promat.migrate;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.poople.promat.models.Candidate;
import com.poople.promat.models.Candidate.Gender;
import com.poople.promat.models.Horoscope;
import com.poople.promat.models.HoroscopeConstants;
import com.poople.promat.models.HoroscopeConstants.BoysMatching;
import com.poople.promat.models.HoroscopeConstants.CompareProfilesByStars;
import com.poople.promat.models.HoroscopeConstants.GirlsMatching;
import com.poople.promat.models.HoroscopeConstants.MatchType;
import com.poople.promat.models.HoroscopeConstants.MatchingStar;
import com.poople.promat.models.HoroscopeConstants.Star;
import com.poople.promat.models.MatchingProfile;

public class StarMatchingEngine {
	private static final Log logger = LogFactory.getLog(StarMatchingEngine.class);
	private Gender matchForGender = null;
	private String femalesExcelFileName;
	private String malesExcelFileName;
	private String femalesExcelFileSheetNames;
	private String malesExcelFileSheetNames;
	
	Collection<Candidate> maleProfiles = null;
	Collection<Candidate> femaleProfiles = null;

	//private Set<Star> matchForStars = new TreeSet<Star>(new CompareStar());
	private Set<Star> matchForStars = BoysMatching.getInstance().keySet();
	
	private static String outFileName;
	private String importConfig;
	public StarMatchingEngine(String strConfigFileName) throws Exception {
		super();
		init(strConfigFileName);
	}

	private void init(String strConfigFileName) throws Exception {
		logger.info("ENTER - init() :" + strConfigFileName);
		//matchForStars.addAll(Arrays.asList(Star.ASVINI,Star.BARANI,Star.KARTHIGAI_1,Star.KARTHIGAI_234,Star.ROHINI,Star.MIRUGASEERIDAM_12,Star.MIRUGASEERIDAM_34,Star.THIRUVATHIRAI,Star.PUNARPOOSAM_123,Star.PUNARPOOSAM_4,Star.POOSAM,Star.AYILYAM,Star.MAGAM,Star.POORAM,Star.UTHRAM_1,Star.UTHRAM_234,Star.ASTHAM,Star.CHITHRAI_12,Star.CHITHRAI_34,Star.SWATHI ,Star.VISAGAM_123,Star.VISAGAM_4,Star.ANUSHAM,Star.KETTAI,Star.MOOLAM ,Star.POORADAM,Star.UTHRADAM_1,Star.UTHRADAM_234,Star.THIRUVONAM ,Star.AVITTAM_12,Star.AVITTAM_34,Star.SATHAYAM ,Star.POORATTATHI_123,Star.POORATTATHI_4,Star.UTHRATTATHI,Star.REVATHI));
		
		Properties configProps = this.load(strConfigFileName);
		this.femalesExcelFileName = configProps.getProperty("females.excel.profile");
		this.malesExcelFileName = configProps.getProperty("males.excel.profile");
		this.malesExcelFileSheetNames = configProps.getProperty("males.excel.profile.sheet.names");
		this.femalesExcelFileSheetNames = configProps.getProperty("females.excel.profile.sheet.names");
		this.importConfig = configProps.getProperty("profile.import.config.file");
		Properties importConfigProps = new Properties();
		importConfigProps.load(new FileInputStream(importConfig));
		this.maleProfiles = importCandidates(importConfigProps, malesExcelFileName, Arrays.asList((malesExcelFileSheetNames.split(","))));
		this.femaleProfiles = importCandidates(importConfigProps, femalesExcelFileName, Arrays.asList((femalesExcelFileSheetNames.split(","))));
		logger.info("EXIT - init()");
	}
	private Collection<Candidate> importCandidates(Properties importConfigProps, String excelFileName, List<String> excelFileSheetNames) throws Exception {
		long startTime = System.currentTimeMillis();
		Collection<Candidate> cList = ExcelDataImport.importData(importConfigProps, excelFileName, excelFileSheetNames);
		long timeTaken = System.currentTimeMillis() - startTime;
		logger.info("Importing "+ malesExcelFileName + " profile completed in " + (timeTaken / 1000) + "s");
		return cList;
	}

	public static void main(String[] args) throws Exception {
		StarMatchingEngine.test(args);
	}
	public static void profileMatcher(String[] args) throws Exception {

		if (args == null || args.length != 1) {
			System.out.println("java com.poople.promat.migrate.StarMatchingEngine <path-to-match-conf-file>");
			return;
		}
		final String confFile = args[0];
		StarMatchingEngine me = new StarMatchingEngine(confFile);
		
		Map<Star, Set<MatchingProfile>> boysResultMap = me.matchAllProfiles(Gender.MALE);
		outFileName = "MatchResultsForBoysStar_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyhhmm")) + ".xls";
		me.writeToExcel(outFileName, boysResultMap);
		
		Map<Star, Set<MatchingProfile>> girlsResultMap = me.matchAllProfiles(Gender.FEMALE);
		outFileName = "MatchResultsForGirlsStar_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyhhmm")) + ".xls";
		me.writeToExcel(outFileName, girlsResultMap);
	}
	public void writeToExcel(String outFileName, Map<Star, Set<MatchingProfile>> resultMap) throws Exception {
		
        System.out.println("writeToExcel - ENTER : Writing output to csv with file name: " +outFileName);
		ExcelManager xl = new ExcelManager();
		Workbook w = xl.createWorkBook();
		
		Sheet s = w.createSheet("results");

		Row row = s.createRow(s.getLastRowNum());
		List<String> header = MatchingProfile.getListFormat();
		header.remove(0);
		header.remove(0);
		xl.writeStringArraytoRow(row, header);

		CellReference fCf = new CellReference(row.getRowNum(), row.getFirstCellNum());
		CellReference lCf = new CellReference(row.getRowNum(), row.getLastCellNum());
		
		s.setAutoFilter(CellRangeAddress.valueOf(fCf.formatAsString() + ":" + lCf.formatAsString()));
		resultMap.forEach((star,matchedSet) -> {
			matchedSet.forEach(m -> {
				Row r = s.createRow(s.getLastRowNum()+1);
				List<String> data = m.toList();
				data.remove(0);
				data.remove(0);
				xl.writeStringArraytoRow(r, data);
			});
			//s.setRowBreak(s.getLastRowNum());
		});
		// Auto size the column widths
		for(int columnIndex = 0; columnIndex < 10; columnIndex++) {
		     s.autoSizeColumn(columnIndex);
		}
		xl.writeWorkBookToFile(w, outFileName);
		xl.closeWorkBook(w);
	}
	public Map<Star, Set<MatchingProfile>> matchAllProfiles(Gender g) throws IOException{
		long startTime = System.currentTimeMillis();
		Map<Star, Set<MatchingProfile>> matchResults = new TreeMap<Star, Set<MatchingProfile>>();
		matchForGender = g;
		logger.info("Stars to be matched for Gender: "+g);	
		matchForStars.forEach(star -> {			
			matchResults.put(star, matchProfile(star));});
		long timeTaken = System.currentTimeMillis() - startTime;
		logger.info("Match completed in " + (timeTaken / 1000) + "s");

		return matchResults;

	}
	private Set<MatchingProfile> matchProfile(Star star){
		
		Set<MatchingProfile> starMatches = new TreeSet<MatchingProfile>(new CompareProfilesByStars());
		logger.info("Star "+ star +" to be matched : "+star);	
		Collection<Candidate> profiles = null;
		if (matchForGender == Gender.MALE) {
			profiles = femaleProfiles;
		} else if (	matchForGender == Gender.FEMALE) {
			profiles = maleProfiles;
		}
		profiles.stream().forEach((matchAgainstCandidate) -> {
			MatchingProfile matchResult = match(star,matchAgainstCandidate);
			if(matchResult != null) {
				logger.debug("Found star " + star + " MATCH to id " + matchResult.getProfile().getId());
				starMatches.add(matchResult);
			}
			else {
				//logger.info("No MATCH profile with Id :"+ matchAgainstCandidate.getId());
			}
		});
		System.out.println(MatchingProfile.getListFormat().stream().collect(Collectors.joining("|")));
		starMatches.forEach(r->System.out.println(r));
		
		
		
		logger.info("matchProfile(long userId) - EXIT : Match completed for star " + star + ". # of matches found." +starMatches.size());
		return starMatches;

	}
	
	private MatchingProfile match(Star mfs, Candidate matchAgainstCandidate){
		logger.debug("match(Candidate matchForCandidate, Candidate matchAgainstCandidate) - ENTER :" +matchAgainstCandidate.getId());
		//MatchingProfile result = null;
		int matStrength = 0;
		MatchType matScore = null;
		
		Horoscope maHoro = matchAgainstCandidate.getHoroscope();
		HoroscopeConstants.Star mas = null;
		if( maHoro !=null) {
			mas = maHoro.getStar();
		}
		if( mfs !=null && mas !=null) {					
			logger.info(mfs + " vs " + mas);
			MatchingStar matchResult = null;
			if(this.matchForGender == Gender.MALE) {
				matchResult = BoysMatching.getInstance().get(mfs).getMatchingStar(mas);
			}else {
				matchResult = GirlsMatching.getInstance().get(mfs).getMatchingStar(mas);
			}
			//BoysMatching.getInstance().get(mfs).getMatchingStars().stream().forEach(star -> {System.out.println(mfs + " matches :" + star.getStar());});
			if (matchResult != null){
				matStrength = matchResult.getStrength();
				matScore = matchResult.getMatch();
			}  else {
				return null;
			}
		}
		else {
			return null;
		}
	
		return new MatchingProfile(null, null, mfs, matStrength, matScore, matchAgainstCandidate);
	}
	private boolean evaluateCondition(Integer mfAttr , Integer maAttr, String operator){
		boolean result = false;
		switch(operator) {
		case ">":
			return (mfAttr > maAttr);
		case "<":
			return (mfAttr < maAttr);
		case ">=":
			return (mfAttr >= maAttr);
		case "<=":
			return (mfAttr <= maAttr);
		case "=":
		case "==":
			return (mfAttr == maAttr);
			
		}
		return result ;
		
	}
	public static void test(String[] args) throws Exception {
		final String confFile = "D:/sandbox/promat2605/promat/src/main/resources/matching.conf";
		String[] newArgs = {confFile};
		StarMatchingEngine.profileMatcher(newArgs);
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

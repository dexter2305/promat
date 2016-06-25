package com.poople.promat.migrate;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.util.CellReference;

import com.poople.promat.models.Candidate;
import com.poople.promat.models.Candidate.Gender;
import com.poople.promat.models.Dob;
import com.poople.promat.models.Horoscope;
import com.poople.promat.models.HoroscopeConstants;
import com.poople.promat.models.HoroscopeConstants.BoysMatching;
import com.poople.promat.models.HoroscopeConstants.GirlsMatching;
import com.poople.promat.models.HoroscopeConstants.MatchType;
import com.poople.promat.models.HoroscopeConstants.MatchingStar;
import com.poople.promat.models.MatchingProfile;
import com.poople.promat.models.Physique;

public class MatchingEngine {
	private static final Log logger = LogFactory.getLog(ExcelProfileWriter.class);
	private String femalesExcelFileName;
	private String malesExcelFileName;
	private CellReference userIdCol;
	private CellReference nameCol;
	private CellReference htCol;
	private CellReference kulamCol;
	private CellReference starCol;
	private CellReference padhamCol;
	private CellReference dobCol;
	private CellReference timeCol;
	private CellReference ageCol;
	private CellReference birthplaceCol;
	private CellReference rasiCol;
	private CellReference rahukethuCol;
	private CellReference sevvaiCol;
	private CellReference maritalStatusCol;
	private String femalesExcelFileSheetNames;
	private String malesExcelFileSheetNames;
	
	Collection<Candidate> profilesToMatchFor = null;
	Collection<Candidate> profilesToMatchAgainst = null;

	private List<Long> matchForIds;
	private boolean matchStar;
	private boolean matchRk;
	private boolean matchSev;
	private boolean doNotMatchSameKulam;
	private String FemaleAgeCondForMale;
	private String MaleAgeCondForFemale;
	private String FemaleHtCondForMale;
	private String MaleHtCondForFemale;
	private Gender matchForGender;
	private static List<String> cellRefs = Arrays.asList("userid", "sex", "name", "star", "age", "dob",
			"time", "birthplace", "kulam", "padham", "rasi", "lagnam", "rk", "sev", "ht", "marital.status");
	public MatchingEngine(String strConfigFileName) throws Exception {
		super();
		init(strConfigFileName);
	}

	private void init(String strConfigFileName) throws Exception {
		logger.info("ENTER - init() :" + strConfigFileName);

		Properties configProps = this.load(strConfigFileName);
		this.femalesExcelFileName = configProps.getProperty("females.excel.profile");
		this.malesExcelFileName = configProps.getProperty("males.excel.profile");
		this.malesExcelFileSheetNames = configProps.getProperty("males.excel.profile.sheet.names");
		this.femalesExcelFileSheetNames = configProps.getProperty("females.excel.profile.sheet.names");
		// Validate if all required cell references are present in input config
		List<String> notPresentList = cellRefs.stream().filter((propName) -> (!configProps.containsKey(propName)))
				.collect(Collectors.toList());
		if (notPresentList != null && notPresentList.size() > 0) {
			throw new Exception("One or more config values are not persent for following properties : "
					+ notPresentList.toString());
		}

		

		this.matchForGender = Gender.valueOf(configProps.getProperty("match.gender"));
		if(matchForGender == Gender.MALE) {
			profilesToMatchFor = importCandidates(malesExcelFileName, Arrays.asList((malesExcelFileSheetNames.split(","))));
			profilesToMatchAgainst = importCandidates(femalesExcelFileName, Arrays.asList((femalesExcelFileSheetNames.split(","))));
		}else {
			profilesToMatchFor = importCandidates(femalesExcelFileName, Arrays.asList((femalesExcelFileSheetNames.split(","))));
			profilesToMatchAgainst = importCandidates(malesExcelFileName, Arrays.asList((malesExcelFileSheetNames.split(","))));
		}
		String strIds = configProps.getProperty("find.match.for.ids");
		this.matchForIds = Arrays.asList((strIds.split(","))).stream().map(Long::parseLong).collect(Collectors.toList());
		this.matchStar = Boolean.valueOf(configProps.getProperty("match.star", "true"));
		this.matchRk = Boolean.valueOf(configProps.getProperty("match.rk", "false"));
		this.matchSev = Boolean.valueOf(configProps.getProperty("match.sev", "false"));
		this.doNotMatchSameKulam = Boolean.valueOf(configProps.getProperty("do.not.match.same.kulam", "true"));
		this.FemaleAgeCondForMale = configProps.getProperty("females.age.condition.for.males","");
		this.FemaleHtCondForMale = configProps.getProperty("females.ht.condition.for.males","");
		this.MaleAgeCondForFemale = configProps.getProperty("males.age.condition.for.females","");
		this.MaleHtCondForFemale = configProps.getProperty("males.ht.condition.for.females","");
				
		logger.info("EXIT - init()");

	}


	private Collection<Candidate> importCandidates(String excelFileName, List<String> excelFileSheetNames) throws IOException {
		long startTime = System.currentTimeMillis();
		Collection<Candidate> cList = ExcelDataImport.importData(excelFileName, excelFileSheetNames);
		long timeTaken = System.currentTimeMillis() - startTime;
		logger.info("Importing "+ malesExcelFileName + " profile completed in " + (timeTaken / 1000) + "s");
		return cList;
	}

	public static void main(String[] args) throws Exception {
		MatchingEngine.test(args);
	}
	public static void profileMatcher(String[] args) throws Exception {

		 if (args == null || args.length != 1) {
			 System.out.println("java com.poople.promat.migrate.MatchingEngine <path-to-match-conf-file>");
			 return;
		 }
		 final String confFile = args[0];
		 MatchingEngine me = new MatchingEngine(confFile);
		 me.matchAllProfiles();
	}
	public Map<Long, Set<MatchingProfile>> matchAllProfiles(){
		long startTime = System.currentTimeMillis();
		Map<Long, Set<MatchingProfile>> matchResults = new HashMap<Long, Set<MatchingProfile>>();
		
		matchForIds.forEach(id -> matchResults.put(id, matchProfile(id)));
		long timeTaken = System.currentTimeMillis() - startTime;
		logger.info("Match completed in " + (timeTaken / 1000) + "s");
		
		return matchResults;

	}
	public Set<MatchingProfile> matchProfile(long id){
		
		//check if id is valid
		Optional<Candidate> profile = profilesToMatchFor.stream().filter((cand) -> (cand.getId() == id)).findFirst();

		Set<MatchingProfile> starMatches = new HashSet<MatchingProfile>();
		logger.info("Profile "+ id+" found status : "+profile.isPresent());
		
		if(profile.isPresent()) {
			Candidate matchForCandidate = profile.get();
			String name = matchForCandidate.getName();
			profilesToMatchAgainst.stream().forEach((matchAgainstCandidate) -> {
				MatchingProfile matchResult = match(matchForCandidate,matchAgainstCandidate);
				if(matchResult != null) {
					logger.info("Found MATCH for profile with Id :"+ matchAgainstCandidate.getId() + " with star "+ matchResult.getStar());
					starMatches.add(matchResult);
				}
				else {
					//logger.info("No MATCH profile with Id :"+ matchAgainstCandidate.getId());
				}
			});
			System.out.println(matchForCandidate);
			System.out.println("Id,Name,MatchingId,MatchingName,Kulam,Star,Strength,MatchType,Dob,Tob,Age,BirthPlace,RahuKethu,Sevvai,Height,MaritalStatus,Company,WorkPlace");
			starMatches.forEach(r->System.out.println(r));
			
		}else{
			starMatches.add(new MatchingProfile(id,"","Id "+ id +"not found in excel"));
		}
		
		logger.info("matchProfile(long userId) - EXIT : Match completed for id " + id + ". # of matches found." +starMatches.size());
		return starMatches;

	}
	
	//private static final BiFunction<Candidate, Candidate, MatchingStar> matches = (mf, ma) -> {
	private MatchingProfile match(Candidate matchForCandidate, Candidate matchAgainstCandidate){
		logger.debug("match(Candidate matchForCandidate, Candidate matchAgainstCandidate) - ENTER :" +matchAgainstCandidate.getId());
		//MatchingProfile result = null;
		int matStrength = 0;
		MatchType matScore = null;
		Horoscope mfHoro = matchForCandidate.getHoroscope();
		Horoscope maHoro = matchAgainstCandidate.getHoroscope();
		HoroscopeConstants.Star mfs = null;
		HoroscopeConstants.Star mas = null;
		String mfRk = "";
		String maRk = "";
		String mfSev = "";
		String maSev = "";
		if( mfHoro !=null) {
			mfs = mfHoro.getStar();
			mfRk = mfHoro.getRaahu_kethu();
			mfSev = mfHoro.getSevvai();
		}
		if( maHoro !=null) {
			mas = maHoro.getStar();
			maRk = maHoro.getRaahu_kethu();
			maSev = maHoro.getSevvai();
		}
		String mfKulam = matchForCandidate.getKulam();
		String mfStatus = matchForCandidate.getMaritalStatus();
		Dob mfDob = matchForCandidate.getDob();
		Integer mfAge = null;
		if(mfDob != null) {
			mfAge = mfDob.age();
		}
		Physique mfPhy = matchForCandidate.getPhysique();
		Long mfHt = null;
		if(mfPhy != null ) {
			mfHt = mfPhy.getHeight();
		}
		String maKulam = matchAgainstCandidate.getKulam();
		String maStatus = matchAgainstCandidate.getMaritalStatus();
		Dob maDob = matchAgainstCandidate.getDob();
		Integer maAge = null;
		if(maDob != null) {
			maAge = maDob.age();
		}
		Physique maPhy = matchAgainstCandidate.getPhysique();
		Long maHt = null;
		if(maPhy != null ) {
			maHt = maPhy.getHeight();
		}
		if(doNotMatchSameKulam) {			
			logger.debug(mfKulam + " vs " + maKulam);
			if(mfKulam != null && !mfKulam.isEmpty() && maKulam != null && !mfKulam.isEmpty()){				
				if(maKulam.equalsIgnoreCase(mfKulam)){
					return null;
				}
			}
		}
		if(this.matchForGender == Gender.MALE) {
			if(mfAge != null && mfAge !=0 && maAge!=null && maAge != 0) {
				logger.debug(mfAge + " vs " + maAge);
				if(MaleAgeCondForFemale != "") {
					if(!evaluateCondition(mfAge, maAge, MaleAgeCondForFemale)) {
						return null;
					} else {
						logger.debug(mfAge + " vs " + maAge);
					}
					
				}
			}
			if(mfHt != null && mfHt !=0 && maHt!=null && maHt != 0) {
				logger.debug(mfHt + " vs " + maHt);
				if(MaleHtCondForFemale != "") {
					if(!evaluateCondition(mfHt.intValue(), maHt.intValue(), MaleHtCondForFemale)) {
						return null;
					}
					
				}
			}
		}else {
			if(mfAge != null && mfAge !=0 && maAge!=null && maAge != 0) {
				logger.debug(mfAge + " vs " + maAge);
				if(FemaleAgeCondForMale != "") {
					if(!evaluateCondition(mfAge, maAge, FemaleAgeCondForMale)) {
						return null;
					}
					
				}
			}
			if(mfHt != null && mfHt !=0 && maHt!=null && maHt != 0) {
				logger.debug(mfHt + " vs " + maHt);
				if(FemaleHtCondForMale != "") {
					if(!evaluateCondition(mfHt.intValue(), maHt.intValue(), FemaleHtCondForMale)) {
						return null;
					}
					
				}
			}
		}
		
		if(matchStar) {	
			if( mfs !=null && mas !=null) {					
				logger.debug(mfs + " vs " + mas);
				MatchingStar matchResult = null;
				if(this.matchForGender == Gender.MALE) {
					matchResult = BoysMatching.getInstance().get(mfs).getMatchingStar(mas);
				}else {
					matchResult = GirlsMatching.getInstance().get(mfs).getMatchingStar(mas);
				}
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
		}
		if(matchRk) {			
			logger.debug(mfRk + " vs " + maRk);
			if(mfRk != null && !mfRk.isEmpty() && maRk != null && !maRk.isEmpty()){	
				if(!mfRk.equalsIgnoreCase("Nil")&& maRk.equalsIgnoreCase("Nil")) {
					return null;
				}
				if(mfRk.equalsIgnoreCase("Nil")&& !maRk.equalsIgnoreCase("Nil")) {
					return null;
				}
			}
		}
		if(matchSev) {
			logger.debug(mfSev + " vs " + maSev);
			if(mfSev != null && !mfSev.isEmpty() && maSev != null && !maSev.isEmpty()){	
				if(!mfSev.equalsIgnoreCase("Nil")&& maSev.equalsIgnoreCase("Nil")) {
					return null;
				}
				if(mfSev.equalsIgnoreCase("Nil")&& !maSev.equalsIgnoreCase("Nil")) {
					return null;
				}
			}
		}
		
		return new MatchingProfile(matchForCandidate.getId(), matchForCandidate.getName(), mas, matStrength, matScore, matchAgainstCandidate);
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
		MatchingEngine.profileMatcher(newArgs);
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

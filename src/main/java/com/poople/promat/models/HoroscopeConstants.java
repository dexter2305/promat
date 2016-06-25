package com.poople.promat.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface HoroscopeConstants {

	enum Star { 
		ASVINI("ASVINI"),
		BARANI("BARANI"),
		KARTHIGAI("KARTHIGAI"),
		KARTHIGAI_1("KARTHIGAI",new int[]{1}),
		KARTHIGAI_234("KARTHIGAI",new int[]{2,3,4}),
		ROHINI("ROHINI"),
		MIRUGASEERIDAM("MIRUGASEERIDAM"),
		MIRUGASEERIDAM_12("MIRUGASEERIDAM",new int[]{1,2}),
		MIRUGASEERIDAM_34("MIRUGASEERIDAM",new int[]{3,4}),
		THIRUVATHIRAI("THIRUVATHIRAI"),
		PUNARPOOSAM("PUNARPOOSAM"),
		PUNARPOOSAM_23("PUNARPOOSAM",new int[]{2,3}),
		PUNARPOOSAM_123("PUNARPOOSAM",new int[]{1,2,3}),
		PUNARPOOSAM_4("PUNARPOOSAM",new int[]{4}),
		POOSAM("POOSAM"),
		AYILYAM("AYILYAM"),
		MAGAM("MAGAM"),
		POORAM("POORAM"),
		UTHRAM("UTHRAM"),
		UTHRAM_1("UTHRAM",new int[]{1}),
		UTHRAM_234("UTHRAM",new int[]{2,3,4}),
		UTHRAM_34("UTHRAM",new int[]{3,4}),
		ASTHAM("ASTHAM"),
		CHITHRAI("CHITHRAI"),
		CHITHRAI_12("CHITHRAI",new int[]{1,2}),
		CHITHRAI_34("CHITHRAI",new int[]{3,4}),
		SWATHI("SWATHI"),
		VISAGAM("VISAGAM"),
		VISAGAM_123("VISAGAM",new int[]{1,2,3}),
		VISAGAM_4("VISAGAM",new int[]{4}),
		ANUSHAM("ANUSHAM"),
		KETTAI("KETTAI"),
		MOOLAM("MOOLAM"),
		POORADAM("POORADAM"),
		UTHRADAM("UTHRADAM"),
		UTHRADAM_1("UTHRADAM",new int[]{1}),
		UTHRADAM_234("UTHRADAM",new int[]{2,3,4}),
		THIRUVONAM("THIRUVONAM"),
		AVITTAM("AVITTAM"),
		AVITTAM_12("AVITTAM",new int[]{1,2}),
		AVITTAM_34("AVITTAM",new int[]{3,4}),
		SATHAYAM("SATHAYAM"),
		POORATTATHI("POORATTATHI"),
		POORATTATHI_123("POORATTATHI",new int[]{1,2,3}),
		POORATTATHI_4("POORATTATHI",new int[]{4}),
		UTHRATTATHI("UTHRATTATHI"),
		REVATHI("REVATHI");
		private String name;
		private int[] padham = new int[]{1,2,3,4};
		private String padhamAsString = "1234";
		Star(String name) {
			this.name = name;
		}
		Star(String name, int[] padham) {
			this.name = name;
			this.padham = padham;
			this.padhamAsString = Stream.of(padham).map(p -> String.valueOf(p)).reduce((p,n) -> (p + "" +n)).get();
		}

		public int[] getPadham() {
			return padham;
		}
		public String getPadhamAsString() {
			return padhamAsString;
		}
		@Override 
		public String toString() {
			//only capitalize the first letter
			String s = super.toString();
			//return name.substring(0, 1) + name.substring(1).toLowerCase();
			return s.substring(0, 1) + s.substring(1).toLowerCase();

		}
		public static Star fromString(String s) throws DataError {
			if (s == null || s.isEmpty()) return null;
			Star star;
			try {
				s = s.toUpperCase().trim();
				star = Star.valueOf(s);
			} catch (IllegalArgumentException ie) {
				star = null;
				throw new DataError("Invalid Star :"+ s);
			}
			return star;
		}
	}


	public enum Planet { 
		SURIYAN,
		CHANDRAN,
		SEVVAI,
		BUDHAN,
		GURU,
		SUKRAN,
		SANI,
		RAHU,
		KETHU;
		@Override 
		public String toString() {
			//only capitalize the first letter
			String s = super.toString();
			return s.substring(0, 1) + s.substring(1).toLowerCase();
		}

		public static Planet fromString(String s) throws DataError {
			if (s == null || s.isEmpty()) return null;
			Planet planet;
			try {
				s = s.toUpperCase().trim();
				planet = Planet.valueOf(s);
			} catch (IllegalArgumentException ie) {
				planet = null;
				throw new DataError("Invalid Planet :"+ s);
			}
			return planet;
		}
	}    
	enum Raasi { 
		MAESHAM,
		RISHABAM,
		MITHUNAM,
		KADAGAM,
		SIMHAM,
		KANNI,
		THULAM,
		VIRUCHIGAM,
		DANUS,
		MAHARAM,
		KUMBAM,
		MEENAM;

		@Override 
		public String toString() {
			//only capitalize the first letter
			String s = super.toString();
			return s.substring(0, 1) + s.substring(1).toLowerCase();
		}

		public static Raasi fromString(String s) throws DataError {
			if (s == null || s.isEmpty()) return null;
			Raasi raasi;
			try {
				s = s.toUpperCase().trim();
				raasi = Raasi.valueOf(s);
			} catch (IllegalArgumentException ie) {
				raasi = null;
				throw new DataError("Invalid Raasi :"+ s);
			}
			return raasi;
		}
	}

	//not using this enum for now
	enum RaahuKethu { 
		NIL,
		YES,
		CRK;
		@Override 
		public String toString() {
			//only capitalize the first letter
			String s = super.toString();
			return s.substring(0, 1) + s.substring(1).toLowerCase();
		}

		public static RaahuKethu fromString(String s) throws DataError {
			if (s == null || s.isEmpty()) return null;
			RaahuKethu rk;
			try {
				s = s.toUpperCase().trim();
				rk = RaahuKethu.valueOf(s);
			} catch (IllegalArgumentException ie) {
				rk = null;
				throw new DataError("Invalid RaahuKethu :"+ s);
			}
			return rk;
		}
	}
	//not using this enum for now
	enum Sevvai { 
		TWO(2),
		FOUR(4),
		SEVEN(7),
		EIGHT(8),
		TWELVE(12);
		private int housePosition;
		private Sevvai(int housePosition) {
			this.housePosition = housePosition;
		}

		public int getHousePosition() {
			return housePosition;
		}
		@Override 
		public String toString() {
			//only capitalize the first letter
			String s = super.toString();
			return s.substring(0, 1) + s.substring(1).toLowerCase();
		}
		public static Sevvai fromString(String s) throws DataError {
			if (s == null || s.isEmpty()) return null;
			Sevvai sevvai;
			try {
				s = s.toUpperCase().trim();
				sevvai = Sevvai.valueOf(s);
			} catch (IllegalArgumentException ie) {
				sevvai = null;
				throw new DataError("Invalid Sevvai :"+ s);
			}
			return sevvai;
		}
	}
	public class MatchingStars {
		HashSet<MatchingStar> matchStars;
		Star star;
		String rasi;
		public MatchingStars(Star star, String rasi, HashSet<MatchingStar> bestMatchStars, HashSet<MatchingStar> mediumMatchStars) {
			super();
			this.star = star;
			this.rasi = rasi;
			this.matchStars = bestMatchStars;
			matchStars.addAll(mediumMatchStars);
		}
		public Set<MatchingStar> getBestMatchStars() {
			 return matchStars.stream().filter(ms -> (ms.getMatch()==MatchType.BEST)).collect(Collectors.toSet());
		}
		public Set<MatchingStar> getMediumMatchStars() {
			return matchStars.stream().filter(ms -> (ms.getMatch()==MatchType.MEDIUM)).collect(Collectors.toSet());
		}
		public Set<MatchingStar> getMatchingStars() {
			return matchStars;
		}
		public MatchingStar getMatchingStar(Star inputStar) {
			Optional<MatchingStar> matching = matchStars.stream().filter(ms -> {
				if(ms.getStar().name.equals(inputStar.name)) {
					if(ms.getStar().getPadhamAsString().contains(inputStar.getPadhamAsString())) {
						return true;
					}
				}
				return false;
				}).findFirst();
			
			return (matching.isPresent())?matching.get():null;
		}
	}
	enum MatchType { 
		BEST,
		MEDIUM;
		@Override 
		public String toString() {
			return super.toString();
		}
	}
	public class MatchingStar {
		private static final long serialVersionUID = 1L;
		int strength;
		MatchType match = MatchType.BEST;
		Star star ;
		
		public MatchingStar(int strength, MatchType match, Star star) {
			super();
			this.strength = strength;
			this.match = match;
			this.star = star;
		}

		public static HashSet<MatchingStar> getMatchingStars(int strength, MatchType match, String stars) {
			HashSet<MatchingStar> starSet = new HashSet<MatchingStar>();
			
			stars = stars.trim();
			stars.replaceAll("[\n\r]", "");
			stars.replaceAll(" ", "");
			for (String s : stars.split(",")) {
				// System.out.println("mat star:"+s.trim());
				try {
					starSet.add(new MatchingStar(strength, match, Star.valueOf(s.trim())));
					
				} catch (IllegalArgumentException e) {
					System.out.println("FIXME :" + s);
					throw e;
				}
			}
			return starSet;
		}

		public int getStrength() {
			return strength;
		}

		public void setStrength(int strength) {
			this.strength = strength;
		}

		public MatchType getMatch() {
			return match;
		}

		public void setMatch(MatchType match) {
			this.match = match;
		}

		public Star getStar() {
			return star;
		}

		public void setStar(Star star) {
			this.star = star;
		}

		@Override
		public boolean equals(Object arg0) {
			return getStar().equals(((MatchingStar) arg0).getStar());
		}

		@Override
		public int hashCode() {
			return getStar().hashCode();
		}

	}
	@SuppressWarnings({ "serial", "unchecked" })
	public static Set<MatchingStar> makeSet( HashSet<MatchingStar>... matchingStarsSetList) {
        return new HashSet<MatchingStar>() {            
            {
                for (HashSet<MatchingStar> matchingStarsSet : matchingStarsSetList)
                   addAll(matchingStarsSet);
            }
        };
}
	public static final class BoysMatching extends HashMap<Star, MatchingStars>{
		private static final long serialVersionUID = 1L;
		private BoysMatching() {		
			this.put(Star.ASVINI,new MatchingStars(Star.ASVINI,"Mesha",MatchingStar.getMatchingStars(12, MatchType.BEST, "BARANI,KARTHIGAI,MIRUGASEERIDAM,PUNARPOOSAM,POOSAM,POORAM,UTHRAM,CHITHRAI,VISAGAM,UTHRADAM_234,AVITTAM,POORATTATHI_123"),MatchingStar.getMatchingStars(7, MatchType.MEDIUM, "ROHINI,THIRUVATHIRAI,ANUSHAM,POORADAM,UTHRADAM_1,SATHAYAM,AVITTAM_34 ")));
			this.put(Star.BARANI,new MatchingStars(Star.BARANI,"Mesha",MatchingStar.getMatchingStars(10, MatchType.BEST, "ASVINI,KARTHIGAI,ROHINI,THIRUVATHIRAI,MAGAM,UTHRAM,ASTHAM,SWATHI,UTHRADAM_234,THIRUVONAM"),MatchingStar.getMatchingStars(7, MatchType.MEDIUM, "MIRUGASEERIDAM,PUNARPOOSAM,AYILYAM,CHITHRAI,VISAGAM,KETTAI,SATHAYAM")));
			this.put(Star.KARTHIGAI_1,new MatchingStars(Star.KARTHIGAI_1,"Mesha ",MatchingStar.getMatchingStars(10, MatchType.BEST, "BARANI,ROHINI,MIRUGASEERIDAM,AYILYAM,POORAM,ASTHAM,CHITHRAI,KETTAI,AVITTAM,REVATHI"),MatchingStar.getMatchingStars(4, MatchType.MEDIUM, "ASVINI,THIRUVATHIRAI,POOSAM,MAGAM,SWATHI,ANUSHAM")));
			this.put(Star.KARTHIGAI_234,new MatchingStars(Star.KARTHIGAI_234,"Rishaba",MatchingStar.getMatchingStars(10, MatchType.BEST, "ROHINI,MIRUGASEERIDAM,AYILYAM,POORAM,ASTHAM,CHITHRAI,KETTAI,AVITTAM_34,REVATHI"),MatchingStar.getMatchingStars(6, MatchType.MEDIUM, "THIRUVATHIRAI,POOSAM,MAGAM,SWATHI,ANUSHAM,MOOLAM,UTHRATTATHI")));
			this.put(Star.ROHINI,new MatchingStars(Star.ROHINI,"Rishaba",MatchingStar.getMatchingStars(6, MatchType.BEST, "MIRUGASEERIDAM,POOSAM,UTHRAM,CHITHRAI,ANUSHAM,UTHRATTATHI"),MatchingStar.getMatchingStars(8, MatchType.MEDIUM, "ASVINI,KARTHIGAI_234,PUNARPOOSAM_123,AYILYAM,VISAGAM,KETTAI,AVITTAM_34,REVATHI")));
			this.put(Star.MIRUGASEERIDAM_12,new MatchingStars(Star.MIRUGASEERIDAM_12,"Rishaba",MatchingStar.getMatchingStars(11, MatchType.BEST, "ROHINI,THIRUVATHIRAI,PUNARPOOSAM,AYILYAM,ASTHAM,VISAGAM,KETTAI,THIRUVONAM,SATHAYAM,POORATTATHI,REVATHI"),MatchingStar.getMatchingStars(7, MatchType.MEDIUM, "BARANI,KARTHIGAI_234,POOSAM,UTHRAM,SWATHI,ANUSHAM,UTHRADAM ")));
			this.put(Star.MIRUGASEERIDAM_34,new MatchingStars(Star.MIRUGASEERIDAM_34,"Mithuna",MatchingStar.getMatchingStars(12, MatchType.BEST, "BARANI,THIRUVATHIRAI,PUNARPOOSAM,AYILYAM,ASTHAM,SWATHI,VISAGAM,KETTAI,POORADAM,THIRUVONAM,POORATTATHI_4,REVATHI"),MatchingStar.getMatchingStars(9, MatchType.MEDIUM, "ASVINI,KARTHIGAI_1,ROHINI,POOSAM,UTHRAM,ANUSHAM,MOOLAM,SATHAYAM,POORATTATHI_123 ")));
			this.put(Star.THIRUVATHIRAI,new MatchingStars(Star.THIRUVATHIRAI,"Mithuna",MatchingStar.getMatchingStars(13, MatchType.BEST, "ASVINI,KARTHIGAI_1,MIRUGASEERIDAM_34,PUNARPOOSAM,POOSAM,MAGAM,UTHRAM,CHITHRAI,VISAGAM,MOOLAM,UTHRADAM_1,POORATTATHI_4,UTHRATTATHI"),MatchingStar.getMatchingStars(6, MatchType.MEDIUM, "BARANI,AYILYAM,POORAM,POORADAM,AVITTAM_34,POORATTATHI_123")));
			this.put(Star.PUNARPOOSAM_123,new MatchingStars(Star.PUNARPOOSAM_123,"Mithuna",MatchingStar.getMatchingStars(12, MatchType.BEST, "BARANI,THIRUVATHIRAI,POOSAM,AYILYAM,ASTHAM,SWATHI,ANUSHAM,KETTAI,POORADAM,THIRUVONAM,UTHRATTATHI,REVATHI"),MatchingStar.getMatchingStars(5, MatchType.MEDIUM, "ASVINI,ROHINI,CHITHRAI,MOOLAM,SATHAYAM")));
			this.put(Star.PUNARPOOSAM_4,new MatchingStars(Star.PUNARPOOSAM_4,"Kadaka",MatchingStar.getMatchingStars(9, MatchType.BEST, "BARANI,ROHINI,POOSAM,AYILYAM,ASTHAM,SWATHI,ANUSHAM,KETTAI,THIRUVONAM"),MatchingStar.getMatchingStars(6, MatchType.MEDIUM, "ASVINI,CHITHRAI,MOOLAM,POORADAM,AVITTAM,UTHRATTATHI")));
			this.put(Star.POOSAM,new MatchingStars(Star.POOSAM,"Kadaka",MatchingStar.getMatchingStars(14, MatchType.BEST, "ASVINI,KARTHIGAI,MIRUGASEERIDAM_12,PUNARPOOSAM,AYILYAM,MAGAM,UTHRAM,CHITHRAI,VISAGAM,KETTAI,MOOLAM,UTHRADAM,AVITTAM,POORATTATHI_123"),MatchingStar.getMatchingStars(4, MatchType.MEDIUM, "ROHINI,ASTHAM,SWATHI,REVATHI")));
			this.put(Star.AYILYAM,new MatchingStars(Star.AYILYAM,"Kadaka",MatchingStar.getMatchingStars(7, MatchType.BEST, "BARANI,ROHINI,POOSAM,UTHRAM,SWATHI,THIRUVONAM,SATHAYAM"),MatchingStar.getMatchingStars(8, MatchType.MEDIUM, "PUNARPOOSAM_4,ASTHAM,CHITHRAI_12,VISAGAM,ANUSHAM,UTHRADAM,AVITTAM,UTHRATTATHI")));
			this.put(Star.MAGAM,new MatchingStars(Star.MAGAM,"Simma",MatchingStar.getMatchingStars(9, MatchType.BEST, "KARTHIGAI_234,POORAM,UTHRAM,CHITHRAI,VISAGAM,POORADAM,UTHRADAM,AVITTAM,POORATTATHI"),MatchingStar.getMatchingStars(6, MatchType.MEDIUM, "THIRUVATHIRAI,ASTHAM,SWATHI,ANUSHAM,THIRUVONAM,SATHAYAM")));
			this.put(Star.POORAM,new MatchingStars(Star.POORAM,"Simma",MatchingStar.getMatchingStars(7, MatchType.BEST, "THIRUVATHIRAI,UTHRAM,ASTHAM,MOOLAM,UTHRADAM,THIRUVONAM,SATHAYAM"),MatchingStar.getMatchingStars(10, MatchType.MEDIUM, "ASVINI,KARTHIGAI,MAGAM,CHITHRAI,SWATHI,VISAGAM,KETTAI,AVITTAM,POORATTATHI_4,REVATHI")));
			this.put(Star.UTHRAM_1,new MatchingStars(Star.UTHRAM_1,"Simma",MatchingStar.getMatchingStars(8, MatchType.BEST, "BARANI,ROHINI,MIRUGASEERIDAM,POORAM,ASTHAM,KETTAI,POORADAM,AVITTAM_34"),MatchingStar.getMatchingStars(9, MatchType.MEDIUM, "ASVINI,THIRUVATHIRAI,SWATHI,ANUSHAM,MOOLAM,THIRUVONAM,AVITTAM_12,SATHAYAM,REVATHI")));
			this.put(Star.UTHRAM_234,new MatchingStars(Star.UTHRAM_234,"Kanni  ",MatchingStar.getMatchingStars(7, MatchType.BEST, "MIRUGASEERIDAM_34,ASTHAM,KETTAI,POORADAM,THIRUVONAM,AVITTAM,REVATHI"),MatchingStar.getMatchingStars(12, MatchType.MEDIUM, "ASVINI,BARANI,MIRUGASEERIDAM_12,THIRUVATHIRAI,POOSAM,AYILYAM,POORAM,SWATHI,ANUSHAM,MOOLAM,SATHAYAM,UTHRATTATHI")));
			this.put(Star.ASTHAM,new MatchingStars(Star.ASTHAM,"Kanni",MatchingStar.getMatchingStars(10, MatchType.BEST, "KARTHIGAI_1,MIRUGASEERIDAM_34,POOSAM,UTHRAM_234,CHITHRAI,ANUSHAM,MOOLAM,UTHRADAM,AVITTAM,UTHRATTATHI"),MatchingStar.getMatchingStars(10, MatchType.MEDIUM, "KARTHIGAI_234,MIRUGASEERIDAM_12,PUNARPOOSAM,AYILYAM,MAGAM,VISAGAM,KETTAI,POORADAM,POORATTATHI,REVATHI")));
			this.put(Star.CHITHRAI_12,new MatchingStars(Star.CHITHRAI_12,"Kanni",MatchingStar.getMatchingStars(13, MatchType.BEST, "BARANI,THIRUVATHIRAI,PUNARPOOSAM,AYILYAM,ASTHAM,SWATHI,VISAGAM,KETTAI,POORADAM,THIRUVONAM,SATHAYAM,POORATTATHI,REVATHI"),MatchingStar.getMatchingStars(6, MatchType.MEDIUM, "KARTHIGAI,ROHINI,POOSAM,POORAM,ANUSHAM,MOOLAM")));
			this.put(Star.CHITHRAI_34,new MatchingStars(Star.CHITHRAI_34,"Thula",MatchingStar.getMatchingStars(10, MatchType.BEST, "BARANI,ROHINI,POORAM,VISAGAM,ANUSHAM,KETTAI,POORADAM,THIRUVONAM,SATHAYAM,POORATTATHI"),MatchingStar.getMatchingStars(9, MatchType.MEDIUM, "ASVINI,KARTHIGAI,THIRUVATHIRAI,PUNARPOOSAM,POOSAM,MAGAM,ASTHAM,MOOLAM,REVATHI")));
			this.put(Star.SWATHI,new MatchingStars(Star.SWATHI,"Thula",MatchingStar.getMatchingStars(12, MatchType.BEST, "KARTHIGAI,MIRUGASEERIDAM,PUNARPOOSAM,POOSAM,MAGAM,UTHRAM,VISAGAM,MOOLAM,UTHRADAM,AVITTAM,POORATTATHI,UTHRATTATHI"),MatchingStar.getMatchingStars(6, MatchType.MEDIUM, "BARANI,POORAM,CHITHRAI,KETTAI,POORADAM,REVATHI")));
			this.put(Star.VISAGAM_123,new MatchingStars(Star.VISAGAM_123,"Thula",MatchingStar.getMatchingStars(11, MatchType.BEST, "BARANI,ROHINI,POOSAM,AYILYAM,POORAM,SWATHI,ANUSHAM,KETTAI,POORADAM,THIRUVONAM,REVATHI"),MatchingStar.getMatchingStars(6, MatchType.MEDIUM, "ASVINI,MIRUGASEERIDAM_12,CHITHRAI_34,MOOLAM,AVITTAM,SATHAYAM")));
			this.put(Star.VISAGAM_4,new MatchingStars(Star.VISAGAM_4,"Vrichika",MatchingStar.getMatchingStars(11, MatchType.BEST, "ROHINI,THIRUVATHIRAI,POOSAM,AYILYAM,POORAM,ASTHAM,ANUSHAM,KETTAI,POORADAM,THIRUVONAM,SATHAYAM"),MatchingStar.getMatchingStars(6, MatchType.MEDIUM, "ASVINI,MIRUGASEERIDAM,CHITHRAI_12,MOOLAM,AVITTAM,REVATHI")));
			this.put(Star.ANUSHAM,new MatchingStars(Star.ANUSHAM,"Vrichika",MatchingStar.getMatchingStars(12, MatchType.BEST, "ASVINI,KARTHIGAI,MIRUGASEERIDAM,PUNARPOOSAM,MAGAM,CHITHRAI_12,VISAGAM_4,KETTAI,UTHRADAM,AVITTAM,POORATTATHI,REVATHI"),MatchingStar.getMatchingStars(6, MatchType.MEDIUM, "ROHINI,AYILYAM,ASTHAM,CHITHRAI_34,VISAGAM_123,THIRUVONAM,SATHAYAM ")));
			this.put(Star.KETTAI,new MatchingStars(Star.KETTAI,"Vrichika",MatchingStar.getMatchingStars(9, MatchType.BEST, "BARANI,ROHINI,POORAM,ASTHAM,SWATHI,POORADAM,THIRUVONAM,SATHAYAM,UTHRATTATHI"),MatchingStar.getMatchingStars(11, MatchType.MEDIUM, "KARTHIGAI,MIRUGASEERIDAM,PUNARPOOSAM_4,POOSAM,UTHRAM,CHITHRAI_12,VISAGAM_4,ANUSHAM,UTHRADAM,AVITTAM,POORATTATHI")));
			this.put(Star.MOOLAM,new MatchingStars(Star.MOOLAM,"Dhanur",MatchingStar.getMatchingStars(12, MatchType.BEST, "BARANI,KARTHIGAI,MIRUGASEERIDAM,PUNARPOOSAM,POOSAM,UTHRAM_234,CHITHRAI,VISAGAM,POORADAM,UTHRADAM,AVITTAM,POORATTATHI"),MatchingStar.getMatchingStars(6, MatchType.MEDIUM, "THIRUVATHIRAI,POORAM,UTHRAM_1,SWATHI,THIRUVONAM,SATHAYAM")));
			this.put(Star.POORADAM,new MatchingStars(Star.POORADAM,"Dhanur",MatchingStar.getMatchingStars(8, MatchType.BEST, "ASVINI,THIRUVATHIRAI,UTHRAM_234,ASTHAM,SWATHI,UTHRADAM,THIRUVONAM,SATHAYAM"),MatchingStar.getMatchingStars(7, MatchType.MEDIUM, "MIRUGASEERIDAM_34,PUNARPOOSAM,UTHRAM_1,MOOLAM,AVITTAM,POORATTATHI,REVATHI")));
			this.put(Star.UTHRADAM_1,new MatchingStars(Star.UTHRADAM_1,"Dhanur",MatchingStar.getMatchingStars(8, MatchType.BEST, "BARANI,ROHINI,AYILYAM,ASTHAM,POORADAM,THIRUVONAM,AVITTAM,REVATHI"),MatchingStar.getMatchingStars(7, MatchType.MEDIUM, "ASVINI,THIRUVATHIRAI,POOSAM,POORAM,SWATHI,SATHAYAM,UTHRATTATHI")));
			this.put(Star.UTHRADAM_234,new MatchingStars(Star.UTHRADAM_234,"Makara",MatchingStar.getMatchingStars(8, MatchType.BEST, "BARANI,ROHINI,MIRUGASEERIDAM,POORAM,THIRUVONAM,AVITTAM,SATHAYAM,REVATHI"),MatchingStar.getMatchingStars(11, MatchType.MEDIUM, "ASVINI,THIRUVATHIRAI,POOSAM,AYILYAM,ASTHAM,SWATHI,VISAGAM,ANUSHAM,KETTAI,POORADAM,UTHRATTATHI")));
			this.put(Star.THIRUVONAM,new MatchingStars(Star.THIRUVONAM,"Makara",MatchingStar.getMatchingStars(8, MatchType.BEST, "ASVINI,MIRUGASEERIDAM,MAGAM,UTHRAM_1,CHITHRAI,UTHRADAM_234,AVITTAM,UTHRATTATHI"),MatchingStar.getMatchingStars(10, MatchType.MEDIUM, "BARANI,PUNARPOOSAM,AYILYAM,POORAM,UTHRAM_234,ANUSHAM,KETTAI,MOOLAM,POORATTATHI,REVATHI")));
			this.put(Star.AVITTAM_12,new MatchingStars(Star.AVITTAM_12,"Makara",MatchingStar.getMatchingStars(10, MatchType.BEST, "ROHINI,THIRUVATHIRAI,PUNARPOOSAM,AYILYAM,POORAM,VISAGAM,KETTAI,THIRUVONAM,SATHAYAM,POORATTATHI_4"),MatchingStar.getMatchingStars(10, MatchType.MEDIUM, "ASVINI,KARTHIGAI,POOSAM,MAGAM,ASTHAM,SWATHI,POORADAM,UTHRADAM_234,POORATTATHI_4,UTHRATTATHI")));
			this.put(Star.AVITTAM_34,new MatchingStars(Star.AVITTAM_34,"Kumba",MatchingStar.getMatchingStars(11, MatchType.BEST, "ROHINI,THIRUVATHIRAI,PUNARPOOSAM,POORAM,ASTHAM,VISAGAM_4,KETTAI,POORADAM,THIRUVONAM,SATHAYAM,POORATTATHI"),MatchingStar.getMatchingStars(10, MatchType.MEDIUM, "ASVINI,KARTHIGAI,POOSAM,AYILYAM,UTHRAM,SWATHI,VISAGAM_123,MOOLAM,UTHRADAM,UTHRATTATHI")));
			this.put(Star.SATHAYAM,new MatchingStars(Star.SATHAYAM,"Kumba",MatchingStar.getMatchingStars(14, MatchType.BEST, "ASVINI,KARTHIGAI,MIRUGASEERIDAM,PUNARPOOSAM,MAGAM,POORAM,UTHRAM,CHITHRAI_12,VISAGAM_4,MOOLAM,UTHRADAM,AVITTAM,POORATTATHI,UTHRATTATHI"),MatchingStar.getMatchingStars(7, MatchType.MEDIUM, "BARANI,POOSAM,AYILYAM,CHITHRAI_34,VISAGAM_123,POORADAM,REVATHI")));
			this.put(Star.POORATTATHI_123,new MatchingStars(Star.POORATTATHI_123,"Kumba",MatchingStar.getMatchingStars(9, MatchType.BEST, "ROHINI,THIRUVATHIRAI,POORAM,ASTHAM,ANUSHAM,KETTAI,POORADAM,THIRUVONAM,UTHRATTATHI"),MatchingStar.getMatchingStars(7, MatchType.MEDIUM, "ASVINI,MIRUGASEERIDAM,POOSAM,AYILYAM,MAGAM,SWATHI,SATHAYAM")));
			this.put(Star.POORATTATHI_4,new MatchingStars(Star.POORATTATHI_4,"Meena",MatchingStar.getMatchingStars(9, MatchType.BEST, "ROHINI,THIRUVATHIRAI,POOSAM,AYILYAM,POORAM,ASTHAM,POORADAM,THIRUVONAM,UTHRATTATHI"),MatchingStar.getMatchingStars(4, MatchType.MEDIUM, "ASVINI,MIRUGASEERIDAM,MAGAM,ANUSHAM")));
			this.put(Star.UTHRATTATHI,new MatchingStars(Star.UTHRATTATHI,"Meena",MatchingStar.getMatchingStars(9, MatchType.BEST, "ASVINI,KARTHIGAI,MIRUGASEERIDAM,PUNARPOOSAM,AYILYAM,MAGAM,UTHRAM,UTHRADAM,REVATHI"),MatchingStar.getMatchingStars(7, MatchType.MEDIUM, "ROHINI,THIRUVATHIRAI,ASTHAM,MOOLAM,THIRUVONAM,AVITTAM,POORATTATHI")));
			this.put(Star.REVATHI,new MatchingStars(Star.REVATHI,"Meena",MatchingStar.getMatchingStars(10, MatchType.BEST, "BARANI,ROHINI,THIRUVATHIRAI,POOSAM,POORAM,ASTHAM,SWATHI,POORADAM,THIRUVONAM,UTHRATTATHI"),MatchingStar.getMatchingStars(8, MatchType.MEDIUM, "KARTHIGAI,MIRUGASEERIDAM,PUNARPOOSAM,UTHRAM,CHITHRAI,ANUSHAM,UTHRADAM,SATHAYAM")));
		}
		private static class BoysMatchingHolder{
		     private static final BoysMatching INSTANCE = new BoysMatching();
		}
	    public static BoysMatching getInstance() {		         
	    	return BoysMatchingHolder.INSTANCE;   
		}
	}
	public static class GirlsMatching extends HashMap<Star, MatchingStars>{
		private static final long serialVersionUID = 1L;

		private GirlsMatching() {	

			this.put(Star.ASVINI,new MatchingStars(Star.ASVINI,"Mesha",MatchingStar.getMatchingStars(8, MatchType.BEST, "BARANI,THIRUVATHIRAI,POOSAM,ANUSHAM,POORADAM,THIRUVONAM,SATHAYAM,UTHRATTATHI"),MatchingStar.getMatchingStars(10, MatchType.MEDIUM, "POORATTATHI,AVITTAM,UTHRADAM,VISAGAM,POORAM,PUNARPOOSAM,MIRUGASEERIDAM_34,CHITHRAI,ROHINI,KARTHIGAI_1")));
			this.put(Star.BARANI,new MatchingStars(Star.BARANI,"Mesha",MatchingStar.getMatchingStars(11, MatchType.BEST, "ASVINI,KARTHIGAI_1,MIRUGASEERIDAM_34,PUNARPOOSAM,  AYILYAM,CHITHRAI_34,VISAGAM,KETTAI,MOOLAM,UTHRADAM,REVATHI"),MatchingStar.getMatchingStars(7, MatchType.MEDIUM, "SATHAYAM,THIRUVONAM,SWATHI,THIRUVATHIRAI,KARTHIGAI_234,MAGAM,VISAGAM_4 ")));
			this.put(Star.KARTHIGAI_1,new MatchingStars(Star.KARTHIGAI_1,"Mesha ",MatchingStar.getMatchingStars(10, MatchType.BEST, "ASVINI,BARANI,THIRUVATHIRAI,POOSAM,ASTHAM,SWATHI,ANUSHAM,MOOLAM,SATHAYAM,UTHRATTATHI"),MatchingStar.getMatchingStars(6, MatchType.MEDIUM, "MIRUGASEERIDAM_34,MAGAM,CHITHRAI,KETTAI,AVITTAM,REVATHI")));
			this.put(Star.KARTHIGAI_234,new MatchingStars(Star.KARTHIGAI_234,"Rishaba",MatchingStar.getMatchingStars(9, MatchType.BEST, "ASVINI,BARANI,POOSAM,MAGAM,SWATHI,ANUSHAM,MOOLAM,SATHAYAM,UTHRATTATHI"),MatchingStar.getMatchingStars(7, MatchType.MEDIUM, "REVATHI,AVITTAM,KETTAI,ASTHAM,POORAM,ROHINI")));
			this.put(Star.ROHINI,new MatchingStars(Star.ROHINI,"Rishaba",MatchingStar.getMatchingStars(13, MatchType.BEST, "BARANI,KARTHIGAI,MIRUGASEERIDAM,PUNARPOOSAM_4,AYILYAM,UTHRAM_1,CHITHRAI_34,VISAGAM,KETTAI,UTHRADAM,AVITTAM,POORATTATHI,REVATHI"),MatchingStar.getMatchingStars(5, MatchType.MEDIUM, "UTHRATTATHI,ANUSHAM,POOSAM,PUNARPOOSAM_123,ASVINI")));
			this.put(Star.MIRUGASEERIDAM_12,new MatchingStars(Star.MIRUGASEERIDAM_12,"Rishaba",MatchingStar.getMatchingStars(11, MatchType.BEST, "ASVINI,KARTHIGAI,ROHINI,POOSAM,UTHRAM_1,ANUSHAM,MOOLAM,UTHRADAM_234,THIRUVONAM,SATHAYAM,UTHRATTATHI"),MatchingStar.getMatchingStars(9, MatchType.MEDIUM, "REVATHI,POORATTATHI,KETTAI,VISAGAM,AYILYAM,SWATHI,PUNARPOOSAM_4,BARANI,POORADAM ")));
			this.put(Star.MIRUGASEERIDAM_34,new MatchingStars(Star.MIRUGASEERIDAM_34,"Mithuna",MatchingStar.getMatchingStars(12, MatchType.BEST, "ASVINI,KARTHIGAI,ROHINI,THIRUVATHIRAI,UTHRAM,ASTHAM,ANUSHAM,MOOLAM,UTHRADAM_1,THIRUVONAM,SATHAYAM,UTHRATTATHI"),MatchingStar.getMatchingStars(9, MatchType.MEDIUM, "REVATHI,POORATTATHI,KETTAI,SWATHI,VISAGAM,POOSAM,POORADAM,PUNARPOOSAM_123,BARANI")));
			this.put(Star.THIRUVATHIRAI,new MatchingStars(Star.THIRUVATHIRAI,"Mithuna",MatchingStar.getMatchingStars(10, MatchType.BEST, "BARANI,MIRUGASEERIDAM,PUNARPOOSAM_123,POORAM,CHITHRAI_12,POORADAM,AVITTAM,VISAGAM_4,POORATTATHI,REVATHI"),MatchingStar.getMatchingStars(8, MatchType.MEDIUM, "UTHRATTATHI,UTHRADAM,MOOLAM,UTHRAM,MAGAM,PUNARPOOSAM_4,KARTHIGAI,ASVINI")));
			this.put(Star.PUNARPOOSAM_123,new MatchingStars(Star.PUNARPOOSAM_123,"Mithuna",MatchingStar.getMatchingStars(10, MatchType.BEST, "ASVINI,MIRUGASEERIDAM,THIRUVATHIRAI,POOSAM,CHITHRAI_12,ANUSHAM,MOOLAM,AVITTAM,SATHAYAM,UTHRATTATHI"),MatchingStar.getMatchingStars(10, MatchType.MEDIUM, "REVATHI,THIRUVONAM,POORADAM,KETTAI,ASTHAM,POOSAM,AYILYAM,CHITHRAI_34,SWATHI,ROHINI")));
			this.put(Star.PUNARPOOSAM_4,new MatchingStars(Star.PUNARPOOSAM_4,"Kadaka",MatchingStar.getMatchingStars(11, MatchType.BEST, "ASVINI,MIRUGASEERIDAM,THIRUVATHIRAI,POOSAM,CHITHRAI,SWATHI,ANUSHAM,MOOLAM,AVITTAM,UTHRATTATHI,SATHAYAM"),MatchingStar.getMatchingStars(8, MatchType.MEDIUM, "REVATHI,THIRUVONAM,KETTAI,POORADAM,ASTHAM,AYILYAM,ROHINI,BARANI")));
			this.put(Star.POOSAM,new MatchingStars(Star.POOSAM,"Kadaka",MatchingStar.getMatchingStars(10, MatchType.BEST, "ROHINI,THIRUVATHIRAI,PUNARPOOSAM,AYILYAM,ASTHAM,SWATHI,VISAGAM,POORATTATHI,REVATHI,SATHAYAM"),MatchingStar.getMatchingStars(9, MatchType.MEDIUM, "ASVINI,KARTHIGAI,MIRUGASEERIDAM,UTHRAM,CHITHRAI,MOOLAM,UTHRADAM_234,AVITTAM,MAGAM")));
			this.put(Star.AYILYAM,new MatchingStars(Star.AYILYAM,"Kadaka",MatchingStar.getMatchingStars(10, MatchType.BEST, "KARTHIGAI,MIRUGASEERIDAM,PUNARPOOSAM,POOSAM,CHITHRAI,VISAGAM_123,ANUSHAM,AVITTAM,POORATTATHI,UTHRATTATHI"),MatchingStar.getMatchingStars(8, MatchType.MEDIUM, "BARANI,ROHINI,THIRUVATHIRAI,ASTHAM,UTHRAM_234,UTHRADAM,THIRUVONAM,SATHAYAM")));
			this.put(Star.MAGAM,new MatchingStars(Star.MAGAM,"Simma",MatchingStar.getMatchingStars(8, MatchType.BEST, "BARANI,THIRUVATHIRAI,POOSAM,SWATHI,ANUSHAM,THIRUVONAM,SATHAYAM,UTHRATTATHI"),MatchingStar.getMatchingStars(6, MatchType.MEDIUM, "KARTHIGAI,POORAM,CHITHRAI_34,ASTHAM,AVITTAM,POORATTATHI ")));
			this.put(Star.POORAM,new MatchingStars(Star.POORAM,"Simma",MatchingStar.getMatchingStars(12, MatchType.BEST, "ASVINI,KARTHIGAI,THIRUVATHIRAI,MAGAM,UTHRAM_1,CHITHRAI_34, VISAGAM,KETTAI,UTHRADAM_234,AVITTAM,POORATTATHI,REVATHI"),MatchingStar.getMatchingStars(5, MatchType.MEDIUM, "THIRUVATHIRAI,SWATHI,MOOLAM,THIRUVONAM,SATHAYAM")));
			this.put(Star.UTHRAM_1,new MatchingStars(Star.UTHRAM_1,"Simma",MatchingStar.getMatchingStars(12, MatchType.BEST, "ASVINI,BARANI,ROHINI,THIRUVATHIRAI,POOSAM,MAGAM,POORAM,SWATHI,ANUSHAM,THIRUVONAM,SATHAYAM,UTHRATTATHI"),MatchingStar.getMatchingStars(7, MatchType.MEDIUM, "REVATHI,AVITTAM,KETTAI,AYILYAM,MIRUGASEERIDAM,POORADAM,MOOLAM")));
			this.put(Star.UTHRAM_234,new MatchingStars(Star.UTHRAM_234,"Kanni  ",MatchingStar.getMatchingStars(13, MatchType.BEST, "ASVINI,BARANI,ROHINI,THIRUVATHIRAI,POOSAM,MAGAM,POORAM,ASTHAM,ANUSHAM,MOOLAM,POORADAM,SATHAYAM,UTHRATTATHI"),MatchingStar.getMatchingStars(6, MatchType.MEDIUM, "REVATHI,AVITTAM_34,KETTAI,SWATHI,AYILYAM,MIRUGASEERIDAM ")));
			this.put(Star.ASTHAM,new MatchingStars(Star.ASTHAM,"Kanni",MatchingStar.getMatchingStars(15, MatchType.BEST, "BARANI,KARTHIGAI,MIRUGASEERIDAM,PUNARPOOSAM,AYILYAM,POORAM,UTHRAM,CHITHRAI_12,VISAGAM_4,KETTAI,POORADAM,UTHRADAM_1,AVITTAM_34,POORATTATHI,REVATHI"),MatchingStar.getMatchingStars(4, MatchType.MEDIUM, "POOSAM,MAGAM,ANUSHAM,UTHRATTATHI")));
			this.put(Star.CHITHRAI_12,new MatchingStars(Star.CHITHRAI_12,"Kanni",MatchingStar.getMatchingStars(10, MatchType.BEST, "ASVINI,KARTHIGAI,ROHINI,THIRUVATHIRAI,POOSAM,MAGAM,ASTHAM,ANUSHAM,MOOLAM,SATHAYAM"),MatchingStar.getMatchingStars(6, MatchType.MEDIUM, "REVATHI,VISAGAM,POORAM,AYILYAM,PUNARPOOSAM,BARANI")));
			this.put(Star.CHITHRAI_34,new MatchingStars(Star.CHITHRAI_34,"Thula",MatchingStar.getMatchingStars(9, MatchType.BEST, "ASVINI,KARTHIGAI,ROHINI,THIRUVATHIRAI,POOSAM,ASTHAM,SWATHI,MOOLAM,THIRUVONAM"),MatchingStar.getMatchingStars(7, MatchType.MEDIUM, "REVATHI,VISAGAM,POORAM,KETTAI,AYILYAM,PUNARPOOSAM,BARANI ")));
			this.put(Star.SWATHI,new MatchingStars(Star.SWATHI,"Thula",MatchingStar.getMatchingStars(10, MatchType.BEST, "BARANI,MIRUGASEERIDAM_34,PUNARPOOSAM,AYILYAM,KETTAI,POORADAM,POORAM,CHITHRAI,VISAGAM,REVATHI"),MatchingStar.getMatchingStars(9, MatchType.MEDIUM, "UTHRATTATHI,UTHRAM,UTHRADAM,KARTHIGAI,POOSAM,MAGAM,MOOLAM,POORATTATHI,AVITTAM_12")));
			this.put(Star.VISAGAM_123,new MatchingStars(Star.VISAGAM_123,"Thula",MatchingStar.getMatchingStars(9, MatchType.BEST, "ASVINI,MIRUGASEERIDAM,THIRUVATHIRAI,POOSAM,MAGAM,CHITHRAI,SWATHI,MOOLAM,AVITTAM_12"),MatchingStar.getMatchingStars(10, MatchType.MEDIUM, "REVATHI,ASTHAM,POORAM,AYILYAM,ROHINI,BARANI,ANUSHAM,KETTAI,AVITTAM_34,SATHAYAM")));
			this.put(Star.VISAGAM_4,new MatchingStars(Star.VISAGAM_4,"Vrichika",MatchingStar.getMatchingStars(11, MatchType.BEST, "ASVINI,MIRUGASEERIDAM,THIRUVATHIRAI,POOSAM,MAGAM,CHITHRAI,SWATHI,ANUSHAM,MOOLAM,AVITTAM,SATHAYAM"),MatchingStar.getMatchingStars(7, MatchType.MEDIUM, "KETTAI,ASTHAM,POORAM,ROHINI,BARANI,AYILYAM,REVATHI")));
			this.put(Star.ANUSHAM,new MatchingStars(Star.ANUSHAM,"Vrichika",MatchingStar.getMatchingStars(9, MatchType.BEST, "ROHINI,PUNARPOOSAM,AYILYAM,ASTHAM,SWATHI,VISAGAM,SATHAYAM,THIRUVONAM,POORATTATHI_123"),MatchingStar.getMatchingStars(10, MatchType.MEDIUM, "REVATHI,POORATTATHI,KETTAI,CHITHRAI,UTHRADAM_234,UTHRAM,MAGAM,MIRUGASEERIDAM,KARTHIGAI,ASVINI ")));
			this.put(Star.KETTAI,new MatchingStars(Star.KETTAI,"Vrichika",MatchingStar.getMatchingStars(10, MatchType.BEST, "KARTHIGAI,MIRUGASEERIDAM,PUNARPOOSAM,POOSAM,UTHRAM,CHITHRAI,VISAGAM,ANUSHAM,AVITTAM"),MatchingStar.getMatchingStars(9, MatchType.MEDIUM, "UTHRATTATHI,POORATTATHI,THIRUVONAM,UTHRADAM,ASTHAM,SWATHI,POORAM,ROHINI,BARANI ")));
			this.put(Star.MOOLAM,new MatchingStars(Star.MOOLAM,"Dhanur",MatchingStar.getMatchingStars(6, MatchType.BEST, "THIRUVATHIRAI,POOSAM,POORAM,ASTHAM,SWATHI,SATHAYAM"),MatchingStar.getMatchingStars(9, MatchType.MEDIUM, "UTHRATTATHI,VISAGAM,CHITHRAI,UTHRAM,PUNARPOOSAM,MIRUGASEERIDAM_34,POORADAM,THIRUVONAM,AVITTAM ")));
			this.put(Star.POORADAM,new MatchingStars(Star.POORADAM,"Dhanur",MatchingStar.getMatchingStars(11, MatchType.BEST, "MIRUGASEERIDAM,PUNARPOOSAM_123,MAGAM,UTHRAM,CHITHRAI,VISAGAM,KETTAI,MOOLAM,UTHRADAM_1,POORATTATHI,REVATHI"),MatchingStar.getMatchingStars(8, MatchType.MEDIUM, "THIRUVATHIRAI,AYILYAM,PUNARPOOSAM_4,ASTHAM,SWATHI,UTHRADAM_234,THIRUVONAM,AVITTAM")));
			this.put(Star.UTHRADAM_1,new MatchingStars(Star.UTHRADAM_1,"Dhanur",MatchingStar.getMatchingStars(11, MatchType.BEST, "THIRUVATHIRAI,POOSAM,MAGAM,POORAM,ASTHAM,SWATHI,ANUSHAM,MOOLAM,POORADAM,SATHAYAM,UTHRATTATHI"),MatchingStar.getMatchingStars(8, MatchType.MEDIUM, "ASVINI,BARANI,MIRUGASEERIDAM,AYILYAM,KETTAI,THIRUVONAM,AVITTAM,REVATHI")));
			this.put(Star.UTHRADAM_234,new MatchingStars(Star.UTHRADAM_234,"Makara",MatchingStar.getMatchingStars(13, MatchType.BEST, "ASVINI,BARANI,POOSAM,MAGAM,POORAM,ASTHAM,SWATHI,ANUSHAM,MOOLAM,POORADAM,THIRUVONAM,SATHAYAM,UTHRATTATHI"),MatchingStar.getMatchingStars(5, MatchType.MEDIUM, "ROHINI,AYILYAM,KETTAI,AVITTAM,REVATHI")));
			this.put(Star.THIRUVONAM,new MatchingStars(Star.THIRUVONAM,"Makara",MatchingStar.getMatchingStars(12, MatchType.BEST, "BARANI,MIRUGASEERIDAM,PUNARPOOSAM,AYILYAM,UTHRAM_234,CHITHRAI,POORAM,VISAGAM,KETTAI,POORADAM,UTHRADAM,AVITTAM,POORATTATHI,REVATHI"),MatchingStar.getMatchingStars(6, MatchType.MEDIUM, "MAGAM,POORAM,UTHRAM_1,ANUSHAM,MOOLAM,UTHRATTATHI")));
			this.put(Star.AVITTAM_12,new MatchingStars(Star.AVITTAM_12,"Makara",MatchingStar.getMatchingStars(11, MatchType.BEST, "ASVINI,KARTHIGAI,POOSAM,UTHRAM_234,ASTHAM,SWATHI,ANUSHAM, MOOLAM,UTHRADAM,THIRUVONAM,SATHAYAM"),MatchingStar.getMatchingStars(9, MatchType.MEDIUM, "UTHRATTATHI,POORADAM,VISAGAM,AYILYAM,PUNARPOOSAM,KARTHIGAI_234,KETTAI,UTHRAM,MAGAM")));
			this.put(Star.AVITTAM_34,new MatchingStars(Star.AVITTAM_34,"Kumba",MatchingStar.getMatchingStars(11, MatchType.BEST, "KARTHIGAI,POOSAM,MAGAM,UTHRAM,ASTHAM,SWATHI,ANUSHAM,MOOLAM,UTHRADAM,THIRUVONAM,SATHAYAM"),MatchingStar.getMatchingStars(10, MatchType.MEDIUM, "POORADAM,KETTAI,VISAGAM,POORAM,AYILYAM,PUNARPOOSAM_4,THIRUVATHIRAI,ROHINI,ASVINI,UTHRATTATHI")));
			this.put(Star.SATHAYAM,new MatchingStars(Star.SATHAYAM,"Kumba",MatchingStar.getMatchingStars(9, MatchType.BEST, "MIRUGASEERIDAM,PUNARPOOSAM,AYILYAM,POORAM,CHITHRAI,VISAGAM,KETTAI,POORADAM,AVITTAM,"),MatchingStar.getMatchingStars(9, MatchType.MEDIUM, "REVATHI,POORATTATHI,UTHRADAM,MOOLAM,ANUSHAM,UTHRAM,POOSAM,PUNARPOOSAM,ASVINI")));
			this.put(Star.POORATTATHI_123,new MatchingStars(Star.POORATTATHI_123,"Kumba",MatchingStar.getMatchingStars(10, MatchType.BEST, "ASVINI,MIRUGASEERIDAM_12,POOSAM,MAGAM,CHITHRAI,SWATHI,ANUSHAM,MOOLAM,AVITTAM,SATHAYAM"),MatchingStar.getMatchingStars(7, MatchType.MEDIUM, "UTHRATTATHI,THIRUVONAM,POORADAM,KETTAI,ANUSHAM,ASTHAM,AYILYAM")));
			this.put(Star.POORATTATHI_4,new MatchingStars(Star.POORATTATHI_4,"Meena",MatchingStar.getMatchingStars(8, MatchType.BEST, "MIRUGASEERIDAM,THIRUVATHIRAI,CHITHRAI_12,ANUSHAM,MOOLAM,AVITTAM,SATHAYAM,UTHRATTATHI"),MatchingStar.getMatchingStars(6, MatchType.MEDIUM, "THIRUVONAM,POORADAM,KETTAI,ASTHAM,POOSAM,SWATHI")));
			this.put(Star.UTHRATTATHI,new MatchingStars(Star.UTHRATTATHI,"Meena",MatchingStar.getMatchingStars(9, MatchType.BEST, "ROHINI,THIRUVATHIRAI,PUNARPOOSAM_23,ASTHAM,KETTAI,THIRUVONAM,SATHAYAM,POORATTATHI,REVATHI"),MatchingStar.getMatchingStars(8, MatchType.MEDIUM, "AVITTAM,UTHRADAM,MOOLAM,SWATHI,AYILYAM,UTHRAM_34,PUNARPOOSAM_4,KARTHIGAI_234")));
			this.put(Star.REVATHI,new MatchingStars(Star.REVATHI,"Meena",MatchingStar.getMatchingStars(9, MatchType.BEST, "KARTHIGAI_234,MIRUGASEERIDAM,PUNARPOOSAM_123,UTHRAM_234,CHITHRAI_12,VISAGAM,ANUSHAM,UTHRADAM,UTHRATTATHI"),MatchingStar.getMatchingStars(9, MatchType.MEDIUM, "SATHAYAM,THIRUVONAM,VISAGAM,ASTHAM,POOSAM,POORADAM,PUNARPOOSAM_4,ROHINI,KARTHIGAI_1")));

		}
		private static class GirlsMatchingHolder{
		     private static final GirlsMatching INSTANCE = new GirlsMatching();
		}
	    public static GirlsMatching getInstance() {		         
	    	return GirlsMatchingHolder.INSTANCE;   
		}
	}
	public static void main(String[] args) throws DataError {
		System.out.println(Sevvai.fromString("Two"));
		System.out.println(RaahuKethu.fromString("CRK"));
		System.out.println(Raasi.fromString("danus"));
		System.out.println(Planet.fromString("sUriyan"));
		System.out.println(Star.fromString("PooRaDam"));
		System.out.println(Star.fromString("uthRaDam_1").toString());
	}
}


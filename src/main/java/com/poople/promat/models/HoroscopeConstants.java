package com.poople.promat.models;

import java.util.HashSet;

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
    	Star(String name) {
    		this.name = name;
    	}
    	Star(String name, int[] padham) {
    		this.name = name;
    		this.padham = padham;
    	}
    	
        public int[] getPadham() {
			return padham;
		}
		@Override 
        public String toString() {
            //only capitalize the first letter
            //String s = super.toString();
            return name.substring(0, 1) + name.substring(1).toLowerCase();

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
    public class MatchingStars{
    	Match bestMatchStars;
    	Match mediumMatchStars;
		public Match getBestMatchStars() {
			return bestMatchStars;
		}
		public void setBestMatchStars(Match bestMatchStars) {
			this.bestMatchStars = bestMatchStars;
		}
		public Match getMediumMatchStars() {
			return mediumMatchStars;
		}
		public void setMediumMatchStars(Match mediumMatchStars) {
			this.mediumMatchStars = mediumMatchStars;
		}
    	
    }
    public class Match extends HashSet<HoroscopeConstants.Star>{
		private static final long serialVersionUID = 1L;
		String strength;

		public String getStrength() {
			return strength;
		}

		public void setStrength(String strength) {
			this.strength = strength;
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


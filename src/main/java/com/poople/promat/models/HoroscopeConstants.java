package com.poople.promat.models;

public interface HoroscopeConstants {
    enum Star { 
    	ASVINI("ASVINI"),
        BARANI("BARANI"),
        KARTHIGAI_1("KARTHIGAI"),
        KARTHIGAI_234("KARTHIGAI"),
        ROHINI("ROHINI"),
        MIRUGASEERIDAM_12("MIRUGASEERIDAM"),
        MIRUGASEERIDAM_34("MIRUGASEERIDAM"),
        THIRUVATHIRAI("THIRUVATHIRAI"),
        PUNARPOOSAM_123("PUNARPOOSAM"),
        PUNARPOOSAM_4("PUNARPOOSAM"),
        POOSAM("POOSAM"),
        AYILYAM("AYILYAM"),
        MAGAM("MAGAM"),
        POORAM("POORAM"),
        UTHRAM_1("UTHRAM"),
        UTHRAM_234("UTHRAM"),
        ASTHAM("ASTHAM"),
        CHITHRAI_12("CHITHRAI"),
        CHITHRAI_34("CHITHRAI"),
        SWATHI("SWATHI"),
        VISAGAM_123("VISAGAM"),
        VISAGAM_4("VISAGAM"),
        ANUSHAM("ANUSHAM"),
        KETTAI("KETTAI"),
        MOOLAM("MOOLAM"),
        POORADAM("POORADAM"),
        UTHRADAM_1("UTHRADAM"),
        UTHRADAM_234("UTHRADAM"),
        THIRUVONAM("THIRUVONAM"),
        AVITTAM_12("AVITTAM"),
        AVITTAM_34("AVITTAM"),
        SATHAYAM("SATHAYAM"),
        POORATTATHI_123("POORATTATHI"),
        POORATTATHI_4("POORATTATHI"),
        UTHRATTATHI("UTHRATTATHI"),
        REVATHI("REVATHI");
    	private String name;
    	Star(String name) {
    		this.name = name;
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
	
    public static void main(String[] args) throws DataError {
		System.out.println(Sevvai.fromString("Two"));
		System.out.println(RaahuKethu.fromString("CRK"));
		System.out.println(Raasi.fromString("danus"));
		System.out.println(Planet.fromString("sUriyan"));
		System.out.println(Star.fromString("PooRaDam"));
		System.out.println(Star.fromString("uthRaDam_1").toString());
	}
}

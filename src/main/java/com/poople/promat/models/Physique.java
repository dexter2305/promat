package com.poople.promat.models;

public class Physique {
    private Long heightInCm;
    private long weightInKg;

    private BodyType bodyType;

    private SkinTone skinTone;

    private Bloodgroup bloodGroup;

    Physique() {
    }

    public Long getHeight() {
        return heightInCm;
    }

    public void setHeight(Long height) {
        this.heightInCm = height;
    }

    public long getWeight() {
        return weightInKg;
    }

    public void setWeight(int weight) {
        this.weightInKg = weight;
    }

    public BodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    public SkinTone getSkinTone() {
        return skinTone;
    }

    public void setSkinTone(SkinTone skinTone) {
        this.skinTone = skinTone;
    }

    public Bloodgroup getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(Bloodgroup bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public enum BodyType {
        NORMAL, SLIM, ATHLETIC, HEAVY
    }

    public enum SkinTone {
        DARK, MODERATE, FAIR;

        public static SkinTone fromString(String s) {
            if (s == null || s.isEmpty()) return null;
            SkinTone tone;
            try {
                tone = SkinTone.valueOf(s);
            } catch (IllegalArgumentException ie) {
                if (s.equalsIgnoreCase("ok") || s.toLowerCase().contains("ok") || s.toLowerCase().contains("wheatish")) tone = SkinTone.MODERATE;
                else if (s.equalsIgnoreCase("fair")  || s.toLowerCase().contains("fair")) tone = SkinTone.FAIR;
                else if (s.equalsIgnoreCase("dark")) tone = SkinTone.DARK;
                else tone = null;
            }
            return tone;
        }

    }

    public enum Bloodgroup {
        OPOSITIVE("O+"),
        ONEGATIVE("O-"),
        APOSITIVE("A+"),
        ANEGATIVE("A-"),
        BPOSITIVE("B+"),
        BNEGATIVE("B-"),
        ABPOSITIVE("AB+"),
        ABNEGATIVE("AB-");
        private String bloodGroup;

        Bloodgroup(String bloodGroup) {
            this.bloodGroup = bloodGroup;
        }

        public String getBloodGroup() {
            return this.bloodGroup;
        }

        @Override
        public String toString() {
            String s = super.toString();
            return s.substring(0, 1) + s.substring(1).toLowerCase();
        }

        public static Bloodgroup fromString(String valueAsString) {
            if (valueAsString == null || valueAsString.trim().isEmpty()) return null;
            Bloodgroup bloodGroup = null;
            try {
                bloodGroup = Bloodgroup.valueOf(valueAsString);
            } catch (IllegalArgumentException ie) {
                valueAsString = valueAsString.trim();
                valueAsString = valueAsString.replaceAll(" ", "");
                Bloodgroup[] values = Bloodgroup.values();
                for (Bloodgroup group : values) {
                    if (group.toString().equalsIgnoreCase(valueAsString)) {
                        bloodGroup = group;
                        break;
                    }
                }
            }
            return bloodGroup;
        }
    }

    @Override
    public String toString() {
        String s = "physique:{" +
                "height:'" + heightInCm + "\'" +
                "weight:'" + weightInKg + "\'" +
                "skinTone:'" + (skinTone != null ? skinTone.toString() : "") + "\'" +
                "bloodGroup:'" + (bloodGroup != null ? bloodGroup.toString() : "") + "\'" +
                "bodyType:'" + (bodyType != null ? bodyType.toString() : "") + "\'" +
                "}";
        return s;
    }
}

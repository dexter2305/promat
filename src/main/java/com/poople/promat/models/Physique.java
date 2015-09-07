package com.poople.promat.models;

public class Physique {
    private int height;
    private int weight;

    private BodyType bodyType;

    private SkinTone skinTone;

    private Bloodgroup bloodGroup;

    Physique() {
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
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
        DARK, MODERATE, FAIR
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
    }

}

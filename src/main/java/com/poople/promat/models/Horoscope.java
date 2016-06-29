package com.poople.promat.models;

import com.poople.promat.models.HoroscopeConstants.Planet;
import com.poople.promat.models.HoroscopeConstants.Raasi;
import com.poople.promat.models.HoroscopeConstants.Star;



public class Horoscope {
    private Dob dateOfBirth;       
    private String birthPlace;
    private Star star;
    private int paadham;  
    private Raasi raasi;    
    private Raasi lagnam; 
    private String raahu_kethu;
    private String sevvai;
    private Planet dasa;
    private String iruppu;
    public Horoscope(Dob dateOfBirth, String birthPlace) {
        super();
        this.dateOfBirth = dateOfBirth;
        this.birthPlace = birthPlace;
    }
    
	public Horoscope(Dob dateOfBirth, String birthPlace, Star star, int paadham, Raasi raasi, Raasi lagnam,
			String raahu_kethu, String sevvai, Planet dasa, String iruppu) {
		super();
		this.dateOfBirth = dateOfBirth;
		this.birthPlace = birthPlace;
		this.star = star;
		this.paadham = paadham;
		this.raasi = raasi;
		this.lagnam = lagnam;
		this.raahu_kethu = raahu_kethu;
		this.sevvai = sevvai;
		this.dasa = dasa;
		this.iruppu = iruppu;
	}

	public Dob getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Dob dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getBirthPlace() {
		return birthPlace;
	}
	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}
	public Star getStar() {
		return star;
	}
	public void setStar(Star star) {
		this.star = star;
	}
	public int getPaadham() {
		return paadham;
	}
	public void setPaadham(int paadham) {
		this.paadham = paadham;
	}
	public Raasi getRaasi() {
		return raasi;
	}
	public void setRaasi(Raasi raasi) {
		this.raasi = raasi;
	}
	public Raasi getLagnam() {
		return lagnam;
	}
	public void setLagnam(Raasi lagnam) {
		this.lagnam = lagnam;
	}
	public String getRaahu_kethu() {
		return raahu_kethu;
	}
	public void setRaahu_kethu(String raahu_kethu) {
		this.raahu_kethu = raahu_kethu;
	}
	public String getSevvai() {
		return sevvai;
	}
	public void setSevvai(String sevvai) {
		this.sevvai = sevvai;
	}
	public Planet getDasa() {
		return dasa;
	}
	public void setDasa(Planet dasa) {
		this.dasa = dasa;
	}
	public String getIruppu() {
		return iruppu;
	}
	public void setIruppu(String iruppu) {
		this.iruppu = iruppu;
	}
    
	@Override
	public String toString() {
		StringBuilder contentBuilder = new StringBuilder();
        contentBuilder
                .append("{")
                .append("birthPlace:").append("'").append(birthPlace).append("'").append(",")
                .append("star:").append("'").append(star).append("'").append(",")
                .append("paadham:").append("'").append(paadham).append("'").append(",")
                .append("raasi:").append("'").append(raasi).append("'").append(",")
                .append("star:").append("'").append(star).append("'").append(",")
                .append("lagnam:").append("'").append(lagnam).append("'").append(",")
                .append("raahu_kethu:").append("'").append(raahu_kethu).append("'").append(",")
                .append("sevvai:").append("'").append(sevvai).append("'").append(",")
                .append("dasa:").append("'").append(dasa).append("'").append(",")
                .append("iruppu:").append("'").append(iruppu).append("'").append(",")
                .append("}");
        return contentBuilder.toString();
	}
    
}

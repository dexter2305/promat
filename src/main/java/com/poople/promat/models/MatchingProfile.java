package com.poople.promat.models;

import java.time.LocalDate;
import java.time.LocalTime;

import com.poople.promat.models.HoroscopeConstants.MatchType;
import com.poople.promat.models.HoroscopeConstants.Star;

public class MatchingProfile{
	private static final long serialVersionUID = 1L;
	private long id;
	private String name;
	private int strength;
	private MatchType match = MatchType.BEST;
	private Star star;
	private String info = "";
	private Candidate profile;
	public MatchingProfile(Long id, String name, String info) {
		super();
		this.id = id;
		this.name = name;
		this.info = info;
	}
	public MatchingProfile(Long id, String name, Star star, int strength, MatchType match, Candidate profile) {
		super();
		this.id = id;
		this.name = name;
		this.star = star;
		this.strength = strength;
		this.match = match;
		this.profile = profile;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Star getStar() {
		return star;
	}
	public void setStar(Star star) {
		this.star = star;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public Candidate getProfile() {
		return profile;
	}
	public void setProfile(Candidate profile) {
		this.profile = profile;
	}
	public void setStrength(int strength) {
		this.strength = strength;
	}
	public void setMatch(MatchType match) {
		this.match = match;
	}
	public MatchType getMatch() {
		return match;
	}
	public int getStrength() {
		return strength;
	}
	@Override
	public String toString() {
		Horoscope h = profile.getHoroscope();
		String birthPlace = "";
		String rk = "";
		String sev = "";
		if(h!=null) {
			birthPlace = h.getBirthPlace();
			rk = h.getRaahu_kethu();
			sev = h.getSevvai();
		}
		Integer age = null;
		LocalDate d = null;
		LocalTime t = null;
		if(profile.getDob() != null) {
			age = profile.getDob().age();
			d = profile.getDob().getBirthdate();
			t = profile.getDob().getBirthtime();
		}
		String workPlace = "";
		String company = "";
		if (profile.getOccupations() != null && !profile.getOccupations().isEmpty()){
			Occupation currOcc = Occupation.getCurrentOccupation(profile.getOccupations());
			if(currOcc !=null) {
				workPlace = currOcc.getCompanyLocation();
				company = currOcc.getCompany();
			}
		}
		String strMat = id+","+name+","+profile.getId()+","+profile.getName()+","+profile.getKulam()+","+star +","+strength+","+match+","+d+","+t+","+age+","+birthPlace+","+rk+","+sev+","+(profile.getPhysique()!=null?profile.getPhysique().getHeight():"")+","+profile.getMaritalStatus()+","+company+","+workPlace;
		
		return strMat.replaceAll("null", "");
		//return "{Id:"+profile.getId()+", Kulam:"+profile.getKulam()+", Star:"+star +", Strength:"+strength+", Match:"+match+", Dob:"+profile.getDob()+", Age:"+age+", BirthPlace:"+birthPlace+", R/K:"+rk+", Sevvai:"+sev+", Height:"+(profile.getPhysique()!=null?profile.getPhysique().getHeight():"")+"}";
	}
	
	
}
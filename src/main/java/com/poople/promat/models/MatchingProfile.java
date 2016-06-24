package com.poople.promat.models;

import java.util.Collection;
import java.util.HashSet;

import com.poople.promat.models.HoroscopeConstants.MatchType;
import com.poople.promat.models.HoroscopeConstants.Star;

public class MatchingProfile{
	private static final long serialVersionUID = 1L;
	private int strength;
	private MatchType match = MatchType.BEST;
	private Star star;
	private String info = "";
	private Candidate profile;
	public MatchingProfile(String info) {
		super();
		this.info = info;
	}
	public MatchingProfile(Star star, int strength, MatchType match, Candidate profile) {
		super();
		this.star = star;
		this.strength = strength;
		this.match = match;
		this.profile = profile;
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
		if(profile.getDob() != null) {
			age = profile.getDob().age();
		}
		return "{Id:"+profile.getId()+", Kulam:"+profile.getKulam()+", Star:"+star +", Strength:"+strength+", Match:"+match+", Dob:"+profile.getDob()+", Age:"+age+", BirthPlace:"+birthPlace+", R/K:"+rk+", Sevvai:"+sev+", Height:"+(profile.getPhysique()!=null?profile.getPhysique().getHeight():"")+"}";
	}
	
	
}
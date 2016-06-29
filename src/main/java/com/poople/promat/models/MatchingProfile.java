package com.poople.promat.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.poople.promat.models.HoroscopeConstants.MatchType;
import com.poople.promat.models.HoroscopeConstants.Star;

public class MatchingProfile{
	private static final long serialVersionUID = 1L;
	private Long id;
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
	public static ArrayList<String> getListFormat() {
		return new ArrayList<String>(Arrays.asList("Id","Name","Star","MatchingId","MatchingName","Kulam","Star","Strength","MatchType","Dob","Tob","Age","BirthPlace","RahuKethu","Sevvai","Height","MaritalStatus","Qualification","WorkPlace","Detail 1","Detail2", "Info"));
	}
	public List<String> toList() {
		String[] strMat = {};
		if (profile != null) {
			Horoscope h = profile.getHoroscope();
			String birthPlace = "";
			String rk = "";
			String sev = "";
			HoroscopeConstants.Star matchedStar = null;
			if(h!=null) {
				birthPlace = h.getBirthPlace();
				rk = h.getRaahu_kethu();
				sev = h.getSevvai();
				matchedStar = h.getStar();
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
			if (profile.getOccupations() != null && !profile.getOccupations().isEmpty()){
				Occupation currOcc = Occupation.getCurrentOccupation(profile.getOccupations());
				if(currOcc !=null) {
					workPlace = currOcc.getCompanyLocation();
				}
			}
			String qual = "";
			if (profile.getEducations() != null && !profile.getEducations().isEmpty()){
				Set<Education> edus = profile.getEducations();
				if(edus !=null) {
					qual = edus.stream().map(Education::getQualification).collect(Collectors.joining(","));
				}
			}
			
			Note n = null;
			String det1= "";
			String det2 = "";
			if (profile.getNotes()!= null && !profile.getNotes().isEmpty()) {
				// as of now only one note is stored in excel
				Iterator<Note> i = profile.getNotes().iterator();
				n = i.next();
				if(n != null) {
					det1 = getStringValue(n.getNote());
				}
				n = i.hasNext() ? i.next() : null;
				if(n != null) {
					det2 = getStringValue(n.getNote());
				}
			}
			strMat = new String[]{getStringValue(id),getStringValue(name),convertToString(star) ,getStringValue(profile.getId()),profile.getName(),profile.getKulam(),convertToString(matchedStar) ,getStringValue(strength),convertToString(match),convertToString(d),convertToString(t),getStringValue(age),birthPlace,rk,sev,(profile.getPhysique()!=null?getStringValue(profile.getPhysique().getHeight()):""),profile.getMaritalStatus(),qual,workPlace,det1,det2, info};
		} else {
			strMat = new String[]{getStringValue(id),"","","","","","","","","","","","","","","","","","","","", info};
		}
		
		return new ArrayList<String>(Arrays.asList(strMat));
	}
	private static String getStringValue(Object s) {
		if (s == null) {
			return "";
		} else {
			return String.valueOf(s);
		}
	}
	private static String convertToString(Object s) {
		if (s == null) {
			return "";
		} else {
			return s.toString();
		}
	}
	@Override
	public String toString() {
		String strMat = this.toList().stream().collect(Collectors.joining("|"));		
		return strMat.replaceAll("null", "");	}
	
	
}
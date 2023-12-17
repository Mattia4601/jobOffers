package jobOffers;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Consultant {
	private String consultantName;
	private List<String> consultantSkills = new LinkedList<>();
	
	// constructor
	public Consultant(String consultantName, List<String> consultantSkills) {
		super();
		this.consultantName=consultantName;
		this.consultantSkills=consultantSkills;
	}

	public String getConsultantName() {
		return consultantName;
	}

	public void setConsultantName(String consultantName) {
		this.consultantName = consultantName;
	}

	public List<String> getConsultantSkills() {
		return consultantSkills;
	}

	public void setConsultantSkills(List<String> consultantSkills) {
		this.consultantSkills = consultantSkills;
	}
	
	// this method must check if the consultant's skills match a set of skills
	public boolean checkConsultantSkills(Set<String> skills) {
		
		for (String skill : skills ) {
			if (!this.consultantSkills.contains(skill)) {
				return false;
			}
		}
		
		return true;
	}
}

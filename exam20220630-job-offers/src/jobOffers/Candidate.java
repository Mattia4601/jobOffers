package jobOffers;

import java.util.*;

public class Candidate {
	private String candidateName;
	private Set<String> skills = new HashSet<>();
	
	public Candidate(String candidateName, Set<String> skills) {
		super();
		this.candidateName = candidateName;
		this.skills = skills;
		
	}
	
	public String getCandidateName() {
		return this.candidateName;
	}
	
	public void setCandidateName(String candidateName) {
		this.candidateName = candidateName;
	}

	public Set<String> getSkills() {
		return skills;
	}

	public void setSkills(Set<String> skills) {
		this.skills = skills;
	}
	
	//this method check if the candidate matches all the required skills
	public boolean checkCandidateSkills(List<String> skills) {
		for (String skill : skills) {
			if (! this.skills.contains(skill)) {
				return false;
			}
		}
		return true;
	}
}

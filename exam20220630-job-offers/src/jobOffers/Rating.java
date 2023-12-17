package jobOffers;

import java.util.Map.Entry;
import java.util.TreeMap;

public class Rating {
	private String consultantName;
	private String candidateName;
	private TreeMap<String,Integer> skillRatings = new TreeMap<>();
	
	// constructor
	public Rating(String consultantName, String candidateName, TreeMap<String, Integer> skillRatings) {
		super();
		this.consultantName = consultantName;
		this.candidateName = candidateName;
		this.skillRatings = skillRatings;
	}

	public String getConsultantName() {
		return consultantName;
	}

	public void setConsultantName(String consultantName) {
		this.consultantName = consultantName;
	}

	public String getCandidateName() {
		return candidateName;
	}

	public void setCandidateName(String candidateName) {
		this.candidateName = candidateName;
	}

	public TreeMap<String, Integer> getSkillRatings() {
		return skillRatings;
	}

	public void setSkillRatings(TreeMap<String, Integer> skillRatings) {
		this.skillRatings = skillRatings;
	}
	
	// this method compute the rating average
	public double getRAtingAvg() {
		int totalSkills = this.skillRatings.size();
		int ratingsSum = this.skillRatings.values().stream()
				.mapToInt(Integer::intValue).sum();
		
		return ratingsSum/totalSkills;
	}
	
	// this method should check if the candidate's skill ratings match the required ratings for a position skills
	public boolean checkCompatibleRatings(TreeMap<String,Integer> skillsLevels) {
		
		if (this.skillRatings == null) {
			return false; // the candidate has no ratings at all
		}
		
		for (Entry<String, Integer> entry : skillsLevels.entrySet()) {
			int reqRate = entry.getValue();
			String reqSkill = entry.getKey();
			
			if(!this.skillRatings.containsKey(reqSkill)) {
				return false; // the candidate doesn't have a rating for a required skill
			}
			else if (this.skillRatings.get(reqSkill) < reqRate ) {
				return false; // the candidate rate for the required skill is too low
			}
		}
		return true;
	}
}

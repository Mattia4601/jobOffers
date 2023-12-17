package jobOffers;

import java.util.*;
import java.util.stream.Collectors;


public class Position {
	private String positionName; 
	TreeMap<String,Integer> skillsLevels = null; //map to associate a level to each skill of the position
	
	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public TreeMap<String, Integer> getSkillsLevels() {
		return skillsLevels;
	}

	public void setSkillsLevels(TreeMap<String, Integer> skillsLevels) {
		this.skillsLevels = skillsLevels;
	}

	// constructor
	public Position(String positionName, TreeMap<String, Integer> skillsLevels) {
		super();
		this.positionName = positionName;
		this.skillsLevels = skillsLevels;
	}
	
	// this method returns the average of the requested levels
	public double getAvg() {
		int sumLevels = skillsLevels.values().stream()
				.mapToInt(v->v).sum();
		return sumLevels / skillsLevels.size();
	}
	
	// this method returns all the skills you need for this position
	public List<String> getRequiredSkills(){
		return this.skillsLevels.keySet().stream()
				.collect(Collectors.toList());
	}
}

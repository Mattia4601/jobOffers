package jobOffers; 
import java.util.*;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

public class JobOffers  {

	// sorted set containing all the skills so NO DUPLICATED SKILLS
	SortedSet<String> skills = new TreeSet<>();
	// collection for the possible positions -> map: key=positionName value=Position object
	TreeMap<String,Position> positionsColl = new TreeMap<>();
	// candidate collection -> map: key candidate's name, value candidate object 
	TreeMap<String,Candidate> candidatesColl = new TreeMap<>();
	// list of applications
	LinkedList<Application> applicationsList = new LinkedList<>();
	// consultant collection -> map: key=consultantName, value:consultantObject
	TreeMap<String,Consultant> consultantColl = new TreeMap<>();
	// ratings collection -> map: key=candidateName, value Rating object
	TreeMap<String,Rating> ratingsColl = new TreeMap<>();
	
	
//R1
	// this method adds certains skills to the set of skills, returns the set size after adding the new skills
	public int addSkills (String... skills) {
		
		for (String skill : skills) {
			this.skills.add(skill);
		}
		
		return this.skills.size();
	}
	// insert a new position with the required skills of knowledge, throws exceptions in three different cases:
	// if the position already exist and has been entered
	// if one or more skills have not been entered
	// if the level of the skill is not between 4 and 8
	public int addPosition (String position, String... skillLevels) throws JOException {
		// check if the position has been already entered
		if (this.positionsColl.containsKey(position)) {
			throw new JOException("This position has already been entered!");
		}
		TreeMap<String, Integer> skill_Levels = new TreeMap<>();
		
		for (String skillLevel : skillLevels) {
			String[] fields = skillLevel.split(":");  //the skillLevel format is like "Java:7"
			int level = Integer.parseInt(fields[1]);
			String skillName = fields[0];
			
			// check if the skill has been entered
			if (! this.skills.contains(skillName)) {
				throw new JOException("Skill not entered!");
			}
			
			// check if the level is not between 4 and 8
			if (level < 4 || level > 8 ) {
				throw new JOException("Level not between 4 and 8!");
			}
			skill_Levels.put(skillName, level);
			
		}
		Position pos = new Position(position, skill_Levels);
		this.positionsColl.put(position, pos);
		return (int) pos.getAvg();
	}
	
//R2	
	// this method inserts a new candidate 
	// throws an exception if the candidate has already been entered or if a skill has not been entered
	// the result is the number of skills
	public int addCandidate (String name, String... skills) throws JOException {
		
		// check if the candidate has already been entered in the collection
		if (this.candidatesColl.containsKey(name)) {
			throw new JOException("The candidate has already been entered!");
		}
		
		// create our skillSet
		Set<String> skillsSet = new HashSet<>();
		
		for (String skill : skills) {
			if (!this.skills.contains(skill)) {
				throw new JOException("Skill hasn't been entered yet!");
			}
			
			skillsSet.add(skill);
		}
		Candidate candidate = new Candidate(name, skillsSet);
		this.candidatesColl.put(name, candidate);
		return skills.length;
	}
	// the method allows a candidate to apply for one or more positions
	// throws an exception if the candidate has not been entered or 
	// if the candidate doesn't have all the required skills or
	// if not all the positions have been entered
	// it returns the list of applications, an application is a string consisting 
	// of the candidate's name followed by ":" and the name of the position
	// strings are sorted by candidate and by positions
	public List<String> addApplications (String candidate, String... positions) throws JOException {
		
		if (!this.candidatesColl.containsKey(candidate))
			throw new JOException("The candidate has not been entered!");
		
		Candidate c = this.candidatesColl.get(candidate);
		for (String pos : positions) {
			// check if the position has already been entered
			if (! this.positionsColl.containsKey(pos)) {
				throw new JOException("Position hasn't been entered yet!");
			}
			Position posObj=this.positionsColl.get(pos);
			// check if the candidate has all the required skill for the position
			boolean flag = c.checkCandidateSkills(posObj.getRequiredSkills());
			if (!flag) {
				throw new JOException("Candidate doesn't match the required skills for this position!");
			}
			
			// add the new application to the list of applications
			this.applicationsList.add(new Application(candidate,pos));
		}
		// creating the output
		List<String> list = new ArrayList<>();
		for (String position: positions) {
			list.add(candidate + ":" + position);
		}
		Collections.sort(list);
		return list;
	} 
//	Method getCandidatesForPositions() gives a map whose keys are the
//	names of the positions in alphabetical order; the values are the ordered
//	lists of the candidates that applied to each position. Positions without
//	candidates are ignored.
	public TreeMap<String, List<String>> getCandidatesForPositions() {
		return this.applicationsList.stream()
				.filter(a->a.getCandidateName()!=null)
				.sorted(Comparator.comparing(Application::getCandidateName))
				.collect(groupingBy(a->a.getPosition(), TreeMap::new,
						Collectors.mapping(a->a.getCandidateName(),Collectors.toList())));
	}
	
	
//R3
	// the method inserts the name of a consultant with the list of his skills
	// the method throws an exception if the consultant has already been entered
	// or a skill has not been entered
	// the result is the number of skills
	public int addConsultant (String name, String... skills) throws JOException {
		
		// check if the consultant has already been entered
		if (this.consultantColl.containsKey(name)) {
			throw new JOException("consultant has already been entered!");
		}
		
		// creating a new list of skills
		List<String> consulSkills = new LinkedList<>();
		
		for (String skill : skills) {
			// check if the skill has already been entered
			if (!this.skills.contains(skill)) {
				throw new JOException("Skill has not already been entered!");
			}
			// adding the skill to the list
			consulSkills.add(skill);
		}
		
		// creating a new consultant object
		Consultant c = new Consultant(name, consulSkills);
		// adding the new consultant object to the consultant collection
		this.consultantColl.put(name, c);
		// return the number of skills
		return skills.length;
	}
	
//	allows a consultant to assign a rating to
//	each of the skills indicated by the candidate.
//	An example of skillRating
//	is as follows:["java:8", "databases:6"]. Each string is made up
//	of the skill followed by a colon and the rating. The rating is between 4
//	and 10 inclusive. The method throws an exception if the consultant or
//	the candidate has not been entered, the consultant's skills do not
//	include all those declared by the candidate, there are skills with
//	out-of-range ratings (e.g. java:11). The result of the method is the
//	average rating.
	public Integer addRatings (String consultant, String candidate, String... skillRatings)  throws JOException {
		
		// check if the consultant has not been entered
		if (!this.consultantColl.containsKey(consultant)) {
			throw new JOException("The consultant has not been entered!");
		}
		
		// check if the candidate has not been entered
		if (!this.candidatesColl.containsKey(candidate)) {
			throw new JOException("The candidate has not been entered!");
		}
		
		// getting the candidate
		Candidate cand = this.candidatesColl.get(candidate);
		
		// check if the consultant's skills match all the candidate's ones
		Consultant cons = this.consultantColl.get(consultant);
		if (!cons.checkConsultantSkills(cand.getSkills())) {
			throw new JOException("The consultant does not match all the candidate's skills!");
		}
		
		TreeMap<String,Integer> skillRatingsMap = new TreeMap<>();
		// for each skill we create a new entry for the skillRatingsMap
		for (String skillRating : skillRatings) {
			String[] fields = skillRating.split(":");
			int rate = Integer.parseInt(fields[1]);
			String skillName = fields[0];
			
			// check if the skill rate is out of range
			if (rate<4 || rate>10) {
				throw new JOException("Skill rate out of range!");
			}
			
			skillRatingsMap.put(skillName, rate);
		
		}
		
		// ratings collection -> map: key=candidateName, value Rating object
		// we create a new entry and we add it to our collection
		Rating ratingObj=new Rating(consultant,candidate,skillRatingsMap);
		this.ratingsColl.put(candidate, ratingObj);
		
		return (int) ratingObj.getRAtingAvg();
	}
	
//R4
	/*
	Method discardApplications() discards the applications of ineligible
	candidates. A candidate is ineligible for a position if one or more of
	his/her skills have ratings below the levels required by the position.
	The skill ratings are those added through method addRatings(). The
	method gives the list of discarded applications; an application is
	indicated by the name of the candidate followed by ":" and the name
	of the position. The list is sorted alphabetically by candidates and by
	positions. The list can be empty.
	*/
	public List<String> discardApplications() {
		
		List<String> discardedApp = new LinkedList<>();
		
		for (Application app : this.applicationsList) {
			// for each application get the candidate and the position to which he wants to apply
			String candName=app.getCandidateName(), posName=app.getPosition();
			Candidate candObj = this.candidatesColl.get(candName);
			Position posObj = this.positionsColl.get(posName);
			//get the rating object associated to the candidate 
			Rating ratingObj=this.ratingsColl.get(candName);
			
			if (ratingObj==null) { //no ratings for this candidate available!!
				//this application must be discarded!
				String newDiscApp=candName+":"+posName;
				discardedApp.add(newDiscApp);
				continue;
			}
				
			
			//check if the candidate has the appropriate level for the skills required for the position 
			if (! ratingObj.checkCompatibleRatings(posObj.getSkillsLevels())  ) {
				//this application must be discarded!
				String newDiscApp=candName+":"+posName;
				discardedApp.add(newDiscApp);
			}
		}
		// we must order the list alphabetically
		Collections.sort(discardedApp);
		return discardedApp;
	}
	/*
	Method getEligibleCandidates(String position) defines the eligible
	candidates for a given position. The candidates must have applied for
	the indicated position, their skills must include those related to the
	position; furthermore the ratings of the skills (provided by
	consultants) must be no lower than the levels defined with method
	addPosition(). The result is a list of eligible candidates in
	alphabetical order; the list may be empty. 
	*/
	public List<String> getEligibleCandidates(String position) {
		
		List<String> eligibleCand = new LinkedList<>();
		Position posObj = this.positionsColl.get(position);
		
		// getting the position skillRatingsMap
		TreeMap<String,Integer> skillLevelsMap = posObj.getSkillsLevels();
		
		//getting the list of applications for the position given by argument
		List<Application> appForPos = this.applicationsList.stream()
		.filter(a->a.getPosition()==position).toList();
		
		for (Application app : appForPos) {
			String candName = app.getCandidateName();
			Rating ratingObj = this.ratingsColl.get(candName);
			if(ratingObj.checkCompatibleRatings(skillLevelsMap)) {
				// candidate is eligible for that position
				eligibleCand.add(candName);
			}
		}
		Collections.sort(eligibleCand);
		return eligibleCand;
	}
	

	
}

		

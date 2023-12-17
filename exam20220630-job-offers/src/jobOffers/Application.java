package jobOffers;

public class Application {
	private String candidateName;
	private String position;
	public Application(String candidateName, String position) {
		super();
		this.candidateName = candidateName;
		this.position = position;
	}
	public String getCandidateName() {
		return candidateName;
	}
	public void setCandidateName(String candidateName) {
		this.candidateName = candidateName;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}

}

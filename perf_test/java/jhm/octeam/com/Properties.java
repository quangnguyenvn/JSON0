package jhm.octeam.com;

public class Properties {

	public Properties(String food, String friends, String university, String demography, String secret) {
		super();
		this.favoriteFood = food;
		this.friends = friends;
		this.university = university;
		this.demography = demography;
		this.secret = secret;
	}

	private String favoriteFood;
	private String friends;
	private String university;
	private String demography;
	private String secret;
	
	public String getFavoriteFood() {
		return favoriteFood;
	}

	public void setFavoriteFood(String favoriteFood) {
		this.favoriteFood = favoriteFood;
	}
	
	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
	}

	public String getFriends() {
		return friends;
	}

	public void setFriends(String friends) {
		this.friends = friends;
	}

	public String getDemography() {
		return demography;
	}

	public void setDemography(String demography) {
		this.demography = demography;
	}


}

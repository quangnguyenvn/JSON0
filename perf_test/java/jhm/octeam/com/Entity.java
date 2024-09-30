package jhm.octeam.com;

public class Entity {
	private String code;
	private String name;
	private String address;
	private String idNumber;
	private String job;
	private String finance;
	private Properties properties;

	public Entity(String code, String name, String address, String idNumber, String job, String finance, Properties properties) {
		super();
		this.code = code;
		this.name = name;
		this.address = address;
		this.idNumber = idNumber;
		this.job = job;
		this.finance = finance;
		this.properties = properties;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getFinance() {
		return finance;
	}

	public void setFinance(String finance) {
		this.finance = finance;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

}

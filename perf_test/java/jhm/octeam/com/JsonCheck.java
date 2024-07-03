package jhm.octeam.com;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.octeam.json_quill.JSON;

import flexjson.JSONSerializer;


public class JsonCheck {
	private Entity entity;
	private Properties properties;
	private Faker faker;
	
	private Entity buildEntity() {
		// properties
		faker = new Faker();
		String shushi = faker.food().sushi();
		String friend = faker.friends().character();
		String university = faker.university().name();
		String demography = faker.demographic().demonym();
		String secret = faker.backToTheFuture().character();
		properties = new Properties(shushi,friend,university,demography,secret);
		
		// entity
		String code = faker.code().gtin13();
		String name = faker.name().nameWithMiddle();
		String address = faker.address().fullAddress();
		String idNumber = faker.idNumber().ssnValid();
		String job = faker.job().keySkills();
		String finance = faker.finance().creditCard();
		entity = new Entity(code, name, address, idNumber, job, finance, properties);
		
		return entity;
	}
	
	public void checkFormat() {
		entity = buildEntity();
		properties = entity.getProperties();
		
		// Check format with Gson
		Gson gson = new Gson();
		String json = gson.toJson(entity);
		System.out.println(json);
		
		// Check format with FlexJson
		JSONSerializer serializer = new JSONSerializer().prettyPrint(true);
		String jsonStr = serializer.serialize(entity);
		System.out.println(jsonStr);
		
		// Check format with JSonSimple
		JSONObject jsonObjectChild = new JSONObject();
		jsonObjectChild.put("food", properties.getFavoriteFood());
		jsonObjectChild.put("friends", properties.getFriends());
		jsonObjectChild.put("university", properties.getUniversity());
		jsonObjectChild.put("demography", properties.getDemography());
		jsonObjectChild.put("secret", properties.getSecret());
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", entity.getCode());
		jsonObject.put("name", entity.getName());
		jsonObject.put("address", entity.getAddress());
		jsonObject.put("idNumber", entity.getIdNumber());
		jsonObject.put("job", entity.getJob());
		jsonObject.put("finance", entity.getFinance());
		jsonObject.put("properties", jsonObjectChild.toJSONString());
		System.out.println(jsonObject.toString());
		
		// Check format with Json quill
		JSON json0Child = new JSON();
		json0Child.put("food", properties.getFavoriteFood());
		json0Child.put("friends", properties.getFriends());
		json0Child.put("university", properties.getUniversity());
		json0Child.put("demography", properties.getDemography());
		json0Child.put("secret", properties.getSecret());
		
		JSON json0 = new JSON();
		json0.put("code", entity.getCode());
		json0.put("name", entity.getName());
		json0.put("address", entity.getAddress());
		json0.put("idNumber", entity.getIdNumber());
		json0.put("job", entity.getJob());
		json0.put("finance", entity.getFinance());
		json0.put("properties", json0Child.toString());
		System.out.println(json0.toString());
		
		// Check format with Jackson
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode objectDetailsChild = objectMapper.createObjectNode();
		objectDetailsChild.put("food", properties.getFavoriteFood());
		objectDetailsChild.put("friends", properties.getFriends());
		objectDetailsChild.put("university", properties.getUniversity());
		objectDetailsChild.put("demography", properties.getDemography());
		objectDetailsChild.put("secret", properties.getSecret());
		
		ObjectNode objectDetails = objectMapper.createObjectNode();
		objectDetails.put("code", entity.getCode());
		objectDetails.put("name", entity.getName());
		objectDetails.put("address", entity.getAddress());
		objectDetails.put("idNumber", entity.getIdNumber());
		objectDetails.put("job", entity.getJob());
		objectDetails.put("finance", entity.getFinance());
		objectDetails.put("properties", objectDetailsChild.toString());
		System.out.println(objectDetails.toString());
	}
}

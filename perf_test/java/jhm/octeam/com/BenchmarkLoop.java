package jhm.octeam.com;

import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.octeam.json_quill.JSON;

import flexjson.JSONSerializer;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 1, jvmArgs = { "-Xms2G", "-Xmx2G" })
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@State(Scope.Benchmark)
public class BenchmarkLoop {

	@Param({ "10000000" })
	private int N;
	
	private Faker faker;
	private Entity entity = fakeEntity();
	private Properties properties = entity.getProperties();

	private Entity fakeEntity() {
		// properties
		faker = new Faker();
		String shushi = faker.food().sushi();
		String friend = faker.friends().character();
		String university = faker.university().name();
		String demography = faker.demographic().demonym();
		String secret = faker.backToTheFuture().character();
		properties = new Properties(shushi, friend, university, demography, secret);

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

	public static void main(String[] args) throws RunnerException {

		Options opt = new OptionsBuilder().include(BenchmarkLoop.class.getSimpleName()).forks(1).build();

		new Runner(opt).run();
	}

	private String createFlexJson() {
		JSONSerializer serializer = new JSONSerializer().prettyPrint(true);
		String jsonStr = serializer.serialize(entity);
		return jsonStr;

	}

	private String createJSONSimple() {	
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
		
		return jsonObject.toString();
	}

	private String createJSON() {		
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

		return json0.toString();

	}

	private String createJackson() {
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

		return objectDetails.toString();
	}

	private String createGson() {
		Gson gson = new Gson();
		String json = gson.toJson(entity);
		return json;
	}

	@Benchmark
	public void loopForGson(Blackhole bh) {

		for (int i = 0; i < N; i++) {
			String s = createGson();
			bh.consume(s);
		}
	}

	@Benchmark
	public void loopForFlexJson(Blackhole bh) {

		for (int i = 0; i < N; i++) {
			String s = createFlexJson();
			bh.consume(s);
		}
	}

	@Benchmark
	public void loopForJackson(Blackhole bh) {

		for (int i = 0; i < N; i++) {
			String s = createJackson();
			bh.consume(s);
		}
	}

	@Benchmark
	public void loopForJquill(Blackhole bh) {

		for (int i = 0; i < N; i++) {
			String s = createJSON();
			bh.consume(s);
		}
	}

	@Benchmark
	public void loopForJSONSimple(Blackhole bh) {

		for (int i = 0; i < N; i++) {
			String s = createJSONSimple();
			bh.consume(s);
		}
	}

}
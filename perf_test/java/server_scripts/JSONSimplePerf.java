import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class JSONSimplePerf {

    private byte[] buf;
    private int pos;

    public JSONSimplePerf() {
        buf = new byte[1 << 29];
        pos = 0;
    }

    public static void main(String[] args) {
        int num = (args.length > 0)?Integer.parseInt(args[0]):0;
        JSONSimplePerf perf = new JSONSimplePerf();
        perf.execute(num);
    }

    private void produceUsingJSONSimple(int size) {

        long startTimestamp = System.currentTimeMillis();

        for (int i = 0; i < size; i++) {
            JSONObject json = new JSONObject();

            json.put("ID",  i);
            json.put("Method", "JSON");
            json.put("Name", "Parent");
            json.put("Message", "Hello World");
	
            JSONObject property = new JSONObject();
            property.put("Key", "Key");
            property.put("Value", "Value");
            json.put("Property", property);

            JSONArray childs = new JSONArray();

            JSONObject child1 = new JSONObject();
            child1.put("ID", "child1");
            child1.put("Name", "child1");

            childs.add(child1);

            JSONObject child2 = new JSONObject();
            child2.put("ID", "child2");
            child2.put("Name", "child2");

            childs.add(child2);

            json.put("Childs", childs);

	    byte[] bytes = json.toJSONString().getBytes();
            System.arraycopy(bytes, 0, buf, pos, bytes.length);
            pos += bytes.length;

            if (pos >= buf.length - 1024) pos = 0;
        }

        long endTimestamp = System.currentTimeMillis();
        System.out.println("Produce " + size + " output using JSONSimple in " + (endTimestamp - startTimestamp) + " ms");
    }

    public final void execute(int num) {
        produceUsingJSONSimple(1 << 10);
        while(true) {
            produceUsingJSONSimple(1 << 20);
            num--;
            if (num == 0) break;
        }
    }
}

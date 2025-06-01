import pineteam.json.JSON0;

public class JSON0Perf {

    private byte[] buf;
    private int pos;

    public JSON0Perf() {
        buf = new byte[1 << 29];
        pos = 0;
    }

    public static void main(String[] args) {
        int num = (args.length > 0)?Integer.parseInt(args[0]):0;
        JSON0Perf perf = new JSON0Perf();
        perf.execute(num);
    }

    private void produceUsingJSON0(int size) {

        JSON0 json = new JSON0();

        long startTimestamp = System.currentTimeMillis();

        for (int i = 0; i < size; i++) {
            json.reset();
            json.put("ID",  i);
            json.put("Method", "JSON");
            json.put("Message", "Hello World");

            JSON0 property = json.createJSON("Property");
            property.put("Key", "Key");
            property.put("Value", "Value");

            JSON0 childs = json.createJSONArray("Childs");
            JSON0 child1 = childs.createJSON("Child1");
            child1.put("ID", "child1");
            child1.put("Name", "child1");

            JSON0 child2 = childs.createJSON("Child2");
            child2.put("ID", "child2");
            child2.put("Name", "child2");

            pos += json.toBytes(buf, pos);
            if (pos > buf.length - 1024) pos = 0;
        }

        long endTimestamp = System.currentTimeMillis();
        System.out.println("Produce " + size + " output using JSON0 in " + (endTimestamp - startTimestamp) + " ms");
    }
   
    public final void execute(int num) {

        produceUsingJSON0(1 << 10);

        while(true) {
            produceUsingJSON0(1 << 20);
            num--;
            if (num == 0) break;
        }
    }	
}

package octeam.core.json;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class JSON0 {
    
    private static final int DEFAULT_FIELDS = 512;
    
    private static final Charset DEFAULT_CHAR_SET = StandardCharsets.US_ASCII;
    
    private static final byte JSON_OPEN = (byte) '{';
    private static final byte JSON_CLOSE = (byte) '}';
    private static final byte JSON_ARRAY_OPEN = (byte) '[';
    private static final byte JSON_ARRAY_CLOSE = (byte) ']';
    private static final byte JSON_VALUE_DELIM = (byte) ':';
    private static final byte JSON_FIELD_DELIM = (byte) ',';
    private static final byte JSON_VALUE_QUOTE = (byte) '\"';

    private Charset charset;

    private boolean isArray;

    private Elements data;
    
    private int head;
    
    private int tail;
    
    private int numItems;
    
    private JSON0(JSON0 json) {
	isArray = false;
	data = json.getData();
	charset = json.getCharset();
	head = -1;
	numItems = 0;
    }
    
    public JSON0() {
	isArray = false;
	init(DEFAULT_FIELDS);
    }
    
    public JSON0(int size) {
	isArray = false;
	init(size);
    }
    
    public JSON0(int size, int bufSize) {
	isArray = false;
        init(size);
	data.initBuf(bufSize);
    }
        
    private static boolean isSpace(byte val) {
	return (val <= (byte) ' ') && (val == (byte) ' ' || val == (byte) '\n' || val == (byte) '\r'|| val == (byte) '\t');
    }

    private static int copy(String value, byte[] buf, int offset, Charset charset) {
	byte[] array = value.getBytes(charset);
	
	System.arraycopy(array, 0, buf, offset, array.length);
	
	offset += array.length;
	
	return offset;
    }
    
    private static int copyKey(String key, byte[] buf, int offset) {
	buf[offset++] = JSON_VALUE_QUOTE;
	offset = copy(key, buf, offset, DEFAULT_CHAR_SET);
	buf[offset++] = JSON_VALUE_QUOTE;
	buf[offset++] = JSON_VALUE_DELIM;
	return offset;
    }
    
    public static final void copy(JSON0 src, JSON0 dest) {
	
	Element element = src.getHead();
	
	int numElements = src.getLength();
	
	for (int i = 0; i < numElements; i++) {
	    
	    String key = element.key;
            Object value = element.value;
	    
	    element = src.next(element);

            if (value instanceof JSON0) {

		JSON0 jsonVal = (JSON0) value;
		JSON0 json;
		
		if (jsonVal.isArray()) {
		    json = dest.createJSONArray(key);
		} else {
		    json = dest.createJSON(key);
		} 
		copy(jsonVal, json);

	    } else if (value instanceof String) {
                dest.put(key, (String) value);
            } else {
                dest.put(key, value);
            }
	}
    }
    
    public static final JSON0 clone(JSON0 src) {
	JSON0 json = new JSON0(src.getDataLength());
	json.isArray(src.isArray());
	JSON0.copy(src, json);
	return json;
    }
    
    private void init(int size) {
        data = new Elements(size);
        head = -1;
        numItems = 0;
        charset = DEFAULT_CHAR_SET;
    }

    private int copyValue(Object val, byte[] buf, int offset) {
	if (val instanceof JSON0) {
	    JSON0 json = (JSON0) val;
	    if (json.isArray()) {
		buf[offset++] = JSON_ARRAY_OPEN;
		offset += json.toBytes(buf, offset, false);
		buf[offset++] = JSON_ARRAY_CLOSE;
	    } else {
		offset += json.toBytes(buf, offset);
	    }
	} else {
	    if (val instanceof String) {
		buf[offset++] = JSON_VALUE_QUOTE;
		offset = copy((String) val, buf, offset, charset);
		buf[offset++] = JSON_VALUE_QUOTE;
	    } else if (val instanceof Character) {
		buf[offset++] = JSON_VALUE_QUOTE;                
                buf[offset++] = (byte) ((char) val);
                buf[offset++] = JSON_VALUE_QUOTE;
	    } else {
		offset = copy(String.valueOf(val), buf, offset, charset);
	    }
	}
	return offset;
    }
    
    private String getString(byte[] buf, int startPos, int endPos) {
        while (isSpace(buf[startPos])) {
            startPos++;
        }

        while (isSpace(buf[--endPos])) {
        }

        if (buf[startPos] == JSON_VALUE_QUOTE) {
            return new String(buf, startPos + 1, endPos - startPos - 1, charset);
        } else {
            return new String(buf, startPos, endPos + 1 - startPos, charset);
        }
    }

    public final Elements getData() {
	return data;
    }

    public final Element[] getElements() {
	return data.getElements();
    }
    
    public final boolean isArray() {
	return isArray; 
    }
    
    public final void isArray(boolean isArray) {
	this.isArray = isArray;
    }

    public final void reset() {
	if (head == 0) {
	    data.reset();
	}
	head = -1;
	numItems = 0;
    }
    
    public final void initMap() {
	data.initMap();
    }

    public final void put(String key, Object value) {
	
	if (isArray) key = null;

	int index = data.put(key, value, head, tail);
	
     	if (head == -1) {
	    head = index;
	} 
	
	tail = index;
	numItems++;
    }
    
    public final void setCharset(Charset charset) {
	this.charset = charset;
    }

    public final Charset getCharset() {
	return charset;
    }

    public final void put(Object value) {
	put(null, value);
    }
    
    public final Object get(String key) {
	return data.get(key, head, numItems);
    }
    
    public final String getString(String key) {
        Object object = get(key);
        if (object == null) return null;
	return object.toString();
    }

    public final JSON0 getArray(String key) {
	
        Object child = get(key);
	
	if (child == null) {
	    JSON0 json = new JSON0(0);
	    json.isArray(true);
	    return json;
	}
	
	if (child instanceof JSON0) {
            JSON0 json = (JSON0) child;
            if (json.isArray()) {
                return json;
            }
        }
	
        JSON0 json = new JSON0(1);
	json.isArray(true);
        json.put(child);
        return json;
    }

    public final JSON0 createJSON(String key) {
	JSON0 json = new JSON0(this);
	put(key, json);
	return json;
    }
    
    public final JSON0 createJSON() {
        return createJSON(null);
    }

    public final JSON0 createJSONArray(String key) {
        JSON0 json = new JSON0(this);
	json.isArray(true);
	put(key, json);
        return json;
    }

    private int toBytes(byte[] buf, int offset, boolean isHeaderIncluded) {
	int startPos = offset;
	
	if (isHeaderIncluded) {
	    if (isArray) {
		buf[offset++] = JSON_ARRAY_OPEN;
	    } else {
		buf[offset++] = JSON_OPEN;
	    }
	}

	int pos = head;
	
	Element[] elements = data.getElements();

	for (int i = 0; i < numItems; i++) {
	    Element element = elements[pos];
	    pos = element.next;
	    if (element.value == null) continue;
	    
	    if (i > 0) {
		buf[offset++] = JSON_FIELD_DELIM;
	    } 
	    
	    if (element.key != null) {
		offset = copyKey(element.key, buf, offset);
	    }
	    
	    offset = copyValue(element.value, buf, offset);
	}
	if (isHeaderIncluded) {
	    if (isArray) {
		buf[offset++] = JSON_ARRAY_CLOSE;
	    } else {
		buf[offset++] = JSON_CLOSE;
	    }
	}
	return offset - startPos;
    }
    
    private int findNextDelim(byte[] buf, int startPos, int endPos, byte delim) {
	for (int i = startPos; i < endPos; i++) {
	    if (buf[i] == delim) return i;
	}
	return -1;
    }
    
    private int findEndValuePos(byte[] buf, int startPos, int endPos) {
	if (buf[startPos] == JSON_OPEN) {
	    return findValuePos(buf, startPos, endPos, JSON_OPEN, JSON_CLOSE);
	} else if (buf[startPos] == JSON_ARRAY_OPEN) {
	    return findValuePos(buf, startPos, endPos, JSON_ARRAY_OPEN, JSON_ARRAY_CLOSE);
	} else {
	    
	    boolean isOutsideQuotationMark = true;
	    
	    for (int i = startPos; i < endPos; i++) {
		
		if (buf[i] == JSON_VALUE_QUOTE && (i == startPos || (i > startPos && buf[i - 1] != (byte) '\\'))) {
		    isOutsideQuotationMark = !isOutsideQuotationMark;
		    continue;
		}

		if (isOutsideQuotationMark && (buf[i] == JSON_FIELD_DELIM || 
					       buf[i] == JSON_CLOSE || buf[i] == JSON_ARRAY_CLOSE)) return i;
	    }    
	    return -1;
	}
    }

    private int findValuePos(byte[] buf, int startPos, int endPos, byte open, byte close) {
	int counter = 1;
	for (int i = startPos + 1; i < endPos; i++) {
	    if (buf[i] == close) {
		counter--;
	    } else if (buf[i] == open) {
		counter++;
	    }
	    if (counter == 0) {
		return (i + 1);
	    }
	}
	return -1;
    }
        
    private boolean parseArray(byte[] buf, int offset, int length) {
	int startPos = offset;
        int endPos = offset + length;

	while (true) {
	    startPos++;
	    
	    while (startPos < endPos && isSpace(buf[startPos])) {
                startPos++;
            }

	    if (startPos >= endPos - 1) return true;
	    
	    int pos = findEndValuePos(buf, startPos, endPos);
	    if (pos < 0) return false;
	    
	    if (buf[startPos] == JSON_OPEN) {
		JSON0 jsonChild = createJSON(null);
		if (!jsonChild.parse(buf, startPos, pos - startPos)) {
		    return false;
		}
	    } else if (buf[startPos] == JSON_ARRAY_OPEN) {
		JSON0 jsonChild = createJSONArray(null);
		if (!jsonChild.parse(buf, startPos, pos - startPos)) {
		    return false;
		}
	    } else {
		put(null, getString(buf, startPos, pos));
	    }
	    startPos = pos;
	}
    }

    private boolean parseJSON(byte[] buf, int offset, int length) {
	int startPos = offset;
	int endPos = offset + length;
	
	while (true) {
	    startPos++;
	    
	    while (startPos < endPos && isSpace(buf[startPos])) {
		startPos++;
	    }

	    if (startPos >= endPos - 1) {
		return true;
	    }

	    int pos = findNextDelim(buf, startPos, endPos, JSON_VALUE_DELIM); 
	    if (pos < 0) return false;
	    	    
	    String key = getString(buf, startPos, pos);
	    startPos = pos + 1;
	    
	    while (startPos < endPos && isSpace(buf[startPos])) {
                startPos++;
            }

	    pos = findEndValuePos(buf, startPos, endPos);
	    if (pos < 0) {
		return false;
	    }

	    if (buf[startPos] == JSON_OPEN) {
		JSON0 jsonChild = createJSON(key);
		if (!jsonChild.parse(buf, startPos, pos - startPos)) return false;
	    } else if (buf[startPos] == JSON_ARRAY_OPEN) {
		JSON0 jsonChild = createJSONArray(key);
		if (!jsonChild.parse(buf, startPos, pos - startPos)) return false;
	    } else {
		put(key, getString(buf, startPos, pos));
	    }
	    startPos = pos;
	}
    }
    
    public final boolean parse(byte[] buf, int offset, int length) {
	int numItemsBeforeParsing = numItems;
	
	initMap();

	int endPos = offset + length;
	while (offset < endPos && isSpace(buf[offset])) {
	    offset++;
	}
	
	while (isSpace(buf[endPos - 1])) {
	    endPos--;
	}
	
	length = endPos - offset;

	if (buf[offset] == JSON_OPEN) {
	    if (!parseJSON(buf, offset, length)) {
		numItems = numItemsBeforeParsing;
		return false;
	    } 
	} else if (buf[offset] == JSON_ARRAY_OPEN) {
	    if (!parseArray(buf, offset, length)) {
		numItems = numItemsBeforeParsing;
		return false;
	    }
	    isArray = true;
	} else {
	    return false;
	}
	return true;
    }

    public final boolean parse(String buf) {
	byte[] bytes = buf.getBytes();
	return parse(bytes, 0, bytes.length);
    }

    public final int getLength() {
	return numItems;
    }
    
    public final int getDataLength() {
	return data.getLength();
    }

    public final Element getHead() {
	if (numItems == 0) return null;
	return data.getElement(head);
    }
    
    public final Element next(Element element) {
	int pos = element.next;
	return data.getElement(pos);
    }
    
    public final int toBytes(byte[] buf, int offset) {
	return toBytes(buf, offset, true);
    }
    
    public final String toString(byte[] buf, int offset) {
	int length = toBytes(buf, offset);
	return new String(buf, offset, length, charset);
    }

    public final String toString() {
	while (true) {
	    try {
		byte[] buf = data.getBuf();
		return toString(buf, 0);
	    } catch (ArrayIndexOutOfBoundsException e) {
		data.doubleBufSize();
	    }
	}
    }
}
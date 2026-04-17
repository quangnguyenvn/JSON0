package pineteam.json;

import java.util.HashMap;
import java.util.Map;

class Elements {

	private static final int DEFAULT_BUF_SIZE = 1 << 12;

	private Element[] elements;
	private int length;

	private byte[] buf;

	private Map<String, Integer> keysMap;

	public Elements(int size) {
		elements = new Element[size];
		length = 0;
	}

	private void increment() {
		length++;
		int size = elements.length;

		if (length > size) {
			int newSize = (size << 1);
			Element[] newElements = new Element[newSize];
			System.arraycopy(elements, 0, newElements, 0, size);
			elements = newElements;
		}
	}

	public final void reset() {
		length = 0;
		if (keysMap != null) {
			keysMap.clear();
		}
	}

	public final void initMap(int head, int numItems) {
		if (keysMap == null) {
			keysMap = new HashMap<String, Integer>();
		}

		if (head < 0) {
			return;
		}

		int pos = head;
		for (int i = 0; i < numItems; i++) {
			Element element = elements[pos];
			if (element.key != null) {
				keysMap.put(head + element.key, pos);
			}
			pos = element.next;
		}
	}

	public final Element[] getElements() {
		return elements;
	}

	public final byte[] getBuf() {
		if (buf == null) {
			initBuf(DEFAULT_BUF_SIZE);
		}
		return buf;
	}

	public final void initBuf(int size) {
		if (size == 0)
			return;
		buf = new byte[size];
	}

	public final void doubleBufSize() {
		buf = new byte[buf.length << 1];
	}

	public final int getLength() {
		return length;
	}

	public final void setLength(int length) {
		this.length = length;
	}

	public final Map<String, Integer> snapshotKeysMap() {
		if (keysMap == null) {
			return null;
		}
		return new HashMap<String, Integer>(keysMap);
	}

	public final void restoreKeysMap(Map<String, Integer> snapshot) {
		if (snapshot == null) {
			keysMap = null;
			return;
		}

		if (keysMap == null) {
			keysMap = new HashMap<String, Integer>();
		} else {
			keysMap.clear();
		}
		keysMap.putAll(snapshot);
	}

	public final int put(String key, Object value, int head, int tail) {
		int index = length;
		increment();

		if (elements[index] == null) {
			elements[index] = new Element();
		}

		Element element = elements[index];
		element.key = key;
		element.value = value;

		if (head >= 0) {
			elements[tail].next = index;
		} else {
			head = index;
		}

		if (key != null && keysMap != null) {
			keysMap.put(head + key, index);
		}

		return index;
	}

	public final Object get(String key, int head, int length) {
		if (keysMap != null) {
			key = head + key;
			Integer index = keysMap.get(key);
			if (index == null)
				return null;
			return elements[index.intValue()].value;
		}

		int pos = head;

		for (int i = 0; i < length; i++) {
			Element element = elements[pos];

			if (key.equals(element.key)) {
				return element.value;
			}
			pos = element.next;
		}
		return null;
	}

	public final Element getElement(int index) {
		return elements[index];
	}

}

package org.mrcsparker.ceeql;

import java.util.HashMap;
import java.util.Map;

public class TestUtils {

	public static class MapBuilder<K, V> {
		 
	    private final Map<K, V> map = new HashMap<K, V>();
	 
	    public MapBuilder<K, V> put(K key, V value) {
	        map.put(key, value);
	        return this;
	    }
	 
	    public Map<K, V> build() {
	        return map;
	    }
	}
}

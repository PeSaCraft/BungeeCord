package net.md_5.bungee.util;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

public class MapUtils {

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map ) {
		return sortByValue(map, Map.Entry.<K, V>comparingByValue());
	}
	
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueReverse( Map<K, V> map ) {
		return sortByValue(map, Map.Entry.<K, V>comparingByValue().reversed());
	}
	
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, Comparator<Map.Entry<K, V>> comparator) {
		Map<K, V> result = new LinkedHashMap<K, V>();
		Stream<Map.Entry<K, V>> st = map.entrySet().stream();
	
		st.sorted( comparator )
			.forEachOrdered( e -> result.put(e.getKey(), e.getValue()) );
	
		return result;
	}	
}

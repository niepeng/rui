package com.rui.android_client.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lsb
 *
 * @date 2012-5-29 下午10:55:31
 */
public class CollectionUtil {
	public static <K, V> HashMap<K, V> newHashMap() {
		return new HashMap<K, V>();
	}

	public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap() {
		return new ConcurrentHashMap<K, V>();
	}

	public static <K, V> TreeMap<K, V> newTreeMap() {
		return new TreeMap<K, V>();
	}

	public static <E> ArrayList<E> newArrayList() {
		return new ArrayList<E>();
	}

	public static <E> HashSet<E> newHashSet() {
		return new HashSet<E>();
	}

	public static <E> Vector<E> newVector() {
		return new Vector<E>();
	}

	public static <E> ArrayList<E> newArrayList(Collection<? extends E> c) {
		return new ArrayList<E>(c);
	}

	public static <E> boolean isEmpty(Collection<E> c) {
		if (c == null || c.isEmpty()) {
			return true;
		}
		return false;
	}
	
	public static boolean isEmpty(long[] ids) {
		if(ids == null || ids.length == 0) {
			return true;
		}
		return false;
	}
	
	public static boolean isEmpty(int[] ids) {
		if(ids == null || ids.length == 0) {
			return true;
		}
		return false;
	}

	public static <K, V> String join(Map<K, V> map, String sepKV, String sepEntry) {
		if (map == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Map.Entry<K, V> en : map.entrySet()) {
			if (first) {
				first = false;
			} else {
				sb.append(sepEntry);
			}
			sb.append(en.getKey());
			sb.append(sepKV);
			sb.append(en.getValue());
		}
		return sb.toString();
	}
}


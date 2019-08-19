package com.anconet.JFindPwndHashes.workers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import org.apache.commons.lang3.StringUtils;

public class ConcurrentMapCollector
		implements Collector<String, ConcurrentMap<String, List<String>>, ConcurrentMap<String, List<String>>> {

	@Override
	public Supplier<ConcurrentMap<String, List<String>>> supplier() {
		return () -> new ConcurrentHashMap<>();
	}

	@Override
	public BiConsumer<ConcurrentMap<String, List<String>>, String> accumulator() {
		return (map, element) -> {

			int firstSeparator = element.indexOf(':');
			int lastSeparator = StringUtils.ordinalIndexOf(element, ":", 3);

			if (firstSeparator != -1 && lastSeparator != -1) {
				String key = element.substring(lastSeparator).replaceAll(":", "").toUpperCase();
				List<String> valueList = map.computeIfAbsent(key, v -> new ArrayList<String>());
				valueList.add(element.substring(0, firstSeparator));
			} else {
				System.err.printf("Skipping malformed element %s.%n", element);
			}
		};
	}

	@Override
	public BinaryOperator<ConcurrentMap<String, List<String>>> combiner() {
		return (map1, map2) -> {
			System.err.println("Not called");
			return null;
		};
	}

	@Override
	public Function<ConcurrentMap<String, List<String>>, ConcurrentMap<String, List<String>>> finisher() {
		return Function.identity();
	}

	@Override
	public Set<Characteristics> characteristics() {
		return Collections.unmodifiableSet(
				EnumSet.of(Characteristics.UNORDERED, Characteristics.IDENTITY_FINISH, Characteristics.CONCURRENT));
	}
}

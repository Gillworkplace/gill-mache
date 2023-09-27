package com.gill.mache.statistic;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

/**
 * InnerStatistics
 *
 * @author gill
 * @version 2023/09/21
 **/
@Component
public class InnerStatistics {

	private final Map<String, AtomicInteger> counts = new ConcurrentHashMap<>();

	private final Map<String, AtomicLong> sums = new ConcurrentHashMap<>();

	private final Map<String, AtomicLong> maxs = new ConcurrentHashMap<>();

	private final Map<String, AtomicLong> mins = new ConcurrentHashMap<>();

	/**
	 * 添加用例
	 * 
	 * @param methodName
	 *            方法名
	 * @param cost
	 *            耗时
	 */
	public void add(String methodName, long cost) {
		AtomicInteger cnt = counts.computeIfAbsent(methodName, key -> new AtomicInteger(0));
		cnt.incrementAndGet();
		AtomicLong sum = sums.computeIfAbsent(methodName, key -> new AtomicLong(0));
		sum.addAndGet(cost);
		AtomicLong max = maxs.computeIfAbsent(methodName, key -> new AtomicLong(cost));
		max.accumulateAndGet(cost, Math::max);
		AtomicLong min = mins.computeIfAbsent(methodName, key -> new AtomicLong(cost));
		min.accumulateAndGet(cost, Math::min);
	}

	/**
	 * 打印统计信息
	 * 
	 * @return 统计信息
	 */
	public String println() {
		return "counts: " + counts + System.lineSeparator() + "sums: " + sums + System.lineSeparator() + "maxs: " + maxs
				+ System.lineSeparator() + "mins: " + mins + System.lineSeparator();
	}
}

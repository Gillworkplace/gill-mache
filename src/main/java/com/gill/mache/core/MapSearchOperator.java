package com.gill.mache.core;

/**
 * MapSearchOperator
 *
 * @author gill
 * @version 2023/09/21
 **/
public interface MapSearchOperator {

	/**
	 * 查询操作
	 * 
	 * @param key
	 *            键
	 * @return 值
	 */
	String getStr(String key);

	/**
	 * 查询操作
	 *
	 * @param key
	 *            键
	 * @return 值
	 */
	byte[] get(String key);
}

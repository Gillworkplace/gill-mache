package com.gill.mache.core;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gill.graft.apis.CommandSerializer;
import com.gill.graft.apis.VersionDataStorage;
import com.gill.graft.model.Snapshot;
import com.gill.mache.exception.MapCommandException;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;

/**
 * MapDataStorage
 *
 * @author gill
 * @version 2023/09/21
 **/
@Component
public class MapDataStorage extends VersionDataStorage<MapCommand> implements MapSearchOperator {

	private static final Logger log = LoggerFactory.getLogger(MapDataStorage.class);

	private Map<String, byte[]> map = new ConcurrentHashMap<>(1024);

	@Autowired
	private MapCommandSerializer serializer;

	@Override
	public byte[] getSnapshotData() {
		return JSONUtil.toJsonStr(map).getBytes(StandardCharsets.UTF_8);
	}

	@Override
	public CommandSerializer<MapCommand> getCommandSerializer() {
		return serializer;
	}

	@Override
	public Object apply(MapCommand cm) {
		if (cm.getKey() == null) {
			throw new MapCommandException("key is necessary for the apply command, cm: " + cm);
		}
		if (cm.getValue() == null) {
			return map.get(cm.getKey());
		}
		return map.put(cm.getKey(), cm.getValue());
	}

	@Override
	public void saveSnapshotToFile(Snapshot snapshot) {
		log.info("ignore to persist with file");
	}

	@Override
	public void saveSnapshot(byte[] bytes) {
		map = JSONUtil.toBean(new String(bytes), new TypeReference<Map<String, byte[]>>() {
		}, true);
	}

	@Override
	public int loadSnapshot() {
		map = new ConcurrentHashMap<>(1024);
		return 0;
	}

	@Override
	public String println() {
		return JSONUtil.toJsonStr(map);
	}

	/**
	 * 查询操作
	 *
	 * @param key
	 *            键
	 * @return 值
	 */
	@Override
	public String getStr(String key) {
		return new String(map.get(key), StandardCharsets.UTF_8);
	}

	/**
	 * 查询操作
	 *
	 * @param key
	 *            键
	 * @return 值
	 */
	@Override
	public byte[] get(String key) {
		return map.get(key);
	}
}

package com.gill.mache.core;

import com.gill.graft.apis.CommandSerializer;

import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Component;

/**
 * MapCommandSerializer
 *
 * @author gill
 * @version 2023/09/21
 **/
@Component
public class MapCommandSerializer implements CommandSerializer<MapCommand> {

	@Override
	public String serialize(MapCommand mapCommand) {
		return JSONUtil.toJsonStr(mapCommand);
	}

	@Override
	public MapCommand deserialize(String s) {
		return JSONUtil.toBean(s, MapCommand.class);
	}
}

package com.gill.mache.controller;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gill.graft.Node;
import com.gill.graft.model.ProposeReply;
import com.gill.mache.core.MapCommand;
import com.gill.mache.core.MapDataStorage;

/**
 * ServerController
 *
 * @author gill
 * @version 2023/09/21
 **/
@RestController
public class ServerController {

	@Autowired
	private Node node;

	@Autowired
	private MapDataStorage dataStorage;

	@GetMapping("/str")
	public String getStr(@RequestParam("key") @NonNull String key) {
		return dataStorage.getStr(key);
	}

	@GetMapping("/bytes")
	public byte[] getBytes(@RequestParam("key") @NonNull String key) {
		return dataStorage.get(key);
	}

	@PutMapping("/str")
	public ProposeReply putStr(@RequestParam("key") @NonNull String key, @RequestParam("value") @NonNull String value) {
		MapCommand cm = new MapCommand(key, value.getBytes(StandardCharsets.UTF_8));
		String cmStr = dataStorage.getCommandSerializer().serialize(cm);
		return node.propose(cmStr);
	}
}

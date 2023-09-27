package com.gill.mache.core;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.gill.graft.Node;
import com.gill.graft.apis.RaftRpcService;
import com.gill.graft.apis.empty.EmptyLogStorage;
import com.gill.graft.apis.empty.EmptyMetaStorage;

/**
 * RaftNode
 *
 * @author gill
 * @version 2023/09/21
 **/
@DependsOn("externConfig")
@Configuration
public class RaftNodeConfiguration {

	@Value("${config.mache.id}")
	private int id;

	@Value("${config.mache.servers}")
	private String servers;

	@Bean
	public Node raftNode(MapDataStorage dataStorage) {
		Node node = new Node(id, EmptyMetaStorage.INSTANCE, dataStorage, EmptyLogStorage.INSTANCE);
		List<RaftRpcService> services = Arrays.stream(servers.split(",")).map(String::trim).filter(Strings::isNotBlank)
				.map(HostPort::new).map(RpcService::new).collect(Collectors.toList());
		node.start(services);
		return node;
	}
}

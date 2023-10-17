package com.gill.mache;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.gill.graft.Node;
import com.gill.graft.model.ProposeReply;
import com.gill.mache.controller.ServerController;
import com.gill.mache.mock.MockNodeWrapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class GillMacheApplicationTests {

	@Autowired
	private Node raftNode;

	@Autowired
	private ServerController serverController;

	@Test
	public void testRaftNodeInit() throws InterruptedException {
		while (!raftNode.isStable()) {
			Thread.sleep(50);
		}
		Assertions.assertTrue(new MockNodeWrapper(raftNode).isLeader());
	}

	@Test
	public void testPutGet() throws InterruptedException {
		testRaftNodeInit();
		String key = "test";
		String value = "zzy";
		ProposeReply reply = serverController.putStr(key, value);
		Assertions.assertEquals(2, reply.getIdx());
		Assertions.assertEquals(value, serverController.getStr(key));
	}
}

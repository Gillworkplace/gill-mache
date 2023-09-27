package com.gill.mache.mock;

import org.springframework.test.util.ReflectionTestUtils;

import com.gill.graft.Node;
import com.gill.graft.machine.RaftMachine;
import com.gill.graft.machine.RaftState;

/**
 * MockNodeWrapper
 *
 * @author gill
 * @version 2023/09/27
 **/
public class MockNodeWrapper extends Node {

	private final Node node;

	public MockNodeWrapper(Node node) {
		this.node = node;
	}

	public boolean isLeader() {
		RaftMachine machine = (RaftMachine) ReflectionTestUtils.getField(node, "machine");
		return machine.getState() == RaftState.LEADER;
	}
}

package com.gill.mache.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gill.graft.Node;
import com.gill.graft.apis.RaftRpcService;
import com.gill.graft.entity.AppendLogEntriesParam;
import com.gill.graft.entity.AppendLogReply;
import com.gill.graft.entity.PreVoteParam;
import com.gill.graft.entity.ReplicateSnapshotParam;
import com.gill.graft.entity.Reply;
import com.gill.graft.entity.RequestVoteParam;

/**
 * InnerController
 *
 * @author gill
 * @version 2023/09/21
 **/
@RestController
@RequestMapping("/raft")
public class InnerController implements RaftRpcService {

	@Autowired
	private Node node;

	@GetMapping("id")
	@Override
	public int getId() {
		return node.getId();
	}

	@PutMapping("pre-vote")
	@Override
	public Reply preVote(@RequestBody PreVoteParam param) {
		return node.preVote(param);
	}

	@PutMapping("request-vote")
	@Override
	public Reply requestVote(@RequestBody RequestVoteParam param) {
		return node.requestVote(param);
	}

	@PutMapping("append-log")
	@Override
	public AppendLogReply appendLogEntries(@RequestBody AppendLogEntriesParam param) {
		return node.appendLogEntries(param);
	}

	@PutMapping("replicate-snapshot")
	@Override
	public Reply replicateSnapshot(@RequestBody ReplicateSnapshotParam param) {
		return node.replicateSnapshot(param);
	}
}

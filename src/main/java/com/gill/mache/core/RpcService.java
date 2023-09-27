package com.gill.mache.core;

import java.util.Optional;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import com.gill.graft.apis.RaftRpcService;
import com.gill.graft.entity.AppendLogEntriesParam;
import com.gill.graft.entity.AppendLogReply;
import com.gill.graft.entity.PreVoteParam;
import com.gill.graft.entity.ReplicateSnapshotParam;
import com.gill.graft.entity.Reply;
import com.gill.graft.entity.RequestVoteParam;
import com.gill.mache.exception.InnerRpcException;

import cn.hutool.json.JSONUtil;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * RpcServer
 *
 * @author gill
 * @version 2023/09/21
 **/
public class RpcService implements RaftRpcService {

	private final HostPort hostPort;

	private volatile Integer id;

	private static class Client {
		private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
	}

	public RpcService(HostPort hostPort) {
		this.hostPort = hostPort;
	}

	@Override
	public int getId() {
		if (id == null) {
			synchronized (this) {
				if (id == null) {
					String url = buildUrl("/mache/raft/id");
					Request request = new Request.Builder().url(url).get().build();
					Call call = Client.HTTP_CLIENT.newCall(request);
					try (Response resp = call.execute()) {
						String ret = Optional.ofNullable(resp.body()).map(catchCheckException(ResponseBody::string))
								.orElseThrow(() -> new InnerRpcException("ret error: " + resp.isSuccessful()));
						id = Integer.valueOf(ret);
					} catch (Exception e) {
						throw new InnerRpcException("call get id failed, e: ", e);
					}
				}
			}
		}
		return id;
	}

	@Override
	public Reply preVote(PreVoteParam preVoteParam) {
		return putCall("/mache/raft/pre-vote", preVoteParam, Reply.class);
	}

	@Override
	public Reply requestVote(RequestVoteParam requestVoteParam) {
		return putCall("/mache/raft/request-vote", requestVoteParam, Reply.class);
	}

	@Override
	public AppendLogReply appendLogEntries(AppendLogEntriesParam appendLogEntriesParam) {
		return putCall("/mache/raft/append-log", appendLogEntriesParam, AppendLogReply.class);
	}

	@Override
	public Reply replicateSnapshot(ReplicateSnapshotParam replicateSnapshotParam) {
		return putCall("/mache/raft/replicate-snapshot", replicateSnapshotParam, Reply.class);
	}

	private String buildUrl(String path) {
		return "http://" + hostPort.getHostPort() + path;
	}

	private <T, R> R putCall(String path, T param, Class<R> rClass) {
		String url = buildUrl(path);
		String bodyStr = JSONUtil.toJsonStr(param);
		RequestBody body = RequestBody.create(bodyStr, MediaType.parse("application/json"));
		Request request = new Request.Builder().url(url).put(body).build();
		Call call = Client.HTTP_CLIENT.newCall(request);
		try (Response resp = call.execute()) {
			String ret = Optional.ofNullable(resp.body()).map(catchCheckException(ResponseBody::string))
					.orElseThrow(() -> new InnerRpcException("ret error: " + resp.isSuccessful()));
			return JSONUtil.toBean(ret, rClass);
		} catch (Exception e) {
			throw new InnerRpcException("call " + path + " failed, e: ", e);
		}
	}

	@FunctionalInterface
	interface CheckedFunction<T, R> {

		/**
		 * apply
		 * 
		 * @param param
		 *            param
		 * @return ret
		 * @throws Exception
		 *             ex
		 */
		R apply(T param) throws Exception;
	}

	@NotNull
	private <T, R> Function<T, R> catchCheckException(CheckedFunction<T, R> function) {
		return body -> {
			try {
				return function.apply(body);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
	}
}

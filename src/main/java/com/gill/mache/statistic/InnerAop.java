package com.gill.mache.statistic;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.hutool.core.util.RandomUtil;

/**
 * InnerAop
 *
 * @author gill
 * @version 2023/09/21
 **/
@Aspect
@Component
public class InnerAop {

	private static final Logger log = LoggerFactory.getLogger(InnerAop.class);

	@Value("${config.log.debug.costThreshold:10}")
	private long costDebugThreshold;

	@Value("${config.log.info.costThreshold:10}")
	private long costInfoThreshold;

	@Autowired
	private InnerStatistics statistics;

	@Pointcut("execution(* com.gill.graft.rpc.client.NettyRpcService.*(..))")
	private void innerPointcut() {
	}

	@Around("innerPointcut()")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] args = joinPoint.getArgs();
		Signature signature = joinPoint.getSignature();
		String methodName = signature.getName();
		String uuid = RandomUtil.randomString(8);
		log.debug("ticket: {} {} => params: {}", uuid, methodName, Arrays.asList(args));
		Object res = joinPoint.proceed();
		log.debug("ticket: {} {} => reply: {}", uuid, methodName, res);
		return res;
	}

	@Around(("innerPointcut()"))
	public Object costAround(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();
		try {
			return joinPoint.proceed();
		} finally {
			long cost = System.currentTimeMillis() - start;
			String methodName = joinPoint.getSignature().getName();
			statistics.add(methodName, cost);
			Object[] args = joinPoint.getArgs();
			if (cost >= costDebugThreshold) {
				log.debug("cost: {} {} => params: {}", cost, methodName, Arrays.asList(args));
			}
			if (cost >= costInfoThreshold) {
				log.debug("cost: {} {} => params: {}", cost, methodName, Arrays.asList(args));
			}
		}
	}
}

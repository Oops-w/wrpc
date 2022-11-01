package com.w.wrpc.proxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.w.wrpc.annotation.RpcService;
import com.w.wrpc.WrpcInstance;
import com.w.wrpc.WrpcServerInstance;
import com.w.wrpc.dto.WrpcMessage;
import com.w.wrpc.enums.RequestTypeEnum;
import com.w.wrpc.util.Snowflake;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * @author wsy
 * @date 2022/10/29 6:55 PM
 * @Description rpc server proxy
 */
public class WrpcServiceInvocationHandler implements InvocationHandler {
    private final Object target;
    private final ObjectMapper objectMapper;
    private CountDownLatch countDownLatch;

    public WrpcServiceInvocationHandler(Object target) {
        this.target = target;
        this.objectMapper = new ObjectMapper();
        this.countDownLatch = new CountDownLatch(0);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        WrpcMessage<?> wrpcMessage = generateWrpcMessage(method, args);

        Collection<WrpcInstance> staredServer = WrpcServerInstance.getStaredServer();
        if (staredServer.isEmpty()) {
            throw new RuntimeException("current not have started server");
        }
        Iterator<WrpcInstance> iterator = staredServer.iterator();
        WrpcServerInstance wrpcInstance = (WrpcServerInstance) iterator.next();

        // todo 如何接收异步的消息?
        return "hello world";
    }

    private WrpcMessage<?> generateWrpcMessage(Method method, Object[] args) throws JsonProcessingException {
        RpcService rpcService = target.getClass().getAnnotation(RpcService.class);
        if (Objects.isNull(rpcService)) {
            throw new RuntimeException("current class not have @RpcService annotation");
        }
        WrpcMessage<Object> wrpcMessage = new WrpcMessage<>();

        if (Void.TYPE.equals(method.getReturnType())) {
            wrpcMessage.setNeedReturn(true);
        } else {
            wrpcMessage.setNeedReturn(false);
        }
        wrpcMessage.setHeartbeat(false);
        wrpcMessage.setRequestID(Snowflake.getInstance().nextId());
        wrpcMessage.setSerialization(rpcService.serialization().getCode());
        wrpcMessage.setVersion(rpcService.version());
        // 暂时只有JSON
        wrpcMessage.setData(objectMapper.writeValueAsString(args));
        wrpcMessage.setRequestType(RequestTypeEnum.REQUEST.getValue());

        return wrpcMessage;
    }
}

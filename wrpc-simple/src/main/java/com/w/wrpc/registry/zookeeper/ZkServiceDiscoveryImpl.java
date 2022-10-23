package com.w.wrpc.registry.zookeeper;

import com.w.wrpc.config.WrpcConfig;
import com.w.wrpc.dto.WrpcRequest;
import com.w.wrpc.exception.WrpcException;
import com.w.wrpc.exception.WrpcExceptionMessage;
import com.w.wrpc.registry.ServiceDiscovery;
import com.w.wrpc.util.ZooKeeperUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.ZooKeeper;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wsy
 * @date 2021/9/22 9:42 下午
 * @Description zooKeeper 发现者
 */
@Slf4j
public class ZkServiceDiscoveryImpl implements ServiceDiscovery {

    // TODO 考虑存储当时存活的节点

    @Override
    public InetSocketAddress lookupService(WrpcRequest request) {
        String interfaceName = request.getInterfaceName();
        List<String> addressList = null;
        try {
            ZooKeeper zooKeeper = ZooKeeperUtil.connect(WrpcConfig.getRegistryAddress());
            addressList = ZooKeeperUtil.getChildByPath(zooKeeper, interfaceName);
        } catch (Exception e) {
            log.error("service discovery exception", e);
        }
        // TODO 添加load balance 进行负载均衡选择address
        if (addressList == null || addressList.size() == 0) {
            log.error("service can not be found {}", interfaceName);
            throw new WrpcException(WrpcExceptionMessage.SERVICE_CAN_NOT_BE_FOUND, interfaceName);
        }
        // 暂时就选择第一个ip
        String[] split = addressList.get(0).split(":");
        String hostname = split[0];
        int port = Integer.parseInt(split[1]);
        return new InetSocketAddress(hostname, port);
    }
}

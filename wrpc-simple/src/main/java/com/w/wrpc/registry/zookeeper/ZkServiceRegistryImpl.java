package com.w.wrpc.registry.zookeeper;

import com.w.wrpc.config.WrpcConfig;
import com.w.wrpc.constants.WrpcConstants;
import com.w.wrpc.registry.ServiceRegistry;
import com.w.wrpc.util.ZooKeeperUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Collection;

/**
 * @author wsy
 * @date 2021/9/21 9:49 下午
 * @Description zooKeeper的注册中心
 */
@Slf4j
public class ZkServiceRegistryImpl implements ServiceRegistry {

    /**
     * 注册方式 例子: /rpc/com.w.wrpc.HelloService
     * /root/serviceName/ip address
     * 持久化节点       临时节点
     *
     * @param rpcServiceName 注册的服务名
     * @param uri        {@link URI} 注册的uri
     */
    @Override
    public void registerService(String rpcServiceName, URI uri) {
        try {
            if (rpcServiceName != null && uri != null) {
                ZooKeeper client = connectServer();
                createRootNode(client);
                // create server node
                createPersistenceNode(client, rpcServiceName);
                createNode(client, rpcServiceName, uri.toString().getBytes());
            }
        } catch (Exception e) {
            log.error("zookeeper register fail", e);
        }
    }

    @Override
    public URI lockupService(String serviceName) {
        return null;
    }

    @Override
    public void connect(URI serviceRegistryUri) {
        connectServer();
    }

    @Override
    public Collection<String> supportedSchemes() {
        return null;
    }


    private ZooKeeper connectServer() {
        try {
            return ZooKeeperUtil.connect(WrpcConfig.getRegistryAddress());
        } catch (IOException e) {
            log.error("connect zookeeper io exception", e);
        } catch (InterruptedException e) {
            log.error("connect zookeeper interrupted exception", e);
        }
        throw new RuntimeException("connect zookeeper exception");
    }


    /**
     * 创建根节点
     *
     * @param client
     * @throws KeeperException
     * @throws InterruptedException
     */
    private void createRootNode(ZooKeeper client) throws KeeperException, InterruptedException {
        Stat exists = ZooKeeperUtil.exist(client, WrpcConstants.ZK_REGISTRY_SERVICE_ROOT_PATH);
        if (exists == null) {
            String resultPath = ZooKeeperUtil.create(client, WrpcConstants.ZK_REGISTRY_SERVICE_ROOT_PATH, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            log.info("创建zookeeper根节点 {}", resultPath);
        }
    }

    /**
     * 创建持久化节点 主要存储可能有服务的rpc服务名称
     *
     * @param client
     * @param rpcServiceName
     */
    private void createPersistenceNode(ZooKeeper client, String rpcServiceName) throws KeeperException, InterruptedException {
        String rpcServiceNodeName = WrpcConstants.ZK_REGISTRY_SERVICE_ROOT_PATH + "/" + rpcServiceName;
        Stat exists = ZooKeeperUtil.exist(client, rpcServiceNodeName);
        if (exists == null) {
            String resultPath = ZooKeeperUtil.create(client, rpcServiceNodeName, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            log.info("创建持久化服务节点 [{}]", resultPath);
        }
    }

    /**
     * 创建临时节点，当客户端关闭的时候临时节点被删除
     *
     * @param client
     * @param path
     * @throws KeeperException
     * @throws InterruptedException
     */
    private void createNode(ZooKeeper client, String path, byte[] data) throws KeeperException, InterruptedException {
        String resultPath = ZooKeeperUtil.create(client, WrpcConstants.ZK_REGISTRY_SERVICE_ROOT_PATH + "/" + path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        log.info("创建节点 ({})", resultPath);
    }
}

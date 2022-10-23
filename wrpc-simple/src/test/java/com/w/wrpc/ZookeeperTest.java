package com.w.wrpc;

import com.w.wrpc.dto.WrpcRequest;
import com.w.wrpc.factory.SingletonBeanFactory;
import com.w.wrpc.registry.ServiceRegistry;
import com.w.wrpc.registry.zookeeper.ZkServiceDiscoveryImpl;
import com.w.wrpc.util.ZooKeeperUtil;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wsy
 * @date 2021/9/21 10:09 下午
 * @Description
 */
public class ZookeeperTest {
    @Test
    public void test() throws IOException, KeeperException, InterruptedException {
        String hostPort = "localhost:2181";
        String zpath = "/";
        List<String> zooChildren = new ArrayList<String>();
        ZooKeeper zk = new ZooKeeper(hostPort, 2000, null);
        if (zk != null) {
            try {
                zooChildren = zk.getChildren(zpath, false);
                System.out.println("Znodes of '/': ");
                for (String child : zooChildren) {
                    //print the children
                    System.out.println(child);
                }
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void zookeeperRegistryTest() throws IOException, InterruptedException, KeeperException {
        SingletonBeanFactory factory = SingletonBeanFactory.getInstance();
        ServiceRegistry serviceRegistry = factory.getBean("zkServiceRegistry", ServiceRegistry.class);
        serviceRegistry.registry("com.w.wrpc.ZookeeperTest", new InetSocketAddress("192.168.1.1", 8080));
        serviceRegistry.registry("com.w.wrpc.ZookeeperTest", new InetSocketAddress("192.168.1.2", 8080));
        serviceRegistry.registry("com.w.wrpc.ZookeeperTest", new InetSocketAddress("192.168.1.3", 8080));

        System.in.read();
    }

    @Test
    public void zooKeeperDiscoveryTest() throws IOException, InterruptedException, KeeperException {
        SingletonBeanFactory factory = SingletonBeanFactory.getInstance();
        ZkServiceDiscoveryImpl zkServiceDiscovery = factory.getBean("zkServiceDiscovery", ZkServiceDiscoveryImpl.class);
        System.out.println(zkServiceDiscovery.lookupService(new WrpcRequest("/rpc/com.w.wrpc.ZookeeperTest", "hegllo", null, null, null)));
    }
}

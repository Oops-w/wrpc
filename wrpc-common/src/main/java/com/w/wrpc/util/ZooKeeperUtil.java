package com.w.wrpc.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author wsy
 * @date 2021/9/22 10:22 上午
 * @Description
 */
@Slf4j
public class ZooKeeperUtil {

    static List<ZooKeeper> list = new ArrayList<>();

    /**
     * 连接zookeeper
     *
     * @param address zookeeper 地址 例如：localhost:2181
     * @return {@link ZooKeeper} 返回此次连接的Zookeeper 客户端
     */
    public static ZooKeeper connect(String address) throws IOException, InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ZooKeeper zooKeeper = new ZooKeeper(address, 20000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                    countDownLatch.countDown();
                }
            }
        });
        countDownLatch.await();
        list.add(zooKeeper);
        log.info("zooKeeper connect success {}", zooKeeper);
        return zooKeeper;
    }

    /**
     * 创建节点
     *
     * @param zooKeeper {@link ZooKeeper} zooKeeper客户端
     * @param path      需要创建node的path
     * @param data      node里的数据
     * @throws KeeperException
     * @throws InterruptedException
     */
    public static String create(ZooKeeper zooKeeper, String path, byte[] data) throws KeeperException, InterruptedException {
        return create(zooKeeper, path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    /**
     * 创建节点
     *
     * @param zooKeeper    {@link ZooKeeper} zooKeeper客户端
     * @param path         需要创建node的path
     * @param data         node里的数据
     * @param aclArrayList 访问控制集合
     * @throws KeeperException
     * @throws InterruptedException
     */
    public static String create(ZooKeeper zooKeeper, String path, byte[] data, ArrayList<ACL> aclArrayList) throws KeeperException, InterruptedException {
        return create(zooKeeper, path, data, aclArrayList, CreateMode.PERSISTENT);
    }

    /**
     * @param zooKeeper    {@link ZooKeeper} zooKeeper客户端
     * @param path         需要创建node的path
     * @param data         node里的数据
     * @param aclArrayList 访问控制集合
     * @param createMode   node创建模式 枚举类型{@link CreateMode}
     * @throws KeeperException
     * @throws InterruptedException
     */
    public static String create(ZooKeeper zooKeeper, String path, byte[] data, ArrayList<ACL> aclArrayList, CreateMode createMode) throws KeeperException, InterruptedException {
        String resPath = zooKeeper.create(path, data, aclArrayList, createMode);
        log.info("zooKeeper create node success option client [{}] create path [{}] data [{}] acl [{}] create mode [{}]", zooKeeper, resPath, data, aclArrayList, createMode);
        return resPath;
    }

    /**
     * @param zooKeeper {@link ZooKeeper} zooKeeper client
     * @param path      node path
     * @return 如果存在返回 {@link Stat} node的元数据 如果不存在返回null
     * @throws KeeperException
     * @throws InterruptedException
     */
    public static Stat exist(ZooKeeper zooKeeper, String path) throws KeeperException, InterruptedException {
        return zooKeeper.exists(path, true);

    }

    /**
     * 删除节点
     *
     * @param zooKeeper
     * @param path
     * @param version
     * @throws KeeperException
     * @throws InterruptedException
     */
    public static void delete(ZooKeeper zooKeeper, String path, int version) throws KeeperException, InterruptedException {
        zooKeeper.delete(path, version);
    }


    /**
     * 根据path获得子节点
     *
     * @param zooKeeper
     * @param path
     */
    public static List<String> getChildByPath(ZooKeeper zooKeeper, String path) throws KeeperException, InterruptedException {
        List<String> children = zooKeeper.getChildren(path, false);
        return children;
    }

    /**
     * 关闭所有zookeeper客户端
     *
     * @throws InterruptedException
     */
    public static void closeAllZookeeperClient() throws InterruptedException {
        for (ZooKeeper zooKeeper : list) {
            log.info("zooKeeper client close ing {}", zooKeeper);
            zooKeeper.close();
        }
        log.info("zooKeeper client close success");
    }


}

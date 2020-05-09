package com.zhaoxun.utils;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;

import java.util.concurrent.CountDownLatch;

/**
 * @Author ZX
 * @Date 2020/5/9 20:22
 * @Version 1.0
 */
public class MyZKLock {
    private CuratorFramework curatorFramework;
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    //总结点
    private static final String PARENT = "all_locks";
    //分节点
    private static final String PRODUCT_LOCK = "my_pro_lock";


    public MyZKLock(CuratorFramework curatorFramework) {
        this.curatorFramework = curatorFramework;
    }

    public void init() {
        curatorFramework = this.curatorFramework.usingNamespace("zhaoxun2020");
        try {
            if (curatorFramework.checkExists().forPath("/" + PARENT) == null) {
                curatorFramework.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath("/" + PARENT);
            }
            //针对分布式锁节点创建相应监听事件
            addWatcherToLock("/" + PARENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取锁
    public void getLock() {
        while (true) {
            try {
                curatorFramework.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.EPHEMERAL)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath("/" + PARENT + "/" + PRODUCT_LOCK);
                System.out.println("获取分布式锁成功");
                return; //记得返回
            } catch (Exception e) {
                //e.printStackTrace();
                System.out.println("获取分布式锁失败");
                try {
                    //如果没获取到锁，需要重新设置同步资源
                    if (countDownLatch.getCount() <= 0) {
                        countDownLatch = new CountDownLatch(1);
                    }
                    countDownLatch.await();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    //释放锁
    public void unLock() {
        try {
            if (curatorFramework.checkExists().forPath("/" + PARENT + "/" + PRODUCT_LOCK) != null) {
                curatorFramework.delete().forPath("/" + PARENT + "/" + PRODUCT_LOCK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("分布式锁释放完毕");
    }

    private void addWatcherToLock(String path) throws Exception {
        PathChildrenCache cache = new PathChildrenCache(curatorFramework, path, true);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                if (pathChildrenCacheEvent.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
                    String path = pathChildrenCacheEvent.getData().getPath();
                    System.out.println("上一个节点已经释放锁或者该会话已断开，节点路径为" + path);
                    if (path.contains(PRODUCT_LOCK)) {
                        System.out.println("计数器释放，让当前请求获取分布式锁");
                        countDownLatch.countDown();
                    }
                }
            }
        });
    }

    //判断是否链接
    public void isOK() {
        System.out.println(curatorFramework.getState());
    }
}

package com.zhaoxun.zookeeperlock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ZookeeperlockApplicationTests {

    @Autowired
    private CuratorFramework curatorFramework;

    @Test
    void do01() {
        System.out.println("当前状态：" + curatorFramework.getState());
        //关闭测试
        curatorFramework.close();
        System.out.println("当前状态：" + curatorFramework.getState());
        //测试通过
    }

    @Test
    void do02() {
        try {
            curatorFramework.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    .forPath("/test","666".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //测试通过，创建节点成功
    }

    @Test
    void do03(){
        try {
            curatorFramework.delete().forPath("/test");
            System.out.println("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

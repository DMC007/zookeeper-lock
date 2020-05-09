package com.zhaoxun.config;

import com.zhaoxun.utils.MyZKLock;
import com.zhaoxun.utils.ZKCurator;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author ZX
 * @Date 2020/5/9 20:15
 * @Version 1.0
 */
@Configuration
public class MyConfig {
    @Bean
    public RetryPolicy retryPolicy() {
        //重试次数，重试间隔
        RetryNTimes retryPolicy = new RetryNTimes(10, 5000);
        return retryPolicy;
    }

    @Bean(initMethod = "start")
    public CuratorFramework curatorFramework() {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString("192.168.25.156:8888")
                .sessionTimeoutMs(16000)
                .retryPolicy(retryPolicy())
                .build();
        return curatorFramework;
    }

    @Bean(initMethod = "init")
    public ZKCurator zkCurator() {
        return new ZKCurator(curatorFramework());
    }

    //这是我们的分布式锁类【上面那个扯淡的，没用到，只是下面的一个模板】
    @Bean(initMethod = "init")
    public MyZKLock myZKLock() {
        return new MyZKLock(curatorFramework());
    }
}

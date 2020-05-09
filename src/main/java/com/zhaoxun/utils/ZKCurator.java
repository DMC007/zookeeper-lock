package com.zhaoxun.utils;

import org.apache.curator.framework.CuratorFramework;

/**
 * @Author ZX
 * @Date 2020/5/9 20:22
 * @Version 1.0
 */
public class ZKCurator {
    private CuratorFramework curatorFramework;

    public ZKCurator(CuratorFramework curatorFramework) {
        this.curatorFramework = curatorFramework;
    }

    public void init() {
        curatorFramework = this.curatorFramework.usingNamespace("zhaoxun2020");
    }

    //判断是否链接
    public void isOK() {
        System.out.println(curatorFramework.getState());
    }
}

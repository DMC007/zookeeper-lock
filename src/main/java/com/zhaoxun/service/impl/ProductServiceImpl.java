package com.zhaoxun.service.impl;

import com.zhaoxun.domain.Product;
import com.zhaoxun.mapper.ProductMapper;
import com.zhaoxun.service.ProductService;
import com.zhaoxun.utils.MyZKLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @Author ZX
 * @Date 2020/5/9 19:18
 * @Version 1.0
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private MyZKLock myZKLock;

    @Override
    public Product findById(Long id) {
        System.out.println("---经过service--");
        return productMapper.findById(id);
    }

    @Override
    public boolean doShop(Long id) {
        //加锁
        myZKLock.getLock();

        System.out.println("---经过service--");
        Product product = productMapper.findById(id);
        try {
            TimeUnit.SECONDS.sleep(6);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(product);
        if (product.getStock() > 6) {
            System.out.println("通过mapper");
            productMapper.updateStock(id);

            //更新完释放锁
            myZKLock.unLock();
            return true;
        }else {
            System.out.println("不通过mapper");
            //库存不足也要释放锁
            myZKLock.unLock();
            return false;
        }
    }
}

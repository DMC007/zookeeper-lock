package com.zhaoxun.service;

import com.zhaoxun.domain.Product;

/**
 * @Author ZX
 * @Date 2020/5/9 19:18
 * @Version 1.0
 */
public interface ProductService {
    Product findById(Long id);

    boolean doShop(Long id);
}

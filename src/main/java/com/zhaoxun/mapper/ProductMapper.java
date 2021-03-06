package com.zhaoxun.mapper;

import com.zhaoxun.domain.Product;

/**
 * @Author ZX
 * @Date 2020/5/9 19:18
 * @Version 1.0
 */
public interface ProductMapper {
    Product findById(Long id);

    void updateStock(Long id);
}

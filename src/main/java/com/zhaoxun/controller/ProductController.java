package com.zhaoxun.controller;

import com.zhaoxun.domain.Product;
import com.zhaoxun.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ZX
 * @Date 2020/5/9 19:17
 * @Version 1.0
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/find/{id}")
    public Product findById(@PathVariable("id") Long id) {
        System.out.println("---经过controller--");
        return productService.findById(id);
    }

    @GetMapping("/buy01/{id}")
    public String buy01(@PathVariable("id") Long id) {
        System.out.println("---经过controller--");
        boolean b = productService.doShop(id);
        return b ? "购买成功" : "库存不足";
    }

    @GetMapping("/buy02/{id}")
    public String buy02(@PathVariable("id") Long id) {
        System.out.println("---经过controller--");
        boolean b = productService.doShop(id);
        return b ? "购买成功" : "库存不足";
    }
}

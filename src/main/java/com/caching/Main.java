package com.caching;

import com.caching.cache.ICache;
import com.caching.cache.impl.LRUCache;
import com.caching.models.TestValue;

import java.util.Date;

public class Main {

    public static void main(String[] args) throws Exception {
        ICache<Integer, TestValue> cache = new LRUCache<>(10);

        for(int i=0; i<15; i++) {
            cache.put(i, new TestValue(i, calculateTtl()));
        }

        System.out.println(cache.get(0));

        System.out.println(cache.get(10));

        Thread.sleep(10000);
    }

    private static Date calculateTtl() {
        return new Date(new Date().getTime() + 3 * 1000);
    }
}
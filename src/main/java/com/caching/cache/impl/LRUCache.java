package com.caching.cache.impl;

import com.caching.cache.Cache;
import com.caching.models.BaseValue;
import com.caching.policy.impl.LRUEvictionPolicy;

public class LRUCache<K, V extends BaseValue> extends Cache<K, V> {

    public LRUCache(int size) {
        super(new LRUEvictionPolicy<>(size));
    }
}

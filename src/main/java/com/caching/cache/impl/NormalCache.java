package com.caching.cache.impl;

import com.caching.cache.Cache;
import com.caching.models.BaseValue;

public class NormalCache<K, V extends BaseValue> extends Cache<K, V> {

    public NormalCache() {
        super(null);
    }
}

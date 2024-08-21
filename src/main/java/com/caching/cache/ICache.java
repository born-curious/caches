package com.caching.cache;

import com.caching.models.BaseValue;

public interface ICache<K, V extends BaseValue> {

    void put(K key, V value);

    V get(K key);

    void remove(K key);
}

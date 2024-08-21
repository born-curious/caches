package com.caching.policy;

import com.caching.models.BaseValue;
import com.caching.models.CacheNode;

public interface EvictionPolicy<K, V extends BaseValue> {

    CacheNode<K, V> updateAfterPut(CacheNode<K, V> node);

    void updateAfterGet(CacheNode<K, V> node);

    void updateAfterRemove(CacheNode<K, V> node);
}

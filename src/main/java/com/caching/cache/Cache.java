package com.caching.cache;

import com.caching.models.BaseValue;
import com.caching.models.CacheNode;
import com.caching.policy.EvictionPolicy;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class Cache<K, V extends BaseValue> implements ICache<K, V> {

    private final ScheduledExecutorService scheduler;
    private final Map<K, CacheNode<K, V>> map;
    private final EvictionPolicy<K, V> cacheEvictionPolicy;

    public Cache(EvictionPolicy<K, V> cacheEvictionPolicy) {
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.map = new HashMap<>();
        this.cacheEvictionPolicy = cacheEvictionPolicy;
        this.initScheduler();
    }

    @Override
    public void put(K key, V value) {
        CacheNode<K, V> node = new CacheNode<>(key, value, null, null);
        map.put(key, node);
        if (Objects.nonNull(cacheEvictionPolicy)) {
            CacheNode<K, V> toBeRemoved = cacheEvictionPolicy.updateAfterPut(node);
            if (Objects.nonNull(toBeRemoved)) {
                removeFromCache(toBeRemoved);
            }
        }
    }

    @Override
    public V get(K key) {
        CacheNode<K, V> node = map.get(key);
        if (Objects.nonNull(cacheEvictionPolicy)) {
            if (Objects.isNull(node)) {
                return null;
            }
            if (hasExpired(node)) {
                removeFromCache(node);
                return null;
            }
            cacheEvictionPolicy.updateAfterGet(node);
        }
        return node.getValue();
    }

    @Override
    public void remove(K key) {
        CacheNode<K, V> node = map.get(key);
        if(Objects.isNull(node)) {
            return;
        }
        removeFromCache(node);
    }

    private void initScheduler() {
        scheduler.scheduleAtFixedRate(() -> {
                    try {
                        removeExpiredNodesFromCache();
                    } catch (Exception ignored) {
                    }
                },
                1,
                1,
                TimeUnit.SECONDS
        );
    }

    private void removeExpiredNodesFromCache() {
        for(Map.Entry<K, CacheNode<K, V>> entry: map.entrySet()) {
            if(hasExpired(entry.getValue())) {
                removeFromCache(entry.getValue());
            }
        }
    }

    private void removeFromCache(CacheNode<K, V> node) {
        map.remove(node.getKey());
        if (Objects.nonNull(cacheEvictionPolicy)) {
            cacheEvictionPolicy.updateAfterRemove(node);
        }
    }

    private boolean hasExpired(CacheNode<K, V> node) {
        Date expiryTime = node.getValue().getExpiryTime();
        if(Objects.isNull(expiryTime)) {
            return false;
        }
        Date now = new Date();
        return expiryTime.before(now);
    }
}

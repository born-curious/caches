package com.caching.policy.impl;

import com.caching.models.BaseValue;
import com.caching.models.CacheNode;
import com.caching.policy.EvictionPolicy;
import lombok.Getter;

import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LRUEvictionPolicy<K, V extends BaseValue> implements EvictionPolicy<K, V> {

    private final int size;
    private final Lock lock;
    private int currentSize;
    @Getter
    private CacheNode<K, V> head, tail;

    public LRUEvictionPolicy(int size) {
        this.lock = new ReentrantLock();
        this.size = size;
        this.currentSize = 0;
        this.head = null;
        this.tail = null;
    }

    @Override
    public CacheNode<K, V> updateAfterPut(CacheNode<K, V> node) {
        lock.lock();
        try {
            node.setNext(head);
            if(Objects.nonNull(head)) {
                head.setPrev(node);
            }
            head = node;
            if(Objects.isNull(tail)) {
                tail = node;
            }
            currentSize++;
            if(currentSize == size + 1) {
                return tail;
            }
        } catch (Exception ignored) {
        } finally {
            lock.unlock();
        }
        return null;
    }

    @Override
    public void updateAfterGet(CacheNode<K, V> node) {
        lock.lock();
        try {
            removeFromList(node);
            node.setPrev(null);
            node.setNext(head);
            head = node;
        } catch (Exception ignored) {
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void updateAfterRemove(CacheNode<K, V> node) {
        lock.lock();
        try {
            removeFromList(node);
            currentSize--;
        } catch (Exception ignored) {
        } finally {
            lock.unlock();
        }
    }

    private void removeFromList(CacheNode<K, V> node) {
        if(tail == node) {
            tail = node.getPrev();
        }
        if(head == node) {
            head = node.getNext();
        }
        if(Objects.nonNull(node.getPrev())) {
            node.getPrev().setNext(node.getNext());
        }
        if(Objects.nonNull(node.getNext())) {
            node.getNext().setPrev(node.getPrev());
        }
    }
}

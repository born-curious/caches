package com.caching.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CacheNode<K, V extends BaseValue> {

    private K key;
    private V value;
    private CacheNode<K, V> next, prev;
}

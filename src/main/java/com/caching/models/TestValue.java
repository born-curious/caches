package com.caching.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class TestValue extends BaseValue {

    private int value;

    public TestValue(int value, Date ttl) {
        super(ttl);
        this.value = value;
    }
}

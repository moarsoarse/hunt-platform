package com.hunt.bpm-launcher-service;

import java.util.Collections;
import java.util.List;

public abstract class TestBuilder<T> {

    private T testObject;

    private List<T> testObjects;

    protected List<T> createFullList() {
        return Collections.singletonList(createFull());
    }

    protected abstract T createFull();

    public T build() {
        testObject = createFull();
        return testObject;
    }

    public List<T> buildList() {
        testObjects = createFullList();
        return testObjects;
    }
}

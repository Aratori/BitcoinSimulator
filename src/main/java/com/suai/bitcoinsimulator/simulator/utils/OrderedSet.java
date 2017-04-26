package com.suai.bitcoinsimulator.simulator.utils;

public abstract class OrderedSet {
    public abstract void insert(Comparable x);
    public abstract Comparable  removeFirst();
    public abstract Comparable 	getFirst();
    public abstract int size();
    public abstract Comparable remove(Comparable x);
}
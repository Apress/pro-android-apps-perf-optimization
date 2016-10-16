package com.apress.proandroid;

public class MyRecord {
	private final short id;
    private final short value;
    // and possibly more

    public MyRecord(short id, short value) {
        this.id = id;
        this.value = value;
    }

    public final short getId() {
        return id;
    }

    public final short getValue() {
        return value;
    }

    public void doSomething() {
        // do something here
    }
}

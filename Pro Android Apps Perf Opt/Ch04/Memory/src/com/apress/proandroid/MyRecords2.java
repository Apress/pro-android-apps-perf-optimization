package com.apress.proandroid;

public class MyRecords2 {
	private short[] records;
    int nbRecords;

    public MyRecords2 (int size) {
        records = new short[size * 2];
    }

    public int addRecord (short id, short value) {
        int index;
        if (nbRecords < records.length) {
            index = nbRecords;
            records[nbRecords * 2] = id;
            records[nbRecords * 2 + 1] = value;
            nbRecords++;
        } else {
            index = -1;
        }
        return index;
    }

    public void deleteRecord (int index) {
        if (index < 0) {
            // throw exception here – invalid argument
        }
        if (index < nbRecords) {
            nbRecords--;
            records[index * 2] = records[nbRecords * 2];
            records[index * 2 + 1] = records[nbRecords * 2 + 1];
        }
    }

    public int sumValues (int id) {
        int sum = 0;
        for (int i = 0; i < nbRecords; i++) {
            if (records[i * 2] == id) {
                sum += records[i * 2 + 1];
            }
        }
        return sum;
    }

    public void doSomethingWithAllRecords () {
    	/*
    	MyRecord r = new MyRecord((short) 0, (short)0);
        for (int i = 0; i < nbRecords; i++) {
            r.id = records[i * 2];
            r.value = records[i * 2 + 1];
            r.doSomething();
        }
        */
        
        for (int i = 0; i < nbRecords; i++) {
        	MyRecord r = new MyRecord(records[i * 2], records[i * 2 + 1]);
            r.doSomething();
        }
    }
}

package com.apress.proandroid;

public class MyRecords3 {
    private short[] recordIds; // first array only for ids
    private short[] recordValues; // second array only for values
    int nbRecords;

    public MyRecords3 (int size) {
        recordIds = new short[size];
        recordValues = new short[size];
    }

    public int addRecord (short id, short value) {
        int index;
        if (nbRecords < recordIds.length) {
            index = nbRecords;
            recordIds[nbRecords] = id;
            recordValues[nbRecords] = value;
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
            recordIds[index] = recordIds[nbRecords];
            recordValues[index] = recordValues[nbRecords];
        }
    }

    public int sumValues (int id) {
        int sum = 0;
        for (int i = 0; i < nbRecords; i++) {
            if (recordIds[i] == id) {
                sum += recordValues[i]; // we only read the value if the id matches
            }
        }
        return sum;
    }

    public void doSomethingWithAllRecords () {
    	/*
    	MyRecord r = new MyRecord((short) 0, (short)0);
        for (int i = 0; i < nbRecords; i++) {
            r.id = recordIds[i];
            r.value = recordValues[i];
            r.doSomething();
        }
        */
        
        for (int i = 0; i < nbRecords; i++) {
        	MyRecord r = new MyRecord(recordIds[i], recordValues[i]);
            r.doSomething();
        }
    }
}

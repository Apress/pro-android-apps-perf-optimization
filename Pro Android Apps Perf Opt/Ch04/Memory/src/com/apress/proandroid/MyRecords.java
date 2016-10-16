package com.apress.proandroid;

public class MyRecords {
	private MyRecord[] records;
    int nbRecords;

    public MyRecords (int size) {
        records = new MyRecord[size];
    }

    public int addRecord (short id, short value) {
        int index;
        if (nbRecords < records.length) {
            index = nbRecords;
            records[nbRecords] = new MyRecord(id, value);
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
            records[index] = records[nbRecords];
            records[nbRecords] = null; // don’t forget to delete reference
        }
    }

    public int sumValues (int id) {
        int sum = 0;
        for (int i = 0; i < nbRecords; i++) {
        	MyRecord r = records[i];
            if (r.getId() == id) {
                sum += r.getValue();
            }
            /*
             * if id and value were public, you could write the following:
             * if (r.id == id) sum += r.value;
             */
            
        }
        return sum;
    }

    public void doSomethingWithAllRecords () {
        for (int i = 0; i < nbRecords; i++) {
            records[i].doSomething();
        }
    }

}

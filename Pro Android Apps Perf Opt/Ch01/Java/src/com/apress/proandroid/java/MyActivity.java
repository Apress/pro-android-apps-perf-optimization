package com.apress.proandroid.java;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;

import org.apache.http.util.ByteArrayBuffer;

import com.apress.proandroid.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Config;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;


public class MyActivity extends Activity {
	private static final String TAG = "APressActivity";
	
	private static final String STATE_COMPUTE = "apress.compute";
	
	private CheckBox garbageCollectionCheckBox;
	private CheckBox methodTracingCheckBox;
	private CheckBox nativeTracingCheckBox;
	private CheckBox uiThreadCheckBox;
	private CheckBox delayCheckBox;
	private CheckBox recursiveFasterCheckBox;
	private EditText numberText;
	private Button mRunButton;
	private TextView mTextView;
	
	private volatile boolean garbageCollectionEnabled;
	private volatile boolean methodTracingEnabled;
	private volatile boolean nativeTracingEnabled;
	private volatile boolean delayEnabled;
	private volatile boolean recursiveFasterOnly;
	private volatile int delay;
	private volatile int defaultN;
	private volatile int recursionThreshold;
	
	private AsyncTask<Integer, Void, String> mTask;
	
	@Override
	protected void onStop() {
		super.onStop();
		if (mTask != null) {
			mTask.cancel(true);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// if called, it will be called before onStop()
		super.onSaveInstanceState(outState);
		if (mTask != null) {
			outState.putInt(STATE_COMPUTE, 100000); // for simplicity, hard-coded value
		}
	}

	/** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	if (Config.DEBUG) {
    		Log.i(TAG, "Running on a debug DEVICE, not a release one (nothing to do with debug/release application)");
    	}
    	if ((getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0 ){
    		Log.i(TAG, "Running a debuggable build");
    		// StrictMode here for example
    	}
    	Log.i(TAG, "Processors: " + Runtime.getRuntime().availableProcessors());
    	
        super.onCreate(savedInstanceState);
        
        long t1 = System.nanoTime();
        setContentView(R.layout.main);
        long t2 = System.nanoTime();
        Log.d(TAG, "Inflated in " + (t2-t1));
        
        t1 = System.nanoTime();
        LayoutInflater li = getLayoutInflater();
        t2 = System.nanoTime();
        View v = li.inflate(R.layout.main, null);
        long t3 = System.nanoTime();
        setContentView(v);
        long t4 = System.nanoTime();
        Log.d(TAG, "Inflated in " + (t2-t1) + " " + (t3-t2) + " " + (t4-t3));
        
        Log.i(TAG, "Activity instance is now " + toString());
        Log.i(TAG, "onCreate called in thread " + Thread.currentThread().getId());
        
        garbageCollectionCheckBox = (CheckBox) findViewById(R.id.gcCheckBox);
        methodTracingCheckBox = (CheckBox) findViewById(R.id.methodTracingCheckBox);
        nativeTracingCheckBox = (CheckBox) findViewById(R.id.nativeTracingCheckBox);
        uiThreadCheckBox = (CheckBox) findViewById(R.id.uiThreadCheckBox);
        delayCheckBox = (CheckBox) findViewById(R.id.delayCheckBox);
        recursiveFasterCheckBox = (CheckBox) findViewById(R.id.recursiveFasterCheckBox);
        numberText = (EditText) findViewById(R.id.number);
        mRunButton = (Button) findViewById(R.id.runTestsButton);
        mTextView = numberText;
        
        // get integer values from resource file
        Resources r = getResources();
        delay = r.getInteger(R.integer.delayBetweenTests);
        defaultN = r.getInteger(R.integer.defaultN);
        recursionThreshold = r.getInteger(R.integer.recursionThreshold);
        
        // default values
        garbageCollectionCheckBox.setChecked(true);
        methodTracingCheckBox.setChecked(false);
        nativeTracingCheckBox.setChecked(false);
        uiThreadCheckBox.setChecked(false);
        delayCheckBox.setChecked(true);
        recursiveFasterCheckBox.setChecked(false);
        numberText.setText(String.valueOf(defaultN));
        
        delayCheckBox.append(" (" + delay + " ms)");
        
        try {
			Log.i(TAG, ""+Class.forName("android.os.Build$VERSION").getField("SDK_INT"));
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        
        Log.i(TAG, "Allocations (10): "+Fibonacci.computeRecursivelyFasterAllocations(50000, 10));
        Log.i(TAG, "Allocations (92): "+Fibonacci.computeRecursivelyFasterAllocations(50000, 92));
        System.gc();
        Log.i(TAG, "actual call (50000): "+Fibonacci.computeRecursivelyFasterUsingPrimitiveAndBigInteger(50000).bitLength());
        
        SQLiteDatabase db = SQLiteDatabase.create(null); // memory-backed database
        db.execSQL("CREATE TABLE cheese (name TEXT, origin TEXT)");
        db.execSQL("INSERT INTO cheese VALUES ('Roquefort', 'Roquefort-sur-Soulzon')");
        db.close();
        
        File file = getDatabasePath("fromage.db");
        File parent = new File(file.getParent());
        parent.mkdirs();
        
        // many transactions
        //testDatabase(null, false);
        //testDatabase(file.getAbsolutePath(), false);
        
        // one transaction
        //testDatabase(null, true);
        //testDatabase(file.getAbsolutePath(), true);
        /*
        mTask = (AsyncTask<Integer, Void, String>) getLastNonConfigurationInstance();
        if (mTask != null) {
        	mRunButton.setEnabled(false);
        }
        */
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_COMPUTE)) {
        	int value = savedInstanceState.getInt(STATE_COMPUTE);
        	mTask = createTask().execute(value);
        }
    }
    
    @Override
	public void onLowMemory() {
		super.onLowMemory();
		Log.w(TAG, "Low on memory");
	}

    public void onClick (View v) {
    	
    	new Thread(new Runnable() {
    	    public void run() {
    	      final BigInteger f = Fibonacci.computeRecursivelyFasterUsingPrimitiveAndBigInteger(100000);
    	      //mTextView.setText(f.toString());
    	      mTextView.post(new Runnable() {
    	        public void run() {
    	        	//mTextView.setText(f.toString());
    	        	Log.i(TAG, f.toString());
    	        }
    	      });
    	    }
    	  }, "fibonacci").start();
    	
    	new AsyncTask<Integer, Void, BigInteger>() {

			@Override
			protected void onPreExecute() {
				mRunButton.setEnabled(false);
			}

			@Override
			protected void onCancelled() {
				mRunButton.setEnabled(true);
			}

			@Override
			protected BigInteger doInBackground(Integer... params) {
				return Fibonacci.computeRecursivelyFasterUsingPrimitiveAndBigInteger(params[0]);
			}

			@Override
			protected void onPostExecute(BigInteger result) {
				mTextView.setText(result.toString());
				mRunButton.setEnabled(true);
			}

    	}.execute(100000);
    	
    	AsyncTask<String, Object, Void> task = new AsyncTask<String, Object, Void>() {

    		private ByteArrayBuffer downloadFile(String urlString, byte[] buffer) {
    			try {
					URL url = new URL(urlString);
					URLConnection connection = url.openConnection();
					InputStream is = connection.getInputStream();
					Log.i(TAG, "InputStream: " + is.getClass().getName());
					//is = new BufferedInputStream(is); // optional line, try with and without
					ByteArrayBuffer baf = new ByteArrayBuffer(64 * 1024);
					int len;
					while ((len = is.read(buffer)) != -1) {
						baf.append(buffer, 0, len);
					}
					return baf;
				} catch (MalformedURLException e) {
					return null;
				} catch (IOException e) {
					return null;
				}
    		}
    		
			@Override
			protected Void doInBackground(String... params) {
				if (params != null && params.length > 0) {
					byte[] buffer = new byte[1 + 0*4 * 1024]; // try different sizes
					for (String url : params) {
						long time = System.currentTimeMillis();
						ByteArrayBuffer baf = downloadFile(url, buffer);
						time = System.currentTimeMillis() - time;
						publishProgress(url, baf, time);
					}
				} else {
					publishProgress(null, null);
				}
				return null;
			}

			@Override
			protected void onProgressUpdate(Object... values) {
				// values[0] is the URL (String), values[1] is the buffer (ByteArrayBuffer)
				String url = (String) values[0];
				ByteArrayBuffer buffer = (ByteArrayBuffer) values[1];
				long time = (Long) values[2];
				if (buffer != null) {
					Log.i(TAG, "Downloaded " + url + " (" + buffer.length() + " bytes) in " + time + " milliseconds");
				} else {
					Log.w(TAG, "Could not download " + url);
				}
				// update UI accordingly
			}

    	};
    	
    	String url1 = "http://www.google.com/index.html";
    	String url2 = "http://d.android.com/reference/android/os/AsyncTask.html";
    	task.execute(url1, url2);
    	/*
    	try {
			Thread.sleep(4000);
	    	task.execute("http://d.android.com/resources/articles/painless-threading.html");
		} catch (InterruptedException e) {
		}
		*/
    }
    
    private AsyncTask<Integer, Void, String> createTask() {
    	return new AsyncTask<Integer, Void, String>() {

    		@Override
    		protected void onCancelled() {
    			Log.i(TAG, "Task cancelled");
    			mRunButton.setEnabled(true);
    			mRunButton.setText(R.string.runAll);
    			mTask = null;
    		}

    		@Override
    		protected void onPostExecute(String message) {
    			super.onPostExecute(message);
    			mRunButton.setEnabled(true);
    			mRunButton.setText(R.string.runAll);
    			alertResults(message);
    			Log.i(TAG, "Computation completed in " + MyActivity.this.toString());
    			Log.i(TAG, "onPostExecute called in thread " + Thread.currentThread().getId());
    			mTask = null;
    		}

    		@Override
    		protected void onPreExecute() {
    			super.onPreExecute();
    			mRunButton.setEnabled(false);
    			mRunButton.setText(R.string.running);
    		}

    		@Override
    		protected String doInBackground(Integer... params) {
    			Log.i(TAG, "Task started!!!");
    			String message = runFibonacciTests(params[0]);
    			return message;
    		}
    	};
    }
    
    private void measureNanoTime() {
    	final int ITERATIONS = 100000;
    	long total = 0;
    	long min = Long.MAX_VALUE;
    	long max = Long.MIN_VALUE;
    	
    	for (int i = 0; i < ITERATIONS; i++) {
    		long time = System.nanoTime();
    		time = System.nanoTime() - time;
    		total += time;
    		if (time < min) {
    			min = time;
    		}
    		if (time > max) {
    			max = time;
    		}
    	}
    	
    	Log.i(TAG, "System.nanoTime() takes about " + ((float)total / ITERATIONS) + " nanoseconds to complete");
    	Log.i(TAG, " Minimum: " + min);
    	Log.i(TAG, " Maximum: " + max);
    }
    
    private void measureThreadCpuTimeNanos() {
    	final int ITERATIONS = 100000;
    	long total = 0;
    	long min = Long.MAX_VALUE;
    	long max = Long.MIN_VALUE;
    	
    	for (int i = 0; i < ITERATIONS; i++) {
    		long time = Debug.threadCpuTimeNanos();
    		time = Debug.threadCpuTimeNanos() - time;
    		total += time;
    		if (time < min) {
    			min = time;
    		}
    		if (time > max) {
    			max = time;
    		}
    	}
    	
    	Log.i(TAG, "Debug.threadCpuTimeNanos() takes about " + ((float)total / ITERATIONS) + " nanoseconds to complete");
    	Log.i(TAG, " Minimum: " + min);
    	Log.i(TAG, " Maximum: " + max);
    }
    
    private void testThreadCpuTimeNanos() {
    	long duration2 = System.nanoTime();
    	long duration = Debug.threadCpuTimeNanos();
    	try {
			Thread.sleep(TimeUnit.MILLISECONDS.convert(1L, TimeUnit.SECONDS));
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		duration = Debug.threadCpuTimeNanos() - duration;
		duration2 = System.nanoTime() - duration2;
    	Log.i(TAG, "Duration: " + duration + " nanoseconds");
    	Log.i(TAG, "Duration2: " + duration2 + " nanoseconds");
    }
    
    private int getTrace() {
    	Debug.startMethodTracing("/sdcard/awesometrace.trace");
    	BigInteger fN = Fibonacci.computeRecursivelyFasterWithCache(100000);
    	Debug.stopMethodTracing();
    	return fN.bitLength();
    }
    
    private int getNativeTrace() {
    	Debug.startNativeTracing();
    	BigInteger fN = Fibonacci.computeRecursivelyFasterWithCache(100000);
    	Debug.stopNativeTracing();
    	return fN.bitLength();
    }
    
	public void onClickButton (View v) {
    	garbageCollectionEnabled = garbageCollectionCheckBox.isChecked();
    	methodTracingEnabled = methodTracingCheckBox.isChecked();
    	nativeTracingEnabled = nativeTracingCheckBox.isChecked();
    	delayEnabled = delayCheckBox.isChecked();
    	recursiveFasterOnly = recursiveFasterCheckBox.isChecked();
    	
    	//onClick(v);
    	
    	//measureNanoTime();
    	//measureThreadCpuTimeNanos();
    	//testThreadCpuTimeNanos();
    	
    	//getTrace();
    	getNativeTrace();
    	
    	int n;
    	try {
    		n = Integer.valueOf(numberText.getText().toString());
    	} catch (NumberFormatException e) {
    		n = defaultN;
    		numberText.setText(String.valueOf(n));
    	}
    	
    	if (uiThreadCheckBox.isChecked()) {
    		String message = runFibonacciTests(n);
    		alertResults(message);
    	} else {
    		mTask = createTask().execute(n);
    		/*    		
    		Thread thread1 = new Thread("cheese1") {
    			@Override
    			public void run() {
    				Log.i(TAG, "I like Munster");
    			}
    		};
    		Thread thread2 = new Thread(new Runnable() {
    			public void run() {
    				Log.i(TAG, "I like Roquefort");
    			}
    		}, "cheese2");
    		Thread thread3 = new Thread(new Runnable() {
    			public void run() {
    				Log.i(TAG, "I like Brie");
    			}
    		}, "cheese3") {
    			@Override
    			public void run() {
    				super.run();
    				Log.i(TAG, "I like Epoisses");
    			}
    		};
    		thread1.setPriority(Thread.MIN_PRIORITY);
    		thread2.setPriority(Thread.MAX_PRIORITY);
    		Log.i(TAG, "UI thread priority: " + Thread.currentThread().getPriority());
    		Thread thread4 = new Thread();
    		Thread thread5 = new Thread() {
    			@Override
    			public void run() {
    				long x = 1;
    				while (true) {
    					x += Fibonacci.recursive((int)x);
    				}
    			}
    		};
    		thread1.start();
    		thread2.start();
    		thread3.start();
    		thread4.start();
    		thread5.setPriority(Thread.MAX_PRIORITY);
    		thread5.start();
    		try {
				Thread.sleep(7*1000);
				thread5.setPriority(Thread.NORM_PRIORITY);
			} catch (InterruptedException e) {
			}
			*/
    	}
    }
    
    public void onClickNumberOfProcessors (View v) {
    	// will return 2 on a Galaxy Tab 10.1 or BeBox Dual603, but only 1 on a Nexus S or Logitech Revue
        final int proc = Runtime.getRuntime().availableProcessors();
        Log.i(TAG, "Number of available processors: " + proc);
    }
    
	private void testDatabase (String name, boolean oneTransaction) {
		String suffix = name == null ? " (memory, " : " (storage, ";
		suffix += oneTransaction ? "one transaction)" : "many transactions)";
		
		Cheeses c;
		long time;
		
		c = new Cheeses(name);
    	time = System.currentTimeMillis();
        c.populateWithStringBuilder(oneTransaction);
        time = System.currentTimeMillis() - time;
        c.dump();
        c.close();
        reportResultAndTime("populateWithStringBuilder"+suffix, 0, time);
        
        gcAndWait();
        c = new Cheeses(name);
    	time = System.currentTimeMillis();
        c.populateWithStringPlus(oneTransaction);
        time = System.currentTimeMillis() - time;
        c.dump();
        c.close();
        reportResultAndTime("populateWithStringPlus"+suffix, 0, time);
        
        gcAndWait();
        c = new Cheeses(name);
    	time = System.currentTimeMillis();
        c.populateWithStringFormat(oneTransaction);
        time = System.currentTimeMillis() - time;
        c.dump();
        c.close();
        reportResultAndTime("populateWithStringFormat"+suffix, 0, time);
        
        gcAndWait();
        c = new Cheeses(name);
    	time = System.currentTimeMillis();
        c.populateWithCompileStatement(oneTransaction);
        time = System.currentTimeMillis() - time;
        reportResultAndTime("populateWithCompilation"+suffix, 0, time);
    	time = System.currentTimeMillis();
    	c.iterateAllColumns();
    	time = System.currentTimeMillis() - time;
        reportResultAndTime("populateWithCompilation iterateAll"+suffix, 0, time);
    	time = System.currentTimeMillis();
    	c.iterateFirstColumn();
    	time = System.currentTimeMillis() - time;
        reportResultAndTime("populateWithCompilation iterateSome"+suffix, 0, time);
        c.dump();
        c.close();

        gcAndWait();
        c = new Cheeses(name);
    	time = System.currentTimeMillis();
        c.populateWithContentValues(oneTransaction);
        time = System.currentTimeMillis() - time;
        c.dump();
        c.close();
        reportResultAndTime("populateWithContentValues"+suffix, 0, time);
        
        gcAndWait();
	}
	
	private long testStrictModeTooSlow (int n) {
		StrictMode.noteSlowCall("testStrictModeTooSlow " + n);
		return Fibonacci.computeRecursively(n);
	}
	
	private void testStrictModeTooSlow() {
		int n = 0;
		while (true) {
			Log.i(TAG, "Testing tooSlow:"+ n);
			testStrictModeTooSlow(n);
			n++;
		}
	}
	
	//native private static void callNative();
	
    private String runFibonacciTests(int n) {
    	String message = "";
    	BigInteger rBig;
    	Long rLong;
    	long rPrimitiveLong;
    	long time;
    	
    	//testStrictModeTooSlow();
    	//callNative();
    	
    	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
    		StrictMode.noteSlowCall("runFibonacciTests");
    	}
    	
    	if (nativeTracingEnabled) {
    		Debug.startNativeTracing();
    	}
    	
    	Log.i(TAG, "Number of bits = " + Fibonacci.computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndSparseArray(n).bitLength());
    	
    	// Calls
    	Log.i(TAG, "Number of calls for n=" + n);
    	if (n < recursionThreshold) { 
    		reportResult("computeRecursivelyCalls", Fibonacci.computeRecursivelyCalls(n));
    		reportResult("computeRecursivelyWithLoopCalls", Fibonacci.computeRecursivelyWithLoopCalls(n));
    	}
    	reportResult("computeRecursivelyFasterCalls", Fibonacci.computeRecursivelyFasterCalls(n, 2));
    	reportResult("computeRecursivelyFasterCalls", Fibonacci.computeRecursivelyFasterCalls(n, 16));
    	reportResult("computeRecursivelyFasterCalls", Fibonacci.computeRecursivelyFasterCalls(n, Fibonacci.PRECOMPUTED_SIZE));
    	
    	if (n >= 1000000) {
            gcAndWait();
            
        	startMethodTracing("computeRecursivelyFasterUsingPrimitiveAndBigInteger");
        	time = System.currentTimeMillis();
            rBig = Fibonacci.computeRecursivelyFasterUsingPrimitiveAndBigInteger(n);
            time = System.currentTimeMillis() - time;
        	stopMethodTracing();
        	message += "computeRecursivelyFasterUsingPrimitiveAndBigInteger: " + time + " ms\n";
            reportResultAndTime("computeRecursivelyFasterUsingPrimitiveAndBigInteger", rBig, time);
            
            gcAndWait();
            
        	startMethodTracing("computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndSparseArray");
        	time = System.currentTimeMillis();
            rBig = Fibonacci.computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndSparseArray(n);
            time = System.currentTimeMillis() - time;
        	stopMethodTracing();
        	message += "computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndSparseArray: " + time + " ms\n";
            reportResultAndTime("computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndSparseArray", rBig, time);
            
            return message;
    	}
    	
    	Log.d(TAG, "Primitive");

    	if (! recursiveFasterOnly) {
    		if (n < recursionThreshold) {
    			startMethodTracing("computeRecursively");
    			time = System.currentTimeMillis();
    			rPrimitiveLong = Fibonacci.computeRecursively(n);
    			time = System.currentTimeMillis() - time;
    			stopMethodTracing();
    			message += "computeRecursively: " + time + " ms\n";
    			reportResultAndTime("computeRecursively", rPrimitiveLong, time);

    			gcAndWait();

    			startMethodTracing("computeRecursivelyUsingPrimitiveInt");
    			time = System.currentTimeMillis();
    			rPrimitiveLong = Fibonacci.computeRecursivelyUsingPrimitiveInt(n);
    			time = System.currentTimeMillis() - time;
    			stopMethodTracing();
    			message += "computeRecursivelyUsingPrimitiveInt: " + time + " ms\n";
    			reportResultAndTime("computeRecursivelyUsingPrimitiveInt", rPrimitiveLong, time);

    			gcAndWait();

    			startMethodTracing("computeRecursivelyWithLoop");
    			time = System.currentTimeMillis();
    			rPrimitiveLong = Fibonacci.computeRecursivelyWithLoop(n);
    			time = System.currentTimeMillis() - time;
    			stopMethodTracing();
    			message += "computeRecursivelyWithLoop: " + time + " ms\n";
    			reportResultAndTime("computeRecursivelyWithLoop", rPrimitiveLong, time);

    			gcAndWait();

    			Fibonacci fib = new Fibonacci();
    			startMethodTracing("computeRecursivelyVirtual");
    			time = System.currentTimeMillis();
    			rPrimitiveLong = fib.computeRecursivelyVirtual(n);
    			time = System.currentTimeMillis() - time;
    			stopMethodTracing();
    			message += "computeRecursivelyVirtual: " + time + " ms\n";
    			reportResultAndTime("computeRecursivelyVirtual", rPrimitiveLong, time);

    			gcAndWait();
    		}

    		startMethodTracing("computeIteratively");
    		time = System.currentTimeMillis();
    		rPrimitiveLong = Fibonacci.computeIteratively(n);
    		time = System.currentTimeMillis() - time;
    		stopMethodTracing();
    		message += "computeIteratively: " + time + " ms\n";
    		reportResultAndTime("computeIteratively", rPrimitiveLong, time);

    		gcAndWait();

    		startMethodTracing("computeIterativelyFaster");
    		time = System.currentTimeMillis();
    		rPrimitiveLong = Fibonacci.computeIterativelyFaster(n);
    		time = System.currentTimeMillis() - time;
    		stopMethodTracing();
    		message += "computeIterativelyFaster: " + time + " ms\n";
    		reportResultAndTime("computeIterativelyFaster", rPrimitiveLong, time);

    		gcAndWait();

    		startMethodTracing("computeIterativelyFasterUsingPrimitiveInt");
    		time = System.currentTimeMillis();
    		rPrimitiveLong = Fibonacci.computeIterativelyFasterUsingPrimitiveInt(n);
    		time = System.currentTimeMillis() - time;
    		stopMethodTracing();
    		message += "computeIterativelyFasterUsingPrimitiveInt: " + time + " ms\n";
    		reportResultAndTime("computeIterativelyFasterUsingPrimitiveInt", rPrimitiveLong, time);

    		gcAndWait();
    	}

    	startMethodTracing("computeRecursivelyFaster");
    	time = System.currentTimeMillis();
        rPrimitiveLong = Fibonacci.computeRecursivelyFaster(n);
        time = System.currentTimeMillis() - time;
    	stopMethodTracing();
    	message += "computeRecursivelyFaster: " + time + " ms\n";
        reportResultAndTime("computeRecursivelyFaster", rPrimitiveLong, time);
        
        gcAndWait();

        Log.d(TAG, "\nLong");
        
        if (! recursiveFasterOnly) {
        	if (n < recursionThreshold) {
        		startMethodTracing("computeRecursivelyUsingLong");
        		time = System.currentTimeMillis();
        		rLong = Fibonacci.computeRecursivelyUsingLong(n);
        		time = System.currentTimeMillis() - time;
        		stopMethodTracing();
        		message += "computeRecursivelyUsingLong: " + time + " ms\n";
        		reportResultAndTime("computeRecursivelyUsingLong", rLong, time);

        		gcAndWait();

        		startMethodTracing("computeRecursivelyWithLoopUsingLong");
        		time = System.currentTimeMillis();
        		rLong = Fibonacci.computeRecursivelyWithLoopUsingLong(n);
        		time = System.currentTimeMillis() - time;
        		stopMethodTracing();
        		message += "computeRecursivelyWithLoopUsingLong: " + time + " ms\n";
        		reportResultAndTime("computeRecursivelyWithLoopUsingLong", rLong, time);

        		gcAndWait();
        	}

        	startMethodTracing("computeIterativelyUsingLong");
        	time = System.currentTimeMillis();
        	rLong = Fibonacci.computeIterativelyUsingLong(n);
        	time = System.currentTimeMillis() - time;
        	stopMethodTracing();
        	message += "computeIterativelyUsingLong: " + time + " ms\n";
        	reportResultAndTime("computeIterativelyUsingLong", rLong, time);

        	gcAndWait();

        	startMethodTracing("computeIterativelyFasterUsingLong");
        	time = System.currentTimeMillis();
        	rLong = Fibonacci.computeIterativelyFasterUsingLong(n);
        	time = System.currentTimeMillis() - time;
        	stopMethodTracing();
        	message += "computeIterativelyFasterUsingLong: " + time + " ms\n";
        	reportResultAndTime("computeIterativelyFasterUsingLong", rLong, time);

        	gcAndWait();
        }

        startMethodTracing("computeRecursivelyFasterUsingLong");
        time = System.currentTimeMillis();
        rLong = Fibonacci.computeRecursivelyFasterUsingLong(n);
        time = System.currentTimeMillis() - time;
        stopMethodTracing();
        message += "computeRecursivelyFasterUsingLong: " + time + " ms\n";
        reportResultAndTime("computeRecursivelyFasterUsingLong", rLong, time);

        gcAndWait();

        Log.d(TAG, "\nBigInteger");

        if (! recursiveFasterOnly) {
        	if (n < recursionThreshold) {
        		startMethodTracing("computeRecursivelyUsingBigInteger");
        		time = System.currentTimeMillis();
        		rBig = Fibonacci.computeRecursivelyUsingBigInteger(n);
        		time = System.currentTimeMillis() - time;
        		stopMethodTracing();
        		message += "computeRecursivelyUsingBigInteger: " + time + " ms\n";
        		reportResultAndTime("computeRecursivelyUsingBigInteger", rBig, time);

        		gcAndWait();

        		startMethodTracing("computeRecursivelyWithLoopUsingBigInteger");
        		time = System.currentTimeMillis();
        		rBig = Fibonacci.computeRecursivelyWithLoopUsingBigInteger(n);
        		time = System.currentTimeMillis() - time;
        		stopMethodTracing();
        		message += "computeRecursivelyWithLoopUsingBigInteger: " + time + " ms\n";
        		reportResultAndTime("computeRecursivelyWithLoopUsingBigInteger", rBig, time);

        		gcAndWait();
        	}

        	startMethodTracing("computeIterativelyUsingBigInteger");
        	time = System.currentTimeMillis();
        	rBig = Fibonacci.computeIterativelyUsingBigInteger(n);
        	time = System.currentTimeMillis() - time;
        	stopMethodTracing();
        	message += "computeIterativelyUsingBigInteger: " + time + " ms\n";
        	reportResultAndTime("computeIterativelyUsingBigInteger", rBig, time);

        	gcAndWait();

        	startMethodTracing("computeIterativelyFasterUsingBigInteger");
        	time = System.currentTimeMillis();
        	rBig = Fibonacci.computeIterativelyFasterUsingBigInteger(n);
        	time = System.currentTimeMillis() - time;
        	stopMethodTracing();
        	message += "computeIterativelyFasterUsingBigInteger: " + time + " ms\n";
        	reportResultAndTime("computeIterativelyFasterUsingBigInteger", rBig, time);

        	gcAndWait();

        	startMethodTracing("computeIterativelyFasterUsingBigIntegerReturnZero");
        	time = System.currentTimeMillis();
        	rBig = Fibonacci.computeIterativelyFasterUsingBigIntegerReturnZero(n);
        	time = System.currentTimeMillis() - time;
        	stopMethodTracing();
        	message += "computeIterativelyFasterUsingBigIntegerReturnZero: " + time + " ms\n";
        	reportResultAndTime("computeIterativelyFasterUsingBigIntegerReturnZero", rBig, time);

        	gcAndWait();
        }

    	startMethodTracing("computeRecursivelyFasterUsingBigInteger");
    	time = System.currentTimeMillis();
        rBig = Fibonacci.computeRecursivelyFasterUsingBigInteger(n);
        time = System.currentTimeMillis() - time;
    	stopMethodTracing();
    	message += "computeRecursivelyFasterUsingBigInteger: " + time + " ms\n";
        reportResultAndTime("computeRecursivelyFasterUsingBigInteger", rBig, time);
        reportResult("calls to computeRecursivelyFasterUsingBigInteger", Fibonacci.computeRecursivelyFasterCalls(n, 2));
        
        gcAndWait();

    	startMethodTracing("computeRecursivelyFasterUsingBigIntegerReturnZero");
    	time = System.currentTimeMillis();
        rBig = Fibonacci.computeRecursivelyFasterUsingBigIntegerReturnZero(n);
        time = System.currentTimeMillis() - time;
    	stopMethodTracing();
    	message += "computeRecursivelyFasterUsingBigIntegerReturnZero: " + time + " ms\n";
        reportResultAndTime("computeRecursivelyFasterUsingBigIntegerReturnZero", rBig, time);
        
        gcAndWait();

    	startMethodTracing("computeRecursivelyFasterUsingBigIntegerAndTable16");
    	time = System.currentTimeMillis();
        rBig = Fibonacci.computeRecursivelyFasterUsingBigIntegerAndTable16(n);
        time = System.currentTimeMillis() - time;
    	stopMethodTracing();
    	message += "computeRecursivelyFasterUsingBigIntegerAndTable16: " + time + " ms\n";
        reportResultAndTime("computeRecursivelyFasterUsingBigIntegerAndTable16", rBig, time);
        reportResult("calls to computeRecursivelyFasterUsingBigIntegerAndTable16", Fibonacci.computeRecursivelyFasterCalls(n, 16));

        gcAndWait();

    	startMethodTracing("computeRecursivelyFasterUsingBigIntegerAndTable64");
    	time = System.currentTimeMillis();
        rBig = Fibonacci.computeRecursivelyFasterUsingBigIntegerAndTable64(n);
        time = System.currentTimeMillis() - time;
    	stopMethodTracing();
    	message += "computeRecursivelyFasterUsingBigIntegerAndTable64: " + time + " ms\n";
        reportResultAndTime("computeRecursivelyFasterUsingBigIntegerAndTable64", rBig, time);

        gcAndWait();

    	startMethodTracing("computeRecursivelyFasterUsingBigIntegerAndTable128");
    	time = System.currentTimeMillis();
        rBig = Fibonacci.computeRecursivelyFasterUsingBigIntegerAndTable128(n);
        time = System.currentTimeMillis() - time;
    	stopMethodTracing();
    	message += "computeRecursivelyFasterUsingBigIntegerAndTable128: " + time + " ms\n";
        reportResultAndTime("computeRecursivelyFasterUsingBigIntegerAndTable128", rBig, time);

        gcAndWait();

    	startMethodTracing("computeRecursivelyFasterUsingBigIntegerAndTable");
    	time = System.currentTimeMillis();
        rBig = Fibonacci.computeRecursivelyFasterUsingBigIntegerAndTable(n);
        time = System.currentTimeMillis() - time;
    	stopMethodTracing();
    	message += "computeRecursivelyFasterUsingBigIntegerAndTable: " + time + " ms\n";
        reportResultAndTime("computeRecursivelyFasterUsingBigIntegerAndTable", rBig, time);
        reportResult("calls to computeRecursivelyFasterUsingBigIntegerAndTable", Fibonacci.computeRecursivelyFasterCalls(n, Fibonacci.PRECOMPUTED_SIZE));
        
        gcAndWait();

    	startMethodTracing("computeRecursivelyFaster2UsingBigIntegerAndTable");
    	time = System.currentTimeMillis();
        rBig = Fibonacci.computeRecursivelyFaster2UsingBigIntegerAndTable(n);
        time = System.currentTimeMillis() - time;
    	stopMethodTracing();
    	message += "computeRecursivelyFaster2UsingBigIntegerAndTable: " + time + " ms\n";
        reportResultAndTime("computeRecursivelyFaster2UsingBigIntegerAndTable", rBig, time);
        
        gcAndWait();
        
    	startMethodTracing("computeRecursivelyFasterUsingPrimitiveAndBigInteger");
    	time = System.currentTimeMillis();
        rBig = Fibonacci.computeRecursivelyFasterUsingPrimitiveAndBigInteger(n);
        time = System.currentTimeMillis() - time;
    	stopMethodTracing();
    	message += "computeRecursivelyFasterUsingPrimitiveAndBigInteger: " + time + " ms\n";
        reportResultAndTime("computeRecursivelyFasterUsingPrimitiveAndBigInteger", rBig, time);
        
        gcAndWait();
        
    	startMethodTracing("computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndSparseArray");
    	time = System.currentTimeMillis();
        rBig = Fibonacci.computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndSparseArray(n);
        time = System.currentTimeMillis() - time;
    	stopMethodTracing();
    	message += "computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndSparseArray: " + time + " ms\n";
        reportResultAndTime("computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndSparseArray", rBig, time);
        
        gcAndWait();
        
    	startMethodTracing("computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndHashMap");
    	time = System.currentTimeMillis();
        rBig = Fibonacci.computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndHashMap(n);
        time = System.currentTimeMillis() - time;
    	stopMethodTracing();
    	message += "computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndHashMap: " + time + " ms\n";
        reportResultAndTime("computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndHashMap", rBig, time);
        
        gcAndWait();
        
    	startMethodTracing("computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndHashMapAndThreadingBad");
    	time = System.currentTimeMillis();
        rBig = Fibonacci.computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndHashMapAndThreadingBad(n);
        time = System.currentTimeMillis() - time;
    	stopMethodTracing();
    	message += "computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndHashMapAndThreadingBad: " + time + " ms\n";
        reportResultAndTime("computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndHashMapAndThreadingBad", rBig, time);
        
        gcAndWait();
        
    	startMethodTracing("recursiveFasterPrimitiveAndBigIntegerAndHashMapAndThreading");
    	time = System.currentTimeMillis();
        rBig = Fibonacci.recursiveFasterPrimitiveAndBigIntegerAndHashMapAndThreading(n);
        time = System.currentTimeMillis() - time;
    	stopMethodTracing();
    	message += "recursiveFasterPrimitiveAndBigIntegerAndHashMapAndThreading: " + time + " ms\n";
        reportResultAndTime("recursiveFasterPrimitiveAndBigIntegerAndHashMapAndThreading", rBig, time);
        
        gcAndWait();
        
    	startMethodTracing("computeRecursivelyFasterUsingBigIntegerAndThreadingNoDependencies");
    	time = System.currentTimeMillis();
        rBig = Fibonacci.computeRecursivelyFasterUsingBigIntegerAndThreadingNoDependencies(n);
        time = System.currentTimeMillis() - time;
    	stopMethodTracing();
    	message += "computeRecursivelyFasterUsingBigIntegerAndThreadingNoDependencies: " + time + " ms\n";
        reportResultAndTime("computeRecursivelyFasterUsingBigIntegerAndThreadingNoDependencies", rBig, time);
        
        gcAndWait();
        
    	startMethodTracing("computeRecursivelyFasterUsingBigIntegerAndThreadingNoDependencies2");
    	time = System.currentTimeMillis();
        rBig = Fibonacci.computeRecursivelyFasterUsingBigIntegerAndThreadingNoDependencies2(n);
        time = System.currentTimeMillis() - time;
    	stopMethodTracing();
    	message += "computeRecursivelyFasterUsingBigIntegerAndThreadingNoDependencies2: " + time + " ms\n";
        reportResultAndTime("computeRecursivelyFasterUsingBigIntegerAndThreadingNoDependencies2", rBig, time);
        
        gcAndWait();
        
    	startMethodTracing("computeRecursivelyFasterUsingPrimitiveLongAndBigIntegerAndThreading");
    	time = System.currentTimeMillis();
        rBig = Fibonacci.computeRecursivelyFasterUsingPrimitiveLongAndBigIntegerAndThreading(n);
        time = System.currentTimeMillis() - time;
    	stopMethodTracing();
    	message += "computeRecursivelyFasterUsingPrimitiveLongAndBigIntegerAndThreading: " + time + " ms\n";
        reportResultAndTime("computeRecursivelyFasterUsingPrimitiveLongAndBigIntegerAndThreading", rBig, time);
        
        gcAndWait();
        
    	startMethodTracing("computeRecursivelyFasterUsingPrimitiveLongAndBigIntegerAndThreadingOneThread");
    	time = System.currentTimeMillis();
        rBig = Fibonacci.computeRecursivelyFasterUsingPrimitiveLongAndBigIntegerAndThreadingOneThread(n);
        time = System.currentTimeMillis() - time;
    	stopMethodTracing();
    	message += "computeRecursivelyFasterUsingPrimitiveLongAndBigIntegerAndThreadingOneThread: " + time + " ms\n";
        reportResultAndTime("computeRecursivelyFasterUsingPrimitiveLongAndBigIntegerAndThreadingOneThread", rBig, time);
        
        gcAndWait();
        
    	startMethodTracing("computeRecursivelyFasterUsingPrimitiveLongAndBigIntegerAndThreading2");
    	time = System.currentTimeMillis();
        rBig = Fibonacci.computeRecursivelyFasterUsingPrimitiveLongAndBigIntegerAndThreading2(n);
        time = System.currentTimeMillis() - time;
    	stopMethodTracing();
    	message += "computeRecursivelyFasterUsingPrimitiveLongAndBigIntegerAndThreading2: " + time + " ms\n";
        reportResultAndTime("computeRecursivelyFasterUsingPrimitiveLongAndBigIntegerAndThreading2", rBig, time);
        
        if (nativeTracingEnabled) {
    		Debug.stopNativeTracing();
    	}
        
        return message;
    }
    
    private void gcAndWait() {
    	if (garbageCollectionEnabled) {
    		System.gc();
    	}
    	if (delayEnabled) {
    		try {
    			Thread.sleep(delay);
    		} catch (InterruptedException e) {
    			System.out.println(e.getStackTrace());
    		}
    	}
    }
    
    private void reportResult(String s, Object result) {
    	Log.i(TAG, s + ": " + result);
    }

    private void reportResultAndTime(String s, Object result, long time) {
    	Log.i(TAG, s + " (" + time + " ms): " + result);
    }
    
    private void startMethodTracing(String traceName) {
    	if (methodTracingEnabled) {
    		Debug.startMethodTracing(traceName);
    	}
    }
    
    private void stopMethodTracing() {
    	if (methodTracingEnabled) {
    		Debug.stopMethodTracing();
    	}
    }
    
    private void alertResults(String message) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog ad = builder.create();
        //ad.setTitle(R.string.resultDialogTitle);
        ad.setMessage(message.trim());
        ad.show();
    }
    
    public synchronized void doSomething (Object o) {
    	// do something with object
    }
    
    public void doSomething2 (Object o) {
    	synchronized (o) {
        	// do something with object
    	}
    	
    	// do something else here, no locking needed
    }
    
    private static final int ITERATIONS = 1000000;
    
    private static void testFibonacci (int n) {
    	long time = System.currentTimeMillis();
    	for (int i = 0; i < ITERATIONS; i++) {
    		// call iterativeFaster(n) or iterativeFasterNative(n) here
    	}
    	time = System.currentTimeMillis() - time;
    	Log.i("Fibonacci", String.valueOf(n)+"> Total time: " + time + " milliseconds");
    }
    
    private static void testFibonacci () {
    	for (int i = 0; i <= 92; i++) {
    		testFibonacci(i);
    	}
    }
    
    private void foo() {
    	MyThread thread = new MyThread("");
    	thread.start();
    	
    	// later...
    	Handler handler = thread.getHandler();
    	
    	// to post a runnable
    	handler.post(new Runnable() {
			public void run() {
				Log.i(TAG, "Where am I? " + Thread.currentThread().getName());
			}
    	});
    	
    	// to send a message
    	int what = 0; // define your own values
    	int arg1 = 1;
    	int arg2 = 2;
    	Message msg = Message.obtain(handler, what, arg1, arg2);
    	handler.sendMessage(msg);
    	
    	// another message...
    	what = 1;
    	msg = Message.obtain(handler, what, new Long(Thread.currentThread().getId()));
    	handler.sendMessageAtFrontOfQueue(msg);
    }
    
    private void createTwoThreads() {
    	// the run() method can simply be overridden...
        Thread thread1 = new Thread("cheese1") {
            @Override
            public void run() {
                Log.i(TAG, "I like Munster");
            }
        };
        
        // ...or a Runnable object can be passed to the Thread constructor
        Thread thread2 = new Thread(new Runnable() {
            public void run() {
                Log.i(TAG, "I like Roquefort");
            }
        }, "cheese2");

        // remember to call start() else the threads won’t be spawned and nothing will happen
        thread1.start();
        thread2.start();
    }
  
    private void createThreadWithPriority() {
    	Thread thread = new Thread("thread name") {
            @Override
            public void run() {
                // do something here
            }
        };
        thread.setPriority(Thread.MAX_PRIORITY); // highest priority (higher than UI thread)
        thread.start();
    }
    
    public void onClickGood (View v) {
        new Thread(new Runnable() {
            public void run() {
                // note the 'final' keyword here (try removing it and see what happens)
                final BigInteger f = Fibonacci.computeRecursivelyFasterUsingPrimitiveAndBigInteger(100000);
                mTextView.post(new Runnable() {
                    public void run() {
                        mTextView.setText(f.toString());
                    }
                });
            }
        }, "fibonacci").start();
    }

    public void onClickBad (View v) {
        new Thread(new Runnable() {
            public void run() {
                BigInteger f = Fibonacci.computeRecursivelyFasterUsingPrimitiveAndBigInteger(100000);
                mTextView.setText(f.toString()); // will throw an exception
            }
        }, "fibonacci").start();
    }

    public void onClickCreateTwoThreads (View v) {
    	createTwoThreads();
    }
    
    public void onClickCreateThreadWithPriority (View v) {
    	createThreadWithPriority();
    }
    
    public void onClickAsyncTask (View v) {
        // AsyncTask<Params, Progress, Result> anonymous class 
        new AsyncTask<Integer, Void, BigInteger>() {
            @Override
            protected BigInteger doInBackground(Integer... params) {
                return Fibonacci.computeRecursivelyFasterUsingPrimitiveAndBigInteger(params[0]);
            }

            @Override
            protected void onPostExecute(BigInteger result) {
                mTextView.setText(result.toString());
            }
        }.execute(100000);
    }

    public void onClickCreateMyThread (View v) {
    	MyThread thread = new MyThread("looper thread");
        thread.start();
        
        try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        // later...
        Handler handler = thread.getHandler(); // careful: this could return null if the handler is not initialized yet
        
        // to post a runnable
        handler.post(new Runnable() {
            public void run() {
                Log.i(TAG, "Where am I? " + Thread.currentThread().getName());
            }
        });
        
        // to send a message
        int what = 0; // define your own values
        int arg1 = 1;
        int arg2 = 2;
        Message msg = Message.obtain(handler, what, arg1, arg2);
        handler.sendMessage(msg);
        
        // another message...
        what = 1;
        msg = Message.obtain(handler, what, new Long(Thread.currentThread().getId()));
        handler.sendMessageAtFrontOfQueue(msg);
        
        // to exit...
        what = 2;
        msg = Message.obtain(handler, what, new Long(Thread.currentThread().getId()));
        handler.sendMessage(msg);
    }
    
    public void onClickCreateMyHandlerThread (View v) {
    	MyHandlerThread thread = new MyHandlerThread("handler thread");
        thread.start();
        
        // later...
        Handler handler = thread.getHandler(); // careful: this could return null if the handler is not initialized yet
        
        // to post a runnable
        handler.post(new Runnable() {
            public void run() {
                Log.i(TAG, "Where am I? " + Thread.currentThread().getName());
            }
        });
        
        // to send a message
        int what = 0; // define your own values
        int arg1 = 1;
        int arg2 = 2;
        Message msg = Message.obtain(handler, what, arg1, arg2);
        handler.sendMessage(msg);
        
        // another message...
        what = 1;
        msg = Message.obtain(handler, what, new Long(Thread.currentThread().getId()));
        handler.sendMessageAtFrontOfQueue(msg);
        
        // to exit...
        what = 2;
        msg = Message.obtain(handler, what, new Long(Thread.currentThread().getId()));
        handler.sendMessage(msg);
    }
    
    public void onClickDownloadFiles (View v) {
    	AsyncTask<String, Object, Void> task = new AsyncTask<String, Object, Void>() {

    	    private ByteArrayBuffer downloadFile(String urlString, byte[] buffer) {
    	        try {
    	            URL url = new URL(urlString);
    	            URLConnection connection = url.openConnection();
    	            InputStream is = connection.getInputStream();
    	            //Log.i(TAG, "InputStream: " + is.getClass().getName()); // if you are curious
    	            //is = new BufferedInputStream(is); // optional line, try with and without
    	            ByteArrayBuffer baf = new ByteArrayBuffer(640 * 1024); // ought to be enough for everybody?
    	            int len;
    	            while ((len = is.read(buffer)) != -1) {
    	                baf.append(buffer, 0, len);
    	            }
    	            return baf;
    	        } catch (MalformedURLException e) {
    	            return null;
    	        } catch (IOException e) {
    	            return null;
    	        }
    	    }

    	    @Override
    	    protected Void doInBackground(String... params) {
    	        if (params != null && params.length > 0) {
    	            byte[] buffer = new byte[4 * 1024]; // try different sizes (1 for example will give lower performance)
    	            for (String url : params) {
    	                long time = System.currentTimeMillis();
    	                ByteArrayBuffer baf = downloadFile(url, buffer);
    	                time = System.currentTimeMillis() - time;
    	                publishProgress(url, baf, time);
    	            }
    	        } else {
    	            publishProgress(null, null);
    	        }
    	        return null; // we don’t care about any result but we still have to return something
    	    }

    	    @Override
    	    protected void onProgressUpdate(Object... values) {
    	        // values[0] is the URL (String), values[1] is the buffer (ByteArrayBuffer), values[2] is the duration
    	        String url = (String) values[0];
    	        ByteArrayBuffer buffer = (ByteArrayBuffer) values[1];
    	        if (buffer != null) {
    	            long time = (Long) values[2];
    	            Log.i(TAG, "Downloaded " + url + " (" + buffer.length() + " bytes) in " + time + " milliseconds");
    	        } else {
    	            Log.w(TAG, "Could not download " + url);
    	        }

    	        // update UI accordingly, etc
    	    }
    	};

    	String url1 = "http://www.google.com/index.html";
    	String url2 = "http://d.android.com/reference/android/os/AsyncTask.html";
    	task.execute(url1, url2);	
    	//task.execute("http://d.android.com/resources/articles/painless-threading.html"); // try that to get exception
    }
}
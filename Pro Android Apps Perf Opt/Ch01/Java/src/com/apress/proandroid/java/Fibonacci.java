package com.apress.proandroid.java;

import java.math.BigInteger;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.os.StrictMode;
import android.util.Log;
import android.util.SparseArray;

public class Fibonacci {
	private static final String TAG = "Fibonacci";
	private static ExecutorService executorService;
	
	public Fibonacci() {
	}
	
	/**
	 * Naive recursive implementation.
	 * 
	 * @param n
	 * @return The n-th Fibonacci number
	 */
	public static long computeRecursively (int n) {
		//StrictMode.noteSlowCall("computeRecursively can be very slow"); // API level 11
		if (n > 1) {
			return computeRecursively(n-2) + computeRecursively(n-1);
		}
		return n;
	}
	
	/**
	 * Naive recursive implementation using int instead of long.
	 * 
	 * @param n
	 * @return The n-th Fibonacci number
	 */
	public static int computeRecursivelyUsingPrimitiveInt (int n) {
		if (n > 1) {
			return computeRecursivelyUsingPrimitiveInt(n-2) + computeRecursivelyUsingPrimitiveInt(n-1);
		}
		return n;
	}
	
	/**
	 * Naive recursive implementation (non-static).
	 * 
	 * @param n
	 * @return The n-th Fibonacci number
	 */
	public long computeRecursivelyVirtual (int n) {
		if (n > 1) {
			return computeRecursivelyVirtual(n-2) + computeRecursivelyVirtual(n-1);
		}
		return n;
	}
	
	/**
	 * Naive implementation with a loop.
	 * 
	 * @param n
	 * @return The n-th Fibonacci number
	 */
	public static long computeRecursivelyWithLoop (int n) {
		if (n > 1) {
			long result = 1;
			do {
				result += computeRecursivelyWithLoop(n-2);
				n--;
			} while (n > 1);
			return result;
		}
		return n;
	}
	
	/**
	 * Naive iterative implementation.
	 * 
	 * @param n
	 * @return The n-th Fibonacci number
	 */
	public static long computeIteratively (int n) {
		if (n > 1) {
			long a = 0, b = 1;
			do {
				long tmp = b;
				b += a;
				a = tmp;
			} while (--n > 1);
			return b;
		}
		return n;
	}
	
	/**
	 * Faster iterative implementation, cutting the number of iterations by half.
	 * 
	 * @param n
	 * @return The n-th Fibonacci number (as a long)
	 */
	public static long computeIterativelyFaster (int n) {
		if (n > 1) {
			long a, b = 1;
			n--;
			a = n & 1;
			n /= 2;
			while (n-- > 0) {
				a += b;
				b += a;
			}
			return b;
		}
		return n;
	}
	
	/**
	 * Faster iterative implementation, cutting the number of iterations by half.
	 * 
	 * @param n
	 * @return The n-th Fibonacci number (as an int)
	 */
	public static int computeIterativelyFasterUsingPrimitiveInt (int n) {
		if (n > 1) {
			int a, b = 1;
			n--;
			a = n & 1;
			n /= 2;
			while (n-- > 0) {
				a += b;
				b += a;
			}
			return b;
		}
		return n;
	}
	
	/**
	 * Faster recursive implementation (based on Fibonacci matrix).
	 * 
	 * @param n
	 * @return The n-th Fibonacci number
	 */
	public static long computeRecursivelyFaster (int n) {
		if (n > 1) {
			int m = (n / 2) + (n & 1);
			long fM   = computeRecursivelyFaster(m);
			long fM_1 = computeRecursivelyFaster(m-1);
			if ((n % 2) == 1) {
				// F(2n-1) = F(n)^2 + F(n-1)^2
				return (fM * fM) + (fM_1 * fM_1);
			} else {
				// F(2n) = [2*F(n-1) + F(n)] * F(n)
				return ((2 * fM_1) + fM) * fM;
			}
		}
		return n;
	}

	/**
	 * Naive recursive implementation.
	 * 
	 * @param n
	 * @return The n-th Fibonacci number as a Long
	 */
	public static Long computeRecursivelyUsingLong (int n) {
		if (n > 1) {
			return computeRecursivelyUsingLong(n-2) + computeRecursivelyUsingLong(n-1);
		}
		return Long.valueOf(n);
	}
	
	/**
	 * Naive implementation with tail recursion optimized.
	 * Note that the compiler could already optimize the tail recursion in naive implementation above.
	 * 
	 * @param n
	 * @return The n-th Fibonacci number as a Long
	 */
	public static Long computeRecursivelyWithLoopUsingLong (int n) {
		if (n > 1) {
			Long result = 1L;
			while (n > 1) {
				result += computeRecursivelyWithLoopUsingLong(n-2);
				n--;
			}
			return result;
		}
		return Long.valueOf(n);
	}
	
	/**
	 * Naive iterative implementation.
	 * 
	 * @param n
	 * @return The n-th Fibonacci number as a Long
	 */
	public static Long computeIterativelyUsingLong (int n) {
		if (n > 1) {
			Long a = 0L, b = 1L;
			do {
				Long tmp = b;
				b += a;
				a = tmp;
			} while (--n > 1);
			return b;
		}
		return Long.valueOf(n);
	}
	
	/**
	 * Faster iterative implementation, cutting the number of iterations by half.
	 * 
	 * @param n
	 * @return The n-th Fibonacci number as a Long
	 */
	public static Long computeIterativelyFasterUsingLong (int n) {
		if (n > 1) {
			Long a, b = 1L;
			n--;
			a = Long.valueOf(n & 1);
			n /= 2;
			while (n-- > 0) {
				a += b;
				b += a;
			}
			return b;
		}
		return Long.valueOf(n);
	}
	
	/**
	 * Faster recursive implementation (based on Fibonacci matrix).
	 * 
	 * @param n
	 * @return The n-th Fibonacci number as a Long
	 */
	public static Long computeRecursivelyFasterUsingLong (int n) {
		if (n > 1) {
			int m = (n / 2) + (n & 1);
			Long fM   = computeRecursivelyFasterUsingLong(m);
			Long fM_1 = computeRecursivelyFasterUsingLong(m-1);
			if ((n % 2) == 1) {
				// F(2n-1) = F(n)^2 + F(n-1)^2
				return (fM * fM) + (fM_1 * fM_1);
			} else {
				// F(2n) = [2*F(n-1) + F(n)] * F(n)
				return ((2 * fM_1) + fM) * fM;
			}
		}
		return Long.valueOf(n);
	}

	/**
	 * Naive recursive implementation.
	 * 
	 * @param n
	 * @return The n-th Fibonacci number as a BigInteger
	 */
	public static BigInteger computeRecursivelyUsingBigInteger (int n) {
		if (n > 1) {
			return computeRecursivelyUsingBigInteger(n-1).add(computeRecursivelyUsingBigInteger(n-2));
		}
		return (n==0)?BigInteger.ZERO:BigInteger.ONE;
	}
	
	/**
	 * Naive implementation with tail recursion optimized.
	 * Note that the compiler could already optimize the tail recursion in naive implementation above.
	 * 
	 * @param n
	 * @return The n-th Fibonacci number as a BigInteger
	 */
	public static BigInteger computeRecursivelyWithLoopUsingBigInteger (int n) {
		if (n > 1) {
			BigInteger result = BigInteger.ONE;
			while (n > 1) {
				result = result.add(computeRecursivelyWithLoopUsingBigInteger(n-2));
				n--;
			}
			return result;
		}
		return (n==0)?BigInteger.ZERO:BigInteger.ONE;
	}
	
	/**
	 * Naive iterative implementation.
	 * 
	 * @param n
	 * @return The n-th Fibonacci number as a BigInteger
	 */
	public static BigInteger computeIterativelyUsingBigInteger (int n) {
		if (n > 1) {
			BigInteger a = BigInteger.ZERO, b = BigInteger.ONE;
			do {
				BigInteger tmp = b;
				b = b.add(a);
				a = tmp;
			} while (--n > 1);
			return b;
		}
		return (n==0)?BigInteger.ZERO:BigInteger.ONE;
	}
	
	/**
	 * Faster iterative implementation, cutting the number of iterations by half. Always return 0.
	 * Always returning 0 allows you to see how much time the additions actually take in the real
	 * implementation (computeIterativelyFasterUsingBigInteger). Here, adding two zeros will be fast.
	 * 
	 * @param n
	 * @return The n-th Fibonacci number as a BigInteger
	 */
	public static BigInteger computeIterativelyFasterUsingBigIntegerReturnZero (int n) {
		if (n > 1) {
			BigInteger a = BigInteger.ZERO, b = BigInteger.ZERO;
			n--;
			n /= 2;
			while (n-- > 0) {
				a = a.add(b);
				b = b.add(a);
			}
			return b; // will always be zero
		}
		return BigInteger.ZERO;
	}
	
	/**
	 * Faster iterative implementation, cutting the number of iterations by half.
	 * 
	 * @param n
	 * @return The n-th Fibonacci number as a BigInteger
	 */
	public static BigInteger computeIterativelyFasterUsingBigInteger (int n) {
		if (n > 1) {
			BigInteger a, b = BigInteger.ONE;
			n--;
			a = ((n & 1) == 1) ? BigInteger.ONE : BigInteger.ZERO;
			n /= 2;
			while (n-- > 0) {
				a = a.add(b);
				b = b.add(a);
			}
			return b;
		}
		return (n==0)?BigInteger.ZERO:BigInteger.ONE;
	}
	
	/**
	 * Faster recursive implementation (based on Fibonacci matrix).
	 * 
	 * @param n
	 * @return The n-th Fibonacci number as a BigInteger
	 */
	public static BigInteger computeRecursivelyFasterUsingBigInteger (int n) {
		if (n > 1) {
			int m = (n / 2) + (n & 1);
			BigInteger fM   = computeRecursivelyFasterUsingBigInteger(m);
			BigInteger fM_1 = computeRecursivelyFasterUsingBigInteger(m-1);
			if ((n & 1) == 1) {
				// F(2m-1) = F(m)^2 + F(m-1)^2
				return fM.pow(2).add(fM_1.pow(2));
			} else {
				// F(2m) = [2*F(m-1) + F(m)] * F(m)
				return fM_1.shiftLeft(1).add(fM).multiply(fM);
			}
		}
		return (n==0)?BigInteger.ZERO:BigInteger.ONE;
	}
	
	/**
	 * Faster recursive implementation (based on Fibonacci matrix). Always return 0.
	 * 
	 * @param n
	 * @return The n-th Fibonacci number as a BigInteger
	 */
	public static BigInteger computeRecursivelyFasterUsingBigIntegerReturnZero (int n) {
		if (n > 1) {
			int m = (n / 2) + (n & 1);
			BigInteger fM   = computeRecursivelyFasterUsingBigIntegerReturnZero(m);
			BigInteger fM_1 = computeRecursivelyFasterUsingBigIntegerReturnZero(m-1);
			if ((n & 1) == 1) {
				// F(2m-1) = F(m)^2 + F(m-1)^2
				return fM.pow(2).add(fM_1.pow(2)); // will always be zero
			} else {
				// F(2m) = [2*F(m-1) + F(m)] * F(m)
				return fM_1.shiftLeft(1).add(fM).multiply(fM); // will always be zero
			}
		}
		return BigInteger.ZERO;
	}
	
	/**
	 * Faster recursive implementation (based on Fibonacci matrix), switching to long computations when n is small enough.
	 * 
	 * @param n
	 * @return The n-th Fibonacci number as a BigInteger
	 */
	public static BigInteger computeRecursivelyFasterUsingPrimitiveAndBigInteger (int n) {
		if (n > 92) {
			int m = (n / 2) + (n & 1);
			BigInteger fM   = computeRecursivelyFasterUsingPrimitiveAndBigInteger(m);
			BigInteger fM_1 = computeRecursivelyFasterUsingPrimitiveAndBigInteger(m-1);
			if ((n & 1) != 0) {
				// F(2m-1) = F(m)^2 + F(m-1)^2
				return fM.pow(2).add(fM_1.pow(2));
			} else {
				// F(2m) = [2*F(m-1) + F(m)] * F(m)
				return fM_1.shiftLeft(1).add(fM).multiply(fM);
			}
		}
		return BigInteger.valueOf(computeIterativelyFaster(n));
	}
	
	/**
	 * Faster recursive implementation (based on Fibonacci matrix), switching to long computations when n is small enough and using a cache.
	 * 
	 * @param n
	 * @return The n-th Fibonacci number as a BigInteger
	 */
	private static BigInteger computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndSparseArray (int n, SparseArray<BigInteger> cache) {
		if (n > 92) {
			BigInteger fN = cache.get(n);
			if (fN == null) {
				int m = (n / 2) + (n & 1);
				BigInteger fM   = computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndSparseArray(m, cache);
				BigInteger fM_1 = computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndSparseArray(m-1, cache);
				if ((n & 1) != 0) {
					// F(2m-1) = F(m)^2 + F(m-1)^2
					fN = fM.pow(2).add(fM_1.pow(2));
				} else {
					// F(2m) = [2*F(m-1) + F(m)] * F(m)
					fN = fM_1.shiftLeft(1).add(fM).multiply(fM);
				}
				cache.put(n, fN);
			}
			return fN;
		}
		return BigInteger.valueOf(computeIterativelyFaster(n));
	}
	
	/**
	 * Faster recursive implementation (based on Fibonacci matrix), switching to long computations when n is small enough and using a cache.
	 * 
	 * @param n
	 * @return The n-th Fibonacci number as a BigInteger
	 */
	public static BigInteger computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndSparseArray (int n) {
		SparseArray<BigInteger> cache = new SparseArray<BigInteger>();
		return computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndSparseArray(n, cache);
	}
	
	/**
	 * Faster recursive implementation (based on Fibonacci matrix), switching to long computations when n is small enough and using a cache.
	 * 
	 * @param n
	 * @return The n-th Fibonacci number as a BigInteger
	 */
	private static BigInteger computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndHashMap (int n, AbstractMap<Integer, BigInteger> cache) {
		if (n > 92) {
			BigInteger fN = cache.get(n);
			if (fN == null) {
				int m = (n / 2) + (n & 1);
				BigInteger fM   = computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndHashMap(m, cache);
				BigInteger fM_1 = computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndHashMap(m-1, cache);
				if ((n & 1) != 0) {
					// F(2m-1) = F(m)^2 + F(m-1)^2
					fN = fM.pow(2).add(fM_1.pow(2));
				} else {
					// F(2m) = [2*F(m-1) + F(m)] * F(m)
					fN = fM_1.shiftLeft(1).add(fM).multiply(fM);
				}
				cache.put(n, fN);
			}
			return fN;
		}
		return BigInteger.valueOf(computeIterativelyFaster(n));
	}
	
	/**
	 * Faster recursive implementation (based on Fibonacci matrix), switching to long computations when n is small enough and using a cache.
	 * 
	 * @param n
	 * @return The n-th Fibonacci number as a BigInteger
	 */
	public static BigInteger computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndHashMap (int n) {
		HashMap<Integer, BigInteger> cache = new HashMap<Integer, BigInteger>();
		return computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndHashMap(n, cache);
	}
	
    public static BigInteger computeRecursivelyFasterWithCache (int n)
    {
        SparseArray<BigInteger> cache = new SparseArray<BigInteger>();
        return computeRecursivelyFasterWithCache(n, cache);
    }
    
    private static BigInteger computeRecursivelyFasterWithCache (int n, SparseArray<BigInteger> cache)
    {
        if (n > 92) {
            BigInteger fN = cache.get(n);
            if (fN == null) {
                int m = (n / 2) + (n & 1);
                BigInteger fM = computeRecursivelyFasterWithCache(m, cache);
                BigInteger fM_1 = computeRecursivelyFasterWithCache(m - 1, cache);
                if ((n & 1) == 1) {
                    fN = fM.pow(2).add(fM_1.pow(2));
                } else {
                    fN = fM_1.shiftLeft(1).add(fM).multiply(fM);
                }
                cache.put(n, fN);
            }
            return fN;
        }
        return BigInteger.valueOf(computeIterativelyFaster(n));
    }

    private static BigInteger computeRecursivelyWithCache (int n)
    {
    	return computeRecursivelyFasterWithCache(n);
    }
    
	/**
	 * Faster recursive implementation (based on Fibonacci matrix), switching to long computations when n is small enough and using a cache and multithreading.
	 * 
	 * @param n
	 * @return The n-th Fibonacci number as a BigInteger
	 */
    public static BigInteger computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndHashMapAndThreadingBad (int n) {
    	int proc = Runtime.getRuntime().availableProcessors();
    	if (n < 128 || proc <= 1) {
    		return computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndHashMap(n);
    	}
    	
    	// there may be 2 or more CPUs (or cores) available - let's use 2 for simplicity for now (half of the work in current thread, half in possibly another)
    	
    	final int m = (n / 2) + (n & 1);
        Callable<BigInteger> callable = new Callable<BigInteger>() {
			public BigInteger call() throws Exception {
				return computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndHashMap(m);
			}
        };
        Future<BigInteger> ffM = executorService.submit(callable);
        
        BigInteger fM;
        BigInteger fM_1;
        BigInteger fN;

        fM_1 = computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndHashMap(m-1);

        try {
        	fM = ffM.get();
		} catch (Exception e) {
			fM = computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndHashMap(m);
		}
		
		if ((n & 1) != 0) {
			// F(2m-1) = F(m)^2 + F(m-1)^2
			fN = fM.pow(2).add(fM_1.pow(2));
		} else {
			// F(2m) = [2*F(m-1) + F(m)] * F(m)
			fN = fM_1.shiftLeft(1).add(fM).multiply(fM);
		}
		
		return fN;
    }

	/**
	 * Faster recursive implementation (based on Fibonacci matrix), switching to long computations when n is small enough and using a cache and multithreading.
	 * 
	 * @param n
	 * @return The n-th Fibonacci number as a BigInteger
	 */
    public static BigInteger recursiveFasterPrimitiveAndBigIntegerAndHashMapAndThreading (int n) {
    	int proc = Runtime.getRuntime().availableProcessors();
    	if (n < 128 || proc <= 1) {
    		return computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndHashMap(n);
    	}
    	
    	// there may be 2 or more CPUs (or cores) available - let's use 2 for simplicity for now (half of the work in current thread, half in possibly another)
    	
    	final ConcurrentHashMap<Integer, BigInteger> cache = new ConcurrentHashMap<Integer, BigInteger>();
    	final int m = (n / 2) + (n & 1);
        Callable<BigInteger> callable = new Callable<BigInteger>() {
			public BigInteger call() throws Exception {
				return computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndHashMap(m, cache);
			}
        };
        // first, submit callable
        Future<BigInteger> ffM = executorService.submit(callable);
        
        BigInteger fM;
        BigInteger fM_1;
        BigInteger fN;

        // second, compute fM_1 in current thread (fM is being computed in parallel)
        fM_1 = computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndHashMap(m-1, cache);

        // third, get fM value
        try {
        	fM = ffM.get();
		} catch (Exception e) {
			// if exception, compute fM in current thread
			fM = computeRecursivelyFasterUsingPrimitiveAndBigIntegerAndHashMap(m, cache);
		}
		
		if ((n & 1) != 0) {
			// F(2m-1) = F(m)^2 + F(m-1)^2
			fN = fM.pow(2).add(fM_1.pow(2));
		} else {
			// F(2m) = [2*F(m-1) + F(m)] * F(m)
			fN = fM_1.shiftLeft(1).add(fM).multiply(fM);
		}
		
		return fN;
    }

	/**
	 * Faster recursive implementation (based on Fibonacci matrix), switching to long computations when n is small enough and using a cache and multithreading.
	 * 
	 * @param n
	 * @return The n-th Fibonacci number as a BigInteger
	 */
    public static BigInteger computeRecursivelyFasterUsingBigIntegerAndThreadingNoDependencies (int n) {
    	int proc = Runtime.getRuntime().availableProcessors();
    	if (n < 128 || proc <= 1) {
    		return computeRecursivelyFasterUsingBigInteger(n);
    	}
    	
    	// there may be 2 or more CPUs (or cores) available - let's use 2 for simplicity for now (half of the work in current thread, half in possibly another)
    	
    	final int m = (n / 2) + (n & 1);
        Callable<BigInteger> callable = new Callable<BigInteger>() {
			public BigInteger call() throws Exception {
				return computeRecursivelyFasterUsingBigInteger(m);
			}
        };
        // first, submit callable
        Future<BigInteger> ffM = executorService.submit(callable);
        
        BigInteger fM;
        BigInteger fM_1;
        BigInteger fN;

        // second, compute fM_1 in current thread (fM is being computed in parallel)
        fM_1 = computeRecursivelyFasterUsingBigInteger(m-1);

        // third, get fM value
        try {
        	fM = ffM.get();
		} catch (Exception e) {
			// if exception, compute fM in current thread
			Log.e(TAG, "ffM.get exception");
			fM = computeRecursivelyFasterUsingBigInteger(m);
		}
		
		if ((n & 1) != 0) {
			// F(2m-1) = F(m)^2 + F(m-1)^2
			fN = fM.pow(2).add(fM_1.pow(2));
		} else {
			// F(2m) = [2*F(m-1) + F(m)] * F(m)
			fN = fM_1.shiftLeft(1).add(fM).multiply(fM);
		}
		
		return fN;
    }

	/**
	 * Faster recursive implementation (based on Fibonacci matrix), switching to long computations when n is small enough and using a cache and multithreading.
	 * 
	 * @param n
	 * @return The n-th Fibonacci number as a BigInteger
	 */
    public static BigInteger computeRecursivelyFasterUsingBigIntegerAndThreadingNoDependencies2 (int n) {
    	int proc = Runtime.getRuntime().availableProcessors();
    	if (n < 128 || proc <= 1) {
    		return computeRecursivelyFasterUsingBigInteger(n);
    	}
    	long t1 = System.currentTimeMillis();
    	long t2;
    	// there may be 2 or more CPUs (or cores) available - let's use 2 for simplicity for now (half of the work in current thread, half in possibly another)
    	
    	final int m = (n / 2) + (n & 1);
        Callable<BigInteger> callable = new Callable<BigInteger>() {
			public BigInteger call() throws Exception {
				//return BigInteger.valueOf(recursiveFasterLong(m));
				return computeRecursivelyFasterUsingBigInteger(m);
			}
        };
        // first, submit callable
        Future<BigInteger> ffM = executorService.submit(callable);
        
        callable = new Callable<BigInteger>() {
			public BigInteger call() throws Exception {
				//return BigInteger.valueOf(recursiveFasterLong(m-1));
				return computeRecursivelyFasterUsingBigInteger(m-1);
				//return recursiveFasterBigInteger(0);
			}
        };
        Future<BigInteger> ffM_1 = executorService.submit(callable);
        t2 = System.currentTimeMillis(); Log.i(TAG, "Checkpoint 1: " + (t2 - t1)); t1 = t2;
        BigInteger fM;
        BigInteger fM_1;
        BigInteger fN;

        try {
        	fM = ffM.get();
		} catch (Exception e) {
			// if exception, compute fM in current thread
			Log.e(TAG, "ffM.get exception");
			fM = computeRecursivelyFasterUsingBigInteger(m);
		}
        t2 = System.currentTimeMillis(); Log.i(TAG, "Checkpoint 2: " + (t2 - t1)); t1 = t2;
        try {
        	fM_1 = ffM_1.get();
		} catch (Exception e) {
			// if exception, compute fM in current thread
			Log.e(TAG, "fM_1.get exception");
			fM_1 = computeRecursivelyFasterUsingBigInteger(m-1);
		}
        t2 = System.currentTimeMillis(); Log.i(TAG, "Checkpoint 3: " + (t2 - t1)); t1 = t2;
		
		if ((n & 1) != 0) {
			// F(2m-1) = F(m)^2 + F(m-1)^2
			fN = fM.pow(2).add(fM_1.pow(2));
		} else {
			// F(2m) = [2*F(m-1) + F(m)] * F(m)
			fN = fM_1.shiftLeft(1).add(fM).multiply(fM);
		}
        t2 = System.currentTimeMillis(); Log.i(TAG, "Checkpoint 4: " + (t2 - t1)); t1 = t2;
		
		return fN;
    }

	/**
	 * Faster recursive implementation (based on Fibonacci matrix), switching to long computations when n is small enough and using a cache and multithreading.
	 * 
	 * @param n
	 * @return The n-th Fibonacci number as a BigInteger
	 */
    
    private static class ComputeRecursivelyFasterCallable implements Callable<BigInteger> {
    	private int n;
    	public ComputeRecursivelyFasterCallable (int n) {
    		this.n = n;
    	}
		public BigInteger call() throws Exception {
			return BigInteger.valueOf(Fibonacci.computeRecursivelyFaster(n));
		}
    	
    }
    public static BigInteger computeRecursivelyFasterUsingPrimitiveLongAndBigIntegerAndThreading (int n) {
    	int proc = Runtime.getRuntime().availableProcessors();
    	if (n < 128 || proc <= 1) {
    		return BigInteger.valueOf(computeRecursivelyFaster(n));
    	}
    	long t1 = System.currentTimeMillis();
    	long t2;
    	// there may be 2 or more CPUs (or cores) available - let's use 2 for simplicity for now (half of the work in current thread, half in possibly another)
    	
    	int m = (n / 2) + (n & 1);
        t2 = System.currentTimeMillis(); Log.i(TAG, "Checkpoint 0: " + (t2 - t1)); t1 = t2;
        ComputeRecursivelyFasterCallable callableM = new ComputeRecursivelyFasterCallable(m);
        ComputeRecursivelyFasterCallable callableM_1 = new ComputeRecursivelyFasterCallable(m-1);
        t2 = System.currentTimeMillis(); Log.i(TAG, "Checkpoint 0.25: " + (t2 - t1)); t1 = t2;
        // first, submit callable
        Future<BigInteger> ffM = executorService.submit(callableM);
        t2 = System.currentTimeMillis(); Log.i(TAG, "Checkpoint 0.5: " + (t2 - t1)); t1 = t2;
        Future<BigInteger> ffM_1 = executorService.submit(callableM_1);
        t2 = System.currentTimeMillis(); Log.i(TAG, "Checkpoint 1: " + (t2 - t1)); t1 = t2;
        
        BigInteger fM;
        BigInteger fM_1;
        BigInteger fN;

        try {
        	fM = ffM.get();
		} catch (Exception e) {
			// if exception, compute fM in current thread
			Log.e(TAG, "ffM.get exception");
			fM = BigInteger.valueOf(computeRecursivelyFaster(m));
		}
        t2 = System.currentTimeMillis(); Log.i(TAG, "Checkpoint 2: " + (t2 - t1)); t1 = t2;
        try {
        	fM_1 = ffM_1.get();
		} catch (Exception e) {
			// if exception, compute fM in current thread
			Log.e(TAG, "fM_1.get exception");
			fM_1 = BigInteger.valueOf(computeRecursivelyFaster(m-1));
		}
        t2 = System.currentTimeMillis(); Log.i(TAG, "Checkpoint 3: " + (t2 - t1)); t1 = t2;
		
		if ((n & 1) != 0) {
			// F(2m-1) = F(m)^2 + F(m-1)^2
			fN = fM.pow(2).add(fM_1.pow(2));
		} else {
			// F(2m) = [2*F(m-1) + F(m)] * F(m)
			fN = fM_1.shiftLeft(1).add(fM).multiply(fM);
		}
        t2 = System.currentTimeMillis(); Log.i(TAG, "Checkpoint 4: " + (t2 - t1)); t1 = t2;
		
		return fN;
    }

    public static BigInteger computeRecursivelyFasterUsingPrimitiveLongAndBigIntegerAndThreadingOneThread (int n) {
    	int proc = Runtime.getRuntime().availableProcessors();
    	if (n < 128 || proc <= 1) {
    		return BigInteger.valueOf(computeRecursivelyFaster(n));
    	}
    	long t1 = System.currentTimeMillis();
    	long t2;
    	// there may be 2 or more CPUs (or cores) available - let's use 2 for simplicity for now (half of the work in current thread, half in possibly another)
    	
        t2 = System.currentTimeMillis(); Log.i(TAG, "Checkpoint 0: " + (t2 - t1)); t1 = t2;
        ComputeRecursivelyFasterCallable callableN = new ComputeRecursivelyFasterCallable(n);
        t2 = System.currentTimeMillis(); Log.i(TAG, "Checkpoint 0.25: " + (t2 - t1)); t1 = t2;
        Future<BigInteger> ffN = executorService.submit(callableN);
        t2 = System.currentTimeMillis(); Log.i(TAG, "Checkpoint 0.5: " + (t2 - t1)); t1 = t2;
        
        BigInteger fN;

        try {
        	fN = ffN.get();
		} catch (Exception e) {
			// if exception, compute fM in current thread
			Log.e(TAG, "ffM.get exception");
			fN = BigInteger.valueOf(computeRecursivelyFaster(n));
		}
        t2 = System.currentTimeMillis(); Log.i(TAG, "Checkpoint 2: " + (t2 - t1)); t1 = t2;
		
		return fN;
    }

    public static BigInteger computeRecursivelyFasterUsingPrimitiveLongAndBigIntegerAndThreading2 (int n) {
    	int proc = Runtime.getRuntime().availableProcessors();
    	if (n < 128 || proc <= 1) {
    		return BigInteger.valueOf(computeRecursivelyFaster(n));
    	}
    	long t1 = System.currentTimeMillis();
    	long t2;
    	// there may be 2 or more CPUs (or cores) available - let's use 2 for simplicity for now (half of the work in current thread, half in possibly another)
    	
    	final int m = (n / 2) + (n & 1);
        t2 = System.currentTimeMillis(); Log.i(TAG, "Checkpoint 1: " + (t2 - t1)); t1 = t2;
        
        final BigInteger fM = BigInteger.ZERO;
        final BigInteger fM_1 = BigInteger.ONE;
        Thread thread1 = new Thread() {
        	@Override
        	public void run() {
        		BigInteger.valueOf(Fibonacci.computeRecursivelyFaster(m));
        	}
        };
        Thread thread2 = new Thread() {
        	@Override
        	public void run() {
        		BigInteger.valueOf(Fibonacci.computeRecursivelyFaster(m-1));
        	}
        };
        thread1.start();
        thread2.start();
        try {
			thread1.join();
	        thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        BigInteger fN;

        t2 = System.currentTimeMillis(); Log.i(TAG, "Checkpoint 2: " + (t2 - t1)); t1 = t2;
		
		if ((n & 1) != 0) {
			// F(2m-1) = F(m)^2 + F(m-1)^2
			fN = fM.pow(2).add(fM_1.pow(2));
		} else {
			// F(2m) = [2*F(m-1) + F(m)] * F(m)
			fN = fM_1.shiftLeft(1).add(fM).multiply(fM);
		}
        t2 = System.currentTimeMillis(); Log.i(TAG, "Checkpoint 3: " + (t2 - t1)); t1 = t2;
		
		return fN;
    }

	public static final int PRECOMPUTED_SIZE = 512; // Must be 128 or greater (see recursiveFaster128BigInteger)
	static BigInteger PRECOMPUTED_RESULTS[] = new BigInteger[PRECOMPUTED_SIZE];
	static long PRECOMPUTED_PRIMITIVE_RESULTS[] = new long[PRECOMPUTED_SIZE];
	
	static {
		long time = System.currentTimeMillis();
		PRECOMPUTED_RESULTS[0] = BigInteger.ZERO;
		PRECOMPUTED_RESULTS[1] = BigInteger.ONE;
		PRECOMPUTED_RESULTS[2] = BigInteger.ONE;
		PRECOMPUTED_PRIMITIVE_RESULTS[0] = 0L;
		PRECOMPUTED_PRIMITIVE_RESULTS[1] = 1L;
		PRECOMPUTED_PRIMITIVE_RESULTS[2] = 1L;
		//PRECOMPUTED_RESULTS[15] = null; // to quickly throw exception if PRECOMPUTED_SIZE is too small
		for (int i = 3; i < PRECOMPUTED_SIZE; i++) {
			PRECOMPUTED_RESULTS[i] =  PRECOMPUTED_RESULTS[i-1].add(PRECOMPUTED_RESULTS[i-2]);
			PRECOMPUTED_PRIMITIVE_RESULTS[i] =  PRECOMPUTED_RESULTS[i].longValue();
		}
		time = System.currentTimeMillis() - time;
		Log.i(TAG, "Initialization of first " + PRECOMPUTED_SIZE + " Fibonacci numbers: " + time + " ms");
		
    	executorService = Executors.newFixedThreadPool(3);
    	
    	executorService = new ThreadPoolExecutor(2, 5, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

		//validate();
	}
	
	/**
	 * Faster recursive implementation (based on Fibonacci matrix).
	 * Fib(0) to Fib(15) are precomputed.
	 * 
	 * @param n
	 * @return The n-th Fibonacci number as a BigInteger
	 */
	public static BigInteger computeRecursivelyFasterUsingBigIntegerAndTable16 (int n) {
		if (n > 15) {
			int m = (n / 2) + (n & 1);
			BigInteger fM   = computeRecursivelyFasterUsingBigIntegerAndTable16(m);
			BigInteger fM_1 = computeRecursivelyFasterUsingBigIntegerAndTable16(m-1);
			if ((n & 1) == 1) {
				// F(2m-1) = F(m)^2 + F(m-1)^2
				return fM.pow(2).add(fM_1.pow(2));
			} else {
				// F(2m) = [2*F(m-1) + F(m)] * F(m)
				return fM_1.shiftLeft(1).add(fM).multiply(fM);
			}
		}
		return PRECOMPUTED_RESULTS[n];
	}
	
	/**
	 * Faster recursive implementation (based on Fibonacci matrix).
	 * Fib(0) to Fib(63) are precomputed.
	 * 
	 * @param n
	 * @return The n-th Fibonacci number as a BigInteger
	 */
	public static BigInteger computeRecursivelyFasterUsingBigIntegerAndTable64 (int n) {
		if (n > 63) {
			int m = (n / 2) + (n & 1);
			BigInteger fM   = computeRecursivelyFasterUsingBigIntegerAndTable64(m);
			BigInteger fM_1 = computeRecursivelyFasterUsingBigIntegerAndTable64(m-1);
			if ((n & 1) == 1) {
				// F(2m-1) = F(m)^2 + F(m-1)^2
				return fM.pow(2).add(fM_1.pow(2));
			} else {
				// F(2m) = [2*F(m-1) + F(m)] * F(m)
				return fM_1.shiftLeft(1).add(fM).multiply(fM);
			}
		}
		return PRECOMPUTED_RESULTS[n];
	}
	
	/**
	 * Faster recursive implementation (based on Fibonacci matrix).
	 * Fib(0) to Fib(127) are precomputed.
	 * 
	 * @param n
	 * @return The n-th Fibonacci number as a BigInteger
	 */
	public static BigInteger computeRecursivelyFasterUsingBigIntegerAndTable128 (int n) {
		if (n > 127) {
			int m = (n / 2) + (n & 1);
			BigInteger fM   = computeRecursivelyFasterUsingBigIntegerAndTable128(m);
			BigInteger fM_1 = computeRecursivelyFasterUsingBigIntegerAndTable128(m-1);
			if ((n & 1) == 1) {
				// F(2m-1) = F(m)^2 + F(m-1)^2
				return fM.pow(2).add(fM_1.pow(2));
			} else {
				// F(2m) = [2*F(m-1) + F(m)] * F(m)
				return fM_1.shiftLeft(1).add(fM).multiply(fM);
			}
		}
		return PRECOMPUTED_RESULTS[n];
	}
	
	/**
	 * Faster recursive implementation (based on Fibonacci matrix).
	 * Fib(0) to Fib(PRECOMPUTED_SIZE-1) are precomputed.
	 * 
	 * @param n
	 * @return The n-th Fibonacci number as a BigInteger
	 */
	public static BigInteger computeRecursivelyFasterUsingBigIntegerAndTable (int n) {
		if (n > PRECOMPUTED_SIZE-1) {
			int m = (n / 2) + (n & 1);
			BigInteger fM   = computeRecursivelyFasterUsingBigIntegerAndTable(m);
			BigInteger fM_1 = computeRecursivelyFasterUsingBigIntegerAndTable(m-1);
			if ((n & 1) == 1) {
				// F(2m-1) = F(m)^2 + F(m-1)^2
				return fM.pow(2).add(fM_1.pow(2));
			} else {
				// F(2m) = [2*F(m-1) + F(m)] * F(m)
				return fM_1.shiftLeft(1).add(fM).multiply(fM);
			}
		}
		return PRECOMPUTED_RESULTS[n];
	}
	
	/**
	 * Faster recursive implementation (based on Fibonacci matrix), very similar to computeRecursivelyFasterNBigInteger.
	 * Fib(0) to Fib(PRECOMPUTED_SIZE-1) are precomputed as long values and a BigInteger is created to return the result.
	 * Make sure you define PRECOMPUTED_SIZE as 92 or smaller to get correct results and be able to compare with recursiveFasterNBigInteger above.
	 * If you define PRECOMPUTED_SIZE to anything greater than 92 then the method will still run but won't return a correct result.
	 * 
	 * @param n
	 * @return The n-th Fibonacci number as a BigInteger
	 */
	public static BigInteger computeRecursivelyFaster2UsingBigIntegerAndTable (int n) {
		if (n > PRECOMPUTED_SIZE-1) {
			int m = (n / 2) + (n & 1);
			BigInteger fM   = computeRecursivelyFaster2UsingBigIntegerAndTable(m);
			BigInteger fM_1 = computeRecursivelyFaster2UsingBigIntegerAndTable(m-1);
			if ((n & 1) == 1) {
				// F(2m-1) = F(m)^2 + F(m-1)^2
				return fM.pow(2).add(fM_1.pow(2));
			} else {
				// F(2m) = [2*F(m-1) + F(m)] * F(m)
				return fM_1.shiftLeft(1).add(fM).multiply(fM);
			}
		}
		return BigInteger.valueOf(PRECOMPUTED_PRIMITIVE_RESULTS[n]);
	}
	
	/**
	 * Computes the total number of function calls when using the naive recursive implementation to compute Fib(n).
	 * 
	 * @param n
	 * @return The number of function calls
	 */
	public static long computeRecursivelyCalls (int n) {
		long calls = 1;
		if (n > 1) {
			calls += computeRecursivelyCalls(n-1);
			calls += computeRecursivelyCalls(n-2);
		}
		return calls;
	}
	
	/**
	 * Computes the total number of function calls when using the naive recursive implementation with tail recursion optimization to compute Fib(n).
	 * 
	 * @param n
	 * @return The number of function calls
	 */
	public static long computeRecursivelyWithLoopCalls (int n) {
		long calls = 1;
		if (n > 1) {
			while (n > 1) {
				calls += computeRecursivelyWithLoopCalls(n-2);
				n--;
			}
		}
		return calls;
	}
	
	/**
	 * Computes the total number of function calls when using the faster recursive implementation to compute Fib(n).
	 * 
	 * @param n
	 * @param threshold The number of precomputed results (Fib(0) to Fib(threshold-1))
	 * @return The number of function calls
	 */
	public static long computeRecursivelyFasterCalls (int n, int threshold) {
		long calls = 1;
		if (n >= threshold) {
			int m = (n / 2) + (n & 1);
			calls += computeRecursivelyFasterCalls(m, threshold);
			calls += computeRecursivelyFasterCalls(m-1, threshold);
		}
		return calls;
	}
	
	/**
	 * Computes the total number of allocations when using the faster recursive implementation to compute Fib(n).
	 * 
	 * @param n
	 * @param threshold The number of precomputed results (Fib(0) to Fib(threshold-1))
	 * @return The number of function calls
	 */
	public static long computeRecursivelyFasterAllocations (int n, int threshold) {
		long allocations = 0;
		if (n >= threshold) {
			int m = (n / 2) + (n & 1);
			allocations += computeRecursivelyFasterAllocations(m, threshold);
			allocations += computeRecursivelyFasterAllocations(m-1, threshold);
			allocations += 3;
		}
		return allocations;
	}
	
	/**
	 * Validates algorithms for a given value and known result.
	 * 
	 * @param n Index of the Fibonacci number to compute.
	 * @param result The result we expect.
	 * @throws RuntimeException If results are invalid
	 */
	private static void validate(int n, BigInteger result) {
		BigInteger rBI;
		
		if (n < 16) {
			rBI = BigInteger.valueOf(computeRecursively(n));
			if (! rBI.equals(result)) {
				throw new RuntimeException("recursive("+n+") returned "+rBI+" instead of "+result);
			}
			
			rBI = BigInteger.valueOf(computeRecursivelyWithLoop(n));
			if (! rBI.equals(result)) {
				throw new RuntimeException("recursiveLoop("+n+") returned "+rBI+" instead of "+result);
			}
		}
		
		rBI = computeRecursivelyFasterUsingBigInteger(n);
		if (! rBI.equals(result)) {
			throw new RuntimeException("recursiveFasterBigInteger("+n+") returned "+rBI+" instead of "+result);
		}
		
		rBI = computeRecursivelyFasterUsingBigIntegerAndTable16(n);
		if (! rBI.equals(result)) {
			throw new RuntimeException("recursiveFaster16BigInteger("+n+") returned "+rBI+" instead of "+result);
		}
		
		rBI = computeRecursivelyFasterUsingBigIntegerAndTable(n);
		if (! rBI.equals(result)) {
			throw new RuntimeException("recursiveFasterNBigInteger("+n+") returned "+rBI+" instead of "+result);
		}
		
		if (n <= 92) {
			// we know computeRecursivelyFasterN2BigInteger returns invalid results for n > 92
			rBI = computeRecursivelyFaster2UsingBigIntegerAndTable(n);
			if (! rBI.equals(result)) {
				throw new RuntimeException("recursiveFasterN2BigInteger("+n+") returned "+rBI+" instead of "+result);
			}
		}

		rBI = computeRecursivelyFasterUsingPrimitiveAndBigInteger(n);
		if (! rBI.equals(result)) {
			throw new RuntimeException("recursiveFasterPrimitiveAndBigInteger("+n+") returned "+rBI+" instead of "+result);
		}
	}
	
	/**
	 * Validates algorithms for specific values
	 */
	private static void validate() {
		validate(0, BigInteger.ZERO);
		validate(1, BigInteger.ONE);
		validate(2, BigInteger.ONE);
		validate(3, BigInteger.valueOf(2));
		validate(4, BigInteger.valueOf(3));
		validate(5, BigInteger.valueOf(5));
		validate(6, BigInteger.valueOf(8));
		validate(7, BigInteger.valueOf(13));
		validate(8, BigInteger.valueOf(21));
		validate(9, BigInteger.valueOf(34));
		validate(10, BigInteger.valueOf(55L));
		validate(11, BigInteger.valueOf(89L));
		validate(50, BigInteger.valueOf(12586269025L));
		validate(51, BigInteger.valueOf(20365011074L));
		validate(92, BigInteger.valueOf(7540113804746346429L));
		validate(93, BigInteger.valueOf(7540113804746346429L).add(computeIterativelyUsingBigInteger(91)));
		validate(1000, computeIterativelyUsingBigInteger(999).add(computeIterativelyUsingBigInteger(998)));
		validate(1001, computeIterativelyUsingBigInteger(1000).add(computeIterativelyUsingBigInteger(999)));
		validate(2000, computeIterativelyUsingBigInteger(1999).add(computeIterativelyUsingBigInteger(1998)));
		validate(2001, computeIterativelyUsingBigInteger(2000).add(computeIterativelyUsingBigInteger(1999)));
	}
}

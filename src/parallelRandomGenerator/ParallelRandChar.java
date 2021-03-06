package parallelRandomGenerator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.security.SecureRandom;


public class ParallelRandChar extends SecureRandom implements Runnable {

	
	private static final long serialVersionUID = 1L;
	
	/** ArrayList Containing The Random Chars */
	private ArrayList<Character> randCharArrList;
		
	/** Number Of Core Available */
	private int cores;
	
	/** Number Of Threads To Use */
	private int threadNum;
	
	/** // Random Sequence's Length */
	private int seqLength; 
	
	/** Random Number's Length For Every Thread */
	private int seqPerThread;
		
	/** Range For The Random Chars */
	private int range; // 
		
	/** Threads' Array */
	private Thread[] t; //


	
	
	/** 
	 * Constructor Without Seed
	 * @param seqLength Random Number's Length
	 * @throws InterruptedException 
	 */
	public ParallelRandChar(int seqLength) throws InterruptedException {
		
		byte[] seed = this.generateSeed(seqLength);
		
		this.setSeed(seed);
		
		this.ParallelRandCharInitialise (seqLength);
		
	}
	
	
	/** 
	 * Constructor With Seed
	 * @param seed Seed Used To Generate The Random Values
	 * @param seqLength Random Number's Length
	 * @throws InterruptedException 
	 */
	public ParallelRandChar(long seed, int seqLength) {
			
		this.setSeed(seed);
	
		this.ParallelRandCharInitialise (seqLength);		
		
	}
	
	
	
	/**
	 * Method that initialise the object
	 * @param seqLength seqLength Random Number's Length
	 * @throws InterruptedException 
	 */
	private void ParallelRandCharInitialise(int seqLength) {
		
		this.randCharArrList = new ArrayList<Character>();
		
		this.seqLength = seqLength;
		
		this.range = 127; // Unicode Charset Table
		
		this.cores = Runtime.getRuntime().availableProcessors();
		
		this.threadNum = this.cores;
		
		this.threadNum = 2*this.cores;
		
		// If more core than required random number, decrease thread number
		while (this.threadNum > this.seqLength) { 
			
			this.threadNum--;
			
		}		
		
		// Random Number Per Core
		this.seqPerThread = (int)(this.seqLength / this.threadNum);
		
		
		// Initialize Threads Array
		this.t = new Thread[this.threadNum];
		
		// Create And Start The Threads
		for (int i = 0; i < this.threadNum; i++) {
		
			t[i] = new Thread (this, "Rand Char Thread " + i);
						
			t[i].start();
		
		}
		
		for (int i = 0; i < this.threadNum; i++) {
			
			while (t[i].isAlive()) {
				
				try {
					
					Thread.sleep(50);
				
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				
				}					
				
			}
						
		}	
		
		// Number Of The Remaining Random Values To Calculate
		int remainingNumbers = this.seqLength - (this.seqPerThread*this.threadNum); 
		
		// If There Are Other Numbers To Calculate
		if (remainingNumbers > 0) {
									
			this.CharacterRandGen(remainingNumbers, this.range);		
			
		}		
			
	}
		
	
	
	/** 
	 * Runnable Method For Thread Execution
	 */
	@Override
	public void run() {
			
		this.CharacterRandGen(this.seqPerThread, this.range);
			
	}

	
	
	/** 
	 * Calculate Random Chars With Inputed Arguments
	 * @param seqLength Array's Length
	 * @param range Range For The Random Numbers
	 */
	public synchronized void CharacterRandGen (int seqLength, int range) {
		
		int tmpValue = 0;
		
		for (int i = 0; i < seqLength; i++) {
			
			do {
				
				tmpValue = this.nextInt(range);
			
			} while (tmpValue < 32);
			
			this.randCharArrList.add((char) tmpValue);
				
		}
		
		
			
	}
	
	
	
	
	/** 
	 * Return The Char ArrayList
	 * @return Char ArrayList Containing The Random Numbers
	 */
	public ArrayList<Character> getRandCharArrList() {
		
		return this.randCharArrList;
	
	}
	
	
	
	/** 
	 * Return The Char Array
	 * @return Char Array Containing The Random Numbers
	 */
	public Character[] getRandCharArr() {
		
		Character[] randCharArr = new Character[this.randCharArrList.size()];
		
		randCharArr = this.randCharArrList.toArray(((Character[])Array.newInstance(this.randCharArrList.get(0).getClass(), this.randCharArrList.size())));
				
		return randCharArr;
	
	}
	
	
	/**
	 * Return The ArrayList Size
	 * @return Int Contatining The ArrayList Size
	 */
	public int getRandCharArrSize() {
		
		return this.randCharArrList.size();
		
	}


	
	/** 
	 * Print The Requested Random Char As A Unique Word
	 */
	@Override
	public String toString() {
		
		String s = new String();
		
		for (Character el: this.getRandCharArrList()) {
			
			s += el;
		}
		
		return s;
		
	}
	
	
	
	/**
	 * Outputs A String Containing Used Algorithm
     */
	public String getAlgo () {
		
		return this.getAlgorithm();
		
	}
	
	
	
	/** 
	 * Print The Requested Random Chars
	 */
	public String toStringNl() {
		
		String s = new String();
		
		for (Character el: this.getRandCharArrList()) {
			
			s += el;
			
		}
		
		return s;
		
	}

	

}


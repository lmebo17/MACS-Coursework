// Bank.java

/*
 Creates a bunch of accounts and uses threads
 to post transactions to the accounts concurrently.
*/

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;

public class Bank {

	public int NUM_WORKERS;
	public static final int ACCOUNTS = 20;	 // number of accounts

	private static CountDownLatch latch;

	private static ArrayBlockingQueue queue;

	public ArrayList<Account> accounts;


	Transaction SENTINEL = new Transaction(-1,0,0);

	public Bank(int numWorkers){
		this.NUM_WORKERS = numWorkers;
		this.queue = new ArrayBlockingQueue<>(numWorkers);
		this.latch = new CountDownLatch(numWorkers);
		this.accounts = new ArrayList<>();
		for(int i = 0; i < ACCOUNTS; i++){
			this.accounts.add(new Account(this, i, 1000));
		}
	}


	private class Worker implements Runnable{
		@Override
		public void run(){
			while(true){
				try {
					Transaction transaction = (Transaction) queue.take();
					if(transaction.from == -1){
						break;
					}
					accounts.get(transaction.from).withdraw(transaction.amount);
					accounts.get(transaction.to).addMoney(transaction.amount);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}

			latch.countDown();

		}
	}

	/*
	 Reads transaction data (from/to/amt) from a file for processing.
	 (provided code)
	 */
	public void readFile(String file) {
			try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			// Use stream tokenizer to get successive words from file
			StreamTokenizer tokenizer = new StreamTokenizer(reader);
			
			while (true) {
				int read = tokenizer.nextToken();
				if (read == StreamTokenizer.TT_EOF) break;  // detect EOF
				int from = (int)tokenizer.nval;
				
				tokenizer.nextToken();
				int to = (int)tokenizer.nval;
				
				tokenizer.nextToken();
				int amount = (int)tokenizer.nval;
				
				// Use the from/to/amount

				Transaction transaction = new Transaction(from,to,amount);
				queue.put(transaction);

			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/*
	 Processes one file of transaction data
	 -fork off workers
	 -read file into the buffer
	 -wait for the workers to finish
	*/
	public void processFile(String file) throws InterruptedException {

		for(int i = 0; i < this.NUM_WORKERS; i++){
			Thread thread = new Thread(new Worker());
			thread.start();
		}

		readFile(file);
		for(int i = 0; i < this.NUM_WORKERS; i++){
			queue.put(new Transaction(-1,0,0));
		}

		latch.await();

		for(int i = 0; i < ACCOUNTS; i++){
			System.out.println("acct:" + i + " bal:" + accounts.get(i).GetBalance() + " trans:" + accounts.get(i).GetTransactions());
		}
	}

	/*
	 Looks at commandline args and calls Bank processing.
	*/
	public static void main(String[] args) throws InterruptedException {
		// deal with command-lines args
		if (args.length == 0) {
			System.out.println("Args: transaction-file [num-workers [limit]]");
			return;
		}
		
		String file = args[0];
		
		int numWorkers = 1;
		if (args.length >= 2) {
			numWorkers = Integer.parseInt(args[1]);
		}

		Bank bank = new Bank(numWorkers);
		bank.processFile(file);
	}

}


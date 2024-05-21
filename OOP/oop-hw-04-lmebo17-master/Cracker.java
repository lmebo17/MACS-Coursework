// Cracker.java
/*
 Generates SHA hashes of short strings in parallel.
*/

import java.security.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class Cracker {
	// Array of chars used to produce strings
	public static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789.,-!".toCharArray();
	private int WordLength;
	private static final int NUM_CHARS = 40;
	private int NUM_WORKERS = 4;
	private  static MessageDigest  messageDigest;
	private static CountDownLatch latch;
	private static ArrayList<Worker> list;
	private  String hashCode;
	private  Boolean found;

	private String result;

	public Cracker(int len, String targ, int num) throws NoSuchAlgorithmException {
		this.WordLength = len;
		this.hashCode = targ;
		this.found = false;
		this.NUM_WORKERS = num;
	}

	public class Worker extends Thread{

		int id;
		int startingIndex;
		int endIndex;

		public Worker(int id, int startingIndex, int endIndex){
			this.id = id;
			this.startingIndex = startingIndex;
			this.endIndex = endIndex;
		}

		@Override
		public void run() {
			for(int i = startingIndex; i <= endIndex; i++){
				try {
					recursion(CHARS[i] + "");
				} catch (NoSuchAlgorithmException e) {
					throw new RuntimeException(e);
				}
			}
		}


		private boolean recursion(String password) throws NoSuchAlgorithmException {
			if(found) return true;
			if(password.length() == WordLength){
				String hash = hashFunction(password);
				if(hash.equals(hashCode)){
					result = password;
					found = true;
					return true;
				}
			}
			if(password.length() < WordLength){
				for(int i = 0; i < NUM_CHARS; i++){
					if(recursion(CHARS[i] + password)) return true;
				}
			}
			return false;
		}
	}


	public String hashFunction(String password) throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
		byte[] bytes = messageDigest.digest(password.getBytes());
		return hexToString(bytes);
	}

	/*
	 Given a byte[] array, produces a hex String,
	 such as "234a6f". with 2 chars for each byte in the array.
	 (provided code)
	*/
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}

	/*
	 Given a string of hex byte values such as "24a26f", creates
	 a byte[] array of those values, one byte value -128..127
	 for each 2 chars.
	 (provided code)
	*/
	public static byte[] hexToArray(String hex) {
		byte[] result = new byte[hex.length()/2];
		for (int i=0; i<hex.length(); i+=2) {
			result[i/2] = (byte) Integer.parseInt(hex.substring(i, i+2), 16);
		}
		return result;
	}


	public String findPassword() throws InterruptedException {
		list = new ArrayList<>();
		for(int i = 0; i < NUM_WORKERS; i++){
			Worker worker = new Worker(i, i*(NUM_CHARS / NUM_WORKERS), (i+1)*(NUM_CHARS/NUM_WORKERS) - 1);
			list.add(worker);
			worker.start();
		}
		for(Worker worker : list){
			worker.join();
		}
		return result;
	}



	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			System.out.println("Args: target length [workers]");
		} else if(args.length == 1){
			Cracker cracker = new Cracker(0, "", 1);
			String password = args[0];
			String hashedPassword = cracker.hashFunction(password);
			System.out.println(hashedPassword);
		} else {
			// args: targ len [num]
			String targ = args[0];
			int len = Integer.parseInt(args[1]);
			int num = 1;
			if (args.length>2) {
				num = Integer.parseInt(args[2]);
			}
			// a! 34800e15707fae815d7c90d49de44aca97e2d759
			// xyz 66b27417d37e024c46526c2f6d358a754fc552f3

			Cracker cracker = new Cracker(len, targ, num);
			latch = new CountDownLatch(cracker.NUM_WORKERS);
			cracker.findPassword();
			System.out.println(cracker.result);
		}
	}
}

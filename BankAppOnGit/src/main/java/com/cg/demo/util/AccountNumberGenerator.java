package com.cg.demo.util;

public class AccountNumberGenerator {
	private static int accountNumber=1;
	public static int getAccountNumber(){
		return accountNumber++;
	}
}

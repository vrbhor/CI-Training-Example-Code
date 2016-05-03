package com.cg.demo.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cg.demo.beans.Account;
import com.cg.demo.beans.Customer;
import com.cg.demo.exceptions.InsufficientBalanceException;
import com.cg.demo.exceptions.InsufficientInitialAmountException;
import com.cg.demo.exceptions.InvalidAccountException;
import com.cg.demo.repo.AccountRepository;
import com.cg.demo.service.AccountService;
import com.cg.demo.service.AccountServiceImpl;

public class TestAccountService {

	private AccountService service;
	
	@Mock private AccountRepository repo;
	
	@Before
	public void init(){
		MockitoAnnotations.initMocks(this);
		service = new AccountServiceImpl(repo);
		
	}
	/* 1. While creating account you should receive valid customer object (should not be null)
	 * 2. amount should be greater than or equal to 500;
	 * 3. If valid input is given then system should create the account successfully.
	 */
	
	@Test(expected=java.lang.IllegalArgumentException.class)
	public void customer_should_not_be_null_while_creating_the_account() throws InsufficientInitialAmountException {
	
		service.createAccount(null, 500);
	}

	@Test(expected=com.cg.demo.exceptions.InsufficientInitialAmountException.class)
	public void minimum_amount_of_fivehundred_must_be_give_to_open_the_account() throws InsufficientInitialAmountException{
		service.createAccount(new Customer(), 400);
	}
	
	@Test
	public void if_valid_input_is_given_system_should_create_account_successfully() throws InsufficientInitialAmountException{
	
		Account oldAccount = new Account(1);
		oldAccount.setBalance(1000);
		when(repo.save(oldAccount)).thenReturn(true);
		
		Account newAccount =service.createAccount(new Customer(), 1000);
		
		
		assertEquals(oldAccount.getBalance()+"", newAccount.getBalance()+"");
		
	}
	
	/* Test cases for show balance
	 * 1. account number should be valid
	 * 2. if valid account number is given then system should return the account information of that account. 
	 * 
	 * 
	 */

	@Test(expected=com.cg.demo.exceptions.InvalidAccountException.class)
	
	public void passing_invalid_accountNumber_to_showBalance_results_into_exception() throws InvalidAccountException{
		when(repo.findByAccountnumber(2)).thenReturn(null);
		service.showBalance(2);
	}
	@Test
	public void balance_should_be_returned_for_valid_accounts() throws InvalidAccountException{
		
		Account a = new Account(1);
		a.setBalance(1000);
		
		when(repo.findByAccountnumber(1)).thenReturn(a);
		
		Account newAccount = service.showBalance(1);
		
		assertEquals(""+a.getBalance(),""+newAccount.getBalance());
		
	}
	
	/* Test cases for withdraw
	 * 1. account number should be valid
	 * 2. amount should not be greater than balance.
	 * 3. amount should not be negative
	 * 4. if all the input is correct, then system should return the account information after transaction is completed.
	 */
	
	@Test(expected=com.cg.demo.exceptions.InvalidAccountException.class)
	public void withdraw_should_throw_excpetion_if_invalid_accountNumber_is_passed() throws InvalidAccountException, InsufficientBalanceException{
		when(repo.findByAccountnumber(2)).thenReturn(null);
		service.withdraw(2, 5000);
	}
	
	@Test(expected=java.lang.IllegalArgumentException.class)
	public void amount_passed_to_withdraw_should_not_be_negative() throws InvalidAccountException, InsufficientBalanceException{
		service.withdraw(1, -88);
	}
	
	@Test(expected=com.cg.demo.exceptions.InsufficientBalanceException.class)
	public void amount_passed_to_withdraw_should_not_be_greater_than_balance() throws InvalidAccountException, InsufficientBalanceException{
		Account a = new Account(1);
		a.setBalance(3000);
		when(repo.findByAccountnumber(1)).thenReturn(a);
		service.withdraw(1, 5000);
	}
	
	
	@Test
	public void if_valid_input_is_passed_then_withdraw_should_complete_the_transaction_successfully() throws InvalidAccountException, InsufficientBalanceException{
		Account a = new Account(1);
		a.setBalance(3000);
		when(repo.findByAccountnumber(1)).thenReturn(a);
		Account newAccount = service.withdraw(1, 500);
		assertEquals((a.getBalance())+"", newAccount.getBalance()+"");
	}
	/* Test cases for deposit
	 * 1. account number should be valid
	 * 2. amount should not be negative
	 * 3. if all the input is correct, then system should return the account information after transaction is completed.
	 */
	

	@Test(expected=com.cg.demo.exceptions.InvalidAccountException.class)
	public void deposit_should_throw_excpetion_if_invalid_accountNumber_is_passed() throws InvalidAccountException, InsufficientBalanceException{
		when(repo.findByAccountnumber(2)).thenReturn(null);
		service.deposit(2, 8765);
	}
	
	@Test(expected=java.lang.IllegalArgumentException.class)
	public void amount_passed_to_deposit_should_not_be_negative() throws InvalidAccountException, InsufficientBalanceException{
		service.deposit(1, -88);
	}
	@Test
	public void if_valid_input_is_passed_then_deposit_should_complete_the_transaction_successfully() throws InvalidAccountException, InsufficientBalanceException{
		Account a = new Account(1);
		a.setBalance(3000);
		when(repo.findByAccountnumber(1)).thenReturn(a);
		Account newAccount = service.deposit(1, 500);
		assertEquals((a.getBalance())+"", newAccount.getBalance()+"");
	}

}

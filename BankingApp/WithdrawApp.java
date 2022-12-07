package BankingApp;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Scanner;

public class WithdrawApp {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		//resources needed for the app
		Connection connection = null;
		CallableStatement cstmt= null;
		CallableStatement cstmt2= null;
		Scanner scanner = new Scanner(System.in);
		
		//variables
		int acc_no;
		float amount;
		float currentBalance;
		
		final float MIN_BAL = 1000f;
		
		//procedure calls
		String withdraw = "{CALL deductAmount(?,?)}";
		String balance = "{CALL getBalance(?,?)}";
		
		System.out.println("Making a withdrawal :");
		System.out.println();
		
		System.out.print("Enter the account no :");
		acc_no = scanner.nextInt();
		
		try
		{
			
			connection = JdbcUtil.getConnection();
			if(connection !=null) {
				cstmt = connection.prepareCall(balance);
				
			}
			if(cstmt != null)
			{
				cstmt.setInt(1, acc_no);
				cstmt.registerOutParameter(2, Types.FLOAT);
				
				cstmt.execute();
				
				currentBalance = cstmt.getFloat(2);
				System.out.println("Current Balance : "+currentBalance);
				
				System.out.print("Enter the withdraw amount :");
				amount = scanner.nextFloat();
				
				if(currentBalance > amount) 
				{
					connection.setAutoCommit(false);
					cstmt = connection.prepareCall(withdraw);
					cstmt.setFloat(1, amount);
					cstmt.setInt(2, acc_no);
					cstmt.executeUpdate();
					
					if(MIN_BAL <= (currentBalance - amount)) {
						connection.commit();
						System.out.println("Withdrawal succesfull...");
					}
					else {
						connection.rollback();
						System.out.println("You do not have sufficient balance...");
					}
					System.out.print("New account balance :");
					cstmt = connection.prepareCall(balance);
					
					cstmt.setInt(1, acc_no);
					cstmt.registerOutParameter(2, Types.FLOAT);
					
					cstmt.execute();
					
					System.out.println(cstmt.getFloat(2));						
					
				}
				else {
					System.out.println("You do not have sufficient balance...");
				}
				
			}
			
		}
		catch(SQLException se) {
			se.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				JdbcUtil.closeConnection(null, cstmt, connection);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(scanner!=null)
			{
				scanner.close();
			}
		}

	}

}

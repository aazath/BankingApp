package BankingApp;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Scanner;

public class BalanceCheckApp {

	public static void main(String[] args) {
		//resources needed for the app
		Connection connection = null;
		CallableStatement cstmt = null;
		Scanner scanner = new Scanner(System.in);
		
		//variables
		int acc_no;
		String balance = "{CALL getBalance(?,?)}";
		
		System.out.println("Checking the account balance :");
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
				
				System.out.println("Your current balance is :" +cstmt.getFloat(2));
				
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

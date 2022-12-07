package BankingApp;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Scanner;

public class DepositApp {

	public static void main(String[] args) {
		//resources needed for the app
		Connection connection = null;
		CallableStatement cstmt = null;
		Scanner scanner = new Scanner(System.in);
		
		//variables
		int acc_no;
		float amount;
		String deposit = "{CALL addAmount(?,?)}";
		String balance = "{CALL getBalance(?,?)}";
		
		System.out.println("Making a deposit :");
		System.out.println();
		
		System.out.print("Enter the account no :");
		acc_no = scanner.nextInt();
		
		try
		{
			
			connection = JdbcUtil.getConnection();
			if(connection !=null) {
				cstmt = connection.prepareCall(deposit);
			}
			if(cstmt != null)
			{
				System.out.print("Enter the deposit amount :");
				amount = scanner.nextFloat();
				cstmt.setFloat(1, amount);
				cstmt.setInt(2, acc_no);
				
				cstmt.executeUpdate();
				
				System.out.print("New account balance :");
				cstmt = connection.prepareCall(balance);
				
				cstmt.setInt(1, acc_no);
				cstmt.registerOutParameter(2, Types.FLOAT);
				
				cstmt.execute();
				
				System.out.println(cstmt.getFloat(2));
				
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

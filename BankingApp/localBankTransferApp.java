package BankingApp;
/*
 CREATE PROCEDURE enterprisejava.getAccountDetails
	(in acc_n int, out c_name varchar(20),out c_addr varchar(20),out c_balance double) 
BEGIN
	SELECT name,addr,balance INTO c_name,c_addr,c_balance
	From bank_details 
	WHERE ac_no = acc_n;
END

CALL getAccountDetails(1020, @name, @c_addr, @c_balance); 
SELECT @name as 'Customer Name',@c_addr as 'Address', @c_balance as 'Balance';
 */
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Scanner;

public class localBankTransferApp {

	public static void main(String[] args) {
		// create the needed resources
		Connection connection = null;
		CallableStatement statement = null;
		Scanner scanner = new Scanner(System.in);
		
		//variables
		int sender_accno, reciever_accno;
		float transferAmount;
		String choice;
		
		System.out.println("$$$$$ Local Transaction $$$$$$");
		System.out.println("==============================");
		System.out.print("Enter the Sender's Account No :");
		sender_accno = scanner.nextInt();
		
		String sender = "{CALL getAccountDetails(?,?,?,?)}";
		String addAmount = "{CALL addAmount(?,?)}";
		String deductAmount = "{CALL deductAmount(?,?)}";
		
		try {
			connection = JdbcUtil.getConnection();
			
			if(connection != null)
			{
				statement = connection.prepareCall(sender);
				
				if(statement!=null)
				{
					//set the input parameter
					statement.setInt(1, sender_accno);
					
					//set the output parameters
					statement.registerOutParameter(2, Types.VARCHAR);
					statement.registerOutParameter(3, Types.VARCHAR);
					statement.registerOutParameter(4, Types.FLOAT);
					
					statement.execute(); 
					System.out.println("Sender Name	 :"+statement.getString(2));
					System.out.println("Sender Address	 :"+statement.getString(3));
					System.out.println("Account Balance	 :"+statement.getFloat(4));	
					
					System.out.println();
					System.out.print("Enter the Transfer Amount :");
					transferAmount = scanner.nextFloat();
					
					if(statement.getFloat(4) > transferAmount && statement.getFloat(4) >=1000)
					{
						System.out.print("Enter the Reciever's Account No :");
						reciever_accno = scanner.nextInt();
				
						//disable the auto-commit
						connection.setAutoCommit(false);
						
						statement = connection.prepareCall(deductAmount);
						statement.setFloat(1, transferAmount);
						statement.setFloat(2, sender_accno);
						statement.execute();
						
						statement = connection.prepareCall(addAmount);
						statement.setFloat(1, transferAmount);
						statement.setFloat(2, reciever_accno);
						statement.execute();
						
						System.out.println("Can you please confirm the transaction [Yes/No] :");
						choice = scanner.next();
						
						if (choice.equalsIgnoreCase("yes")) {
							connection.commit();
							System.out.println("Transaction commited");
						} else {
							connection.rollback();
							System.out.println("Transaction rollback");
						}
					}else
					{
						System.out.println("Your account balance is insufficient to carryout this transaction");
					}
					System.out.println();
					System.out.println("Account status after the transaction");
					
					statement = connection.prepareCall(sender);
					//set the input parameter
					statement.setInt(1, sender_accno);
					
					//set the output parameters
					statement.registerOutParameter(2, Types.VARCHAR);
					statement.registerOutParameter(3, Types.VARCHAR);
					statement.registerOutParameter(4, Types.FLOAT);
					
					statement.execute(); 
					
					System.out.println("Account Balance	 :"+statement.getFloat(4));
						
				}
				
		}
		}
			catch(SQLException se) {
				se.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				JdbcUtil.closeConnection(null, statement, connection);
				if(scanner!=null)
					scanner.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}

/*Chelin Tutorials 2012. All Rights Reserved.
 *Educational Purpose Only.Non-Comercial & Profitable.
 *Contact: chelo_c@live.com /chelinho1397@gmail.com
 */
package server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**Every client instance has its own IndividualThread on the server**/
public class IndividualThread implements Runnable {

	private Socket client;
	private ServerFrame serverFrame;
	private boolean running;
	
	BufferedReader in = null;
	PrintWriter out = null;
	
	public IndividualThread(Socket new_conecction, ServerFrame serverFrame) {
		client=new_conecction;
		running=true;
		this.serverFrame=serverFrame;
		this.serverFrame.registerClient(this);
		
	}

	
	public void run(){
		create_buffers();

		String line="<!>New Conecction<!> : "+client.getInetAddress();
		System.out.println(line);
		serverFrame.addline(line);
		serverFrame.broadcast(line );
		
		recieve(); //this methods recieves data in a while loop

	}


	/**Creates the two buffers**/
	private void create_buffers() {
		try{
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);
		}
		catch (IOException e) {
			System.out.println("in or out failed");
			System.exit(-1);
		}
	}

	/**Reads data from the buffer**/
	public void recieve(){
		String line,parseline;
		while(running){
			
			try{
				
				Thread.sleep(100);
				
				line = in.readLine();
				if(line!= null){
					
					if(check_logout(line))logout(); //if  logout message is given, l/out
					else{
						parseline=client.getInetAddress()+"  Said: "+line;
						parseline=parseline.substring(1); //remove the "/" from te addrs
						System.out.println(parseline);
						serverFrame.addline(parseline);
						serverFrame.broadcast(parseline);
						}
					}
			} 

			//CATCHS
			catch (IOException e) {
				System.out.println("IOERROR INDV THREAD.");
				e.printStackTrace();
				System.exit(-1);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**Sends data to the socket**/
	public  void send(String line) {
			out.println(line);
		}
	
	

	/**Checks if the logout message was sent**/
	private boolean check_logout(String line) {
		if (line.length()<7)return false;
		return((line.substring(0, 7).compareTo("/logout")==0));
	}
	
	
	/**Logout: removes client from list and broadcast a message**/
	private void logout() {
		String line = client.getInetAddress()+" <!>Disconected<!>";
		serverFrame.addline(line);
		serverFrame.broadcast(line);
		running=false;
		serverFrame.removeClient(this);
		try {
			client.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
						
	}

		

}

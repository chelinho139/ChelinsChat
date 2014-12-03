/*Chelin Tutorials 2012. All Rights Reserved.
 *Educational Purpose Only.Non-Comercial & Profitable.
 *Contact: chelo_c@live.com /chelinho1397@gmail.com
 */

package server;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.net.InetAddress;
import java.net.UnknownHostException;


@SuppressWarnings("serial")
public class ServerFrame extends JFrame{
	
	
	//Change the Adress for your IP or "localhost" for internal test.
	//for example: ADRESS = "190.XXX.XX.XX"
	public static final int PORT = 9999;
	public static final String ADRESS = "localhost";
	
	Vector<IndividualThread> clients; //vector holding diferrent clients
	JPanel panel;
	JTextArea textArea;
	ServerSocket serversocket;
	

	ServerFrame(){
		init_UI();
		start_server();
		listenSocket();
	}

	/**Starts the User Interface*/
	private void init_UI() {
		setTitle("ChelinTutorials Server");
		setBounds(200, 200, 400, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE );


		textArea = new JTextArea();
		textArea.setBackground(Color.white);
		
		JScrollPane scrollpane = new JScrollPane(textArea);
				
		
		
		panel = new JPanel();

		panel.setLayout(new BorderLayout());
		getContentPane().add(panel);
		panel.add("North", new JLabel("Log:"));
		panel.add("Center", scrollpane);
		
		setVisible(true);
	}


	/**Starts the socket Server.*/
	private void start_server() {
		try{

			InetAddress ip = InetAddress.getByName(ADRESS);
				
			serversocket = new ServerSocket(PORT,0,ip);
			clients = new Vector<IndividualThread>();
		} 
		
		catch (UnknownHostException e) {

		}
		catch (IOException e) {
			System.out.println("Could not start conection on port "+PORT);
			System.exit(-1);
		}
		System.out.println("Server started on IP: "+serversocket.getInetAddress());
	}

	/**Funcion que escucha constantemente por nuevas conecciones.
	 * Al conectarse alguna coneccion abre un thread con esa coneccion*/
	public void listenSocket(){

		try{
			while(true){
				Thread.sleep(200);
				Socket new_conecction= serversocket.accept();

				IndividualThread w = new IndividualThread(new_conecction,this);
				Thread t = new Thread(w);
				t.start();
			}
		}
		
		//2 Catch 1 para conection y otro para el sleep.
		catch (IOException e) {
			System.out.println("Accept failed: 4444");
			System.exit(-1);
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	
	}
	
	
	protected void finalize(){
		try{
			System.out.println("Closing server...");
			serversocket.close();
		} 
		catch (IOException e) {
			System.out.println("Could not close socket");
			System.exit(-1);
		}
	}


	public void addline(String line) {
		textArea.append(line+"\n");
	}	


	/**Adds a client to the client vectors**/
	public void registerClient(IndividualThread clientthread) {
		clients.add(clientthread);
	}
	
	/**Removes a client to the client vectors**/
	public void removeClient(IndividualThread clientthread) {
		clients.remove(clientthread);
		
	}
	
	/**Sends a message to all clients**/
	public void broadcast(String parseline) {
		
		String sendline = parseline;
		
		Iterator<IndividualThread> it;
		it = clients.iterator();
		int i = 0;
		while(it.hasNext()){
			IndividualThread temp_client = it.next();
			temp_client.send(sendline);
			
		}
	}
	
	public static void main(String[] args){
		@SuppressWarnings("unused")
		ServerFrame window = new ServerFrame();
	}





}

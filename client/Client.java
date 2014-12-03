/*Chelin Tutorials 2012. All Rights Reserved.
 *Educational Purpose Only.Non-Comercial & Profitable.
 *Contact: chelo_c@live.com /chelinho1397@gmail.com
 */

package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;



@SuppressWarnings("serial")
public class Client extends JFrame implements ActionListener, Runnable{

	//Change the Adress for your IP or "localhost" for internal test.
	//for example: ADRESS = "190.XXX.XX.XX"
	public static final int PORT = 9999;
	public static final String ADRESS = "localhost";
	
	//Conection Stuff
	private Socket socket = null;
	private PrintWriter serverout = null;
	private BufferedReader serverin = null;
	private Thread  rcvthread;
	
	
	//UI Stuff
	private  JPanel toppanel,centerpanel;
	private JLabel toplabel;
	private JTextField textfield;
	private JButton sendbutton;
	private JTextArea textArea;
	private boolean running;
	
	Client(){ 
		init_UI();
		
		connect();
		
		running=true;
		
		//Create a thread that recieves messages
		rcvthread = new Thread(this);
		rcvthread.start();
	}

	/**Creates User Interface**/
	private void init_UI() {
		setTitle("ChelinTutorials Client 1.0");
		setBounds(200, 200, 400, 200);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowListnerClient(this));
		
		
		Container content= getContentPane();
		content.setLayout(new BorderLayout());

		toppanel=new JPanel();
		toppanel.setLayout(new FlowLayout());
		toplabel= new JLabel("You Say:");
		textfield= new JTextField(23);
		textfield.addActionListener(this);
	
	
		sendbutton = new JButton("Send!");
		sendbutton.addActionListener(this);
		toppanel.add(toplabel);
		toppanel.add(textfield);
		toppanel.add(sendbutton);
		
		centerpanel=new JPanel();
		centerpanel.setLayout(new BorderLayout());
		centerpanel.setBorder(
				BorderFactory.createLineBorder(Color.gray));
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBackground(Color.white);
		JScrollPane scrollpane = new JScrollPane(textArea); 
			
		centerpanel.add(scrollpane,BorderLayout.CENTER);
		
		content.add(toppanel, BorderLayout.SOUTH);
		content.add(centerpanel, BorderLayout.CENTER);


	}

	/**Create a new Socket**/
	public void connect(){
		try{
			socket = new Socket(ADRESS,PORT);
			serverout = new PrintWriter(socket.getOutputStream(), true);
			serverin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} 
		catch (UnknownHostException e) {
			System.out.println("Unknow Host execption");
			System.exit(1);
		} 
		catch  (IOException e) {
			System.out.println("No Server Found");
			System.exit(1);
		}
	}

	/**Sends a given text over the socket.
	 * 
	 * @param  text
	 * String to send over the socket
	 */
	private void send_data(String text){

		try {
			serverout.println(text);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**Recieves a string from the socket**/
	private void rcv_data(){
		String line;
		try{
			line = serverin.readLine();
			if(line!=null){
				
				System.out.println("rcv:"+line);
				textArea.append(line+"\n");
				scroll_down();
			}
		} 
		catch (IOException e){
			System.out.println("RCV DATA IN CLIENT FAILED");
			e.printStackTrace();
			System.exit(1);
		}	
	}

	/**Loops over an over recieving message**/
	public void run() {
		
		while(running){
			System.out.println("socketis closed: "+socket.isClosed());
			if(socket.isClosed())running=false;
			System.out.println("while running: "+running);
			rcv_data();
			try {
				Thread.sleep(200);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**Executed when a button is clicked**/
	public void actionPerformed(ActionEvent arg0) {
		send_data(textfield.getText());
		scroll_down();
		textfield.setText("");		
		
		
	}
	/**Scrolls down**/
	private void scroll_down(){
		textArea.selectAll();//Sirve para mirar abajo 
	}


	/**Log outs the client**/
	public void logout() {
		send_data("/logout");
		running=false;
		
		try {
			Thread.sleep(500);
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args){
		Client window = new Client();
		window.setVisible(true);

	}




}

/*Chelin Tutorials 2012. All Rights Reserved.
 *Educational Purpose Only.Non-Comercial & Profitable.
 *Contact: chelo_c@live.com /chelinho1397@gmail.com
 */
package client;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/*Or you could and adpter..*/
public class WindowListnerClient implements WindowListener{
	
	Client ventanacliente;
	
	WindowListnerClient(Client ventanacliente){
		this.ventanacliente=ventanacliente;
	}
	
	
	public void windowClosed(WindowEvent arg0) {
		System.out.println("Client Closed");
		ventanacliente.logout();
	}
	public void windowActivated(WindowEvent arg0) {	
	}
	public void windowClosing(WindowEvent arg0) {	
		
	}
	
	public void windowDeactivated(WindowEvent arg0) {
	}
	public void windowDeiconified(WindowEvent arg0) {
	}
	public void windowIconified(WindowEvent arg0) {
	}

	public void windowOpened(WindowEvent arg0) {
	}
	
	

}
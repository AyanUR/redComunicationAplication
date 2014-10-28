import javax.swing.JOptionPane;
import javax.swing.JFrame;
import java.net.Socket;
import java.net.InetAddress;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
public class Client{
	private Socket client;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private LinkedList <Product> products;
	private Tienda tienda;
	public Client(String ip,int port){
		try{
			client=new Socket(InetAddress.getByName(ip),port);
			System.out.print("\ncliente conectado :D");
			reciveTienda();
		}catch(Exception e){System.out.print("\nerror al iniciar cliente :("+e.getMessage());}
	}
	public void reciveTienda(){
		Object aux;
		try{
			ois=new ObjectInputStream(client.getInputStream());
			if((aux=ois.readObject())!=null)
				tienda=(Tienda)aux;
			tienda.addTableandButton(client);
			tienda.setVisible(true);
		}catch(Exception e){System.out.print("\nerror al recivir productos :("+e.getMessage());}
	}
	public static void main(String args[]){
			Client c=new Client(JOptionPane.showInputDialog(null,"ingrese la ip del servidor"),Integer.parseInt(JOptionPane.showInputDialog(null,"ingrese el puerto del servidor")));
	}	
}

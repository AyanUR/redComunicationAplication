import java.net.Socket;
import java.net.InetAddress;
import javax.swing.JOptionPane;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
public class Client{
	private Socket client;
	public Client(String ip,int port){
		try{
			client=new Socket(InetAddress.getByName(ip),port);
			System.out.print("\nclient connect :d");
			sendPropertyOfBuscaminas();
			receiveBuscaminas();
		}catch(Exception e){System.out.print("\nerror in wake up client "+e.getMessage());}
	}
	public void sendPropertyOfBuscaminas(){
		try{
			ObjectOutputStream oos=new ObjectOutputStream(client.getOutputStream());
		//	oos.writeInt(Integer.parseInt(JOptionPane.showInputDialog(null,"numero de filas en el tablero del buscaminas")));	oos.flush();
		//	oos.writeInt(Integer.parseInt(JOptionPane.showInputDialog(null,"numero de columnas en el tablero del buscaminas")));	oos.flush();
			String levelString=JOptionPane.showInputDialog(null,"dificultad facil[f]/media[m]/dificil[d]");
			Float level=new Float(.7);
			if(levelString.startsWith("f"))
				level=new Float(.75);
			if(levelString.startsWith("m"))
				level=new Float(.50);
			if(levelString.startsWith("d"))
				level=new Float(.30);
			oos.writeFloat(level);	oos.flush();
		}catch(Exception e){System.out.print("\nerror in sendPropertyOfBuscaminas "+e.getMessage());}
	}
	public void receiveBuscaminas(){
		try{
			Buscaminas buscaminas;
			ObjectInputStream ois=new ObjectInputStream(client.getInputStream());
			buscaminas=(Buscaminas)ois.readObject();
			buscaminas.setClient(client);
			buscaminas.setVisible(true);
		}catch(Exception e){System.out.print("\nerror in receiveBuscaminas "+e.getMessage());}
	}
	public static void main(String []args){
		Client client=new Client(JOptionPane.showInputDialog(null,"enter ip the server"),Integer.parseInt(JOptionPane.showInputDialog(null,"port")));
	}
}

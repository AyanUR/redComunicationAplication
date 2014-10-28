import java.net.MulticastSocket;
import java.net.InetAddress;
import java.net.DatagramPacket;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectOutputStream;
import javax.swing.JOptionPane;
import java.util.LinkedList;
public class Server implements Runnable{
	private MulticastSocket server;
	private LinkedList<User> onLine=new LinkedList<User>();
	public Server(String ip,int port){
		try{
			server=new MulticastSocket(port);
			server.setReuseAddress(true);
			server.joinGroup(InetAddress.getByName(ip));
			System.out.print("\nwake server ok :d");
		}catch(Exception e){System.out.print("\nerror al crear el servidor "+e.getMessage());}
	}
	public User serchForName(LinkedList <User> list,String name){
      int i;
      for(i=0;i<list.size();i++){
         if(list.get(i).getName().equals(name))
            return list.get(i);
      }
      return null;
   }
	public void printUsers(){
		int i;
		for(i=0;i<onLine.size();i++)
			System.out.print("\n"+onLine.get(i).getName());
	}
	public void sendContact(){
		try{
			ServerSocket server=new ServerSocket(5000);
			System.out.print("\nservidor iniciado para enviar contacts esperando cliete...");
			Socket client=server.accept();
			ObjectOutputStream oos=new ObjectOutputStream(client.getOutputStream());
			oos.writeObject(onLine);	oos.flush();
			System.out.print("\ncliente conectado... se envio los contactos :D");
			oos.close();	client.close();	server.close();
		}catch(Exception e){System.out.print("\nerror al enviar contactos "+e.getMessage());}
	}
	public void run(){
		String message;
		try{
			while(true){
				byte []bufer=new byte[512];
				DatagramPacket packet=new DatagramPacket(bufer,bufer.length);
				server.receive(packet);
				message=new String(packet.getData());
				message=message.substring(0,message.lastIndexOf("|"));
				if(message.startsWith("<inicio>")){
					sendContact();
					String name=message.substring(9,message.lastIndexOf(">"));
					if(serchForName(onLine,name)==null)
						onLine.add(new User(name,packet.getAddress(),packet.getPort()));
				}
//				System.out.print("\n|"+message+"|\t"+packet.getAddress()+"\t"+packet.getPort());
				Thread.sleep(17);
			}
		}catch(Exception e){System.out.print("\nerror al recivir mensaje"+e.getMessage());}
	}
	public static void main(String []args){
		Server s=new Server(JOptionPane.showInputDialog(null,"grupo a abrir"),Integer.parseInt(JOptionPane.showInputDialog(null,"puerto")));
		Thread t=new Thread(s);
		t.start();
	}
}

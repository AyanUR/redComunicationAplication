import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
public class Server{
	private ServerSocket server;
	private Socket client;
	private LinkedList<Score> scores=new LinkedList<Score>();
	private Runnable thread=new Runnable(){
		public void run(){
			try{
				Socket aux=client;
				ObjectInputStream ois=new ObjectInputStream(aux.getInputStream());
//				sendBuscaminas(aux,ois.readInt(),ois.readInt(),ois.readFloat());
				sendBuscaminas(aux,9,9,ois.readFloat());
				waitScore(aux);
			}catch(Exception e){System.out.print("error in run "+e.getMessage());}
		}
	};
	public Server(int port){
		try{
			server=new ServerSocket(port);
			server.setReuseAddress(true);
			System.out.print("\nserver wake up ok :d");
			while(true){
				System.out.print("\nwait client ...");
				client=server.accept();
				System.out.print("\nclient connect from "+client.getInetAddress()+"\t"+client.getPort());
				Thread atiende=new Thread(thread);
				atiende.start();
				Thread.sleep(15);
			}
		}catch(Exception e){System.out.print("\nerror in wake up server "+e.getMessage());}
	}
	public void sendBuscaminas(Socket client,int row,int column,float level){
		try{
			Buscaminas buscaminas=new Buscaminas(row,column,level,scores);
			ObjectOutputStream oos=new ObjectOutputStream(client.getOutputStream());
			oos.writeObject(buscaminas);	oos.flush();
		}catch(Exception e){System.out.print("\nerror to send buscaminas "+e.getMessage());}
	}
	public void waitScore(Socket client){
		try{
			ObjectInputStream ois=new ObjectInputStream(client.getInputStream());
			while(true){
				String name=(String)ois.readObject();
				int i;
				int mines=ois.readInt();
				int seconds=ois.readInt();
				System.out.print("nombre "+name+" mines "+mines+" seconds "+seconds);
				if(scores.size()<11)
					scores.add(new Score(name,mines,seconds,client.getInetAddress()));
				else{
					if((seconds/mines)<scores.getLast().getpuntuacionMinima())
					for(i=0;i<scores.size();i++){
						if((scores.get(i).getseconds()/scores.get(i).getmines())>seconds/mines)
							scores.get(i).changeAll(name,mines,seconds,client.getInetAddress());
					}
				}
				Thread.sleep(15);
			}
		}catch(Exception e){System.out.print("\nerror in waitScore "+e.getMessage());}
	}
	public static void main(String []args){
		Server server=new Server(Integer.parseInt(JOptionPane.showInputDialog(null,"port")));
	}
}

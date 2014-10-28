import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import java.util.LinkedList;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
public class Server implements Runnable{
	private File []files;
	private LinkedList <Product>products=new LinkedList <Product>();
	private LinkedList <Compra>comprados;	
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private ServerSocket server;
	private Tienda tienda;
	public Server(int port){
		putProductInList();
		try{
			server=new ServerSocket(port);	
		}catch(Exception e){System.out.print("\nerror al iniciar el servidor :(\t"+e.getMessage());}
	}
	public void selectFiles(){
		JFileChooser selector=new JFileChooser();
		selector.setMultiSelectionEnabled(true);
		if(selector.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
			files=selector.getSelectedFiles();
	}
	public void putProductInList(){
		String line;
		String []infoProduct;
		JOptionPane.showMessageDialog(null,"ingrese el documento que contiene los productos a vender");
		selectFiles();
		try{
			FileReader lector=new FileReader(files[0]);
			BufferedReader bufer=new BufferedReader(lector);
			while((line=bufer.readLine())!=null){
		 		infoProduct=line.split(",");
				products.add(new Product(infoProduct[0],Integer.parseInt(infoProduct[1]),Float.parseFloat(infoProduct[2]),infoProduct[3],infoProduct[4]));	
			}
			bufer.close();	lector.close();
		}catch(Exception e){System.out.print("\nerror al poner productos en lista\t"+e.getMessage());}
	}
	public void sendProducts(Socket client){
		try{
			oos=new ObjectOutputStream(client.getOutputStream());
			oos.writeObject(tienda);	oos.flush();
		}catch(Exception e ){System.out.print("\nerror al enviar productos :("+e.getMessage());}
		waitResponse(client);
	}
	public void waitResponse(Socket client){
			Object aux;
			int i;
			try{
				ois=new ObjectInputStream(client.getInputStream());
					String []information=(String [])ois.readObject();
					if((aux=ois.readObject())!=null)
						products=(LinkedList <Product>)aux;		
					if((aux=ois.readObject())!=null)
						comprados=(LinkedList <Compra>)aux;
					System.out.print("\n\ncliente "+information[0]+"\ntargeta"+information[1]+"\ncompro lo siguiente:");
					for(i=0;i<comprados.size();i++)
						comprados.get(i).printCompra();
					//System.out.print("\ntotal "+comprados.getLast().getTotal()+"\n");
			}catch(Exception e){System.out.print("\nerror al recivir comprados\t"+e.getMessage());}
	}
	public void run(){
		try{
			while(true){
				tienda=new Tienda("tienda de ayan",750,700,products);
				System.out.print("\nesperando cliente...");
				Socket client=server.accept();
				System.out.print("\ncliente "+client.getInetAddress()+"\t"+client.getPort());
				sendProducts(client);
			}
		}catch(Exception e){System.out.print("\nerror en el hilo :(\t"+e.getMessage());}
	}
	public static void main(String args[]){
		Server s=new Server(Integer.parseInt(JOptionPane.showInputDialog(null,"ingrese el puerto")));
		Thread thread=new Thread(s);
		thread.start();
	}
}

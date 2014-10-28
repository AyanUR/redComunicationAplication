import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
public class Servidor extends JFrame{
	private static JTextArea area;
	private static JScrollPane scroll;
	private static ServerSocket servidor;
	private static Socket cliente;
	private static ObjectInputStream ois;
	private static int i;
	public Servidor(String title){
		super(title);
		setLayout(new GridLayout(1,1));
		setSize(300,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		area=new JTextArea();
		scroll=new JScrollPane(area);
		this.add(scroll);
		setVisible(true);
	}
	public static void reciveFile(String route){
		String namefile="";
		long tamanofile=0,completado=0;
		int leidos;
		byte []bufer=new byte[1024];
		try{
			namefile=(String)ois.readObject();
			tamanofile=ois.readLong();
			area.append("\nreciviendo "+namefile+"\ttamano = "+tamanofile);
			BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(route+namefile));
			while((tamanofile-completado)>0){
				if((tamanofile-completado)>=bufer.length)
					leidos=ois.read(bufer,0,bufer.length);
				else
					leidos=ois.read(bufer,0,(int)(tamanofile-completado));
				completado+=leidos;
				bos.write(bufer,0,leidos);	bos.flush();
			}
			area.append("\nrecivido "+completado);
			bos.close();
		}catch(Exception e){area.append("\nerror al recivir el archivo "+namefile+"/"+tamanofile+" "+e.getMessage());}
	}
	public static void main(String []args){
		int port,numfiles;
		String route;
		Servidor mensajes;
		
		route=JOptionPane.showInputDialog(null,"ingrese la ruta donde guardar los archivos");
		port=Integer.parseInt(JOptionPane.showInputDialog(null,"puerto a iniciar la conexion"));
		mensajes=new Servidor("area de mensajes");
		try{
			servidor=new ServerSocket(port);
			while(true){
				area.append("\nesperando cliente...");
				cliente=servidor.accept();
				//cliente.setSoTimeout(3000);
area.append("\ncliente conectado desde "+cliente.getInetAddress()+" con el puerto "+cliente.getPort());
				ois=new ObjectInputStream(cliente.getInputStream());
				numfiles=ois.readInt();
				System.out.print("\nnumfiles = "+numfiles);
				for(i=0;i<numfiles;i++)
					reciveFile(route);
			}
		}catch(Exception e){area.append("\nerror al iniciar el servidor "+e.getMessage());}
	}
}

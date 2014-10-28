import java.net.*;
import java.io.*;
import javax.swing.*; 
import java.awt.*;
public class Cliente extends JFrame{
	private static File []files;
	private static Socket cliente;
	private static int i;
	private static JTextArea area;
	private static JScrollPane scroll;
	private static ObjectOutputStream oos;
	public Cliente(String title){
		super(title);
		setLayout(new GridLayout(1,1));
		setSize(500,300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		area=new JTextArea();
		scroll=new JScrollPane(area);
		this.add(scroll);
		this.setVisible(true);
	}
	public static void selectFiles(){
		JFileChooser selector= new JFileChooser();
		selector.setMultiSelectionEnabled(true);
		if(selector.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
			files=selector.getSelectedFiles();
	}
	public static void sendFile(File file){
		byte[]bufer=new byte[1024];
		int leidos=0;
		try{
			BufferedInputStream bis=new BufferedInputStream(new FileInputStream(file.getAbsolutePath()));
			//BufferedOutputStream bos=new BufferedOutputStream(cliente.getOutputStream());
			oos.writeObject(file.getName());	oos.flush();
			area.append("\nse envio el nombre del archivo "+file.getName()+" correctamente");
			oos.writeLong(file.length());	oos.flush();
			area.append("\nse envio el tamano del archivo "+file.length()+" correctamente");
			while((leidos=bis.read(bufer,0,bufer.length))!=-1){
				oos.write(bufer,0,leidos);	oos.flush();
			}
			area.append("\nse envio correctamete "+file.getName());
			bis.close();
		}catch(Exception e){area.append("\nerror al enviar "+file.getName()+" "+e.getMessage());}
	}
	public static void main(String []args){
		String ip;
		int port;
		Cliente mensajes;
		
		ip=JOptionPane.showInputDialog(null,"ingrese la direccion ip del servidor");
		port=Integer.parseInt(JOptionPane.showInputDialog(null,"puerto a inicial la conexion"));
		System.out.println("\nip = "+ip+" port= "+port);
		selectFiles();
		mensajes=new Cliente("area de mensajes");
		try{
			cliente=new Socket(InetAddress.getByName(ip),port);
			//cliente.setSoLinger(true,11);
			area.append("\ncliente conectado desde "+ip+" atraves del puerto "+port);
			oos=new ObjectOutputStream(cliente.getOutputStream());
			oos.writeInt(files.length);	oos.flush();
			for(i=0;i<files.length;i++)
				sendFile(files[i]);
			mensajes.setVisible(false);
			mensajes.dispose();
			cliente.close();
		}catch(Exception e){area.append("\nerror al inicial cliente "+e.getMessage());}
	}
}

import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Font;
import java.net.Socket;
import java.net.MulticastSocket;
import java.net.InetAddress;
import java.net.DatagramPacket;
import java.util.LinkedList;
import java.io.File;
import java.io.ObjectInputStream;
public class Client implements Runnable{
	private MulticastSocket client;
	private DatagramPacket packetEnviado;
	private byte []buferEnviado=new byte[512];
	private String nick,privado="<private>";
	private LinkedList<User> onLine=new LinkedList<User>();
	private Emoticon []emoticones;
	private JFrame window=new JFrame("chat");
	private JTextPane messages=new JTextPane();
	private JPanel contacts=new JPanel(new GridLayout(0,1));
	private JTextField input=new JTextField(11);
	private ItemListener itemListener=new ItemListener(){
		public void itemStateChanged(ItemEvent c){
			User temp=(User)c.getSource();
			if(privado.contains(temp.getName()))
				privado=privado.replace("<"+temp.getName()+">","");
			else
				privado+="<"+temp.getName()+">";
		}
	};
	public Client(String ip,int port){
		try{
			client=new MulticastSocket(port);
			client.joinGroup(InetAddress.getByName(ip));
			packetEnviado=new DatagramPacket(buferEnviado,buferEnviado.length,InetAddress.getByName(ip),port);
			nick=JOptionPane.showInputDialog(null,"nick");
			privado+="<"+nick+">";
			System.out.print("\n"+nick+" conectado :d");
		}catch(Exception e ){System.out.print("\nerro al conectar cliente "+e.getMessage());}
		buildWindow();
		loadEmoticones();
	}
	public void buildWindow(){
		KeyListener keyListener=new KeyListener(){
			public void keyReleased(KeyEvent er){
				if(er.getKeyCode()!=KeyEvent.VK_ENTER)
					return;
				if(privado.equals("<private><"+nick+">"))
					sendMessage("<msn><"+nick+"> -> "+input.getText());
				else
					sendMessage(privado+" -> "+input.getText());
				input.setText("");
			}
			public void keyTyped(KeyEvent et){;}
			public void keyPressed(KeyEvent ep){;}
		};
		JScrollPane scrollOfMessages=new JScrollPane(messages);
		JScrollPane scrollOfContacts=new JScrollPane(contacts);
		window.setSize(500,600);
		window.setLayout(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		messages.setEnabled(false);
		messages.setFont(new Font("Dialog",Font.BOLD,17));
		messages.setBounds(0,0,400,500);
		window.add(messages);
		contacts.setBounds(400,0,100,500);
		window.add(contacts);
		input.setBounds(0,500,600,50);
		input.addKeyListener(keyListener);
		window.add(input);
		window.setVisible(true);
	}
	public void loadEmoticones(){
		int i;
		File temp=new File("emoticones");
		File []files=temp.listFiles();
		emoticones=new Emoticon[files.length];
		for(i=0;i<files.length;i++){
			String name=files[i].getName().substring(0,files[i].getName().lastIndexOf("."));
			emoticones[i]=new Emoticon(name,name,files[i].getAbsolutePath());
		}
	}
	public void sendMessage(String message){
		message+="|";
		try{
			packetEnviado.setData(message.getBytes());
			packetEnviado.setLength(message.length());
			client.send(packetEnviado);
		}catch(Exception e){System.out.print("\nerror al enviar mensaje "+e.getMessage());}
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
	public void addContactInPanel(User contact){
		contact.addItemListener(itemListener);
		contacts.add(contact);
		contacts.revalidate();
	}
	public Emoticon isEmoticon(String token){
		int i;
		for(i=0;i<emoticones.length;i++){
			if(emoticones[i].getToken().equals(token))
				return emoticones[i];
		}
		return null;
	}
	public void putMessageInTextPane(String message){
		String []aux=message.split(" ");
		int i;
		Emoticon emoticon;
		try{
			for(i=0;i<aux.length;i++){
				if((emoticon=isEmoticon(aux[i]))!=null){
					messages.setCaretPosition(messages.getStyledDocument().getLength());
					messages.insertIcon(emoticon);
				}
				else
					messages.getStyledDocument().insertString(messages.getStyledDocument().getLength(),aux[i]+" ",null);
			}
			messages.getStyledDocument().insertString(messages.getStyledDocument().getLength(),System.getProperty("line.separator"),null);
		}catch(Exception e){System.out.print("\nerror al poner el mensaje "+e.getMessage());}
	}
	public void receiveContact(){
		int i;
		try{
			Socket client=new Socket(InetAddress.getByName("127.0.0.1"),5000);
			System.out.print("\ncliente conectado listo para recivir contacts");
			ObjectInputStream ois=new ObjectInputStream(client.getInputStream());
			onLine.addAll((LinkedList <User>)ois.readObject());
			ois.close();	client.close();
			System.out.print("\nrecivi contacts :D");
		}catch(Exception e){System.out.print("\nerror al recivir "+e.getMessage());}
		for(i=0;i<onLine.size();i++)
			addContactInPanel(onLine.get(i));
	}
	public void run(){
		String message;
		try{
			sendMessage("<inicio><"+nick+">");
			receiveContact();
			while(true){
				byte []bufer=new byte[512];
				DatagramPacket packet=new DatagramPacket(bufer,bufer.length);
				client.receive(packet);
				message=new String(packet.getData());
				message=message.substring(0,message.lastIndexOf("|"));
				if(message.startsWith("<inicio>")&&!message.endsWith("<"+nick+">")){
					String name=message.substring(9,message.lastIndexOf(">"));
					if(serchForName(onLine,name)==null){
						onLine.add(new User(name,packet.getAddress(),packet.getPort()));
						addContactInPanel(onLine.getLast());
					}
				}
				if(message.startsWith("<msn>")||(message.startsWith("<private>")&&message.contains("<"+nick+">")))
					putMessageInTextPane(message);
				Thread.sleep(17);
			}
		}catch(Exception e){System.out.print("\nerror al recivir "+e.getMessage());}
	}
	public static void main(String []args){
		Client c=new Client(JOptionPane.showInputDialog(null,"direccion de grupo"),Integer.parseInt(JOptionPane.showInputDialog(null,"puerto")));
		Thread t=new Thread(c);
		t.start();
	}
}

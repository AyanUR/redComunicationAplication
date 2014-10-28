import javax.swing.JCheckBox;
import java.net.InetAddress;
import java.util.LinkedList;
public class User extends JCheckBox{
	private String name;
	private InetAddress ip;
	private int port;
	public User(String name,InetAddress ip,int port){
		super(name);
		this.name=name;
		this.ip=ip;
		this.port=port;
	}
	public String getName(){
		return name;
	}
}

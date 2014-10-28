import javax.swing.ImageIcon;
public class Emoticon extends ImageIcon{
	public String name,token;
	public Emoticon(String name,String token,String route){
		super(route);
		this.name=name;
		this.token=token;
	}
	public String getName(){
		return name;
	}
	public String getToken(){
		return token;
	}
}

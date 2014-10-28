import java.net.InetAddress;
public class Score{
	private static float puntuacionMinima=new Float(0);
	private String name;
	private int mines,seconds;
	private InetAddress ip;
	public Score(String name,int mines,int seconds,InetAddress ip){
		this.name=name;
		this.mines=mines;
		this.seconds=seconds;
		this.ip=ip;
		if((seconds/mines)>puntuacionMinima)
			puntuacionMinima=(seconds/mines);
	}
	public Float getpuntuacionMinima(){
		return puntuacionMinima;
	}
	public String getname(){
		return name;
	}
	public int getmines(){
		return mines;
	}
	public int getseconds(){
		return seconds;
	}
	public void changeAll(String name,int mines,int seconds,InetAddress ip){
		this.name=name;
		this.mines=mines;
		this.seconds=seconds;
		this.ip=ip;
	}
}

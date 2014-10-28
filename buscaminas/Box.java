import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.io.Serializable;
public class Box extends JButton implements Serializable{
	private static final long serialVersionUID=1L;
	private boolean mine=true,flat=false,click=false,destapado=false;
	private int mineOround=0,row,column;
	public Box(float level,int row,int column){
		super("");
		setFont(new Font("Serif",Font.BOLD,21));
		setBackground(java.awt.Color.blue);
		this.row=row;
		this.column=column;
		if(Math.random()<level)
			mine=false;
	}
	public boolean isMine(){
		return mine;
	}
	public boolean getClick(){
		return click;
	}
	public boolean getdestapado(){
		return destapado;
	}
	public void setdestapado(boolean destapado){
		this.destapado=destapado;
	}
	public int getRow(){
		return row;
	}
	public int getColumn(){
		return column;
	}
	public void setmineOround(int mineOround){
		this.mineOround=mineOround;
	}
	public boolean drawFlat(){
		if(!flat){
			setIcon(new ImageIcon("imagenes/flat.png"));
			flat=true;
		}else{
			setIcon(null);
			flat=false;
		}
		return flat;
	}
	public void setClick(boolean value){
		click=value;
	}
	public int getmineOround(){
		return mineOround;
	}
	public void drawMine(){
		setIcon(new ImageIcon("imagenes/mine.gif"));
	}
	/*public void destapa(){
		if(mineOround>0)
			setText(""+mineOround);
		else
			setBackground(java.awt.Color.white);
		click=true;
	}*/
}

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.InputEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.GridLayout;
import java.awt.Font;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
public class Buscaminas extends JFrame implements MouseListener,Runnable,Serializable{
	private static final long serialVersionUID=1L;
	private JButton newGame=new JButton("new game"),scoresButton=new JButton("scores");
	private JLabel timeLabel=new JLabel("0"),minesLabel=new JLabel("");
	private LinkedList<Score> scores=new LinkedList<Score>();
	private JPanel board;
	private Box [][]boxs;
	private int row,column,mineRest,mineTotal,counter,second;
	private Float level;
	private boolean isReady=false;
	private Socket client;
/*	private Runnable timer=new Runnable(){
		public void run(){
				second=0;
				while(isReady){
					try{
						Thread.sleep(1000);
						timeLabel.setText(""+(++second));
					}
					catch(InterruptedException e){System.out.print("\nerror in timer "+e.getMessage());}
				}
		}
	};*/
/*	private MouseAdapter listener=new MouseAdapter(){
		public void mouseClicked(MouseEvent e){
			Box aux;
			if(e.getSource()instanceof Box){
				aux=(Box)e.getSource();
				startTimer();
				if((e.getModifiers()&InputEvent.BUTTON1_MASK)==InputEvent.BUTTON1_MASK){
					if(aux.isMine())
						endGame();
					else{
						if(!aux.getClick())
							counter++;
						if((row*column)==(mineTotal+counter))
							winGame();
						aux.destapa();
					}
				}else{putOrquitFlat(aux);}
			}else{putBoard(row,column,level);}
		}
	};*/
	public Buscaminas(int row,int column,float level,LinkedList <Score> scores){
		super("buscaminas");
		setSize(500,600);
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.row=row;	this.column=column;	this.level=level;	this.scores=scores;
		board=new JPanel(new GridLayout(row,column));
		boxs=new Box[row][column];
		timeLabel.setBounds(25,0,100,70);
		timeLabel.setFont(new Font("Serif",Font.BOLD,21));
		add(timeLabel);
		newGame.addMouseListener(this);
		newGame.setBounds(150,0,200,35);
		add(newGame);
		scoresButton.addMouseListener(this);
		scoresButton.setBounds(150,35,200,35);
		add(scoresButton);
		minesLabel.setBounds(400,0,100,70);
		minesLabel.setFont(new Font("Serif",Font.BOLD,21));
		add(minesLabel);
		putBoard(row,column,level);
		board.setBounds(0,70,500,400);
		add(board);
		//setVisible(true);
	}
	public void mousePressed(MouseEvent e){;}
	public void mouseReleased(MouseEvent e){;}
	public void mouseEntered(MouseEvent e){;}
	public void mouseExited(MouseEvent e){;}	
	public void mouseClicked(MouseEvent e){
			Box aux;
			if(e.getSource()instanceof Box){
				aux=(Box)e.getSource();
				startTimer();
				if((e.getModifiers()&InputEvent.BUTTON1_MASK)==InputEvent.BUTTON1_MASK){
					if(aux.isMine())
						endGame();
					else{
						if(!aux.getClick())
							counter++;
						if((row*column)==(mineTotal+counter))
							winGame();
						destapa(aux);
					}
				}else{putOrquitFlat(aux);}
			}else{
				if(((JButton)e.getSource()).getText().equals("new game"))
					putBoard(row,column,level);
				else{
					int i;
					JPanel panel=new JPanel(new GridLayout(11,2));
					for(i=0;i<scores.size();i++){
						panel.add(new JLabel(scores.get(i).getname()));
						panel.add(new JLabel(""+(scores.get(i).getseconds()/scores.get(i).getmines())));
					}
					JOptionPane.showMessageDialog(null,panel);
				}
			}
	}
	public void destapa(Box button){
		//System.out.print("\nrow="+button.getRow()+"\tcolumn"+button.getColumn());
		if(button.getmineOround()>0)
         button.setText(""+button.getmineOround());
      else{
 			button.setBackground(java.awt.Color.white);
			button.setdestapado(true);
			int i=button.getRow(),j=button.getColumn(),k,l;
			for(l=i-1;l<i+2;l++){
				for(k=j-1;k<j+2;k++){
					if((l>=0&&l<row)&&(k>=0&&k<column)&&!boxs[l][k].getdestapado())
						destapa(boxs[l][k]);
				}
			}	
		}
      button.setClick(true);
	}
	public void run(){
				second=0;
				while(isReady){
					try{
						Thread.sleep(1000);
						timeLabel.setText(""+(++second));
					}
					catch(InterruptedException e){System.out.print("\nerror in timer "+e.getMessage());}
				}
	}
	public void putBoard(int row,int column,float level){
		board.removeAll();
		board.paintAll(board.getGraphics());
		mineRest=counter=mineTotal=second=0;	timeLabel.setText(""+second);	minesLabel.setText(""+mineRest);
		int i,j;
		for(i=0;i<row;i++){
			for(j=0;j<column;j++){
				boxs[i][j]=new Box(level,i,j);
				if(boxs[i][j].isMine())mineRest++;
				boxs[i][j].addMouseListener(this);
				board.add(boxs[i][j]);
			}
		}
		putNumberInBox(row,column);
		minesLabel.setText(""+(mineTotal=mineRest));
	}
	public void startTimer(){
		if(!isReady){
			isReady=true;
			Thread thread=new Thread(this);
			thread.start();
		}
	}
	public void endGame(){
		int i,j;
		Box aux;
		isReady=false;
		JOptionPane.showMessageDialog(null,"as perdido pisaste una mina");
		for(i=0;i<row;i++){
			for(j=0;j<column;j++){
				if(boxs[i][j].isMine())
					boxs[i][j].drawMine();
				boxs[i][j].setEnabled(false);
			}
		}
	}
	public void winGame(){
		isReady=false;
		JOptionPane.showMessageDialog(null,"felisidades minero ganaste !");
		try{
			ObjectOutputStream oos=new ObjectOutputStream(client.getOutputStream());
			String name=JOptionPane.showInputDialog(null,"ingresa tu nombre");
			oos.writeObject(name);	oos.flush();
			oos.writeInt(mineTotal);	oos.flush();
			oos.writeInt(second);	oos.flush();
			updateScores(name);
		}catch(Exception e){System.out.print("\nerror al enviar score "+e.getMessage());}
//		String []playing=firstPlace.split("|");
//		String []name=new String[playing.length];
	}
	public void updateScores(String name){
		int i;
		if(scores.size()<11)
			scores.add(new Score(name,mineTotal,second,client.getInetAddress()));
		else{
			if((second/mineTotal)<scores.getLast().getpuntuacionMinima())
				for(i=0;i<scores.size();i++){
					if((scores.get(i).getseconds()/scores.get(i).getmines())>second/mineTotal)
						scores.get(i).changeAll(name,mineTotal,second,client.getInetAddress());
				}
		}
	}
	public void putOrquitFlat(Box box){
		if(box.drawFlat())
			minesLabel.setText(""+(--mineRest));
		else
			minesLabel.setText(""+(++mineRest));
	}
	public void putNumberInBox(int row,int column){
		int i,j,k,l,cta;
		for(i=0;i<row;i++){
			for(j=0;j<column;j++){
				cta=0;
				for(l=i-1;l<i+2;l++){
					for(k=j-1;k<j+2;k++){
						if((l>=0&&l<row)&&(k>=0&&k<column))
							if(boxs[l][k].isMine())
								cta++;
					}
				}	
				boxs[i][j].setmineOround(cta);
			}
		}
	}
	public void setClient(Socket client){
		this.client=client;
	}
}

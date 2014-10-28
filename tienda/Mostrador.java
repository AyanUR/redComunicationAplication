import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.ImageIcon;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JFrame;
import javax.swing.text.BadLocationException;
import java.awt.Image;
import java.awt.GridLayout;
import java.io.Serializable;
public class Mostrador extends JPanel implements Serializable{
	private static final long serialVersionUID=1L;
	public JButton []addCar;
	public JScrollPane scroll;
	public Mostrador(int numberProducts){
			super(new GridLayout(0,2));
			addCar=new JButton[numberProducts];
			scroll=new JScrollPane(this);
//			information=new JTextPane[numberProducts];
	}
	public void addProduct(int numberProduct,Product product,Tienda tienda){
		addImage(product.getImage(),150,150);
		addInformation(numberProduct,product.getName(),product.getParts(),product.getPrice(),product.getDescription(),tienda);
	}
	public void addImage(String route,int width,int height){
		Icon icon=new ImageIcon(new ImageIcon(route).getImage().getScaledInstance(width,height,Image.SCALE_DEFAULT));
		this.add(new JLabel(icon));
		this.revalidate();
	}
	public void addInformation(int numberProduct,JLabel name,JLabel parts,JLabel price,JLabel description,Tienda tienda){
		JPanel temp=new JPanel(new GridLayout(5,1));
		temp.add(name);temp.add(parts);temp.add(price);temp.add(description);
		temp.add(addCar[numberProduct]=new JButton("agregar"));
		addCar[numberProduct].addActionListener(tienda);
		this.add(temp);
		this.revalidate();
	}
}

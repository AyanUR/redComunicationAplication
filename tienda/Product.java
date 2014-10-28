import javax.swing.JLabel;
import java.util.LinkedList;
import java.io.Serializable;
public class Product implements Serializable{
	private static final long serialVersionUID=1L;
	private String nameImage,nombre;
	private int piesas;
	private Float precio;
	private JLabel name,parts,price,description;
	public Product(String name,int parts,Float price,String description,String nameImage){
		this.name=new JLabel(name);
		this.parts=new JLabel(""+parts);
		this.price=new JLabel(""+price);
		this.description=new JLabel(description);
		this.nameImage=nameImage;
		this.nombre=name;
		this.piesas=parts;
		this.precio=price;
	}
	public int serchForName(String name,LinkedList <Product> productos){
		int i;
		for(i=0;i<productos.size();i++){
			if(productos.get(i).getNombre().equals(name))
				return i;
		}
		return -1;
	}
	public void printProduct(){
		System.out.print("\n"+this.getNombre()+" "+this.getPiesas()+" "+this.getPrecio());
	}
	public JLabel getDescription(){
		return description;
	}
	public JLabel getName(){
		return  name;
	}
	public JLabel getPrice(){
		return price;
	}
	public JLabel getParts(){
		return parts;
	}
	public String getNombre(){
		return nombre;
	}
	public String getImage(){
		return nameImage;
	}
	public int getPiesas(){
		return piesas;
	}
	public Float getPrecio(){
		return precio;
	}
	public void popParts(){
		parts.setText(""+(--piesas));
	}
	public void pushParts(){
		parts.setText(""+(++piesas));
	}
}

import java.util.LinkedList;
import java.io.Serializable;
public class Compra implements Serializable{
	private static final long serialVersionUID=1L;
	private static Float total=new Float(0);
	private String name;
	private int parts;
	private Float price;
	private Float subTotal;
	public Compra(String name,int parts,Float price){
		this.name=name;
		this.parts=parts;
		this.price=price;
		this.subTotal=price;
		total+=price;
	}
	public int serchForName(String name,LinkedList <Compra> list){
		int i;
		for(i=0;i<list.size();i++){
			if(list.get(i).getName().equals(name))
				return i;
		}
		return -1;
	}
	public void printCompra(){
		System.out.print("\n"+this.getParts()+" "+this.getName()+" precio unitario $"+this.getPrice()+" subTotal $"+this.getsubTotal()+"M.N");
	}
	public String getName(){
		return name;
	}
	public Float getPrice(){
		return price;
	}
	public Float getsubTotal(){
		return subTotal;
	}
	public int getParts(){
		return parts;
	}
	public Float getTotal(){
		return total;
	}
	public void pushParts(){
		parts++;
		subTotal=parts*price;
		total+=price;
	}
	public void popParts(){	
		parts--;
		subTotal=parts*price;
		total-=price;
	}
}

import java.util.LinkedList;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.net.Socket;
public class Tienda extends JFrame implements Serializable,ActionListener{
	private static final long serialVersionUID=1L;
	public Mostrador mostrador;	
	private LinkedList <Compra> comprados=new LinkedList<Compra>();
	private LinkedList <Product> products;
	private JTable tableOfCompras;
	private DefaultTableModel modelo;
	private JButton closeCompra=new JButton("cerrar compra");
	private JLabel total=new JLabel();
	private Socket client;
	public Tienda(String title,int width,int height,LinkedList <Product> products){
		super(title);
		setSize(width,height);
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int i;
		this.products=products;
		mostrador=new Mostrador(products.size());
		for(i=0;i<products.size();i++){
			if(products.get(i).getPiesas()>0)
				mostrador.addProduct(i,products.get(i),this);
		}
		mostrador.scroll.setBounds(0,0,350,650);
		add(mostrador.scroll);
		//addTableandButton();
	}
	public void actionPerformed(ActionEvent event){
		int i;
		JButton aux=(JButton)event.getSource();
		if(aux==closeCompra){
			try{
				String []information={JOptionPane.showInputDialog(null,"nombre"),JOptionPane.showInputDialog(null,"numero de targeta")};
				ObjectOutputStream oos=new ObjectOutputStream(client.getOutputStream());
				oos.writeObject(information);	oos.flush();
				oos.writeObject(products);	oos.flush();
				oos.writeObject(comprados);	oos.flush();
				cleanTable();
				comprados.clear();
			}catch(Exception e){System.out.print("\nerror al cerrar la compra "+e.getMessage());}
			return;
		}
		for(i=0;aux!=mostrador.addCar[i];i++){;}
		if(products.get(i).getPiesas()>0){
			products.get(i).popParts();
			addCompra(products.get(i));
			total.setText("total: "+comprados.getLast().getTotal());
		}else{JOptionPane.showMessageDialog(null,"no seas menso ya no hay mas piesas! :@");}	
	}
	public void cleanTable(){
		int i=modelo.getRowCount()-1;
		for(;i>=0;i--)
			modelo.removeRow(i);
	}
	public void calcTotal(){
		Float cta=new Float(0);
		int i;
		for(i=0;i<comprados.size();i++)
			cta+=comprados.get(i).getsubTotal();
	}
	public void addCompra(Product product){
		int i;
		for(i=0;i<comprados.size();i++){
			if(comprados.get(i).getName().equals(product.getNombre())){//si esta en lista de comprados
				comprados.get(i).pushParts();
				modelo.setValueAt(comprados.get(i).getParts(),i,2);
				modelo.setValueAt(comprados.get(i).getsubTotal(),i,3);
				return ;
			}
		}//no esta en lista de comprados
		comprados.add(new Compra(product.getNombre(),1,product.getPrecio()));
		modelo.addRow(new Object[]{comprados.getLast().getName(),comprados.getLast().getPrice(),comprados.getLast().getParts(),comprados.getLast().getsubTotal()});
	}
	public void addTableandButton(Socket client){
		this.client=client;
		String []nameColumnforTable={"nombre","precioUnitario","piesas","subTotal"};
		modelo=new DefaultTableModel(null,nameColumnforTable);
		tableOfCompras=new JTable(modelo);	//tableOfCompras.setEnabled(false);
		tableOfCompras.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent evento){
				int fila=tableOfCompras.rowAtPoint(evento.getPoint());
				if(comprados.get(fila).getParts()>0){
					comprados.get(fila).popParts();
					total.setText("total: "+comprados.get(fila).getTotal());
					int i=products.getFirst().serchForName(comprados.get(fila).getName(),products);
					products.get(i).pushParts();
					modelo.setValueAt(comprados.get(fila).getParts(),fila,2);
					modelo.setValueAt(comprados.get(fila).getsubTotal(),fila,3);
				}if(comprados.get(fila).getParts()==0){modelo.removeRow(fila);comprados.remove(fila);}
			}		
		});
		JScrollPane scroll=new JScrollPane(tableOfCompras);
		scroll.setBounds(350,0,400,600);
		add(scroll);
		closeCompra.addActionListener(this);
		closeCompra.setBounds(350,600,200,50);
		add(closeCompra);
		total.setBounds(550,600,200,50);
		add(total);
	}
}

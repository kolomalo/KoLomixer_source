import com.ms.wfc.app.*;
import com.ms.wfc.core.*;
import com.ms.wfc.ui.*;
import com.ms.wfc.html.*;
import com.ms.directX.*;
import java.io.*;
import java.util.*;

/**
 * Esta clase puede tomar un número variable de parámetros
 * en la línea de comandos. La ejecución del programa comienza 
 * con el método main(). La llamada al constructor de clase no tiene lugar a menos que se cree un objeto del tipo 'Form1'
 * en el método main().
 */
public class Form1 extends Form
{	
	Vector VInfo=new Vector();
	FrmCanal c1;
	FrmCanal c2;
	
	FileInputStream FISInfo;
	ObjectInputStream OISInfo;
	FileOutputStream FOSInfo;
	ObjectOutputStream OOSInfo;
		
					  
	public Form1()
	{
		
		//super();
		
		// Requerido para la compatibilidad con el Diseñador de formularios de Visual J++
		initForm();
		// TODO: agregar el código del constructor después de la llamada a initForm.	
		this.show();
				
		try
		{
			FISInfo=new FileInputStream("C:\\Documents and Settings\\Antonio\\Mis documentos\\Mi música\\Info.kolo");
			OISInfo=new ObjectInputStream(FISInfo);
			try
			{
				VInfo=(Vector)OISInfo.readObject();
			}
			catch (ClassNotFoundException cnfe){}
			FISInfo.close();
		}
		catch (IOException ioe){}
		
		c1=new FrmCanal(this,2,2,this.getHandle());
		c1.setLeft(8);
		this.add(c1);
		c1.show();
		
		c2=new FrmCanal(this,3,1,this.getHandle());		
		c2.setLeft(610);				
		this.add(c2);		
		c2.show();
			
		this.setWindowState(2);
	}

	/**
	 * Form1 reemplaza el método "dispose" para poder
	 * limpiar la lista de componentes.
	 */
	public void dispose()
	{
		super.dispose();
		components.dispose();
	}	
	
	private void menuSalir_click(Object source, Event e)
	{
		System.gc();
		c1.cerrar();
		c2.cerrar();
		this.close();
	}
	

	//BARRAS DESPLAZAMIENTO
	private void trkBalance_scroll(Object source, Event e)
	{
		if (c1.cargado) c1.setBalance(trkBalance.getValue());
		if (c2.cargado) c2.setBalance(trkBalance.getValue());
	}

	private void trkBalance_mouseUp(Object source, MouseEvent e)
	{
		if (c1.cargado) c1.setBalance(0);
		if (c2.cargado) c2.setBalance(0);
		trkBalance.setValue(0);
	}

	private void trkCrossf_scroll(Object source, Event e)
	{
		int v=trkCrossf.getValue();		
		if (v<=0)
		{
			if (c2.cargado)	c2.setVolumen((c2.intBaseVol +v));				
			if (c1.cargado) c1.setVolumen(c1.intBaseVol);
		}
		else
		{
			if (c1.cargado) c1.setVolumen((c1.intBaseVol -v));				
			if (c2.cargado) c2.setVolumen(c2.intBaseVol);
		}		
	}

	private void trkCrossf_mouseUp(Object source, MouseEvent e)
	{
		if (c1.cargado) c1.setVolumen(c1.intBaseVol);
		if (c2.cargado) c2.setVolumen(c2.intBaseVol);
		trkCrossf.setValue(0);
	}

	
	//METODOS
	public void guardar()
	{
		try
		{
			FileOutputStream FOSInfo=new FileOutputStream("C:\\Documents and Settings\\Antonio\\Mis documentos\\Mi música\\Info.kolo");
			ObjectOutputStream OOSInfo=new ObjectOutputStream(FOSInfo);
			try
			{
				OOSInfo.writeObject((Vector)VInfo);
				OOSInfo.close();
			}
			catch(IOException ioe){}
		}
		catch (IOException ioe){}
	}	

	private void UDwnBpm_valueChanged(Object source, Event e)
	{
		float bpm=new Float(lblBpm.getText()).floatValue();
		int frq;
		if (c1.cargado && c1.InfoC.bpm>0)
		{
			c1.intBasePitch=c1.calcFreq(bpm);
			c1.fltBaseBpm=bpm;
			c1.setSpeed(c1.intBasePitch,0);
		}
		if (c2.cargado && c2.InfoC.bpm>0)
		{
			c2.intBasePitch=c2.calcFreq(bpm);
			c2.fltBaseBpm=bpm;
			c2.setSpeed(c2.intBasePitch,0);
		}
	}

	private void Form1_closing(Object source, CancelEvent e)
	{
		guardar();
		c1.cerrar();
		c2.cerrar();
	}

	/**
	 * NOTA: el código siguiente es necesario para el diseñador de
	 * formularios de Visual J++ y puede modificarse con el editor de formularios.
	 * No lo modifique desde el editor de código.
	 */
	Container components = new Container();
	MenuItem menuArchivo = new MenuItem();
	MainMenu mainMenu1 = new MainMenu();
	MenuItem menuSalir = new MenuItem();
	GroupBox groupBox1 = new GroupBox();
	TrackBar trkBalance = new TrackBar();
	Label lblR = new Label();
	Label lblL = new Label();
	TrackBar trkCrossf = new TrackBar();
	GroupBox groupBox2 = new GroupBox();
	Label label2 = new Label();
	Label label3 = new Label();
	UpDown UDwnBpm = new UpDown();
	Label lblBpm = new Label();
	Label label4 = new Label();
	MenuItem menuEdicion = new MenuItem();
	MenuItem menuEdLista = new MenuItem();
	MenuItem menuConfig = new MenuItem();
	MenuItem menuAyuda = new MenuItem();
	MenuItem menuAyAcerca = new MenuItem();

	private void initForm()
	{
		menuSalir.setText("Salir");
		menuSalir.addOnClick(new EventHandler(this.menuSalir_click));

		menuArchivo.setMenuItems(new MenuItem[] {
								 menuSalir});
		menuArchivo.setText("Archivo");

		groupBox1.setBackColor(Color.ACTIVEBORDER);
		groupBox1.setForeColor(Color.MENUTEXT);
		groupBox1.setLocation(new Point(410, -6));
		groupBox1.setSize(new Point(200, 110));
		groupBox1.setTabIndex(0);
		groupBox1.setTabStop(false);
		groupBox1.setText("");

		trkBalance.setAnchor(ControlAnchor.NONE);
		trkBalance.setBackColor(Color.ACTIVEBORDER);
		trkBalance.setLocation(new Point(25, 10));
		trkBalance.setSize(new Point(155, 42));
		trkBalance.setTabIndex(1);
		trkBalance.setTabStop(false);
		trkBalance.setText("trackBar1");
		trkBalance.setLargeChange(0);
		trkBalance.setMinimum(-100);
		trkBalance.setMaximum(100);
		trkBalance.setSmallChange(3000);
		trkBalance.setTickStyle(TickStyle.BOTH);
		trkBalance.setTickFrequency(10);
		trkBalance.addOnMouseUp(new MouseEventHandler(this.trkBalance_mouseUp));
		trkBalance.addOnScroll(new EventHandler(this.trkBalance_scroll));

		lblR.setAnchor(ControlAnchor.NONE);
		lblR.setBackColor(Color.ACTIVEBORDER);
		lblR.setLocation(new Point(178, 22));
		lblR.setSize(new Point(10, 13));
		lblR.setTabIndex(2);
		lblR.setTabStop(false);
		lblR.setText("R");
		lblR.setAutoSize(true);
		lblR.setTextAlign(HorizontalAlignment.CENTER);

		lblL.setAnchor(ControlAnchor.NONE);
		lblL.setBackColor(Color.ACTIVEBORDER);
		lblL.setLocation(new Point(18, 22));
		lblL.setSize(new Point(10, 13));
		lblL.setTabIndex(3);
		lblL.setTabStop(false);
		lblL.setText("L");
		lblL.setAutoSize(true);
		lblL.setTextAlign(HorizontalAlignment.CENTER);

		trkCrossf.setAnchor(ControlAnchor.NONE);
		trkCrossf.setBackColor(Color.ACTIVEBORDER);
		trkCrossf.setLocation(new Point(25, 65));
		trkCrossf.setSize(new Point(155, 42));
		trkCrossf.setTabIndex(0);
		trkCrossf.setTabStop(false);
		trkCrossf.setText("trackBar1");
		trkCrossf.setLargeChange(0);
		trkCrossf.setMinimum(-1000);
		trkCrossf.setMaximum(1000);
		trkCrossf.setSmallChange(0);
		trkCrossf.setTickStyle(TickStyle.BOTH);
		trkCrossf.setTickFrequency(100);
		trkCrossf.addOnMouseUp(new MouseEventHandler(this.trkCrossf_mouseUp));
		trkCrossf.addOnScroll(new EventHandler(this.trkCrossf_scroll));

		groupBox2.setLocation(new Point(410, 106));
		groupBox2.setSize(new Point(200, 208));
		groupBox2.setTabIndex(1);
		groupBox2.setTabStop(false);
		groupBox2.setText("");

		label2.setLocation(new Point(14, 78));
		label2.setSize(new Point(14, 12));
		label2.setTabIndex(4);
		label2.setTabStop(false);
		label2.setText("<--");
		label2.setTextAlign(HorizontalAlignment.CENTER);

		label3.setLocation(new Point(586, 72));
		label3.setSize(new Point(14, 12));
		label3.setTabIndex(2);
		label3.setTabStop(false);
		label3.setText("-->");
		label3.setTextAlign(HorizontalAlignment.CENTER);

		lblBpm.setFont(new Font("MS Shell Dlg", 22.0f, FontSize.POINTS, FontWeight.NORMAL, false, false, false, CharacterSet.DEFAULT, 0));
		lblBpm.setLocation(new Point(64, 32));
		lblBpm.setSize(new Point(56, 32));
		lblBpm.setTabIndex(0);
		lblBpm.setTabStop(false);
		lblBpm.setText("160");
		lblBpm.setBorderStyle(BorderStyle.FIXED_3D);
		lblBpm.setTextAlign(HorizontalAlignment.CENTER);

		UDwnBpm.setBuddyControl(lblBpm);
		UDwnBpm.setLocation(new Point(120, 32));
		UDwnBpm.setSize(new Point(16, 32));
		UDwnBpm.setTabIndex(1);
		UDwnBpm.setMinimum(130);
		UDwnBpm.setMaximum(200);
		UDwnBpm.setValue(160);
		UDwnBpm.addOnValueChanged(new EventHandler(this.UDwnBpm_valueChanged));

		label4.setFont(new Font("MS Shell Dlg", 14.0f, FontSize.POINTS, FontWeight.NORMAL, false, false, false, CharacterSet.DEFAULT, 0));
		label4.setLocation(new Point(74, 10));
		label4.setSize(new Point(42, 20));
		label4.setTabIndex(2);
		label4.setTabStop(false);
		label4.setText("BPM");
		label4.setTextAlign(HorizontalAlignment.CENTER);

		menuEdLista.setText("Lista de CUEs");

		menuEdicion.setMenuItems(new MenuItem[] {
								 menuEdLista});
		menuEdicion.setText("Edición");

		menuConfig.setText("Configuración");

		menuAyAcerca.setText("Acerca de...");

		menuAyuda.setMenuItems(new MenuItem[] {
							   menuAyAcerca});
		menuAyuda.setText("Ayuda");

		mainMenu1.setMenuItems(new MenuItem[] {
							   menuArchivo, 
							   menuEdicion, 
							   menuConfig, 
							   menuAyuda});
		/* @designTimeOnly mainMenu1.setLocation(new Point(734, 50)); */

		this.setBackColor(Color.ACTIVEBORDER);
		this.setForeColor(Color.WINDOWTEXT);
		this.setText("KoLo V-DJ");
		this.setAutoScaleBaseSize(new Point(5, 13));
		this.setBorderStyle(FormBorderStyle.FIXED_SINGLE);
		this.setClientSize(new Point(1018, 723));
		this.setMenu(mainMenu1);
		this.setStartPosition(FormStartPosition.MANUAL);
		this.addOnClosing(new CancelEventHandler(this.Form1_closing));

		this.setNewControls(new Control[] {
							label3, 
							groupBox2, 
							groupBox1});
		groupBox1.setNewControls(new Control[] {
								 label2, 
								 trkCrossf, 
								 lblL, 
								 lblR, 
								 trkBalance});
		groupBox2.setNewControls(new Control[] {
								 label4, 
								 UDwnBpm, 
								 lblBpm});
	}

	/**
	 * Punto de entrada principal para la aplicación. 
	 *
	 * @param args Matriz de parámetros pasados a la aplicación
	 * mediante la línea de comandos.
	 */
	public static void main(String args[])
	{
		Application.run(new Form1());
	}
}
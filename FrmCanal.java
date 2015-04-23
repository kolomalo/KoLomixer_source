import com.ms.wfc.app.*;
import com.ms.wfc.core.*;
import com.ms.wfc.ui.*;
import com.ms.wfc.html.*;
import java.util.*;
import java.io.RandomAccessFile;

/**
 * Esta clase puede tomar un número variable de parámetros 
 * en la línea de comandos. La ejecución del programa comienza 
 * con el método main(). La llamada al constructor de clase no tiene lugar a menos que se cree un objeto del tipo 'FrmCanal'
 * en el método main().
 */
public class FrmCanal extends Form
{	
	Dll dll;
	BPM hilo;
	Graphics GphPanel;
	Form1 f1;
	FrmInfo fi;
	Info InfoC;
	RandomAccessFile raf;
	byte[] tagv1=new byte[128];
	public boolean blnTag=false;
	String strPathFileName="";
	String strFileName="";
	String strPath="C:\\Documents and Settings\\Antonio\\Mis documentos\\Mi música\\";
	int intSaveCue=0;
	int intBasePitch;
	float fltBaseBpm;
	public int intBaseVol=0;
	int intLongitud;
	int intTrkPos;
	int intLoop=0;
	int intTrkFinePitchMax;
	int intTrkFinePitchMin;	
	int intPanelPos;
	int intTarjeta;
	int whandle;	
	int puntero;	
	public boolean cargado=false;
	public boolean añadido=false;
	

	public FrmCanal(Form1 f, int tarjeta, int ndll, int handle)
	{
		super();

		// Requerido para la compatibilidad con el Diseñador de formularios de Visual J++
		initForm();
		// TODO: agregar el código del constructor después de la llamada a initForm.
		
		f1=f;		
		dll=new Dll(ndll);
		
		intTarjeta=tarjeta;
		whandle=handle;
		this.setTopLevel(false);
		trkPitch.setSize(42,125);
		intTrkFinePitchMax=trkFinePitch.getMaximum();
		intTrkFinePitchMin=trkFinePitch.getMinimum();
		inic_tarjeta();
		cmbTarjeta.setSelectedIndex(intTarjeta-1);
	}

	/**
	 * FrmCanal reemplaza el método dispose para poder limpiar 
	 * la lista de componentes.
	 */
	public void dispose()
	{
		super.dispose();
		components.dispose();
	}	
	
	////////////////////////////////////////////////////////////////////////
	///////////////////////////// METODOS //////////////////////////////////
	////////////////////////////////////////////////////////////////////////
	private void interfaz(boolean b)
	{
		btnPlay.setEnabled(b);
		btnStop.setEnabled(b);
		btnResetPitch.setEnabled(b);
		btnPitchUp.setEnabled(b);
		btnPitchDown.setEnabled(b);
		btnBendUp.setEnabled(b);
		btnBendDown.setEnabled(b);
		btnPosAtras.setEnabled(b);
		btnPosDelante.setEnabled(b);
		btnGuardar.setEnabled(b);
		btnBendUp.setEnabled(b);
		btnBendDown.setEnabled(b);
		btnBigBendDown.setEnabled(b);
		btnBigBendUp.setEnabled(b);
		btnInfo.setEnabled(b);
		btnInicio.setEnabled(b);
		btnFin.setEnabled(b);
		trkFinePitch.setEnabled(b);
		trkPitch.setEnabled(b);
		trkVolumen.setEnabled(b);
		trkPos.setEnabled(b);
	}
	
	public int calcFreq(float bpm)
	{
		return Math.round((float)((bpm*44100.0)/InfoC.bpm));
	}
	
	public float calcBpm(int freq)
	{
		return (float)(((float)freq*InfoC.bpm)/44100);
	}
	
	public void setSpeed(int freq, float bpm)
	{
		if (freq!=0)
		{
			edtPitch.setText(""+calcBpm(freq));
		}
		else
		{
			freq=calcFreq(bpm);
			float b=calcBpm(freq);
			edtPitch.setText(""+b);
		}
		
		if ((freq>99) && (freq<100001)) dll.setFreq(puntero,freq);
		else 
		{
			dll.setFreq(puntero,intBasePitch);
			freq=intBasePitch;
		}
		
		if (freq>intTrkFinePitchMax)
			trkFinePitch.setValue(intTrkFinePitchMax);
		else
		{
			if (freq<intTrkFinePitchMin)
				trkFinePitch.setValue(intTrkFinePitchMin);
			else
				trkFinePitch.setValue(freq);
		}
		trkPitch.setValue(freq);		
	}
	
	public void setBalance(int bal)
	{
		dll.setBalance(puntero,bal);
	}
	
	public void setVolumen(int vol)
	{
		if (vol<0) vol=0;
		dll.setVolumen(puntero,vol);
	}
	
	public String getFileName(String path)
	{
		int t=0;
		for (int i=path.length()-1; (i>=0) && (path.charAt(i)!='\\'); i--)
			t=i;
		strPath=path.substring(0,t);
		return path.substring(t,path.length());
	}
	
	public void ordenar()
	{
		int entero1,entero2;
		boolean mod=true;
		while (mod)
		{
			String temp="";
			int tmp=0;
			mod=false;
			for (int i=0; i<19; i++)
			{
				entero1=InfoC.getCue(i);
				if (entero1!=-1)
					for (int k=0; k<i; k++)
					{
						entero2=InfoC.getCue(k);
						if (entero1<entero2)
						{
							InfoC.setCue(entero2,i);
							InfoC.setCue(entero1,k);
							
							tmp=InfoC.getDesfase(i);
							InfoC.setDesfase(i,InfoC.getDesfase(k));
							InfoC.setDesfase(k,tmp);
							
							tmp=InfoC.getCompas(i);
							InfoC.setCompas(i,InfoC.getCompas(k));
							InfoC.setCompas(k,tmp);
							
							tmp=InfoC.getTipo(i);
							InfoC.setTipo(i,InfoC.getTipo(k));
							InfoC.setTipo(k,tmp);
							
							tmp=InfoC.getXpos(i);
							InfoC.setXPos(i,InfoC.getXpos(k));
							InfoC.setXPos(k,tmp);
							
							mod=true;
							break;
						}
					}
			}
		}
	}
	
	public void dibujaPaneles()//hay k optimizar
	{
		if (cargado)
		{
			GphPanel=pnlCue.createGraphics();
			GphPanel.clearRect(0,0,372,20);
			EdtInfo.setText(""+InfoC.bpm);
			for (int i=0,x,x2; i<19; i++)
			{
				x=InfoC.getXpos(i);
				if (x>-1)
				{
					for (int k=i+1; k<19; k++)
					{
						x2=InfoC.getXpos(k);
						if (x2>-1) 
						{
							switch (InfoC.getTipo(i))
							{
							case 0:
								{
									GphPanel.fill(x+1,0,x2-1,20,new Brush(Color.WHITE));
									break;
								}
							case 1:
								{
									GphPanel.fill(x+1,0,x2-1,20,new Brush(Color.CYAN));
									break;
								}
							case 2:
								{
									GphPanel.fill(x+1,0,x2-1,20,new Brush(Color.MAGENTA));
									break;
								}
							}							
							break;
						}
					}
					GphPanel.drawLine(x,0,x,20);
				}
			}
			
			GphPanel=pnlNcue.createGraphics();
			GphPanel.clearRect(0,0,387,20);
			for (int i=0,x; i<19; i++)
			{
				x=InfoC.getXpos(i);
				if (x>-1)
					GphPanel.drawString(""+i, x+2,6);
			}
			
			GphPanel=pnlCompas.createGraphics();
			GphPanel.clearRect(0,0,372,20);
			for (int i=0,x,x2; i<19; i++)
			{
				x=InfoC.getXpos(i);
				if (x>-1)
				{
					for (int q=i+1; q<19; q++)
					{
						x2=InfoC.getXpos(q);
						if (x2>-1)
						{
							x=((x2-x)/2)+x-6;
							GphPanel.drawString(""+InfoC.getCompas(i)+"+"+InfoC.getDesfase(i),x,2);
							break;
						}
					}
				}
			}
		}
	}
	
	private void pnlCue_paint(Object source, PaintEvent e)
	{
		dibujaPaneles();
	}
	
	private void pnlNcue_paint(Object source, PaintEvent e)
	{
		dibujaPaneles();
	}

	private void pnlCompas_paint(Object source, PaintEvent e)
	{
		dibujaPaneles();
	}
	
	private void inic_tarjeta ()
	{
		boolean abierto=dll.iniciar(intTarjeta,44100,32|512,whandle);
		boolean start=dll.start();
	}
	
	public void cerrar()
	{
		dll.cerrar();
	}	
	
	private void getInfo()
	{
		boolean encontrado=false;
		for (int i=0; i<f1.VInfo.size() && encontrado==false; i++)
		{
			if ((((Info)f1.VInfo.elementAt(i)).getNombre()).equals(strFileName))
			{
				encontrado=true;
				añadido=true;
				InfoC=(Info)f1.VInfo.elementAt(i);
			}
		}
		if (encontrado==false)
		{
			InfoC=new Info(strFileName);
			añadido=false;
		}
		intBaseVol=1000-InfoC.volumen;
		intLongitud=dll.getLongitud(puntero);
		long t;
		long pos;
		byte[] tag=new byte[3];		
		try
		{
			raf=new RandomAccessFile(strPathFileName,"r");
			t=raf.length();
			pos=t-128;
			raf.seek(pos);
			raf.read(tag);
			if (tag[0]=='T' && tag[1]=='A' && tag[2]=='G')
			{
				blnTag=true;
				raf.seek(pos);
				raf.read(tagv1);
			}
			else blnTag=false;
			raf.close();
		}
		catch (java.io.IOException ioe){}		
	}
	
	private void inicializar()
	{
		if (cargado) dll.free(puntero);
		else
		{
			interfaz(true);
			cargado=true;
		}
		puntero=dll.abrir(false,strPathFileName,0,0,0);//0x20000);
		getInfo();
		btnPlay.setText(">");
		trkPos.setTickFrequency(intLongitud/1000);
		trkPos.setMaximum(intLongitud/100);			
		lblEstado.setText(strFileName);
		setVolumen(intBaseVol);
		trkVolumen.setValue(1000-intBaseVol);			
		fltBaseBpm=(new Float(f1.lblBpm.getText())).floatValue();			
		if (InfoC.bpm>0) intBasePitch=calcFreq(fltBaseBpm);
		else intBasePitch=44100;
		setSpeed(intBasePitch,0);
		int p=InfoC.getCue(0);
		if (p!=-1)
		{
			dll.setPosActual(0,p);
			trkPos.setValue(p/100);
		}
		else 
		{
			dll.setPosActual(0,0);
			trkPos.setValue(0);
		}
		dll.preBuff(puntero);
	}
	
	////////////////////////////////////////////////////////////////////////
	//////////////////////////////BOTONES///////////////////////////////////
	////////////////////////////////////////////////////////////////////////
	
	private void btnAbrir_click(Object source, Event e)
	{
		OfdAbrir.setInitialDir(strPath);
		OfdAbrir.showDialog();		
		if (OfdAbrir.getFileName().compareTo(strPathFileName)!=0)
		{
			strPathFileName=OfdAbrir.getFileName();
			strFileName=getFileName(strPathFileName);			
			inicializar();
			dibujaPaneles();
		}
	}
	
	private void btnPlay_click(Object source, Event e)
	{
		switch (dll.estado(puntero))
		{
		case 0:
			{
				dll.reproducir(puntero,false,0);
				tmrTrkPos.setEnabled(true);
				btnPlay.setText("[ ]");
				break;
			}
		case 1:
			{
				dll.pausa(puntero);
				tmrTrkPos.setEnabled(false);
				btnPlay.setText(">");
				break;
			}
		case 3:
			{
				dll.preBuff(puntero);
				dll.resumir(puntero);
				tmrTrkPos.setEnabled(true);
				btnPlay.setText("[ ]");
				break;
			}
		}
	}

	private void btnStop_click(Object source, Event e)
	{
		tmrTrkPos.setEnabled(false);
		dll.stop(puntero);
		int p=InfoC.getCue(0);
		if (p!=-1)
		{
			dll.setPosActual(puntero,p);
			trkPos.setValue(p/100);
		}
		else 
		{
			dll.setPosActual(puntero,0);
			trkPos.setValue(0);
		}
		btnPlay.setText(">");
		dll.preBuff(puntero);
	}
	
	private void btnResetPitch_click(Object source, Event e)
	{
		intBasePitch=44100;
		fltBaseBpm=InfoC.bpm;
		setSpeed(intBasePitch,0);
	}

	private void btnBendDown_mouseDown(Object source, MouseEvent e)
	{
		setSpeed(intBasePitch-1000,0);
	}

	private void btnBendDown_mouseUp(Object source, MouseEvent e)
	{
		setSpeed(intBasePitch,0);
	}

	private void btnBendUp_mouseDown(Object source, MouseEvent e)
	{
		setSpeed(intBasePitch+1000,0);
	}

	private void btnPitchDown_click(Object source, Event e)
	{
		if (intBasePitch>104)
		{
			intBasePitch-=5;
			fltBaseBpm=calcBpm(intBasePitch);
			setSpeed(intBasePitch,0);
		}
	}

	private void btnPitchUp_click(Object source, Event e)
	{
		if (intBasePitch<99996)
		{
			intBasePitch+=5;
			fltBaseBpm=calcBpm(intBasePitch);
			setSpeed(intBasePitch,0);
		}		
	}
	
	private void btnBigBendDown_mouseDown(Object source, MouseEvent e)
	{
		setSpeed(intBasePitch-4000,0);
	}
	
	private void btnBigBendUp_mouseDown(Object source, MouseEvent e)
	{
		setSpeed(intBasePitch+4000,0);
	}
	
	private void btnCue0_click(Object source, Event e)
	{
		if (cargado)
		{
			int ncue=(new Integer(((Button)source).getText())).intValue();
			switch (intSaveCue)
			{
			case 0:
				{
					int p=InfoC.getCue(ncue);
					if (p>-1)
					{
						dll.setPosActual(puntero,p);
						trkPos.setValue(p/100);					
					}
					break;
				}
			case 1:
				{
					int p=dll.getPosActual(puntero);
					InfoC.setCue(p,ncue);
					int x=Math.round((float)(p*371f)/(float)intLongitud);
					InfoC.setXPos(ncue,x);
					ordenar();
					dibujaPaneles();
					break;
				}
			case 2:
				{
					InfoC.setCue(-1,ncue);
					InfoC.setXPos(ncue,-1);
					InfoC.setTipo(ncue,0);
					InfoC.setCompas(ncue,0);
					InfoC.setDesfase(ncue,0);
					ordenar();
					dibujaPaneles();
				}
			}
		}
	}

	private void btnPosAtras_click(Object source, Event e)
	{
		int pos=dll.getPosActual(puntero);
		if (pos>500)
		{
			dll.setPosActual(puntero,pos-500);
			trkPos.setValue((pos-500)/100);
		}
		else 
		{
			dll.setPosActual(puntero,500);
			trkPos.setValue(500);
		}
	}

	private void btnPosDelante_click(Object source, Event e)
	{
		int pos=dll.getPosActual(puntero);
		if (pos<intLongitud-500)
		{
			dll.setPosActual(puntero,pos+500);
			trkPos.setValue((pos+500)/100);
		}
		else 
		{
			dll.setPosActual(puntero,intLongitud-500);
			trkPos.setValue((intLongitud-500)/100);
		}
	}
	
	private void btnInicio_click(Object source, Event e)
	{
		dll.setPosActual(puntero,0);
		trkPos.setValue(0);
	}

	private void btnFin_click(Object source, Event e)
	{
		dll.setPosActual(puntero,intLongitud-1);
		trkPos.setValue((intLongitud-1)/100);
	}
	
	private void btnEditar_click(Object source, Event e)
	{
		fi=new FrmInfo(this);
		f1.addOwnedForm(fi);
		fi.show();	
	}
	
	private void btnGuardar_click(Object source, Event e)
	{
		if (!añadido) 
		{
			f1.VInfo.addElement(InfoC);
			añadido=true;
		}
		f1.guardar();
	}
	
		
	////////////////////////////////////////////////////////////////////////
	////////////////////// BARRAS DESPLAZAMIENTO ///////////////////////////
	////////////////////////////////////////////////////////////////////////
	
	private void trkVolumen_scroll(Object source, Event e)
	{
		intBaseVol=1000-trkVolumen.getValue();
		setVolumen(intBaseVol);
	}
	
	private void trkVolumen_mouseUp(Object source, MouseEvent e)
	{
		InfoC.volumen=trkVolumen.getValue();
	}

	private void trkPitch_scroll(Object source, Event e)
	{
		setSpeed(trkPitch.getValue(),0);
	}	

	private void trkPitch_mouseUp(Object source, MouseEvent e)
	{
		setSpeed(intBasePitch,0);
		trkPitch.setValue(intBasePitch);
	}

	private void trkFinePitch_scroll(Object source, Event e)
	{
		intBasePitch=trkFinePitch.getValue();
		setSpeed(intBasePitch,0);
		trkPitch.setValue(intBasePitch);
	}
	
	private void trkPos_scroll(Object source, Event e)
	{
		tmrTrkPos.setEnabled(false);
	}

	private void trkPos_mouseUp(Object source, MouseEvent e)
	{
		dll.setPosActual(puntero,trkPos.getValue()*100);
		tmrTrkPos.setEnabled(true);
	}
	
	
	///////////////////////////TIMER//////////////////////////////////////
	private void tmrTrkPos_timer(Object source, Event e)
	{
		trkPos.setValue(dll.getPosActual(puntero)/100);
	}
	
	
	////////////////////////////TECLAS/////////////////////////////////////
	private void FrmCanal_keyDown(Object source, KeyEvent e)
	{
		if (e.getKeyCode()==Key.CONTROL_KEY)
			intSaveCue=1;
		else
			if (e.getKeyCode()==Key.SHIFT_KEY)
				intSaveCue=2;
	}

	private void FrmCanal_keyUp(Object source, KeyEvent e)
	{
		intSaveCue=0;
	}


	///////////////////////////////PANEL///////////////////////////////////
	private void pnlCue_mouseMove(Object source, MouseEvent e)
	{
		intPanelPos=e.x;
	}
	
	private void pnlCue_mouseDown(Object source, MouseEvent e)
	{
		int pos=Math.round(intLongitud*((float)intPanelPos/371.0f));
		dll.setPosActual(puntero,pos);
		trkPos.setValue(pos/100);
	}
	
	//////////////////////////////COMBO BOX ///////////////////////////////
	private void cmbTarjeta_selectedIndexChanged(Object source, Event e)
	{
		if (intTarjeta!=cmbTarjeta.getSelectedIndex()+1)
		{
			int pos=0;
			int estado=0;
			if (cargado)
			{
				pos=dll.getPosActual(puntero);
				estado=dll.estado(puntero);
				dll.cerrar();
				intTarjeta=cmbTarjeta.getSelectedIndex()+1;
				inic_tarjeta();
				puntero=dll.abrir(false,strPathFileName,0,0,0);
				switch (estado)
				{
				case 1:
					{
						dll.setPosActual(puntero,pos+27000);
						setSpeed(intBasePitch,0);
						setVolumen(intBaseVol);
						dll.reproducir(puntero,false,0);
						break;
					}
				case 2:
					{
						setSpeed(intBasePitch,0);
						setVolumen(intBaseVol);
						dll.reproducir(puntero,false,0);
						dll.pausa(puntero);
						dll.setPosActual(puntero,pos);
						break;
					}
				}
			}
			else
			{
				intTarjeta=cmbTarjeta.getSelectedIndex()+1;
				inic_tarjeta();
			}
		}
	}

	/**
	 * NOTA: el código siguiente es necesario para el diseñador de
	 * formularios de Visual J++ y puede modificarse con el editor de formularios.
	 * No lo modifique desde el editor de código.
	 */
	Container components = new Container();
	Edit lblEstado = new Edit();
	Button btnPlay = new Button();
	Button btnStop = new Button();
	TrackBar trkPitch = new TrackBar();
	Button btnResetPitch = new Button();
	TrackBar trkPos = new TrackBar();
	TrackBar trkVolumen = new TrackBar();
	TrackBar trkFinePitch = new TrackBar();
	Button btnPitchDown = new Button();
	Button btnPitchUp = new Button();
	Panel pnlCue = new Panel();
	Button btnAbrir = new Button();
	Button btnBendDown = new Button();
	Button btnBendUp = new Button();
	OpenFileDialog OfdAbrir = new OpenFileDialog();
	Timer tmrTrkPos = new Timer(components);
	Edit EdtInfo = new Edit();
	Button btnCue0 = new Button();
	Button btnCue1 = new Button();
	Button btnCue2 = new Button();
	Button btnCue3 = new Button();
	Button btnCue4 = new Button();
	Button btnCue5 = new Button();
	Button btnCue6 = new Button();
	Button btnCue7 = new Button();
	Button btnCue8 = new Button();
	Button btnCue9 = new Button();
	Button btnCue10 = new Button();
	Button btnCue11 = new Button();
	Button btnCue12 = new Button();
	Button btnCue13 = new Button();
	Button btnCue14 = new Button();
	Button btnCue15 = new Button();
	Button btnCue16 = new Button();
	Button btnCue17 = new Button();
	Button btnCue18 = new Button();
	Button btnBigBendDown = new Button();
	Button btnBigBendUp = new Button();
	Button btnPosAtras = new Button();
	Button btnPosDelante = new Button();
	Button btnGuardar = new Button();
	Button btnInfo = new Button();
	public Edit edtPitch = new Edit();
	Panel pnlNcue = new Panel();
	Panel pnlCompas = new Panel();
	Button btnInicio = new Button();
	Button btnFin = new Button();
	ComboBox cmbTarjeta = new ComboBox();

	private void initForm()
	{
		this.setBackColor(Color.ACTIVEBORDER);
		this.setText("FrmCanal");
		this.setAutoScaleBaseSize(new Point(5, 13));
		this.setBorderStyle(FormBorderStyle.NONE);
		this.setClientSize(new Point(400, 388));
		this.setControlBox(false);
		this.setKeyPreview(true);
		this.setMaximizeBox(false);
		this.setMinimizeBox(false);
		this.setShowInTaskbar(false);
		this.setStartPosition(FormStartPosition.MANUAL);
		this.addOnKeyDown(new KeyEventHandler(this.FrmCanal_keyDown));
		this.addOnKeyUp(new KeyEventHandler(this.FrmCanal_keyUp));
		this.addOnPaint(new PaintEventHandler(this.pnlCue_paint));

		lblEstado.setBackColor(Color.INACTIVEBORDER);
		lblEstado.setLocation(new Point(8, 0));
		lblEstado.setSize(new Point(212, 20));
		lblEstado.setTabIndex(44);
		lblEstado.setText("");
		lblEstado.setReadOnly(true);

		btnPlay.setEnabled(false);
		btnPlay.setFont(new Font("MS Sans Serif", 10.0f, FontSize.POINTS, FontWeight.BOLD, false, false, false, CharacterSet.DEFAULT, 0));
		btnPlay.setLocation(new Point(166, 334));
		btnPlay.setSize(new Point(35, 25));
		btnPlay.setTabIndex(0);
		btnPlay.setTabStop(false);
		btnPlay.setText(">");
		btnPlay.addOnClick(new EventHandler(this.btnPlay_click));

		btnStop.setEnabled(false);
		btnStop.setFont(new Font("MS Sans Serif", 8.0f, FontSize.POINTS, FontWeight.NORMAL, false, false, false, CharacterSet.DEFAULT, 0));
		btnStop.setLocation(new Point(210, 334));
		btnStop.setSize(new Point(45, 25));
		btnStop.setTabIndex(1);
		btnStop.setTabStop(false);
		btnStop.setText("Parar");
		btnStop.addOnClick(new EventHandler(this.btnStop_click));

		trkPitch.setBackColor(Color.ACTIVEBORDER);
		trkPitch.setEnabled(false);
		trkPitch.setLocation(new Point(226, 32));
		trkPitch.setSize(new Point(42, 42));
		trkPitch.setTabIndex(2);
		trkPitch.setTabStop(false);
		trkPitch.setText("trackBar1");
		trkPitch.setLargeChange(0);
		trkPitch.setMinimum(100);
		trkPitch.setMaximum(100000);
		trkPitch.setOrientation(Orientation.VERTICAL);
		trkPitch.setValue(44100);
		trkPitch.setSmallChange(0);
		trkPitch.setTickStyle(TickStyle.BOTH);
		trkPitch.setTickFrequency(5000);
		trkPitch.addOnMouseUp(new MouseEventHandler(this.trkPitch_mouseUp));
		trkPitch.addOnScroll(new EventHandler(this.trkPitch_scroll));

		btnResetPitch.setEnabled(false);
		btnResetPitch.setLocation(new Point(346, 42));
		btnResetPitch.setSize(new Point(40, 15));
		btnResetPitch.setTabIndex(3);
		btnResetPitch.setTabStop(false);
		btnResetPitch.setText("Reset");
		btnResetPitch.addOnClick(new EventHandler(this.btnResetPitch_click));

		trkPos.setEnabled(false);
		trkPos.setLocation(new Point(2, 238));
		trkPos.setSize(new Point(400, 42));
		trkPos.setTabIndex(4);
		trkPos.setTabStop(false);
		trkPos.setText("trackBar1");
		trkPos.setLargeChange(0);
		trkPos.setMaximum(0);
		trkPos.setSmallChange(0);
		trkPos.setTickStyle(TickStyle.BOTH);
		trkPos.setTickFrequency(10000);
		trkPos.addOnMouseUp(new MouseEventHandler(this.trkPos_mouseUp));
		trkPos.addOnScroll(new EventHandler(this.trkPos_scroll));

		trkVolumen.setEnabled(false);
		trkVolumen.setLocation(new Point(2, 32));
		trkVolumen.setSize(new Point(42, 130));
		trkVolumen.setTabIndex(7);
		trkVolumen.setTabStop(false);
		trkVolumen.setText("trackBar1");
		trkVolumen.setLargeChange(0);
		trkVolumen.setMaximum(1000);
		trkVolumen.setOrientation(Orientation.VERTICAL);
		trkVolumen.setSmallChange(0);
		trkVolumen.setTickStyle(TickStyle.BOTH);
		trkVolumen.setTickFrequency(1000);
		trkVolumen.addOnMouseUp(new MouseEventHandler(this.trkVolumen_mouseUp));
		trkVolumen.addOnScroll(new EventHandler(this.trkVolumen_scroll));

		trkFinePitch.setEnabled(false);
		trkFinePitch.setLocation(new Point(294, 32));
		trkFinePitch.setSize(new Point(42, 125));
		trkFinePitch.setTabIndex(6);
		trkFinePitch.setTabStop(false);
		trkFinePitch.setText("trackBar1");
		trkFinePitch.setLargeChange(0);
		trkFinePitch.setMinimum(38000);
		trkFinePitch.setMaximum(55000);
		trkFinePitch.setOrientation(Orientation.VERTICAL);
		trkFinePitch.setValue(44100);
		trkFinePitch.setSmallChange(0);
		trkFinePitch.setTickStyle(TickStyle.BOTH);
		trkFinePitch.setTickFrequency(1000);
		trkFinePitch.addOnScroll(new EventHandler(this.trkFinePitch_scroll));

		btnPitchDown.setEnabled(false);
		btnPitchDown.setLocation(new Point(372, 68));
		btnPitchDown.setSize(new Point(20, 30));
		btnPitchDown.setTabIndex(8);
		btnPitchDown.setTabStop(false);
		btnPitchDown.setText("P-");
		btnPitchDown.addOnClick(new EventHandler(this.btnPitchDown_click));

		btnPitchUp.setEnabled(false);
		btnPitchUp.setLocation(new Point(372, 104));
		btnPitchUp.setSize(new Point(20, 30));
		btnPitchUp.setTabIndex(9);
		btnPitchUp.setTabStop(false);
		btnPitchUp.setText("P+");
		btnPitchUp.addOnClick(new EventHandler(this.btnPitchUp_click));

		pnlCue.setBackColor(Color.ACTIVECAPTIONTEXT);
		pnlCue.setLocation(new Point(14, 194));
		pnlCue.setSize(new Point(376, 20));
		pnlCue.setTabIndex(16);
		pnlCue.setText("panel1");
		pnlCue.setBorderStyle(BorderStyle.FIXED_3D);
		pnlCue.addOnMouseDown(new MouseEventHandler(this.pnlCue_mouseDown));
		pnlCue.addOnMouseMove(new MouseEventHandler(this.pnlCue_mouseMove));
		pnlCue.addOnPaint(new PaintEventHandler(this.pnlCue_paint));

		btnAbrir.setFont(new Font("MS Sans Serif", 8.0f, FontSize.POINTS, FontWeight.NORMAL, false, false, false, CharacterSet.DEFAULT, 0));
		btnAbrir.setLocation(new Point(116, 334));
		btnAbrir.setSize(new Point(40, 25));
		btnAbrir.setTabIndex(5);
		btnAbrir.setText("Abrir");
		btnAbrir.addOnClick(new EventHandler(this.btnAbrir_click));

		btnBendDown.setEnabled(false);
		btnBendDown.setLocation(new Point(342, 68));
		btnBendDown.setSize(new Point(20, 30));
		btnBendDown.setTabIndex(12);
		btnBendDown.setTabStop(false);
		btnBendDown.setText("B-");
		btnBendDown.addOnMouseDown(new MouseEventHandler(this.btnBendDown_mouseDown));
		btnBendDown.addOnMouseUp(new MouseEventHandler(this.btnBendDown_mouseUp));

		btnBendUp.setEnabled(false);
		btnBendUp.setLocation(new Point(342, 104));
		btnBendUp.setSize(new Point(20, 30));
		btnBendUp.setTabIndex(13);
		btnBendUp.setTabStop(false);
		btnBendUp.setText("B+");
		btnBendUp.addOnMouseDown(new MouseEventHandler(this.btnBendUp_mouseDown));
		btnBendUp.addOnMouseUp(new MouseEventHandler(this.btnBendDown_mouseUp));

		OfdAbrir.setFilter("Sonidos soportados|*.wav;*.mp3;*.ogg|Sonido Wav|*.wav|Sonido Mp3|*.mp3|Sonido Ogg|*.ogg");
		OfdAbrir.setInitialDir("d:\\");
		OfdAbrir.setRestoreDirectory(true);
		OfdAbrir.setTitle("Abrir cancion");
		/* @designTimeOnly OfdAbrir.setLocation(new Point(6, 346)); */

		tmrTrkPos.setInterval(300);
		tmrTrkPos.addOnTimer(new EventHandler(this.tmrTrkPos_timer));
		/* @designTimeOnly tmrTrkPos.setLocation(new Point(2, 366)); */

		EdtInfo.setBackColor(Color.BLACK);
		EdtInfo.setFont(new Font("MS Sans Serif", 8.0f, FontSize.POINTS, FontWeight.NORMAL, false, false, false, CharacterSet.DEFAULT, 0));
		EdtInfo.setForeColor(Color.WHITE);
		EdtInfo.setLocation(new Point(46, 32));
		EdtInfo.setSize(new Point(175, 120));
		EdtInfo.setTabIndex(43);
		EdtInfo.setText("");
		EdtInfo.setMultiline(true);
		EdtInfo.setReadOnly(true);

		btnCue0.setLocation(new Point(14, 288));
		btnCue0.setSize(new Point(15, 15));
		btnCue0.setTabIndex(35);
		btnCue0.setTabStop(false);
		btnCue0.setText("0");
		btnCue0.addOnClick(new EventHandler(this.btnCue0_click));

		btnCue1.setLocation(new Point(34, 288));
		btnCue1.setSize(new Point(15, 15));
		btnCue1.setTabIndex(33);
		btnCue1.setTabStop(false);
		btnCue1.setText("1");
		btnCue1.addOnClick(new EventHandler(this.btnCue0_click));

		btnCue2.setLocation(new Point(54, 288));
		btnCue2.setSize(new Point(15, 15));
		btnCue2.setTabIndex(30);
		btnCue2.setTabStop(false);
		btnCue2.setText("2");
		btnCue2.addOnClick(new EventHandler(this.btnCue0_click));

		btnCue3.setLocation(new Point(74, 288));
		btnCue3.setSize(new Point(15, 15));
		btnCue3.setTabIndex(34);
		btnCue3.setTabStop(false);
		btnCue3.setText("3");
		btnCue3.addOnClick(new EventHandler(this.btnCue0_click));

		btnCue4.setLocation(new Point(94, 288));
		btnCue4.setSize(new Point(15, 15));
		btnCue4.setTabIndex(32);
		btnCue4.setTabStop(false);
		btnCue4.setText("4");
		btnCue4.addOnClick(new EventHandler(this.btnCue0_click));

		btnCue5.setLocation(new Point(114, 288));
		btnCue5.setSize(new Point(15, 15));
		btnCue5.setTabIndex(27);
		btnCue5.setTabStop(false);
		btnCue5.setText("5");
		btnCue5.addOnClick(new EventHandler(this.btnCue0_click));

		btnCue6.setLocation(new Point(134, 288));
		btnCue6.setSize(new Point(15, 15));
		btnCue6.setTabIndex(31);
		btnCue6.setTabStop(false);
		btnCue6.setText("6");
		btnCue6.addOnClick(new EventHandler(this.btnCue0_click));

		btnCue7.setLocation(new Point(154, 288));
		btnCue7.setSize(new Point(15, 15));
		btnCue7.setTabIndex(29);
		btnCue7.setTabStop(false);
		btnCue7.setText("7");
		btnCue7.addOnClick(new EventHandler(this.btnCue0_click));

		btnCue8.setLocation(new Point(174, 288));
		btnCue8.setSize(new Point(15, 15));
		btnCue8.setTabIndex(24);
		btnCue8.setTabStop(false);
		btnCue8.setText("8");
		btnCue8.addOnClick(new EventHandler(this.btnCue0_click));

		btnCue9.setLocation(new Point(194, 288));
		btnCue9.setSize(new Point(15, 15));
		btnCue9.setTabIndex(28);
		btnCue9.setTabStop(false);
		btnCue9.setText("9");
		btnCue9.addOnClick(new EventHandler(this.btnCue0_click));

		btnCue10.setLocation(new Point(214, 288));
		btnCue10.setSize(new Point(15, 15));
		btnCue10.setTabIndex(26);
		btnCue10.setTabStop(false);
		btnCue10.setText("10");
		btnCue10.addOnClick(new EventHandler(this.btnCue0_click));

		btnCue11.setLocation(new Point(234, 288));
		btnCue11.setSize(new Point(15, 15));
		btnCue11.setTabIndex(21);
		btnCue11.setTabStop(false);
		btnCue11.setText("11");
		btnCue11.addOnClick(new EventHandler(this.btnCue0_click));

		btnCue12.setLocation(new Point(254, 288));
		btnCue12.setSize(new Point(15, 15));
		btnCue12.setTabIndex(25);
		btnCue12.setTabStop(false);
		btnCue12.setText("12");
		btnCue12.addOnClick(new EventHandler(this.btnCue0_click));

		btnCue13.setLocation(new Point(274, 288));
		btnCue13.setSize(new Point(15, 15));
		btnCue13.setTabIndex(23);
		btnCue13.setTabStop(false);
		btnCue13.setText("13");
		btnCue13.addOnClick(new EventHandler(this.btnCue0_click));

		btnCue14.setLocation(new Point(294, 288));
		btnCue14.setSize(new Point(15, 15));
		btnCue14.setTabIndex(19);
		btnCue14.setTabStop(false);
		btnCue14.setText("14");
		btnCue14.addOnClick(new EventHandler(this.btnCue0_click));

		btnCue15.setLocation(new Point(314, 288));
		btnCue15.setSize(new Point(15, 15));
		btnCue15.setTabIndex(22);
		btnCue15.setTabStop(false);
		btnCue15.setText("15");
		btnCue15.addOnClick(new EventHandler(this.btnCue0_click));

		btnCue16.setLocation(new Point(334, 288));
		btnCue16.setSize(new Point(15, 15));
		btnCue16.setTabIndex(20);
		btnCue16.setTabStop(false);
		btnCue16.setText("16");
		btnCue16.addOnClick(new EventHandler(this.btnCue0_click));

		btnCue17.setLocation(new Point(354, 288));
		btnCue17.setSize(new Point(15, 15));
		btnCue17.setTabIndex(18);
		btnCue17.setTabStop(false);
		btnCue17.setText("17");
		btnCue17.addOnClick(new EventHandler(this.btnCue0_click));

		btnCue18.setLocation(new Point(374, 288));
		btnCue18.setSize(new Point(15, 15));
		btnCue18.setTabIndex(17);
		btnCue18.setTabStop(false);
		btnCue18.setText("18");
		btnCue18.addOnClick(new EventHandler(this.btnCue0_click));

		btnBigBendDown.setEnabled(false);
		btnBigBendDown.setFont(new Font("MS Sans Serif", 11.0f, FontSize.POINTS, FontWeight.NORMAL, false, false, false, CharacterSet.DEFAULT, 0));
		btnBigBendDown.setLocation(new Point(270, 68));
		btnBigBendDown.setSize(new Point(20, 30));
		btnBigBendDown.setTabIndex(10);
		btnBigBendDown.setTabStop(false);
		btnBigBendDown.setText("^");
		btnBigBendDown.addOnMouseDown(new MouseEventHandler(this.btnBigBendDown_mouseDown));
		btnBigBendDown.addOnMouseUp(new MouseEventHandler(this.btnBendDown_mouseUp));

		btnBigBendUp.setEnabled(false);
		btnBigBendUp.setLocation(new Point(270, 104));
		btnBigBendUp.setSize(new Point(20, 30));
		btnBigBendUp.setTabIndex(11);
		btnBigBendUp.setTabStop(false);
		btnBigBendUp.setText("v");
		btnBigBendUp.addOnMouseDown(new MouseEventHandler(this.btnBigBendUp_mouseDown));
		btnBigBendUp.addOnMouseUp(new MouseEventHandler(this.btnBendDown_mouseUp));

		btnPosAtras.setEnabled(false);
		btnPosAtras.setLocation(new Point(148, 312));
		btnPosAtras.setSize(new Point(32, 14));
		btnPosAtras.setTabIndex(41);
		btnPosAtras.setTabStop(false);
		btnPosAtras.setText("<--");
		btnPosAtras.addOnClick(new EventHandler(this.btnPosAtras_click));

		btnPosDelante.setEnabled(false);
		btnPosDelante.setLocation(new Point(188, 312));
		btnPosDelante.setSize(new Point(32, 14));
		btnPosDelante.setTabIndex(36);
		btnPosDelante.setTabStop(false);
		btnPosDelante.setText("-->");
		btnPosDelante.addOnClick(new EventHandler(this.btnPosDelante_click));

		btnGuardar.setEnabled(false);
		btnGuardar.setLocation(new Point(176, 154));
		btnGuardar.setSize(new Point(46, 16));
		btnGuardar.setTabIndex(38);
		btnGuardar.setText("Guardar");
		btnGuardar.addOnClick(new EventHandler(this.btnGuardar_click));

		btnInfo.setEnabled(false);
		btnInfo.setLocation(new Point(46, 154));
		btnInfo.setSize(new Point(46, 16));
		btnInfo.setTabIndex(37);
		btnInfo.setText("Info");
		btnInfo.addOnClick(new EventHandler(this.btnEditar_click));

		edtPitch.setBackColor(Color.INACTIVEBORDER);
		edtPitch.setLocation(new Point(340, 138));
		edtPitch.setSize(new Point(54, 20));
		edtPitch.setTabIndex(42);
		edtPitch.setText("");
		edtPitch.setMaxLength(8);
		edtPitch.setReadOnly(true);

		pnlNcue.setBackColor(Color.ACTIVEBORDER);
		pnlNcue.setLocation(new Point(10, 174));
		pnlNcue.setSize(new Point(386, 20));
		pnlNcue.setTabIndex(15);
		pnlNcue.setText("panel1");
		pnlNcue.addOnPaint(new PaintEventHandler(this.pnlNcue_paint));

		pnlCompas.setBackColor(Color.ACTIVEBORDER);
		pnlCompas.setLocation(new Point(14, 214));
		pnlCompas.setSize(new Point(376, 20));
		pnlCompas.setTabIndex(14);
		pnlCompas.setText("panel1");
		pnlCompas.addOnPaint(new PaintEventHandler(this.pnlCompas_paint));

		btnInicio.setEnabled(false);
		btnInicio.setLocation(new Point(94, 312));
		btnInicio.setSize(new Point(32, 14));
		btnInicio.setTabIndex(40);
		btnInicio.setTabStop(false);
		btnInicio.setText("Inicio");
		btnInicio.addOnClick(new EventHandler(this.btnInicio_click));

		btnFin.setEnabled(false);
		btnFin.setLocation(new Point(244, 312));
		btnFin.setSize(new Point(32, 14));
		btnFin.setTabIndex(39);
		btnFin.setTabStop(false);
		btnFin.setText("Fin");
		btnFin.addOnClick(new EventHandler(this.btnFin_click));

		cmbTarjeta.setLocation(new Point(228, 0));
		cmbTarjeta.setSize(new Point(166, 21));
		cmbTarjeta.setTabIndex(47);
		cmbTarjeta.setText("");
		cmbTarjeta.setMaxDropDownItems(3);
		cmbTarjeta.setItems(new Object[] {
							"Tarjeta 1", 
							"Tarjeta 2", 
							"Tarjeta 3"});
		cmbTarjeta.addOnSelectedIndexChanged(new EventHandler(this.cmbTarjeta_selectedIndexChanged));

		this.setNewControls(new Control[] {
							cmbTarjeta, 
							lblEstado, 
							btnFin, 
							btnInicio, 
							pnlCompas, 
							pnlNcue, 
							edtPitch, 
							btnInfo, 
							EdtInfo, 
							btnGuardar, 
							btnPosDelante, 
							btnPosAtras, 
							btnBigBendUp, 
							btnBigBendDown, 
							btnCue18, 
							btnCue17, 
							btnCue16, 
							btnCue15, 
							btnCue14, 
							btnCue13, 
							btnCue12, 
							btnCue11, 
							btnCue10, 
							btnCue9, 
							btnCue8, 
							btnCue7, 
							btnCue6, 
							btnCue5, 
							btnCue4, 
							btnCue3, 
							btnCue2, 
							btnCue1, 
							btnCue0, 
							pnlCue, 
							btnPitchUp, 
							btnPitchDown, 
							trkFinePitch, 
							btnBendUp, 
							btnBendDown, 
							btnAbrir, 
							trkVolumen, 
							trkPos, 
							btnResetPitch, 
							trkPitch, 
							btnStop, 
							btnPlay});
	}

}

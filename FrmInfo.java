import com.ms.wfc.app.*;
import com.ms.wfc.core.*;
import com.ms.wfc.ui.*;
import com.ms.wfc.html.*;

/**
 * Esta clase puede tomar un número variable de parámetros 
 * en la línea de comandos. La ejecución del programa comienza 
 * con el método main(). La llamada al constructor de clase no tiene lugar a menos que se cree un objeto del tipo 'FrmInfo'
 * en el método main().
 */
public class FrmInfo extends Form
{
	Label[] lbl=new Label[19];
	Edit[] edtCompas=new Edit[19];
	Edit[] edtDesfase=new Edit[19];
	ComboBox[] cboxTipo=new ComboBox[19];	
	FrmCanal fc;
	BPM hilo;
	
	public FrmInfo (FrmCanal frc)
	{
		super();

		// Requerido para la compatibilidad con el Diseñador de formularios de Visual J++
		initForm();
		// TODO: agregar el código del constructor después de la llamada a initForm.
		
		fc=frc;
		this.setOwner(fc.f1);
		
		lblNombre.setText(fc.strPathFileName);
		this.setText(fc.strFileName);		
		edtBpm.setText(""+fc.InfoC.bpm);
		
		for (int i=0; i<19; i++)
		{
			lbl[i]=new Label();
			lbl[i].setLeft(1+((i/7)*130));
			lbl[i].setTop(((i%7)*22)+30);
			lbl[i].setHeight(20);
			lbl[i].setWidth(12);
			lbl[i].setText(""+i);
			lbl[i].setTextAlign(1);
			this.tabCue.add(lbl[i]);
			
			edtCompas[i]=new Edit();
			edtCompas[i].setLeft(20+((i/7)*130));
			edtCompas[i].setTop(((i%7)*22)+26);
			edtCompas[i].setMaxLength(2);
			edtCompas[i].setWidth(20);
			this.tabCue.add(edtCompas[i]);			
		
			edtDesfase[i]=new Edit();
			edtDesfase[i].setLeft(43+((i/7)*130));
			edtDesfase[i].setTop(((i%7)*22)+26);
			edtDesfase[i].setMaxLength(2);
			edtDesfase[i].setWidth(20);
			this.tabCue.add(edtDesfase[i]);
			
			cboxTipo[i]=new ComboBox();
			cboxTipo[i].setLeft(66+((i/7)*130));
			cboxTipo[i].setTop(((i%7)*22)+26);
			cboxTipo[i].setWidth(60);
			cboxTipo[i].setItemHeight(13);
			cboxTipo[i].setStyle(2);
			cboxTipo[i].setItems(new Object[]{"","Sube","Baja"});
			this.tabCue.add(cboxTipo[i]);			
			if (fc.InfoC.getCue(i)==-1)
			{
				cboxTipo[i].setEnabled(false);
				edtDesfase[i].setEnabled(false);
				edtDesfase[i].setBackColor(Color.BLACK);
				edtCompas[i].setEnabled(false);
				edtCompas[i].setBackColor(Color.BLACK);
			}			
			if (i==18)
			{
				cboxTipo[i].setEnabled(false);
				edtDesfase[i].setReadOnly(true);
				edtCompas[i].setReadOnly(true);
			}
		}		
		if (!edtCompas[0].getEnabled() || !edtCompas[18].getEnabled())
			btnCalcBpm.setEnabled(false);
		
		generar();		
	}

	/**
	 * FrmInfo reemplaza el método dispose para poder limpiar 
	 * la lista de componentes.
	 */
	public void dispose()
	{
		super.dispose();
		components.dispose();
	}

	private void btnBorrar_click(Object source, Event e)
	{
		for (int i=0; i<19; i++)
		{
			edtCompas[i].setText("0");
			edtDesfase[i].setText("0");
			cboxTipo[i].setSelectedIndex(0);
		}
	}
	
	private void editarTag(boolean b)
	{
		edtArtista.setEnabled(b);
		if (!b) edtArtista.setBackColor(Color.INACTIVEBORDER);
		else edtArtista.setBackColor(Color.WHITE);
		edtAlbum.setEnabled(b);
		if (!b) edtAlbum.setBackColor(Color.INACTIVEBORDER);
		else edtAlbum.setBackColor(Color.WHITE);
		edtTitulo.setEnabled(b);
		if (!b) edtTitulo.setBackColor(Color.INACTIVEBORDER);
		else edtTitulo.setBackColor(Color.WHITE);
		edtAño.setEnabled(b);
		if (!b) edtAño.setBackColor(Color.INACTIVEBORDER);
		else edtAño.setBackColor(Color.WHITE);
		edtComentario.setEnabled(b);
		if (!b) edtComentario.setBackColor(Color.INACTIVEBORDER);
		else edtComentario.setBackColor(Color.WHITE);
		cboxGenero.setEnabled(b);
		if (!b) cboxGenero.setBackColor(Color.INACTIVEBORDER);
		else cboxGenero.setBackColor(Color.WHITE);
	}
	
	private void generar()
	{
		for (int i=0; i<19; i++)
		{
			edtCompas[i].setText(""+fc.InfoC.getCompas(i));
			edtDesfase[i].setText(""+fc.InfoC.getDesfase(i));
			switch (fc.InfoC.getTipo(i))
			{
			case 0:
				{
					cboxTipo[i].setSelectedIndex(0);
					break;
				}
			
			case 1:
				{
					cboxTipo[i].setSelectedIndex(1);
					break;
				}
				
			case 2:
				{
					cboxTipo[i].setSelectedIndex(2);
				}
			}
		}
		
		if (fc.blnTag)
		{
			editarTag(true);
			for (int i=3; i<33; i++)
			{
				edtTitulo.setText(edtTitulo.getText()+(char)(fc.tagv1[i]));
				edtArtista.setText(edtArtista.getText()+(char)(fc.tagv1[i+30]));
				edtAlbum.setText(edtAlbum.getText()+(char)(fc.tagv1[i+60]));
				edtComentario.setText(edtComentario.getText()+(char)(fc.tagv1[i+94]));
			}
			for (int i=93;i<97;i++)
				edtAño.setText(edtAño.getText()+(char)(fc.tagv1[i]));
		}
		else editarTag(false);
	}

	private void btnGuardar_click(Object source, Event e)
	{
		fc.InfoC.bpm=(new Float(edtBpm.getText())).floatValue();
		for (int i=0; i<19; i++)
		{
			fc.InfoC.setCompas(i,(new Integer(edtCompas[i].getText())).intValue());
			fc.InfoC.setDesfase(i,(new Integer(edtDesfase[i].getText())).intValue());
			fc.InfoC.setTipo(i, cboxTipo[i].getSelectedIndex());
		}
		fc.setSpeed(0,(new Float(fc.f1.lblBpm.getText()).floatValue()));
		fc.intBasePitch=fc.calcFreq((new Float(fc.edtPitch.getText()).floatValue()));
		if(!fc.añadido) 
		{
			fc.f1.VInfo.addElement(fc.InfoC);
			fc.añadido=true;
			fc.f1.guardar();
		}
		this.close();
	}

	private void btnCalcBpm_click(Object source, Event e)
	{
		int compas=0;
		int desfase=0;
		int beats=0;
		int buff=0;
		for (int i=0; i<19; i++)
		{
			compas=compas+(new Integer(edtCompas[i].getText())).intValue();
			desfase=desfase+(new Integer(edtDesfase[i].getText())).intValue();
		}
		beats=compas*32+desfase;
		if (beats>0)
		{
			buff=fc.InfoC.getCue(18)-fc.InfoC.getCue(0);
			float duracion=(float)buff/176400f;
			float bpm=((60f*(float)beats)/duracion);
			edtBpm.setText(""+bpm);			
		}
	}

	private void btnAutoBpm_click(Object source, Event e)
	{
		int min=(new Integer(lblMin.getText())).intValue();
		int max=(new Integer(lblMax.getText())).intValue();
		double[] progreso=new double[1];
		double[] acierto=new double[1];
		btnAutoBpm.setEnabled(false);
		hilo=new BPM(this,fc.strPathFileName,(double)min,(double)max,progreso,acierto);
		hilo.start();
	}

	/**
	 * NOTA: el código siguiente es necesario para el diseñador de
	 * formularios de Visual J++ y puede modificarse con el editor de formularios.
	 * No lo modifique desde el editor de código.
	 */
	Container components = new Container();
	Label lblNombre = new Label();
	Button btnGuardar = new Button();
	Button btnBorrar = new Button();
	TabPage tabCue = new TabPage();
	TabPage tabBpm = new TabPage();
	TabControl tab = new TabControl();
	Button btnCalcBpm = new Button();
	Label lblBpm = new Label();
	Edit edtBpm = new Edit();
	TabPage tabTag = new TabPage();
	GroupBox groupBox2 = new GroupBox();
	GroupBox groupBox1 = new GroupBox();
	Button btnAutoBpm = new Button();
	Label label1 = new Label();
	Label label2 = new Label();
	Label lblMin = new Label();
	Label lblMax = new Label();
	UpDown upDown1 = new UpDown();
	UpDown upDown2 = new UpDown();
	Label label7 = new Label();
	Label label11 = new Label();
	Label label12 = new Label();
	Label label13 = new Label();
	Label label14 = new Label();
	Label label15 = new Label();
	Edit edtTitulo = new Edit();
	Edit edtArtista = new Edit();
	Edit edtAlbum = new Edit();
	Edit edtComentario = new Edit();
	Edit edtAño = new Edit();
	Label label16 = new Label();
	ComboBox cboxGenero = new ComboBox();
	Label label3 = new Label();
	Label label4 = new Label();

	private void initForm()
	{
		this.setText("");
		this.setAutoScaleBaseSize(new Point(5, 13));
		this.setBorderStyle(FormBorderStyle.FIXED_SINGLE);
		this.setClientSize(new Point(418, 301));
		this.setMaximizeBox(false);
		this.setMinimizeBox(false);
		this.setShowInTaskbar(false);
		this.setStartPosition(FormStartPosition.CENTER_SCREEN);

		lblNombre.setLocation(new Point(6, 10));
		lblNombre.setSize(new Point(406, 18));
		lblNombre.setTabIndex(1);
		lblNombre.setTabStop(false);
		lblNombre.setText("");
		lblNombre.setBorderStyle(BorderStyle.FIXED_3D);

		btnGuardar.setLocation(new Point(334, 274));
		btnGuardar.setSize(new Point(78, 26));
		btnGuardar.setTabIndex(2);
		btnGuardar.setText("Guardar");
		btnGuardar.addOnClick(new EventHandler(this.btnGuardar_click));

		btnBorrar.setLocation(new Point(316, 182));
		btnBorrar.setSize(new Point(78, 26));
		btnBorrar.setTabIndex(0);
		btnBorrar.setText("Borrar");
		btnBorrar.addOnClick(new EventHandler(this.btnBorrar_click));

		tabCue.setLocation(new Point(4, 25));
		tabCue.setSize(new Point(398, 211));
		tabCue.setTabIndex(1);
		tabCue.setText("CUE\'s");

		tabBpm.setLocation(new Point(4, 25));
		tabBpm.setSize(new Point(398, 211));
		tabBpm.setTabIndex(0);
		tabBpm.setText("BPM");

		tab.setLocation(new Point(6, 32));
		tab.setSize(new Point(406, 240));
		tab.setTabIndex(0);
		tab.setText("tabControl1");
		tab.setSelectedIndex(2);

		btnCalcBpm.setLocation(new Point(226, 18));
		btnCalcBpm.setSize(new Point(78, 26));
		btnCalcBpm.setTabIndex(0);
		btnCalcBpm.setText("Calcula BPM");
		btnCalcBpm.addOnClick(new EventHandler(this.btnCalcBpm_click));

		lblBpm.setLocation(new Point(122, 18));
		lblBpm.setSize(new Point(24, 14));
		lblBpm.setTabIndex(0);
		lblBpm.setTabStop(false);
		lblBpm.setText("BPM");

		edtBpm.setLocation(new Point(150, 14));
		edtBpm.setSize(new Point(60, 20));
		edtBpm.setTabIndex(1);
		edtBpm.setText("");
		edtBpm.setMaxLength(8);
		edtBpm.setTextAlign(HorizontalAlignment.CENTER);

		tabTag.setLocation(new Point(4, 25));
		tabTag.setSize(new Point(398, 211));
		tabTag.setTabIndex(2);
		tabTag.setText("ID3v1 Tag");

		groupBox2.setLocation(new Point(10, 122));
		groupBox2.setSize(new Point(320, 60));
		groupBox2.setTabIndex(2);
		groupBox2.setTabStop(false);
		groupBox2.setText("Cálculo BPM automático");

		groupBox1.setLocation(new Point(10, 50));
		groupBox1.setSize(new Point(320, 56));
		groupBox1.setTabIndex(3);
		groupBox1.setTabStop(false);
		groupBox1.setText("Cálculo BPM con CUE\'s");

		btnAutoBpm.setLocation(new Point(226, 22));
		btnAutoBpm.setSize(new Point(78, 26));
		btnAutoBpm.setTabIndex(0);
		btnAutoBpm.setText("Calcula BPM");
		btnAutoBpm.addOnClick(new EventHandler(this.btnAutoBpm_click));

		label1.setLocation(new Point(12, 28));
		label1.setSize(new Point(20, 12));
		label1.setTabIndex(2);
		label1.setTabStop(false);
		label1.setText("Min");

		label2.setLocation(new Point(122, 28));
		label2.setSize(new Point(20, 12));
		label2.setTabIndex(1);
		label2.setTabStop(false);
		label2.setText("Max");

		lblMin.setFont(new Font("MS Sans Serif", 12.0f, FontSize.POINTS, FontWeight.NORMAL, false, false, false, CharacterSet.DEFAULT, 0));
		lblMin.setLocation(new Point(34, 24));
		lblMin.setSize(new Point(36, 22));
		lblMin.setTabIndex(4);
		lblMin.setTabStop(false);
		lblMin.setText("130");
		lblMin.setBorderStyle(BorderStyle.FIXED_3D);
		lblMin.setTextAlign(HorizontalAlignment.CENTER);

		lblMax.setFont(new Font("MS Sans Serif", 12.0f, FontSize.POINTS, FontWeight.NORMAL, false, false, false, CharacterSet.DEFAULT, 0));
		lblMax.setLocation(new Point(146, 24));
		lblMax.setSize(new Point(36, 22));
		lblMax.setTabIndex(3);
		lblMax.setTabStop(false);
		lblMax.setText("180");
		lblMax.setBorderStyle(BorderStyle.FIXED_3D);
		lblMax.setTextAlign(HorizontalAlignment.CENTER);

		upDown1.setBuddyControl(lblMin);
		upDown1.setLocation(new Point(70, 24));
		upDown1.setSize(new Point(16, 22));
		upDown1.setTabIndex(6);
		upDown1.setMinimum(110);
		upDown1.setMaximum(220);
		upDown1.setValue(130);

		upDown2.setBuddyControl(lblMax);
		upDown2.setLocation(new Point(182, 24));
		upDown2.setSize(new Point(16, 22));
		upDown2.setTabIndex(5);
		upDown2.setMinimum(110);
		upDown2.setMaximum(220);
		upDown2.setValue(180);

		label7.setForeColor(Color.ACTIVECAPTION);
		label7.setLocation(new Point(5, 6));
		label7.setSize(new Point(100, 14));
		label7.setTabIndex(3);
		label7.setTabStop(false);
		label7.setText("Nº   C     D        Tipo");

		label11.setLocation(new Point(36, 56));
		label11.setSize(new Point(32, 12));
		label11.setTabIndex(5);
		label11.setTabStop(false);
		label11.setText("Titulo");
		label11.setTextAlign(HorizontalAlignment.RIGHT);

		label12.setLocation(new Point(36, 82));
		label12.setSize(new Point(32, 12));
		label12.setTabIndex(4);
		label12.setTabStop(false);
		label12.setText("Artista");
		label12.setTextAlign(HorizontalAlignment.RIGHT);

		label13.setLocation(new Point(36, 108));
		label13.setSize(new Point(32, 12));
		label13.setTabIndex(3);
		label13.setTabStop(false);
		label13.setText("Album");
		label13.setTextAlign(HorizontalAlignment.RIGHT);

		label14.setLocation(new Point(36, 134));
		label14.setSize(new Point(32, 12));
		label14.setTabIndex(1);
		label14.setTabStop(false);
		label14.setText("Año");
		label14.setTextAlign(HorizontalAlignment.RIGHT);

		label15.setLocation(new Point(16, 158));
		label15.setSize(new Point(52, 12));
		label15.setTabIndex(0);
		label15.setTabStop(false);
		label15.setText("Comentario");
		label15.setTextAlign(HorizontalAlignment.RIGHT);

		edtTitulo.setLocation(new Point(72, 52));
		edtTitulo.setSize(new Point(218, 20));
		edtTitulo.setTabIndex(9);
		edtTitulo.setText("");
		edtTitulo.setMaxLength(30);

		edtArtista.setLocation(new Point(72, 78));
		edtArtista.setSize(new Point(218, 20));
		edtArtista.setTabIndex(7);
		edtArtista.setText("");
		edtArtista.setMaxLength(30);

		edtAlbum.setLocation(new Point(72, 104));
		edtAlbum.setSize(new Point(218, 20));
		edtAlbum.setTabIndex(8);
		edtAlbum.setText("");
		edtAlbum.setMaxLength(30);

		edtComentario.setLocation(new Point(72, 156));
		edtComentario.setSize(new Point(218, 20));
		edtComentario.setTabIndex(6);
		edtComentario.setText("");
		edtComentario.setMaxLength(30);

		edtAño.setLocation(new Point(72, 130));
		edtAño.setSize(new Point(34, 20));
		edtAño.setTabIndex(10);
		edtAño.setText("");
		edtAño.setMaxLength(4);
		edtAño.setTextAlign(HorizontalAlignment.CENTER);

		label16.setLocation(new Point(136, 134));
		label16.setSize(new Point(32, 12));
		label16.setTabIndex(2);
		label16.setTabStop(false);
		label16.setText("Album");
		label16.setTextAlign(HorizontalAlignment.RIGHT);

		cboxGenero.setLocation(new Point(172, 130));
		cboxGenero.setSize(new Point(118, 21));
		cboxGenero.setTabIndex(11);
		cboxGenero.setText("");

		label3.setForeColor(Color.ACTIVECAPTION);
		label3.setLocation(new Point(133, 6));
		label3.setSize(new Point(104, 14));
		label3.setTabIndex(2);
		label3.setTabStop(false);
		label3.setText("Nº   C     D        Tipo");

		label4.setForeColor(Color.ACTIVECAPTION);
		label4.setLocation(new Point(263, 6));
		label4.setSize(new Point(102, 14));
		label4.setTabIndex(1);
		label4.setTabStop(false);
		label4.setText("Nº   C     D        Tipo");

		this.setNewControls(new Control[] {
							lblNombre, 
							btnGuardar, 
							tab});
		tabCue.setNewControls(new Control[] {
							  label4, 
							  label3, 
							  label7, 
							  btnBorrar});
		tabBpm.setNewControls(new Control[] {
							  groupBox2, 
							  groupBox1, 
							  edtBpm, 
							  lblBpm});
		tab.setNewControls(new Control[] {
						   tabBpm, 
						   tabCue, 
						   tabTag});
		tabTag.setNewControls(new Control[] {
							  cboxGenero, 
							  label16, 
							  edtAño, 
							  edtComentario, 
							  edtAlbum, 
							  edtArtista, 
							  edtTitulo, 
							  label15, 
							  label14, 
							  label13, 
							  label12, 
							  label11});
		groupBox2.setNewControls(new Control[] {
								 upDown2, 
								 upDown1, 
								 lblMax, 
								 lblMin, 
								 label2, 
								 label1, 
								 btnAutoBpm});
		groupBox1.setNewControls(new Control[] {
								 btnCalcBpm});
	}
}

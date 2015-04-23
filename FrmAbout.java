import com.ms.wfc.app.*;
import com.ms.wfc.core.*;
import com.ms.wfc.ui.*;
import com.ms.wfc.html.*;

/**
 * Esta clase puede tomar un número variable de parámetros 
 * en la línea de comandos. La ejecución del programa comienza 
 * con el método main(). La llamada al constructor de clase no tiene lugar a menos que se cree un objeto del tipo 'FrmAbout'
 * en el método main().
 */
public class FrmAbout extends Form
{
	public FrmAbout()
	{
		super();

		// Requerido para la compatibilidad con el Diseñador de formularios de Visual J++
		initForm();
		// TODO: agregar el código del constructor después de la llamada a initForm.
		
		
		
		
	}

	/**
	 * FrmAbout reemplaza el método dispose para poder limpiar 
	 * la lista de componentes.
	 */
	public void dispose()
	{
		super.dispose();
		components.dispose();
	}

	/**
	 * NOTA: el código siguiente es necesario para el diseñador de
	 * formularios de Visual J++ y puede modificarse con el editor de formularios.
	 * No lo modifique desde el editor de código.
	 */
	Container components = new Container();
	Label label1 = new Label();
	Label label2 = new Label();
	Label label3 = new Label();
	Label label4 = new Label();
	Label label5 = new Label();
	Label label6 = new Label();
	Label label7 = new Label();

	private void initForm()
	{
		this.setText("FrmAbout");
		this.setAutoScaleBaseSize(new Point(5, 13));
		this.setClientSize(new Point(300, 237));

		label1.setFont(new Font("MS Sans Serif", 14.0f, FontSize.POINTS, FontWeight.NORMAL, false, false, false, CharacterSet.DEFAULT, 0));
		label1.setLocation(new Point(90, 10));
		label1.setSize(new Point(94, 24));
		label1.setTabIndex(0);
		label1.setTabStop(false);
		label1.setText("KoLo V-DJ");

		label2.setLocation(new Point(16, 54));
		label2.setSize(new Point(178, 14));
		label2.setTabIndex(5);
		label2.setTabStop(false);
		label2.setText("Programación:   Jose Luis Bode Lacal");

		label3.setLocation(new Point(18, 70));
		label3.setSize(new Point(178, 14));
		label3.setTabIndex(4);
		label3.setTabStop(false);
		label3.setText("          Diseño:   Jose Luis Bode Lacal");

		label4.setLocation(new Point(222, 2));
		label4.setSize(new Point(74, 14));
		label4.setTabIndex(3);
		label4.setTabStop(false);
		label4.setText("Copyright 2002");

		label5.setForeColor(Color.BLUE);
		label5.setLocation(new Point(16, 158));
		label5.setSize(new Point(178, 14));
		label5.setTabIndex(2);
		label5.setTabStop(false);
		label5.setText("Publicado bajo licencia FREEWARE.");

		label6.setForeColor(new Color(192, 0, 0));
		label6.setLocation(new Point(16, 174));
		label6.setSize(new Point(208, 42));
		label6.setTabIndex(1);
		label6.setTabStop(false);
		label6.setText("Esta autorizada la libre distribución de este programa siempre que no se modifique su contenido.");

		label7.setLocation(new Point(18, 100));
		label7.setSize(new Point(260, 42));
		label7.setTabIndex(6);
		label7.setTabStop(false);
		label7.setText("Este programa fue creado como proyecto de fin de curso del módulo de ciclo superior de Administrador de Sistemas Informaticos.");

		this.setNewControls(new Control[] {
							label7, 
							label6, 
							label5, 
							label4, 
							label3, 
							label2, 
							label1});
	}

	/**
	 * Punto de entrada principal para la aplicación. 
	 *
	 * @param args Matriz de parámetros pasados a la aplicación
	 * mediante la línea de comandos.
	 */
}

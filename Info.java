import java.io.Serializable;

public class Info implements Serializable
{
	String nombre;
	float bpm;
	int volumen;
	private int[] cue=new int[19];
	private int[] compas=new int[19];
	private int[] desfase=new int[19];
	private int[] xpos=new int[19];
	private int[] tipo=new int[19];
	private String[] comentario= new String[19];
	
	//constructor
	public Info (String n)
	{
		nombre=n;
		volumen=0;
		bpm=0;
		for (int i=0; i<19; i++)
		{
			cue[i]=-1;
			compas[i]=0;
			desfase[i]=0;
			xpos[i]=-1;
			tipo[i]=0;
		}
	}
	
	//metodos k aceptan
	public void setBpm (float b)
	{
		bpm=b;
	}
	
	public void setCue (int trkpos, int ncue)
	{
		cue[ncue]=trkpos;
	}
	
	public void setCompas (int ncompas, int c)
	{
		compas[ncompas]=c;
	}
	
	public void setDesfase (int ndesfase, int d)
	{
		desfase[ndesfase]=d;
	}
	
	public void setXPos (int npos, int p)
	{
		xpos[npos]=p;
	}
	
	public void setTipo (int ntipo,int t)
	{
		tipo[ntipo]=t;
	}
	
	public void setVolumen (int vol)
	{
		volumen=vol;
	}
	
	public void setComent (int ncue, String coment)
	{
		comentario[ncue]=coment;
	}
	
	
	
	//metodos k devuelven
	public double getBpm ()
	{
		return bpm;
	}
	
	public int getCue (int ncue)
	{
		return cue[ncue];
	}
	
	public int getCompas (int ncompas)
	{
		return compas[ncompas];
	}
	
	public int getDesfase (int ndesfase)
	{
		return desfase[ndesfase];
	}
	
	public int getTipo (int ntipo)
	{
		return tipo[ntipo];
	}
	
	public int getXpos (int npos)
	{
		return xpos[npos];
	}	
	
	public String getNombre()
	{
		return nombre;
	}
	
	public int getVolumen ()
	{
		return volumen;
	}
	
	public String getComent(int ncue)
	{
		return comentario[ncue];
	}
}
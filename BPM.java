public class BPM extends Thread
{
	public double bpm;
	private String archivo; 
	private double minBpm,maxBpm;
	private double[] punteroProgreso,punteroAcierto;
	private FrmInfo finfo;
	
	
	public BPM(FrmInfo fi, String archiv, double mnBpm, double mxBpm, double[] puntProgreso, double[] puntAcierto)
	{
		archivo=archiv;
		minBpm=mnBpm;
		maxBpm=mxBpm;
		punteroProgreso=puntProgreso;
		punteroAcierto=puntAcierto;
		finfo=fi;
	}
	
	public void run()
	{
		try
		{
			finfo.edtBpm.setText(""+getBpm(archivo,minBpm,maxBpm,punteroProgreso,punteroAcierto));
		}
		catch (java.lang.UnknownError ue)
		{
			finfo.edtBpm.setText("Error al calcular BPM");
			this.stop();
		}
		finfo.btnAutoBpm.setEnabled(true);
	}
	
	/**
	 * @dll.import ("BeatCnt.dll",entrypoint="BeatGetBpmFromFile")
	 */
	public static native double getBpm(String archivo, double minBpm, double maxBpm, double[] punteroProgreso, double[] punteroAcierto);		
}
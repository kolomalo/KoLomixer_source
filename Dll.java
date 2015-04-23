public class Dll
{
	int vdll;
	
	
	public Dll (int ndll)
	{
		vdll=ndll;
	}
	

	public boolean iniciar(int dispositivo, int freq, int flags,int whandle)
	{
		if (vdll==1) return iniciar1(dispositivo,freq,flags,whandle);
		else return iniciar2(dispositivo,freq,flags,whandle);
	}
	
	public int abrir(boolean memoria, String archivo, int offset, int longitud, int flags)
	{
		if (vdll==1) return abrir1(memoria,archivo,offset,longitud,flags);
		else return abrir2(memoria,archivo,offset,longitud,flags);
	}

	public boolean reproducir(int puntero, boolean flush, int flags)
	{
		if (vdll==1) return reproducir1(puntero,flush,flags);
		else return reproducir2(puntero,flush,flags);
	}
	
	public int setAtributos(int puntero, int freq, int volumen, int balance)
	{
		if (vdll==1) return setAtributos1(puntero,freq,volumen,balance);
		else return setAtributos2(puntero,freq,volumen,balance);
	}
	
	public boolean start()
	{
		if (vdll==1) return start1();
		else return start2();
	}
	
	public int getLongitud(int puntero)
	{
		if (vdll==1) return getLongitud1(puntero);
		else return getLongitud2(puntero);
	}
	
	public int getPosActual(int puntero)
	{
		if (vdll==1) return getPosActual1(puntero);
		else return getPosActual2(puntero);
	}
	
	public int setPosActual(int puntero, long posicion)
	{
		if (vdll==1) return setPosActual1(puntero,posicion);
		else return setPosActual2(puntero,posicion);
	}
	
	public int error()
	{
		if (vdll==1) return error1();
		else return error2();
	}
	
	public void cerrar()
	{
		if (vdll==1) cerrar1();
		else cerrar2();
	}
	
	public int setVolumen(int puntero, int volumen)
	{
		if (vdll==1) return setAtributos1(puntero,-1,volumen,-101);
		else return setAtributos2(puntero,-1,volumen,-101);
	}
	
	public int setBalance(int puntero, int balance)
	{
		if (vdll==1) return setAtributos1(puntero,-1,-1,balance);
		else return setAtributos2(puntero,-1,-1,balance);
	}
		
	public int setFreq(int puntero, int freq)
	{
		if (vdll==1) return setAtributos1(puntero,freq,-1,-101);
		else return setAtributos2(puntero,freq,-1,-101);
	}
	
	public int pausa(int puntero)
	{
		if (vdll==1) return pausa1(puntero);
		else return pausa2(puntero);
	}
	
	public int stop(int puntero)
	{
		if (vdll==1) return parar1(puntero);
		else return parar2(puntero);
	}
	
	public int estado(int puntero)
	{
		if (vdll==1) return estado1(puntero);
		else return estado2(puntero);
	}
	
	public int resumir(int puntero)
	{
		if (vdll==1) return resumir1(puntero);
		else return resumir2(puntero);
	}
	
	public int preBuff(int puntero)
	{
		if (vdll==1) return preBuff1(puntero);
		else return preBuff2(puntero);
	}
	
	public int free(int puntero)
	{
		if (vdll==1) return free1(puntero);
		else return free2(puntero);
	}
	
	
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////// BASS.DLL ////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * @dll.import ("bass.dll",entrypoint="BASS_Init")
	 */
	private static native boolean iniciar1(int dispositivo, int freq, int flags,int whandle);
		
	/**
	 * @dll.import ("bass.dll",entrypoint="BASS_StreamCreateFile")
	 */
	private static native int abrir1(boolean memoria, String archivo, int offset, int longitud, int flags);
		
	/**
	 * @dll.import ("bass.dll",entrypoint="BASS_StreamPlay")
	 */
	private static native boolean reproducir1(int puntero, boolean flush, int flags);
	
	/**
	 * @dll.import ("bass.dll",entrypoint="BASS_ChannelSetAttributes")
	 */
	private static native int setAtributos1(int puntero, int freq, int volumen, int balance);
	
	/**
	 * @dll.import ("bass.dll",entrypoint="BASS_Start")
	 */
	private static native boolean start1();
	
	/**
	 * @dll.import ("bass.dll",entrypoint="BASS_SetLogCurves")
	 */
	private static native int curvasVolumen1(boolean volumen, boolean balance);
	
	/**
	 * @dll.import ("bass.dll",entrypoint="BASS_StreamGetLength")
	 */
	private static native int getLongitud1(int puntero);
	
	/**
	 * @dll.import ("bass.dll",entrypoint="BASS_ChannelGetPosition")
	 */
	private static native int getPosActual1(int puntero);
	
	/**
	 * @dll.import ("bass.dll",entrypoint="BASS_ChannelSetPosition")
	 */
	private static native int setPosActual1(int puntero, long posicion);
	
	/**
	 * @dll.import ("bass.dll",entrypoint="BASS_Free")
	 */
	private static native void cerrar1();
	
	/**
	 * @dll.import ("bass.dll",entrypoint="BASS_ErrorGetCode")
	 */
	private static native int error1();
	
	/**
	 * @dll.import ("bass.dll",entrypoint="BASS_ChannelStop")
	 */
	private static native int parar1(int puntero);
	
	/**
	 * @dll.import ("bass.dll",entrypoint="BASS_ChannelPause")
	 */
	private static native int pausa1(int puntero);
	
	/**
	 * @dll.import ("bass.dll",entrypoint="BASS_ChannelIsActive")
	 */
	private static native int estado1(int puntero);
	
	/**
	 * @dll.import ("bass.dll",entrypoint="BASS_ChannelResume")
	 */
	private static native int resumir1(int puntero);
	
	/**
	 * @dll.import ("bass.dll",entrypoint="BASS_StreamPreBuf")
	 */
	private static native int preBuff1(int puntero);
	
	/**
	 * @dll.import ("bass.dll",entrypoint="BASS_StreamFree")
	 */
	private static native int free1(int puntero);
	
	
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////// BASS2.DLL ////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * @dll.import ("bass2.dll",entrypoint="BASS_Init")
	 */
	private static native boolean iniciar2(int dispositivo, int freq, int flags,int whandle);
		
	/**
	 * @dll.import ("bass2.dll",entrypoint="BASS_StreamCreateFile")
	 */
	private static native int abrir2(boolean memoria, String archivo, int offset, int longitud, int flags);
		
	/**
	 * @dll.import ("bass2.dll",entrypoint="BASS_StreamPlay")
	 */
	private static native boolean reproducir2(int puntero, boolean flush, int flags);
	
	/**
	 * @dll.import ("bass2.dll",entrypoint="BASS_ChannelSetAttributes")
	 */
	private static native int setAtributos2(int puntero, int freq, int volumen, int balance);
	
	/**
	 * @dll.import ("bass2.dll",entrypoint="BASS_Start")
	 */
	private static native boolean start2();
	
	/**
	 * @dll.import ("bass2.dll",entrypoint="BASS_SetLogCurves")
	 */
	private static native int curvasVolumen2(boolean volumen, boolean balance);
	
	/**
	 * @dll.import ("bass2.dll",entrypoint="BASS_StreamGetLength")
	 */
	private static native int getLongitud2(int puntero);
	
	/**
	 * @dll.import ("bass2.dll",entrypoint="BASS_ChannelGetPosition")
	 */
	private static native int getPosActual2(int puntero);
	
	/**
	 * @dll.import ("bass2.dll",entrypoint="BASS_ChannelSetPosition")
	 */
	private static native int setPosActual2(int puntero, long posicion);
	
	/**
	 * @dll.import ("bass2.dll",entrypoint="BASS_Free")
	 */
	private static native void cerrar2();
	
	/**
	 * @dll.import ("bass2.dll",entrypoint="BASS_ErrorGetCode")
	 */
	private static native int error2();
	
	/**
	 * @dll.import ("bass2.dll",entrypoint="BASS_ChannelStop")
	 */
	private static native int parar2(int puntero);
	
	/**
	 * @dll.import ("bass2.dll",entrypoint="BASS_ChannelPause")
	 */
	private static native int pausa2(int puntero);
	
	/**
	 * @dll.import ("bass2.dll",entrypoint="BASS_ChannelIsActive")
	 */
	private static native int estado2(int puntero);
	
	/**
	 * @dll.import ("bass2.dll",entrypoint="BASS_ChannelResume")
	 */
	private static native int resumir2(int puntero);
	
	/**
	 * @dll.import ("bass2.dll",entrypoint="BASS_StreamPreBuf")
	 */
	private static native int preBuff2(int puntero);
	
	/**
	 * @dll.import ("bass2.dll",entrypoint="BASS_StreamFree")
	 */
	private static native int free2(int puntero);
	
}

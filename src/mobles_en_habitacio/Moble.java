package mobles_en_habitacio;

import java.io.DataOutputStream;

public class Moble {

	private double ample;
	private double llarg;
	private int color;		
	
	public Moble() {}
	
	public Moble(double ample, double llarg, int color) {
		super();
		this.ample = ample;
		this.llarg = llarg;
		this.color = color;
	}
	
	public double getAmple() {
		return ample;
	}
	public void setAmple(double ample) {
		this.ample = ample;
	}
	public double getLlarg() {
		return llarg;
	}
	public void setLlarg(double llarg) {
		this.llarg = llarg;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return "Moble [ample=" + ample + ", llarg=" + llarg + ", color=" + color + "]";
	}
	
	
	//ask user to enter Moble's data
	public void setNewMoble()
	{
		this.ample = Double.parseDouble(Main.getStringFromScanner("Introdueix la amplada del moble (double): "));
		this.llarg = Double.parseDouble(Main.getStringFromScanner("Introdueix la llargada del moble (double): "));
		this.color = Integer.parseInt(Main.getStringFromScanner("Introdueix el color del moble (int 0-9): "));		
	}
	
	
	//write Moble data into a file
	public void writeMoble(DataOutputStream dos)
	{
		try 
		{
			dos.writeDouble(this.ample);
			dos.writeDouble(this.llarg);
			dos.writeInt(this.color);
			dos.close();
			System.out.println("Moble saved -- OK\n");
		}
		catch(Exception e)
		{
			System.out.println("Moble not saved -- ERROR ("+e+")\n");
		}
	}
}

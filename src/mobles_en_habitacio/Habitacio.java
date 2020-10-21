package mobles_en_habitacio;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class Habitacio {

	private double ample = 0.0;
	private double llarg = 0.0;
	private String nom = "";
	
	List<Moble> mobles = new ArrayList<Moble>();
	String[] properties = new String[]{"ample","llarg","nom"};

	public Habitacio(){}
	
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
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}

	
	@Override
	public String toString() {
		return "Habitacio [ample=" + ample + ", llarg=" + llarg + ", nom=" + nom + "], Mobles [quantitat="+getQuantitatMobles()+"]\n";
	}
	
	public void addMoble(Moble moble) {
		this.mobles.add(moble);
	}

	public int getQuantitatMobles() 
	{
		return mobles.size();
	}
	
	public void addNewMoble() {
		
		Moble moble = new Moble();
		moble.setNewMoble();
		this.addMoble(moble);
	
		System.out.println("Moble added -- OK\n");
	}	
	
	public void openRoom(String pathFile)
	{
		File f = new File(pathFile);
		
		//check if file exists and if is a file
		if (f.exists() && f.isFile())
		{
			DataInputStream dis = null;
			try 
			{
				dis = Main.getDISFromFile(pathFile); //returns FileNotFoundException
				this.setAmple(dis.readDouble());
				this.setLlarg(dis.readDouble());
				this.setNom(dis.readUTF());
				
				int size = (int) dis.readInt();
				for(int i = 0; i < size; i++)
				{
					Moble moble = new Moble();
					moble.setAmple(dis.readDouble());
					moble.setLlarg(dis.readDouble());
					moble.setColor(dis.readInt());
					this.addMoble(moble);
				}
			}
			catch(Exception e)
			{
				System.out.println("Room not opened -- ERROR ("+e+")\n");
			}
			finally 
			{
				try 
				{
					dis.close();
				}
				catch(Exception e)
				{
					System.out.println("DIS not closed -- ERROR ("+e+")\n");
				}
			}

			System.out.println("Existing room\n");
		}
		else
		{
			this.setAmple(3.0);
			this.setLlarg(6.0);
			this.setNom("Main Room");
			
			System.out.println("New Room added\n");
		}
	}
	
	public void writeRoom(DataOutputStream dos)
	{
		try 
		{
			dos.writeDouble(this.getAmple());
			dos.writeDouble(this.getLlarg());
			dos.writeUTF(this.getNom());
			dos.writeInt(this.getQuantitatMobles());
			for(Moble moble : mobles)
			{
				dos.writeDouble(moble.getAmple());
				dos.writeDouble(moble.getLlarg());
				dos.writeInt(moble.getColor());
			}		
			System.out.println("Data saved -- OK\n");
		}		
		catch(Exception e)
		{
			System.out.println("Write ERROR ("+e+")\n");
		}
		finally 
		{
			if(dos != null)
			{
				try 
				{
					dos.close();
				}
				catch(Exception e)
				{
					System.out.println("DOS not closed -- ERROR ("+e+")");
				}			
			}
		}
	}
	
	public void readRoom()
	{
		System.out.println("HABITACIÓ");
		System.out.println(this.toString());
		this.llistaMobles();
	}	
	

	public void llistaMobles()
	{
		System.out.println("MOBLES");
		
		if(this.getQuantitatMobles()<=0)
		{
			System.out.println("No hi ha mobles\n");
		}
		else
		{
			int i = 0;
			for(Moble moble : mobles)
			{
				System.out.println((i+1)+". "+moble.toString());
				i++;
			}			
		}
		System.out.println("\n");
	}
	
	
	public void readProperties(String pathFile)
	{
		File propertyFile = new File(pathFile);
		if(propertyFile.exists() && propertyFile.isFile())
			
			try {
				InputStream inData = new FileInputStream(propertyFile); //Data of properties's file
				Properties fileProperties = new Properties();

				//load data from properties
				fileProperties.load(inData);
				
				for(int i = 0; i < properties.length; i++)
				{
					System.out.println(fileProperties.getProperty(properties[i]));
				}
				
			}
			catch(Exception e)
			{
				System.out.println("Properties not loaded");
			}
		else
		{
			System.out.println("File not found ("+propertyFile.getAbsolutePath()+")");
		}	
	}
	
	
	public String[] getProperties()
	{
		return this.properties;
	}
	
}
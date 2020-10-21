package mobles_en_habitacio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class Habitacio {

	private double ample = 0.0;
	private double llarg = 0.0;
	private String nom = "";
	
	List<Moble> mobles = new ArrayList<Moble>();
	String[] properties = new String[]{"ample","llarg","nom","quantitat_mobles"};

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
	
	public List<Moble> getMobles() {
		return mobles;
	}

	public String[] getProperties() {
		return properties;
	}

	public void setProperties(String[] properties) {
		this.properties = properties;
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
	

	//Load default values for the room
	public void loadRoomDefaultValues()
	{
		this.setAmple(3.0);
		this.setLlarg(6.0);
		this.setNom("Main room");
	}
	
	
	public void addNewMoble() {
		
		Moble moble = new Moble();
		moble.setNewMoble();
		this.addMoble(moble);
	
		System.out.println("Moble added -- OK\n");
	}	


	//list each Moble data from mobles ArrayList
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

	
	//write loaded data in a properties file
	public void writeProperties(String pathFile)
	{
		try {
			OutputStream outData = new BufferedOutputStream(new FileOutputStream(pathFile));
			Properties fileProperties = new Properties();

			fileProperties.setProperty(properties[0], String.valueOf(this.getAmple())); //ample
			fileProperties.setProperty(properties[1], String.valueOf(this.getLlarg()));
			fileProperties.setProperty(properties[2], this.getNom());
			fileProperties.setProperty(properties[3], String.valueOf(this.getMobles().size()));
			
			//second param is for comments (null)
			fileProperties.store(outData, null);
		}
		catch(Exception e)
		{
			System.out.println("Properties not saved ("+e+")\n");
		}
	}	
	
	
	//read the properties file's content and load its data	
	public void readProperties(String pathFile)
	{
		try {
			InputStream inData = new BufferedInputStream(new FileInputStream(pathFile)); //Data of properties's file
			Properties fileProperties = new Properties();

			//load data from properties
			fileProperties.load(inData);

			this.setAmple(Double.parseDouble(fileProperties.getProperty(properties[0])));
			this.setLlarg(Double.parseDouble(fileProperties.getProperty(properties[1])));
			this.setNom(fileProperties.getProperty(properties[2]));
		}
		catch(Exception e)
		{
			System.out.println("Properties not loaded ("+e+")\n");
		}
	}	

	
	//write loaded data to the file pathFile's path
	public void writeMobles(String pathFile)
	{
		DataOutputStream dos = null;
		try 
		{
			dos = Main.getDOSFromFile(pathFile);
			dos.writeInt(this.getMobles().size());
			for(Moble moble : this.getMobles())
			{
				dos.writeDouble(moble.getAmple());
				dos.writeDouble(moble.getLlarg());
				dos.writeInt(moble.getColor());
			}
			System.out.println("Mobiliari saved -- OK\n");
		}
		catch(Exception e)
		{
			System.out.println("Moble not Saved -- ERROR("+e+")\n");
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
					System.out.println("DOS not closed ("+e+")\n");
				}
			}				
		}
	}
	
	
	//check if entered pathFile is a file in order to read it and load its data
	public void readMobles(String pathFile)
	{
		File file = new File(pathFile);
		if(file.exists() && file.isFile())
		{
			DataInputStream dis = null;
			try {
				dis = Main.getDISFromFile(pathFile); //Data of properties's file

				int size = dis.readInt();
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
				System.out.println("Mobles not loaded ("+e+")\n");
			}
			finally
			{
				if(dis != null)
				{
					try
					{
						dis.close();
					}
					catch(Exception e)
					{
						System.out.println("DOS not closed ("+e+")\n");
					}
				}				
			}
		}
		else
		{
			System.out.println("No Mobles file\n");
		}
	}	

}
package mobles_en_habitacio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Scanner;

public class Main {

	private static Habitacio hb = new Habitacio();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String moblesFile = "";
		String extension = "properties";
		int option = 1;
		String chosen_option = "";
		
		String pathFile = getStringFromScanner("Introdueix el path del File amb les dades de l'Habitació (*.properties): ");
		if(!checkFileExtension(pathFile, extension)) 
		{
			chosen_option = getStringFromScanner("El file introduït no té una extensió .properties. Crear-ne un de nou? (y|N): ");
			if(chosen_option.matches("y|Y"))
			{
				pathFile = createSetExtensionFile(pathFile, extension);
				hb.loadRoomDefaultValues();
				hb.writeProperties(pathFile);
				hb.readProperties(pathFile);
				
				moblesFile = createSetExtensionFile(pathFile,"dat");
				hb.readMobles(moblesFile);
			}
			else
			{
				option = 0;
				System.out.println("Closing\n");
			}
		}
		else
		{
			hb.readProperties(pathFile);
			moblesFile = createSetExtensionFile(pathFile,"dat");
			hb.readMobles(moblesFile);
		}
		
		
		while(option != 0) 
		{
			chosen_option = printMenu();
			if(chosen_option.matches("0|1|2|3|4"))
			{
				option = Integer.parseInt(chosen_option);
				switch(option)
				{
					case 1:
						System.out.println(hb.toString());
						break;				
				
					case 2: 
						hb.addNewMoble();
						break;
						
					case 3:
						hb.writeProperties(pathFile);
						
						if(0 < hb.getMobles().size())
						{
							hb.writeMobles(moblesFile);
						}
						break;
						
					case 4:
						hb.llistaMobles();
						break;
						
					case 0:
						System.out.println("Closing...\n");		
						break;
						
					default: 
						System.out.println("Wrong option!\n");
				}
			}
			else
			{
				System.out.println("Wron option ("+option+")\n");
			}
			
		};
	}
	
	
	//print main menu, ask for an option and return the chosen option as String
	public static String printMenu() 
	{
		System.out.println("------ GESTIÓ HABITACIÓ ------");
		System.out.println("1 - Mostrar per pantalla les dades de l'habitació: ");
		System.out.println("2 - Afegir un moble a l'habitació: ");
		System.out.println("3 - Guardar a disc l'habitació: ");
		System.out.println("4 - Llistar per pantalla tots els mobles de l'habitació, amb totes les seves dades: ");
		System.out.println("0 - Sortir");
		
		return getStringFromScanner("Introdueix una opció: ");
	}
	
	
	//use a Scanner and return the content as String
	public static String getStringFromScanner(String text)
	{
		System.out.println(text);
		Scanner sc = new Scanner(System.in);
		return sc.next();
	}

	//return an instance of a DataOutputStream object 	
	public static DataOutputStream getDOSFromFile(String pathFile)
	{
		DataOutputStream dos = null;
		
		try 
		{
			dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(pathFile)));
		}
		catch(Exception e)
		{
			System.out.println("No DOS -- ERROR\n");
		}
		
		return dos;
	}

	//return an instance of a DataInputStream object 
	public static DataInputStream getDISFromFile(String pathFile)
	{
		DataInputStream dis = null;
		
		try 
		{
			dis = new DataInputStream(new BufferedInputStream(new FileInputStream(pathFile)));
		}
		catch(Exception e)
		{
			System.out.println("No DIS -- ERROR\n");
		}
		
		return dis;
	}	
	

	//check if file has the set extension
	public static boolean checkFileExtension(String pathFile, String extension)
	{
		boolean right = false;
		File f = new File(pathFile);
		
		String[] fullFileName = f.getName().split("\\.",2);
		
		if(fullFileName[fullFileName.length-1].matches(extension))
		{
			right = true;
		}
		
		return right;
	}
	
	
	//create a file with the set extension
	public static String createSetExtensionFile(String pathFile, String extension)
	{
		File f = new File(pathFile);
		String[] fullFileName = f.getName().split("\\.",2);
		File nf = new File(f.getParent()+"/"+fullFileName[0]+"."+extension);
		
		return nf.getAbsolutePath(); 
	}	
		
	
}


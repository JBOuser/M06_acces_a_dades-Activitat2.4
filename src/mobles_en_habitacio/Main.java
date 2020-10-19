package mobles_en_habitacio;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Scanner;

public class Main {

	private static Habitacio hb = new Habitacio();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String pathFile = getStringFromScanner("Introdueix el path del File de l'Habitació: ");;
		hb.openRoom(pathFile);
		int option = 1;
		String chosen_option = "";

		do {
			chosen_option = printMenu();
			if(chosen_option.matches("0|1|2|3|4"))
			{
				option = Integer.parseInt(chosen_option);
				switch(option)
				{
					case 1:
						hb.readRoom();					
						break;				
				
					case 2: 
						hb.addNewMoble();
						break;
						
					case 3:
						hb.writeRoom(getDOSFromFile(pathFile));
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
			
		}while(option != 0);
		
	}
	
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
	
	
	public static String getStringFromScanner(String text)
	{
		System.out.println(text);
		Scanner sc = new Scanner(System.in);
		return sc.next();
	}

	
	public static DataOutputStream getDOSFromFile(String pathFile)
	{
		DataOutputStream dos = null;
		
		try 
		{
			dos = new DataOutputStream(new FileOutputStream(pathFile));
		}
		catch(Exception e)
		{
			System.out.println("No DOS -- ERROR\n");
		}
		
		return dos;
	}

	
	public static DataInputStream getDISFromFile(String pathFile)
	{
		DataInputStream dis = null;
		
		try 
		{
			dis = new DataInputStream(new FileInputStream(pathFile));
		}
		catch(Exception e)
		{
			System.out.println("No DIS -- ERROR\n");
		}
		
		return dis;
	}	
	
	
	
}


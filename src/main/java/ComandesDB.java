import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

public class ComandesDB {

	//EXERCICI 5
	public void createNewComanda()
	{
		Logger.getLogger("org.hibernate").setLevel(Level.OFF);		
		StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
		SessionFactory sessionFactory = null;
		Session session = null;
		
		Scanner sc = new Scanner(System.in);			
		
		Comandes comanda5 = null;

		try {
			Map<Integer, Clients> allClients = new LinkedHashMap<Integer, Clients>();
			Clients chosenClient = null;
			// String nameStartWith = "Sa";

			sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
			session = sessionFactory.openSession();
			session.beginTransaction();

			System.out.println("Selecciona un dels clients: ");
			String allClientsQuery = "from Clients"; // select all
			Query queryResult = session.createQuery(allClientsQuery);
			List<Clients> matchedClients = queryResult.list();

			if (0 < matchedClients.size()) {
				// print all clients
				int clientLinkedNumber = 1;
				String chosenClientOption = "";
				String allowedValues = "";
				for (Clients client : matchedClients) {
					allowedValues += String.valueOf(clientLinkedNumber) + "|";
					allClients.put(clientLinkedNumber, client);
					System.out
							.println(clientLinkedNumber + ") " + client.getDni() + " - " + client.getNom());
					clientLinkedNumber++;
				}
				;

				do {
					System.out.print("option (available " + allowedValues + ")?: ");
					chosenClientOption = sc.next();
				} while (!chosenClientOption.matches(allowedValues));

				// return a matched Clients instance
				chosenClient = allClients.get(Integer.parseInt(chosenClientOption));

				// request the new Comandes num_comanda, preu and data values
				String string_aux_5 = "";
				String num_comanda = "";
				float preu_total = 0f;
				Date data = null;

				// create a new Comandes with got data
				comanda5 = new Comandes();

				// set num_comanda
				do {
					System.out.print("Introdueix el número de comanda (8 digits): ");
					string_aux_5 = sc.next();

				} while (!(string_aux_5.length() == 8) && !string_aux_5.matches("^[0-9]{8}$"));
				comanda5.setNumComanda(string_aux_5);

				string_aux_5 = "";
				// set preu
				do {
					System.out.print("Introdueix el preu (digit p.e. 63.9): ");
					string_aux_5 = sc.next();

				} while (!(string_aux_5.length() == 8) && !string_aux_5.matches("^[0-9]+\\.[0-9]+$"));
				comanda5.setPreuTotal(Float.parseFloat(string_aux_5));

				string_aux_5 = "";
				// set data
				do {
					System.out.print("Introdueix la data (format 09-06-2001): ");
					string_aux_5 = sc.next();

				} while (!(string_aux_5.length() == 8)
						&& !string_aux_5.matches("^[0-9]{2}-[0-9]{2}-[0-9]{4}$"));
				
				// Hibernate does not map Java's Date(year, month, day) to MySQL's Date(). It is better to 
				//parse an String date as a SimpleDateFormat and cast a parsed DateFormat with this 
				//SimpleDateFormat as Date
				String ownDate = string_aux_5;
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				comanda5.setData((Date) dateFormat.parse(ownDate));
				
				//save the client linked
				comanda5.setClients(chosenClient);
				session.save(comanda5);
				System.out.println("Comanda '"+comanda5.getNumComanda()+"' saved");

			} else {
				System.out.println("No Clients");
			}
			session.getTransaction().commit();
		} 
		
		catch (Exception e) {
			System.out.println("Comanda not saved");
			session.getTransaction().rollback();
			System.out.println(e);
		}
		
		finally {
			if (session != null) {
				session.close();
			}

			if (sessionFactory != null) {
				sessionFactory.close();
			}

			if (registry != null) {
				StandardServiceRegistryBuilder.destroy(registry);
			}
		}		
	}
	
}

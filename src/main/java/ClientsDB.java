import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.PersistenceException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;

public class ClientsDB {


	// EXERCISE 1
	public void removeAClient()
	{
		Logger.getLogger("org.hibernate").setLevel(Level.OFF);
		StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
		SessionFactory sessionFactory = null;
		Session session = null;
		
		Scanner sc = new Scanner(System.in);
		Map<Integer, Clients> allClients = new LinkedHashMap<Integer, Clients>();

		try {
			sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

			session = sessionFactory.openSession();
			session.beginTransaction();

			Clients chosenClient = null;
			System.out.println("Selecciona un dels clients a esborrar: ");

			String allClientsQuery = "from Clients"; // select all
			Query queryResult = session.createQuery(allClientsQuery);
			List<Clients> matchedClients = queryResult.list();

			// print all clients
			int clientLinkedNumber = 1;
			for (Clients client : matchedClients) {
				allClients.put(clientLinkedNumber, client);
				System.out.println(clientLinkedNumber + ") " + client.getDni() + " - " + client.getNom());
				clientLinkedNumber++;
			}
			;
			System.out.print("option: ");

			// choose an option among got clients
			String chosenClientOption = sc.next();
			try {
				if (allClients.containsKey(Integer.parseInt(chosenClientOption))) {
					// return a matched Clients instance
					chosenClient = allClients.get(Integer.parseInt(chosenClientOption));

					queryResult = session.createQuery("from Clients C where C.dni = :dni");
					queryResult.setParameter("dni", chosenClient.getDni());
					matchedClients = queryResult.list();

					if (0 < matchedClients.size()) {
						Iterator<Clients> iterator = matchedClients.iterator();
						while (iterator.hasNext()) {

							Clients clientToDelete = iterator.next();
							session.delete(clientToDelete);
							System.out.println("Client '" + clientToDelete.getDni() + "' deleted");
						}
					} else {
						System.out.println("No Clients found with dni '" + chosenClient.getDni() + "'");
					}
				} else {
					System.out.println("Wrong option '" + chosenClientOption + "'");
				}
			} catch (Exception e) {
				System.out.println(e);
			}

			//make a commit of changes
			session.getTransaction().commit();
			
		} 
		
		catch (Exception e) 
		{
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
	
	
	// EXERCISE 2
	public void updateAClient()
	{
		Logger.getLogger("org.hibernate").setLevel(Level.OFF);
		StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
		SessionFactory sessionFactory = null;
		Session session = null;
		
		Scanner sc = new Scanner(System.in);

		try {
			sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

			session = sessionFactory.openSession();
			session.beginTransaction();

			Map<Integer, Clients> allClients = new LinkedHashMap<Integer, Clients>();
			Clients chosenClient = null;
			System.out.println("Selecciona un dels clients a esborrar: ");

			String allClientsQuery = "from Clients"; // select all
			Query queryResult = session.createQuery(allClientsQuery);
			List<Clients> matchedClients = queryResult.list();

			// print all clients
			int clientLinkedNumber = 1;
			for (Clients client : matchedClients) {
				allClients.put(clientLinkedNumber, client);
				System.out.println(clientLinkedNumber + ") " + client.getDni() + " - " + client.getNom());
				clientLinkedNumber++;
			}
			;
			System.out.print("option: ");

			// choose an option among got clients

			String chosenClientOption = sc.next();
			try {
				if (allClients.containsKey(Integer.parseInt(chosenClientOption))) {
					// return a matched Clients instance
					chosenClient = allClients.get(Integer.parseInt(chosenClientOption));

					// set chosen Clients new nom
					String string_aux = "";
					do {
						System.out.print(
								"Introdueix el nou nom del client (1 caràcter min - 30 caràcters max): ");
						string_aux = sc.next();
					} while (!(0 < string_aux.length()) && !(string_aux.length() <= 30)
							&& !string_aux.matches("^[\\w|\\_|\\'|\\-|\\s]{1,30}$"));

					chosenClient.setNom(string_aux);

					// set Clients instance's name
					do {
						System.out.print("Introdueix de nou si el client és premium o no (true|false): ");
						string_aux = sc.next();
					} while (!(0 < string_aux.length()) && !(string_aux.length() <= 30)
							&& !string_aux.matches("^[true|false]$"));

					chosenClient.setPremium(Boolean.parseBoolean(string_aux));

					queryResult = session.createQuery(
							"update Clients as C set nom = :nom, premium = :premium where C.dni = :dni");
					queryResult.setParameter("nom", chosenClient.getNom());
					queryResult.setParameter("premium", chosenClient.getPremium());
					queryResult.setParameter("dni", chosenClient.getDni());
					int rowsUpdated = queryResult.executeUpdate();

					if (0 < rowsUpdated) {
						System.out.print("Clients '" + chosenClient.getDni() + "' updated");
						System.out.print("Rows updated: " + rowsUpdated);
					} else {
						System.out.print("No rows updated");
					}
				} else {
					System.out.println("Wrong option '" + chosenClientOption + "'");
				}
			} catch (Exception e) {
				System.out.println(e);
			}

			session.getTransaction().commit();
		} 
		
		catch (Exception e) 
		{
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
	
	
	//EXERCICI 3
	public void getClientStartsWith()
	{
		Logger.getLogger("org.hibernate").setLevel(Level.OFF);		
		StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
		SessionFactory sessionFactory = null;
		Session session = null;
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println(
				"Introdueix un nom o la part amb que la que pot començar el nom d'un o més clients:");
		String nomStartWith = sc.next();
		// String nameStartWith = "Sa";

		try {
			sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

			session = sessionFactory.openSession();
			session.beginTransaction();

			String nomStartWithQuery = "from Clients as c where c.nom like :nomStartWith";
			Query queryResult = session.createQuery(nomStartWithQuery);
			queryResult.setParameter("nomStartWith", nomStartWith + "%");
			List<Clients> matchedClients = queryResult.list();

			if (0 < matchedClients.size()) {
				for (Clients client : matchedClients) {
					System.out.println(client.getNom() + " : " + client.getDni());
				}
				;
				System.out.print("");
			} else {
				System.out.print("No Clients matched");
			}

			session.getTransaction().commit();
		} 
		
		catch (Exception e) 
		{
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
	
	
	//EXERCICI 4
	public void createNewClient()
	{
		Logger.getLogger("org.hibernate").setLevel(Level.OFF);		
		StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
		SessionFactory sessionFactory = null;
		Session session = null;
		
		Scanner sc = new Scanner(System.in);		
		
		System.out.println("\nCreació d'un nou Client");
		Clients client4 = new Clients();

		// set Clients instance's dni
		String string_aux_4;
		do {
			System.out.print("Introdueix el dni del client (8 digits): ");
			string_aux_4 = sc.next();
		} while (!(string_aux_4.length() == 8) && !string_aux_4.matches("^[0-9]{8}$"));
		client4.setDni(string_aux_4);

		// set Clients instance's name
		do {
			System.out.print("Introdueix el nom del client (1 caràcter mín - 30 caràcters max): ");
			string_aux_4 = sc.next();
		} while (!(0 < string_aux_4.length()) && !(string_aux_4.length() <= 30)
				&& !string_aux_4.matches("^[\\w|\\_|\\'|\\-|\\s]{1,30}$"));
		client4.setNom(string_aux_4);

		// set Clients instance's premium
		do {
			System.out.print("Introdueix si el client és premium o no (true|false): ");
			string_aux_4 = sc.next();
		} while (!string_aux_4.matches("^(true|false)$"));
		client4.setPremium(Boolean.parseBoolean(string_aux_4));

		registry = new StandardServiceRegistryBuilder().configure().build();
		sessionFactory = null;
		session = null;

		try {
			sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

			session = sessionFactory.openSession();
			session.beginTransaction();

			// save the client on DB
			session.save(client4);

			session.getTransaction().commit();
			// session.flush();
			session.close();
			sessionFactory.close();

			System.out.print("Added '" + client4.toString() + "'");
		} 					
		
		catch (Exception e) {
			session.getTransaction().rollback();
			System.out.println(e);
			
		} finally {
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
	
	
	//EXERCICI 6
	public void showAClientComandes()
	{
		Logger.getLogger("org.hibernate").setLevel(Level.OFF);		
		StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
		SessionFactory sessionFactory = null;
		Session session = null;
		
		Scanner sc = new Scanner(System.in);	

		try {
			sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
			session = sessionFactory.openSession();
			session.beginTransaction();

			Map<Integer, Clients> allClients = new LinkedHashMap<Integer, Clients>();
			Clients chosenClient = null;
			System.out.println("Selecciona un dels clients: ");
			// String nameStartWith = "Sa";

			String allClientsQuery = "from Clients"; // select all
			Query queryResult = session.createQuery(allClientsQuery);
			List<Clients> matchedClients = queryResult.list();

			if (0 < matchedClients.size()) {
				// print all clients
				int clientLinkedNumber = 1;
				for (Clients client : matchedClients) {
					allClients.put(clientLinkedNumber, client);
					System.out
							.println(clientLinkedNumber + ") " + client.getDni() + " - " + client.getNom());
					clientLinkedNumber++;
				}
				;
				System.out.print("option: ");

				// choose an option among got clients
				String chosenClientOption = sc.next();
				try {
					if (allClients.containsKey(Integer.parseInt(chosenClientOption))) {
						// return a matched Clients instance
						chosenClient = allClients.get(Integer.parseInt(chosenClientOption));
					}
				} catch (Exception e) {
					System.out.println("Wrong option: " + chosenClientOption);
				}

				// get comandes from chosen client
				if (chosenClient != null) {
					String clientComandesQuery = "from Comandes where dni_client = :dni_client"; // select
																									// all
					Query queryClientComandesResult = session.createQuery(clientComandesQuery);
					queryClientComandesResult.setParameter("dni_client", chosenClient.getDni());
					List<Comandes> matchedClientComandes = queryClientComandesResult.list();

					if (0 < matchedClientComandes.size()) {
						System.out.println("\nComandes of "+chosenClient.getDni()+": ");
						for (Comandes comanda : matchedClientComandes) {
							System.out.println(comanda.toString());
							System.out.println("------------");
						}
					} else {
						System.out.println("Client '" + chosenClient + "' has no Comandes");
					}
				}
			} else {
				System.out.print("No Clients");
			}

			session.getTransaction().commit();
		} 
		
		catch (Exception e) 
		{
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
	
	
	//EXERCICI 7
	public void showAllFacturedByClient()
	{
		Logger.getLogger("org.hibernate").setLevel(Level.OFF);		
		StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
		SessionFactory sessionFactory = null;
		Session session = null;		
		
		try {
			sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

			session = sessionFactory.openSession();
			session.beginTransaction();

			// 1.0
			// String totalFacturacioClient = "from Comandes where dni_client = 12345678";

			// 2.0
			String totalFacturacioClient = "from Comandes";
			Query queryResult = session.createQuery(totalFacturacioClient);

			List<Comandes> matchedClientComandes = queryResult.list();
			// List<?> matchedClientComandes = queryResult.list();

			Map<String, Float> totalFacturacioPerClient = new HashMap<String, Float>();
			Map<Float, String> totalFacturacioPerClientOrderedDescendantly = new TreeMap<Float, String>(
					Collections.reverseOrder());
			for (Comandes comanda : matchedClientComandes) {
				if (totalFacturacioPerClient.containsKey(comanda.getClients().getDni())) {
					Float preu_total = totalFacturacioPerClient.get(comanda.getClients().getDni())
							+ comanda.getPreuTotal();
					totalFacturacioPerClient.put(comanda.getClients().getDni(), preu_total);
				} else {
					totalFacturacioPerClient.put(comanda.getClients().getDni(), comanda.getPreuTotal());
				}
			}

			for (Map.Entry<String, Float> row : totalFacturacioPerClient.entrySet()) {
				totalFacturacioPerClientOrderedDescendantly.put(row.getValue(), row.getKey());
			}

			System.out.println("\nResum facturació de tots els clients");
			for (Map.Entry<Float, String> row : totalFacturacioPerClientOrderedDescendantly.entrySet()) {
				System.out.println(row.getKey() + " : " + row.getValue());
			}
		} 
		
		catch (Exception e) 
		{
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

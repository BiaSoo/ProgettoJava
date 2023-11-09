package Agenda;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import jbook.util.Input;

/**
 * Interfaccia è la classe che si occupa di far interagire l'intero codice con
 * l'utente, permette inoltre di effettuare operazioni sulle agende e sui
 * singoli appuntenti.
 * 
 * @author Guido Lorenzo Broglio 20043973, Gabriele Magenta Biasina 20044231
 */
public class Interfaccia {
	/**
	 * Questo è il metodo main che nel codice permette di lanciare il programma e
	 * consente di inizializzare tutti i valori necessari nei case sottostanti
	 * 
	 * @param args
	 * @throws AppuntamentoException
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public static void main(String[] args) throws NumberFormatException, AppuntamentoException {
		String file_path = "agenda.txt";

		do {
			System.out.println("""
					MENU OPERAZIONI:
					\n1 Creazione nuova agenda
					\n2 Cancellazione agenda
					\n3 Creazione nuova agenda da file
					\n4 Scrittura agenda su file
					\n5 Visualizzazione agende
					\n6 Modifica agenda
					\n7 Uscita dal programma""");

			System.out.print("Effettua una scelta: ");

			switch (Input.readString()) {
			/**
			 * Case 1 utilizzato per la creazione di un'agenda dato in input da tastiera il
			 * nome dell'agenda
			 */
			case "1":
				System.out.println("CREAZIONE AGENDA\n");
				System.out.print("Inserisci il nome dell'agenda: ");
				try {
					String nome = Input.readString();
					Manager.creaAgenda(nome);
					System.out.println("Agenda creata con successo.");
				} catch (ManagerException e) {
					System.out.println("ERRORE: " + e.getMessage());
				}
				break;

			/**
			 * Case 2 viene utilizzato per la cancellazione di un'agenda, anche se questa ha
			 * al suo interno degli appuntamenti. Prende sempre il nome dell'agenda da
			 * tastiera
			 */
			case "2":
				System.out.println("CANCELLAZIONE AGENDA\n");
				System.out.print("Inserisci il nome dell'agenda da cancellare: ");
				try {
					Manager.cancellaAgenda(Input.readString());
					System.out.println("Agenda cancellata con successo.");
				} catch (ManagerException e) {
					System.out.println("ERRORE: " + e.getMessage());
				}
				break;

			/**
			 * Case 3 Utilizzato per la creazione di una nuova agenda da file di testo
			 * Vengono chiesti in input il percorso in cui è salvato il file e il nome della
			 * nuova agenda
			 */
			case "3":
				System.out.println("CREAZIONE AGENDA DA FILE\n");
				System.out.print("Inserisci il percorso del file: ");
				String filePath = Input.readString();
				System.out.print("Inserisci il nome dell'agenda: ");
				String nuovo = Input.readString();
				try {
					Manager.creaDaFile(filePath, nuovo);
					System.out.println("Agenda creata da file con successo.");
				} catch (IOException | AgendaException | AppuntamentoException | ManagerException e) {
					System.out.println("ERRORE: " + e.getMessage());
				}
				break;

			/**
			 * Case 4 utilizzato per scrivere un'agenda già esistente sul file di testo
			 * prende in input il nome dell'agenda che si desidera salvare su file
			 */
			case "4":
				System.out.println("SCRITTURA AGENDA SU FILE\n");
				System.out.print("Inserisci il nome dell'agenda: ");
				String nome = Input.readString();

				if (nome != "") {
					try {
						Agenda agenda = ricercaAgenda(nome);
						agenda.scriviSuFile(file_path);
					} catch (ManagerException e) {
						System.out.println(e.getMessage());
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					System.out.println("Agenda non trovata!");
				}
				break;

			/**
			 * Case 5 utilizzato per la visualizzazione del numero di agende presenti
			 */
			case "5":
				System.out.println("VISUALIZZAZIONE AGENDE\n");
				int numeroAgendeVisualizzazione = Manager.numeroAgende();
				if (numeroAgendeVisualizzazione <= 0) {
					System.out.println("Nessuna agenda presente.");
				} else {
					for (Agenda a : Manager.manager) {
						System.out.println(a.toString());
					}
				}
				break;
			/**
			 * Case 6 permette di modificare l'agenda dando in input da tastiera il suo nome
			 */
			case "6":
				modificaAgenda();
				break;
			/**
			 * Case 7 consente di uscire dal programma
			 */
			case "7":
				System.out.println("USCITA DAL PROGRAMMA\n");
				return;

			default:
				System.out.println("ERRORE La scelta inserita non è valida!");
				break;
			}
		} while (true);
	}

	private static void modificaAgenda() {
		Agenda agenda;

		System.out.println("MODIFICA AGENDA\n");
		try {
			controlloEsistenzaAgende();
		} catch (ManagerException e) {
			System.out.println(e.getMessage());
			return;
		}

		try {
			System.out.println("Su quale agenda vuoi apportare modifiche?");
			agenda = ricercaAgenda(Input.readString());
		} catch (ManagerException e) {
			System.out.println(e.getMessage());
			return;
		}

		System.out.println("Stai modificando l'agenda " + agenda.getNome());

		/**
		 * E' presente un sotto-menù utilizzato per descrivere all'utente le operazioni
		 * che è possibile fare su un'agenda
		 */
		do {
			System.out.println("""
					MENU OPERAZIONI:
					\n1 Aggiunta appuntamento
					\n2 Eliminazione appuntamento
					\n3 Modifica appuntamento
					\n4 Visualizzazione agenda
					\n5 Uscita
					""");

			System.out.println("Inserire l'operazione che si desidera effettuare");

			switch (Input.readString()) {
			/**
			 * Case 1 permette l'aggiunta di un appuntamento dati in input ora appuntamento,
			 * data appuntamento, durata appuntamento,nome della persona, luogo
			 * dell'appuntamento
			 */
			case "1":
				String ora, data, durata, nomePersona, luogo;
				System.out.println("AGGIUNTA APPUNTAMENTO\n");
				do {
					ora = Input.readString("Inserisci l'orario (format: hh:mm): ");
				} while (!ora.matches("([01][0-9]|2[0-3]):([0-5][0-9])"));
				do {
					data = Input.readString("Inserisci la data (format: yyyy-mm-dd): ");
				} while (!data.matches("\\d{4}-(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])"));
				do {
					durata = Input.readString("Inserisci la durata in minuti: ");
				} while (!durata.matches("^\\d+$"));

				nomePersona = Input.readString("Inserisci il nome della persona: ");
				luogo = Input.readString("Inserisci il luogo: ");

				try {
					agenda.inserisciAppuntamento(
							new Appuntamento(ora, data, Integer.parseInt(durata), nomePersona, luogo));
				} catch (DateTimeParseException e) {
					System.out.println(e.getMessage());
				} catch (AgendaException e) {
					System.out.println(e.getMessage());
				} catch (NumberFormatException e) {
					System.out.println(e.getMessage());
				} catch (AppuntamentoException e) {
					System.out.println(e.getMessage());
				}
				System.out.println("Appuntamento aggiunto correttamente all'agenda " + agenda.getNome() + ".");
				break;

			/**
			 * Case 2 utilizzato per la cancellazione di un appuntamento dato in input il
			 * nome della persona dell'appuntamento
			 */
			case "2":
				System.out.println("ELIMINAZIONE APPUNTAMENTO\n");
				System.out.println("Selezionare l'appuntamento che si desidera eliminare: ");

				int i = 0;
				for (Appuntamento a : agenda) {
					System.out.println(i + ". " + a.toString());
					i++;
				}

				try {
					agenda.cancellaAppuntamento(Input.readInt());
				} catch (AgendaException e) {
					System.out.println("Appuntamento non rimosso!");
					System.out.println("ERRORE: " + e.getMessage());
				}
				break;

			/**
			 * Case 3 contiene tutti i metodi per la modifica di un appuntamento all'interno
			 * di una agenda
			 */
			case "3":
				modificaAppuntamento(agenda);

				/**
				 * Case 4 consente di visualizzare gli appuntamenti presenti in un'agenda
				 */
			case "4":
				System.out.println("VISUALIZZAZIONE AGENDA\n");
				System.out.println("L'agenda ha " + agenda.numeroAppuntamento() + " appuntamenti");

				if (agenda.numeroAppuntamento() == 0) {
					System.out.println("Non risultano appuntamenti nell'agenda selezionata!");
					break;
				}

				for (Appuntamento a : agenda) {
					System.out.println(a.toString());
				}
				break;
			/**
			 * Case 5 premette di ritornare al menù crecedente
			 */
			case "5":
				System.out.println("USCITA\n");
				return;

			default:
				System.out.println("ERRORE La scelta inserita non è valida!");
				break;
			}
		} while (true);
	}

	private static void modificaAppuntamento(Agenda agenda) {
		if (agenda.numeroAppuntamento() != 0) {
			System.out.println("MODIFICA APPUNTAMENTO\n");

			
			int i = 0, indice=0;
			for (Appuntamento a : agenda) {
				System.out.println(i + ". " + a.toString());
				i++;
			}
			try{
				indice = Input.readInt("Inserisci l'indice corrispondente all'appuntamento da modificate: ");
			}
			catch(Exception e)
			{
				System.out.println("Inserire un indice numerico da 0 :");
			}
			

			System.out.println("""
					MENU OPERAZIONI APPUNTAMENTI:
					\n1 Modifica data
					\n2 Modifica ora
					\n3 Modifica durata
					\n4 Modifica luogo
					\n5 Modifica nome
					\n6 Uscita
					""");

			try{
				String scelta=Input.readString("Inserisci l' operazione che si desidera effettuare: ");
			
			
				switch (scelta) {
				/**
				 * Case 1 permette di modificare la data di un appuntamento esistente
				 */
				case "1":
					System.out.println("MODIFICA DATA\n");
					try {
						String giorno= Input.readString("Inserisci la nuova data (di tipo yyyy-mm-dd): ");
						agenda.modificaGiorno(indice, giorno);
					} catch (AgendaException e) {
						e.printStackTrace();
					}
					break;

				/**
				 * Case 2 consente di modificare l'ora di un appuntamento
				 */
				case "2":
					System.out.println("MODIFICA ORA\n");
					try {
						String orario= Input.readString("Inserisci il nuovo orario (di tipo hh:mm): ");
						agenda.modificaOrario(indice, orario);
					} catch (AgendaException e) {
						e.printStackTrace();
					}
					break;

				/**
				 * Case 3 permette di modificare la durata di un appuntamento
				 */
				case "3":
					System.out.println("MODIFICA DURATA\n");
					try {
						int durata=Input.readInt("Inserisci la nuova durata (di tipo min): ");
						agenda.modificaDurata(indice, durata);
					} catch (AgendaException e) {
						e.printStackTrace();
					}
					break;

				/**
				 * Case 4 consente di modificare il luogo dove avrà sede l'appuntamento
				 */
				case "4":
					System.out.println("MODIFICA LUOGO\n");
					try {
						String luogo=Input.readString("Inserisci il nuovo luogo: ");
						agenda.modificaLuogo(indice,luogo); 
					} catch (AgendaException e) {
						e.printStackTrace();
					}
					break;

				/**
				 * Case 5 permette di modificare il nome di chi sarà presente all'appuntamento
				 */
				case "5":
					System.out.println("MODIFICA NOME\n");
					try {
						String nome= Input.readString("Inserisci il nuovo nome: ");
						agenda.modificaNome(indice,nome);
					} catch (AgendaException e) {
						e.printStackTrace();
					}
					break;

				/**
				 * case 6 consente di tornare al menù precedente
				 */
				case "6":
					System.out.println("USCITA\n");
					return;
				}
			}
			catch(AppuntamentoException e)
			{
				System.out.println(e.getMessage());
			}
		} else {
			System.out.println("Nessun appuntamento trovato!");
		}
	}

	/**
	 * Questo metodo controlla l'esistenza dell'agenda
	 * 
	 * @param nome
	 * @return
	 */
	private static void controlloEsistenzaAgende() throws ManagerException {
		if (Manager.manager.isEmpty()) {
			throw new ManagerException("Nessuna agenda presente, creane una.");
		}
	}

	/**
	 * Questo metodo permette di cercare l'agenda dando in input il suo nome
	 * 
	 * @param nome
	 * @return
	 * @throws ManagerException
	 */
	private static Agenda ricercaAgenda(String nome) throws ManagerException {
		for (Agenda agenda : Manager.manager) {
			if (agenda.getNome().equals(nome)) {
				return agenda;
			}
		}
		throw new ManagerException("Agenda non trovata.\n");
	}
}
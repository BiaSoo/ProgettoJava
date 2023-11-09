package Agenda;

import java.util.*;
import java.io.*;
import java.time.*;

/**
 * La classe Agenda e' la classe responsabile della gestione degli appuntamenti.
 * Contiene tutte le informazioni più importanti come il nome dell'agenda ed i
 * dati contenuti negli appuntamenti Sono presenti dei metodi che vengono
 * utilizzati per la gestione degli appuntamenti come la cancellazione,
 * l'inserimento e l'aggiornamento.
 * 
 * @author Guido Lorenzo Broglio 20043973, Gabriele Magenta Biasina 20044231
 */
public class Agenda implements Iterable<Appuntamento> {
	public final String nome;
	private ArrayList<Appuntamento> appuntamenti;
	private static int numeroAgende = 0;

	/**
	 * Il metodo crea un oggetto Agenda con il nome e la lista di appuntamenti
	 * specificata.
	 * 
	 * @param nome         è il nome dell'agenda.
	 * @param appuntamenti è la lista di appuntamenti dell'agenda.
	 */
	public Agenda(String nome) {
		this.nome = nome;
		appuntamenti = new ArrayList<Appuntamento>();
		numeroAgende++;
	}

	/**
	 * Il metodo crea un oggetto Agenda con il nome specificato e una lista vuota di
	 * appuntamenti.
	 * 
	 * @param nome è il nome dell'Agenda.
	 */
	public Agenda() {
		this("Agenda " + numeroAgende);
	}

	/**
	 * Il metodo restituisce il nome dell'agenda.
	 * 
	 * @return il nome dell'agenda.
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Il metodo restituisce un appuntamento dato l'indice.
	 * 
	 * @return l'elenco degli appuntamenti dell'agenda.
	 */
	public Appuntamento getAppuntamento(int indice) {
		return appuntamenti.get(indice);
	}

	/**
	 * Il metodo restituisce la lista degli appuntamenti.
	 * 
	 * @return l'elenco degli appuntamenti dell'agenda.
	 */
	public ArrayList<Appuntamento> getListaAppuntamenti() {
		return appuntamenti;
	}


	/**
	 * Il metodo restituisce il numero di appuntamenti presenti nell'agenda.
	 * 
	 * @return il numero di appuntamenti presenti
	 */
	public int numeroAppuntamento() {
		return appuntamenti.size();
	}

	/**
	 * Il metodo scrive l'appuntamento dell'agenda su un file specificato.
	 * 
	 * @param filePath e' il percorso del file su cui scrivere gli appuntamenti.
	 * @throws IOException viene utilizzato nel caso in cui avviene un errore
	 *                     durante la scrittura del file.
	 */
	public void scriviSuFile(String filePath) throws IOException {
		try (BufferedWriter buff = new BufferedWriter(new FileWriter(filePath))) {
			for (Appuntamento app : appuntamenti) {
				buff.write(app.getOrario() + ";" + app.getGiorno() + ";" + app.getDurata() + ";" + app.getNome() + ";"
						+ app.getLuogo());
				buff.newLine();
			}
		} catch (IOException e) {
			throw new IOException("Errore durante la scrittura del file.", e);
		}
	}

	/**
	 * Il metodo inserisce un nuovo appuntamento nell'Agenda.
	 * 
	 * @param newAppuntamento e' il nuovo appuntamento da inserire.
	 * @throws AgendaException viene utilizzato se l'appuntamento si sovrappone con
	 *                         uno già presente in Agenda.
	 */
	public void inserisciAppuntamento(Appuntamento newAppuntamento) throws AgendaException {
		controlloConflitti(newAppuntamento);
		appuntamenti.add(newAppuntamento);
		appuntamenti.sort((appuntamento1,appuntamento2) -> appuntamento1.compara(appuntamento2));
	}

	/**
	 * Il metodo controlla eventuali conflitti nell'Agenda.
	 * 
	 * @param appuntamento e' l'appuntamento da inserire.
	 * @throws AgendaException viene utilizzato se l'appuntamento si sovrappone con
	 *                         uno già presente in Agenda.
	 */
	public void controlloConflitti(Appuntamento appuntamento) throws AgendaException {
		if (appuntamenti.stream().anyMatch(a -> a.controlloSovrapposizione(appuntamento))) {
			throw new AgendaException("Appuntamento in conflitto con uno già presente.");
		}
	}

	/**
	 * Il metodo ricerca gli appuntamenti che corrispondono al nome specificato.
	 * 
	 * @param ricerca e' il nome da cercare negli appuntamenti.
	 * @return una lista di appuntamenti che corrispondono alla ricerca.
	 * @throws AgendaException se non ci sono appuntamenti presenti nell'agenda o se
	 *                         non sono presenti appuntamenti corrispondenti alla
	 *                         ricerca.
	 */
	public List<Appuntamento> ricercaAppuntamentiNome(String ricerca) throws AgendaException {
		List<Appuntamento> risultati = new ArrayList<>();

		if (appuntamenti.isEmpty()) {
			throw new AgendaException("Nessun appuntamento presente nell'agenda.");
		}

		for (Appuntamento appuntamento : this.appuntamenti) {
			if (appuntamento.getNome().equals(ricerca)) {
				risultati.add(appuntamento);
			}
		}

		if (risultati.isEmpty()) {
			throw new AgendaException("Nessun appuntamento trovato.");
		}

		return risultati;
	}

	/**
	 * Il metodo ricerca gli appuntamenti che corrispondono alla data specificata.
	 * 
	 * @param ricerca e' la data da cercare negli appuntamenti. La data ha un
	 *                formato particolare che deve seguire: "dd-MM-yyyy"
	 * @return una lista di appuntamenti che corrispondono alla ricerca.
	 * @throws AgendaException se non ci sono appuntamenti presenti nell'agenda o se
	 *                         non sono presenti appuntamenti corrispondenti alla
	 *                         ricerca.
	 */
	public List<Appuntamento> ricercaAppuntamentiData(String ricerca) throws AgendaException {
		List<Appuntamento> risultati = new ArrayList<>();

		if (appuntamenti.isEmpty()) {
			throw new AgendaException("Nessun appuntamento presente nell'agenda.");
		}

		for (Appuntamento appuntamento : appuntamenti) {

			if (appuntamento.getGiorno().equals(LocalDate.parse(ricerca))) {
				risultati.add(appuntamento);
			}
		}
		if (risultati.isEmpty()) {
			throw new AgendaException("Nessun appuntamento trovato.");
		}

		return risultati;
	}
 
	/**
	 * Modifica l'orario dell'appuntamento all'indice specificato.
	 * 
	 * @param indice dell'appuntamento da modificare.
	 *	
	 * @param orario dell'appuntamento.
	 *
	 * @throws AgendaException Se si verifica un errore durante la modifica dell'appuntamento.
	 *	
	 * @throws AppuntamentoException Se l'indice specificato è invalido.
	 */
	public void modificaOrario(int indice, String orario)throws AgendaException, AppuntamentoException 
	{
			Appuntamento modifica= appuntamenti.get(indice);
			Appuntamento modificato= new Appuntamento(orario, modifica.getGiorno().toString(), modifica.getDurata(), modifica.getNome(), modifica.getLuogo());
			cancellaAppuntamento(indice);
			Aggiunta(modifica,modificato);
	}

	/**
	 * Modifica il giorno dell'appuntamento all'indice specificato.
	 * 
	 * @param indice dell'appuntamento da modificare.
	 * @param giorno dell'appuntamento.
	 * @throws AgendaException Se si verifica un errore durante la modifica dell'appuntamento.
	 * @throws AppuntamentoException Se l'indice specificato è invalido.
	 */
	public void modificaGiorno(int indice, String giorno)throws AgendaException, AppuntamentoException 
	{

			Appuntamento modifica= appuntamenti.get(indice);
			Appuntamento modificato= new Appuntamento(modifica.getOrario().toString(), giorno, modifica.getDurata(), modifica.getNome(), modifica.getLuogo());
			cancellaAppuntamento(indice);
			Aggiunta(modifica,modificato);
		
	}

	/**
	 * Modifica la durata dell'appuntamento all'indice specificato.
	 * 
	 * @param indice dell'appuntamento da modificare.
	 * @param durata dell'appuntamento.
	 * @throws AgendaException Se si verifica un errore durante la modifica dell'appuntamento.
	 * @throws AppuntamentoException Se l'indice specificato è invalido.
	 */
	public void modificaDurata(int indice, int durata)throws AgendaException, AppuntamentoException 
	{
			Appuntamento modifica= appuntamenti.get(indice);
			Appuntamento modificato= new Appuntamento(modifica.getOrario().toString(), modifica.getGiorno().toString(), durata, modifica.getNome(), modifica.getLuogo());
			cancellaAppuntamento(indice);
			Aggiunta(modifica,modificato);
	}

	/**
	 * Modifica il nome dell'appuntamento all'indice specificato.
	 * 
	 * @param indice dell'appuntamento da modificare.
	 * @param nome dell'appuntamento.
	 * @throws AgendaException Se si verifica un errore durante la modifica dell'appuntamento.
	 * @throws AppuntamentoException Se l'indice specificato è invalido.
	 */
	public void modificaNome(int indice, String nome)throws AgendaException, AppuntamentoException 
	{
			Appuntamento modifica= appuntamenti.get(indice);
			Appuntamento modificato= new Appuntamento(modifica.getOrario().toString(), modifica.getGiorno().toString(), modifica.getDurata(), nome, modifica.getLuogo());
			cancellaAppuntamento(indice);
			Aggiunta(modifica,modificato);
	}

	/**
	 * Modifica il luogo dell'appuntamento all'indice specificato.
	 * 
	 * @param indice dell'appuntamento da modificare.
	 * @param luogo dell'appuntamento.
	 * @throws AgendaException Se si verifica un errore durante la modifica dell'appuntamento.
	 * @throws AppuntamentoException Se l'indice specificato è invalido.
	 */
	public void modificaLuogo(int indice, String luogo)throws AgendaException, AppuntamentoException 
	{
			Appuntamento modifica= appuntamenti.get(indice);
			Appuntamento modificato= new Appuntamento(modifica.getOrario().toString(), modifica.getGiorno().toString(), modifica.getDurata(), modifica.getNome(), luogo);
			cancellaAppuntamento(indice);
			Aggiunta(modifica,modificato);
	}


	/**
	 * Il metodo aggiunge un nuovo appuntamento all'agenda.
	 * 
	 * @param modifica e' l'appuntamento originale da modificare.
	 * @param modificato e' il nuovo appuntamento con le modifiche.
	 * @throws AgendaException Se si verifica un errore durante l'aggiunta dell'appuntamento.
	 */
	public void Aggiunta(Appuntamento modifica, Appuntamento modificato)throws AgendaException
	{
		try
		{
			inserisciAppuntamento(modificato);
		}
		catch(AgendaException e)
		{
			appuntamenti.add(modifica);
			appuntamenti.sort((appuntamento1,appuntamento2) -> appuntamento1.compara(appuntamento2));
			throw new AgendaException(e.getMessage());
		}
	}

	/**
	 * Il metodo restituisce una stringa che rappresenta l'oggetto Agenda; inclusi
	 * nome e appuntamenti.
	 * 
	 * @return una stringa che rappresenta l'oggetto Agenda.
	 */
	@Override
	public String toString() {
		return "\nAgenda [nome=" + nome + ", appuntamenti=" + appuntamenti.toString() + "]";
	}

	/**
	 * Il metodo restituisce un iteratore per scorrere gli appuntamenti dell'agenda.
	 * 
	 * @return un iteratore per gli appunti dell'agenda.
	 */
	@Override
	public Iterator<Appuntamento> iterator() {
		return appuntamenti.iterator();
	}

	/**
	 * Il metodo cancella l'appuntamento con il nome specificato, se presente
	 * nell'agenda.
	 * 
	 * @param nome e' il nome dell'appuntamento da cancellare.
	 * @throws AgendaException viene utilizzato questa eccezione quando non viene
	 *                         trovato alcun appuntamento con quel nome.
	 */
	public void cancellaAppuntamento(int indice) throws AgendaException {
		if (numeroAppuntamento() < indice)
			throw new AgendaException("Appuntamento non presente.");
		appuntamenti.remove(indice);
	}
}
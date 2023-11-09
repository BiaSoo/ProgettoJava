package Agenda;

import java.time.*;
import java.time.format.*;

/**
 * La classe Appuntamento ha il compito di gestire tutti i dati che
 * contraddistinguono gli appuntamenti quindi orario, giorno, durata, nome e
 * luogo. Sono presenti anche tutti i formatter per quanto riguarda orario e
 * giorno.
 * 
 * @author Guido Lorenzo Broglio 20043973, Gabriele Magenta Biasina 20044231
 */
public class Appuntamento {
	private final LocalTime orario;
	private final LocalTime orarioFine;
	private final LocalDate giorno;
	private final int durata;
	private final String nome;
	private final String luogo;

	/**
	 * Il metodo crea un nuovo oggetto appuntamento con i valori specificati.
	 * 
	 * @param orario e' l'orario dell'appuntamento nel formato "hh:MM".
	 * @param giorno e' il giorno dell'appuntamento nel formato "dd-MM-yyyy".
	 * @param durata e' la durata dell'appuntamento in minuti.
	 * @param nome   e' il nome della persona che ha l'appuntamento.
	 * @param luogo  e' il luogo dell'appuntamento.
	 * @throws AppuntamentoException se uno dei valori non segue il formato
	 *                               corretto, lancia l'eccezione.
	 */
	public Appuntamento(String orario, String giorno, int durata, String nome, String luogo)
			throws AppuntamentoException {
		controlloFormato(orario, giorno, durata, nome, luogo);

		this.orario = LocalTime.parse(orario);
		this.giorno = LocalDate.parse(giorno);
		this.durata = durata;
		this.orarioFine = LocalTime.parse(orario).plusMinutes(durata);
		this.nome = nome;
		this.luogo = luogo;
	}

	/**
	 * Il metodo verifica se l'appuntamento richiesto si sovrappone con l'altro
	 * appuntamento fornito come parametro.
	 * 
	 * @param altroAppuntamento e' l'appuntamento da controllare per la
	 *                          sovraposizione.
	 * @return true se l'appuntamento si sovrappone, altrimenti false se va bene.
	 * @throws AppuntamentoException viene utilizzato se si verifica un errore
	 *                               durante il controllo.
	 */
	public boolean controlloSovrapposizione(Appuntamento appuntamento) {
		return (appuntamento.getGiorno().equals(this.getGiorno()) && appuntamento.getOrario().equals(this.getOrario())) || (appuntamento.getOrario().isAfter(this.orario) && appuntamento.getOrario().isBefore(this.orarioFine)
						|| appuntamento.getOrarioFine().isAfter(this.orario)
								&& appuntamento.getOrarioFine().isBefore(this.orarioFine)
						|| appuntamento.getOrario().isBefore(this.orario)
								&& appuntamento.getOrarioFine().isAfter(this.orarioFine));
	}

	/**
	 * Il metodo imposta l'orario, il giorno, la durata, il nome e il luogo
	 * dell'appuntamento.
	 * 
	 * @param orario e' il nuovo orario dell'appuntamento nel suo formato "HH-mm".
	 * @param giorno e' il nuovo giorno dell'appuntamento nel suo formato
	 *               "dd-MM-yyyy."
	 * @param durata e' la nuova durata dell'appuntamento in minuti.
	 * @param nome   e' il nuovo nome dell'appuntamento.
	 * @param luogo  e' il nuovo luogo dell'appuntamento.
	 * @throws AppuntamentoException viene lanciata se il formato non è valido.
	 */
	public void controlloFormato(String orario, String giorno, int durata, String nome, String luogo)
			throws AppuntamentoException {
		try {
			LocalDate.parse(giorno);
		} catch (DateTimeParseException e) {
			throw new AppuntamentoException("La data deve essere valida e in formato 'YYYY-MM-DD'.");
		}
		try {
			LocalTime.parse(orario);
		} catch (DateTimeParseException e) {
			throw new AppuntamentoException("L'orario deve essere valido e in formato 'hh:mm'.");
		}
		if (durata <= 0)
			throw new AppuntamentoException("La durata non può essere negativa o zero.");
		if (nome.matches(".*\\d.*"))
			throw new AppuntamentoException("Il nome non può contenere cifre.");
		// il luogo può contere cifre
	}

	/**
	 * Il metodo restituisce l'orario dell'appuntamento.
	 * 
	 * @return l'orario dell'appuntamento.
	 */
	public LocalTime getOrario() {
		return orario;
	}

	/**
	 * Il metodo restituisce l'orario di fine appuntamento.
	 * 
	 * @return l'orario dell'appuntamento.
	 */
	public LocalTime getOrarioFine() {
		return orarioFine;
	}

	/**
	 * Il metodo restituisce il giorno dell'appuntamento
	 * 
	 * @return il giorno dell'appuntamento.
	 */
	public LocalDate getGiorno() {
		return giorno;
	}

	/**
	 * Il metodo restituisce la durata dell'appuntamento.
	 * 
	 * @return la durata dell'appuntamento in minuti.
	 */
	public int getDurata() {
		return durata;
	}

	/**
	 * Il metodo restituisce il nome dell'appuntamento.
	 * 
	 * @return il nome dell'appuntamento.
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Il metodo restituisce il luogo dell'appuntamento.
	 * 
	 * @return il luogo dell'appuntamento.
	 */
	public String getLuogo() {
		return luogo;
	}

	/**
	 * Il metodo restituisce una rappresentazione testuale dell'appuntamento
	 * 
	 * @return una stringa che rappresenta l'appuntamento con i suoi attributi.
	 */
	@Override
	public String toString() {
		return "Appuntamento [Orario= " + orario.toString() + ", Giorno=" + giorno.toString() + ", Durata=" + durata
				+ ", Nome=" + nome + ", Luogo=" + luogo + "]";
	}
	
	/**
	 * Il metodo confronta due appuntamenti in base alla loro fine.
	 * 
	 * @return un intero che rappresenta lo stato della sovrapposizione dei due appuntamenti.
	 */
	public int compara(Appuntamento appuntamento) {
		return this.getOrarioFine().atDate(this.getGiorno()).compareTo(appuntamento.getOrarioFine().atDate(appuntamento.getGiorno()));
	}
}
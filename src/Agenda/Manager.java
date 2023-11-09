package Agenda;

import java.io.*;
import java.util.*;

/**
 * La classe Manager rappresenta un gestore delle agende.
 * Sono presenti alcuni funzioni come la ricerca e la cancellazione.
 * Avviene anche un controllo di esistenza.
 * @author Guido Lorenzo Broglio 20043973, Gabriele Magenta Biasina 20044231
 */
public class Manager 
{
    static final ArrayList<Agenda> manager = new ArrayList<>();

    /**
     * Il metodo verifica se esiste già un'agenda con il nome specificato nel manager.
     * @param nome e' il nome dell'agenda da controllare.
     * @throws ManagerException viene lanciata se esiste già un agenda con quel nome.
     */
    public static void controlloEsistenzaAgenda(String nome) throws ManagerException 
    {
        for (Agenda agenda : manager) 
        {
            if (agenda.getNome().equals(nome)) 
            {
                throw new ManagerException("Il nome selezionato è già presente nel manager.\n");
            }
        }
    }

    /**
     * Il metodo crea una nuova agenda con il nome specificato.
     * @param nome e' il nome dell'ageda da creare.
     * @return l'agenda appena creata.
     * @throws ManagerException viene lanciata quando è già presente un'agenda con quel nome.
     */
    public static void creaAgenda(String nome) throws ManagerException 
    {
        if(nome == "") {
        	manager.add(new Agenda());
        }
        else {
        	controlloEsistenzaAgenda(nome);
        	manager.add(new Agenda(nome));
        }
    }

    /**
     * Il metodo ricerca e restituisce l'agenda con il nome desiderato.
     * @param nome e' il nome dell'agenda da ricercare
     * @return l'agenda con il nome ricercato.
     * @throws ManagerException viene lanciata se l'agenda non è presente nel manager.
     */
    public static Agenda ricercaAgenda(String nome) throws ManagerException 
    {
        for (Agenda agenda : manager) 
        {
            if (agenda.getNome().equals(nome)) 
            {
                return agenda;
            }
        }
        throw new ManagerException("Agenda non trovata.\n");
    }

    /**
     * Il metodo cancella l'agenda con il nome specificato .
     * @param nome e' il noe dell'agenda da cancellare.
     * @throws ManagerException viene lanciata se l'agenda non è presente nel manager.
     */
    public static void cancellaAgenda(String nome) throws ManagerException 
    {
        Agenda agenda = ricercaAgenda(nome);
        if (agenda != null) 
        {
            manager.remove(agenda);
        } 
        else 
        {
            throw new ManagerException("Cancellazione non avvenuta, agenda non presente nel manager.\n");
        }
    }

    /**
     * Il metodo crea una nuova agenda a partire da un file e la aggiunge al manager.
     * @param filePath e' il percorso del file da cui leggere i dati.
     * @param nuovo e' il nome della nuova agenda da creare.
     * @return l'agenda appea creata.
     * @throws IOException viene lanciata se si verifica un errore di I/O durante la lettura del file.
     * @throws AgendaException viene lanciata se si verifica un errore nell'agenda.
     * @throws AppuntamentoException viene lanciata se si verifica un errore negli appuntamenti.
     * @throws ManagerException viene lanciata se eiste già un'agenda con il nome specificato.
     */
    public static Agenda creaDaFile(String filePath, String nuovo)
            throws IOException, AgendaException, AppuntamentoException, ManagerException 
    {
        if (!new File(filePath).exists()) 
        {
            throw new ManagerException("Il file " + filePath + " non esiste.");
        }
        controlloEsistenzaAgenda(nuovo);
        Agenda agenda = new Agenda(nuovo);
        try (BufferedReader buff = new BufferedReader(new FileReader(filePath))) 
        {
            String file = "";
            while ((file = buff.readLine()) != null) 
            {
                String[] Appuntamento = file.split(";");
                String orario = Appuntamento[0];
                String giorno = Appuntamento[1];
                int durata = Integer.parseInt(Appuntamento[2]);
                String nome = Appuntamento[3];
                String luogo = Appuntamento[4];
                Appuntamento appuntamento = new Appuntamento(orario, giorno, durata, nome, luogo); 
                agenda.inserisciAppuntamento(appuntamento);
            }
        }
        manager.add(agenda);
        return agenda;
    }

    /**
     * Il metodo restituisce il numero di agende presenti nel manager.
     * @return il numero di agende presenti nel manager.
     */
    public static int numeroAgende() 
    {
        return manager.size();
    }
    
    /**
     * Il metodo si occupa di svuotare il manager.
     */
    public static void svuota() {
    	manager.clear();
    }
}

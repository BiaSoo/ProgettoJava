package Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import org.junit.jupiter.api.*;
import Agenda.*;


class ManagerTest 
{
	
	@BeforeEach
	public void setup() {
		Manager.svuota();
	}
	
	@Test
	public void testControlloEsistenzaAgenda() throws ManagerException 
	{
	    Manager.creaAgenda("Visite");

	    assertThrows(ManagerException.class, () -> Manager.creaAgenda("Visite"));
	    assertDoesNotThrow(() -> Manager.creaAgenda("Controlli"));
	}
	
	@Test
    void testCreaDaFileGiaPresente() throws ManagerException 
	{
		// Creazione di agenda da file con nome già presente
		Manager.creaAgenda("agenda1");
		Exception thrown = assertThrows(ManagerException.class, () -> Manager.creaDaFile("fileProva.txt", "agenda1"));
		assertEquals("Il nome selezionato è già presente nel manager.\n", thrown.getMessage());
	}
	
	@Test
    void testCreaDaFile() throws IOException, AgendaException, AppuntamentoException, ManagerException {
        // Creazione di agenda da file con dati validi
        String filePath = "fileProva.txt";
        String nuovo = "agenda1";

        Agenda agenda = Manager.creaDaFile(filePath, nuovo);
        assertNotNull(agenda);
        assertEquals(1, Manager.numeroAgende());
        assertFalse(agenda.getListaAppuntamenti().isEmpty());
        assertEquals("Appuntamento [Orario= 10:00, Giorno=2023-09-29, Durata=90, Nome=Aldo, Luogo=Vercelli]", agenda.getAppuntamento(0).toString());
    }

	
	@Test
	public void testCreaDaFileNonRiuscito() 
	{
	    String filePath = "nonEsiste.txt";
	    String nomeAgenda = "Agenda1";
	    assertThrows(ManagerException.class, () -> Manager.creaDaFile(filePath, nomeAgenda));
	}

	
	@Test
	public void testNumeroAgende() throws ManagerException 
	{
	    assertEquals(0, Manager.numeroAgende());

	    Manager.creaAgenda("Agenda1");
	    assertEquals(1, Manager.numeroAgende());

	    Manager.creaAgenda("Agenda2");
	    assertEquals(2, Manager.numeroAgende());

	    Manager.cancellaAgenda("Agenda1");
	    assertEquals(1, Manager.numeroAgende());

	    Manager.cancellaAgenda("Agenda2");
	    assertEquals(0, Manager.numeroAgende());
	}

}
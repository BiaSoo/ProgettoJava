package Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.time.LocalTime.*;
import java.time.LocalDate.*;
import java.util.*;
import org.junit.jupiter.api.*;
import Agenda.*;

class AgendaTest {
	Agenda ag1;
	Appuntamento ap1;
	Appuntamento ap2;

	@BeforeEach
	void Before() throws AgendaException, AppuntamentoException {
		ag1 = new Agenda("Visite");
		ap1 = new Appuntamento("10:00", "2023-09-29", 90, "Aldo", "Vercelli");
		ag1.inserisciAppuntamento(ap1);
	}

	@Test
	void TestAgenda() throws AppuntamentoException, AgendaException {
		String nomeAgenda = "Visite";
		ag1 = new Agenda(nomeAgenda);

		assertEquals("Il nome dell'agenda non corrispone a quello impostato.", nomeAgenda, ag1.getNome());
		ArrayList<Appuntamento> appAggiunti = ag1.getListaAppuntamenti();
		assertTrue("L'elenco di agenda dovrebbe essere vuoto.\n", appAggiunti.isEmpty());
		ap1 = new Appuntamento("10:00", "2023-09-29", 90, "Aldo", "Vercelli");
		ag1.inserisciAppuntamento(ap1);
		ArrayList<Appuntamento> appAggiornati = ag1.getListaAppuntamenti();
		assertFalse("L'elenco degli appuntamenti dell'agenda dovrebbe essere vuoto", appAggiornati.isEmpty());
		assertEquals("L'appuntamento aggiunto non corrisponde a quello presente nell'agenda.\n", ap1,
				appAggiornati.get(0));
	}

	@Test
	void testInserisciAppuntamentoVuoto() throws AgendaException, AppuntamentoException {
		Agenda ag0 = new Agenda("Vuota");
		Appuntamento ap1 = new Appuntamento("10:00", "2023-09-29", 90, "Aldo", "Vercelli");
		ag0.inserisciAppuntamento(ap1);
		assertEquals(1, ag0.getListaAppuntamenti().size());
		assertTrue(ag0.getListaAppuntamenti().contains(ap1));
	}

	@Test
	void testInserisciAppuntamentoPieno() throws AgendaException, AppuntamentoException {
		Agenda ag1 = new Agenda("Visite");
		Appuntamento ap1 = new Appuntamento("10:00", "2023-09-29", 90, "Aldo", "Vercelli");
		Appuntamento ap2 = new Appuntamento("12:00", "2023-09-29", 60, "Giacomo", "Vercelli");
		ag1.inserisciAppuntamento(ap1);
		ag1.inserisciAppuntamento(ap2);
		Appuntamento ap3 = new Appuntamento("15:00", "2023-09-29", 45, "Giovanni", "Vercelli");
		assertDoesNotThrow(() -> ag1.inserisciAppuntamento(ap3));
	}

	@Test
	void testInserisciAppuntamentoSovrapposto() throws AgendaException, AppuntamentoException {
		Agenda ag1 = new Agenda("Visite");
		Appuntamento ap1 = new Appuntamento("10:00", "2023-09-29", 90, "Aldo", "Vercelli");
		Appuntamento ap2 = new Appuntamento("12:00", "2023-09-29", 60, "Giacomo", "Vercelli");
		ag1.inserisciAppuntamento(ap1);
		ag1.inserisciAppuntamento(ap2);
		Appuntamento ap3 = new Appuntamento("11:00", "2023-09-29", 120, "Giovanni", "Vercelli");
		assertThrows(AgendaException.class, () -> ag1.inserisciAppuntamento(ap3));
	}

	@Test
	void testRicercaAppuntamenti() throws AgendaException, AppuntamentoException {
		Agenda ag1 = new Agenda("Visite");
		Agenda ag2 = new Agenda("Vuota");

		Appuntamento ap1 = new Appuntamento("10:00", "2023-09-29", 90, "Aldo", "Vercelli");
		Appuntamento ap2 = new Appuntamento("12:00", "2023-09-29", 60, "Giovanni", "Vercelli");
		Appuntamento ap3 = new Appuntamento("14:00", "2023-09-29", 40, "Giacomo", "Vercelli");

		ag1.inserisciAppuntamento(ap1);
		ag1.inserisciAppuntamento(ap2);
		ag1.inserisciAppuntamento(ap3);

		List<Appuntamento> risultatiData = ag1.ricercaAppuntamentiData("2023-09-29");
		assertEquals(3, risultatiData.size());
		assertTrue(risultatiData.contains(ap1));
		assertTrue(risultatiData.contains(ap2));
		assertTrue(risultatiData.contains(ap3));

		List<Appuntamento> risultatiNome = ag1.ricercaAppuntamentiNome("Giovanni");
		assertEquals(1, risultatiNome.size());
		assertTrue(risultatiNome.contains(ap2));

		assertThrows(AgendaException.class, () -> ag1.ricercaAppuntamentiData("2023-09-30"));
		assertThrows(AgendaException.class, () -> ag1.ricercaAppuntamentiNome("Mario"));
		assertThrows(AgendaException.class, () -> ag2.ricercaAppuntamentiData("2023-09-30"));
		assertThrows(AgendaException.class, () -> ag2.ricercaAppuntamentiNome("Mario"));

	}

	

	@Test
	public void testScriviSuFile() throws IOException, AppuntamentoException, AgendaException {
		ag1 = new Agenda("Visite");
		Appuntamento ap1 = new Appuntamento("10:00", "2023-09-29", 90, "Aldo", "Vercelli");
		Appuntamento ap2 = new Appuntamento("12:00", "2023-09-29", 60, "Giacomo", "Vercelli");
		Appuntamento ap3 = new Appuntamento("15:00", "2023-09-29", 120, "Giovanni", "Vercelli");
		ag1.inserisciAppuntamento(ap1);
		ag1.inserisciAppuntamento(ap2);
		ag1.inserisciAppuntamento(ap3);

		String filePath = "fileProva.txt";
		ag1.scriviSuFile(filePath);

		try (BufferedReader buff = new BufferedReader(new FileReader("fileProva.txt"))) {
			assertEquals("10:00;2023-09-29;90;Aldo;Vercelli", buff.readLine());
			assertEquals("12:00;2023-09-29;60;Giacomo;Vercelli", buff.readLine());
			assertEquals("15:00;2023-09-29;120;Giovanni;Vercelli", buff.readLine());
		}

		String invalidFilePath = "path/non_esistente/fileProva.txt";
		IOException exception = assertThrows(IOException.class, () -> ag1.scriviSuFile(invalidFilePath));
		assertEquals("Errore durante la scrittura del file.", exception.getMessage());
	}

	@Test
	public void testNumeroAppuntamento() throws AppuntamentoException, AgendaException {
		ag1 = new Agenda("Visite");

		assertEquals(0, ag1.numeroAppuntamento());
		ag1.inserisciAppuntamento(new Appuntamento("10:00", "2023-09-29", 90, "Aldo", "Vercelli"));
		assertEquals(1, ag1.numeroAppuntamento());

		ag1.inserisciAppuntamento(new Appuntamento("12:00", "2023-09-29", 60, "Giacomo", "Vercelli"));
		assertEquals(2, ag1.numeroAppuntamento());
	}


	@Test
	public void testToString() throws AgendaException, AppuntamentoException {
		Agenda ag1 = new Agenda("Visite");
		Appuntamento ap1 = new Appuntamento("10:00", "2023-09-29", 90, "Aldo", "Vercelli");
		ag1.inserisciAppuntamento(ap1);

		String expectedOutput = "\nAgenda [nome=Visite, appuntamenti=[" + ap1.toString() + "]]";
		String actualOutput = ag1.toString();

		assertEquals(expectedOutput, actualOutput);
	}

	@Test
	public void testIterator() throws AppuntamentoException, AgendaException {
		Agenda ag1 = new Agenda("Visite");
		Appuntamento ap1 = new Appuntamento("10:00", "2023-09-29", 90, "Aldo", "Vercelli");
		Appuntamento ap2 = new Appuntamento("14:00", "2023-05-17", 90, "Giovanni", "Milano");
		ag1.inserisciAppuntamento(ap1);
		ag1.inserisciAppuntamento(ap2);

		Iterator<Appuntamento> iterator = ag1.iterator();

		Appuntamento appuntamento2 = iterator.next();
		assertEquals(ap2, appuntamento2);
		assertTrue(iterator.hasNext());
	}

	@Test
	void testCancellaAppuntamento() throws AgendaException, AppuntamentoException {
		Agenda ag1 = new Agenda("Appuntamenti");

		Appuntamento ap1 = new Appuntamento("10:00", "2023-09-29", 90, "Aldo", "Vercelli");
		Appuntamento ap2 = new Appuntamento("12:00", "2023-09-29", 60, "Giovanni", "Vercelli");
		Appuntamento ap3 = new Appuntamento("14:00", "2023-09-29", 40, "Giacomo", "Vercelli");
		ag1.inserisciAppuntamento(ap1);
		ag1.inserisciAppuntamento(ap2);
		ag1.inserisciAppuntamento(ap3);
		
		assertThrows(AgendaException.class, () -> ag1.cancellaAppuntamento(4));
	}
}

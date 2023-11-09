package Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.*;
import Agenda.*;



public class AppuntamentoTest {
	    
	Agenda ag1;
	Appuntamento appuntamento;

		@BeforeEach
		void Before() throws AgendaException, AppuntamentoException
		{
			ag1 = new Agenda ("Visite");
			appuntamento = new Appuntamento("10:00", "2023-09-29", 90, "Aldo", "Vercelli");
			ag1.inserisciAppuntamento(appuntamento);
		}
		@Test
		void testControlloFormato() throws AppuntamentoException {
		    // Testo il caso di successo
		    
		    appuntamento = new Appuntamento("12:00", "2023-07-20", 60, "Mario", "Roma");
		    

		    assertNotNull(appuntamento);

		    // Testo il caso di durata negativa
		    AppuntamentoException e = Assertions.assertThrows(AppuntamentoException.class, () -> new Appuntamento("12:00", "2023-07-20", -1, "Mario", "Roma"));
		    assertEquals(e.getMessage(),"La durata non può essere negativa o zero.");
		   

		    // Testo il caso di nome con cifre
		     e = Assertions.assertThrows(AppuntamentoException.class, () -> new Appuntamento("12:00", "2023-07-20", 60, "Mario12", "Roma"));
		     assertEquals(e.getMessage(),"Il nome non può contenere cifre.");
		    

		    // Testo il caso di giorno e orario non validi
		     e = Assertions.assertThrows(AppuntamentoException.class, () -> new Appuntamento("25:00", "2023-07-32", 60, "Mario", "Roma"));
		     assertEquals(e.getMessage(),"La data deve essere valida e in formato 'YYYY-MM-DD'.");
		    
		}

	    @Test
	    void testControlloSovrapposizione() throws AppuntamentoException {
	    	Appuntamento appuntamento1 = new Appuntamento("12:00", "2023-07-20", 60, "Mario", "Roma");
	    	Appuntamento appuntamento2 = new Appuntamento("12:30", "2023-07-20", 30, "Giuseppe", "Milano");
	    	Appuntamento appuntamento3 = new Appuntamento("13:00", "2023-07-20", 30, "Luigi", "Palermo");

	        assertTrue(appuntamento1.controlloSovrapposizione(appuntamento2));
	        assertFalse(appuntamento1.controlloSovrapposizione(appuntamento3));
	    }

	    @Test
	    void testGetOrario() throws AppuntamentoException {
	        Appuntamento appuntamento = new Appuntamento("12:00", "2023-07-20", 60, "Mario", "Roma");
	        assertEquals(LocalTime.of(12, 0), appuntamento.getOrario());
	    }

	    @Test
	    void testGetOrarioFine() throws AppuntamentoException {
	        Appuntamento appuntamento = new Appuntamento("12:00", "2023-07-20", 59, "Mario", "Roma");
	        assertEquals(LocalTime.of(12, 59), appuntamento.getOrarioFine());
	    }

	    @Test
	    void testGetGiorno() throws AppuntamentoException {
	        Appuntamento appuntamento = new Appuntamento("12:00", "2023-07-20", 60, "Mario", "Roma");
	        assertEquals(LocalDate.of(2023, 7, 20), appuntamento.getGiorno());
	    }

	    @Test
	    void testGetDurata() throws AppuntamentoException {
	    	Appuntamento appuntamento = new Appuntamento("12:00", "2023-07-20", 60, "Mario", "Roma");
	        assertEquals(60, appuntamento.getDurata());
	    }

	    @Test
	    void testGetNome() throws AppuntamentoException {
	        Appuntamento appuntamento = new Appuntamento("12:00", "2023-07-20", 60, "Mario", "Roma");
	        assertEquals("Mario", appuntamento.getNome());
	    }
	    
	    @Test
	    void testGetLuogo() throws AppuntamentoException {
	        Appuntamento appuntamento = new Appuntamento("12:00", "2023-07-20", 60, "Mario", "Roma");
	        assertEquals("Roma", appuntamento.getLuogo());
	    }

	    @Test
	    void testToString() throws AppuntamentoException {
	        Appuntamento appuntamento = new Appuntamento("12:00", "2023-07-20", 60, "Mario", "Roma");
	        assertEquals("Appuntamento [Orario= 12:00, Giorno=2023-07-20, Durata=60, Nome=Mario, Luogo=Roma]",appuntamento.toString());
	    }

	    @Test
	    void testCompara() throws AppuntamentoException {
	        Appuntamento appuntamento1 = new Appuntamento("12:00", "2023-07-20", 60, "Mario", "Roma");
	        Appuntamento appuntamento2 = new Appuntamento("12:30", "2023-07-20", 30, "Giuseppe", "Milano");
	        Appuntamento appuntamento3 = new Appuntamento("13:00", "2023-07-20", 30, "Luigi", "Palermo");

	        assertEquals(0, appuntamento1.compara(appuntamento2));
	        assertEquals(-1, appuntamento1.compara(appuntamento3));
	    }

	    @Test
	    void testControlloFormatoDataNonValida() {
	        AppuntamentoException e = Assertions.assertThrows(AppuntamentoException.class, () -> new Appuntamento("12:00", "2023-07-32", 60, "Mario", "Roma"));
	        assertEquals(e.getMessage(),"La data deve essere valida e in formato 'YYYY-MM-DD'.");
	    }

	    @Test
	    void testControlloFormatoOrarioNonValido() {
	    	AppuntamentoException e = Assertions.assertThrows(AppuntamentoException.class, () -> new Appuntamento("25:00", "2023-07-20", 60, "Mario", "Roma"));
	    	assertEquals(e.getMessage(),"L'orario deve essere valido e in formato 'hh:mm'.");
	    }

	    @Test
	    void testControlloFormatoDurataNonValida() {
	    	AppuntamentoException e = Assertions.assertThrows(AppuntamentoException.class, () -> new Appuntamento("12:00", "2023-07-20", -1, "Mario", "Roma"));
	    	assertEquals(e.getMessage(),"La durata non può essere negativa o zero.");
	    }

	    @Test
	    void testControlloFormatoNomeNonValido() {
	    	AppuntamentoException e = Assertions.assertThrows(AppuntamentoException.class, () -> new Appuntamento("12:00", "2023-07-20", 60, "Mario12", "Roma"));
	    	assertEquals(e.getMessage(),"Il nome non può contenere cifre.");
	    }
}
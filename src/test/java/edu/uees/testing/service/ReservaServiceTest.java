package edu.uees.testing.service;

import edu.uees.testing.availability.DisponibilidadClient;
import edu.uees.testing.domain.EstadoReserva;
import edu.uees.testing.domain.Reserva;
import edu.uees.testing.notification.Notificador;
import edu.uees.testing.repository.ReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReservaServiceTest {

    private ReservaService servicio;

    @BeforeEach
    void setUp() {
        // Para las pruebas de métodos puros (puedeCancelar y calcularTotal)
        // podemos seguir usando nulls o las instancias mock.
        servicio = new ReservaService(null, null, null);
    }

    // ==========================================
    // PRUEBAS DEL LABORATORIO 1 (MÉTODOS PUROS)
    // ==========================================

    @Test
    @DisplayName("CP-01: Cancelar con 5 horas de anticipación es permitido")
    void cincoHorasPermitenCancelar() {
        assertTrue(servicio.puedeCancelar(5));
    }

    @Test
    @DisplayName("CP-02: Cancelar con 2 horas exactas de anticipación es el límite permitido")
    void dosHorasEsElLimitePermitido() {
        assertTrue(servicio.puedeCancelar(2));
    }

    @Test
    @DisplayName("CP-03: Cancelar con 1 hora de anticipación no es permitido")
    void unaHoraNoPermiteCancelar() {
        assertFalse(servicio.puedeCancelar(1));
    }

    @Test
    @DisplayName("CP-04: Cancelar sin anticipación (0 horas) no es permitido")
    void ceroHorasNoPermiteCancelar() {
        assertFalse(servicio.puedeCancelar(0));
    }

    @Test
    @DisplayName("CP-05: Tipo NORMAL no recibe descuento")
    void normalNoRecibeDescuento() {
        assertEquals(100.0, servicio.calcularTotal("NORMAL", 100.0), 0.001);
    }

    @Test
    @DisplayName("CP-06: Tipo VIP recibe el 15% de descuento")
    void vipRecibeQuincePorCiento() {
        assertEquals(85.0, servicio.calcularTotal("VIP", 100.0), 0.001);
    }

    @Test
    @DisplayName("CP-07: Tipo ESTUDIANTE recibe el 10% de descuento")
    void estudianteRecibeDiezPorCiento() {
        assertEquals(90.0, servicio.calcularTotal("ESTUDIANTE", 100.0), 0.001);
    }

    @Test
    @DisplayName("CP-08: Un total base de cero retorna cero sin importar el descuento")
    void totalBaseCeroRetornaCero() {
        assertEquals(0.0, servicio.calcularTotal("VIP", 0.0), 0.001);
    }

    @Test
    @DisplayName("CP-09: Un total base negativo lanza IllegalArgumentException")
    void totalNegativoEsInvalido() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> servicio.calcularTotal("NORMAL", -1.0)
        );
        assertEquals("Total base inválido", ex.getMessage());
    }

    // =========================================================================
    // PRUEBAS DEL LABORATORIO 2: DOBLES DE PRUEBA (STUB / MOCK) EN CONFIRMAR()
    // =========================================================================

    @Test
    @DisplayName("Caso 1: Reserva disponible se confirma, se guarda en repositorio y se notifica")
    void reservaDisponibleSeConfirmaGuardaYNotifica() {
        // Arrange
        DisponibilidadClient disponibilidad = mock(DisponibilidadClient.class);
        ReservaRepository repository = mock(ReservaRepository.class);
        Notificador notificador = mock(Notificador.class);

        // Stubbing: forzamos que la reserva SI esté disponible
        when(disponibilidad.estaDisponible(any())).thenReturn(true);

        ReservaService servicioConDobles = new ReservaService(disponibilidad, repository, notificador);
        Reserva reserva = new Reserva("R-001", "NORMAL");

        // Act
        servicioConDobles.confirmar(reserva);

        // Assert
        assertEquals(EstadoReserva.CONFIRMADA, reserva.getEstado());
        verify(repository).guardar(reserva);
        verify(notificador).enviarConfirmacion(reserva);
    }

    @Test
    @DisplayName("Caso 2: Reserva no disponible lanza excepción, NO se guarda ni notifica")
    void reservaNoDisponibleNoSeGuardaNiNotifica() {
        // Arrange
        DisponibilidadClient disponibilidad = mock(DisponibilidadClient.class);
        ReservaRepository repository = mock(ReservaRepository.class);
        Notificador notificador = mock(Notificador.class);

        // Stubbing: forzamos que la reserva NO esté disponible
        when(disponibilidad.estaDisponible(any())).thenReturn(false);

        ReservaService servicioConDobles = new ReservaService(disponibilidad, repository, notificador);
        Reserva reserva = new Reserva("R-002", "NORMAL");

        // Act & Assert
        assertThrows(
                IllegalStateException.class,
                () -> servicioConDobles.confirmar(reserva)
        );

        verify(repository, never()).guardar(any());
        verify(notificador, never()).enviarConfirmacion(any());
    }

    @Test
    @DisplayName("Caso 3: Reserva nula lanza excepción y NO consulta ninguna dependencia")
    void reservaNulaNoConsultaDependencias() {
        // Arrange
        DisponibilidadClient disponibilidad = mock(DisponibilidadClient.class);
        ReservaRepository repository = mock(ReservaRepository.class);
        Notificador notificador = mock(Notificador.class);

        ReservaService servicioConDobles = new ReservaService(disponibilidad, repository, notificador);

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> servicioConDobles.confirmar(null)
        );

        verify(disponibilidad, never()).estaDisponible(any());
        verify(repository, never()).guardar(any());
        verify(notificador, never()).enviarConfirmacion(any());
    }
}
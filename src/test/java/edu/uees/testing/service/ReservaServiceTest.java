package edu.uees.testing.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReservaServiceTest {

    private ReservaService servicio;

    @BeforeEach
    void setUp() {
        // En este laboratorio instanciamos ReservaService pasando null en sus dependencias
        // porque puedeCancelar y calcularTotal son métodos puros (no usan colaboradores externos).
        servicio = new ReservaService(null, null, null);
    }

    // ==========================================
    // PRUEBAS DE CANCELACIÓN (puedeCancelar)
    // ==========================================

    @Test
    @DisplayName("CP-01: Cancelar con 5 horas de anticipación es permitido")
    void cincoHorasPermitenCancelar() {
        // Arrange
        int horas = 5;

        // Act
        boolean resultado = servicio.puedeCancelar(horas);

        // Assert
        assertTrue(resultado);
    }

    @Test
    @DisplayName("CP-02: Cancelar con 2 horas exactas de anticipación es el límite permitido")
    void dosHorasEsElLimitePermitido() {
        // Arrange
        int horas = 2;

        // Act
        boolean resultado = servicio.puedeCancelar(horas);

        // Assert
        assertTrue(resultado);
    }

    @Test
    @DisplayName("CP-03: Cancelar con 1 hora de anticipación no es permitido")
    void unaHoraNoPermiteCancelar() {
        // Arrange
        int horas = 1;

        // Act
        boolean resultado = servicio.puedeCancelar(horas);

        // Assert
        assertFalse(resultado);
    }

    @Test
    @DisplayName("CP-04: Cancelar sin anticipación (0 horas) no es permitido")
    void ceroHorasNoPermiteCancelar() {
        // Arrange
        int horas = 0;

        // Act
        boolean resultado = servicio.puedeCancelar(horas);

        // Assert
        assertFalse(resultado);
    }

    // ==========================================
    // PRUEBAS DE DESCUENTOS (calcularTotal)
    // ==========================================

    @Test
    @DisplayName("CP-05: Tipo NORMAL no recibe descuento")
    void normalNoRecibeDescuento() {
        // Arrange
        String tipo = "NORMAL";
        double totalBase = 100.0;

        // Act
        double totalCalculado = servicio.calcularTotal(tipo, totalBase);

        // Assert
        assertEquals(100.0, totalCalculado, 0.001);
    }

    @Test
    @DisplayName("CP-06: Tipo VIP recibe el 15% de descuento")
    void vipRecibeQuincePorCiento() {
        // Arrange
        String tipo = "VIP";
        double totalBase = 100.0;

        // Act
        double totalCalculado = servicio.calcularTotal(tipo, totalBase);

        // Assert
        assertEquals(85.0, totalCalculado, 0.001);
    }

    @Test
    @DisplayName("CP-07: Tipo ESTUDIANTE recibe el 10% de descuento")
    void estudianteRecibeDiezPorCiento() {
        // Arrange
        String tipo = "ESTUDIANTE";
        double totalBase = 100.0;

        // Act
        double totalCalculado = servicio.calcularTotal(tipo, totalBase);

        // Assert
        assertEquals(90.0, totalCalculado, 0.001);
    }

    @Test
    @DisplayName("CP-08: Un total base de cero retorna cero sin importar el descuento")
    void totalBaseCeroRetornaCero() {
        // Arrange
        String tipo = "VIP";
        double totalBase = 0.0;

        // Act
        double totalCalculado = servicio.calcularTotal(tipo, totalBase);

        // Assert
        assertEquals(0.0, totalCalculado, 0.001);
    }

    @Test
    @DisplayName("CP-09: Un total base negativo lanza IllegalArgumentException")
    void totalNegativoEsInvalido() {
        // Arrange
        String tipo = "NORMAL";
        double totalBase = -1.0;

        // Act & Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> servicio.calcularTotal(tipo, totalBase)
        );

        assertEquals("Total base inválido", ex.getMessage());
    }
}
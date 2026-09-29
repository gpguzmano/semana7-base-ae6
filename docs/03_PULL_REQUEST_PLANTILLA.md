# Plantilla de Pull Request

## Objetivo
Proteger las reglas críticas de negocio del módulo de reservas (`ReservaService`), cubriendo reglas de cancelación, cálculo de tarifas con descuentos y confirmación con colaboradores externos mediante pruebas unitarias integradas con JUnit 5, Mockito y JaCoCo.

## Cambios realizados
- Implementación de la suite completa de pruebas unitarias en `ReservaServiceTest` (12 casos de prueba).
- Incorporación de dobles de prueba (`Stub` y `Mock`) con Mockito para aislar las dependencias de `confirmar()`.
- Configuración y análisis del reporte de cobertura de código JaCoCo.
- Documentación de la matriz de casos y el análisis de cobertura en la carpeta `docs/`[cite: 12, 14, 15].

## Casos de prueba
- Cancelación: `cincoHorasPermitenCancelar`, `dosHorasEsElLimitePermitido`, `unaHoraNoPermiteCancelar`, `ceroHorasNoPermiteCancelar`[cite: 13].
- Descuentos: `normalNoRecibeDescuento`, `vipRecibeQuincePorCiento`, `estudianteRecibeDiezPorCiento`, `totalBaseCeroRetornaCero`, `totalNegativoEsInvalido`[cite: 13].
- Confirmación: `reservaDisponibleSeConfirmaGuardaYNotifica`, `reservaNoDisponibleNoSeGuardaNiNotifica`, `reservaNulaNoConsultaDependencias`[cite: 13].

## Cómo verificar
```bash
mvn clean test
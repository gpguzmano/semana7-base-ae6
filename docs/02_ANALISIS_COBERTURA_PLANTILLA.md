# Análisis de cobertura

## Resultado observado
- Cobertura de líneas: 100% en `ReservaService` y `Reserva`
- Cobertura de ramas: 100% en `ReservaService` y `Reserva`
- Clase o método analizado: `ReservaService` (`puedeCancelar`, `calcularTotal`, `confirmar`) y `Reserva`

## Huecos relevantes
1. **Verificación de interacciones en colaboradores de `confirmar`:** El método `confirmar` utiliza tres servicios externos (`DisponibilidadClient`, `ReservaRepository`, `Notificador`)[cite: 13]. Sin stubs y mocks, la ejecución no podía cubrir ni la persistencia ni la notificación.
2. **Excepción por argumento nulo en `confirmar`:** La rama de guardia `if (reserva == null)` requería verificar que el flujo se interrumpiera inmediatamente sin consultar `DisponibilidadClient`.

## Decisiones
- ¿Qué prueba nueva se añadió?: Se integraron tres casos de prueba para `confirmar()` utilizando Mockito: `reservaDisponibleSeConfirmaGuardaYNotifica()`, `reservaNoDisponibleNoSeGuardaNiNotifica()` y `reservaNulaNoConsultaDependencias()`.
- ¿Qué riesgo protege?: Garantiza que solo las reservas válidas y disponibles cambien a estado `CONFIRMADA`, se persistan en la base de datos y envíen notificaciones al cliente.
- ¿Por qué no basta con el porcentaje?: Porque el porcentaje solo indica que las líneas de código fueron ejecutadas durante la prueba, pero no confirma que los efectos secundarios (como llamadas a `guardar` o `enviarConfirmacion`) hayan ocurrido con los argumentos adecuados y en el orden correcto.
# Análisis de cobertura

## Resultado observado
- Cobertura de líneas: 100%
- Cobertura de ramas: 100%
- Clase o método analizado: `ReservaService` (`puedeCancelar`, `calcularTotal` y `confirmar`)[cite: 10, 11]

## Huecos relevantes
1. **Falta de verificación de dependencias en `confirmar`:** El método `confirmar(Reserva)` dependía de tres interfaces externas (`DisponibilidadClient`, `ReservaRepository`, `Notificador`)[cite: 10, 11]. Sin dobles de prueba (`Stub`/`Mock`), no era posible comprobar si las notificaciones o la persistencia se ejecutaban en el orden y momento correctos[cite: 11].
2. **Validación de ramas en valores de entrada nulos:** Faltaba comprobar que al recibir un argumento `null` en `confirmar()`, el sistema reaccionara con una excepción `IllegalArgumentException` sin llegar a invocar a las dependencias externas[cite: 10, 11].

## Decisiones
- ¿Qué prueba nueva se añadió?:
    - `reservaDisponibleSeConfirmaGuardaYNotifica()`: Evalúa el flujo exitoso usando `when(...).thenReturn(true)` como Stub y verifica llamadas a `guardar()` y `enviarConfirmacion()` con `verify(...)`[cite: 11].
    - `reservaNoDisponibleNoSeGuardaNiNotifica()`: Simula falta de disponibilidad mediante Stub, valida la excepción `IllegalStateException` y verifica que jamás se llame a la persistencia o notificación usando `never()`[cite: 11].
    - `reservaNulaNoConsultaDependencias()`: Valida que una reserva `null` corte la ejecución de inmediato y no consulte a `DisponibilidadClient`[cite: 11].
- ¿Qué riesgo protege?: Evita efectos secundarios no deseados (como guardar en base de datos o enviar un correo electrónico de confirmación cuando una reserva no está disponible o es inválida)[cite: 11].
- ¿Por qué no basta con el porcentaje?: Porque el porcentaje solo mide que la línea de código fue ejecutada durante el test, pero no garantiza que las aserciones (`assert`) sean correctas ni que los efectos secundarios e interacciones con dependencias externas hayan sido verificados adecuadamente[cite: 11].
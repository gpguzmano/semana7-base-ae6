# Matriz de casos

| ID | Regla | Escenario | Entrada | Esperado | Tipo | Riesgo |
|---|---|---|---|---|---|---|
| CP-01 | Cancelación | Anticipación habitual | 5 | true | Normal | Permitir cancelación con tiempo suficiente |
| CP-02 | Cancelación | Límite permitido | 2 | true | Límite | Detectar uso incorrecto de > en lugar de >= |
| CP-03 | Cancelación | Debajo del límite | 1 | false | Límite | Permitir cancelación fuera de ventana permitida |
| CP-04 | Cancelación | Sin anticipación | 0 | false | Extremo | Permitir cancelación inmediata inválida |
| CP-05 | Descuentos | Cliente NORMAL | "NORMAL", 100.0 | 100.0 | Normal | Aplicar descuento a tarifa sin beneficio |
| CP-06 | Descuentos | Cliente VIP | "VIP", 100.0 | 85.0 | Alternativo | No aplicar el 15% de descuento correspondiente |
| CP-07 | Descuentos | Cliente ESTUDIANTE | "ESTUDIANTE", 100.0 | 90.0 | Alternativo | No aplicar el 10% de descuento correspondiente |
| CP-08 | Descuentos | Total base en cero | "VIP", 0.0 | 0.0 | Límite | Error aritmético en importe de valor cero |
| CP-09 | Descuentos | Total base negativo | "NORMAL", -1.0 | IllegalArgumentException | Inválido | Procesar un valor negativo sin lanzar excepción |
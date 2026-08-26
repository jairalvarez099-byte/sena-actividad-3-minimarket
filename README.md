# Actividad 3 — Sistema de Gestión de Ventas e Inventario "Minimarket"

**Aprendiz:** Jair Alvarez Alvarez

## ⚠️ Restricción de paradigma
Este proyecto es **Programación Estructurada Pura**.
**NO** hay clases de dominio ni objetos propios: solo **métodos estáticos**,
**tipos primitivos** y **arreglos paralelos**.

## Los arreglos paralelos
```java
String[] nombresProductos   // posición 0 = nombre del producto 0
double[] preciosProductos   // posición 0 = precio del producto 0
int[]    stockProductos     // posición 0 = stock  del producto 0
```
Los tres se recorren con el **mismo índice**: por eso se llaman paralelos.

## Métodos obligatorios implementados
| Método | Opción del menú | Qué hace |
|---|---|---|
| `mostrarMenu()` | — | Dibuja el menú principal |
| `registrarProducto(...)` | 1 | Registra hasta 10 productos con validaciones |
| `mostrarInventario(...)` | 2 | Imprime la tabla del inventario |
| `procesarVenta(...)` | 3 | Valida stock, descuenta, calcula factura |
| `generarReporte(...)` | 4 | Ingresos, producto más caro/barato, unidades vendidas |

## Reglas de negocio
- Máximo **10 productos**.
- El precio debe ser **mayor a 0**; el stock **mayor o igual a 0**.
- Si la cantidad pedida **supera el stock** → error y se cancela la venta.
- Si el subtotal supera **$100.000** → **10% de descuento**.
- Se aplica **IVA del 19%** sobre la base ya descontada.

## Cómo compilar y ejecutar
```bash
javac -d out GestionMinimarket.java
java -cp out GestionMinimarket
```

## Ejemplo de salida esperada
```
1. Registrar: "Aceite Premier 1L", precio 18000, stock 20
3. Vender: producto 1, cantidad 8

============ FACTURA DE VENTA ============
Producto  : Aceite Premier 1L
Cantidad  : 8 unidades
Sub total : $144000.00
Descuento : -$14400.00  (10% por compra mayor a $100.000)
IVA (19%) : $24624.00
TOTAL     : $154224.00
==========================================
```

import java.util.Locale;
import java.util.Scanner;

/**
 * ACTIVIDAD 3 - Sistema de Gestion de Ventas e Inventario "Minimarket"
 * Aprendiz: Jair Alvarez Alvarez
 *
 * PARADIGMA: Programacion Estructurada Pura.
 * NO se crean clases de dominio ni objetos propios.
 * Solo metodos estaticos, tipos primitivos y arreglos paralelos.
 *
 * Los tres arreglos son PARALELOS: la posicion 0 de los tres se refiere
 * al mismo producto (nombre, precio y stock del producto 0).
 */
public class GestionMinimarket {

    static Scanner scanner = new Scanner(System.in);

    // Constantes del negocio
    static final int MAX_PRODUCTOS = 10;
    static final double IVA = 0.19;
    static final double MONTO_MINIMO_DESCUENTO = 100000.0;
    static final double PORCENTAJE_DESCUENTO = 0.10;

    public static void main(String[] args) {
        // Se unifica el formato numerico: decimales con PUNTO tanto al leer
        // como al imprimir, sin importar el idioma del computador.
        Locale.setDefault(Locale.US);
        scanner.useLocale(Locale.US);

        // --- Arreglos paralelos: el corazon del sistema ---
        String[] nombresProductos = new String[MAX_PRODUCTOS];
        double[] preciosProductos = new double[MAX_PRODUCTOS];
        int[] stockProductos = new int[MAX_PRODUCTOS];

        int contadorProductos = 0;        // cuantos productos hay registrados
        double totalVentasAcumuladas = 0; // ingresos de la sesion
        int articulosVendidos = 0;        // unidades vendidas en la sesion

        int opcion;

        do {
            mostrarMenu();
            opcion = leerEntero();

            switch (opcion) {
                case 1 -> contadorProductos = registrarProducto(
                              nombresProductos, preciosProductos, stockProductos, contadorProductos);

                case 2 -> mostrarInventario(
                              nombresProductos, preciosProductos, stockProductos, contadorProductos);

                case 3 -> {
                    double venta = procesarVenta(
                              nombresProductos, preciosProductos, stockProductos, contadorProductos);
                    if (venta > 0) {
                        totalVentasAcumuladas += venta;
                        articulosVendidos += ultimaCantidadVendida;
                    }
                }

                case 4 -> generarReporte(totalVentasAcumuladas, nombresProductos,
                              preciosProductos, contadorProductos, articulosVendidos);

                case 0 -> System.out.println("\nGracias por usar el sistema. Hasta pronto.");

                default -> System.out.println("\n[!] Opcion invalida. Elija un numero del 0 al 4.");
            }

        } while (opcion != 0);

        scanner.close();
    }

    // Variable auxiliar para saber cuantas unidades salieron en la ultima venta
    static int ultimaCantidadVendida = 0;

    // ==================== MENU PRINCIPAL ====================
    public static void mostrarMenu() {
        System.out.println("\n==================================================");
        System.out.println("    MINIMARKET - SISTEMA DE GESTION Y VENTAS");
        System.out.println("==================================================");
        System.out.println("1. Registrar / Actualizar Productos");
        System.out.println("2. Consultar Inventario Completo");
        System.out.println("3. Realizar una Venta");
        System.out.println("4. Generar Reporte de Ventas y Estadisticas");
        System.out.println("0. Salir del Sistema");
        System.out.println("==================================================");
        System.out.print("Seleccione una opcion: ");
    }

    // ==================== OPCION 1: REGISTRAR ====================
    public static int registrarProducto(String[] nombres, double[] precios,
                                        int[] stock, int contador) {

        System.out.println("\n--- REGISTRO DE PRODUCTO ---");

        if (contador >= MAX_PRODUCTOS) {
            System.out.println("[!] ALERTA: inventario lleno. Solo caben "
                               + MAX_PRODUCTOS + " productos.");
            return contador;
        }

        System.out.print("Nombre del producto: ");
        String nombre = scanner.nextLine().trim();

        // Validacion del precio: debe ser mayor a 0
        double precio;
        do {
            System.out.print("Precio unitario (mayor a 0): ");
            precio = leerDecimal();
            if (precio <= 0) {
                System.out.println("[!] El precio debe ser mayor a 0.");
            }
        } while (precio <= 0);

        // Validacion del stock: debe ser mayor o igual a 0
        int cantidad;
        do {
            System.out.print("Cantidad inicial en stock (0 o mas): ");
            cantidad = leerEntero();
            if (cantidad < 0) {
                System.out.println("[!] El stock no puede ser negativo.");
            }
        } while (cantidad < 0);

        nombres[contador] = nombre;
        precios[contador] = precio;
        stock[contador] = cantidad;

        System.out.println("[OK] Producto registrado con el ID " + contador + ".");
        return contador + 1;   // se devuelve el nuevo total de productos
    }

    // ==================== OPCION 2: INVENTARIO ====================
    public static void mostrarInventario(String[] nombres, double[] precios,
                                         int[] stock, int contador) {

        System.out.println("\n--- INVENTARIO COMPLETO ---");

        if (contador == 0) {
            System.out.println("No hay productos registrados en el sistema.");
            return;
        }

        System.out.printf("%-5s %-25s %-15s %-10s%n", "ID", "NOMBRE", "PRECIO", "STOCK");
        System.out.println("---------------------------------------------------------");
        for (int i = 0; i < contador; i++) {
            System.out.printf("%-5d %-25s $%-14.2f %-10d%n",
                              i, nombres[i], precios[i], stock[i]);
        }
        System.out.println("---------------------------------------------------------");
        System.out.println("Total de productos registrados: " + contador);
    }

    // ==================== OPCION 3: VENTA ====================
    public static double procesarVenta(String[] nombres, double[] precios,
                                       int[] stock, int contador) {

        System.out.println("\n--- PROCESAR VENTA ---");
        ultimaCantidadVendida = 0;

        if (contador == 0) {
            System.out.println("No hay productos registrados en el sistema.");
            return 0;
        }

        mostrarInventario(nombres, precios, stock, contador);

        System.out.print("\nID del producto a vender: ");
        int id = leerEntero();

        if (id < 0 || id >= contador) {
            System.out.println("[!] ERROR: el ID " + id + " no existe. Venta cancelada.");
            return 0;
        }

        System.out.print("Cantidad deseada: ");
        int cantidad = leerEntero();

        if (cantidad <= 0) {
            System.out.println("[!] ERROR: la cantidad debe ser mayor a 0. Venta cancelada.");
            return 0;
        }

        // VALIDACION CLAVE: no vender mas de lo que hay
        if (cantidad > stock[id]) {
            System.out.println("[!] ERROR: stock insuficiente. Disponible: "
                               + stock[id] + " unidades. Venta cancelada.");
            return 0;
        }

        // Hay stock suficiente: se descuenta del inventario
        stock[id] = stock[id] - cantidad;

        double subtotal = precios[id] * cantidad;
        double descuento = 0;

        if (subtotal > MONTO_MINIMO_DESCUENTO) {
            descuento = subtotal * PORCENTAJE_DESCUENTO;
        }

        double baseGravable = subtotal - descuento;
        double valorIva = baseGravable * IVA;
        double totalPagar = baseGravable + valorIva;

        System.out.println("\n============ FACTURA DE VENTA ============");
        System.out.printf("Producto  : %s%n", nombres[id]);
        System.out.printf("Cantidad  : %d unidades%n", cantidad);
        System.out.printf("Sub total : $%.2f%n", subtotal);
        System.out.printf("Descuento : -$%.2f  %s%n", descuento,
                          (descuento > 0 ? "(10% por compra mayor a $100.000)" : ""));
        System.out.printf("IVA (19%%) : $%.2f%n", valorIva);
        System.out.printf("TOTAL     : $%.2f%n", totalPagar);
        System.out.println("==========================================");
        System.out.println("Stock restante de " + nombres[id] + ": " + stock[id]);

        ultimaCantidadVendida = cantidad;
        return totalPagar;
    }

    // ==================== OPCION 4: REPORTE ====================
    public static void generarReporte(double totalVentasAcumuladas, String[] nombres,
                                      double[] precios, int contador, int articulosVendidos) {

        System.out.println("\n--- REPORTE DE VENTAS Y ESTADISTICAS ---");

        System.out.printf("Total acumulado de ingresos : $%.2f%n", totalVentasAcumuladas);
        System.out.println("Total de articulos vendidos : " + articulosVendidos + " unidades");

        if (contador == 0) {
            System.out.println("No hay productos registrados para calcular el mas caro/barato.");
            return;
        }

        // Buscar el mas caro y el mas barato recorriendo el arreglo
        int indiceCaro = 0;
        int indiceBarato = 0;

        for (int i = 1; i < contador; i++) {
            if (precios[i] > precios[indiceCaro])   indiceCaro = i;
            if (precios[i] < precios[indiceBarato]) indiceBarato = i;
        }

        System.out.printf("Producto mas caro   : %s ($%.2f)%n",
                          nombres[indiceCaro], precios[indiceCaro]);
        System.out.printf("Producto mas barato : %s ($%.2f)%n",
                          nombres[indiceBarato], precios[indiceBarato]);
    }

    // ==================== METODOS AUXILIARES DE LECTURA SEGURA ====================
    // Estos metodos evitan que el programa se caiga si el usuario escribe letras.

    public static int leerEntero() {
        while (!scanner.hasNextInt()) {
            System.out.print("[!] Debe ingresar un numero entero. Intente de nuevo: ");
            scanner.nextLine();
        }
        int valor = scanner.nextInt();
        scanner.nextLine();   // limpia el buffer (Enter pendiente)
        return valor;
    }

    public static double leerDecimal() {
        while (!scanner.hasNextDouble()) {
            System.out.print("[!] Debe ingresar un numero. Intente de nuevo: ");
            scanner.nextLine();
        }
        double valor = scanner.nextDouble();
        scanner.nextLine();
        return valor;
    }
}

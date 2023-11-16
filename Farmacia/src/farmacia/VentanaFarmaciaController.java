/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package farmacia;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 *
 * @author neon
 */
public class VentanaFarmaciaController implements Initializable {

    @FXML
    private Label label;
    @FXML
    private TableColumn tip;
    @FXML
    private TableColumn nom;
    @FXML
    private TableColumn aid;
    @FXML
    private TableColumn pre;
    @FXML
    private TableColumn uni;
    @FXML
    private Button agr;
    @FXML
    private Button comp;
    @FXML
    private Button vpro;
    @FXML
    private TableView<nodo> tabla;

    ObservableList<nodo> nodos;
    ObservableList<String> historial;
    @FXML
    private TextField ti;
    @FXML
    private TextField nomb;
    @FXML
    private TextField ide;
    @FXML
    private TextField preci;
    @FXML
    private TextField unidad;
    @FXML
    private Button elimi;
    @FXML
    private MenuItem m1;
    @FXML
    private MenuItem t2;

    private Map<String, Integer> ventasPorProducto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ventasPorProducto = new HashMap<>();
        historial = FXCollections.observableArrayList();
        nodos = FXCollections.observableArrayList();
        File file = new File("src/farmacia/Farmacos.txt");

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(",");
                nodo nike = new nodo(line[0], line[1], line[2], Double.parseDouble(line[3]), Integer.parseInt(line[4]));
                nodos.add(nike);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("El archivo no se pudo encontrar");
        }
        tip.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        tip.setStyle("-fx-alignment: CENTER-LEFT");
        nom.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        nom.setStyle("-fx-alignment: CENTER-LEFT");
        aid.setCellValueFactory(new PropertyValueFactory<>("id"));
        aid.setStyle("-fx-alignment: CENTER-LEFT");
        uni.setCellValueFactory(new PropertyValueFactory<>("unidades"));
        uni.setStyle("-fx-alignment: CENTER-LEFT");
        pre.setCellValueFactory(new PropertyValueFactory<>("precio"));
        pre.setStyle("-fx-alignment: CENTER-LEFT");
        int size = nodos.size();
        for (int i = 0; i < size; i++) {
            int prevIndex = i == 0 ? size - 1 : i - 1;
            int nextIndex = i == size - 1 ? 0 : i + 1;
            nodos.get(i).setAnt(nodos.get(prevIndex));
            nodos.get(i).setSig(nodos.get(nextIndex));
        }
        tabla.setItems(nodos);
    }

    @FXML
    private void agregar(ActionEvent event) {
        //Este es el metodo push!!!
        // Al inicio verificar si todos los campos de texto están llenos
        if (ti.getText().trim().isEmpty() || nomb.getText().trim().isEmpty() || ide.getText().trim().isEmpty() || unidad.getText().trim().isEmpty()
                || preci.getText().trim().isEmpty()) {
            // Mostrar mensaje de advertencia sobre campos vacíos
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setHeaderText("Mensaje de información");
            alerta.setTitle("Diálogo de advertencia");
            alerta.setContentText("Es necesario llenar todos los campos");
            alerta.showAndWait();
            return;
        }

        // Luego crea un nuevo nodo con los datos ingresados
        nodo nuevo = new nodo(ti.getText().trim(), nomb.getText().trim(), ide.getText().trim(),
                Double.parseDouble(preci.getText().trim()), Integer.parseInt(unidad.getText().trim()));

        // Agregar el nuevo nodo al inicio de la lista
        if (!nodos.isEmpty()) {
            nodo ultimo = nodos.get(nodos.size() - 1);
            nuevo.setSig(nodos.get(0)); // El nuevo nodo apunta al primer nodo actual
            ultimo.setSig(nuevo); // El último nodo actual apunta al nuevo nodo
        } else {
            // Si la lista está vacía, hacer que el nuevo nodo apunte a sí mismo
            nuevo.setSig(nuevo); // El único nodo apunta a sí mismo
        }
        nodos.add(0, nuevo);

        // Actualizar la tabla y limpiar los campos de texto
        tabla.setItems(nodos);
        tabla.refresh();
        ti.clear();
        nomb.clear();
        ide.clear();
        unidad.clear();
        preci.clear();

        guardarNodoEnArchivoInicio(nuevo);
    }

    private void guardarNodoEnArchivoInicio(nodo nodo) {
        String archivoRuta = "src/farmacia/Farmacos.txt";

        try {
            // Leer el contenido actual del archivo
            Scanner scanner = new Scanner(new File(archivoRuta));
            StringBuilder fileContent = new StringBuilder();

            while (scanner.hasNextLine()) {
                fileContent.append(scanner.nextLine()).append("\n");
            }
            scanner.close();

            // Agregar los datos del nuevo nodo al inicio del contenido del archivo
            String nuevoDato = nodo.getTipo() + "," + nodo.getNombre() + "," + nodo.getId() + "," + nodo.getPrecio() + "," + nodo.getUnidades() + "\n";
            fileContent.insert(0, nuevoDato);

            // Escribir el contenido actualizado en el archivo
            FileWriter fileWriter = new FileWriter(archivoRuta);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(fileContent.toString());
            bufferedWriter.close();

            // Mostrar un mensaje de éxito
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Datos guardados dentro del archivo");
            alerta.setContentText("Los datos se guardaron correctamente.");
            alerta.showAndWait();
        } catch (IOException e) {
            // Mostrar un mensaje de error en caso de excepción
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setContentText("No se pudieron guardar los datos");
            alerta.showAndWait();
        }
    }

    private void actualizaArchivo(nodo producto, int cantidadComprada) {
        String rutaArchivo = "src/tiendanike/Archivo.txt";

        try {
            File archivo = new File(rutaArchivo);
            Scanner scanner = new Scanner(archivo);
            StringBuilder contenidoArchivo = new StringBuilder();

            while (scanner.hasNextLine()) {
                contenidoArchivo.append(scanner.nextLine()).append("\n");
            }
            scanner.close();

            String idProducto = producto.getId();

            String[] lineas = contenidoArchivo.toString().split("\\n");
            for (int i = 0; i < lineas.length; i++) {
                String[] elementos = lineas[i].split(",");
                if (elementos.length >= 5 && elementos[2].equals(idProducto)) {
                    int unidades = Integer.parseInt(elementos[3]);
                    unidades -= cantidadComprada;
                    elementos[3] = Integer.toString(unidades);
                    lineas[i] = String.join(",", elementos);
                    break;
                }
            }

            FileWriter fileWriter = new FileWriter(rutaArchivo);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for (String linea : lineas) {
                bufferedWriter.write(linea);
                bufferedWriter.newLine();
            }

            bufferedWriter.close();
        } catch (IOException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setHeaderText("Error al actualizar el archivo");
            alerta.setContentText("Se produjo un error al actualizar el archivo.");
            alerta.showAndWait();
        }
    }

    @FXML
    private void VerVentanaProductos(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        abrirventana("Productos.fxml", stage);
    }

    private void abrirventana(String fxmlFileName, Stage stage) throws IOException {
        // Crear un nuevo cargador de FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void pop(ActionEvent event) {
        if (!nodos.isEmpty()) {
            nodos.remove(nodos.size() - 1);
            String archivoRuta = "src/farmacia/Farmacos.txt";
            File archivo = new File(archivoRuta);
            try {
                ObservableList<String> lineas = FXCollections.observableArrayList();
                Scanner scanner = new Scanner(archivo);
                while (scanner.hasNextLine()) {
                    lineas.add(scanner.nextLine());
                }
                scanner.close();
                FileWriter fileWriter = new FileWriter(archivoRuta);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                for (int i = 0; i < lineas.size() - 1; i++) {
                    bufferedWriter.write(lineas.get(i));
                    bufferedWriter.newLine();
                }
                bufferedWriter.close();
                Alert ale = new Alert(Alert.AlertType.INFORMATION);
                ale.setHeaderText("Información");
                ale.setContentText("Elemento eliminado al final de la lista y del archivo!");
                ale.showAndWait();
                tabla.refresh();
            } catch (IOException e) {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setHeaderText("Error al eliminar");
                alerta.setContentText("Se produjo un error al eliminar el elemento del archivo.");
                alerta.showAndWait();
            }
        }
    }

    @FXML
    private void histori(ActionEvent event) {
        if (historial.isEmpty()) {
            // Mostrar mensaje de advertencia si no hay compras en el historial
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setHeaderText("Historial de Compras");
            alerta.setContentText("No hay compras registradas en el historial.");
            alerta.showAndWait();
        } else {
            // Crear una cadena que contenga el historial de compras
            StringBuilder historia = new StringBuilder();
            for (String compra : historial) {
                historia.append(compra).append("\n");
            }

            // Mostrar el historial de compras en una alerta
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setHeaderText("Historial de Compras");
            alerta.setContentText(historia.toString());
            alerta.showAndWait();
        }
    }

    @FXML
    private void agregarunidades(ActionEvent event) {
        TextInputDialog dialogo = new TextInputDialog("");
        dialogo.setTitle("Modificar Unidades de Producto");
        dialogo.setHeaderText(null);
        dialogo.setContentText("Ingrese la ID del producto a modificar unidades:");
        Optional<String> idProducto = dialogo.showAndWait();

        if (idProducto.isPresent()) {
            String idBuscada = idProducto.get();
            nodo productoEncontrado = null;
            nodo primerNodo = nodos.get(0);
            nodo nodoActual = primerNodo;
            do {
                if (nodoActual.getId().equals(idBuscada)) {
                    productoEncontrado = nodoActual;
                    break;
                }
                nodoActual = nodoActual.getSig();
            } while (nodoActual != primerNodo);
            if (productoEncontrado != null) {
                TextInputDialog dialogoUnidades = new TextInputDialog("");
                dialogoUnidades.setTitle("Modificar Unidades");
                dialogoUnidades.setHeaderText(null);
                dialogoUnidades.setContentText("Ingrese la nueva cantidad de unidades:");

                Optional<String> nuevasUnidades = dialogoUnidades.showAndWait();
                if (nuevasUnidades.isPresent()) {
                    try {
                        int cantidadNueva = Integer.parseInt(nuevasUnidades.get());

                        if (cantidadNueva < 0) {
                            Alert alerta = new Alert(Alert.AlertType.WARNING);
                            alerta.setHeaderText("Cantidad inválida");
                            alerta.setContentText("La cantidad de unidades no puede ser negativa.");
                            alerta.showAndWait();
                        } else {
                            int unidadesAnteriores = productoEncontrado.getUnidades();
                            productoEncontrado.setUnidades(cantidadNueva);
                            ActualizarDatosArchivo(productoEncontrado, unidadesAnteriores - cantidadNueva);
                            tabla.refresh();
                            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                            alerta.setHeaderText("Unidades modificadas");
                            alerta.setContentText("Las unidades del producto con ID "
                                    + idBuscada + " se han modificado correctamente.");
                            alerta.showAndWait();
                        }
                    } catch (NumberFormatException e) {
                        // Mostrar mensaje si la entrada no es un número válido
                        Alert alerta = new Alert(Alert.AlertType.ERROR);
                        alerta.setHeaderText("Error");
                        alerta.setContentText("Ingrese un número válido para las unidades.");
                        alerta.showAndWait();
                    }
                }
            } else {
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setHeaderText("Producto no encontrado");
                alerta.setContentText("No se encontró un producto con la ID ingresada.");
                alerta.showAndWait();
            }
        }
    }

    private void ActualizarDatosArchivo(nodo producto, int cantidadComprada) {
        String archivoRuta = "src/farmacia/Farmacos.txt";
        File archivo = new File(archivoRuta);
        try {
            ObservableList<String> lineas = FXCollections.observableArrayList();
            Scanner scanner = new Scanner(archivo);
            while (scanner.hasNextLine()) {
                lineas.add(scanner.nextLine());
            }
            scanner.close();
            for (int i = 0; i < lineas.size(); i++) {
                String[] elementos = lineas.get(i).split(",");
                if (elementos.length >= 5 && elementos[2].equals(producto.getId())) {
                    int unidades = Integer.parseInt(elementos[4]);
                    unidades -= cantidadComprada;
                    elementos[4] = Integer.toString(unidades);
                    lineas.set(i, String.join(",", elementos));
                    break;
                }
            }
            FileWriter fileWriter = new FileWriter(archivoRuta);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (String linea : lineas) {
                bufferedWriter.write(linea);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void pmasvendido(ActionEvent event) {
        String productoMasVendido = "";
        int maxVentas = 0;

        for (Map.Entry<String, Integer> entry : ventasPorProducto.entrySet()) {
            String productoID = entry.getKey();
            int ventas = entry.getValue();

            if (ventas > maxVentas) {
                maxVentas = ventas;
                productoMasVendido = productoID;
            }
        }

        if (!productoMasVendido.isEmpty()) {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setHeaderText("Producto más vendido:");
            alerta.setContentText("El producto más vendido: " + productoMasVendido);
            alerta.showAndWait();
        } else {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setHeaderText("No hay datos de ventas");
            alerta.setContentText("Aún no se ha registrado ninguna venta.");
            alerta.showAndWait();
        }
    }

    private void registrarCompraEnHistorial(nodo producto, int cantidad, double total) {
        String detalleCompra = "Compra: " + producto.getTipo() + ", Cantidad: " + cantidad + ", Total: $" + total;

        // Agregar el detalle de la compra al historial
        historial.add(detalleCompra);
    }

    @FXML
    private void Comprar(ActionEvent event) {
        nodo productoSeleccionado = tabla.getSelectionModel().getSelectedItem();

        if (productoSeleccionado == null) {
            // Mostrar mensaje de advertencia si no se ha seleccionado ningún producto
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setHeaderText("Producto no seleccionado");
            alerta.setContentText("Seleccione un producto de la tabla para comprar.");
            alerta.showAndWait();
            return;
        }

        TextInputDialog dialogo = new TextInputDialog("");
        dialogo.setTitle("Cantidad a comprar");
        dialogo.setHeaderText(null);
        dialogo.setContentText("Ingrese la cantidad a comprar (disponibles: " + productoSeleccionado.getUnidades() + "):");
        Optional<String> cantidad = dialogo.showAndWait();

        if (!cantidad.isPresent()) {
            return;
        } else if (!cantidad.get().matches("^[1-9]\\d*$")) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setHeaderText("Entrada inválida");
            alerta.setContentText("La cantidad de unidades debe ser un número entero positivo.");
            alerta.showAndWait();
            return;
        }

        int cantidadComprar = Integer.parseInt(cantidad.get());

        if (cantidadComprar > productoSeleccionado.getUnidades()) {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setHeaderText("Cantidad no disponible");
            alerta.setContentText("La cantidad de unidades seleccionada no está disponible.");
            alerta.showAndWait();
            return;
        }

        // Realizar la compra: Actualizar la cantidad de unidades en el producto seleccionado
        productoSeleccionado.setUnidades(productoSeleccionado.getUnidades() - cantidadComprar);

        // Actualizar el archivo con la nueva cantidad de unidades
        ActualizarDatosArchivo(productoSeleccionado, cantidadComprar);

        double totalPagar = productoSeleccionado.getPrecio() * cantidadComprar;
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setHeaderText("Confirmar compra");
        confirmacion.setContentText("Precio unitario: $" + productoSeleccionado.getPrecio() + "\nCantidad a comprar: " + cantidadComprar + "\nTotal a pagar: $" + totalPagar);
        Optional<ButtonType> result = confirmacion.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Registrar la compra en el historial (aquí puedes implementar tu lógica para el historial)
            registrarCompraEnHistorial(productoSeleccionado, cantidadComprar, totalPagar);
        }

        if (productoSeleccionado.getUnidades() <= 0) {
            nodos.remove(productoSeleccionado);

            tabla.setItems(null);
            tabla.layout();
            tabla.setItems(FXCollections.observableList(nodos));

            Alert informacion = new Alert(Alert.AlertType.INFORMATION);
            informacion.setHeaderText("Unidades agotadas");
            informacion.setContentText("No quedan unidades disponibles para este producto.");
            informacion.showAndWait();
        }

        tabla.refresh();
    }
}

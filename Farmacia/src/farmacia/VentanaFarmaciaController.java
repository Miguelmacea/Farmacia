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
}

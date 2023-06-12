/**
 * Sample Skeleton for 'Crimes.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Coppia;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class CrimesController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxCategoria"
    private ComboBox<String> boxCategoria; // Value injected by FXMLLoader

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="boxArco"
    private ComboBox<Coppia> boxArco; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	this.boxArco.getItems().clear();
    	String categoria = this.boxCategoria.getValue();
    	Integer anno = this.boxAnno.getValue();
    	if(categoria=="" || anno==null) {
    		this.txtResult.setText("Selezionare i valori richiesti!");
    		return;
    	}
    	try {
    		model.creaGrafo(categoria,anno);
    		this.txtResult.setText("Grafo creato!\n");
    		this.txtResult.appendText("# Vertici: "+this.model.getVerticiSize()+"\n");
    		this.txtResult.appendText("# Archi: "+this.model.getArchiSize()+"\n");
    		this.txtResult.appendText("\nArchi con peso massimo:\n");
    		List<Coppia> l = model.getArchiMax();
    		for(Coppia c : l) {
    			this.txtResult.appendText(c+"\n");
    		}
    		this.boxArco.getItems().addAll(l);
    	} catch(NumberFormatException e){
    		this.txtResult.setText("Valore inserito non valido!");
    		return;
    	}
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	this.txtResult.clear();
    	Coppia c = this.boxArco.getValue();
    	if(c==null) {
    		this.txtResult.setText("Selezionare un arco!");
    		return;
    	}
    	String e1 = c.getE1();
    	String e2 = c.getE2();
    	List<String> l = model.percorso(e1, e2);
    	for(String s : l) {
    		this.txtResult.appendText(s+"\n");
    	}
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Crimes.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.boxCategoria.getItems().addAll(model.getCategorie());
    	this.boxAnno.getItems().addAll(model.getAnni());
    }
}

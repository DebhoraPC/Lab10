package it.polito.tdp.porto;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class PortoController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Author> boxPrimo;

    @FXML
    private ComboBox<Author> boxSecondo;

    @FXML
    private TextArea txtResult;
    

    @FXML
    void handleCoautori(ActionEvent event) {
    	
    	if (boxPrimo.getValue() == null) {
    		txtResult.appendText("Selezionare il Primo Autore.\n");
    		return;
    	}
    	
    	Author primo = boxPrimo.getValue();
    	
    	model.creaGrafo();
    	
    	List<Author> coautori = model.getVerticiAdiacenti(primo);
    	
    	if (coautori == null || coautori.isEmpty()) {
    		txtResult.appendText("L'autore " + primo.toString() + " non ha co-autori.\n");
    		return;
    	}
    		
    	for (Author co: coautori)
    		txtResult.appendText(co.toString());

    }

    @FXML
    void handleSequenza(ActionEvent event) {
    	
    	// ESERCIZIO 2

    }

    @FXML
    void initialize() {
        assert boxPrimo != null : "fx:id=\"boxPrimo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert boxSecondo != null : "fx:id=\"boxSecondo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Porto.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		
	}
}

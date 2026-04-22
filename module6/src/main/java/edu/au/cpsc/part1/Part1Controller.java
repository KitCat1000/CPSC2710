/* CPSC
 * Project: CPSC 2710 - Module 6 JavaFX Assignment
 * Author: Nicole Tressler
 * Auburn Email: nit0005@auburn.edu
 * Date: April 20, 2026
 * Description: Practice with JavaFX properties and bindings.
 *
 */
package edu.au.cpsc.part1;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;


public class Part1Controller implements Initializable {


    // Unidirectional Binding Example
    @FXML
    private TextField messageTextField;

    @FXML
    private TextField echoTextField;

    // Bi-directional Binding Example
    @FXML
    private TextField firstBidirectionalTextField;

    @FXML
    private TextField secondBidirectionalTextField;

    // Numeric Binding Example (Slider to Image Opacity)
    @FXML
    private Slider secretSlider;

    @FXML
    private ImageView secretOverlapImageView;

    // Boolean to String Binding Example
    @FXML
    private CheckBox selectMeCheckBox;

    @FXML
    private Label selectMeLabel;

    // Computed Property Binding Example (Length)
    @FXML
    private TextField tweetTextField;

    @FXML
    private Label numberOfCharactersLabel;

    // Conditional Binding Example (Computed + When/Then/Otherwise)
    @FXML
    private Label validityLabel;


    /**
     * @param location  The location used to resolve relative paths for the root object
     * @param resources The resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        echoTextField.textProperty().bind(messageTextField.textProperty());


        firstBidirectionalTextField.textProperty()
                .bindBidirectional(secondBidirectionalTextField.textProperty());


        secretOverlapImageView.opacityProperty().bind(secretSlider.valueProperty());


        selectMeLabel.textProperty().bind(
                selectMeCheckBox.selectedProperty().asString()
        );


        numberOfCharactersLabel.textProperty().bind(
                tweetTextField.textProperty().length().asString()
        );


        validityLabel.textProperty().bind(
                Bindings.when(tweetTextField.textProperty().length().lessThanOrEqualTo(10))
                        .then("Valid")
                        .otherwise("Invalid")
        );

    }

}
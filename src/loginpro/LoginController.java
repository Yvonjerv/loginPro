package loginpro;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoginController implements Initializable {

    @FXML
    private JFXButton lbl_close;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btn_forgot;

    @FXML
    private Button btn_facebook;

    @FXML
    private Button btnSignup;

    @FXML
    private Label lblErrors;

    @FXML
    private JFXButton btnSignin;

    @FXML
    private ImageView  btnClose;

    /// -- 
    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    @FXML
    public void handleButtonAction(ActionEvent event) {

        if (event.getSource() == btnSignin) {
            logIn();
            //login here
            if (status == "Success") {
                try {
                    //add you loading or delays - ;-)
                    Node node = (Node) event.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    //stage.setMaximized(true);
                    stage.close();
                    Scene scene = new Scene(FXMLLoader.load(getClass().getResource("OnBoard.fxml")));
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }

            }
        }

        if (event.getSource() == btnClose) {
            System.exit(0);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if (con == null) {
            lblErrors.setTextFill(Color.TOMATO);
            lblErrors.setText("Server Error : Check");
        } else {
            lblErrors.setTextFill(Color.GREEN);
            lblErrors.setText("Server is up : Good to go");
        }
    }

    public LoginController() throws SQLException {
        con = DBConnector.conDB();
    }

    //we gonna use string to check for status
    String status;

    private void logIn() {

        status = "Success";
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (username.length() == 0 || password.length() == 0) {
            lblErrors.setTextFill(Color.TOMATO);
            lblErrors.setText("Empty credentials");
            status = "Error";
        } else {
            //query
            String sql = "SELECT * FROM LOGIN WHERE username=? and password =?";

            try {
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

                resultSet = preparedStatement.executeQuery();

                if (!resultSet.next()) {
                    lblErrors.setTextFill(Color.TOMATO);
                    lblErrors.setText("Enter correct email/password");
                    status = "Error";
                } else {
                    lblErrors.setTextFill(Color.GREEN);
                    lblErrors.setText("Login Successful..Redirecting..");

                }
            } catch (SQLException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);

                status = "Exception";
            }
        }

    }
}

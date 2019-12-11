package application;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaView;

import javax.sound.sampled.*;
import java.io.*;
import java.sql.*;

public class MainGuiControl {
	@FXML
    private Button btnSearch;

    @FXML
    private TextField txtStatus;
    @FXML
    private TextField txtSearch;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtName2;
    @FXML
    private TextField txtOrigin;
    @FXML
    private TextArea txtDescription;

    @FXML
    private ImageView imgView;
    @FXML
    private Button btnHowTo;

    private static String pathname = "";
    private static final String DB_PATH = System.getProperty("user.dir") + "/" + "animalsDatabase.db";
    private static Connection connection;

    @FXML
    private void search() {
        txtDescription.clear();
        txtOrigin.clear();
        txtStatus.clear();
        txtName.clear();
        txtName2.clear();
        imgView.setImage(null);

        try {
            Statement stmt = connection.createStatement();
            String s_query = "SELECT * FROM animals WHERE name LIKE '%" + txtSearch.getText() + "%';";
            System.out.println(s_query);
            ResultSet rs = stmt.executeQuery(s_query);
            if (rs.getFetchSize() == 0) {
                txtStatus.setText("Zu deiner Suchanfrage hat das Lexikon leider nichts gefunden!");
                Image image = new Image(getClass().getResourceAsStream("/images/404.png"));
                imgView.setImage(image);
            }
            int i = 0;
            while (rs.next() && i == 0) {
                i++;
                txtStatus.setText("Zu deiner Suchanfrage passt am ehesten folgendes Tier: ");
                txtName.setText(rs.getString("name"));
                txtName2.setText(rs.getString("detailName"));
                txtOrigin.setText(rs.getString("origin"));

                // Load Image
                txtDescription.setText(rs.getString("description"));
                InputStream input = rs.getBinaryStream("image");
                Image image = new Image(input);
                imgView.setImage(image);

                //Load Audio File
                InputStream fis = rs.getBinaryStream("voice");
                pathname = System.getProperty("user.dir") + "/src/voices/" + txtName.getText() + ".wav";
                try {
                    File file = new File(pathname);
                    FileOutputStream fileoutputstream = new FileOutputStream(file);
                    int j;
                    while((j = fis.read()) != -1)
                        fileoutputstream.write(j);
                    fileoutputstream.close();
                }
                catch(Exception exception) {
                    exception.printStackTrace();
                }



            }

            rs.close();

        } catch (SQLException e) {
            System.err.println("Couldn't handle DB-Query");
            e.printStackTrace();
        }

    }

    @FXML
    public void doIt(){
        try {
            Clip sound = AudioSystem.getClip();
            File file = new File(pathname);
            sound.open(AudioSystem.getAudioInputStream(file));
            sound.start();
        }
        catch (UnsupportedAudioFileException e1) {
            e1.printStackTrace();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        catch (LineUnavailableException e3) {
            e3.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        btnSearch.disableProperty().bind(
                Bindings.isEmpty(txtSearch.textProperty())
        );

        btnHowTo.disableProperty().bind(
                Bindings.isEmpty(txtName.textProperty())
        );

        try {
            Class.forName("org.sqlite.JDBC");
            if (connection != null)
                return;
            System.out.println("Creating Connection to Database...");
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
            if (!connection.isClosed())
                System.out.println("...Connection established");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            System.err.println("Fehler beim Laden des JDBC-Treibers");
            e.printStackTrace();
        }
    }

    private void playSound(String file) {

    }
}

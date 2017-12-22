/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.musik.calendartools.ui;

import de.biware.pf.stadtkirche.musik.calendartools.ui.service.ConvertException;
import de.biware.pf.stadtkirche.musik.calendartools.ui.service.ConverterService;
import de.biware.pf.stadtkirche.musik.calendartools.ui.service.ICalendarProbenplanConverterService;
import de.biware.pf.stadtkirche.musik.calendartools.ui.service.PDFProbenplanConverterService;
import de.biware.pf.stadtkirche.musik.calendartools.ui.service.ProcessWireCSVConverterService;
import de.biware.pf.stadtkirche.musik.calendartools.ui.service.WordProbenplanConverterService;
import de.biware.pf.stadtkirche.nusik.calendartools.observer.ConverterProgressObserver;
import de.biware.pf.stadtkirche.nusik.calendartools.observer.ReaderWriterMessageObserver;
import de.biware.pf.stadtkirche.nusik.calendartools.reader.ExcelEnsembleDetectionFactory;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.prefs.Preferences;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

/**
 *
 * @author svenina
 */
public class MainStage extends Application implements ReaderWriterMessageObserver, ConverterProgressObserver {

    private static String EMSBLE_SHEET_NAME = "Ensembles";
    
    //private TextField spinnerDay;
    private CheckBox shouldValidate;
    private CheckBox cbCsv;
    private TextField tfOutputDirCsv;
    private CheckBox cbProbenplanPDF;
    private TextField tfOutputDirPDF;
    private CheckBox cbProbenplanWord;
    private TextField tfOutputDirWord;
    private CheckBox cbProbenplanICS;
    private TextField tfOutputDirICS;
    private TextField tfExcelFile;
    private ListView<String> listMessages;
    private TextField tfSheetName;
    private Button buttonStartConvert;
    private Button openLogFile;
    private ProgressBar progressBar;
    private double maxProgressCount;
    private double currentProgressCount;

    private final Logger logger = Logger.getLogger(this.getClass());

    @Override
    public void start(Stage primaryStage) throws Exception {

        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane);

        scene.getStylesheets().add("styles.css");

        this.initUI(borderPane, primaryStage);

        primaryStage.setTitle("Kalenderwerkzeuge 'Musik! an der Stadtkirche Pforzheim'");
        primaryStage.setScene(scene);
        primaryStage.setWidth(1280);
        primaryStage.setHeight(800);
        primaryStage.show();
    }

    private void initUI(BorderPane borderPane, Stage primaryStage) {
        this.createInputArea(borderPane, primaryStage);
        this.createOutputChanelArea(borderPane);
    }

    private void createInputArea(BorderPane borderPane, Stage primaryStage) {
        HBox hboxInput = new HBox(15);
        HBox hboxInputRow2 = new HBox();
        hboxInputRow2.getStyleClass().add("pane");
        hboxInput.getStyleClass().add("pane");
        tfExcelFile = new TextField();
        tfExcelFile.setPrefWidth(700);
        //tfExcelFile.setText(this.readLastPathSelection());
        tfSheetName = new TextField("Gesamtprobenplan");
        tfSheetName.setPrefWidth(300);
        //spinnerDay = new TextField("364");
        Button tfChooseExcelFile = new Button("...");
        shouldValidate = new CheckBox("Validierung (experimentell)");

        hboxInput.getChildren().addAll(new Label("Exceldatei"), tfExcelFile, tfChooseExcelFile, new Label("Tabellenblatt"), tfSheetName);
        //hboxInputRow2.getChildren().addAll(new Label("testweise Tagesdifferenz"), spinnerDay);
        hboxInputRow2.getChildren().addAll(shouldValidate);

        VBox vbBoxContainer = new VBox(15);
        vbBoxContainer.getChildren().addAll(this.createMenubar(), hboxInput, hboxInputRow2, new Separator());
        //vbBoxContainer.getChildren().addAll(this.createMenubar(), hboxInput, new Separator());

        borderPane.setTop(vbBoxContainer);

        this.installExcelFileChooser(tfChooseExcelFile, tfExcelFile, primaryStage);

    }

    private void installExcelFileChooser(Button onButton, TextField tfExcelFile, Stage stage) {
        final FileChooser fileChooser = new FileChooser();
        File initialDirectory = new File(this.readLastPathSelection());
        if (!initialDirectory.isDirectory()) {
            initialDirectory = initialDirectory.getParentFile();
        }
        if (!initialDirectory.exists()) {
            initialDirectory = new File(System.getProperty("user.home"));
        }
        fileChooser.setInitialDirectory(initialDirectory);
        onButton.setOnAction((final ActionEvent e) -> {
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                tfExcelFile.setText(file.getAbsolutePath());
                tfOutputDirCsv.setText(file.getParent() + "/csv/");
                tfOutputDirPDF.setText(file.getParent() + "/pdf/");
                tfOutputDirWord.setText(file.getParent() + "/word/");
                tfOutputDirICS.setText(file.getParent() + "/ics");
            }
        });
    }

    private void createOutputChanelArea(BorderPane borderPane) {
        this.progressBar = new ProgressBar(0);
        this.progressBar.prefWidthProperty().bind(borderPane.widthProperty());

        this.cbCsv = new CheckBox("CSV-Export für ProcessWire");
        this.tfOutputDirCsv = new TextField();
        HBox hboxCsv = new HBox(15, cbCsv, new Label("Ausgabeverzeichnis"), tfOutputDirCsv, createOpenDirectoryButton(tfOutputDirCsv));

        this.cbProbenplanPDF = new CheckBox("Probenpläne als PDF");
        this.tfOutputDirPDF = new TextField();
        HBox hboxPdf = new HBox(15, cbProbenplanPDF, new Label("Ausgabeverzeichnis"), tfOutputDirPDF, createOpenDirectoryButton(tfOutputDirPDF));

        this.cbProbenplanWord = new CheckBox("Probenpläne als Word");
        this.tfOutputDirWord = new TextField();
        HBox hboxWord = new HBox(15, cbProbenplanWord, new Label("Ausgabeverzeichnis"), tfOutputDirWord, createOpenDirectoryButton(tfOutputDirWord));

        this.cbProbenplanICS = new CheckBox("Probenplände als iCalendar");
        this.tfOutputDirICS = new TextField();
        HBox hboxICS = new HBox(15, cbProbenplanICS, new Label("Ausgabeverzeichnis"), tfOutputDirICS, createOpenDirectoryButton(tfOutputDirICS));

        buttonStartConvert = new Button("starte Konvertierung");
        buttonStartConvert.setOnAction((ActionEvent e) -> {
            Platform.runLater(() -> {
                //button.setDisable(true);
                handleConvertExcelFileGesture();
                //button.setDisable(false);
            });
        });

        openLogFile = new Button("öffne Logdatei");
        openLogFile.setOnAction((event) -> {
            Platform.runLater(() -> {
                handleOpenLogfileGesture();
            });
        });

        listMessages = new ListView<>();
        listMessages.prefWidthProperty().bind(borderPane.prefWidthProperty());
        
        HBox buttonBox = new HBox(15);
        buttonBox.getChildren().addAll(buttonStartConvert, openLogFile);

        VBox vbox = new VBox(15);
        vbox.getStyleClass().add("pane");
        vbox.getChildren().addAll(hboxCsv, hboxPdf, hboxWord, hboxICS, new Separator(), buttonBox, new Separator(), progressBar, listMessages);

        borderPane.setCenter(vbox);

        //-- enable proerties
        this.setupOutputChannelTextFieldBehaviour(tfOutputDirCsv, cbCsv);
        this.setupOutputChannelTextFieldBehaviour(tfOutputDirPDF, cbProbenplanPDF);
        this.setupOutputChannelTextFieldBehaviour(tfOutputDirWord, cbProbenplanWord);
        this.setupOutputChannelTextFieldBehaviour(tfOutputDirICS, cbProbenplanICS);
    }

    private Button createOpenDirectoryButton(TextField tf) {
        Button button = new Button("Zielordner öffnen");

        button.setOnAction((ActionEvent e) -> {
            final File dir = new File(tf.getText() + "/");
            if (Desktop.isDesktopSupported()) {
                Platform.runLater(() -> {
                    try {
                        Desktop.getDesktop().open(dir);
                    } catch (IOException ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                });
            }
        });

        return button;
    }

    private MenuBar createMenubar() {
        Menu menuDatei = new Menu("Datei");
        MenuItem menuExit = new MenuItem("Beenden");
        menuDatei.getItems().addAll(menuExit);

        menuExit.setOnAction((ActionEvent e) -> {
            System.exit(0);
        });

        Menu menuHilfe = new Menu("Hilfe");
        MenuItem menuAbout = new MenuItem("Produktinformation");
        menuHilfe.getItems().addAll(menuAbout);

        menuAbout.setOnAction((ActionEvent e) -> {
            Stage stage = new Stage();
            stage.setResizable(false);
            AboutBox aboutBox = new AboutBox(stage, "Kalenderwerkzeuge", getClass().getPackage().getImplementationVersion());
            stage.showAndWait();
        });

        MenuBar menubar = new MenuBar(menuDatei, menuHilfe);

        return menubar;
    }

    private void setupOutputChannelTextFieldBehaviour(TextField tf, CheckBox cb) {
        tf.setDisable(true);
        tf.editableProperty().bind(cb.selectedProperty());
        cb.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            tf.setDisable(!newValue);
        });
        cb.setPrefWidth(200);
        tf.setPrefWidth(700);
    }

    public static void main(String... args) {
        MDC.put("user", System.getProperty("user.name"));
        launch(args);
    }

    private void info(final String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setContentText(message);
            alert.show();
        });
    }

    private void error(final String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText(message);
            alert.show();
        });
    }

    private void error(final String message, Throwable ex) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle(message);
            alert.setHeaderText(ex.getClass().getSimpleName());
            alert.setContentText(ex.getLocalizedMessage());

            // Create expandable Exception.
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String exceptionText = sw.toString();

            Label label = new Label("The exception stacktrace was:");

            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

            // Set expandable Exception into the dialog pane.
            alert.getDialogPane().setExpandableContent(expContent);

            alert.show();
        });
    }

    private void disableStartButton(final boolean disable) {
        Platform.runLater(() -> {
            buttonStartConvert.setDisable(disable);
        });
    }

    private void handleOpenLogfileGesture() {
        File logFile = new File("xls2csv.log");
        if (Desktop.isDesktopSupported()) {
            Platform.runLater(() -> {
                try {
                    Desktop.getDesktop().open(logFile);
                } catch (Throwable ex) {
                    logger.error(ex.getMessage(), ex);
                    error(ex.getMessage() + ": " + logFile.getAbsolutePath(), ex);
                }
            });
        }

    }

    private void handleConvertExcelFileGesture() {
        Thread t = new Thread(() -> {
            disableStartButton(true);
            try {
                _handleConvertExcelFileGesture();
                info("Konvertierung erfolgreich abgeschlossen");

            } catch (ConvertException ex) {
                logger.error(ex.getMessage(), ex);
                Platform.runLater(() -> {
                    listMessages.getItems().add(ex.getMessage());
                    logger.error(ex.getMessage());
                });
                //error("Fehler bei der Konvertierung. Das Ergebnis ist ggf. unvollständig!", ex);
                error("Fehler bei der Konvertierung. Ggf. sind Validierungsfehler aufgetreten und bzw. oder das Ergebnis ist unvollständig. Bitte beachten Sie die Meldungsausgabe.");
            } finally {
                disableStartButton(false);
            }
        });
        t.start();
    }

    private void _handleConvertExcelFileGesture() throws ConvertException {
        String tabellenBlatt = tfSheetName.getText();
        this.rememberLastPathSelection(new File(tfExcelFile.getText()));
        MDC.put("document", new File(tfExcelFile.getText()).getName());
        MDC.put("username", System.getProperty("user.name"));
        if (cbCsv.isSelected()) {
            this.progressBar.setProgress(0);
            ConverterService service = new ProcessWireCSVConverterService(shouldValidate.isSelected(), new ExcelEnsembleDetectionFactory(new File(tfExcelFile.getText()), EMSBLE_SHEET_NAME));
            //int days = this.spinnerDay.getValue().intValue();
            //if(!this.spinnerDay.getText().isEmpty()) {
            //    ((ProcessWireCSVConverterService)service).setCalendarEventProcessor(new DateCalendarEventManipulatingProcessor(Integer.parseInt(this.spinnerDay.getText())));
            //}
            service.registerReaderWriterMessageObserver(this);
            service.registerConverterProgressObserver(this);
            service.convert(new File(tfExcelFile.getText()), new File(tfOutputDirCsv.getText()), tabellenBlatt);
        }

        if (cbProbenplanPDF.isSelected()) {
            this.progressBar.setProgress(0);
            ConverterService service = new PDFProbenplanConverterService(shouldValidate.isSelected(),new ExcelEnsembleDetectionFactory(new File(tfExcelFile.getText()), EMSBLE_SHEET_NAME));
            service.registerReaderWriterMessageObserver(this);
            service.registerConverterProgressObserver(this);
            service.convert(new File(tfExcelFile.getText()), new File(tfOutputDirPDF.getText()), tabellenBlatt);
        }

        if (cbProbenplanWord.isSelected()) {
            this.progressBar.setProgress(0);
            ConverterService service = new WordProbenplanConverterService(shouldValidate.isSelected(), new ExcelEnsembleDetectionFactory(new File(tfExcelFile.getText()), EMSBLE_SHEET_NAME));
            service.registerReaderWriterMessageObserver(this);
            service.registerConverterProgressObserver(this);
            service.convert(new File(tfExcelFile.getText()), new File(tfOutputDirWord.getText()), tabellenBlatt);
        }

        if (cbProbenplanICS.isSelected()) {
            this.progressBar.setProgress(0);
            ConverterService service = new ICalendarProbenplanConverterService(shouldValidate.isSelected(), new ExcelEnsembleDetectionFactory(new File(tfExcelFile.getText()), EMSBLE_SHEET_NAME));
            service.registerReaderWriterMessageObserver(this);
            service.registerConverterProgressObserver(this);
            service.convert(new File(tfExcelFile.getText()), new File(tfOutputDirICS.getText()), tabellenBlatt);
        }

        this.onMessage(getClass().getSimpleName(), "Konvertierung ist beendet");
    }

    private void rememberLastPathSelection(File excelFile) {
        Preferences.userRoot().put(this.getClass().getName() + ".execelfile", excelFile.getAbsolutePath());
    }

    private String readLastPathSelection() {
        return Preferences.userRoot().get(this.getClass().getName() + ".execelfile", System.getProperty("user.home"));
    }

    @Override
    public void onMessage(String from, String message) {
        Platform.runLater(() -> {
            listMessages.getItems().add("[" + from + "] " + message);

        });
        logger.info(message);
    }

    @Override
    public void setCompleteCount(int count) {
        this.maxProgressCount = count;
        this.currentProgressCount = 0;
    }

    @Override
    public void onBeforeConvert() {
        // nop
    }

    @Override
    public void onAfterContert() {
        Platform.runLater(() -> {
            ++currentProgressCount;
            double progress = currentProgressCount / maxProgressCount;
            this.progressBar.setProgress(progress);
        });

    }
}

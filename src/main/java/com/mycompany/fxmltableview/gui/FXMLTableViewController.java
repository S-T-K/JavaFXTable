package com.mycompany.fxmltableview.gui;

import com.mycompany.fxmltableview.datamodel.Batch;
import com.mycompany.fxmltableview.datamodel.Dataset;
import com.mycompany.fxmltableview.datamodel.Entry;
import com.mycompany.fxmltableview.datamodel.Entry.orderbyRT;
import com.mycompany.fxmltableview.datamodel.Peak;
import com.mycompany.fxmltableview.datamodel.RawDataFile;
import com.mycompany.fxmltableview.datamodel.Reference;
import com.mycompany.fxmltableview.logic.CertaintyCalculator;
import com.mycompany.fxmltableview.logic.Session;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import com.univocity.parsers.tsv.TsvWriter;
import com.univocity.parsers.tsv.TsvWriterSettings;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeSortMode;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;
import org.apache.commons.io.FileUtils;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.textfield.TextFields;

//this is the Controller for the Main GUI
public class FXMLTableViewController implements Initializable {

    //link fxml information to controller
    @FXML
    private TreeTableView<Entry> metTable;

    @FXML
    TreeTableColumn nameColumn;

    @FXML
    TreeTableColumn numColumn, scoreColumn, scorepeakfoundColumn, scorepeakcloseColumn, scorecertaintyColumn, scorepeakrangeColumn, scorefitaboveColumn;

    @FXML
    TreeTableColumn rtColumn;

    @FXML
    TreeTableColumn mzColumn;

    @FXML
    Accordion accordion;

    @FXML
    public Button addBatchButton, paramButton, shiftButton, outputButton;

    @FXML
    public Button parameterButton, referenceMatrixButton, referenceFilesButton, batchFilesButton, checkResultsButton, p1, p2, p3, p4, p5, p6;

    @FXML
    ProgressBar progressbar;

    @FXML
    TextField Scales, MinSignals, MinConsSignals, RTTol, MZTol, SliceMZTol, Res, Base, RTTolShift, Start, End, Noise, AdName1, AdName2, AdName3, AdName4, AdName5, AdName6, AdName7, AdName8, AdName9, AdName10, AdName11, AdName12, AdName13, AdName14, AdName15, AdName16, AdName17, AdName18, AdName19, AdName20, AdName21, AdName22, AdName23, AdName24, AdName25, AdName26, AdName27, AdName28, AdName29, AdName30, AdName31, AdName32, AdName33, AdName34, AdName35, AdName36, AdName37, AdName38, AdName39, AdName40, AdName41, AdName42, AdMass1, AdMass2, AdMass3, AdMass4, AdMass5, AdMass6, AdMass7, AdMass8, AdMass9, AdMass10, AdMass11, AdMass12, AdMass13, AdMass14, AdMass15, AdMass16, AdMass17, AdMass18, AdMass19, AdMass20, AdMass21, AdMass22, AdMass23, AdMass24, AdMass25, AdMass26, AdMass27, AdMass28, AdMass29, AdMass30, AdMass31, AdMass32, AdMass33, AdMass34, AdMass35, AdMass36, AdMass37, AdMass38, AdMass39, AdMass40, AdMass41, AdMass42, AdM1, AdM2, AdM3, AdM4, AdM5, AdM6, AdM7, AdM8, AdM9, AdM10, AdM11, AdM12, AdM13, AdM14, AdM15, AdM16, AdM17, AdM18, AdM19, AdM20, AdM21, AdM22, AdM23, AdM24, AdM25, AdM26, AdM27, AdM28, AdM29, AdM30, AdM31, AdM32, AdM33, AdM34, AdM35, AdM36, AdM37, AdM38, AdM39, AdM40, AdM41, AdM42, AdC1, AdC2, AdC3, AdC4, AdC5, AdC6, AdC7, AdC8, AdC9, AdC10, AdC11, AdC12, AdC13, AdC14, AdC15, AdC16, AdC17, AdC18, AdC19, AdC20, AdC21, AdC22, AdC23, AdC24, AdC25, AdC26, AdC27, AdC28, AdC29, AdC30, AdC31, AdC32, AdC33, AdC34, AdC35, AdC36, AdC37, AdC38, AdC39, AdC40, AdC41, AdC42;

    @FXML
    Label labelmin, dislabel, label1, label2, label3, label4, label5, label6, label7, label8, label9, label10, label11, noiselabel, Adlabel1, Adlabel2, Adlabel3, Adlabel4, Adlabel5, Adlabel6, Adlabel7, Adlabel8, Adlabel9, Adlabel10, Adlabel11, Adlabel12, Adlabel13, Adlabel14, Adlabel15, Adlabel16, Adlabel17, Adlabel18, Adlabel19, Adlabel20, Adlabel21, Adlabel22, Adlabel23, Adlabel24, Adlabel25, Adlabel26, Adlabel27, Adlabel28, Adlabel29, Adlabel30, Adlabel31, Adlabel32, Adlabel33, Adlabel34, Adlabel35, Adlabel36, Adlabel37, Adlabel38, Adlabel39, Adlabel40, Adlabel41, Adlabel42;

    @FXML
    Rectangle box1, box2, box3, box4;

    @FXML
    public ChoiceBox PeakPick;

    @FXML
    public CheckBox toggleadductgeneration, changedcheckbox;

    @FXML
    TabPane TabPane;

    @FXML
    AnchorPane adductanchor, inputTab, indicatorbar, paramanchor;

    @FXML
    TableView<Information> InputTable;

    @FXML
    TableView<OutputFormat> option2table;

    @FXML
    TableColumn infocol, headcol, C1, C2, C3, C4, C5, C6, C7, C8, C9, C10;

    @FXML
    RadioButton option1, option2;

    @FXML
    SplitPane splitpane;

    @FXML
    Pane maskerpane;

    @FXML
    public MenuItem saveP, loadP, restoreP;

    private ObservableList<OutputFormat> outputformat;

    //Check for changed parameters
    String oldPick = "init";
    String oldBase = "init";
    String oldRT = "init";
    String oldSN = "init";
    String oldScales = "init";

    //List with MasterListofOGroups for table, Ogroups (adducts within the Ogroups)
    private ObservableList<Entry> MasterListofOGroups;

    //current session, storing all information
    Session session;
    HashMap<TitledPane, Dataset> panelink;
    private HashMap<Dataset, BatchController> datasettocontroller;

    //number of current batches, as an index
    int batchcount;

    //max number of adducts in Input Matrix
    int maxnumber;

    ColorAdjust adjust;
    DropShadow shadow;

    //adducts
    List<StringProperty> AdMs = new ArrayList<>();
    List<StringProperty> AdCs = new ArrayList<>();
    List<Label> AdLs = new ArrayList<>();

    //progress indication bar
    public int currentstep = 1;
    public AtomicInteger loading = new AtomicInteger(0);

    //initialize the table, and various elements
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {

            initializeButtons();
            toggleadductgeneration.selectedProperty().set(false);
            toggleAdductGeneration();

            adjust = new ColorAdjust();
            adjust.setBrightness(-0.2);
            adjust.setSaturation(-0.9);

            shadow = new DropShadow();

            adjust.setInput(shadow);
            indicatorbar.setEffect(adjust);

            metTable.setSortMode(TreeSortMode.ALL_DESCENDANTS);

            //set Factories for the tables
            nameColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Entry, String>("OGroup"));  //String in brackets has to be the same as PropertyValueFactory property= "..." in fxml
            scoreColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Entry, Float>("Score"));
            scorepeakfoundColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Entry, String>("ScorepeakfoundString"));
            scorepeakcloseColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Entry, String>("ScorepeakcloseString"));
            scorecertaintyColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Entry, Float>("Scorecertainty"));
            scorepeakrangeColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Entry, String>("ScorepeakrangeString"));
            scorefitaboveColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Entry, String>("ScorefitaboveString"));
            numColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Entry, String>("Num"));
            rtColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Entry, Float>("RT"));
            mzColumn.setCellValueFactory(new TreeItemPropertyValueFactory<Entry, String>("MZString"));
            scorefitaboveColumn.setComparator(new FloatStringComparator());
            scorepeakcloseColumn.setComparator(new FloatStringComparator());
            scorepeakrangeColumn.setComparator(new FloatStringComparator());
            mzColumn.setComparator(new MZStringComparator());
            //create new Session
            session = new Session(this);
            session.getReference().setName("Batch Nr. 1: Reference");

            try {
                FileUtils.deleteDirectory(new File("tmp"));
            } catch (IOException ex) {
                Logger.getLogger(FXMLTableViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
            new File("tmp").mkdirs();
            AdMs.addAll(Arrays.asList(AdM1.textProperty(), AdM2.textProperty(), AdM3.textProperty(), AdM4.textProperty(), AdM5.textProperty(), AdM6.textProperty(), AdM7.textProperty(), AdM8.textProperty(), AdM9.textProperty(), AdM10.textProperty(), AdM11.textProperty(), AdM12.textProperty(), AdM13.textProperty(), AdM14.textProperty(), AdM15.textProperty(), AdM16.textProperty(), AdM17.textProperty(), AdM18.textProperty(), AdM19.textProperty(), AdM20.textProperty(), AdM21.textProperty(), AdM22.textProperty(), AdM23.textProperty(), AdM24.textProperty(), AdM25.textProperty(), AdM26.textProperty(), AdM27.textProperty(), AdM28.textProperty(), AdM29.textProperty(), AdM30.textProperty(), AdM31.textProperty(), AdM32.textProperty(), AdM33.textProperty(), AdM34.textProperty(), AdM35.textProperty(), AdM36.textProperty(), AdM37.textProperty(), AdM38.textProperty(), AdM39.textProperty(), AdM40.textProperty(), AdM41.textProperty(), AdM42.textProperty()));
            AdCs.addAll(Arrays.asList(AdC1.textProperty(), AdC2.textProperty(), AdC3.textProperty(), AdC4.textProperty(), AdC5.textProperty(), AdC6.textProperty(), AdC7.textProperty(), AdC8.textProperty(), AdC9.textProperty(), AdC10.textProperty(), AdC11.textProperty(), AdC12.textProperty(), AdC13.textProperty(), AdC14.textProperty(), AdC15.textProperty(), AdC16.textProperty(), AdC17.textProperty(), AdC18.textProperty(), AdC19.textProperty(), AdC20.textProperty(), AdC21.textProperty(), AdC22.textProperty(), AdC23.textProperty(), AdC24.textProperty(), AdC25.textProperty(), AdC26.textProperty(), AdC27.textProperty(), AdC28.textProperty(), AdC29.textProperty(), AdC30.textProperty(), AdC31.textProperty(), AdC32.textProperty(), AdC33.textProperty(), AdC34.textProperty(), AdC35.textProperty(), AdC36.textProperty(), AdC37.textProperty(), AdC38.textProperty(), AdC39.textProperty(), AdC40.textProperty(), AdC41.textProperty(), AdC42.textProperty()));
            AdLs.addAll(Arrays.asList(Adlabel1, Adlabel2, Adlabel3, Adlabel4, Adlabel5, Adlabel6, Adlabel7, Adlabel8, Adlabel9, Adlabel10, Adlabel11, Adlabel12, Adlabel13, Adlabel14, Adlabel15, Adlabel16, Adlabel17, Adlabel18, Adlabel19, Adlabel20, Adlabel21, Adlabel22, Adlabel23, Adlabel24, Adlabel25, Adlabel26, Adlabel27, Adlabel28, Adlabel29, Adlabel30, Adlabel31, Adlabel32, Adlabel33, Adlabel34, Adlabel35, Adlabel36, Adlabel37, Adlabel38, Adlabel39, Adlabel40, Adlabel41, Adlabel42));

            //listener for adduct parameter list
            ChangeListener listener = new ChangeListener() {
                @Override
                public void changed(ObservableValue o, Object oldVal, Object newVal) {
                    //get index of value
                    int index = AdMs.indexOf((StringProperty) o);
                    if (index < 0) {
                        index = AdCs.indexOf((StringProperty) o);
                    }
                    //get numbers
                    String string = AdCs.get(index).get();
                    int c;
                    try {
                        c = Integer.parseInt(string.substring(0, string.length() - 1));
                    } catch (NumberFormatException e) {
                        c = 0;
                    } catch (StringIndexOutOfBoundsException e) {
                        c = 0;
                    }
                    int m;
                    try {
                        m = Integer.parseInt(AdMs.get(index).get());
                    } catch (NumberFormatException e) {
                        m = 0;
                    }

                    //make string
                    if (m < 1 || c < 1) {
                        string = "";
                    } else {
                        //test if division possible
                        if (m % c == 0) {
                            m = m / c;
                            c = 1;
                        } else if (c % m == 0) {
                            c = c / m;
                            m = 1;
                        }

                        if (c > 1) {
                            if (m > 1) {
                                if (m == c) {
                                    string = "M + ";
                                } else {
                                    string = m + "M/" + c + " +";
                                }
                            } else {
                                string = "M/" + c + " +";
                            }
                        } else if (m > 1) {
                            string = m + "M +";
                        } else {
                            string = "M +";
                        }
                    }
                    AdLs.get(index).setText(string);

                }
            };

            for (int i = 0; i < AdMs.size(); i++) {
                AdMs.get(i).addListener(listener);
            }
            for (int i = 0; i < AdCs.size(); i++) {
                AdCs.get(i).addListener(listener);
            }

            //Parameters
            AdName1.textProperty().bindBidirectional(session.getListofadductnameproperties().get(0));
            AdName2.textProperty().bindBidirectional(session.getListofadductnameproperties().get(1));
            AdName3.textProperty().bindBidirectional(session.getListofadductnameproperties().get(2));
            AdName4.textProperty().bindBidirectional(session.getListofadductnameproperties().get(3));
            AdName5.textProperty().bindBidirectional(session.getListofadductnameproperties().get(4));
            AdName6.textProperty().bindBidirectional(session.getListofadductnameproperties().get(5));
            AdName7.textProperty().bindBidirectional(session.getListofadductnameproperties().get(6));
            AdName8.textProperty().bindBidirectional(session.getListofadductnameproperties().get(7));
            AdName9.textProperty().bindBidirectional(session.getListofadductnameproperties().get(8));
            AdName10.textProperty().bindBidirectional(session.getListofadductnameproperties().get(9));
            AdName11.textProperty().bindBidirectional(session.getListofadductnameproperties().get(10));
            AdName12.textProperty().bindBidirectional(session.getListofadductnameproperties().get(11));
            AdName13.textProperty().bindBidirectional(session.getListofadductnameproperties().get(12));
            AdName14.textProperty().bindBidirectional(session.getListofadductnameproperties().get(13));
            AdName15.textProperty().bindBidirectional(session.getListofadductnameproperties().get(14));
            AdName16.textProperty().bindBidirectional(session.getListofadductnameproperties().get(15));
            AdName17.textProperty().bindBidirectional(session.getListofadductnameproperties().get(16));
            AdName18.textProperty().bindBidirectional(session.getListofadductnameproperties().get(17));
            AdName19.textProperty().bindBidirectional(session.getListofadductnameproperties().get(18));
            AdName20.textProperty().bindBidirectional(session.getListofadductnameproperties().get(19));
            AdName21.textProperty().bindBidirectional(session.getListofadductnameproperties().get(20));
            AdName22.textProperty().bindBidirectional(session.getListofadductnameproperties().get(21));
            AdName23.textProperty().bindBidirectional(session.getListofadductnameproperties().get(22));
            AdName24.textProperty().bindBidirectional(session.getListofadductnameproperties().get(23));
            AdName25.textProperty().bindBidirectional(session.getListofadductnameproperties().get(24));
            AdName26.textProperty().bindBidirectional(session.getListofadductnameproperties().get(25));
            AdName27.textProperty().bindBidirectional(session.getListofadductnameproperties().get(26));
            AdName28.textProperty().bindBidirectional(session.getListofadductnameproperties().get(27));
            AdName29.textProperty().bindBidirectional(session.getListofadductnameproperties().get(28));
            AdName30.textProperty().bindBidirectional(session.getListofadductnameproperties().get(29));
            AdName31.textProperty().bindBidirectional(session.getListofadductnameproperties().get(30));
            AdName32.textProperty().bindBidirectional(session.getListofadductnameproperties().get(31));
            AdName33.textProperty().bindBidirectional(session.getListofadductnameproperties().get(32));
            AdName34.textProperty().bindBidirectional(session.getListofadductnameproperties().get(33));
            AdName35.textProperty().bindBidirectional(session.getListofadductnameproperties().get(34));
            AdName36.textProperty().bindBidirectional(session.getListofadductnameproperties().get(35));
            AdName37.textProperty().bindBidirectional(session.getListofadductnameproperties().get(36));
            AdName38.textProperty().bindBidirectional(session.getListofadductnameproperties().get(37));
            AdName39.textProperty().bindBidirectional(session.getListofadductnameproperties().get(38));
            AdName40.textProperty().bindBidirectional(session.getListofadductnameproperties().get(39));
            AdName41.textProperty().bindBidirectional(session.getListofadductnameproperties().get(40));
            AdName42.textProperty().bindBidirectional(session.getListofadductnameproperties().get(41));
            AdMass1.textProperty().bindBidirectional(session.getListofadductmassproperties().get(0), new NumberStringConverter());
            AdMass2.textProperty().bindBidirectional(session.getListofadductmassproperties().get(1), new NumberStringConverter());
            AdMass3.textProperty().bindBidirectional(session.getListofadductmassproperties().get(2), new NumberStringConverter());
            AdMass4.textProperty().bindBidirectional(session.getListofadductmassproperties().get(3), new NumberStringConverter());
            AdMass5.textProperty().bindBidirectional(session.getListofadductmassproperties().get(4), new NumberStringConverter());
            AdMass6.textProperty().bindBidirectional(session.getListofadductmassproperties().get(5), new NumberStringConverter());
            AdMass7.textProperty().bindBidirectional(session.getListofadductmassproperties().get(6), new NumberStringConverter());
            AdMass8.textProperty().bindBidirectional(session.getListofadductmassproperties().get(7), new NumberStringConverter());
            AdMass9.textProperty().bindBidirectional(session.getListofadductmassproperties().get(8), new NumberStringConverter());
            AdMass10.textProperty().bindBidirectional(session.getListofadductmassproperties().get(9), new NumberStringConverter());
            AdMass11.textProperty().bindBidirectional(session.getListofadductmassproperties().get(10), new NumberStringConverter());
            AdMass12.textProperty().bindBidirectional(session.getListofadductmassproperties().get(11), new NumberStringConverter());
            AdMass13.textProperty().bindBidirectional(session.getListofadductmassproperties().get(12), new NumberStringConverter());
            AdMass14.textProperty().bindBidirectional(session.getListofadductmassproperties().get(13), new NumberStringConverter());
            AdMass15.textProperty().bindBidirectional(session.getListofadductmassproperties().get(14), new NumberStringConverter());
            AdMass16.textProperty().bindBidirectional(session.getListofadductmassproperties().get(15), new NumberStringConverter());
            AdMass17.textProperty().bindBidirectional(session.getListofadductmassproperties().get(16), new NumberStringConverter());
            AdMass18.textProperty().bindBidirectional(session.getListofadductmassproperties().get(17), new NumberStringConverter());
            AdMass19.textProperty().bindBidirectional(session.getListofadductmassproperties().get(18), new NumberStringConverter());
            AdMass20.textProperty().bindBidirectional(session.getListofadductmassproperties().get(19), new NumberStringConverter());
            AdMass21.textProperty().bindBidirectional(session.getListofadductmassproperties().get(20), new NumberStringConverter());
            AdMass22.textProperty().bindBidirectional(session.getListofadductmassproperties().get(21), new NumberStringConverter());
            AdMass23.textProperty().bindBidirectional(session.getListofadductmassproperties().get(22), new NumberStringConverter());
            AdMass24.textProperty().bindBidirectional(session.getListofadductmassproperties().get(23), new NumberStringConverter());
            AdMass25.textProperty().bindBidirectional(session.getListofadductmassproperties().get(24), new NumberStringConverter());
            AdMass26.textProperty().bindBidirectional(session.getListofadductmassproperties().get(25), new NumberStringConverter());
            AdMass27.textProperty().bindBidirectional(session.getListofadductmassproperties().get(26), new NumberStringConverter());
            AdMass28.textProperty().bindBidirectional(session.getListofadductmassproperties().get(27), new NumberStringConverter());
            AdMass29.textProperty().bindBidirectional(session.getListofadductmassproperties().get(28), new NumberStringConverter());
            AdMass30.textProperty().bindBidirectional(session.getListofadductmassproperties().get(29), new NumberStringConverter());
            AdMass31.textProperty().bindBidirectional(session.getListofadductmassproperties().get(30), new NumberStringConverter());
            AdMass32.textProperty().bindBidirectional(session.getListofadductmassproperties().get(31), new NumberStringConverter());
            AdMass33.textProperty().bindBidirectional(session.getListofadductmassproperties().get(32), new NumberStringConverter());
            AdMass34.textProperty().bindBidirectional(session.getListofadductmassproperties().get(33), new NumberStringConverter());
            AdMass35.textProperty().bindBidirectional(session.getListofadductmassproperties().get(34), new NumberStringConverter());
            AdMass36.textProperty().bindBidirectional(session.getListofadductmassproperties().get(35), new NumberStringConverter());
            AdMass37.textProperty().bindBidirectional(session.getListofadductmassproperties().get(36), new NumberStringConverter());
            AdMass38.textProperty().bindBidirectional(session.getListofadductmassproperties().get(37), new NumberStringConverter());
            AdMass39.textProperty().bindBidirectional(session.getListofadductmassproperties().get(38), new NumberStringConverter());
            AdMass40.textProperty().bindBidirectional(session.getListofadductmassproperties().get(39), new NumberStringConverter());
            AdMass41.textProperty().bindBidirectional(session.getListofadductmassproperties().get(40), new NumberStringConverter());
            AdMass42.textProperty().bindBidirectional(session.getListofadductmassproperties().get(41), new NumberStringConverter());
            AdM1.textProperty().bindBidirectional(session.getListofadductmproperties().get(0), new NumberStringConverter());
            AdM2.textProperty().bindBidirectional(session.getListofadductmproperties().get(1), new NumberStringConverter());
            AdM3.textProperty().bindBidirectional(session.getListofadductmproperties().get(2), new NumberStringConverter());
            AdM4.textProperty().bindBidirectional(session.getListofadductmproperties().get(3), new NumberStringConverter());
            AdM5.textProperty().bindBidirectional(session.getListofadductmproperties().get(4), new NumberStringConverter());
            AdM6.textProperty().bindBidirectional(session.getListofadductmproperties().get(5), new NumberStringConverter());
            AdM7.textProperty().bindBidirectional(session.getListofadductmproperties().get(6), new NumberStringConverter());
            AdM8.textProperty().bindBidirectional(session.getListofadductmproperties().get(7), new NumberStringConverter());
            AdM9.textProperty().bindBidirectional(session.getListofadductmproperties().get(8), new NumberStringConverter());
            AdM10.textProperty().bindBidirectional(session.getListofadductmproperties().get(9), new NumberStringConverter());
            AdM11.textProperty().bindBidirectional(session.getListofadductmproperties().get(10), new NumberStringConverter());
            AdM12.textProperty().bindBidirectional(session.getListofadductmproperties().get(11), new NumberStringConverter());
            AdM13.textProperty().bindBidirectional(session.getListofadductmproperties().get(12), new NumberStringConverter());
            AdM14.textProperty().bindBidirectional(session.getListofadductmproperties().get(13), new NumberStringConverter());
            AdM15.textProperty().bindBidirectional(session.getListofadductmproperties().get(14), new NumberStringConverter());
            AdM16.textProperty().bindBidirectional(session.getListofadductmproperties().get(15), new NumberStringConverter());
            AdM17.textProperty().bindBidirectional(session.getListofadductmproperties().get(16), new NumberStringConverter());
            AdM18.textProperty().bindBidirectional(session.getListofadductmproperties().get(17), new NumberStringConverter());
            AdM19.textProperty().bindBidirectional(session.getListofadductmproperties().get(18), new NumberStringConverter());
            AdM20.textProperty().bindBidirectional(session.getListofadductmproperties().get(19), new NumberStringConverter());
            AdM21.textProperty().bindBidirectional(session.getListofadductmproperties().get(20), new NumberStringConverter());
            AdM22.textProperty().bindBidirectional(session.getListofadductmproperties().get(21), new NumberStringConverter());
            AdM23.textProperty().bindBidirectional(session.getListofadductmproperties().get(22), new NumberStringConverter());
            AdM24.textProperty().bindBidirectional(session.getListofadductmproperties().get(23), new NumberStringConverter());
            AdM25.textProperty().bindBidirectional(session.getListofadductmproperties().get(24), new NumberStringConverter());
            AdM26.textProperty().bindBidirectional(session.getListofadductmproperties().get(25), new NumberStringConverter());
            AdM27.textProperty().bindBidirectional(session.getListofadductmproperties().get(26), new NumberStringConverter());
            AdM28.textProperty().bindBidirectional(session.getListofadductmproperties().get(27), new NumberStringConverter());
            AdM29.textProperty().bindBidirectional(session.getListofadductmproperties().get(28), new NumberStringConverter());
            AdM30.textProperty().bindBidirectional(session.getListofadductmproperties().get(29), new NumberStringConverter());
            AdM31.textProperty().bindBidirectional(session.getListofadductmproperties().get(30), new NumberStringConverter());
            AdM32.textProperty().bindBidirectional(session.getListofadductmproperties().get(31), new NumberStringConverter());
            AdM33.textProperty().bindBidirectional(session.getListofadductmproperties().get(32), new NumberStringConverter());
            AdM34.textProperty().bindBidirectional(session.getListofadductmproperties().get(33), new NumberStringConverter());
            AdM35.textProperty().bindBidirectional(session.getListofadductmproperties().get(34), new NumberStringConverter());
            AdM36.textProperty().bindBidirectional(session.getListofadductmproperties().get(35), new NumberStringConverter());
            AdM37.textProperty().bindBidirectional(session.getListofadductmproperties().get(36), new NumberStringConverter());
            AdM38.textProperty().bindBidirectional(session.getListofadductmproperties().get(37), new NumberStringConverter());
            AdM39.textProperty().bindBidirectional(session.getListofadductmproperties().get(38), new NumberStringConverter());
            AdM40.textProperty().bindBidirectional(session.getListofadductmproperties().get(39), new NumberStringConverter());
            AdM41.textProperty().bindBidirectional(session.getListofadductmproperties().get(40), new NumberStringConverter());
            AdM42.textProperty().bindBidirectional(session.getListofadductmproperties().get(41), new NumberStringConverter());
            AdC1.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(0));
            AdC2.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(1));
            AdC3.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(2));
            AdC4.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(3));
            AdC5.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(4));
            AdC6.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(5));
            AdC7.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(6));
            AdC8.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(7));
            AdC9.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(8));
            AdC10.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(9));
            AdC11.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(10));
            AdC12.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(11));
            AdC13.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(12));
            AdC14.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(13));
            AdC15.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(14));
            AdC16.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(15));
            AdC17.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(16));
            AdC18.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(17));
            AdC19.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(18));
            AdC20.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(19));
            AdC21.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(20));
            AdC22.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(21));
            AdC23.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(22));
            AdC24.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(23));
            AdC25.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(24));
            AdC26.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(25));
            AdC27.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(26));
            AdC28.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(27));
            AdC29.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(28));
            AdC30.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(29));
            AdC31.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(30));
            AdC32.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(31));
            AdC33.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(32));
            AdC34.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(33));
            AdC35.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(34));
            AdC36.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(35));
            AdC37.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(36));
            AdC38.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(37));
            AdC39.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(38));
            AdC40.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(39));
            AdC41.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(40));
            AdC42.textProperty().bindBidirectional(session.getListofadductchargeproperties().get(41));

//        Adlabel1.textProperty().bind(new StringBinding() {{bind(session.getListofadductmproperties().get(0));}
//      @Override
//      protected String computeValue() {
//      return makeString(session.getListofadductmproperties().get(0).intValue(),session.getListofadductchargeproperties().get(0).get());}});
//        Adlabel2.textProperty().bind(new StringBinding() {{bind(session.getListofadductmproperties().get(1));}
//      @Override
//      protected String computeValue() {
//      return makeString(session.getListofadductmproperties().get(1).intValue(),session.getListofadductchargeproperties().get(1).get());}});
//        Adlabel3.textProperty().bind(new StringBinding() {{bind(session.getListofadductmproperties().get(2));}
//      @Override
//      protected String computeValue() {
//      return makeString(session.getListofadductmproperties().get(2).intValue(),session.getListofadductchargeproperties().get(2).get());}});
//        Adlabel4.textProperty().bind(new StringBinding() {{bind(session.getListofadductmproperties().get(3));}
//      @Override
//      protected String computeValue() {
//      return makeString(session.getListofadductmproperties().get(3).intValue(),session.getListofadductchargeproperties().get(3).get());}});
//        Adlabel5.textProperty().bind(new StringBinding() {{bind(session.getListofadductmproperties().get(4));}
//      @Override
//      protected String computeValue() {
//      return makeString(session.getListofadductmproperties().get(4).intValue(),session.getListofadductchargeproperties().get(4).get());}});
//        Adlabel6.textProperty().bind(new StringBinding() {{bind(session.getListofadductmproperties().get(5));}
//      @Override
//      protected String computeValue() {
//      return makeString(session.getListofadductmproperties().get(5).intValue(),session.getListofadductchargeproperties().get(5).get());}});
//        Adlabel7.textProperty().bind(new StringBinding() {{bind(session.getListofadductmproperties().get(6));}
//      @Override
//      protected String computeValue() {
//      return makeString(session.getListofadductmproperties().get(6).intValue(),session.getListofadductchargeproperties().get(6).get());}});
            RTTol.textProperty().bindBidirectional(session.getRTTolProp(), new NumberStringConverter());
            MinSignals.textProperty().bindBidirectional(session.getMinnumofsignals(), new NumberStringConverter());
            MinConsSignals.textProperty().bindBidirectional(session.getMinnumofconsecutivesignals(), new NumberStringConverter());
            Scales.textProperty().bindBidirectional(session.getScales());
            Start.textProperty().bindBidirectional(session.getStart(), new NumberStringConverter());
            Noise.textProperty().bindBidirectional(session.getNoisethreshold(), new NumberStringConverter());
            End.textProperty().bindBidirectional(session.getEnd(), new NumberStringConverter());
            RTTolShift.textProperty().bindBidirectional(session.getPeakRTTolerance(), new NumberStringConverter());
            MZTol.textProperty().bindBidirectional(session.getMZTolProp(), new NumberStringConverter());
            SliceMZTol.textProperty().bindBidirectional(session.getSliceMZTolProp(), new NumberStringConverter());
            Res.textProperty().bindBidirectional(session.getResProp(), new NumberStringConverter());
            Base.textProperty().bindBidirectional(session.getBaseProp(), new NumberStringConverter());
            PeakPick.setItems(FXCollections.observableArrayList(
                    "Gauss Peak Correlation", "MassSpecWavelet", "Savitzky-Golay Filter", "Experimental")
            );

            PeakPick.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                public void changed(ObservableValue ov, Number value, Number newVal) {
                    session.setPeackPick(PeakPick.getItems().get(newVal.intValue()).toString());
                    if (PeakPick.getItems().get(newVal.intValue()).toString().equals("MassSpecWavelet")) {
                        noiselabel.setText("S/N Threshold:");
                        Scales.setVisible(true);
                        dislabel.setVisible(true);
                        box3.setHeight(129);
                        label10.setLayoutY(422);
                        box4.setLayoutY(425);
                        labelmin.setLayoutY(434);
                        RTTolShift.setLayoutY(430);
                        Res.setLayoutY(460);
                        label11.setLayoutY(434);
                        label6.setLayoutY(464);
                        label7.setLayoutY(438);

                    } else {
                        noiselabel.setText("NU Threshold:");
                        Scales.setVisible(false);
                        dislabel.setVisible(false);
                        box3.setHeight(98);
                        label10.setLayoutY(391);
                        box4.setLayoutY(394);
                        labelmin.setLayoutY(401);
                        RTTolShift.setLayoutY(399);
                        Res.setLayoutY(429);
                        label11.setLayoutY(404);
                        label6.setLayoutY(433);
                        label7.setLayoutY(407);
                    }
                }

            });
            PeakPick.getSelectionModel().select(Integer.parseInt(session.getProperties().getProperty("PeakPick")));
            panelink = new HashMap<>();
            setDatasettocontroller(new HashMap<>());

//set batchcount to 0,
            batchcount = 0;

            Label label = new Label(
                    "Hello there! \n\n\n"
                    + "This is the MetMatch main window.\n\n"
                    + "The parameter pane to the left contains all processing \n"
                    + "parameters, sorted into 4 different categories. Simply \n"
                    + "click the tabs to switch between them. \n\n"
                    + "Once you are done, click the green button \"Apply\" to \n"
                    + "continue to the next processing step. Clicking the green \n"
                    + "button will always take you to the next step. So if you \n"
                    + "are lost, just click the green button! \n\n\n"
                    + "Thank you for using MetMatch!");
            label.setAlignment(Pos.CENTER);
            label.setMinHeight(500);
            label.setMinWidth(500);
            label.setTextAlignment(TextAlignment.JUSTIFY);
            label.setFont(Font.font("Verdana", 14));
            metTable.setPlaceholder(label);

            InputTable.setItems(session.getInfos());

            infocol.setCellValueFactory(
                    new PropertyValueFactory<Information, String>("information")
            );

            headcol.setCellValueFactory(new PropertyValueFactory<Information, String>("header"));
            headcol.setCellFactory(new TextFieldCellFactory());
            InputTable.setEditable(true);

            InputTable.setRowFactory(tv -> new TableRow<Information>() {
                private Tooltip tooltip = new Tooltip();

                @Override
                public void updateItem(Information info, boolean empty) {
                    super.updateItem(info, empty);
                    if (info == null) {
                        setTooltip(null);
                    } else {
                        tooltip.setText(info.getTooltip());
                        setTooltip(tooltip);
                    }
                }
            });

            outputformat = FXCollections.observableArrayList(
                    new OutputFormat()
            );
            C1.setCellValueFactory(new PropertyValueFactory<Information, String>("C1"));
            C1.setCellFactory(new AutoTextFieldCellFactory());
            C2.setCellValueFactory(new PropertyValueFactory<Information, String>("C2"));
            C2.setCellFactory(new AutoTextFieldCellFactory());
            C3.setCellValueFactory(new PropertyValueFactory<Information, String>("C3"));
            C3.setCellFactory(new AutoTextFieldCellFactory());
            C4.setCellValueFactory(new PropertyValueFactory<Information, String>("C4"));
            C4.setCellFactory(new AutoTextFieldCellFactory());
            C5.setCellValueFactory(new PropertyValueFactory<Information, String>("C5"));
            C5.setCellFactory(new AutoTextFieldCellFactory());
            C6.setCellValueFactory(new PropertyValueFactory<Information, String>("C6"));
            C6.setCellFactory(new AutoTextFieldCellFactory());
            C7.setCellValueFactory(new PropertyValueFactory<Information, String>("C7"));
            C7.setCellFactory(new AutoTextFieldCellFactory());
            C8.setCellValueFactory(new PropertyValueFactory<Information, String>("C8"));
            C8.setCellFactory(new AutoTextFieldCellFactory());
            C9.setCellValueFactory(new PropertyValueFactory<Information, String>("C9"));
            C9.setCellFactory(new AutoTextFieldCellFactory());
            C10.setCellValueFactory(new PropertyValueFactory<Information, String>("C10"));
            C10.setCellFactory(new AutoTextFieldCellFactory());

            option2table.setItems(outputformat);
            option1.selectedProperty().set(true);
            option1changed();

            try {
                TitledPane tps = new TitledPane();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Batch.fxml"));
                //loader.setRoot(tps);
                Reference reference = session.getReference();
                reference.setName("Batch Nr. 1: Reference");
                session.addDataset(reference);
                batchcount++;
                panelink.put(tps, reference);
                loader.setController(new BatchController(session, reference, progressbar, getMasterListofOGroups(), tps, this));
                getDatasettocontroller().put(reference, loader.getController());
                reference.setController(loader.getController());
                tps = loader.load();
                tps.setExpanded(true);
                getAccordion().getPanes().add(tps);
                getAccordion().setExpandedPane(tps);

            } catch (IOException ex) {
                Logger.getLogger(FXMLTableViewController.class.getName()).log(Level.SEVERE, null, ex);
            }

            accordion.setDisable(true);
            accordion.setVisible(false);

        } catch (IOException ex) {
            Logger.getLogger(FXMLTableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //Open File Chooser for Data Matrix
    public void openReferenceDataMatrixChooser() throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                progressbar.setVisible(true);
                progressbar.progressProperty().set(-1.0);
            }
        });

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Reference Matrix (*.tsv, *.txt)", "*.tsv", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show open file dialog
        File file = fileChooser.showOpenDialog(null);
        try {
            session.setReferenceTsv(file);
            System.out.println(session.getReferenceTsv().toString());

            setMasterListofOGroups(session.parseReferenceTsv());
            loadP.setVisible(false);
            restoreP.setVisible(false);
        } finally {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    progressbar.setVisible(false);
                }
            });
        }

        //Disable Option
        referenceMatrixButton.setOnAction((event) -> {

        });
        //generate additional adducts
        session.finalizeAdducts();
        generateAdductsnew();
        //Convert List into TreeTable Entries
        TreeItem<Entry> superroot = new TreeItem<>();

        int numberofadducts = 0;
        //for all OGroups
        for (int i = 0; i < getMasterListofOGroups().size(); i++) {
            TreeItem<Entry> root = new TreeItem<>(getMasterListofOGroups().get(i));
            getMasterListofOGroups().get(i).treeitem=root;
            root.setExpanded(false);
            superroot.getChildren().add(root);

            for (int j = 0; j < getMasterListofOGroups().get(i).getListofAdducts().size(); j++) {
                TreeItem<Entry> childNode1 = new TreeItem<>(getMasterListofOGroups().get(i).getListofAdducts().get(j));
                getMasterListofOGroups().get(i).getListofAdducts().get(j).treeitem=childNode1;
                root.getChildren().add(childNode1);
                numberofadducts++;
            }

        }
        session.setNumberofadducts(numberofadducts);

        getMetTable().setRoot(superroot);
        getMetTable().setShowRoot(false);

        addBatchButton.setDisable(false);
        addBatchButton.setVisible(true);
        accordion.setVisible(true);
        setParameterPane(false);

        TabPane.setVisible(false);
        MinSignals.setDisable(true);
        MinConsSignals.setDisable(true);
        MZTol.setDisable(true);
        SliceMZTol.setDisable(true);
        RTTol.setDisable(true);

        Start.setDisable(true);
        End.setDisable(true);
        toggleadductgeneration.setDisable(true);
        adductanchor.setDisable(true);
        inputTab.setDisable(true);

        session.prepare();

        getMetTable().getSortOrder().clear();
        getMetTable().getSortOrder().add(mzColumn);
        getMetTable().getSortOrder().add(rtColumn);

        //add float click functionality to the TreeTable
        getMetTable().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {

                    try {

                        //create new window
                        Stage stage = new Stage();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/fxml_adductview.fxml"));
                        Pane myPane = (Pane) loader.load();
                        Scene myScene = new Scene(myPane);
                        stage.setScene(myScene);
                        stage.setTitle("EIC Viewer");
                        Fxml_adductviewController controller = loader.<Fxml_adductviewController>getController();
                        controller.setSession(session);
                        controller.setMainController(getController());

                        //add MasterListofOGroups to new controller
                        controller.metTable = getMetTable();

                        //print graphs
                        controller.print(-2);
                        stage.show();
                        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

                            public void handle(WindowEvent we) {
                                controller.close();
                            }
                        });

                    } catch (IOException ex) {
                        Logger.getLogger(FXMLTableViewController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        });

        setstep(3);
        accordion.setDisable(false);

    }

    //add a new batch
    public Batch addBatch() {

        try {
            TitledPane tps = new TitledPane();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Batch.fxml"));
            //loader.setRoot(tps);
            Batch batch = new Batch(batchcount);
            batch.setName("Batch Nr. " + (batchcount + 1));
            session.addDataset(batch);
            batchcount++;
            panelink.put(tps, batch);
            loader.setController(new BatchController(session, batch, progressbar, getMasterListofOGroups(), tps, this));
            getDatasettocontroller().put(batch, loader.getController());
            batch.setController(loader.getController());
            tps = loader.load();
            tps.setExpanded(true);
            getAccordion().getPanes().add(tps);
            getAccordion().setExpandedPane(tps);
            batch.getController().BatchPane = tps;
            return batch;
        } catch (IOException ex) {
            Logger.getLogger(FXMLTableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    //calculates Shift and opens a new window
    public void newwindowcalculate() throws IOException, InterruptedException {

        //open new window
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/fxml_pathshiftview.fxml"));
        Pane myPane = (Pane) loader.load();
        Scene myScene = new Scene(myPane);
        stage.setScene(myScene);
        stage.setTitle("Shift Calculator");
        Fxml_pathshiftviewController controller = loader.<Fxml_pathshiftviewController>getController();
        controller.setSupercontroller(this);
        controller.setSession(session);

        //print graphs
        controller.print(getMasterListofOGroups());
        System.out.println("PRINTNEW");
        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                controller.close();
            }
        });

    }

    //does the Shift calculation
    public void calculate(CountDownLatch latch, ProgressBar prog) throws IOException, InterruptedException {

        FloatProperty progress = new SimpleFloatProperty(0.0f);
        prog.progressProperty().bind(progress);

        Task task = new Task<Void>() {
            @Override
            public Void call() throws IOException, InterruptedException {
                CertaintyCalculator calc = new CertaintyCalculator(session);
                for (int d = 0; d < session.getListofDatasets().size(); d++) {
                    if (session.getListofDatasets().get(d).getActive()) {
                        for (int f = 0; f < session.getListofDatasets().get(d).getListofFiles().size(); f++) {
                            RawDataFile currentfile = session.getListofDatasets().get(d).getListofFiles().get(f);
                            if (currentfile.getActive().booleanValue()) {

                                Collections.sort(getMasterListofOGroups(), new orderbyRT());
                                float[][] matrix = new float[getMasterListofOGroups().size()][session.getResolution()];

                                CountDownLatch latchpeak = new CountDownLatch(1);
                                Task task = new Task<Void>() {
                                    @Override
                                    public Void call() throws IOException, InterruptedException {
                                        session.getIothread().lockFile(currentfile, true);
                                        double start = System.currentTimeMillis();
                                        LinkedList<Integer> queue = new LinkedList<Integer>();
                                        //go trough and check if all are ready
                                        for (int i = 0; i < getMasterListofOGroups().size(); i++) {
                                            //if not ready, add to queue
                                            if (getMasterListofOGroups().get(i).isStored(currentfile)) {
                                                queue.add(i);
                                                session.getIothread().readOGroup(getMasterListofOGroups().get(i), currentfile);
                                                //if ready animate
                                            } else {
                                                getMasterListofOGroups().get(i).peakpickOGroup(currentfile);
                                                getMasterListofOGroups().get(i).getOGroupPropArraySmooth(currentfile, matrix, i);
                                                //System.out.println(i + " of " + getMasterListofOGroups().size() + " OGroups calculated");
                                            }
                                        }
                                        //System.out.println("Size of Queue: " + queue.size());
                                        //go through queue until it is empty
                                        double picktime = 0;
                                        while (queue.size() > 0) {
                                            int size = queue.size();
                                            for (int j = 0; j < size; j++) {
                                                Integer current = queue.pop();
                                                if (getMasterListofOGroups().get(current).isStored(currentfile)) {
                                                    queue.add(current);
                                                } else {
                                                    double pick = System.currentTimeMillis();
                                                    getMasterListofOGroups().get(current).peakpickOGroup(currentfile);
                                                    picktime += System.currentTimeMillis() - pick;
                                                    getMasterListofOGroups().get(current).getOGroupPropArraySmooth(currentfile, matrix, current);

                                                }
                                            }
                                            //System.out.println("Size of Queue: " + queue.size());
                                        }

                                        latchpeak.countDown();
                                        System.out.println("Total peak picking time: " + picktime);
                                        System.out.println("Total time: " + (System.currentTimeMillis() - start));
                                        session.getIothread().lockFile(currentfile, false);
                                        return null;
                                    }

                                };

                                //new thread that executes task
                                new Thread(task).start();
                                latchpeak.await();

                                //Test artificial shift
//                float[][] matrix2 = new float[MasterListofOGroups.size()][session.getResolution()];
//                for (int i = 0; i< MasterListofOGroups.size(); i++) {
//                    int currentshift = (int) (Math.floor(10+(Math.sin(MasterListofOGroups.get(i).getRT())*10)));
//                    for (int j = 0; j<currentshift; j++) {
//                        matrix2[i][j] = 0; 
//                    }
//                    for (int j = currentshift; j<session.getResolution(); j++) {
//                        matrix2[i][j] = matrix[i][j-currentshift];
//                        
//                    }
//                }
//               
//                matrix = matrix2;
                                //calculate weight matrix
                                float[][] weights = new float[getMasterListofOGroups().size()][session.getResolution()];
                                //fill first row
                                for (int j = 0; j < session.getResolution(); j++) {
                                    weights[0][j] = matrix[0][j];

                                }
                                //TODO: Penalty for change in j
                                //fill rest of weights matrix
                                float penalty = session.getListofDatasets().get(0).getPenalty();
                                for (int i = 1; i < getMasterListofOGroups().size(); i++) {
                                    for (int j = 0; j < session.getResolution(); j++) {
                                        float max = 0;
                                        if (weights[i - 1][j] > max) {
                                            max = weights[i - 1][j] + matrix[i][j];
                                        }
                                        if ((j - 1) > 0 && weights[i - 1][j - 1] + matrix[i][j] > max) {
                                            max = weights[i - 1][j - 1] + matrix[i][j];
                                        }
                                        if ((j + 1) < session.getResolution() && weights[i - 1][j + 1] + matrix[i][j] > max) {
                                            max = weights[i - 1][j + 1] + matrix[i][j];
                                        }
                                        weights[i][j] = max;

                                    }

                                }
                                //get max in last row
                                float max = 0;
                                int maxint = 0;
                                for (int j = 0; j < session.getResolution(); j++) {
                                    if (weights[getMasterListofOGroups().size() - 1][j] > max) {
                                        maxint = j;
                                        max = weights[getMasterListofOGroups().size() - 1][j];
                                    }
                                }

                                getMasterListofOGroups().get(getMasterListofOGroups().size() - 1).setFittedShift(currentfile, (short) maxint);

                                //TODO: animate range as function of time
                                for (int i = getMasterListofOGroups().size() - 1; i > -1; i--) {
                                    max = 0;

                                    int j = maxint;
                                    if (weights[i][j] > max) {
                                        max = weights[i][j];
                                        maxint = j;
                                    }
                                    if ((j - 1) > 0 && weights[i][j - 1] - penalty > max) {
                                        max = weights[i][j - 1] - penalty;
                                        maxint = j - 1;
                                    }

                                    if ((j + 1) < session.getResolution() && weights[i][j + 1] - penalty > max) {
                                        //max = weights[i][j+1];
                                        maxint = j + 1;
                                    }

                                    getMasterListofOGroups().get(i).setFittedShift(currentfile, (short) maxint);

                                    //set score for OPGroup
                                    //getMasterListofOGroups().get(i).addScore(currentfile, (getMasterListofOGroups().get(i).getOGroupPropArraySmooth(currentfile)[getMasterListofOGroups().get(i).getOGroupFittedShift(currentfile)]));
                                    //set score for every addact
                                    for (int a = 0; a < getMasterListofOGroups().get(i).getListofAdducts().size(); a++) {
                                        //getMasterListofOGroups().get(i).getListofAdducts().get(a).addScore(currentfile, (getMasterListofOGroups().get(i).getListofAdducts().get(a).getAdductPropArray(currentfile)[getMasterListofOGroups().get(i).getOGroupFittedShift(currentfile)]));
                                    }

                                    getMetTable().refresh();

                                }
                                //TODO number of active files

                                progress.set(progress.get() + 1.0f / (session.getListofDatasets().get(d).getListofFiles().size()));
                                System.out.println("Calculation: " + progress.get() + "%");
                                calc.calculate(currentfile, matrix);
                            }
                        }
                    }
                }

                //don't recalculate unless something changes
                //session.setPeakPickchanged(false);
                latch.countDown();
                return null;
            }

        };

        //new thread that executes task
        new Thread(task).start();

    }

    //calculates Shift and opens a new window
    public void newWindowShiftFitting() throws IOException, InterruptedException {
        
        if (!session.compactlist) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            String Title = String.valueOf(session.gettotalnumberofadducts());
                            alert.setTitle("Large Number of Adducts");
                            alert.setHeaderText(Title + " adducts are included in the reference matrix. Do you want to delete empty adducts?");
                            alert.setContentText("A large number of empty adducts can slow down processing speed and overall responsiveness of MetMatch. Do you want to delete empty adducts? \nDeleted adducts will not be accounted for when loading new files.\nThis could take a few minutes.");

                            ButtonType buttonTypeOne = new ButtonType("Yes");
                            ButtonType buttonTypeTwo = new ButtonType("No");
                            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);
                       
                            
                            Optional<ButtonType> result = alert.showAndWait();
                        
                            if (result.get() == buttonTypeOne) {
                                double progress = 0;
                                double step = 1.0d/MasterListofOGroups.size();
                             for (Entry e:MasterListofOGroups) {
            TreeItem<Entry> ogroup = e.treeitem;
            List<Entry> emptylist = e.getListofemptyAdducts();
            e.getListofAdducts().removeAll(emptylist);
            for (Entry a:emptylist) {
                    ogroup.getChildren().remove(a.treeitem);
            }
            
            System.out.println((progress+=step)*100 + "%");
        }
                             session.compactlist=true;
                                try {
                                    openshiftcalculationwindow();
                                } catch (IOException ex) {
                                    Logger.getLogger(FXMLTableViewController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else if (result.get() == buttonTypeTwo) {
                                try {
                                    openshiftcalculationwindow();
                                } catch (IOException ex) {
                                    Logger.getLogger(FXMLTableViewController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else  {
                            
                                
                            }
}});
            
            
            
            
            
        } else {
   openshiftcalculationwindow(); }
        
    }
    
    public void openshiftcalculationwindow() throws IOException {
        maskerpane.setVisible(true);
        indicatorbar.setEffect(adjust);

        //open new window
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/fxml_gravityshiftview.fxml"));
        Pane myPane = (Pane) loader.load();
        Scene myScene = new Scene(myPane);
        stage.setScene(myScene);
        stage.setTitle("Shift Calculator");
        Fxml_gravityshiftviewController controller = loader.<Fxml_gravityshiftviewController>getController();
        controller.setSupercontroller(this);
        controller.setSession(session);
        controller.stage = stage;

        //print graphs
        //controller.animate(getMasterListofOGroups());
        controller.setOlist(MasterListofOGroups);
        System.out.println("PRINTNEW");
        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                controller.close();
                controller.abort();
                maskerpane.setVisible(false);
                indicatorbar.setEffect(shadow);
            }
        });

        
    }
    

    public float getmaxofrange(float[][] weights, int row, int col, int range) {
        float max = 0;

        for (int i = (col - range); i <= (col + range); i++) {
            if (i < session.getResolution() && i >= 0 && weights[row][i] > max) {
                max += weights[row][i];
            }

        }

        return max;
    }

    /**
     * @return the metTable
     */
    public TreeTableView<Entry> getMetTable() {
        return metTable;
    }

    public FXMLTableViewController getController() {
        return this;
    }

    /**
     * @param metTable the metTable to set
     */
    public void setMetTable(TreeTableView<Entry> metTable) {
        this.metTable = metTable;
    }

    /**
     * @return the MasterListofOGroups
     */
    public ObservableList<Entry> getMasterListofOGroups() {
        return MasterListofOGroups;
    }

    /**
     * @param MasterListofOGroups the MasterListofOGroups to set
     */
    public void setMasterListofOGroups(ObservableList<Entry> MasterListofOGroups) {
        this.MasterListofOGroups = MasterListofOGroups;
    }

    /**
     * @return the accordion
     */
    public Accordion getAccordion() {
        return accordion;
    }

    /**
     * @param accordion the accordion to set
     */
    public void setAccordion(Accordion accordion) {
        this.accordion = accordion;
    }

    /**
     * @return the datasettocontroller
     */
    public HashMap<Dataset, BatchController> getDatasettocontroller() {
        return datasettocontroller;
    }

    /**
     * @param datasettocontroller the datasettocontroller to set
     */
    public void setDatasettocontroller(HashMap<Dataset, BatchController> datasettocontroller) {
        this.datasettocontroller = datasettocontroller;
    }

    public void generateOutput() throws FileNotFoundException, UnsupportedEncodingException, IOException {

        //sort by OGroup and Num, to get order of Input
        getMetTable().getSortOrder().clear();
        getMetTable().getSortOrder().add(numColumn);
        getMetTable().getSortOrder().add(nameColumn);
        getMetTable().sort();

        //generate sorted List
        List<Entry> list = new ArrayList<Entry>();
        TreeItem<Entry> root = getMetTable().getRoot();

        for (int i = 0; i < root.getChildren().size(); i++) {
            list.add(root.getChildren().get(i).getValue());
        }

//        for (int i = 0; i < session.getListofDatasets().size(); i++) {
//            for (int j = 0; j < session.getListofDatasets().get(i).getListofFiles().size(); j++) {
//                RawDataFile file = session.getListofDatasets().get(i).getListofFiles().get(j);
//                System.out.println("File: " + file.getName());
//                for (int o = 0; o < list.size(); o++) {
//                    for (int s = 0; s < list.get(o).getListofAdducts().size(); s++) {
//                        System.out.println("OGroup: " + list.get(o).getOGroup() + "  Number: " + list.get(o).getListofAdducts().get(s).getNum() + "   Area: " + list.get(o).getListofAdducts().get(s).getListofSlices().get(file).getfittedArea());
//                    }
//                }
//
//            }
//
//        }
        //parse Input Matrix again
        TsvParserSettings settings = new TsvParserSettings();
        settings.getFormat().setLineSeparator("\n");

        TsvParser parser = new TsvParser(settings);
        FileReader reader = new FileReader(session.getReferenceTsv());
        List<String[]> allRows = parser.parseAll(reader);

        //get Headers
        List<String> headers = Arrays.asList(allRows.get(0));
        headers = new ArrayList<>(headers);

        //List for convenience
        List<List<String>> rows = new ArrayList<>();
        for (int i = 0; i < allRows.size(); i++) {
            rows.add(new ArrayList<>(Arrays.asList(allRows.get(i))));
        }

        PrintWriter printwriter = new PrintWriter("C:\\Users\\stefankoch\\Documents\\Output\\output.txt", "UTF-8");
        TsvWriter writer = new TsvWriter(printwriter, new TsvWriterSettings());

        //add newly generated adducts to rows
        int currentline = 1;
        for (int o = 0; o < list.size(); o++) {
            for (int s = 0; s < list.get(o).getListofAdducts().size(); s++) {
                Entry adduct = list.get(o).getListofAdducts().get(s);
                //while not included
                while (list.get(o).getOGroup() != Integer.parseInt(rows.get(currentline).get(10))) {
                    currentline++;
                }

                //if old
                if (list.get(o).getListofAdducts().get(s).getNum() <= maxnumber) {
                    currentline++;
                    //if new
                } else {
                    //check if empty
                    boolean empty = true;
                    for (int i = 0; i < session.getListofDatasets().size(); i++) {
                        for (int j = 0; j < session.getListofDatasets().get(i).getListofFiles().size(); j++) {
                            RawDataFile file = session.getListofDatasets().get(i).getListofFiles().get(j);
                            if (adduct.getListofSlices().containsKey(file) && adduct.getListofSlices().get(file).getfittedArea() != null) {
                                empty = false;
                                break;
                            }
                        }
                    }

                    adduct.setEmpty(empty);

                    if (!empty) {
                        List<String> newline = new ArrayList<String>();
                        newline.add(String.valueOf(adduct.getNum()));
                        newline.add(String.valueOf(adduct.getMZ()));
                        newline.add("");
                        newline.add("");
                        newline.add(String.valueOf(adduct.getRT()));
                        newline.add(String.valueOf(adduct.getXn()));
                        newline.add(String.valueOf(adduct.getOriginalAdduct().getCharge()));
                        newline.add(adduct.getOriginalAdduct().getScanEvent());
                        newline.add(adduct.getOriginalAdduct().getIonisation());
                        newline.add("");
                        newline.add(String.valueOf(adduct.getOGroup()));
                        newline.add(adduct.getIon());
                        newline.add("");
                        if (adduct.getM() > 0) {
                            newline.add(String.valueOf(adduct.getM()));
                        } else {
                            newline.add("");
                        }
                        rows.add(currentline, newline);
                        currentline++;
                    }
                }
            }
        }

        //write data from top to bottom, add data of currentfile
        for (int i = 0; i < session.getListofDatasets().size(); i++) {
            for (int j = 0; j < session.getListofDatasets().get(i).getListofFiles().size(); j++) {
                RawDataFile file = session.getListofDatasets().get(i).getListofFiles().get(j);
                headers.add(14, file.getName().substring(0, file.getName().length() - 6) + "_Test_Area");
                currentline = 1;
                for (int o = 0; o < list.size(); o++) {
                    for (int s = 0; s < list.get(o).getListofAdducts().size(); s++) {
                        if (!list.get(o).getListofAdducts().get(s).isEmpty()) {
                            if (list.get(o).getListofAdducts().get(s).getListofSlices().get(file) == null || list.get(o).getListofAdducts().get(s).getListofSlices().get(file).getfittedArea() == null) {
                                rows.get(currentline).add(14, "");
                                currentline++;
                            } else {
                                rows.get(currentline).add(14, Float.toString(list.get(o).getListofAdducts().get(s).getListofSlices().get(file).getfittedArea()));
                                currentline++;
                            }
                        }
                    }
                }

            }

        }

        //back to arrays...
        allRows = new ArrayList<String[]>();
        for (int i = 1; i < rows.size(); i++) {
            String[] row = new String[rows.get(i).size()];
            row = rows.get(i).toArray(row);
            allRows.add(row);
        }

        writer.writeHeaders(headers);
        for (int i = 1; i < rows.size(); i++) {
            writer.writeRow(rows.get(i).toArray());
        }

        writer.close();
        Runtime.getRuntime().exec("explorer.exe /select," + "C:\\Users\\stefankoch\\Documents\\Output\\output.txt");
        getMetTable().getSortOrder().clear();
        getMetTable().getSortOrder().add(mzColumn);
        getMetTable().getSortOrder().add(rtColumn);

    }

    public void generateOutputSpecific() throws FileNotFoundException, IOException {
        //get headers for Output
        String[] headers = outputformat.get(0).getHeaders();
        //because a header could start with "Files:", we may have to adjust them and increase the size
        List<String> nheaders = new ArrayList<>();
        for (String string : headers) {
            if (string != null) {
                if (string.startsWith("Files:")) {
                    for (RawDataFile file : session.getAllFiles()) {
                        nheaders.add(file.getName() + ": " + string.substring(7));
                    }
                } else {
                    nheaders.add(string);
                }
            }
        }

        //generate Strings for all Ions
        List<String[]> OutallRows = new ArrayList<>();

        //initialize
        int c = 0;  //count
        for (Entry entry : MasterListofOGroups) {
            for (Entry adduct : entry.getListofAdducts()) {
                OutallRows.add(new String[nheaders.size()]);
                c++;
            }
        }
        boolean[] hasdata = new boolean[c];

        //iterate over all headers
        c = 0;    //current column

        for (String string : headers) {
            if (string != null) {
                int r = 0; //row
                //find out which information is needed
                switch (string) {
                    case "Reference Retention Time (RT) of Ion":
                        //for all Ions
                        for (Entry entry : MasterListofOGroups) {
                            for (Entry adduct : entry.getListofAdducts()) {
                                OutallRows.get(r)[c] = Float.toString(adduct.getRT());
                                r++;
                            }
                        }
                        c++;
                        break;
                    case "Files: Retention Time of Peak":
                        //for all Files
                        for (RawDataFile file : session.getAllFiles()) {
                            r = 0;
                            //for all Ions
                            for (Entry entry : MasterListofOGroups) {
                                for (Entry adduct : entry.getListofAdducts()) {
                                    if (adduct.getListofSlices().containsKey(file)) {
                                        Peak peak = adduct.getListofSlices().get(file).getFittedPeak();
                                        if (peak != null) {
                                            OutallRows.get(r)[c] = String.valueOf((peak.getIndexRT()));
                                            hasdata[r] = true;
                                        }
                                    }
                                    r++;
                                }
                            }
                            c++;
                        }
                        break;
                    case "Reference Mass/Charge (MZ) of Ion":
                        //for all Ions
                        for (Entry entry : MasterListofOGroups) {
                            for (Entry adduct : entry.getListofAdducts()) {
                                OutallRows.get(r)[c] = Float.toString(adduct.getMZ());
                                r++;
                            }
                        }
                        c++;
                        break;
                    case "Files: Mass/Charge (MZ) of Peak":
                        //for all Files
                        for (RawDataFile file : session.getAllFiles()) {
                            r = 0;
                            //for all Ions
                            for (Entry entry : MasterListofOGroups) {
                                for (Entry adduct : entry.getListofAdducts()) {
                                    if (adduct.getListofSlices().containsKey(file)) {
                                        Peak peak = adduct.getListofSlices().get(file).getFittedPeak();
                                        if (peak != null) {
                                            OutallRows.get(r)[c] = String.valueOf((peak.getMZ()));
                                            hasdata[r] = true;
                                        }
                                    }
                                    r++;
                                }
                            }
                            c++;
                        }
                        break;
                    case "Files: Peak Area":
                        //for all Files
                        for (RawDataFile file : session.getAllFiles()) {
                            r = 0;
                            //for all Ions
                            for (Entry entry : MasterListofOGroups) {
                                for (Entry adduct : entry.getListofAdducts()) {
                                    if (adduct.getListofSlices().containsKey(file)) {
                                        Peak peak = adduct.getListofSlices().get(file).getFittedPeak();
                                        if (peak != null) {
                                            OutallRows.get(r)[c] = String.valueOf((peak.getArea()));
                                            hasdata[r] = true;
                                        }
                                    }
                                    r++;
                                }
                            }
                            c++;
                        }
                        break;
                    case "Ion ID":
                        //for all Ions
                        for (Entry entry : MasterListofOGroups) {
                            for (Entry adduct : entry.getListofAdducts()) {
                                OutallRows.get(r)[c] = Float.toString(adduct.getNum());
                                r++;
                            }
                        }
                        c++;
                        break;
                    case "Metabolite ID":
                        //for all Ions
                        for (Entry entry : MasterListofOGroups) {
                            for (Entry adduct : entry.getListofAdducts()) {
                                OutallRows.get(r)[c] = Float.toString(entry.getOGroup());
                                r++;
                            }
                        }
                        c++;
                        break;
                    case "Number of Carbon Atoms":
                        //for all Ions
                        for (Entry entry : MasterListofOGroups) {
                            for (Entry adduct : entry.getListofAdducts()) {
                                OutallRows.get(r)[c] = Float.toString(adduct.getXn());
                                r++;
                            }
                        }
                        c++;
                        break;
                    case "Ion Form":
                        //for all Ions
                        for (Entry entry : MasterListofOGroups) {
                            for (Entry adduct : entry.getListofAdducts()) {
                                OutallRows.get(r)[c] = adduct.getIon();
                                r++;
                            }
                        }
                        c++;
                        break;
                    case "Uncharged Ion Mass":
                        //for all Ions
                        for (Entry entry : MasterListofOGroups) {
                            for (Entry adduct : entry.getListofAdducts()) {
                                OutallRows.get(r)[c] = Float.toString(adduct.getM());
                                r++;
                            }
                        }
                        c++;
                        break;
                    case "Ion Charge":
                        //for all Ions
                        for (Entry entry : MasterListofOGroups) {
                            for (Entry adduct : entry.getListofAdducts()) {
                                OutallRows.get(r)[c] = Float.toString(adduct.getCharge());
                                r++;
                            }
                        }
                        c++;
                        break;
                    case "Scan Event":
                        //for all Ions
                        for (Entry entry : MasterListofOGroups) {
                            for (Entry adduct : entry.getListofAdducts()) {
                                OutallRows.get(r)[c] = adduct.getScanEvent();
                                r++;
                            }
                        }
                        c++;
                        break;
                    case "Ionisation Mode":
                        //for all Ions
                        for (Entry entry : MasterListofOGroups) {
                            for (Entry adduct : entry.getListofAdducts()) {
                                OutallRows.get(r)[c] = adduct.getIonisation();
                                r++;
                            }
                        }
                        c++;
                        break;
                    case "Files: Retention Time Shift of Peak":
                        //for all Files
                        for (RawDataFile file : session.getAllFiles()) {
                            r = 0;
                            //for all Ions
                            for (Entry entry : MasterListofOGroups) {
                                for (Entry adduct : entry.getListofAdducts()) {
                                    if (adduct.getListofSlices().containsKey(file)) {
                                        Peak peak = adduct.getListofSlices().get(file).getFittedPeak();
                                        if (peak != null) {
                                            OutallRows.get(r)[c] = String.valueOf(peak.getIndexRT() - adduct.getRT());
                                            hasdata[r] = true;
                                        }
                                    }
                                    r++;
                                }
                            }
                            c++;
                        }
                        break;
                    case "Files: Mass/Charge (MZ) Shift of Peak":
                        //for all Files
                        for (RawDataFile file : session.getAllFiles()) {
                            r = 0;
                            //for all Ions
                            for (Entry entry : MasterListofOGroups) {
                                for (Entry adduct : entry.getListofAdducts()) {
                                    if (adduct.getListofSlices().containsKey(file)) {
                                        Peak peak = adduct.getListofSlices().get(file).getFittedPeak();
                                        if (peak != null) {
                                            OutallRows.get(r)[c] = String.valueOf((peak.getMZ() - adduct.getMZ()) / ((adduct.getMZ() / 1000000f)));
                                            hasdata[r] = true;
                                        }
                                    }
                                    r++;
                                }
                            }
                            c++;
                        }
                        break;

                }

            }
        }

        //clean empty Ions
        for (int i = hasdata.length - 1; i >= 0; i--) {
            if (!hasdata[i]) {
                OutallRows.remove(i);
            }
        }

        String file = Paths.get(".").toAbsolutePath().normalize().toString();
        file = file.concat("\\Output\\matched_" + session.getReferenceTsv().getName().substring(0, session.getReferenceTsv().getName().length() - 4) + ".txt");
        PrintWriter printwriter = new PrintWriter(file, "UTF-8");
        TsvWriter writer = new TsvWriter(printwriter, new TsvWriterSettings());
        writer.writeHeaders(nheaders);
        for (int i = 0; i < OutallRows.size(); i++) {
            writer.writeRow(OutallRows.get(i));
        }

        writer.close();
        Runtime.getRuntime().exec("explorer.exe /select," + file);

        System.out.println("Done");

    }

    public void generateOutputExtended() throws FileNotFoundException, UnsupportedEncodingException, IOException {

        //list of OGroups
        List<Entry> ogroups = MasterListofOGroups;
        int[] indices = session.getIndices();

        //parse Input Matrix again
        TsvParserSettings settings = new TsvParserSettings();
        settings.getFormat().setLineSeparator("\n");

        TsvParser parser = new TsvParser(settings);
        FileReader reader = new FileReader(session.getReferenceTsv());
        List<String[]> InallRows = parser.parseAll(reader);
        String[] Inheader = InallRows.get(0);

        //generate new Matrix, with all files and all adducts
        List<String[]> OutallRows = new ArrayList<>();
        //get first file column
        int ffc = InallRows.get(0).length;
        for (int i = 0; i < Inheader.length; i++) {
            System.out.println(Inheader[i]);
            if (Inheader[i].startsWith("_")) {
                ffc = i;
                break;
            }
        }

        //get first non file information
        int fnfi = 0;
        for (int i = ffc + 1; i < Inheader.length; i++) {
            if (!Inheader[i].startsWith("_")) {
                fnfi = i;
                break;
            }
        }

        //number of new columns
        int nof = session.getAllFiles().size();

        //new header
        String[] Outheader = new String[Inheader.length + nof * 4];
        for (int i = 0; i < ffc; i++) {
            Outheader[i] = Inheader[i];
        }
        //current column
        int cc = ffc;
        //write new headers
        for (int i = 0; i < nof; i++) {
            RawDataFile file = session.getAllFiles().get(i);
            file.setColumn(cc);
            Outheader[cc++] = file.getName().substring(0, file.getName().length() - 6) + ".Area";
            Outheader[cc++] = file.getName().substring(0, file.getName().length() - 6) + ".RT-Shift";
            Outheader[cc++] = file.getName().substring(0, file.getName().length() - 6) + ".MZ-Shift";
            Outheader[cc++] = file.getName().substring(0, file.getName().length() - 6) + ".MZ";
        }

        //write old information after files
        for (int i = ffc; i < Inheader.length; i++) {
            Outheader[cc++] = Inheader[i];
        }

        //new number of columns
        int noc = Inheader.length + nof * 4;

        //start on adducts
        List<String[]> Outrows = new ArrayList<>();
        List<Boolean> hasdata = new ArrayList<>();
        for (int o = 0; o < ogroups.size(); o++) {
            for (int a = 0; a < ogroups.get(o).getListofAdducts().size(); a++) {
                Entry adduct = ogroups.get(o).getListofAdducts().get(a);
                //generate basic information including any relevant old info
                String[] info = new String[noc];
                //if old adduct
                if (adduct.getOriginalAdduct() == null) {
                    for (int i = 0; i < ffc; i++) {
                        info[i] = InallRows.get(adduct.getInline())[i];
                    }
                    cc = ffc + nof * 4;
                    for (int i = ffc; i < Inheader.length; i++) {
                        info[cc++] = InallRows.get(adduct.getInline())[i];
                    }
                    hasdata.add(Boolean.TRUE);
                    //if new adduct
                } else {
                    info[indices[0]] = String.valueOf(adduct.getNum());
                    info[indices[1]] = String.valueOf(adduct.getMZ());
                    info[indices[2]] = String.valueOf(adduct.getRT());
                    if (indices[3] > -1) {
                        info[indices[3]] = String.valueOf(adduct.getXn());
                    }
                    info[indices[4]] = String.valueOf(adduct.getOGroup());
                    if (indices[5] > -1) {
                        info[indices[5]] = String.valueOf(adduct.getIon());
                    }
                    if (indices[6] > -1) {
                        info[indices[6]] = String.valueOf(adduct.getM());
                    }
                    if (indices[7] > -1) {
                        if (adduct.getCharge() != -999) {
                            info[indices[7]] = String.valueOf(adduct.getCharge());
                        } else {
                            info[indices[7]] = "";
                        }
                    }
                    if (indices[8] > -1) {
                        info[indices[8]] = String.valueOf(adduct.getScanEvent());
                    }
                    if (indices[9] > -1) {
                        info[indices[9]] = String.valueOf(adduct.getIonisation());
                    }
                    hasdata.add(Boolean.FALSE);
                }

                //write Data
                for (int i = 0; i < session.getAllFiles().size(); i++) {
                    RawDataFile file = session.getAllFiles().get(i);
                    if (adduct.getListofSlices().containsKey(file)) {
                        Peak peak = adduct.getListofSlices().get(file).getFittedPeak();
                        if (peak != null) {
                            info[file.getColumn()] = String.valueOf(peak.getArea());
                            info[file.getColumn() + 1] = String.valueOf((peak.getIndexRT() - adduct.getRT()));
                            info[file.getColumn() + 2] = String.valueOf((peak.getMZ() - adduct.getMZ()) / ((adduct.getMZ() / 1000000f)));
                            info[file.getColumn() + 3] = String.valueOf(peak.getMZ());
                            hasdata.set(hasdata.size() - 1, Boolean.TRUE);
                        }
                    }

                }

                Outrows.add(info);
                adduct.setOutline(Outrows.size() - 1);

            }

        }

        //delete empty 
        for (int i = Outrows.size() - 1; i >= 0; i--) {
            if (!hasdata.get(i)) {
                Outrows.remove(i);
            }
        }
        String file = Paths.get(".").toAbsolutePath().normalize().toString();
        file = file.concat("\\Output\\matched_" + session.getReferenceTsv().getName().substring(0, session.getReferenceTsv().getName().length() - 4) + ".txt");
        PrintWriter printwriter = new PrintWriter(file, "UTF-8");
        TsvWriter writer = new TsvWriter(printwriter, new TsvWriterSettings());
        writer.writeHeaders(Outheader);
        for (int i = 0; i < Outrows.size(); i++) {
            writer.writeRow(Outrows.get(i));
        }

        writer.close();
        Runtime.getRuntime().exec("explorer.exe /select," + file);

        System.out.println("Done");

    }

    public void generateAdducts() {
        if (toggleadductgeneration.selectedProperty().get()) {

            //get highest num of Adduct
            int max = 0;
            for (int i = 0; i < MasterListofOGroups.size(); i++) {
                for (int j = 0; j < MasterListofOGroups.get(i).getListofAdducts().size(); j++) {
                    if (MasterListofOGroups.get(i).getListofAdducts().get(j).getNum() > max) {
                        max = MasterListofOGroups.get(i).getListofAdducts().get(j).getNum();
                    }
                }
            }
            this.maxnumber = max;
            max++;

            for (int o = 0; o < MasterListofOGroups.size(); o++) {
                int size = MasterListofOGroups.get(o).getListofAdducts().size();
                for (int a = 0; a < size; a++) {
                    Entry adduct = MasterListofOGroups.get(o).getListofAdducts().get(a);
                    //for every adduct, check if ion specified
                    if (adduct.getIon() == null) {
                        //if not
                        for (int j = 0; j < session.getListofadductnames().size(); j++) {
                            //subtract every possible adduct
                            for (int k = 0; k < session.getListofadductnames().size(); k++) {
                                //and add every possible adduct
                                if (j != k) {
                                    //don't add the same value
                                    Float mass = adduct.getMZ() + session.getListofadductmasses().get(j) - session.getListofadductmasses().get(k);
                                    Float ppm = mass / 1000000 * session.getMZTolerance();
                                    boolean duplicate = false;
                                    for (int c = 0; c < MasterListofOGroups.get(o).getListofAdducts().size(); c++) {
                                        if (Math.abs(mass - MasterListofOGroups.get(o).getListofAdducts().get(c).getMZ()) < ppm) {
                                            duplicate = true;
                                            // System.out.println("Duplicate generated");
                                            break;
                                        }
                                    }
                                    if (!duplicate) {
                                        String Ion = "[(" + adduct.getNum() + "-" + session.getListofadductnames().get(k) + ")+" + session.getListofadductnames().get(j) + "]+";
                                        MasterListofOGroups.get(o).addAdduct(new Entry(max, mass, adduct.getRT(), adduct.getXn(), adduct.getOGroup(), Ion, adduct.getM(), adduct.getLabeledXn(), session, MasterListofOGroups.get(o), adduct));
                                        max++;
                                    }
                                }
                            }

                        }

                        //if ion specified
                    } else //if multiple Ions specified
                     if (adduct.getIon().indexOf(',') > 0) {
                            //do something
                        } else {
                            String Ion = adduct.getIon().substring(adduct.getIon().indexOf('+') + 1, adduct.getIon().indexOf(']', 3));
                            int k = session.getListofadductnames().indexOf(Ion);
                            //if specified Ion in List
                            if (k > 0) {
                                for (int j = 0; j < session.getListofadductnames().size(); j++) {
                                    //and add every possible adduct
                                    if (j != k) {
                                        //don't add the same value
                                        Float mass = adduct.getMZ() + session.getListofadductmasses().get(j) - session.getListofadductmasses().get(k);
                                        Float ppm = mass / 1000000 * session.getMZTolerance();
                                        boolean duplicate = false;
                                        for (int c = 0; c < MasterListofOGroups.get(o).getListofAdducts().size(); c++) {
                                            if (Math.abs(mass - MasterListofOGroups.get(o).getListofAdducts().get(c).getMZ()) < ppm) {
                                                duplicate = true;
                                                //System.out.println("Duplicate generated");
                                                break;
                                            }
                                        }
                                        if (!duplicate) {
                                            Ion = "[M+" + session.getListofadductnameproperties().get(j).get() + "]+";
                                            MasterListofOGroups.get(o).addAdduct(new Entry(max, adduct.getMZ() + mass, adduct.getRT(), adduct.getXn(), adduct.getOGroup(), Ion, adduct.getM(), adduct.getLabeledXn(), session, MasterListofOGroups.get(o), adduct));
                                            max++;
                                        }
                                    }
                                }

                            }
                        }

                }
            }

        }
    }

    public void generateAdductsnew() {
        if (toggleadductgeneration.selectedProperty().get()) {

            //get highest num of Adduct
            int max = 0;
            for (int i = 0; i < MasterListofOGroups.size(); i++) {
                for (int j = 0; j < MasterListofOGroups.get(i).getListofAdducts().size(); j++) {
                    if (MasterListofOGroups.get(i).getListofAdducts().get(j).getNum() > max) {
                        max = MasterListofOGroups.get(i).getListofAdducts().get(j).getNum();
                    }
                }
            }
            this.maxnumber = max;
            max++;

            double start = System.currentTimeMillis();
            for (int o = 0; o < MasterListofOGroups.size(); o++) {
                //System.out.println(o);
                int size = MasterListofOGroups.get(o).getListofAdducts().size();
                for (int a = 0; a < size; a++) {
                    Entry adduct = MasterListofOGroups.get(o).getListofAdducts().get(a);
                    //for every adduct, check if ion specified
                    if (adduct.getIon() == null) {
                        //if not
                        int j = 0;
                        for (String namen : session.getListofadductnames()) {

                            //add every possible adduct
                            int k = 0;
                            for (String nameo : session.getListofadductnames()) {

                                //to every hypothetical adduct
//                                System.out.println(adduct.getIonisation());
//                                System.out.println(session.getListofadductpolarities().get(k));
                                if (j != k && (adduct.getIonisation() == null || adduct.getIonisation().equals(session.getListofadductpolarities().get(k).substring(0, 1)))) {
                                    //don't add the same value
                                    //get original mass
                                    Float mass = adduct.getMZ() - session.getListofadductmasses().get(k);
                                    mass *= session.getListofadductcharges().get(k);
                                    mass /= session.getListofadductms().get(k);
                                    float M = mass;
                                    //get new mass
                                    mass *= session.getListofadductms().get(j);
                                    mass /= session.getListofadductcharges().get(j);
                                    mass += session.getListofadductmasses().get(j);

                                    boolean duplicate = MasterListofOGroups.get(o).checkforduplicates(mass);
                                    if (!duplicate) {
                                        String Ion;
                                        if (session.getListofadductms().get(j) == 1) {
                                            Ion = "[M(" + adduct.getNum() + ":[";
                                        } else {
                                            Ion = "[" + session.getListofadductms().get(j) + "M(" + adduct.getNum() + ":[";
                                        }

                                        if (session.getListofadductms().get(k) == 1) {
                                            Ion = Ion + "M" + nameo + "]";
                                        } else {
                                            Ion = Ion + session.getListofadductms().get(k) + "M" + nameo + "]";
                                        }
                                        
                                        String sign = session.getListofadductpolarities().get(k);
                                        Ion = Ion.concat(sign);
                                        Ion = Ion.concat(")" + namen + "]");
                                        
                                        sign = session.getListofadductpolarities().get(j);
                                       
                                            Ion = Ion.concat(sign);
                                        
                                        MasterListofOGroups.get(o).addAdduct(new Entry(max, mass, adduct.getRT(), adduct.getXn(), adduct.getOGroup(), Ion, M, adduct.getLabeledXn(), session, MasterListofOGroups.get(o), adduct));
                                        max++;
                                    }
                                }
                                k++;
                            }
                            j++;
                        }

                        //if ion specified
                    } else //if multiple Ions specified
                     if (adduct.getIon().indexOf(',') > 0) {
                            //do something
                            String ionString = adduct.getIon();
                            String mString = adduct.getmString();
//                            List<String> listofadductnames = new ArrayList<>();
//                            List<Float> listofadductms = new ArrayList<>();

//if no Ms specified
                            if (mString == null) {
                                //Placeholder: Pretend there was nothing specified. TODO: Only look for known ones?

                                int j = 0;
                                for (String namen : session.getListofadductnames()) {

                                    //add every possible adduct
                                    int k = 0;
                                    for (String nameo : session.getListofadductnames()) {

                                        //to every hypothetical adduct
                                        if (j != k && (adduct.getIonisation() == null || adduct.getIonisation().equals(session.getListofadductpolarities().get(k).substring(0, 1)))) {
                                            //don't add the same value
                                            //get original mass
                                            Float mass = adduct.getMZ() - session.getListofadductmasses().get(k);
                                            mass *= session.getListofadductcharges().get(k);
                                            mass /= session.getListofadductms().get(k);
                                            float M = mass;
                                            //get new mass
                                            mass *= session.getListofadductms().get(j);
                                            mass /= session.getListofadductcharges().get(j);
                                            mass += session.getListofadductmasses().get(j);

                                            Float ppm = mass / 1000000 * session.getMZTolerance();
                                            boolean duplicate = MasterListofOGroups.get(o).checkforduplicates(mass);
                                            if (!duplicate) {
                                                String Ion;
                                                if (session.getListofadductms().get(j) == 1) {
                                                    Ion = "[M(" + adduct.getNum() + ":[";
                                                } else {
                                                    Ion = "[" + session.getListofadductms().get(j) + "M(" + adduct.getNum() + ":[";
                                                }

                                                if (session.getListofadductms().get(k) == 1) {
                                                    Ion = Ion + "M" + nameo + "]";
                                                } else {
                                                    Ion = Ion + session.getListofadductms().get(k) + "M" + nameo + "]";
                                                }
                                                String charge = session.getListofadductcharges().get(k).toString();
                                                String sign = session.getListofadductpolarities().get(k);
                                                
                                                    Ion = Ion.concat(sign);
                                                
                                                Ion = Ion.concat(")" + namen + "]");
                                                sign = session.getListofadductpolarities().get(j);
                                                
                                                    Ion = Ion.concat(sign);
                                                
                                                MasterListofOGroups.get(o).addAdduct(new Entry(max, mass, adduct.getRT(), adduct.getXn(), adduct.getOGroup(), Ion, M, adduct.getLabeledXn(), session, MasterListofOGroups.get(o), adduct));
                                                max++;
                                            }
                                        }
                                        k++;
                                    }
                                    j++;
                                }

                            } else {

                                String[] nameArray = ionString.split(",");
                                String[] mArray = mString.split(",");

                                int j = 0;
                                for (String namen : session.getListofadductnames()) {

                                    //add every possible adduct
                                    for (int k = 0; k < nameArray.length; k++) {

                                        //to every hypothetical adduct
                                        if (j != k && (adduct.getIonisation() == null || adduct.getIonisation().equals(session.getListofadductpolarities().get(k).substring(0, 1)))) {
                                            //don't add the same value
                                            //get original mass
                                            Float mass = Float.parseFloat(mArray[k]);
                                            float M = mass;
                                            //get new mass
                                            mass *= session.getListofadductms().get(j);
                                            mass /= session.getListofadductcharges().get(j);
                                            mass += session.getListofadductmasses().get(j);

                                            Float ppm = mass / 1000000 * session.getMZTolerance();
                                            boolean duplicate = MasterListofOGroups.get(o).checkforduplicates(mass);
                                            if (!duplicate) {
                                                String Ion;
                                                if (session.getListofadductms().get(j) == 1) {
                                                    Ion = "[M(" + adduct.getNum() + ":";
                                                } else {
                                                    Ion = "[" + session.getListofadductms().get(j) + "M(" + adduct.getNum() + ":";
                                                }

                                                Ion = Ion + nameArray[k];

                                                Ion = Ion.concat(")" + namen + "]");
                                               
                                                String sign = session.getListofadductpolarities().get(j);
                                                
                                                Ion = Ion.concat(sign);
                                                
                                                MasterListofOGroups.get(o).addAdduct(new Entry(max, mass, adduct.getRT(), adduct.getXn(), adduct.getOGroup(), Ion, M, adduct.getLabeledXn(), session, MasterListofOGroups.get(o), adduct));
                                                max++;
                                            }
                                        }
                                    }
                                    j++;
                                }

                            }

                        } else {
                            //if only one ion specified
                            int j = 0;
                            for (String namen : session.getListofadductnames()) {

                                //and add every possible adduct
                                Float mass = adduct.getM();
                                //get new mass
                                mass *= session.getListofadductms().get(j);
                                mass /= session.getListofadductcharges().get(j);
                                mass += session.getListofadductmasses().get(j);

                                Float ppm = mass / 1000000 * session.getMZTolerance();
                                boolean duplicate = MasterListofOGroups.get(o).checkforduplicates(mass);
                                if (!duplicate) {
                                    String Ion;
                                    if (session.getListofadductms().get(j) == 1) {
                                        Ion = "[M(" + adduct.getNum() + ")" + namen + "]";
                                    } else {
                                        Ion = "[" + session.getListofadductms().get(j) + "M(" + adduct.getNum() + ")" + namen + "]";
                                    }
                                    String charge = session.getListofadductcharges().get(j).toString();
                                    String sign = session.getListofadductpolarities().get(j);
                                    
                                        Ion = Ion.concat(sign);
                                    
                                    MasterListofOGroups.get(o).addAdduct(new Entry(max, adduct.getMZ() + mass, adduct.getRT(), adduct.getXn(), adduct.getOGroup(), Ion, adduct.getM(), adduct.getLabeledXn(), session, MasterListofOGroups.get(o), adduct));
                                    max++;
                                }
                                j++;
                            }

                        }

                }
MasterListofOGroups.get(o).sortedmasses=null;
            }

            System.out.println("Total time: " + (System.currentTimeMillis() - start));
        }
        
        
    }

    public void parameters() {
        if (TabPane.visibleProperty().get()) {

        } else {
            showParameters();
        }

    }

    public void showParameters() {
        maskerpane.setVisible(true);
        indicatorbar.setEffect(adjust);
        setParameterPane(true);
        TabPane.setVisible(true);
        accordion.setVisible(false);
        addBatchButton.setVisible(false);
        oldPick = PeakPick.getSelectionModel().getSelectedItem().toString();
        oldBase = Base.getText();
        oldRT = RTTolShift.getText();
        oldSN = Noise.getText();
        oldScales = Scales.getText();
        newOption(paramButton);

    }

    public void hideParameters() {
        accordion.setVisible(true);

        if (currentstep > 2) {
            accordion.setDisable(false);
        }

        maskerpane.setVisible(false);
        indicatorbar.setEffect(shadow);
        setParameterPane(false);
        TabPane.setVisible(false);
        accordion.setVisible(true);
        addBatchButton.setVisible(true);
        session.prepare();

        //indicate change
        boolean changed = false;
        if (!oldPick.equals(PeakPick.getSelectionModel().getSelectedItem().toString())) {
            changed = true;
        }
        if (!oldBase.equals(Base.getText())) {
            changed = true;
        }

//        if (!oldRT.equals(RTTolShift.getText())) {
//            changed = true;
//        }
        if (!oldSN.equals(Noise.getText())) {
            changed = true;
        }

        if (!oldScales.equals(Scales.getText())) {
            changed = true;
        }
        if (changed) {
            session.newPeakPickversion();
        }

    }

    public void setParameterPane(boolean bool) {
//        label1.setVisible(bool);
//        label2.setVisible(bool);
//        label3.setVisible(bool);
//        label4.setVisible(bool);
//        label5.setVisible(bool);
//        label6.setVisible(bool);
//        label7.setVisible(bool);
//        label8.setVisible(bool);
//        label9.setVisible(bool);
//        label10.setVisible(bool);
//        label11.setVisible(bool);
//        box1.setVisible(bool);
//        box2.setVisible(bool);
//        box3.setVisible(bool);
//        box4.setVisible(bool);
//        RTTol.setVisible(bool);
//        RTTolShift.setVisible(bool);
//        MZTol.setVisible(bool);
//        SliceMZTol.setVisible(bool);
//        Res.setVisible(bool);
//        Base.setVisible(bool);
//        PeakPick.setVisible(bool);
        paramButton.setVisible(bool);

    }

    //when closing the window, end all running processes, such as Rengine
    public void close() {
        session.getEngine().end();
        session.getIothread().terminate();

    }

    public void toggleAdductGeneration() {
        boolean toggle = !toggleadductgeneration.selectedProperty().get();
        adductanchor.setDisable(toggle);
    }

    //from: http://stackoverflow.com/questions/7880494/uitableview-better-editing-through-binding
    public static class TextFieldCellFactory
            implements Callback<TableColumn<Information, String>, TableCell<Information, String>> {

        @Override
        public TableCell<Information, String> call(TableColumn<Information, String> param) {
            TextFieldCell textFieldCell = new TextFieldCell();
            return textFieldCell;
        }

        public static class TextFieldCell extends TableCell<Information, String> {

            private TextField textField;
            private StringProperty boundToCurrently = null;

            public TextFieldCell() {
                textField = new TextField();
                textField.focusedProperty().addListener(new ChangeListener<Boolean>() {

                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        TextField tf = (TextField) getGraphic();
                    }
                });
                textField.hoverProperty().addListener(new ChangeListener<Boolean>() {

                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        TextField tf = (TextField) getGraphic();
                    }
                });
                this.setGraphic(textField);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    // Show the Text Field
                    this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

                    // Retrieve the actual String Property that should be bound to the TextField
                    // If the TextField is currently bound to a different StringProperty
                    // Unbind the old property and rebind to the new one
                    ObservableValue<String> ov = getTableColumn().getCellObservableValue(getIndex());
                    SimpleStringProperty sp = (SimpleStringProperty) ov;

                    if (this.boundToCurrently == null) {
                        this.boundToCurrently = sp;
                        this.textField.textProperty().bindBidirectional(sp);
                    } else if (this.boundToCurrently != sp) {
                        this.textField.textProperty().unbindBidirectional(this.boundToCurrently);
                        this.boundToCurrently = sp;
                        this.textField.textProperty().bindBidirectional(this.boundToCurrently);
                    }
//            System.out.println("item=" + item + " ObservableValue<String>=" + ov.getValue());
                    //this.textField.setText(item);  // No longer need this!!!
                } else {
                    this.setContentDisplay(ContentDisplay.TEXT_ONLY);
                }
            }

        }
    }

    public void option1changed() {
        if (option1.selectedProperty().get()) {
            option2.selectedProperty().set(false);
            option2table.setDisable(true);
        } else {
            option2.selectedProperty().set(true);
            option2table.setDisable(false);
        }

    }

    public void option2changed() {
        if (option2.selectedProperty().get()) {
            option1.selectedProperty().set(false);
            option2table.setDisable(false);
        } else {
            option1.selectedProperty().set(true);
            option2table.setDisable(true);
        }

    }

    public class AutoTextFieldCellFactory
            implements Callback<TableColumn<Information, String>, TableCell<Information, String>> {

        @Override
        public TableCell<Information, String> call(TableColumn<Information, String> param) {
            TextFieldCell textFieldCell = new TextFieldCell();
            return textFieldCell;
        }

        public class TextFieldCell extends TableCell<Information, String> {

            private TextField textField;
            private StringProperty boundToCurrently = null;

            public TextFieldCell() {
                String strCss;
                // Padding in Text field cell is not wanted - we want the Textfield itself to "be"
                // The cell.  Though, this is aesthetic only.  to each his own.  comment out
                // to revert back.  
                strCss = "-fx-padding: 0;";

                this.setStyle(strCss);
                textField = new TextField();
                ChangeListener listener = new ChangeListener() {
                    @Override
                    public void changed(ObservableValue o, Object oldVal, Object newVal) {
                        if (((ReadOnlyBooleanProperty) o).get()) {
                            ((TableCell) ((TextField) ((ReadOnlyBooleanProperty) o).getBean()).getParent()).setPrefWidth(260);
                        } else {
                            ((TableCell) ((TextField) ((ReadOnlyBooleanProperty) o).getBean()).getParent()).setPrefWidth(75);
                        }
                    }
                };

                textField.hoverProperty().addListener(listener);
                strCss = ""
                        + //"-fx-background-color: -fx-shadow-highlight-color, -fx-text-box-border, -fx-control-inner-background;" +
                        "-fx-background-color: -fx-control-inner-background;"
                        + //"-fx-background-insets: 0, 1, 2;" +
                        "-fx-background-insets: 0;"
                        + //"-fx-background-radius: 3, 2, 2;" +
                        "-fx-background-radius: 0;"
                        + "-fx-padding: 3 5 3 5;"
                        + /*Play with this value to center the text depending on cell height??*/ //"-fx-padding: 0 0 0 0;" +
                        "-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%);"
                        + "-fx-cursor: text;"
                        + "";

                TextFields.bindAutoCompletion(textField, session.getOutputoptions());
                textField.focusedProperty().addListener(new ChangeListener<Boolean>() {

                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        TextField tf = (TextField) getGraphic();
                        String strStyleGotFocus = "-fx-background-color: lightblue, -fx-text-box-border, -fx-control-inner-background;"
                                + "-fx-background-insets: -0.4, 1, 2;"
                                + "-fx-background-radius: 3.4, 2, 2;";
                        String strStyleLostFocus
                                = //"-fx-background-color: -fx-shadow-highlight-color, -fx-text-box-border, -fx-control-inner-background;" +
                                "-fx-background-color: -fx-control-inner-background;"
                                + //"-fx-background-insets: 0, 1, 2;" +
                                "-fx-background-insets: 0;"
                                + //"-fx-background-radius: 3, 2, 2;" +
                                "-fx-background-radius: 0;"
                                + "-fx-padding: 3 5 3 5;"
                                + /**/ //"-fx-padding: 0 0 0 0;" +
                                "-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%);"
                                + "-fx-cursor: text;"
                                + "";
                        if (newValue.booleanValue()) {
                            tf.setStyle(strStyleGotFocus);
                        } else {
                            tf.setStyle(strStyleLostFocus);
                        }

                    }
                });
                textField.hoverProperty().addListener(new ChangeListener<Boolean>() {
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        TextField tf = (TextField) getGraphic();
                        String strStyleGotHover = "-fx-background-color: lightblue, -fx-text-box-border, derive(-fx-control-inner-background, 10%);"
                                + "-fx-background-insets: 1, 2.8, 3.8;"
                                + "-fx-background-radius: 3.4, 2, 2;";
                        String strStyleLostHover
                                = //"-fx-background-color: -fx-shadow-highlight-color, -fx-text-box-border, -fx-control-inner-background;" +
                                "-fx-background-color: -fx-control-inner-background;"
                                + //"-fx-background-insets: 0, 1, 2;" +
                                "-fx-background-insets: 0;"
                                + //"-fx-background-radius: 3, 2, 2;" +
                                "-fx-background-radius: 0;"
                                + "-fx-padding: 3 5 3 5;"
                                + /**/ //"-fx-padding: 0 0 0 0;" +
                                "-fx-prompt-text-fill: derive(-fx-control-inner-background,-30%);"
                                + "-fx-cursor: text;"
                                + "";
                        String strStyleHasFocus = "-fx-background-color: lightblue, -fx-text-box-border, -fx-control-inner-background;"
                                + "-fx-background-insets: -0.4, 1, 2;"
                                + "-fx-background-radius: 3.4, 2, 2;";
                        if (newValue.booleanValue()) {
                            tf.setStyle(strStyleGotHover);
                        } else if (!tf.focusedProperty().get()) {
                            tf.setStyle(strStyleLostHover);
                        } else {
                            tf.setStyle(strStyleHasFocus);
                        }

                    }
                });
                textField.setStyle(strCss);
                this.setGraphic(textField);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    // Show the Text Field
                    this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

                    // Retrieve the actual String Property that should be bound to the TextField
                    // If the TextField is currently bound to a different StringProperty
                    // Unbind the old property and rebind to the new one
                    ObservableValue<String> ov = getTableColumn().getCellObservableValue(getIndex());
                    SimpleStringProperty sp = (SimpleStringProperty) ov;

                    if (this.boundToCurrently == null) {
                        this.boundToCurrently = sp;
                        this.textField.textProperty().bindBidirectional(sp);
                    } else if (this.boundToCurrently != sp) {
                        this.textField.textProperty().unbindBidirectional(this.boundToCurrently);
                        this.boundToCurrently = sp;
                        this.textField.textProperty().bindBidirectional(this.boundToCurrently);
                    }
//            System.out.println("item=" + item + " ObservableValue<String>=" + ov.getValue());
                    //this.textField.setText(item);  // No longer need this!!!
                } else {
                    this.setContentDisplay(ContentDisplay.TEXT_ONLY);
                }
            }

        }
    }

    public void Output() throws UnsupportedEncodingException, IOException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                progressbar.setVisible(true);
                progressbar.progressProperty().unbind();
                progressbar.progressProperty().set(-1.0);
            }
        });

        Task task = new Task<Void>() {
            @Override
            public Void call() throws IOException, InterruptedException {
                try {
                    if (changedcheckbox.selectedProperty().get()) {
                        for (RawDataFile file : session.getAllFiles()) {
                            file.writeChangedFile();
                        }
                    }

                    if (option1.selectedProperty().get()) {
                        generateOutputExtended();
                    } else {
                        generateOutputSpecific();
                    }

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            progressbar.setVisible(false);
                        }
                    });

                            //-----------------------------------
//        //debug: delete 25% of OGroups at random
//        int currentsize = MasterListofOGroups.size();
//        System.out.println("Old number of Groups: " + currentsize);
//        int nextsize = (int) (currentsize*0.75);
//        
//        while (MasterListofOGroups.size()>nextsize) {
//            int max = MasterListofOGroups.size();
//            int min = 0;
//            int rand = ThreadLocalRandom.current().nextInt(min, max);
//            MasterListofOGroups.remove(rand);
//        }
//         System.out.println("New number of Groups: " + MasterListofOGroups.size());
//        session.newPeakPickversion();
//        
//        //---------------------------------
                    return null;
                } catch (Exception e) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "");
                            alert.setTitle("Error");
                            alert.setHeaderText("Output file could not be written!");
                            alert.setContentText("MetMatch can't write the output file.\n\nPlease exit all other applications that are currently accessing the output file and try again.");
                            alert.showAndWait();
                            progressbar.setVisible(false);
                        }
                    });
                    



                    return null;
                }

            }

        };

        //new thread that executes task
        new Thread(task).start();

    }

    public void initializeButtons() {
        //parameterButton, referenceMatrixButton, referenceFilesButton, batchFilesButton, checkResultsButton, p1, p2, p3, p4, p5, p6

        initOption(parameterButton);
        initOption(referenceMatrixButton);
        initOption(referenceFilesButton);
        initOption(batchFilesButton);
        initOption(checkResultsButton);
        initOption(outputButton);
        initOption(shiftButton);
        setstep(2);
        newOption(paramButton);

        //TEST
//        parameterButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent evt) {
//                
// 
//                if (evt.getButton()==MouseButton.SECONDARY) {
//                    
// 
//                    double targetX = evt.getScreenX();
//                    double targetY = evt.getScreenY();
// 
//                    GridPane pane = new GridPane();
//                    pane.add(new Label(""), 1, 1);
//                    pane.add(new Label("TODO, could contain parameters, images, descriptions, help..."), 1, 2);
//                    pane.add(new Label(""), 1, 3);
//                    pane.add(new TextField("Parameter!"), 1, 4);
//                    pane.add(new Label(""), 1, 5);
//                    Label label = new Label("?");
//                    pane.add(label, 1, 6);
//         
//                    pane.add(new Label(""), 1, 7);
//                    ImageView view = new ImageView();
//                    view.setImage(new Image("file:TestBild.png", true));
//                    
//                    
//                    
//                    PopOver popOver = new PopOver(pane);
//                    popOver.setTitle("Detachable!");
//                               label.setOnMouseEntered(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent evt) {
//                double start = System.currentTimeMillis();
//                boolean finished = false;
//                while (!finished&&System.currentTimeMillis()-start<750) {
//                if (System.currentTimeMillis()-start>500&&label.isHover()) {
//                    try {
//                        finished=true;
//                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/fxml_gravityshiftview.fxml"));
//                        Pane myPane = (Pane) loader.load();
//                        pane.add(myPane, 1, 8);
//                        pane.getChildren().remove(label);
//                    } catch (IOException ex) {
//                        Logger.getLogger(FXMLTableViewController.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//                
//            }
//            }
//        });
// 
//                    double size = 3;
//                   
//                        popOver.show(outputButton, targetX, targetY);
//                    
//                }
//            }
//        });
    }

    public void newOption(Button button) {
        button.setDisable(false);
        button.setStyle(
                "-fx-background-radius: 5em; "
                + "-fx-base: #2CFF00; "
                + "-fx-focus-color: transparent; "
                + "-fx-background-insets: 0;"
        );

    }

    public void oldOption(Button button) {
        button.setDisable(false);
        button.setStyle(
                "-fx-background-radius: 5em; "
                + "-fx-base: #D8FFCF; "
                + "-fx-focus-color: transparent; "
                + "-fx-background-insets: 0;"
        );
    }

    public void activePath(Button button) {
        button.setDisable(false);
        button.setStyle(
                "-fx-base: #D8FFCF; "
                + "-fx-background-insets: 0;"
        );
    }

    public void initOption(Button button) {
        button.setStyle(
                "-fx-background-radius: 5em; "
                + "-fx-focus-color: transparent; "
                + "-fx-background-insets: 0; "
                + "-fx-opacity: 1.0; "
                + "-fx-base: #AFAFAF; "
        );
    }

    public void disableOption(Button button) {
        button.setDisable(true);
        button.setStyle("-fx-background-radius: 5em; "
                + "-fx-focus-color: transparent; "
                + "-fx-background-insets: 0; "
                + "-fx-opacity: 1.0; "
                + "-fx-base: #AFAFAF; "
        );
    }

    public void inactivePath(Button button) {
        button.setDisable(true);
        button.setStyle(
                "-fx-background-insets: 0;"
                + "-fx-opacity: 1.0; "
                + "-fx-base: #AFAFAF; "
        );
    }

    public void openReferenceFiles() throws FileNotFoundException {
        session.getReference().getController().openBatchmzxmlChooser();
    }

    public void openBatchFiles() throws FileNotFoundException {
        Batch batch = addBatch();
        batch.getController().openBatchmzxmlChooser();
    }

    public class FloatStringComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {

            if (o1.isEmpty()) {
                return -1;
            }
            if (o2.isEmpty()) {
                return 1;
            }
            if (Float.parseFloat(o1) == Float.parseFloat(o2)) {
                return 0;
            }

            if (Float.parseFloat(o1) > Float.parseFloat(o2)) {
                return 1;
            }
            if (Float.parseFloat(o1) < Float.parseFloat(o2)) {
                return -1;
            }
            return 0;
        }
    }

    public class MZStringComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            //get the first number
            if (o1.contains("to")) {
                o1 = o1.substring(0, o1.indexOf("to") - 1);
            }

            if (o2.contains("to")) {
                o2 = o2.substring(0, o2.indexOf("to") - 1);
            }

            if (o1.isEmpty()) {
                return -1;
            }
            if (o2.isEmpty()) {
                return 1;
            }
            if (Float.parseFloat(o1) == Float.parseFloat(o2)) {
                return 0;
            }

            if (Float.parseFloat(o1) > Float.parseFloat(o2)) {
                return 1;
            }
            if (Float.parseFloat(o1) < Float.parseFloat(o2)) {
                return -1;
            }
            return 0;
        }

    }

//update progress indication bar            
    public void setstep(int step) {

        //check for highest allowed step
        if (step == 7 && currentstep == 5) {
        } else if (step == 4 && currentstep > 4) {
            return;
        } else if (step - currentstep > 1) {
            return;
        } else {
        }
        currentstep = step;

        inactivePath(p1);
        inactivePath(p2);
        inactivePath(p3);
        inactivePath(p4);
        inactivePath(p5);
        inactivePath(p6);
        disableOption(parameterButton);
        disableOption(referenceMatrixButton);
        disableOption(referenceFilesButton);
        disableOption(batchFilesButton);
        disableOption(shiftButton);
        disableOption(checkResultsButton);
        disableOption(outputButton);

        //activate buttons
        switch (step) {
            case 7:
                oldOption(outputButton);
                activePath(p6);
            case 6:
                oldOption(checkResultsButton);
                activePath(p5);
            case 5:
                oldOption(shiftButton);
                activePath(p4);
            case 4:
                oldOption(batchFilesButton);
                activePath(p3);
            case 3:
                oldOption(referenceFilesButton);
                activePath(p2);
            case 2:
                oldOption(referenceMatrixButton);
                activePath(p1);
            case 1:
                oldOption(parameterButton);
        }

        switch (step) {
            case 7:
                newOption(outputButton);
                break;
            case 6:
                newOption(checkResultsButton);
                break;
            case 5:
                newOption(shiftButton);
                break;
            case 4:
                newOption(batchFilesButton);
                break;
            case 3:
                newOption(referenceFilesButton);
                break;
            case 2:
                newOption(referenceMatrixButton);
                break;
            case 1:
                newOption(parameterButton);
        }
    }

//to open files with system program associated with the file
    public static boolean open(File file) {
        try {
            if (OSDetector.isWindows()) {
                Runtime.getRuntime().exec(new String[]{"rundll32", "url.dll,FileProtocolHandler",
                    file.getAbsolutePath()});
                return true;
            } else if (OSDetector.isLinux() || OSDetector.isMac()) {
                Runtime.getRuntime().exec(new String[]{"/usr/bin/open",
                    file.getAbsolutePath()});
                return true;
            } else // Unknown OS, try with desktop
            {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file);
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return false;
        }
    }

    public char lastChar(String string) {
        return string.charAt(string.length() - 1);
    }

    public void loadParameters() throws FileNotFoundException, IOException {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Properties file (.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        //Set to user directory or go to default if cannot access
        String userDirectoryString = System.getProperty("user.dir");
        File userDirectory = new File(userDirectoryString + "//Parameters");
        if (!userDirectory.canRead()) {
            userDirectory = new File("c:/");
        }
        fileChooser.setInitialDirectory(userDirectory);

        File file = fileChooser.showOpenDialog(null);

        Properties properties = new Properties();
        FileInputStream in = new FileInputStream(file);
        properties.load(in);
        in.close();

        session.loadParameters(properties);

    }

    public void saveParameters() throws FileNotFoundException, IOException {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Properties file (.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        //Set to user directory or go to default if cannot access
        String userDirectoryString = System.getProperty("user.dir");
        File userDirectory = new File(userDirectoryString + "//Parameters");
        if (!userDirectory.canRead()) {
            userDirectory = new File("c:/");
        }
        fileChooser.setInitialDirectory(userDirectory);

        File file = fileChooser.showSaveDialog(null);

        session.saveParameters(file);

    }

    public void loaddefaultParameters() throws FileNotFoundException, IOException {

        //Set extension filter
        Properties properties = new Properties();
        FileInputStream in = new FileInputStream("Parameters//default.txt");
        properties.load(in);
        in.close();

        session.loadParameters(properties);

    }

}

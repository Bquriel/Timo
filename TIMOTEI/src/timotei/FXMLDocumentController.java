package timotei;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;

public class FXMLDocumentController implements Initializable {
    private Post post;
    @FXML
    private WebView webWindow;
    @FXML
    private Label inputLabel;
    @FXML
    private ImageView imageSendMail;
    @FXML
    private ImageView imageManageItems;
    @FXML
    private TextArea addPostInfo;
    @FXML
    private TextArea itemInfoText;
    @FXML
    private TextField inputItemName;
    @FXML
    private TextArea inputItemDescription;
    @FXML
    private Slider sliderX;
    @FXML
    private TabPane tabPane;
    @FXML
    private Button createItemButton;
    @FXML
    private CheckBox breakableCheckBox;
    @FXML
    private Slider sliderY;
    @FXML
    private Slider sliderZ;
    @FXML
    private Button deleteItemButton;
    @FXML
    private TextField inputItemWeight;
    @FXML
    private Label itemCreatedLabel;
    @FXML
    private ComboBox<SmartPost> allPostsComboBox;
    @FXML
    private ComboBox<SmartPost> availablePostsComboBox;
    @FXML
    private TextArea deletePostInfo;
    @FXML
    private TextArea itemInfoTextSend;
    @FXML
    private ComboBox<Item> itemViewComboBox;
    @FXML
    private ComboBox<Package> packageClasses;
    @FXML
    private ComboBox<Item> itemViewSend;
    @FXML
    private TextField senderForename;
    @FXML
    private TextField senderSurname;
    @FXML
    private TextField receiverForename;
    @FXML
    private TextField receiverSurname;
    @FXML
    private ListView<LogEvent> log;
    @FXML
    private Button sendPackageButton;
    @FXML
    private Label packageSentLabel;
    @FXML
    private ComboBox<SmartPost> availablePostsSender;
    @FXML
    private ComboBox<SmartPost> availablePostsReceiver;
    @FXML
    private TabPane mapLogWindow;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        webWindow.getEngine().load(getClass().getResource("indexT.html").toExternalForm());
        setImages();
        post = Post.getInstance();
        post.initialize( Boolean.valueOf( inputLabel.getText()));
        allPostsComboBox.getItems().addAll( post.getSmartPosts());
        log.getItems().addAll( post.getLog());
        addPostInfo.setVisible( false );
        deletePostInfo.setVisible( false );
        deleteItemButton.setDisable( true );
        itemViewComboBox.getItems().addAll( post.getItems());
        itemViewSend.getItems().addAll( post.getItems());
        packageClasses.getItems().addAll( post.getPackages());
    }    
    
    //Sets imagefields.
    private void setImages(){
        File file = new File("ManageItems.png");
        Image image = new Image( file.toURI().toString() );
        imageManageItems.setImage( image );
        File file2 = new File("mail.png");
        Image image2 = new Image( file2.toURI().toString() );
        imageSendMail.setImage( image2 );
    }
    
    //Checks whether creating item is allowed.
    private void checkItemCreationParameters(){
        itemCreatedLabel.setVisible( false );
        createItemButton.setDisable( true );
        boolean isFloat = true;
        String name     = inputItemName.getText();
        String weight   = inputItemWeight.getText();
        try{
            Float.valueOf( weight );
        } catch( NumberFormatException ex ){
            isFloat = false;
        }
        if( !name.equals( "" ) && sliderX.getValue() != 0 
                && sliderY.getValue() != 0 && sliderZ.getValue() != 0 && isFloat ){
            createItemButton.setDisable( false );
        }
    }
    
    //Checks whether sending item is allowed.
    private void checkSendParameters(){
        packageSentLabel.setVisible( false );
        sendPackageButton.setDisable( true );
        Item i = itemViewSend.getValue();
        Package p = packageClasses.getValue();
        if( !senderForename.getText().equals( "" ) && 
                !senderSurname.getText().equals( "" ) && 
                !receiverForename.getText().equals( "" ) && 
                !receiverSurname.getText().equals( "" ) &&
                i != null && p != null && 
                availablePostsSender.getValue() != null &&
                availablePostsReceiver.getValue() != null ){
            sendPackageButton.setDisable( false );
        }
    }
    
    //Resets all fields after use for user comfrot.
    private void resetFields(){
        //Main window
        allPostsComboBox.setValue( null );
        addPostInfo.clear();
        availablePostsComboBox.setValue( null );
        deletePostInfo.clear();
        addPostInfo.setVisible( false );
        deletePostInfo.setVisible( false );
        
        //Item window
        itemViewComboBox.setValue( null );
        itemInfoText.clear();
        createItemButton.setDisable( true );
        inputItemName.clear();
        inputItemDescription.clear();
        inputItemWeight.clear();
        sliderX.setValue( 0 );
        sliderY.setValue( 0 );
        sliderZ.setValue( 0 );
        breakableCheckBox.setSelected( false );
        itemCreatedLabel.setVisible( false );
        
        //Package window
        senderForename.clear();
        senderSurname.clear();
        receiverForename.clear();
        receiverSurname.clear();
        itemViewSend.setValue( null );
        availablePostsSender.setValue( null );
        availablePostsReceiver.setValue( null );
        itemInfoTextSend.clear();
        packageClasses.setValue( null );
        sendPackageButton.setDisable( true );
        packageSentLabel.setVisible( false );
    }

    @FXML
    private void onManageItemsPressed(ActionEvent event) {
        tabPane.getSelectionModel().select( 1 );
        resetFields();
    }

    @FXML
    private void onSendPackagePressed(ActionEvent event) {
        tabPane.getSelectionModel().select( 2 );
        resetFields();
    }

    @FXML
    private void onComboboxSelected(Event event) {
        SmartPost temp = allPostsComboBox.getValue();
        if( temp != null ){
            addPostInfo.setVisible( true );
            addPostInfo.setText( temp.getInfo());
        }
    }
    
    @FXML
    private void onAvailableComboboxSelected(Event event) {
        SmartPost temp = availablePostsComboBox.getValue();
        if( temp != null ){
            deletePostInfo.setVisible( true );
            deletePostInfo.setText( temp.getInfo());
        }
    }

    @FXML
    private void addButtonPressed(ActionEvent event) {
        SmartPost temp = allPostsComboBox.getValue();
        if( temp != null ){
            availablePostsComboBox.getItems().add( temp );
            availablePostsSender.getItems().add( temp );
            availablePostsReceiver.getItems().add( temp );
            double lon = temp.getLatitude();
            double lat = temp.getLongitude();
            String info = temp.getInfoForMap();
            webWindow.getEngine().executeScript( 
                    "document.createMarker(" + lon + "," + lat + ", '" + info + "')");
        }
    }
    
    @FXML
    private void deleteButtonPressed(ActionEvent event) {
        SmartPost temp = availablePostsComboBox.getValue();
        if( temp != null ){
            availablePostsComboBox.getItems().remove( temp );
            availablePostsSender.getItems().remove( temp );
            availablePostsReceiver.getItems().remove( temp );
            if( (temp = availablePostsComboBox.getValue()) == null ){
                deletePostInfo.clear();
                deletePostInfo.setVisible( false );
            } else{
                deletePostInfo.setText( temp.getInfo() );
            }
        }
    }

    @FXML
    private void itemSelectClicked(Event event) {
        Item temp = itemViewComboBox.getValue();
        if( temp != null ){
            itemInfoText.setText( temp.getInfo());
            deleteItemButton.setDisable( false );
        }
    }

    @FXML
    private void onCreateItemClicked(ActionEvent event) {
        float weight = Float.valueOf( inputItemWeight.getText());
        float x = (float) sliderX.getValue() ;
        float y = (float) sliderY.getValue();
        float z = (float) sliderZ.getValue();
        Item i = new Item( inputItemName.getText(), inputItemDescription.getText(), 
        weight, x, y, z, breakableCheckBox.isSelected());
        if( post.addItem( i ) ){
            resetFields();
            itemCreatedLabel.setVisible( true );
            itemViewComboBox.getItems().add( i );
            itemViewSend.getItems().add( i );
            ItemEvent e = new ItemEvent( i.getName(), true );
            post.addLogEvent( e );
            log.getItems().add( e );
        }
    }

    @FXML
    private void onDeleteSelectedItemClicked(ActionEvent event) {
        Item i = itemViewComboBox.getValue();
        if( post.removeItem( i )){
            itemViewComboBox.getItems().remove( i );
            itemViewSend.getItems().remove( i );
            ItemEvent e = new ItemEvent( i.getName(), false );
            post.addLogEvent( e );
            log.getItems().add( e );
            
            //Update item info.
            i = itemViewComboBox.getValue();
            if( i != null ){
                itemInfoText.setText( i.getInfo());
            } else{
                deleteItemButton.setDisable( true );
                itemInfoText.setText( "" );
            }
        }
    }

    @FXML
    private void onBackFromManageButtonClicked(ActionEvent event) {
        tabPane.getSelectionModel().select( 0 );
        resetFields();
    }

    @FXML
    private void onBackFromSendButtonClicked(ActionEvent event) {
        tabPane.getSelectionModel().select( 0 );
        resetFields();
    }

    @FXML
    private void onDragDimensionDone(MouseEvent event) {
        checkItemCreationParameters();
    }

    @FXML
    private void onItemInfoGiven(KeyEvent event) {
        checkItemCreationParameters();
    }

    @FXML
    private void itemSelectClickedSend(Event event) {
        Item temp = itemViewSend.getValue();
        if( temp != null ){
            itemInfoTextSend.setText( temp.getInfo());
        }
        checkSendParameters();
    }


    @FXML
    private void onPackageClassChosen(Event event) {
        checkSendParameters();
    }

    @FXML
    private void onPackageSent(ActionEvent event) {
        ArrayList<Double> coords = new ArrayList();
        SmartPost start = availablePostsSender.getValue();
        SmartPost end   = availablePostsReceiver.getValue();
        Item i          = itemViewSend.getValue();
        Package pack    = packageClasses.getValue();
        coords.add( start.getLatitude());
        coords.add( start.getLongitude());
        coords.add( end.getLatitude());
        coords.add( end.getLongitude());
        int distance = (int) Math.round((double) webWindow.getEngine().executeScript( 
                "document.pathLength(" + coords + ")" ));
        if( pack.canSendItem( i, distance )){
            webWindow.getEngine().executeScript( "document.createPath(" + coords +
                ", 'blue'," + pack.getCategory() + ")" );
            if( start.sendPackage( i, pack, distance )){
                packageSentLabel.setText( "Package sent!" );
                packageSentLabel.setVisible( true );
                LogEvent e = new SendEvent( senderForename.getText(), senderSurname.getText(),
                    receiverForename.getText(), receiverSurname.getText(), start.toString(), 
                    end.toString(), distance, i.getName(), pack.toString(), false );
                log.getItems().add( e );
                post.addLogEvent( e );                
            } else{
                packageSentLabel.setText( "Item broke down!" );
                packageSentLabel.setVisible( true );   
                LogEvent e = new SendEvent( senderForename.getText(), senderSurname.getText(),
                    receiverForename.getText(), receiverSurname.getText(), start.toString(), 
                    end.toString(), distance, i.getName(), pack.toString(), true );
                log.getItems().add( e );
                post.addLogEvent( e );
            }
        } else{
            packageSentLabel.setText( "Item does not fit!" );
            packageSentLabel.setVisible( true );   
        }
        
    }

    @FXML
    private void onSendInfoEntered(KeyEvent event) {
        checkSendParameters();
    }

    @FXML
    private void onDeletePathsPressed(ActionEvent event) {
        webWindow.getEngine().executeScript( "document.deletePaths()" );
    }

    public void onClose(){
        System.out.println( "Writing check" );
        System.out.println( "Exiting application.." );
        post.writeCheck();
        post.closeDatabaseConnection();
    }
}
package InfoMan.P212.controller;

import InfoMan.P212.DatabaseConnection;
import InfoMan.P212.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainController {
    @FXML
    private TextField firstName;

    @FXML
    private TextField middleName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField address;

    @FXML
    private TextField phoneNumber;

    @FXML
    private TextField email;

    @FXML
    private RadioButton male;

    @FXML
    private RadioButton female;

    @FXML
    private TableView <Student> table;

    @FXML
    private TableColumn<Student, String> colFirstName;

    @FXML
    private TableColumn<Student, String> colMiddleName;

    @FXML
    private TableColumn<Student, String> colLastName;

    @FXML
    private TableColumn<Student, String> colAddress;

    @FXML
    private TableColumn<Student, String> colPhoneNumber;

    @FXML
    private TableColumn<Student, String> colEmail;

    @FXML
    private TableColumn<Student, String> colGender;

    private boolean isEditing = false;
    private int studentId = 0;
    public ToggleGroup gender;
    private DatabaseConnection db;

    private ObservableList<Student> studentList = FXCollections.observableArrayList();

    public void initialize() throws SQLException{
        db = new DatabaseConnection();

        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colMiddleName.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));

        loadStudents();
    }
    public void loadStudents() throws SQLException{
        studentList.clear();
        String sql = "SELECT * from students";
        
        Statement pstmt  = db.getConnection().createStatement();
        ResultSet result = pstmt.executeQuery(sql);

        while(result.next()){
            Student student = new Student(result.getInt("id"),
                    result.getString("first_name"),
                    result.getString("middle_name"),
                    result.getString("last_name"),
                    result.getString("address"),
                    result.getString("phone_number"),
                    result.getString("email"),
                    result.getString("gender"));
            studentList.add(student);
        }

        table.setItems(studentList);

    }

    @FXML
    private void save() throws SQLException {
        if (!isEditing) {
            String sql = "INSERT INTO students(first_name, middle_name, last_name, address, phone_number, email, gender) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = db.getConnection().prepareStatement(sql);
            pstmt.setString(1, firstName.getText());
            pstmt.setString(2, middleName.getText());
            pstmt.setString(3, lastName.getText());
            pstmt.setString(4, address.getText());
            pstmt.setString(5, phoneNumber.getText());
            pstmt.setString(6, email.getText());
            if (male.isSelected()) {
                pstmt.setString(7, "Male");
            } else if (female.isSelected()) {
                pstmt.setString(7, "Female");
            }
            if (pstmt.executeUpdate() == 1){
                firstName.clear();
                middleName.clear();
                lastName.clear();
                address.clear();
                phoneNumber.clear();
                email.clear();
                loadStudents();
            }
        } else{
            String sql = "UPDATE students SET first_name = ?, middle_name = ?, last_name = ?, address = ?, phone_number = ?, email = ?, gender = ? WHERE id = ?";
            try{
                PreparedStatement pstmt = db.getConnection().prepareStatement(sql);
                pstmt.setString(1, firstName.getText());
                pstmt.setString(2, middleName.getText());
                pstmt.setString(3, lastName.getText());
                pstmt.setString(4, address.getText());
                pstmt.setString(5, phoneNumber.getText());
                pstmt.setString(6, email.getText());
                if (male.isSelected()){
                    pstmt.setString(7, "Male");
                }
                else if (female.isSelected()){
                    pstmt.setString(7,"Female");
                }
                pstmt.setInt(8, studentId);

                if (pstmt.executeUpdate() == 1) {
                    firstName.clear();
                    middleName.clear();
                    lastName.clear();
                    address.clear();
                    phoneNumber.clear();
                    email.clear();
                    loadStudents ();
                    isEditing = false;
                    studentId = 0;
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
     @FXML
    private void delete(){
        Student selectedStudent = table.getSelectionModel().getSelectedItem();
        if(selectedStudent  != null){
            String sql = "DELETE from students WHERE id = ?";
            try{
                PreparedStatement pstmt = db.getConnection().prepareStatement(sql);
                pstmt.setInt(1, selectedStudent.getId());
                pstmt.executeUpdate();

                studentList.remove(selectedStudent);
            }catch(SQLException e){
                e.printStackTrace();
            }

        }

     }

     @FXML
    private void edit(){
         Student selectedStudent = table.getSelectionModel().getSelectedItem();
         if(selectedStudent  != null){
             firstName.setText(selectedStudent.getFirstName());
             middleName.setText(selectedStudent.getMiddleName());
             lastName.setText(selectedStudent.getLastName());
             address.setText(selectedStudent.getAddress());
             phoneNumber.setText(selectedStudent.getPhoneNumber());
             email.setText(selectedStudent.getEmail());
             isEditing = true;
             studentId = selectedStudent.getId();
         }

     }
}

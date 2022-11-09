package chemintippool;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.RetentionPolicy;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.Sheets.Spreadsheets.Values.Append;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.DeleteDimensionRequest;
import com.google.api.services.sheets.v4.model.DimensionRange;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.UpdateValuesByDataFilterResponse;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;



public class App {
    private static Sheets sheetsService;
    private static String APPLICATION_NAME = "Example app";
    private static String SPREADSHEET_ID = "1SsNd_VDjHxkLj3WsbxnHt6NAW1rqmkui_3xwAGp824A";

    private static Credential authorize() throws IOException, GeneralSecurityException {
        InputStream in = App.class.getResourceAsStream("/credentials.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
            GsonFactory.getDefaultInstance(), new InputStreamReader(in)
        );

        List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
            GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(),
            clientSecrets, scopes)
            .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
            .setAccessType("offline")
            .build();

        Credential credential = new AuthorizationCodeInstalledApp(
            flow, new LocalServerReceiver())
            .authorize("user");

        return credential;
    }

    public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
        Credential credential = authorize();

        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
            GsonFactory.getDefaultInstance(),credential)
            .setApplicationName(APPLICATION_NAME)
            .build();

    }

    private static JFrame f = new JFrame("Chemin Tip Pool Manager");

    private static JPanel container = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private static JPanel title = new JPanel();
    private static JPanel textEntry = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private static JPanel body = new JPanel(new GridLayout(1, 4));
    private static JPanel shiftType = new JPanel(new GridLayout(4, 1));
    private static JPanel barStaff;
    private static JPanel runningStaff;
    private static JPanel servingStaff;
    private static JPanel savePanel = new JPanel();

    private static List<Employee> barStaffList = new ArrayList();
    private static List<Employee> runningStaffList = new ArrayList();
    private static List<Employee> servingStaffList = new ArrayList();

    
    private static String[] shiftTypes = {"Morning", "Afternoon", "Evening"};
    private static JRadioButton[] shiftButtons = new JRadioButton[shiftTypes.length];
    private static ButtonGroup shifts = new ButtonGroup();
    private static List<JRadioButton> barButtons = new ArrayList();
    private static List<JRadioButton> runningButtons = new ArrayList();
    private static List<JRadioButton> servingButtons = new ArrayList();
    private static JTextField date = new JTextField(10);
    private static JTextField tips = new JTextField(10);
    //private static JButton saveButton = new JButton("Save");


    private static double bartenderCut;
    private static double runnerCut;
    private static double serverCut;

    public static void main(String[] args) throws IOException, GeneralSecurityException{
        sheetsService = getSheetsService();
        String range = "Employees!A2:G100";
        String controlRange = "Controls!B2:B4";


        ValueRange response = sheetsService.spreadsheets().values()
            .get(SPREADSHEET_ID, range)
            .execute();


        ValueRange controlResponse = sheetsService.spreadsheets().values()
            .get(SPREADSHEET_ID, controlRange)
            .execute();

        List<List<Object>> values = response.getValues();
        List<List<Object>> controls = controlResponse.getValues();

        if (controls == null || controls.isEmpty()) {
            System.out.println("No data found.");
        } else {
            bartenderCut = Double.parseDouble(controls.get(0).get(0).toString());
            runnerCut = Double.parseDouble(controls.get(1).get(0).toString());
            serverCut = Double.parseDouble(controls.get(2).get(0).toString());
        }

        
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
            for (List row : values) {
                if(row.get(3).equals("Bartender")) {
                    barStaffList.add(new Employee(row.get(0).toString(), row.get(1).toString(), row.get(2).toString(), row.get(3).toString(), Double.parseDouble(row.get(4).toString()), Double.parseDouble(row.get(5).toString()), Double.parseDouble(row.get(6).toString())));
                }
            }
        }
        
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
            for (List row : values) {
                if(row.get(3).equals("Runner")) {
                    runningStaffList.add(new Employee(row.get(0).toString(), row.get(1).toString(), row.get(2).toString(), row.get(3).toString(), Double.parseDouble(row.get(4).toString()), Double.parseDouble(row.get(5).toString()), Double.parseDouble(row.get(6).toString())));
                }
            }
        }

        
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
            for (List row : values) {
                if(row.get(3).equals("Server")) {
                    servingStaffList.add(new Employee(row.get(0).toString(), row.get(1).toString(), row.get(2).toString(), row.get(3).toString(), Double.parseDouble(row.get(4).toString()), Double.parseDouble(row.get(5).toString()), Double.parseDouble(row.get(6).toString())));
                }
            }
        }


        //temp
        // List<Employee> bartendersExample = Arrays.asList(barStaffList.get(0), barStaffList.get(2), barStaffList.get(3));
        // List<Employee> runnersExample = Arrays.asList(runningStaffList.get(0), runningStaffList.get(2), runningStaffList.get(3));
        // List<Employee> serversExample = Arrays.asList(servingStaffList.get(0), servingStaffList.get(2), servingStaffList.get(3));
        // Shift shiftExample = new Shift("7/4/2022", 1850.0, "Afternoon", bartendersExample, runnersExample, serversExample);


        
        f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
        f.setSize(600, 800);
        f.setResizable(false);
        f.setLayout(new BorderLayout());
        
        barStaff = new JPanel(new GridLayout(barStaffList.toArray().length + 1, 1));
        runningStaff = new JPanel(new GridLayout(runningStaffList.toArray().length + 1, 1));
        servingStaff = new JPanel(new GridLayout(servingStaffList.toArray().length + 1, 1));

        container.add(textEntry);
        body.add(shiftType);
        body.add(barStaff);
        body.add(runningStaff);
        body.add(servingStaff);
        container.add(body);
        f.add(savePanel, BorderLayout.SOUTH);
        f.add(title, BorderLayout.NORTH);
        f.add(container, BorderLayout.CENTER);
        
        //title panel
        JLabel titleLabel = new JLabel("Shift Data Entry");
        title.add(titleLabel);
        
        //text entry
        JLabel dateLabel = new JLabel("Date");
        textEntry.add(dateLabel);
        textEntry.add(date);
        
        JLabel tipsLabel = new JLabel("Tips");
        textEntry.add(tipsLabel);
        textEntry.add(tips);
        
        //body: shift info selectors container
        //shiftType
        JLabel shiftTypeLabel = new JLabel("Shift Type");
        shiftType.add(shiftTypeLabel);

        
        for (int i = 0; i < shiftTypes.length; i++) {
            shiftButtons[i] = new JRadioButton(shiftTypes[i]);
            shiftType.add(shiftButtons[i]);
            shifts.add(shiftButtons[i]);
        }

        //barstaff
        JLabel barStaffLabel = new JLabel("Bar Staff");
        barStaff.add(barStaffLabel);
        for (int i = 0; i < barStaffList.toArray().length; i++) {
            barButtons.add(new JRadioButton(barStaffList.get(i).getName()));
            barStaff.add(barButtons.get(i));
        }

        //runningstaff
        JLabel runningStaffLabel = new JLabel("Running Staff");
        runningStaff.add(runningStaffLabel);
        for (int i = 0; i < runningStaffList.toArray().length; i++) {
            runningButtons.add(new JRadioButton(runningStaffList.get(i).getName()));
            runningStaff.add(runningButtons.get(i));
        }

        //servingstaff
        JLabel servingStaffLabel = new JLabel("Serving Staff");
        servingStaff.add(servingStaffLabel);
        for (int i = 0; i < servingStaffList.toArray().length; i++) {
            servingButtons.add(new JRadioButton(servingStaffList.get(i).getName()));
            servingStaff.add(servingButtons.get(i));
        }

        //savePanel
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                save();
                f.dispose();
            }
        });
        savePanel.add(saveButton);



        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                f.setVisible(true);
            }
        });

        
    }

    public static void save() {
        List<Employee> barShift = new ArrayList();
        List<Employee> runningShift = new ArrayList();
        List<Employee> servingShift = new ArrayList();

        String shiftTypeString = "";
        
        System.out.println("date: " + date.getText());
        System.out.println("total tips: " + tips.getText());
        System.out.println("\nShift Type:");
        for (Enumeration<AbstractButton> buttons = shifts.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                System.out.println(button.getText());
                shiftTypeString = button.getText();
            }
        }
        System.out.println("\nBartenders:");
        for (JRadioButton button : barButtons) {
            if (button.isSelected()) {
                System.out.println(button.getText());
                barShift.add(findEmployee(button.getText()));
            }
        }
        System.out.println("\nRunners:");
        for (JRadioButton button : runningButtons) {
            if (button.isSelected()) {
                System.out.println(button.getText());
                runningShift.add(findEmployee(button.getText()));
            }
        }
        System.out.println("\nServers:");
        for (JRadioButton button : servingButtons) {
            if (button.isSelected()) {
                System.out.println(button.getText());
                servingShift.add(findEmployee(button.getText()));
            }
        }

        String dateStr = date.getText();
        double totalTips = Double.parseDouble(tips.getText());

        try {
            updateTipPool(totalTips, barShift, runningShift, servingShift);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        Shift newShift = new Shift(dateStr, totalTips, shiftTypeString, barShift, runningShift, servingShift);
       
        try {
            saveShift(newShift);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Employee findEmployee(String Name) {
        for (Employee comp : barStaffList) {
            if (comp.getName().equals(Name)) {
                return comp;
            }
        }
        for (Employee comp : servingStaffList) {
            if (comp.getName().equals(Name)) {
                return comp;
            }
        }
        for (Employee comp : runningStaffList) {
            if (comp.getName().equals(Name)) {
                return comp;
            }
        }
        return null;
    }

    public static void saveShift(Shift newShift) throws IOException{
        ValueRange appendBody = new ValueRange()
            .setValues(Arrays.asList(
                    Arrays.asList(newShift.getDate(), newShift.getType(), newShift.getBartenders(), newShift.getRunners(), newShift.getServers(), newShift.getTips())
            ));

        AppendValuesResponse appendResult = sheetsService.spreadsheets().values()
            .append(SPREADSHEET_ID, "Shifts", appendBody)
            .setValueInputOption("USER_ENTERED")
            .setInsertDataOption("INSERT_ROWS")
            .setIncludeValuesInResponse(true)
            .execute();
        
    }

    public static void updateTipPool(double totalTips, List<Employee> barShift, List<Employee> runningShift, List<Employee> servingShift) throws IOException{
        double barEarnings = (Math.floor(((totalTips * bartenderCut) / barShift.toArray().length) * 100))/100;
        double runnerEarnings = (Math.floor(((totalTips * runnerCut) / runningShift.toArray().length) * 100))/100;
        double serverEarnings = (Math.floor(((totalTips * serverCut) / servingShift.toArray().length) * 100))/100;

        for(Employee bartender : barShift) {
            double newOwed = bartender.getOwed() + barEarnings;
            double newEarnedTotal = bartender.getEarnedTotal() + barEarnings;
            ValueRange owedUpdate = new ValueRange()
                .setValues(Arrays.asList(
                    Arrays.asList(newOwed)
                ));

            UpdateValuesResponse owedResult = sheetsService.spreadsheets().values()
                .update(SPREADSHEET_ID, "Employees!E" + bartender.getId(), owedUpdate)
                .setValueInputOption("Raw")
                .execute();

            ValueRange earnedLSUpdate = new ValueRange()
                .setValues(Arrays.asList(
                    Arrays.asList(barEarnings)
                ));

            UpdateValuesResponse earnedLSResult = sheetsService.spreadsheets().values()
                .update(SPREADSHEET_ID, "Employees!F" + bartender.getId(), earnedLSUpdate)
                .setValueInputOption("Raw")
                .execute();

            ValueRange earnedTotalUpdate = new ValueRange()
                .setValues(Arrays.asList(
                    Arrays.asList(newEarnedTotal)
                ));

            UpdateValuesResponse earnedTotalResult = sheetsService.spreadsheets().values()
                .update(SPREADSHEET_ID, "Employees!G" + bartender.getId(), earnedTotalUpdate)
                .setValueInputOption("Raw")
                .execute();
        }
        for(Employee runner : runningShift) {
            double newOwed = runner.getOwed() + runnerEarnings;
            double newEarnedTotal = runner.getEarnedTotal() + runnerEarnings;
            ValueRange owedUpdate = new ValueRange()
                .setValues(Arrays.asList(
                    Arrays.asList(newOwed)
                ));

            UpdateValuesResponse owedResult = sheetsService.spreadsheets().values()
                .update(SPREADSHEET_ID, "Employees!E" + runner.getId(), owedUpdate)
                .setValueInputOption("Raw")
                .execute();

            ValueRange earnedLSUpdate = new ValueRange()
                .setValues(Arrays.asList(
                    Arrays.asList(runnerEarnings)
                ));

            UpdateValuesResponse earnedLSResult = sheetsService.spreadsheets().values()
                .update(SPREADSHEET_ID, "Employees!F" + runner.getId(), earnedLSUpdate)
                .setValueInputOption("Raw")
                .execute();

            ValueRange earnedTotalUpdate = new ValueRange()
                .setValues(Arrays.asList(
                    Arrays.asList(newEarnedTotal)
                ));

            UpdateValuesResponse earnedTotalResult = sheetsService.spreadsheets().values()
                .update(SPREADSHEET_ID, "Employees!G" + runner.getId(), earnedTotalUpdate)
                .setValueInputOption("Raw")
                .execute();
        }
        for(Employee server : servingShift) {
            double newOwed = server.getOwed() + serverEarnings;
            double newEarnedTotal = server.getEarnedTotal() + serverEarnings;
            ValueRange owedUpdate = new ValueRange()
                .setValues(Arrays.asList(
                    Arrays.asList(newOwed)
                ));

            UpdateValuesResponse owedResult = sheetsService.spreadsheets().values()
                .update(SPREADSHEET_ID, "Employees!E" + server.getId(), owedUpdate)
                .setValueInputOption("Raw")
                .execute();

            ValueRange earnedLSUpdate = new ValueRange()
                .setValues(Arrays.asList(
                    Arrays.asList(serverEarnings)
                ));

            UpdateValuesResponse earnedLSResult = sheetsService.spreadsheets().values()
                .update(SPREADSHEET_ID, "Employees!F" + server.getId(), earnedLSUpdate)
                .setValueInputOption("Raw")
                .execute();

            ValueRange earnedTotalUpdate = new ValueRange()
                .setValues(Arrays.asList(
                    Arrays.asList(newEarnedTotal)
                ));

            UpdateValuesResponse earnedTotalResult = sheetsService.spreadsheets().values()
                .update(SPREADSHEET_ID, "Employees!G" + server.getId(), earnedTotalUpdate)
                .setValueInputOption("Raw")
                .execute();
        }
    }
    

    public Object getGreeting() {
        return null;
    }
}


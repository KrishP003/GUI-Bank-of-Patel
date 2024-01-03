package com.example.bank_of_patel;

import com.example.bank_of_patel.bank.AccountDatabase;
import com.example.bank_of_patel.bank.accounts.Account;
import com.example.bank_of_patel.bank.accounts.AccountTypeCommand;
import com.example.bank_of_patel.bank.accounts.Checking;
import com.example.bank_of_patel.bank.accounts.CollegeChecking;
import com.example.bank_of_patel.bank.accounts.MoneyMarket;
import com.example.bank_of_patel.bank.accounts.Savings;
import com.example.bank_of_patel.bank.personaldata.Campus;
import com.example.bank_of_patel.bank.personaldata.Date;
import com.example.bank_of_patel.bank.personaldata.Profile;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TextArea;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;


import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * This is the Controller for the GUI.
 * @author Dharmik Patel and Krish Patel
 *
 */
public class BankOfPatelController {

    @FXML
    private TextField firstNameInOpenClose, lastNameInOpenClose, initialAmount;
    @FXML
    private CheckBox loyaltyCheckBox;
    @FXML
    ToggleGroup accountTypeGroup, campusLocation;
    @FXML
    private TextField firstNameInDepWith, lastNameInDepWith, changeAmount;
    @FXML
    private DatePicker dobInOpenClose, dobInDepWith;
    @FXML
    private TextArea console;
    @FXML
    VBox loyaltyVBox;
    @FXML
    HBox campusLocationHBox;

    private static final int CLOSING = 0;
    private static final int OPENING = 1;
    private static final int DEPOSITING = 2;
    private static final int WITHDRAWING = 3;
    private AccountDatabase accountDatabase = new AccountDatabase();
    private int currentTask;
    private static final int maxAmountOfCommands = 6;
    private static final int INDEX_OF_ACCOUNT_TYPE = 0;
    private static final int INDEX_OF_FNAME = 1;
    private static final int INDEX_OF_LNAME = 2;
    private static final int INDEX_OF_DOB = 3;
    private static final int INDEX_OF_BALANCE = 4;
    private static final int INDEX_OF_CAMPUS = 5;
    private static final int INDEX_OF_LOYALTY = 5;

    /**
     * This action method runs when ever the console area is
     * updated with console.appendText(). This scrolls all the way down
     * to the bottom for the user, so the user does not have to.
     */
    @FXML
    protected void onConsoleTextUpdated(){
        console.setScrollTop(Double.MAX_VALUE);
    }

    /**
     * This action method runs when user clicks the savings radio button in
     * the open and close tab.
     * It disables the campus radio group, and enables the loyalty checkbox
     */
    @FXML
    protected void onMouseClickSavings(){
        disableCampusLocation();
        enableLoyalty();
    }
    /**
     * This action method runs when user clicks the college checking radio
     * button in the open and close tab.
     * It enables the campus radio group, and disables the loyalty checkbox
     */
    @FXML
    protected void onMouseClickCollegeChecking(){
        enableCampusLocation();
        disableLoyalty();
    }
    /**
     * This action method runs when user clicks the checking or MM radio
     * buttons in the open and close tab.
     * It disables the campus radio group, and disables the loyalty checkbox
     */
    @FXML
    protected void onMouseClickCheckingOrMM(){
        disableCampusLocation();
        disableLoyalty();
    }

    /**
     * This helper method disables the campus location radio group
     */
    private void disableCampusLocation(){
        campusLocation.selectToggle(null);
        campusLocationHBox.setDisable(true);
    }
    /**
     * This helper method disables the loyalty checkbox
     */
    private void disableLoyalty(){
        loyaltyCheckBox.setSelected(false);
        loyaltyVBox.setDisable(true);
    }
    /**
     * This helper method enables the campus location radio group
     */
    private void enableCampusLocation(){
        campusLocationHBox.setDisable(false);
    }
    /**
     * This helper method enables the loyalty checkbox
     */
    private void enableLoyalty(){
        loyaltyVBox.setDisable(false);
    }

    /**
     * This action method runs when open button is clicked.
     * It calls the proper helper methods to make add an account.
     */
    @FXML
    protected void onOpen() {
        currentTask = OPENING;
        newEventOnConsole();
        try {
            validateInputInOpeningAndClosing();
            String[] commands = buildStringOfCmds();
            open(commands);
        }catch (NullPointerException e){
            return;
        }

    }
    /**
     * This action method runs when close button is clicked.
     * It calls the proper helper methods to make close an account.
     */
    @FXML
    protected void onClose() {
        currentTask = CLOSING;
        newEventOnConsole();
        try {
            validateInputInOpeningAndClosing();
            String[] commands = buildStringOfCmds();
            close(commands);
        }catch (NullPointerException e){
            return;
        }
    }
    /**
     * This action method runs when clear button is clicked.
     * It clears all the input fields, buttons, date pickers, etc. on
     * all the tabs
     */
    @FXML
    protected void onClear() {
        firstNameInOpenClose.setText("");
        lastNameInOpenClose.setText("");
        firstNameInDepWith.setText("");
        lastNameInDepWith.setText("");
        campusLocation.selectToggle(null);
        accountTypeGroup.selectToggle(null);
        loyaltyCheckBox.setSelected(false);
        initialAmount.setText("");
        changeAmount.setText("");
        dobInOpenClose.setValue(null);
        dobInDepWith.setValue(null);
    }
    /**
     * This action method runs when deposit button is clicked.
     * It calls the proper helper methods to deposit money.
     */
    @FXML
    protected void onDeposit() {
        currentTask = DEPOSITING;
        newEventOnConsole();
        try {
            validateInputInOpeningAndClosing();
            String[] commands = buildStringOfCmds();
            deposit(commands);
        }catch (NullPointerException e){
            return;
        }
    }
    /**
     * This action method runs when withdraw button is clicked.
     * It calls the proper helper methods to withdraw money.
     */
    @FXML
    protected void onWithdraw() {
        currentTask = WITHDRAWING;
        newEventOnConsole();
        try {
            validateInputInOpeningAndClosing();
            String[] commands = buildStringOfCmds();
            withdraw(commands);
        }catch (NullPointerException e){
            return;
        }
    }
    /**
     * This action method runs when print database button is clicked
     * This prints the accounts in sorted order
     */
    @FXML
    protected void onPrintAll() {
        newEventOnConsole();
        accountDatabase.printSorted(console);
    }
    /**
     * This action method runs when print with interests and fees database
     * button is clicked This prints the accounts in sorted order, with
     * interests and fees
     */
    @FXML
    protected void onPrintAllWithIntAndFee() {
        newEventOnConsole();
        accountDatabase.printFeesAndInterests(console);
    }
    /**
     * This action method runs when apply interests and fees database
     * button is clicked
     * This prints the accounts in sorted order, after interests and fees
     * have been applied
     */
    @FXML
    protected void onApplyIntAndFees() {
        newEventOnConsole();
        accountDatabase.printUpdatedBalances(console);
    }
    /**
     * This action method runs when the upload from file button is clicked
     * It calls the proper helper methods to import accounts from a txt file
     */
    @FXML
    protected void onUpload() {
        newEventOnConsole();
        upload();
    }

    /**
     * This method makes new accounts from a txt file. It launches a file
     * chooser dialogue
     * and when the user picks a file, it opens accounts for each new line.
     */
    private void upload(){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Database File");
        fileChooser.getExtensionFilters().add(
                new ExtensionFilter("Text Files", "*.txt"));


        File selectedFile = fileChooser.showOpenDialog(new Stage());

        Scanner scanner;
        try {
            scanner = new Scanner(selectedFile);
            String currentFullLine;
            while (scanner.hasNextLine()){
                currentFullLine = scanner.nextLine();
                if(currentFullLine.isBlank()) continue;
                currentTask = OPENING;
                open(currentFullLine.split(","));
                //console.appendText(currentFullLine + "\n");
            }
            scanner.close();
        } catch (NullPointerException e) {
            console.appendText("File not picked");
        } catch (FileNotFoundException e){
            console.appendText("Pick a valid file");
        }



    }

    /**
     * This helper method is called before all button preses/actions in the
     * GUI.
     * It adds a new line to the console to differentiate actions.
     */
    private void newEventOnConsole(){
        console.appendText("\n");
    }

    /**
     * This helper method takes the input data, and builds a String[].
     * So the String[] can be passed into the open(), close(), deposit(),
     * withdraw()
     */
    private String[] buildStringOfCmds(){
        TextField firstName, lastName, amount;
        DatePicker dob;
        if(currentTask == OPENING || currentTask == CLOSING){
            firstName = firstNameInOpenClose;
            lastName = lastNameInOpenClose;
            dob = dobInOpenClose;
            amount = initialAmount;
        }else {
            firstName = firstNameInDepWith;
            lastName = lastNameInDepWith;
            dob = dobInDepWith;
            amount = changeAmount;
        }
        String[] commands = new String[maxAmountOfCommands];
        commands[INDEX_OF_ACCOUNT_TYPE] = ((RadioButton)accountTypeGroup.
                getSelectedToggle()).getAccessibleText();
        commands[INDEX_OF_FNAME] = firstName.getText();
        commands[INDEX_OF_LNAME] = lastName.getText();
        commands[INDEX_OF_DOB] = String.format("%s/%s/%s", dob.getValue().
                        getMonthValue(), dob.getValue().getDayOfMonth()
                , dob.getValue().getYear());
        if(currentTask == CLOSING) return commands;
        commands[INDEX_OF_BALANCE] = amount.getText();
        if(currentTask != OPENING) return commands;
        if(((RadioButton)accountTypeGroup.getSelectedToggle()).
                getAccessibleText().equals(AccountTypeCommand.CC.name())){
            RadioButton selectedCampus = (RadioButton)campusLocation.
                    getSelectedToggle();
            if(selectedCampus == null) {
                console.appendText("Please select an campus\n");
                return null;
            }
            commands[INDEX_OF_CAMPUS] = selectedCampus.getAccessibleText();
        } else if (((RadioButton)accountTypeGroup.getSelectedToggle()).
                getAccessibleText().equals(AccountTypeCommand.S.name())) {
            String isLoyal = loyaltyCheckBox.isSelected() ? "1" : "0";
            commands[INDEX_OF_LOYALTY] = isLoyal;
        }
        return commands;
    }

    /**
     * This helper method is called before parsing the user's data.
     * It makes sure all fields are filled in. It throws an exception,
     * when it finds error. The caller method catches the exception.
     */
    private void validateInputInOpeningAndClosing()
            throws NullPointerException{
        TextField firstName, lastName, amount;
        DatePicker dob;
        if(currentTask == OPENING || currentTask == CLOSING){
            firstName = firstNameInOpenClose;
            lastName = lastNameInOpenClose;
            dob = dobInOpenClose;
            amount = initialAmount;
        }else {
            firstName = firstNameInDepWith;
            lastName = lastNameInDepWith;
            dob = dobInDepWith;
            amount = changeAmount;
        }
        RadioButton selectedAccountType = (RadioButton)
                accountTypeGroup.getSelectedToggle();
        if(selectedAccountType == null) {
            console.appendText("Select an accountType\n");
            throw new NullPointerException();
        }
        if(firstName.getText().isBlank()){
            console.appendText("Please enter a first name\n");
            throw new NullPointerException();
        }
        if(lastName.getText().isBlank()){
            console.appendText("Please enter a last name\n");
            throw new NullPointerException();
        }
        LocalDate selectedDate = dob.getValue();
        if(selectedDate == null){
            console.appendText("Please enter a date of birth\n");
            throw new NullPointerException();
        }
        if(currentTask == CLOSING) return;
        if(amount.getText().isBlank()){
            console.appendText("Please enter an amount\n");
            throw new NullPointerException();
        }
    }
    /**
     * Opens an account from data from command line.
     * Checks for restrictions and errors.
     * Only the format bellow is allowed
     * Spacing does not matter, and the character case
     * or the data tokens do not matter.
     * O  C John Doe 2/19/2000 599.99
     * O  CC Jane Doe 10/1/2000 999.99 0
     * O  S april March 1/15/1987 1500 1
     * O  MM Roy Brooks 10/31/1979 2909.10
     * Handles:
     *      InputMismatchException,
     *      NumberFormatException,
     *      NoSuchElementException,
     *      invalid dob,
     *      age restrictions,
     *      type of account restrictions,
     *      invalid campus codes,
     *      invalid amounts.
     * @param commands Data to make an account from.
     */
    private void open(String[] commands) {
        AccountTypeCommand accountType = getAndCheckAccountType(commands);
        if (accountType == null) return;

        Account accountToAdd = makeAccount(commands, accountType);
        if (accountToAdd == null) return;

        if (accountTypeRestrictionCheck(accountType, accountToAdd)) return;
        if(!accountDatabase.open(accountToAdd)) {
            console.appendText(String.format("%s(%s) is already in the " +
                            "database.\n",
                    accountToAdd.getHolder(), accountType.name()));
            return;
        }
        console.appendText(String.format("%s(%s) opened.\n",
                accountToAdd.getHolder(),
                accountType.name()));
    }

    /**
     * Closes an account from bank, if it exists.
     * Only works with the format bellow.
     * Spacing does not matter, and the character case
     * or the data tokens do not matter.
     * C ACCOUNT_TYPE FNAME LNAME DOB
     * Handles errors as well.
     * @param commands Data to identify what account to close
     */
    private void close(String[] commands){
        AccountTypeCommand accountType = getAndCheckAccountType(commands);
        if (accountType == null) return;

        Account accountToClose = makeAccount(commands, accountType);
        if (accountToClose == null) return;

        if(!accountDatabase.close(accountToClose)){
            console.appendText(
                    String.format("%s(%s) is not in the database.\n",
                    accountToClose.getHolder(), accountType.name()));
            return;
        }
        console.appendText(String.format("%s(%s) has been closed.\n",
                accountToClose.getHolder(),
                accountType.name()));
    }

    /**
     * This method deposits money into the given account.
     * @param commands The data formatted in a string[]
     */
    private void deposit(String[] commands){
        AccountTypeCommand accountType = getAndCheckAccountType(commands);
        if (accountType == null) return;

        Account accountWithAmountOfMoneyToDeposit =
                makeAccount(commands, accountType);
        if (accountWithAmountOfMoneyToDeposit == null) return;

        boolean isAccountOpenBank = accountDatabase.contains(
                accountWithAmountOfMoneyToDeposit);
        if(isAccountOpenBank){
            accountDatabase.deposit(accountWithAmountOfMoneyToDeposit);
            console.appendText(
                    String.format("%s(%s) Deposit - balance updated.\n",
                    accountWithAmountOfMoneyToDeposit.getHolder(),
                    accountType.name()));
        }
        else {
            console.appendText(
                    String.format("%s(%s) is not in the database.\n",
                    accountWithAmountOfMoneyToDeposit.getHolder(),
                    accountType.name()));
        }
    }

    /**
     * This method withdraws money from the account.
     * @param commands The data formatted in a string[]
     */
    private void withdraw(String[] commands){
        AccountTypeCommand accountType = getAndCheckAccountType(commands);
        if (accountType == null) return;

        Account accountWithAmountOfMoneyToWithdraw =
                makeAccount(commands, accountType);
        if (accountWithAmountOfMoneyToWithdraw == null) return;
        boolean isAccountOpenBank = accountDatabase.contains(
                accountWithAmountOfMoneyToWithdraw);
        if(isAccountOpenBank)
            if(accountDatabase.withdraw(accountWithAmountOfMoneyToWithdraw))
                console.appendText(
                        String.format("%s(%s) Withdraw - balance updated.\n",
                        accountWithAmountOfMoneyToWithdraw.getHolder(),
                        accountType.name()));
            else
                console.appendText(String.format("%s(%s) Withdraw - " +
                                "insufficient fund.\n",
                        accountWithAmountOfMoneyToWithdraw.getHolder(),
                        accountType.name()));
        else
            console.appendText(
                    String.format("%s(%s) is not in the database.\n",
                    accountWithAmountOfMoneyToWithdraw.getHolder(),
                    accountType.name()));
    }

    /**
     * The method makes sure a person that a person can not hold a
     * checking and college checking account at the same time.
     * @param accountType C or CC
     * @param accountToAdd The account to check on
     * @return True if good, false if not
     */
    private boolean accountTypeRestrictionCheck(
            AccountTypeCommand accountType, Account accountToAdd) {
        if(accountType.equals(AccountTypeCommand.CC)
                && accountDatabase.contains(
                accountToAdd.getHolder(),
                AccountTypeCommand.C)){
            console.appendText(
                    String.format("%s(%s) is already in the database.\n",
                    accountToAdd.getHolder(), AccountTypeCommand.CC.name()));
            return true;
        }
        if(accountType.equals(AccountTypeCommand.C)
                && accountDatabase.contains(
                accountToAdd.getHolder(),
                AccountTypeCommand.CC)){
            console.appendText(
                    String.format("%s(%s) is already in the database.\n",
                    accountToAdd.getHolder(), AccountTypeCommand.C.name()));
            return true;
        }
        return false;
    }

    /**
     * THis method will make a valid account
     * @param commands The data formatted in a string[]
     * @param accountType The type of account
     * @return returns a valid account, null if not valid
     */
    private Account makeAccount(
            String[] commands, AccountTypeCommand accountType) {
        Account account = null;
        switch (accountType){
            case C -> account = makeCheckingAccount(commands);
            case CC -> account = makeCollegeCheckingAccount(commands);
            case S -> account = makeSavingsAccount(commands);
            case MM -> account = makeMoneyMarketAccount(commands);
        }
        return account;
    }

    /**
     * This method makes a checking account.
     * @param commands The data formatted in a string[]
     * @return A valid checking account or null
     */
    private Checking makeCheckingAccount(String[] commands){
        Profile profile = makeAndCheckProfile(commands);
        if(profile == null) return null;

        if(currentTask == CLOSING){
            return new Checking(profile);
        }
        else{
            double balanceAmount = getAndCheckAmount(commands);
            if(balanceAmount == -1) return null;
            return new Checking(profile, balanceAmount);
        }
    }

    /**
     * This method makes a college checking account
     * @param commands The data formatted in a string[]
     * @return a valid college checking account or null
     */
    private CollegeChecking makeCollegeCheckingAccount(String[] commands){
        Profile profile = makeAndCheckProfile(commands);
        if(profile == null) return null;
        if(currentTask == CLOSING){
            return new CollegeChecking(profile);
        } else {
            double balanceAmount = getAndCheckAmount(commands);
            if (balanceAmount == -1) return null;
            if(currentTask == OPENING){
                if(profile.getAge() >= MoneyMarket.MAX_AGE){
                    console.appendText(
                            String.format("DOB invalid: %s over 24.\n",
                            profile.getDOB()));
                    return null;
                }
                Campus campus = getAndCheckCampus(commands);
                if (campus == null) return null;
                return new CollegeChecking(profile, balanceAmount, campus);
            }
            return new CollegeChecking(profile, balanceAmount);
        }
    }

    /**
     * This method makes a valid savings account
     * @param commands The data formatted in a string[]
     * @return a savings account or null
     */
    private Savings makeSavingsAccount(String[] commands){
        Profile profile = makeAndCheckProfile(commands);
        if(profile == null) return null;

        if(currentTask == CLOSING){
            return new Savings(profile);
        } else {
            double balanceAmount = getAndCheckAmount(commands);
            if (balanceAmount == -1) return null;
            if(currentTask == OPENING){
                int isLoyal = getAndCheckLoyalty(commands);
                if (isLoyal == -1) return null;
                return new Savings(
                        profile, balanceAmount, isLoyal == 1);
            }
            return new Savings(profile, balanceAmount);
        }
    }

    /**
     * This method makes a valid money market account
     * @param commands The data formatted in a string[]
     * @return a money market account or null
     */
    private MoneyMarket makeMoneyMarketAccount(String[] commands){
        Profile profile = makeAndCheckProfile(commands);
        if(profile == null) return null;

        if(currentTask == CLOSING){
            return new MoneyMarket(profile);
        }else {
            double balanceAmount = getAndCheckAmount(commands);
            if (balanceAmount == -1) return null;
            if(currentTask == OPENING){
                if (balanceAmount < MoneyMarket.ACCOUNT_THRESHOLD) {
                    console.appendText(
                            String.format("Minimum of $%.0f to open " +
                                            "a Money Market account.\n",
                            (MoneyMarket.ACCOUNT_THRESHOLD)));
                    return null;
                }
            }
            return new MoneyMarket(profile, balanceAmount);
        }
    }

    /**
     * This method makes a valid Profile
     * @param commands The data formatted in a string[]
     * @return a valid profile or null
     */
    private Profile makeAndCheckProfile(String[] commands){
        String fname, lname, dob;
        try{
            fname = commands[INDEX_OF_FNAME];
            lname = commands[INDEX_OF_LNAME];
            dob = commands[INDEX_OF_DOB];
        }catch (ArrayIndexOutOfBoundsException err){
            missingDataOutput(err);
            return null;
        }
        Date date = getAndCheckDOB(dob);
        if(date == null) return null;

        Profile profileToReturn = new Profile(fname, lname, date);
        if(profileToReturn.getAge() < Account.MIN_AGE){
            console.appendText(String.format("DOB invalid: %s under 16.\n",
                    profileToReturn.getDOB()));
            return null;
        }
        return profileToReturn;
    }

    /**
     * Utility method to make and validate the DOB.
     * @param date date start time in format "MONTH/DAY/YEAR"
     * @return returns a valid Date. Null if not valid
     */
    private Date getAndCheckDOB(String date) {
        Date dob = new Date(date);
        if(!(dob.isValid())){
            console.appendText(String.format("DOB invalid: %s not " +
                            "a valid calendar date!\n", date));
            return null;
        } else if (!(dob.isLessThanPresentDate())) {
            console.appendText(String.format("DOB invalid: %s " +
                    "cannot be today or a future day.\n", date));
            return null;
        }
        return dob;
    }

    /**
     * Method to make account type
     * @param commands The data formatted in a string[]
     * @return A valid AccountType or null
     */
    private AccountTypeCommand getAndCheckAccountType(String[] commands) {
        AccountTypeCommand accountType;
        try {
            accountType = AccountTypeCommand.valueOf(
                    commands[INDEX_OF_ACCOUNT_TYPE]);
        }catch (IndexOutOfBoundsException | IllegalArgumentException err) {
            missingDataOutput(err);
            return null;
        }
        return accountType;
    }

    /**
     * Method to make Double amount from string
     * @param commands The data formatted in a string[]
     * @return A valid amount or null
     */
    private double getAndCheckAmount(String[] commands){
        double balanceAmount;
        try {
            balanceAmount = Double.parseDouble(
                    commands[INDEX_OF_BALANCE]);
        } catch (ArrayIndexOutOfBoundsException err){
            missingDataOutput(err);
            return -1;
        } catch (NumberFormatException err){
            console.appendText("Not a valid amount.");
            return -1;
        }
        if(balanceAmount <= 0) {
            if(currentTask == OPENING)
                console.appendText(
                        "Initial deposit cannot be 0 or negative.");
            else if(currentTask == DEPOSITING)
                console.appendText(
                        "Deposit - amount cannot be 0 or negative.");
            else if(currentTask == WITHDRAWING)
                console.appendText(
                        "Withdraw - amount cannot be 0 or negative.");
            return -1;
        }
        return balanceAmount;
    }

    /**
     * Method to make a valid Campus
     * @param commands The data formatted in a string[]
     * @return A valid Campus or null
     */
    private Campus getAndCheckCampus(String[] commands){
        Campus campus;
        try{
            int campusIndex = Integer.parseInt(
                    commands[INDEX_OF_CAMPUS]);
            if(campusIndex >= Campus.values().length){
                console.appendText("Invalid campus code.");
                return null;
            }
            campus = Campus.values()[campusIndex];
        } catch (IndexOutOfBoundsException | NumberFormatException err){
            missingDataOutput(err);
            return null;
        }
        return campus;
    }

    /**
     * Method to parse the CLI to get a valid loyalty status
     * @param commands The data formatted in a string[]
     * @return A valid amloyalty status(0 or 1) ount or -1
     */
    private int getAndCheckLoyalty(String[] commands){
        int isLoyal = -1;
        try{
            isLoyal = Integer.parseInt(
                    commands[INDEX_OF_LOYALTY]);
        } catch (IndexOutOfBoundsException | NumberFormatException err){
            missingDataOutput(err);
        }
        if(!(isLoyal == 0 || isLoyal == 1)){
            missingDataOutput(new Exception("IS LOYAL WRONG"));
            return -1;
        }
        return isLoyal;
    }

    /**
     * A utility method to just print an error.
     */
    private void missingDataOutput(Exception err){
        console.appendText(err.toString());
        if(currentTask == OPENING)
            console.appendText("Missing data for opening an account.");
        else if(currentTask == CLOSING)
            console.appendText("Missing data for closing an account.");
        else
            console.appendText("Missing data for making an account.");
    }

}
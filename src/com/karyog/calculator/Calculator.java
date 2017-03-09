package com.karyog.calculator;

import android.util.Log;

// This is the main class of logic for the calculator. It takes input from MainActivity class, performs
// the calculation and returns the correct answer to be displayed to the user.
public class Calculator {
	double num1;
	String operator;
	double memory;
	boolean shouldClearText;
	double result;

	String currentDisplayText;
	String currentDisplayMiniText;

	String finalDisplayText;
	String finalDisplayMiniText;
	
	// Functions created to provide the current Display and MiniDisplay(shows equation to be calculated)
	// strings to an external class.
	// This is used by the MainActivity to update what is seen by the user in the Display and MiniDisplay
	public String getFinalDisplayText() {
		return finalDisplayText;
	}

	public String getFinalDisplayMiniText() {
		return finalDisplayMiniText;
	}
	// Constructor used to create a new instance of the class and define variables of this instance
	public Calculator() {
		num1 = 0.0;
		operator = "";
		memory = 0.0;
		shouldClearText = false;
//		Log.d("Constructor", Boolean.valueOf(shouldClearText).toString());
		result = 0.0;

		currentDisplayText = Double.valueOf(num1).toString();
		currentDisplayMiniText = Double.valueOf(num1).toString();

		finalDisplayText = Double.valueOf(num1).toString();
		finalDisplayMiniText = Double.valueOf(num1).toString();
	}
	// Takes in button values from MainActivity and determines what the character is by matching them to
	// cases. When there is a match a certain function is executed depending on what the character is.
	// If the character is not recognized it is considered misc and runs the miscOperatorEntered function.
	public void calculate(String buttonText) {
		currentDisplayMiniText = finalDisplayMiniText;
		currentDisplayText = finalDisplayText;

		switch (buttonText) {
		case "0":
		case "1":
		case "2":
		case "3":
		case "4":
		case "5":
		case "6":
		case "7":
		case "8":
		case "9":
			numberEntered(buttonText);
			break;
		case ".":
			periodEntered(buttonText);
			break;
		case "*":
		case "/":
		case "-":
		case "+":
			operatorEntered(buttonText);
			break;
		case "=":
			calculateResult();
			break;
		case "1/x":
			calculateReciprocal();
			break;
		case "MC":
		case "MR":
		case "MS":
		case "MPlus":
		case "MMinus":
			performMemoryOperation(buttonText);
			break;

		case "B":
			backspaceEntered();
			break;
		case "CE":
		case "C":
			clearScreen(buttonText);
			break;
		case "+/-":
			toggleSign();
			break;
		default:
			miscOperatorEntered(buttonText);
			break;
		}
		standardizeDisplay();
	}
	// standardizes the displayed text so that innaccurate values cannot be entered. For ex. 3.333.3
	private void standardizeDisplay() {
		switch(finalDisplayText) {
		case "0.0":
		case "-0.0":
			finalDisplayText="0";
			break;
		default:
			double displayNumber = Double.parseDouble(finalDisplayText);
			if ((displayNumber == Math.floor(displayNumber)) && !Double.isInfinite(displayNumber)) {
			    finalDisplayText = String.valueOf((int) Math.floor(displayNumber));
			}
		}
	}
	// When a period is entered the value is added to the finalDisplayText, where the display is updated
	// with the addition of the period (when MainActivity calls FinalDisplayText() from this class).
	private void periodEntered(String period) {
		String existingDisplay = currentDisplayText;
		Log.d("periodEntered", Boolean.valueOf(shouldClearText).toString());

		if (shouldClearText == true) {
			existingDisplay = "0";
			shouldClearText = false;
		}

		finalDisplayText = existingDisplay + period;
		System.out.println(". text = " + finalDisplayText);
			
	}
	// When a number is entered the value is added to the finalDisplayText, where the display is updated
	// with the addition of the new number (when MainActivity calls finalDisplayText() from this class).
	private void numberEntered(String number) {
		String existingDisplay = currentDisplayText;
		Log.d("numberEntered", Boolean.valueOf(shouldClearText).toString());

		if (shouldClearText == true) {
			existingDisplay = "0.0";
			Log.d("numberEntered", "existingDisplay is " + existingDisplay + "in shouldClearText == true");
			shouldClearText = false;
		}

		if (existingDisplay.equals("0") || existingDisplay.equals("0.0")) {
			existingDisplay = "";
			Log.d("numberEntered", "existingDisplay is " + existingDisplay + "in existingDisplay.equals==0");
		}


		Log.d("numberEntered", "existingDisplay is " + existingDisplay + "right before finalDisplayText is calculated");
		finalDisplayText = existingDisplay + number;
		System.out.println("1 text = " + finalDisplayText);
	}
	// When the toggle (+/-) button is pressed the finalDisplayText is updated to be the opposite value,
	// negative or positive. This value will be used in further operations.
	private void toggleSign() {
		double number = Double.parseDouble(currentDisplayText);

		number = -number;
		System.out.println("Number is " + number);

		finalDisplayText = Double.valueOf(number).toString();
	}
	// When the cases for clearing occurs, the finalDisplayText is cleared to 0. This means that the
	// text in display is 0, but miniDisplay(the current full equation) is not cleared.
	// In addition the current number value (num1) is reset to 0, as to keep display and calculation
	// relationship consistent.
	private void clearScreen(String clearButtonText) {
		switch (clearButtonText) {
		case "CE":
			finalDisplayText = "0";
			break;

		case "C":
			finalDisplayText = "0";
			num1 = 0;
			operator = "";
			break;
		}
	}
	// When B is pressed the last value in finalDisplayText is removed. This is done by getting the length
	// and taking the substring of finalDisplayText with one less character.
	private void backspaceEntered() {
		Log.d("backspaceEntered", Boolean.valueOf(shouldClearText).toString());
		if (shouldClearText == true) {
			finalDisplayText = "0.0";
			return;
		}

		int length = currentDisplayText.length();

		if (length > 0) {
			String newText = currentDisplayText.substring(0, length - 1);

			if (newText.equals("-")) {
				newText = "0";
			}

			if (newText.equals("")) {
				newText = "0";
			}

			finalDisplayText = newText;
		}

	}
	// When a known operator is entered, a calculation is done on the current value and the next value
	// after the operator. The result of the calculation is saved in the "result" variable and equation
	// is updated in miniDisplay
	private void operatorEntered(String operation) {
		Log.d("operatorEntered", "invoked");
		System.out.println("current_display_text: " + currentDisplayText);
		num1 = Double.parseDouble(currentDisplayText);

		if (!operator.equals("")) {
			performOperation(result, num1, "" + operator);
			finalDisplayMiniText = currentDisplayMiniText + " " + currentDisplayText + " " + operation;
		} else {
			result = num1;
			finalDisplayMiniText = currentDisplayText + " " +  operation;
		}

		operator = operation;

		Log.d("operatorEntered", Boolean.valueOf(shouldClearText).toString());
		shouldClearText = true;
	}
	// When a character is entered that is unknown a log message is printed and the character is ignored
	// by the application
	private void miscOperatorEntered(String operation) {
		Log.d("miscOperatorEntered", operation);
	}
	// When the equals button is pressed the last number value entered is set to num2 and the result 
	//(previous calculations when "=" not pressed) is used as the other value in the calculation.
	// The result is then set to the finalDisplayText (showing on the display) and the MiniDisplayText 
	// is cleared.
	private void calculateResult() {
		double num2 = Double.parseDouble(currentDisplayText);

		performOperation(result, num2, operator);

		finalDisplayText = String.valueOf(result);


		num1 = 0.0;
		operator = "";
		result = 0.0;
		finalDisplayMiniText = String.valueOf(num1);
		shouldClearText = true;
	}
	// Used when the user wants to find the rational value of the current number (1/currentDisplayText). 
	// The result is cleared and the finalDisplayText (showing on display) is set to the rational value calculated. 
	// The MiniDisplayText is reset to 0.
	private void calculateReciprocal() {
		if (currentDisplayText.equalsIgnoreCase("0.0")) {
			finalDisplayText = "To Infinity and beyond!";
			// TODO: Do I need an isCalculatable kind of variable? What if the user does a -1 on this?
		} else {
			double num2 = Double.parseDouble(currentDisplayText);
			num2 = 1 / num2;
			finalDisplayText = String.valueOf(num2);
		}
		num1 = 0.0;
		operator = "";
		result = 0.0;
		finalDisplayMiniText = String.valueOf(num1);
		shouldClearText = true;
	}
	// Whenever a calculation is needed to occur from other methods, performOperation is called. This takes in the
	// 2 numbers and the operation to be used between them.
	//
	// ** Note: This does not currently take into account order of operations when calculating as calculations are
	//	    done right away when this is called.
	private void performOperation(double num1, double num2, String op) {
		switch (op) {
		case "*":
			result = num1 * num2;
			break;
		case "/":
			result = num1 / num2;
			break;
		case "-":
			result = num1 - num2;
			break;
		case "+":
			result = num1 + num2;
			break;
		default:

		}

	}
	// When any Memory operation is used the current value that is displayed (currentDisplayText) is
	// manipulated as follows:
	//
	//		- MC clears the Memory
	//		- MR recalls the current value in memory and puts it the the current display
	//		- MS saves the current value in the display
	//		- M+ adds the current value in display to the value already stored in memory
	//		- M- subtracts the current value in display from the value already stored in memory
	private void performMemoryOperation(String operation) {
		switch (operation) {
		case "MC":
			memory = 0;
			break;
		case "MR":
			currentDisplayText = "" + memory;
			break;
		case "MS":
			memory = Double.parseDouble(currentDisplayText);
			break;
		case "M+":
			memory = memory + Double.parseDouble(currentDisplayText);
			break;
		case "M-":
			memory = memory - Double.parseDouble(currentDisplayText);
			break;
		default:

		}
	}
}

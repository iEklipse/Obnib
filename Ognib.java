// -----------------------------------------------------------------------------------------------------------------------
// Assignment 3
// File name: Ognib.java
// Written by: Tri-Luong Steven Dien (7415281), Hoai An Luu (7187661)
// For Comp 248 Section R/Fall 2014
// Function of the program: The program is about the game of Ognib (Bingo), the user plays against a computer.
//							The program will automatically generate game cards with random numbers on each one.
//							The computer starts first by calling 3 tokens on his card.
//							Then it is the user's turn and he's allowed to call only one number on his card.
//							At each token called, the program will verify if there's a winner.
//							If there is one, the game end with a congratulations message or else, the game continues.
// ----------------------------------------------------------------------------------------------------------------------- 

import java.util.Random;
import java.util.Scanner;

public class Ognib {

	public static void main(String[] args) {

		//Declaration and initialization of the variables for the dimension of the arrays (game card)
		final int ROWS = 5;
		final int COLUMNS = 5;
		final int LENGTH = ROWS*COLUMNS;

		//Declaration and initialization of the constants for limits (number between 1 and 100 will be generated)
		final int MIN_VALUE = 1;
		final int MAX_VALUE = 100;

		//Declaration and initialization of the 2 arrays (computer and player)
		int[] boardPlayer = new int[LENGTH];
		int[] boardComputer = new int[LENGTH];

		//Declaration and initialization of the object from the Random class
		Random rand = new Random();

		//Declaration and initialization of the variables for the random generated numbers
		int randomNumberPlayer = 0;
		int randomNumberComputer = 0;
		boolean isDuplicatePlayer = false;
		boolean isDuplicateComputer = false;

		//Declaration and initialization of the variables for the token of each player
		int randomIndexComputer;
		boolean alreadyTakenNumber;
		int tokenNumberPlayer = 0;

		//Declaration and initialization of the variables to know who's turn is it
		boolean playerTurn = false;
		boolean computerTurn = true;

		//Declaration and initialization of the variables for the winner
		boolean winnerFound = false;
		String winnerIs = "Nobody";
		String playerName;

		//Declaration and initialization of the scanner (keyboard input)
		Scanner myKeyboard = new Scanner(System.in);

		//Welcome message + ask the user for his name and uppercase the first letter
		System.out.println("Welcome to Ognib! Let's play.");
		System.out.print("What is your name? ");
		playerName = myKeyboard.next();
		playerName = playerName.substring(0,1).toUpperCase() + playerName.substring(1,playerName.length());

		//Creates the game card of each player (user and computer) with our own method
		gameCardCreation(boardPlayer, randomNumberPlayer, rand, MAX_VALUE, MIN_VALUE,isDuplicatePlayer);
		gameCardCreation(boardComputer, randomNumberComputer, rand, MAX_VALUE, MIN_VALUE,isDuplicateComputer);

		//Sorts the two arrays so the values inside the arrays will be sorted in ascending order
		selectionSort(boardPlayer);
		selectionSort(boardComputer);

		//Prints the player board in the console with our own method
		System.out.println("\n" + playerName +" - Game Card:");
		showCard(boardPlayer);

		//The game continues until there's a winner
		do
		{
			System.out.println();

			//Computer's turn
			while (computerTurn)
			{
				//The computer randomly chooses an random index on his card
				//If the value inside the index equals zero, it chooses another index
				for (int tokenNumber = 1; tokenNumber <= 3; tokenNumber++)
				{
					do
					{
						randomIndexComputer = rand.nextInt(LENGTH);
						alreadyTakenNumber = false;

						if (boardComputer[randomIndexComputer] == 0)
							alreadyTakenNumber = true;
					}
					while (alreadyTakenNumber);

					//Prints the number chose by the computer
					System.out.println("Computer's token:\t" + boardComputer[randomIndexComputer]);

					//On the player's card, it looks for the number called by the computer and change to zero if the program finds the number
					//On the computer's card, it changes the value inside the random generated index to zero
					for (int i = 0; i < boardPlayer.length; i++)
						if (boardPlayer[i] == boardComputer[randomIndexComputer])
							boardPlayer[i] = 0;
					boardComputer[randomIndexComputer] = 0;

					//Verifies if there's a winner with our method
					winnerIs = cardVerification(boardPlayer, boardComputer, ROWS, LENGTH, playerName, winnerIs);

					//If there's a winner, the program change the variable winnerFound to true and stops the loop
					if (winnerIs.equals("Computer") || winnerIs.equals(playerName) || winnerIs.equals("Tie"))
						{
						winnerFound = true;
						}

					//At the end of the computer turn (after 3 tokens called) or when a winner is found, 
					//it prints the player's card in the console
					//If a winner is found, it stops the game and prints the computer's card or else the game continue with the player turn 
					if (tokenNumber == 3 || winnerFound == true)
					{
						//Prints the player board in the console with our own method if the turn or the game is over
						gameOrTurnOverShowPlayerCard(boardPlayer, playerName, winnerIs);

						if (winnerFound)
						{
							computerTurn = false;
							playerTurn = false;

							//Prints the computer board in the console with our own method if the game is over
							gameOverShowComputerCard(boardComputer, winnerIs);
							break;
						}
						else
						{
							computerTurn = false;
							playerTurn = true;
						}
					}
				}
			}

			//Player's turn
			while (playerTurn)
			{
				//Asks the user for a number on his card
				System.out.print("\nIt's your turn, chose a number: ");
				tokenNumberPlayer = myKeyboard.nextInt();

				//Looks for the number called in both card, if the number is found, it changes it to zero
				for (int i = 0; i < boardPlayer.length; i++)
					if (boardPlayer[i] == tokenNumberPlayer)
						boardPlayer[i] = 0;
				for (int i = 0; i < boardComputer.length; i++)
					if (boardComputer[i] == tokenNumberPlayer)
						boardComputer[i] = 0;

				//Verifies if there's a winner with our method
				winnerIs = cardVerification(boardPlayer, boardComputer, ROWS, LENGTH, playerName, winnerIs);

				//If there's a winner, the program change the variable winnerFound to true and stops the loop
				if (winnerIs.equals("Computer") || winnerIs.equals(playerName) || winnerIs.equals("Tie"))
					winnerFound = true;

				//Prints the player board in the console with our own method if the turn or the game is over
				gameOrTurnOverShowPlayerCard(boardPlayer, playerName, winnerIs);

				//If a winner is found, it stops the game and prints the computer's card or else the game continue with the computer turn 
				if (winnerFound)
				{
					computerTurn = false;
					playerTurn = false;

					//Prints the computer board in the console with our own method if the game is over
					gameOverShowComputerCard(boardComputer, winnerIs);
				}
				else
				{
					computerTurn = true;
					playerTurn = false;
				}
			}
		}
		while (!winnerFound);

		//Closes the scanner (keyboard input)
		myKeyboard.close();
	}


	//Creation of our own method that creates game card with random generated numbers inside each index
	public static void gameCardCreation(int[] board, int randomNumber, Random rand, int MAX_VALUE, int MIN_VALUE, boolean isDuplicate)
	{
		for (int i = 0; i < board.length; ++i)
		{
			do
			{
				//Picks a number between 1 and 100 (inclusively)
				randomNumber = rand.nextInt((MAX_VALUE - MIN_VALUE) + 1) + MIN_VALUE;

				isDuplicate = false;

				//Checks if there's a duplicate or else it looks for another number
				for (int j = 0; j < i; ++j)
					if (board[j] == randomNumber)
						isDuplicate = true;
			}
			while (isDuplicate);

			//Applies the number to the index
			board[i] = randomNumber;
		}	
	}

	//Creation of our own method that sort the values of the array in ascending order
	//The code was taken from the slides
	//Source: 248-ch6-ArraysPrtA
	public static void selectionSort(int[] board)
	{
		int min, temp;

		//For every element, except the last one
		for (int index = 0; index < board.length-1; index++) 
		{
			min = index;

			//Finds the smallest value between index and the last element of the array
			for (int scan = index+1; scan < board.length; scan++)
				if (board[scan] < board[min])
					min = scan;

			//Swaps the smallest value with the index
			temp = board[min];
			board[min] = board[index];
			board[index] = temp;  
		}
	}

	//Creation of our own method that prints in the console the game card of the computer or the player (depends on which one we would like to see)
	public static void showCard(int[] board)
	{
		System.out.print("==================================================================\n| ");
		for (int i = 0; i < board.length; ++i)
		{
			if ((i % 5) == 0 && !(i == 0))
				System.out.print("\n|");

			if (board[i] == 100)
				System.out.print("     " + board[i] + "    |");
			if (board[i] >= 10 && board[i] < 100)
				if (i == 0)
					System.out.print("    " + board[i] + "     |");
				else
					System.out.print("     " + board[i] + "     |");
			if (board[i] < 10)
				if (i == 0)
					System.out.print("     " + board[i] + "     |");
				else
					System.out.print("      " + board[i] + "     |");
		}
		System.out.println("\n==================================================================");
	}

	//Creation of our own method that verifies if there's a winner.
	//Returns the name of the winner (Computer or the player name) or "Tie" if there's a tie (By default: there's no winner)
	public static String cardVerification(int[] boardPlayer, int[] boardComputer, int ROWS, int LENGTH, String playerName, String winnerIs)
	{
		for (int x = 0; x < ROWS; x++)
		{
			//Declaration and initialization of local variables
			int sumColumnPlayer = 0;
			int sumRowPlayer = 0;
			int sumDiagonal1Player = 0;
			int sumDiagonal2Player = 0;

			int sumColumnComputer = 0;
			int sumRowComputer = 0;
			int sumDiagonal1Computer = 0;
			int sumDiagonal2Computer = 0;
			

			//Looks if there's a column win
			for (int column = 0; column < boardPlayer.length; column += 5)
			{
				sumColumnPlayer += boardPlayer[column + x];
				sumColumnComputer += boardComputer[column + x];
				if ((sumColumnPlayer == 0 || sumColumnComputer == 0) && (column % 4 == 0) && (column != 0))
				{
					System.out.println("\nColumn win detected...");
					break;
				}
			}

			//Looks if there's a row win
			for (int row = 0; row < ROWS; row++)
			{
				sumRowPlayer += boardPlayer[row + ROWS*x];
				sumRowComputer += boardComputer[row + ROWS*x];
				if ((sumRowPlayer == 0 || sumRowComputer == 0) && (row % 4 == 0) && (row != 0))
				{
					System.out.println("\nRow win detected...");
					break;
				}
			}

			//Looks if there's a diagonal win (top left corner to bottom right corner)
			for (int diag1 = 0; diag1 < boardPlayer.length; diag1 += 6)
			{
				sumDiagonal1Player += boardPlayer[diag1];
				sumDiagonal1Computer += boardComputer[diag1];
				if ((sumDiagonal1Player == 0 || sumDiagonal1Computer == 0) && (diag1 == boardPlayer.length - 1))
				{
					System.out.println("\nDiagonal win detected...");
					break;
				}					
			}

			//Looks if there's a diagonal win (top right corner to bottom left corner)
			for (int diag2 = 4; diag2 < LENGTH - 4; diag2 += 4)
			{
				sumDiagonal2Player += boardPlayer[diag2];
				sumDiagonal2Computer += boardComputer[diag2];
				if ((sumDiagonal2Player == 0 || sumDiagonal2Computer == 0) && (diag2 == LENGTH - 5))
				{
					System.out.println("\nDiagonal win detected...");
					break;
				}
			}

			//If there's a winner, displays the results (Who won or tie)
			if ((sumColumnPlayer == 0 || sumRowPlayer == 0 || sumDiagonal1Player == 0 || sumDiagonal2Player == 0) && 
					(sumColumnComputer == 0 || sumRowComputer == 0 ||sumDiagonal1Computer == 0 || sumDiagonal2Computer == 0))
			{
				winnerIs = "Tie";
				System.out.println("\nIt's a tie !");
				System.out.print("\n=== Final Results ===\n");
				break;
			}

			if (sumColumnPlayer == 0 || sumRowPlayer == 0 || sumDiagonal1Player == 0 || sumDiagonal2Player == 0)
			{
				winnerIs = playerName;
				System.out.print("\n=== Final Results ===\n");
				break;
			}

			if (sumColumnComputer == 0 || sumRowComputer == 0 ||sumDiagonal1Computer == 0 || sumDiagonal2Computer == 0)
			{
				winnerIs = "Computer";
				System.out.print("\n=== Final Results ===\n");
				break;
			}
		}
		return(winnerIs);
	}

	//Creation of our own method that shows the player card if the computer's turn is over or if a winner is found
	public static void gameOrTurnOverShowPlayerCard(int[] boardPlayer, String playerName, String winnerIs)
	{
		System.out.println("\n" + playerName + " - Game Card:");
		showCard(boardPlayer);

		if (winnerIs.equals("Tie"))
			System.out.println();

		if (winnerIs.equals(playerName))
			System.out.println("Congratulations, " + playerName + ", you have a winning card!");	
	}

	//Creation of our own method that shows the computer card if a winner is found
	public static void gameOverShowComputerCard(int[] boardComputer, String winnerIs)
	{
		System.out.println("Computer - Game Card:");
		showCard(boardComputer);

		if (winnerIs.equals("Computer"))
			System.out.println("The computer won the game");
	}
}

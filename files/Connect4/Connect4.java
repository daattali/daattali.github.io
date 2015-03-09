//*****************************************************************************
//*Name: Dean Attali                                                          *
//*Time of creation : November, 2004                                          *
//*Course Code: ICS3M1                                                        *
//*Name : Connect4.java                                                       *
//*Teacher : Mr. Fernandes                                                    *
//*Function  : Makes a program that can play connect four with computer levels*
//*            as well as human players playing head to head.                 *
//*Major Skills: Applets, recursion, Frames, tabbedpanes, methods, timers     *
//*              actionlistener, handleEvent, and most importantly, patience  *
//*              and endurance.                                               *
//*                                                                           *
//*Special Specifications: <NONE>, our program should run FLAWLESSLY          *
//*Extra Features: 1. Indicates what type of game is being played             *
//*                2. Piece Drops with animation through each slot above      *
//*                3. User can click on the column to drop the piece instead  *
//*                   of having a button on each of the columns               *
//*                4. There is an arrow that updates itself for each player   *
//*                   colour and if each column is playable (not full) above  *
//*                   each column                                             *
//*                5. Splash Screen, with flashing title (timer)              *
//*                6. Computer Level 4 where the computer only plays on a     *
//*                   safe column until it is forced to do otherwise          *
//*                7. Computer play Level 5 where it will set up a double     *
//*                   trap, meaning that one token placed will enable two     *
//*                   slots to win (sure win)                                 *
//*                8. Computer play Level 6 where the computer can detect     *
//*                   potential double-traps set up by the user, and blocks   *
//*                   them before the user can win. Although this looks like  *
//*                   the computer can not lose, an experienced player can    *
//*                   trick the computer by making several places for double- *
//*                   traps and the computer at this level can not tell which *
//*                   double-trap is more lethal/dangerous, so it can only    *
//*                   stop one of the double traps                            *
//*                                                                           *
//*                                                                           *
//* *NOTE*: Instead of making a lot of small appearance-orientated special    *
//*       features, I decided that it would be better to come up with fewer   *
//*       but more challenging extra features, hence, our amount of different *
//*       computer play levels, those levels really took some time and I put *
//*       a lot of effort into these "smarter" levels, therefore, I think    *
//*       that really deserve a 100% for the Extra Features part of my       *
//*       wonderful and stimulating program.                                  *
//*                                                                           *
//*****************************************************************************


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.applet.*;


public class Connect4 extends Applet implements ActionListener {

	int
		board[][]=new int[6][7],//an array to store the board values. 0=empty, 1=player1, 2=player2
		score[] = new int[2], //the score of each player
		playerNum=0, //the current player, 0=game hasn't started
		level=0,//the game type - 0 for two players and 1, 2, 3, 4, 5 for the corresponding computer level
		column=0, //the column number of where the mouse is
		currentRow=0,//the current row where the piece should be drawn during the animation of it dropping
		empty=0, //the first empty row from the bottom of a column
		dropCol=0, //the column where the piece drops in the animation. Without this variable, the column where the piece drops would change when the mouse moves
		safePlay[]; //an array that stores what columns are safe to play in (for higher levels)

	boolean
		won=false, //checks if a player has just won
		smart=false, //true when the computer should "be smart" and think 2 turns ahead
		canPlayHere[]=new boolean[7], //an array that stores in which columns the computer can play and not lose the next turn
		smartWin=false; //for level 5, if the computer needs to find a smart win this is true

	Color bg=Color.lightGray; //the background colour, default is light gray
	final Color COL1=Color.red; //constant colour of player 1
	final Color COL2=Color.yellow; //constant colour of player 2

	JTabbedPane tabbedPane; //the tabbed pane for the help file
	JPanel panel1, panel2, panel3; //panels 1-3 are the help pages...

    JFrame selectFrame = new JFrame(); //a frame to choose the game type

	JFrame helpFrame = new JFrame();//creates the JFrame for the help file

	JComboBox selectBox; //a combo box to select the game type to play

	//an array that stores the values of the combo box
	String[] gameTypes = {"Two - Player Game", "One Player - Level 1", "One Player - Level 2", "One Player - Level 3", "One Player - Level 4", "One Player - Level 5", "One Player - Level 6"};

	Button //create buttons
		newButt=new Button("New Game"),
		helpButt=new Button("Help"),
		OK =new Button("OK");

	Timer
		titleTimer=new Timer(200, this), //timer to flash the title
		dropTimer=new Timer(100, this); //the timer to drop the pieces



	public void init() {

		//applet's properties
		setBackground(bg); //set the background colour

		titleTimer.start(); //start the title timer

		//add action listeners to the buttons
		newButt.addActionListener(this);
		helpButt.addActionListener(this);

		setSize(400, 450); //set the size of the applet

		//select game frame's properties
		selectFrame.setSize(150, 120);
		selectFrame.setBackground(Color.darkGray);
		selectFrame.setUndecorated(true); //remove max/min/close buttons

		OK.setFont(new Font("SansSerif", Font.BOLD, 12)); //set the font of the OK button to bold
		OK.addActionListener(this); //add the OK button

		Container frameCont=selectFrame.getContentPane(); //get the content pane of the frame
		frameCont.setLayout(new FlowLayout()); //set the frame's layout
		frameCont.setBackground(Color.blue); //make the colour of the frame more noticeable

		selectBox=new JComboBox(gameTypes); //create the combo box

		//create a label
		JLabel label=new JLabel("Select Game Type:");
		label.setFont(new Font("SansSerif", Font.BOLD, 14));
		label.setForeground(Color.cyan);

		//the interface of the frame
		frameCont.add(label);
		frameCont.add(selectBox);
		frameCont.add(OK);

	} //end method init



	public void actionPerformed(ActionEvent e) {

		if (e.getSource()==OK) { //if the OK button in the select game frame is clicked

			level=selectBox.getSelectedIndex(); //the difficulty level is the same as the index of the selected item in the combo box

			selectFrame.dispose(); //close the frame

			showType(getGraphics()); //write the type of game being played
			newBoard(getGraphics());

		} //end if

		else if (selectFrame.isShowing()|helpFrame.isVisible()); //if the select game frame or the help file is running, ignore any clicks on the game window

		else if (e.getSource()==titleTimer) { //if it is the title timer

			int titCol=1+(int)(Math.random()*9); //make a random number between 1-9
			titlePage(getGraphics(), titCol); //call titlepage to make the title flash

		} //end if

		else if (e.getSource()==dropTimer) { //if it is the drop piece timer

			dropPiece(getGraphics()); //drop the piece

			currentRow++; //next time the piece is in the next row

		} //end else if

		else if (e.getSource()==newButt) { //if it is the new game button

			dropTimer.stop(); //if a piece is being dropped, stop dropping it

			//reset scores
			score[0]=0;
			score[1]=0;

			newBoard(getGraphics()); //call newBoard to make a new board

			playerNum=1; //set the turn to player1

			selectFrame.setLocation((int)getLocationOnScreen().getX()+(int)getSize().getWidth(),(int)getLocationOnScreen().getY()+((int)getSize().getHeight()/2));
			selectFrame.show(); //show the select game frame

		} //end else if

		else if (e.getSource()==helpButt){//if the help button was clicked

			helpFrame.setTitle( "Connect Four Help File" );//sets the name of the popup
			helpFrame.setSize( 400, 250 ); //sets the size
			helpFrame.setLocation(430,100);//sets the location of this frame
			helpFrame.setBackground( Color.gray );//sets the colour

			JPanel topPanel = new JPanel();//makes a panel for the top of the frame
			topPanel.setLayout( new BorderLayout() );
			helpFrame.getContentPane().add( topPanel );//gets the contentpane for the panel

			// Create the tab pages
			createPage1();
			createPage2();
			createPage3();

			// Create a tabbed pane
			tabbedPane = new JTabbedPane();//adds each panel into the tabbed pane
			tabbedPane.addTab( "Gameplay", panel1 );
			tabbedPane.addTab( "Strategies", panel2 );
			tabbedPane.addTab( "Endgames", panel3 );
			topPanel.add( tabbedPane, BorderLayout.CENTER );

			helpFrame.setVisible(true);//shows the frame

		} //end else if

	} //end method actionPerformed

	public boolean handleEvent(Event e) {

		if (selectFrame.isShowing()) return false; //if the select game frame is running, break from the method to ignore any mouse events in the game window

		if (playerNum==1 | playerNum==2 & level==0) column = ((e.x+2)/45)-1; //determine the column where the mouse is, unless it's the computer's turn

		if (e.id==502) { //mouse release

			if (titleTimer.isRunning()) { //if the applet is in the title page

				titleTimer.stop(); //stop the title timer

				//add the buttons
				add(newButt);
				//add(helpButt);

				validate(); //refresh

				newBoard(getGraphics()); //make a new board

				playerNum=1; //set the turn to player1

				selectFrame.setLocation((int)getLocationOnScreen().getX()+(int)getSize().getWidth(),(int)getLocationOnScreen().getY()+((int)getSize().getHeight()/2));
				selectFrame.show(); //show the select game frame

			} //end if

			else { //if the applet is not in the title page

				if (dropTimer.isRunning()==false & e.x>=43 & e.x<=357 & e.y>=72 & e.y<=347) { //if the click is inside the board dimensions and no piece is currently being dropped

					if (board[0][column]==0) { //if the column is not full

						playPiece(false); //calls the play piece method

					} //end if

				} //end if

			} //end else

		} //end if

		else if (e.id==503) { //mouse move

			//if the mouse is moved inside the board while the game is played
			if (e.x>=43 & e.x<=357 & e.y>=72 & e.y<=347 & titleTimer.isRunning()==false) {

				setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR)); //change the cursor to crosshairs

				//draw an arrow above the column, with the colour based on what player it is (only if no piece is currently being dropped)
				if (playerNum==1 & dropTimer.isRunning()==false) drawArrow(getGraphics(), COL1);
				else if (playerNum==2 & level==0 & dropTimer.isRunning()==false) drawArrow(getGraphics(), COL2); //if it's player 2, only draw an arrow if it is a 2-player game

			} //end if

			else setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); //set the cursor to default

		}//end else if

		return true; //something has to be returned...

	} //end method handleEvent



	public void titlePage(Graphics g, int color) {

		//write our names
		g.setColor(Color.black);
		g.setFont(new Font("SansSerif", Font.BOLD, 16));
		g.drawString("Dean Attali, November 2004", 90, 237);
		g.setFont(new Font(null, Font.PLAIN, 12));
		g.drawString("(Click anywhere to continue...)", 120, 290);
		g.drawString("V1.1",190, 200);

		switch (color) { //determine what the colour is based on the number
			case 1: g.setColor(Color.red); break;
			case 2: g.setColor(Color.black); break;
			case 3: g.setColor(Color.blue); break;
			case 4: g.setColor(Color.cyan); break;
			case 5: g.setColor(Color.white); break;
			case 6: g.setColor(Color.green); break;
			case 7: g.setColor(Color.orange); break;
			case 8: g.setColor(Color.yellow); break;
			case 9: g.setColor(Color.magenta); break;
		} //end switch

		//write the title
		g.setFont(new Font("Serif", Font.BOLD, 38));
		g.drawString("Connect 4", 120, 178);

		showStatus("Connect 4 V1.1 - by Dean Attali, November 2004"); //display the  title in the status bar

	} //end method titlePage



	public void newBoard(Graphics g) {

		for (int i=0;i<6;i++) { //loop 6 times
			for (int j=0;j<7;j++) { //loop 7 times

				board[i][j]=0; //make the whole board empty

			} //end nested for
		} //end for

		//draw the board
		g.setColor(bg);
		g.fillRect(0, 0, 500, 400);
		g.setColor(Color.black);
		g.fillRect(38, 68, 324, 283);
		g.setColor(Color.blue);
		g.fillRect(40, 70, 320, 279);
		g.setColor(Color.white);

		//draw the circles on the board
		for (int i=0;i<7;i++) { //7 columns
			for (int j=0;j<6;j++) { //6 rows

				g.fillOval(45*(i+1),45*(j+1)+30, 40, 40); //draw a circle

			} //end nested for
		} //end for

		updateScore(getGraphics()); //update the scores

		validate(); //refresh

	} //end method newBoard



	public void drawArrow(Graphics g, Color playerCol) {

		//clear any previous arrows drawn
		g.setColor(bg);
		g.fillRect(0, 35, 400, 21);

		if (board[0][column]==0) g.setColor(playerCol); //if the column is not full, the arrow colour is the player co lour
		else g.setColor(Color.gray); //if the column is full, the arrow is gray

		int xArrow[]=new int[8], yArrow[]=new int[8]; //arrays that store the x and y values of the arrow

		//determine the x values of the arrow
		xArrow[0]=(column+1)*45+15;
		xArrow[1]=xArrow[0]+10;
		xArrow[2]=xArrow[1];
		xArrow[3]=xArrow[2]+5;
		xArrow[4]=xArrow[3]-10;
		xArrow[5]=xArrow[4]-10;
		xArrow[6]=xArrow[5]+5;
		xArrow[7]=xArrow[6];

		//determine the y values of the arrow
		yArrow[0]=35;
		yArrow[1]=35;
		yArrow[2]=45;
		yArrow[3]=45;
		yArrow[4]=55;
		yArrow[5]=45;
		yArrow[6]=45;
		yArrow[7]=35;

		g.fillPolygon(xArrow, yArrow, 8); //draw the arrow

		g.setColor(Color.black); //set the colour to black
		g.drawPolygon(xArrow, yArrow, 8); //outline the arrow

	} //end method drawArrow



	public void dropPiece(Graphics g) {

		//set the colour of the piece
		if (playerNum==1) g.setColor(COL1);
		else g.setColor(COL2);

		g.fillOval((dropCol+1)*45, (currentRow+1)*45+30, 40, 40); //draw the piece

		g.setColor(Color.white); //set the colour to white

		if (currentRow>0) g.fillOval((dropCol+1)*45, (currentRow)*45+30, 40, 40); //delete the piece above it

		if (empty==currentRow) { //when the piece reaches the lowest empty spot

			boolean gameContinue=false; //will become true if there is an empty column

			dropTimer.stop(); //stop dropping the piece

			won=false; //reset won

			won=checkWin(1, 0, empty, (dropCol-1)); //check if the player has won, first check to the left

			if (won) { //if the player has won

				score[(playerNum-1)]++; //the player's score increments

				updateScore(getGraphics()); //update the scores

				JOptionPane.showMessageDialog(null, "Player "+(playerNum)+" wins!", "Connect4", JOptionPane.INFORMATION_MESSAGE); //show a message

				newBoard(getGraphics()); //make a new board

			} //end if

			validate(); //refresh

			//change the player turn
			if (playerNum==1) playerNum=2;
			else playerNum=1;

			column=dropCol; //set the column to the dropped column

			//change the arrow co lour
			if (playerNum==1) drawArrow(getGraphics(), COL1);
			else drawArrow(getGraphics(), COL2);

			 //if it's the computer's turn, call the correct method
			if (playerNum==2 & level > 0) {

				switch (level) { //determine what level the game is, and call that method

					case 1: lvl1(); break;
					case 2: lvl2(); break;
					case 3: lvl3(); break;
					case 4: lvl4(); break;
					case 5: case 6: lvlTrap(); break;


				} //end switch

			} //end if

			for (int i=0;i<7;i++) { //loop 7 columns

				if (board[0][i]==0) gameContinue=true; //if there is an empty column, continue game

			} //end for

			if (gameContinue==false & won==false) { //if the board is full and no one won in the last move

				//display a message indicating the game is tied
				JOptionPane.showMessageDialog(null, "Tie Game!", "Connect4", JOptionPane.INFORMATION_MESSAGE);

				newBoard(getGraphics()); //make a new board

			} //end if

		} //end if

	} //end method dropPiece



	public void updateScore(Graphics g) {

		g.setColor(bg); //set the colour to match the background
		g.fillRect(108, 355, 200, 100); //delete the previous string that was there
		g.setColor(Color.black); //set the colour to black

		for (int i=0;i<2;i++) { //loop twice

			g.setFont(new Font("Serif", Font.BOLD, 20)); //make the font bigger
			g.drawString("Player "+(i+1)+" Score:  "+score[i], 125, (i*30)+405); //show the scores

		} //end for

		showType(getGraphics());

	} //end method updateScore



	public void showType(Graphics g) {

		g.setColor(bg); //set the colour to match the background
		g.fillRect(108, 355, 200, 30); //delete the previous string that was there
		g.setColor(Color.black); //set the colour to black
		g.setFont(new Font("SansSerif", Font.BOLD, 22)); //make the font bigger

		switch (level) { //write what level it is

			case 0: g.drawString("Two Players", 136, 375); break; //2-player game

			default: g.drawString("Comp Level "+level, 130, 375); break; //1-player game

		} //end switch

	} //end method showType



	public boolean checkWin(int dir, int connected, int checkRow, int checkCol) {

		if (connected>=3) won=true; //if there are 4 pieces in a row, one more pattern is completed

		if (won) return true; //if the player has already won, stop checking

		switch (dir) { //determine which direction to check

		 /* For each case (direction) there is an if...else statement
		    The if part is an error check + check if the piece matches to the player, and calls
		    	the recursive method with the next piece in this direction
		    The else part calls the recursive method with the next direction to check, because
		    	the piece there didn't match, or it was outside the array */

			case 1: //horizontal left

				if (checkCol>=0 && board[checkRow][checkCol]==playerNum) { connected++; return checkWin(dir, connected, checkRow, --checkCol); }
				else return checkWin(++dir, connected, empty, (dropCol+1));

			case 2: //horizontal right

				if (checkCol<=6 && board[checkRow][checkCol]==playerNum) { connected++;return checkWin(dir, connected, checkRow, ++checkCol); }
				else return checkWin(++dir, 0, (empty+1), dropCol);

			case 3: //vertical (down only)

				if (checkRow<=5 && board[checkRow][checkCol]==playerNum) { connected++; return checkWin(dir, connected, ++checkRow, checkCol); }
				else return checkWin(++dir, 0, (empty-1), (dropCol-1));

			case 4: //diagonal up left

				if (checkRow>=0 && checkCol>=0 && board[checkRow][checkCol]==playerNum) { connected++; return checkWin(dir, connected, --checkRow, --checkCol); }
				else return checkWin(++dir, connected, (empty+1), (dropCol+1));

			case 5: //diagonal down right

				if (checkRow<=5 && checkCol<=6 && board[checkRow][checkCol]==playerNum) {connected++; return checkWin(dir, connected, ++checkRow, ++checkCol); }
				else return checkWin(++dir, 0, (empty+1), (dropCol-1));

			case 6: //diagonal down left

				if (checkRow<=5 && checkCol>=0 && board[checkRow][checkCol]==playerNum) { connected++; return checkWin(dir, connected, ++checkRow, --checkCol); }
				else return checkWin(++dir, connected, (empty-1), (dropCol+1));

			case 7: //diagonal up right

				if (checkRow>=0 && checkCol<=6 && board[checkRow][checkCol]==playerNum) { connected++; return checkWin(dir, connected, --checkRow, ++checkCol); }
				else return false; //if all checking has been made and no pattern was found, return false

		} //end switch

		return won; //return true or false, depending if the player has won or not

	} //end method checkWin



	public void playPiece(boolean comp){

		//draw an arrow where the player played
		if (playerNum==1) drawArrow(getGraphics(), COL1);
		else drawArrow(getGraphics(), COL2);

		empty=findEmpty(column); //find the first empty row from the bottom

		//start dropping the piece from the top
		if (comp==true)	currentRow=-1;
		else currentRow = 0;

		dropCol=column; //the column where the piece drops is the clicked column

		board[empty][dropCol]=playerNum; //assign the first empty row to the current player

		dropTimer.start(); //start the drop piece timer

	} //end method playPiece


	public int findEmpty(int c) {

		for (int i=5;i>=0;i--) { //loop from row 5 to 0

			//if the current row is empty, then it is the lowest empty space and return it
			if (board[i][c]==0) return i;

		} //end for

		return 0; //this line will never be executed

	} //end method findEmpty



	public void lvl1() {

		do {
			column=(int)(Math.random()*7); //choose a random column
		} while (board[0][column]!=0); //until the random column is not full

		playPiece(true); //calls the play piece method

	} //end method lvl1



	public void lvl2(){

		int winLook=compPlay(2); //search for a winner

		if (winLook>=0) playPiece(true); //if a winner was found, play there
		else lvl1(); //if the computer cannot win, drop a piece in a random column

	} //end method lvl2



	public void lvl3() {

		int winLook=compPlay(2); //search for a winner

		if (winLook>=0) playPiece(true); //if a winner was found, play there
		else { //if no winner was found

			int loseLook=compPlay(1); //search for a loser

			if (loseLook>=0) playPiece(true); //if a loser was found, play there
			else lvl1(); //if no winner nor loser was found, select a random column

		} //end else part of if...else

	} //end method lvl3



	public void lvl4() {

		int play=thinkAhead(); //think 2 turns ahead

		if (play>=0) playPiece(true); //if there is somewhere to play, play there
		else lvl1(); //if there is no safe play, pick a random column

	} //end method lvl4



	public void lvlTrap() {

		trap(); //look for a play that will trap the player

	} //end method level5



	public int compPlay (int search) {

		if (search==1) { //search for a loser

			playerNum=1; //since the computer will be pretending to be the person, temporarily change the turn to the person's

			int loseLook=compPlay(2); //search for a loser

			playerNum=2; //switch the turn back to the computer

			if (loseLook>=0) return loseLook; //if a loser is found, return it

		} //end if

		else { //search for winner

			for (int x=0; x <7; x++) { //loop from column 0 to 6

				boolean playThis=false; //true if the computer should play in the specific column

				if (smartWin) { //if the computer is looking for a smart win
					if (canPlayHere[x]) { //if this column is safe to play

						playThis=pretendPlay(x); //if it's safe to play here, pretend to play here

					} //end if
				} //end if

				else { //if the computer is not smart
					playThis=pretendPlay(x); //pretend to play in this column
				} //end else

				if ((smartWin) & (playThis)) return x; //if the computer is looking 3 steps ahead, return x
				else if (smart==false & playThis==false | (smart) & (playThis)) continue; //if this column shouldn't be played, skip to next column
				else if ((smart) & playThis==false & board[0][x]==0) canPlayHere[x]=true; //if the "smart" computer found a place to play, add this column to the list of columns that can be played
				else if (smart==false & (playThis)) return x; //if the computer is normal and a winner/loser was found, return it

			} //end for

		} //end else if

		return -1; //if no winner/loser was found, return -1

	} //end method compPlay



	public boolean pretendPlay(int pretendCol) {

		if (level>=4 & (smart) & smartWin==false) { //if the computer should think 2 steps ahead

			column=pretendCol; //pretend that the column is pretendCol (because other methods are being called, and pretendCol will not be recognized)

			empty=findEmpty(column); //find the empty space in that column

			if (board[1][column]!=0 | board[0][column]!=0) return false; //if the column is full or 1 before being full, the computer can't pretend to drop 2 pieces there

			board[empty][column]=2; //pretend that the space is the computer's

		} //end if

		//pretend that the column is pretendCol (because other methods are being called, and pretendCol will not be recognized)
		column = pretendCol;
		dropCol = pretendCol;

		empty=findEmpty(column); //find the first empty row from the bottom

		if (board[0][column]!=0) return false; //if this column is full, return false

		board[empty][column]=playerNum; //pretend that the computer/human plays in this space

		won = false; //reset won to false
		won = checkWin(1,0,empty,(column-1)); //check if the computer would win if he had a piece there

		board[empty][column] = 0; //reset the space back to empty
		if (level>=4 & (smart) & smartWin==false) board[(empty+1)][column]=0; //if the computer was thinking ahead, turn the first piece to empty again

		//if a winner/loser was found, return true and if not return false
		if (won) return true;
		else return false;

	} //end method pretendPlay


	public int thinkAhead() {

		smart=false; //at the beginning the computer is normal
		smartWin=false; //at the beginning the computer is looking for normal wins

		int winLook=compPlay(2); //search for a winner

		if (winLook>=0) return winLook; //if a winner is found, play it
		else { //no winner found

			int loseLook=compPlay(1); //search for a loser

			if (loseLook>=0) return loseLook; //if a loser was found, play there
			else { //if no loser was found either, play in a column that will not let the player win

				smart=true; //now the computer should "be smart" and think ahead

				for (int i=0;i<7;i++) { //loop 7 times
					canPlayHere[i]=false; //set the array to false
				} //end for

				int notGive=compPlay(1); //search for a column that will become a loser if it is played

				int columnsAvailable=0; //the number of columns the computer can play in without losing

				for (int i=0;i<7;i++) { //loop 7 times
					if (canPlayHere[i]==true) columnsAvailable++; //count how many columns are options to play
				} //end for

				safePlay=new int[columnsAvailable]; //create the array based on how many columns are safe

				int counter=0; //a counter variable

				for (int i=0;i<7;i++) { //loop 7 times

					if (canPlayHere[i]==true) { //if it is safe to play in this column
						safePlay[counter]=i; //the array contains only the safe columns
						counter++; //increment counter
					} //end if

				} //end for

				if (columnsAvailable>0) { //if there are available columns to play

					int random=(int)(Math.random()*safePlay.length); //pick a random number
					column=safePlay[random]; //determine the safe column

					if (level>=5) smartWin=true; //the computer needs to find a smart win

					return column; //if a column that won't become a loser after played is found, play one of them

				} //end if

				else return -1; //if there aren't any columns available, return 0

			} //end else

		} //end else if

	} //end method thinkAhead



	public void trap() {

		int play=thinkAhead(); //think 2 turns ahead

		if (play>=0 & smartWin==false) playPiece(true); //if a winner/loser was found, play it

		else if (play>=0 & (smartWin)) { //if the computer needs to look for a smart win

			boolean played=false; //true when the column was tried to be played

			//the locations of where the first and last pieces would drop
			int initRow=empty, initCol=column, winRow, winCol;

			for (int i=0; i<safePlay.length;i++) { //loop the number of safe columns there are

				played=false; //column hasn't been played it

				if (board[0][safePlay[i]]!=0) continue; //if the column is full, skip to the next one

				//pretend that the column is i
				column=safePlay[i];
				dropCol=safePlay[i];

				empty=findEmpty(column); //find the empty row

				//store the location of the piece that was first dropped, and the one that was dropped after to delete them later
				initRow=empty;
				initCol=column;
				winRow=empty;
				winCol=column;

				board[initRow][initCol]=2; //pretend that the space is the computer's

				int doubleWin=compPlay(2); //search for a winner after playing a piece here

				if (doubleWin>=0) { //if he would win

					//set the location of the winning piece
					winRow=empty;
					winCol=column;

					board[winRow][winCol]=1; //pretend that the player will block the computer if he played there

					doubleWin=compPlay(2); //search for a winner after the player will block the computer

					if (doubleWin>=0) { //if the computer would win again

						//reset both the first dropped piece and the last one
						board[winRow][winCol]=0;
						board[initRow][initCol]=0;

						played=true; //now the column will be played

						column=initCol; //the computer should play where he first dropped a piece

						playPiece(true); //play the smart win

						break;

					} //end if

					else { //if he wouldn't win again

						//reset the pieces back to empty
						board[winRow][winCol]=0;
						board[initRow][initCol]=0;

					} //end else

				} //end if

				else { //if he wouldn't win

					 //reset the pieces back to empty
					board[winRow][winCol]=0;
					board[initRow][initCol]=0;

				} //end else

			} //end for

			if (played==false & level==5) { //if no play can trap the player and it's level 5

				do {

					board[initRow][initCol]=0; //reset the piece back to empty

					//if there is no smart win, just play in a safe column
					int random=(int)(Math.random()*safePlay.length); //pick a random number
					column=safePlay[random]; //determine the safe column
					playPiece(true); //play the piece

				} while (board[0][column]!=0); //loop until the column is not full

			} //end if

			else if (played==false & level==6) { //if no play can trap the player and it's level 6

				boolean safeColumn[]=new boolean[7]; //stores if each column is safe or not

				for (int i=0;i<7;i++) { //loop 7 times

					safeColumn[i]=false; //initialize all columns to unsafe

					for (int j=0;j<safePlay.length;j++) { //loop the number of safe columns

						if (safePlay[j]==i) safeColumn[i]=true; //this column is safe

					} //end for

				} //end for

				for (int c=1;c<5;c++) { //loop from column 1 to 4

					if (played) break; //if a column has been found to play in, break out of the for loop

					for (int r=5;r>=0;r--) { //loop all the rows

						if (board[5][c]!=0) { //check that the column isn't empty

							//2 pieces one next to the other
							if (board[r][c]==1 && board[r][c+1]==1 && findEmpty(c-1)==r && findEmpty(c+2)==r) {

								int closer=0, farther=0; //the columns to the end of the pieces that are closer and farther to the middle

								//determine which column is closer to the middle
								if (c<3) { //the right end is closer

									closer=c+2;
									farther=c-1;

								} //end if

								else { //the left end is closer

									closer=c-1;
									farther=c+2;

								} //end else


								if (safeColumn[closer]) { //if it's safe to play in the closer column to the middle, play there

									column=closer; //the column to play in is the closer one
									played=true; //a column was found to play
									playPiece(true);
									break;

								} //end if

								else if (safeColumn[farther]) { //if it's safe to play in the farther column to the middle, play there

									column=farther; //the column to play in is the farther one
									played=true; //a column was found to play
									playPiece(true);
									break;

								} //end else if

							} //end if

							//2 pieces with a single space between them
							else if (c<4 && board[r][c]==1 && board[r][c+2]==1 && findEmpty(c+1)==r && findEmpty(c-1)==r && findEmpty(c+3)==r) {

								if (safeColumn[c+1]) { //if it is safe to play in the middle

									column=c+1;
									played=true; //a column was found to play
									playPiece(true);
									break;

								} //end if

								else if (safeColumn[c-1]) { //if it is safe to play to the left

									column=c-1;
									played=true; //a column was found to play
									playPiece(true);
									break;

								} //end else if

								else if (safeColumn[c+3]) { //if it is safe to play to the right

									column=c+3;
									played=true; //a column was found to play
									playPiece(true);
									break;

								} //end else if

							} //end if

						} //end if

					} //end for

				} //end for

				if (played==false) { //if no column to play in was found

					int random=(int)(Math.random()*safePlay.length); //pick a random number
					column=safePlay[random]; //determine the safe column
					playPiece(true); //play the piece

				} //end if

			} //end else if

		} //end else if

		else lvl1(); //if there is nothing to do, pick a random column
	} ///end method trap



	public void createPage1() {

		JTextArea general=new JTextArea(5, 30); //create a textarea
		panel1 = new JPanel(); //starts the panel
		panel1.setBackground(Color.lightGray);//sets the colour and the layout of the page
		panel1.setLayout( new FlowLayout() );

		panel1.add(new JLabel("The connect 4 help file."));//this is just a title

		general.setText("The objective of this game is to connect\nfour tokens in a straight line, all directions\nare acceptable, i.e. horizontal, vertical and\nboth diagonal directions... ");
		general.setFont(new Font("SansSerif",Font.BOLD,14));//sets the appropriate font settings
		general.setForeground(Color.red); //set the colour of the writing
		general.setBackground(Color.lightGray); //the background colour
		general.setEditable(false); //make the textarea non-editable
		panel1.add(general);

	} //end method createPage1



	public void createPage2() {

		//everything is exact same thing as createPage1, with a different help topic

		JTextArea strategies=new JTextArea(5, 30);
		panel2 = new JPanel();
		panel2.setBackground(Color.lightGray);
		panel2.setLayout( new FlowLayout() );
		panel2.add(new JLabel("Strategies"));

		panel2.add(new JLabel("The connect 4 help file."));

		strategies.setText("The Strategies involved in this game can\nbe summed up pretty easily, basically, all\nyou want to do is win before your opponent, a very\ngood way to do so is to create a double-trap,\nhence creating 2 situations where you can win by\njust placing 1 token");
		strategies.setFont(new Font("SansSerif",Font.BOLD,14));
		strategies.setForeground(Color.red);
		strategies.setBackground(Color.lightGray);
		strategies.setEditable(false);
		panel2.add(strategies);

	} //end method createage2



	public void createPage3() {

		//everything is exact same thing as createPage1, with a different help topic

		JTextArea endGame=new JTextArea(5, 30);
		panel3 = new JPanel();
		panel3.setBackground(Color.lightGray);
		panel3.setLayout( new FlowLayout() );
		panel3.add(new JLabel("End Games -__-"));

		endGame.setText("The Game can only end in 3 different ways, either\nPlayer 1 won, or Player 2 won, or that it ended up\nin a tie, that is it for the connect4 game, to\nget back to the game, close this window\nby clicking the x at the top right corner");
		endGame.setFont(new Font("SansSerif",Font.BOLD,14));
		endGame.setForeground(Color.red);
		endGame.setBackground(Color.lightGray);
		endGame.setEditable(false);
		panel3.add(endGame);

	} //end method createPage3

} //end class Connect4


package sudoku;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.util.ArrayList;
import java.util.Random;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
/**
 * This class creates a Sudoku game. 
 * It allows the user to fill numbers from 1-9 within the grids. 
 * The user can check if their solution is correct. 
 * If prompted, it also provides the solution for the puzzle.  
 * @author Abigya Devkota
 * @version 1.0
 *
 */
public class Window extends Application {
	private static TextField[][] cells = new TextField[9][9];
	 private Button btSolve = new Button("Solve");
	  private Button btReset = new Button("New Game");
	  private Button btSubmit = new Button("Check");
	  private Button btClear = new Button("Reset");
	  private static Label lblStatus = new Label();
	  private static int[][]grid = new int[9][9];
	 
	@Override
	public void start(Stage primaryStage) throws Exception {
		/**Create a grid for the game**/
		GridPane[][] panels = new GridPane[9][9];
		    GridPane gridpane = new GridPane();
		    gridpane.setStyle("-fx-border-color: cadetblue;");
		    for (int i = 0; i < 3; i++) {
		      for (int j = 0; j < 3; j++) {
		        gridpane.add(panels[i][j] = new GridPane(), j, i);
		        panels[i][j].setStyle("-fx-border-color: cadetblue;");
		        }
		    }
		    /**Create a 9*9 TextField to get user input **/
		   for (int i = 0; i < 9; i++) {
			      for (int j = 0; j < 9; j++) {
			        panels[i / 3][j / 3].add(cells[i][j] = new TextField(), j % 3, i % 3);
			        cells[i][j].setPrefColumnCount(1);
			       }
			}
		    /**Buttons for the game**/
		    HBox hBox = new HBox(5);
		    hBox.getChildren().addAll(btSolve, btReset, btSubmit,btClear);
		    hBox.setAlignment(Pos.CENTER);
		    
		    BorderPane pane = new BorderPane();
		    pane.setCenter(gridpane);
		    pane.setBottom(hBox);
		    
		    /**Create a label at the top to show how the game is going on**/
		    pane.setTop(lblStatus);
		    
		    BorderPane.setAlignment(lblStatus, Pos.CENTER);
		   
		    /**Create a integer grid for the given game**/
		    for (int i = 0; i < 9; i++) {
			      for (int j = 0; j < 9; j++) {
			    	  if (cells[i][j].getText().trim().length() == 0) {
			          grid[i][j] = 0;
			        } else {
			          grid[i][j] = Integer.parseInt(cells[i][j].getText());
			        }
			      }
			    }
		   
		    /**A game is already initialized when the user launches it**/
		    initializeNumbers();
		    
		    /**Event Handlers**/
     		
		    /**Create a new game**/
		    btReset.setOnAction(e -> initializeNumbers());
		   
		    /**Check your solution**/
		    btSubmit.setOnAction(e -> {
			   if (checkInput(grid)==false || checkNumber(grid)==false){
				   lblStatus.setText("Invalid input");
			   }else{
				   lblStatus.setText("");
				   submit();
		   }
			   });
		    /**Reset the game**/
		    btClear.setOnAction(e -> clear());
		  
		    /**Solves the game if the user decides to give up**/
		    btSolve.setOnAction(e -> {
			 if (!isValid(grid)){
				  lblStatus.setText("Invalid input");
			  }else if (search(grid)) {
				  lblStatus.setText("The solution is found");
			      printGrid(grid);
			    }   
			    else{
			      lblStatus.setText("No solution");
			  }
			  
		  });
		    /**Create a scene and place it in the stage**/
			Scene scene = new Scene(pane, 500, 600);
	        primaryStage.setScene(scene);
		    primaryStage.setTitle("Sudoku"); 
	        primaryStage.show();
	       
	}
	
	public static void main(String[] args){
		launch(args);
	}
	/** 
	 * This method generates a Sudoku game. 
	 * It can create upto 60 random empty cells for the user to solve.
	 * The difficulty of a puzzle is thus based on the number of empty cells.
	 */
	public void initializeNumbers() {
		    	lblStatus.setText("");
		    	int k=1,n=1;                
		    	                            
			    for(int b=0;b<9;b++){
			 	      k=n;
			 	      for(int c=0;c<9;c++){
			 	          if(k<=9){
			 	              cells[b][c].setText(Integer.toString(k));
			 	        	 k++;
			 	          }else{
			 	           k=1;
			 	           cells[b][c].setText(Integer.toString(k));
			 	           k++;
			 	          }
			 	      }
			 	      n=k+3;
			 	      if(k==10)
			 	      n=4;
			 	      if(n>9)
			 	      n=(n%9)+1;
			 	   }
			   /**Generate random numbers to interchange rows and columns within a same group**/
			   int k1,k2,max=2,min=0;
			    Random r= new Random();
			    int check = r.nextInt(2);
			      for (int i=0;i<3;i++){
			    /**There are three groups.So we are using for loop three times.**/
			         k1=r.nextInt(max-min+1)+min;
			    /**This while is just to ensure k1 is not equal to k2.**/
			          do{
			            k2=r.nextInt(max-min+1)+min;
			          }while(k1==k2);
			          max+=3;min+=3;
			   
			          if(check==1){
			         /** change rows**/
			        	  int temp;/**k1 and k2 are two rows that we have selected to interchange.**/
				       for(int j=0;j<9;j++) {
				          temp=Integer.parseInt(cells[k1][j].getText());
				          cells[k1][j].setText(cells[k2][j].getText());
				          cells[k2][j].setText(Integer.toString(temp));
				          }
			          }else if(check==0){
			        	  /**change columns**/
			        	  int temp;
					      for(int j=0;j<9;j++){
					         temp=Integer.parseInt(cells[j][k1].getText());
					         cells[j][k1].setText(cells[j][k2].getText());
					         cells[j][k2].setText(Integer.toString(temp));
					        
					      }
			          }
			      }
			      
			     /**set cells to read-only first**/
			      for (int i=0;i<9;i++){
			    	  for (int j=0;j<9;j++){
			    		  cells[i][j].setStyle("-fx-control-inner-background:paleturquoise;");
			    		  cells[i][j].setEditable(false);
			    	  }
			      } 
			       
			     /**Random shuffle/create some editable cells to allow user input**/ 
	 	            Random num = new Random();
					   for (int i=0;i<60;i++){
						   int a = num.nextInt(9);
						   int b = num.nextInt(9);
						   cells[a][b].setText(" ");
						   cells[a][b].setStyle("-fx-control-inner-background:snow;");
						   cells[a][b].setEditable(true);
					   }
				    	
				    	
		    }
			/** 
			 * Reset the given game by erasing user input.
			 */
		    public void clear(){
		    	lblStatus.setText("");
		    	for (int i=0;i<9;i++){
		    		for (int j=0;j<9;j++){
		    			if (cells[i][j].isEditable()==true){
		    				cells[i][j].setText(" ");
		    				cells[i][j].setStyle("-fx-control-inner-background:snow;");
		    			}
			    	}
		    	}
		    	
		    }
		    /**
		     * Check if the user's solution is correct or not.
		     */
		    public void submit(){
		    	int[][]arraycopy = new int[9][9];
		    	
		    	/**Store the string values of textfield into a 9*9 array**/
		    	for (int i=0;i<9;i++){
		    		for (int j=0;j<9;j++){
		    			arraycopy[i][j]=Integer.parseInt(cells[i][j].getText().trim());
			    	}
		    	}
		    	/**create an arraylist for every row in the grid**/
		    	for (int k=0;k<9;k++){
		    	ArrayList<Integer> grid = new ArrayList<Integer>();
		    	for (int i=0;i<9;i++){
		    		grid.add(i, arraycopy[k][i]);
		    	}
		    	
		    	for (int i=1;i<10;i++){
		    		/**This arraylist stores the total number of occurrences of a number in the given row**/
					ArrayList<Integer> mistakes = new ArrayList<>();
					while (grid.indexOf(i)!=-1 && grid.lastIndexOf(i)!=-1){
						int first = grid.indexOf(i);
						int last= grid.lastIndexOf(i);
						if (first!=last){
							mistakes.add(first);
							mistakes.add(last);
						}else{
							mistakes.add(first);
						}
						grid.set(first, 0);
						grid.set(last, 0);
						
					}
					
					int count = mistakes.size();
					/**If the number of mistakes is greater than 1, that means a number is repeated more than once in the given row**/
					if (count >1){
						
					for (Integer x: mistakes){
						if (cells[k][(int)x].isEditable()==true){
							/**Highlight the mistakes**/
						cells[k][(int)x].setStyle("-fx-control-inner-background:red;");
						}
					}
					}else if (count <=1 ){
						
						for (Integer x: mistakes){
							if (cells[k][(int)x].isEditable()==true){
							cells[k][(int)x].setStyle("-fx-control-inner-background:snow;");
							}
					}
					
				}
		    	
		    }
		    	
		    }
		       
		}
		    
		    /**
		     * Determine the number of free cells from the interface.
		     * @param grid
		     * @return A 9*9 array that contains the positions of the empty cells in the grid.
		     */
		    public static int[][] getFreeCellList(int[][] grid) {
		       int numberOfFreeCells = 0;   
		        for (int i = 0; i < 9; i++)
		          for (int j = 0; j < 9; j++) 
		            if (grid[i][j] == 0) 
		              numberOfFreeCells++;
		        
		        /**Store free cell positions into freeCellList**/ 
		        int[][] freeCellList = new int[numberOfFreeCells][2];
		        int count = 0;
		        for (int i = 0; i < 9; i++)
		          for (int j = 0; j < 9; j++) 
		            if (grid[i][j] == 0) {
		              freeCellList[count][0] = i;
		              freeCellList[count++][1] = j;
		            }
		        
		        return freeCellList;
		      }

		     /**
		      * Displays the grid in the interface.
		      * @param grid
		      */
		      public static void printGrid(int[][] grid) {
		        for (int i = 0; i < 9; i++) {
		          for (int j = 0; j < 9; j++){
		            cells[i][j].setText(Integer.toString(grid[i][j]));
		            if (cells[i][j].isEditable()==true){
	    				cells[i][j].setStyle("-fx-control-inner-background:snow;");
	    			}else{
	    				cells[i][j].setStyle("-fx-control-inner-background:paleturquoise;");
	    			}
		          
		          
		        }
		      }
		      }

		      /** 
		       * Searches for a solution of the given puzzle. 
		       * Important note: All puzzles generated by this class can be solved since 
		       * the method for generating the game creates a solution and then creates 
		       * empty cells to be filled out. 
		       * @param grid
		       * @return true if a solution exists.
		       */
		      public static boolean search(int[][] grid) {
		        int[][] freeCellList = getFreeCellList(grid); // Free cells
		        if (freeCellList.length == 0) 
		          return true; // "No free cells");
		        
		        int k = 0; // Start from the first free cell      
		        while (true) {
		          int i = freeCellList[k][0];
		          int j = freeCellList[k][1];
		          if (grid[i][j] == 0) 
		            grid[i][j] = 1; // Fill the free cell with number 1

		          if (isValid(i, j, grid)) {
		            if (k + 1 == freeCellList.length) { // No more free cells 
		              return true; // A solution is found
		            }
		            else { // Move to the next free cell
		              k++;
		            }
		          }
		          else if (grid[i][j] < 9) {
		            // Fill the free cell with the next possible value
		            grid[i][j] = grid[i][j] + 1; 
		          }
		          else { // free cell grid[i][j] is 9, backtrack
		            while (grid[i][j] == 9) {
		              if (k == 0) {
		                return false; // No possible value
		              }
		              grid[i][j] = 0; // Reset to free cell
		              k--; // Backtrack to the preceding free cell
		              i = freeCellList[k][0];
		              j = freeCellList[k][1];
		            }

		            // Fill the free cell with the next possible value, 
		            // search continues from this free cell at k
		            grid[i][j] = grid[i][j] + 1; 
		          }
		        }
		      }

		      /** 
		       * Check whether grid[i][j] is valid in the grid.
		       * @param i row index of the number
		       * @param j column index of the number
		       * @param grid the entire 9*9 grid
		       * @return true if a number is valid in the given row, column and grid
		       */
		      public static boolean isValid(int i, int j, int[][] grid) {
		        // Check whether grid[i][j] is valid at the i's row
		        for (int column = 0; column < 9; column++)
		          if (column != j && grid[i][column] == grid[i][j])
		            return false;

		        // Check whether grid[i][j] is valid at the j's column
		        for (int row = 0; row < 9; row++)
		          if (row != i && grid[row][j] == grid[i][j])
		            return false;

		        // Check whether grid[i][j] is valid in the 3 by 3 box
		        for (int row = (i / 3) * 3; row < (i / 3) * 3 + 3; row++)
		          for (int col = (j / 3) * 3; col < (j / 3) * 3 + 3; col++)
		            if (!(row == i && col == j) && grid[row][col] == grid[i][j])
		              return false;

		        return true; // The current value at grid[i][j] is valid
		      }

		      /** 
		       * Checks whether the fixed cells are valid in the grid. 
		       * @param grid
		       * @return true if the number in the cell is valid
		       */
		      public static boolean isValid(int[][] grid) {
		        for (int i = 0; i < 9; i++)
		          for (int j = 0; j < 9; j++)
		            if (grid[i][j] < 0 || grid[i][j] > 9 ||
		               (grid[i][j] != 0 && !isValid(i, j, grid))) 
		              return false;

		        return true; // The fixed cells are valid
		      }
		    		  
		      /**
		       * Throws an exception if user input is not a number.
		       * @param grid
		       * @return true if user input can be converted to an integer. 
		       * false otherwise
		       */
		      boolean checkInput(int[][]grid){
		    	  for (int i=0;i<9;i++){
		    		  for (int j=0;j<9;j++){
		    			  try{
		    				  Integer.parseInt(cells[i][j].getText().trim());
		    			  }catch(NumberFormatException ex){

		    				  return false;
		    			  }
		    			  
		    		  }
		    	  }
		    	  return true;
		      
}
		      /**
		       * Check whether user input is a number between 1 and 9
		       * @param grid
		       * @return true if user input is between 1 and 9
		       */
		      boolean checkNumber(int grid[][]){
		    	  for (int i=0;i<9;i++){
		    		  for (int j=0;j<9;j++){
		    			  if (grid[i][j]<1 || grid[i][j]>9){
		    				  return false;
		    				 
		    			  }
		    		  }
		    	  }
		    	  return true;
		    	  
		      }


}

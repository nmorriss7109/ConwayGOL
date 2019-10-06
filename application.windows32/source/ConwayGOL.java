import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.Random; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class ConwayGOL extends PApplet {



static final int BACKGROUND = 0;        //BG Color
static final int CELL_SIZE = 10;        //Size of cells in pixels
static final int NUM_CELL_X = 100;      //How many cell we want in x direction
static final int NUM_CELL_Y = 100;      //       '          '        y   ' '
static final Boolean WRAP_EDGES = true; //Toggle edge wrapping
int brushSize = 1;                    //Brush size
int updateDelay = 10;       //Update delay in ms

int cellMatrix[][] = new int[NUM_CELL_X][NUM_CELL_Y];    //Make the matrix that will store our cell values

Boolean drawMode = false;
Boolean paused = true;

public void settings() {
  size(NUM_CELL_X*CELL_SIZE, NUM_CELL_Y*CELL_SIZE);
}

public void setup() {
  background(BACKGROUND);
  fillMat(cellMatrix, 0);         //fill our matrix with 0's
  //fillRand(cellMatrix, 800);    //uncomment to init with random values
}

//Fill a matrix with n's
public void fillMat(int[][] matrix, int n) {
  for (int i = 0; i < matrix.length; i++) {
    for (int j = 0; j < matrix[0].length; j++) {
      matrix[i][j] = n;
    }
  }
}
//Writes 1's in random places throughout cellMatrix
public void fillRand(int[][] matrix, int n) {
  for (int i = 0; i < n; i++) {
    int x = new Random().nextInt(NUM_CELL_X);
    int y = new Random().nextInt(NUM_CELL_X);
    int[] xy = {x, y};
    writecell(matrix, xy[0], xy[1], 1);
  }
}

//Writes a value to a certain desired cellel in the matrix
public void writecell(int[][] matrix, int x, int y, float val) {
  int cellVal = (int)map(val, 0, 1, 0, 255);
  //Checks to see if valid coord
  if (x >= 0 && y >= 0 && x < NUM_CELL_X && y < NUM_CELL_Y)
    matrix[x][y] = cellVal;
}

public int neighborCount(int x, int y) {
  int count = 0;
  for (int i = (WRAP_EDGES? -1 : (x>0 ? -1 : 0)); i <= (WRAP_EDGES? 1 : (x<NUM_CELL_X-1 ? 1 : 0)); i++) {
    for (int j = (WRAP_EDGES? -1 : (y>0 ? -1 : 0)); j <= (WRAP_EDGES? 1 : (y<NUM_CELL_Y-1 ? 1 : 0)); j++) {
      if (i != 0 || j != 0) {
        int a,b;
        if ((i+x)<0) a = NUM_CELL_X-1; else if ((i+x)>=NUM_CELL_X) a = 0; else a = (i+x);
        if ((j+y)<0) b = NUM_CELL_Y-1; else if ((j+y)>=NUM_CELL_X) b = 0; else b = (j+y);
        if (cellMatrix[(a)][(b)] > 0){
          count++; 
        }
      }
    }
  }
  return count;
}

public void updateState() {
  //We need to copy cellMatrix into a new array so that we can update vaules all at once
  int[][] cellMatrix2 = new int[NUM_CELL_X][NUM_CELL_Y];
  for (int i = 0; i < NUM_CELL_X; i++) {
    for (int j = 0; j < NUM_CELL_Y; j++) {
      cellMatrix2[i][j] = cellMatrix[i][j];
    }
  }
  //Iterate through each cellel
  for (int x = 0; x < NUM_CELL_X; x++) {
    for (int y = 0; y < NUM_CELL_Y; y++) {
      //Check cellels directly around the one we're focused on to count it's live neighbors
      int countLiveAdj = neighborCount(x,y);
      
      //===============================
      //RULES FOR CONWAY'S GAME OF LIFE
      if (countLiveAdj < 2 || countLiveAdj > 3) {
        writecell(cellMatrix2, x, y, 0); 
      } else if (countLiveAdj == 3) {
        writecell(cellMatrix2, x, y, 1); 
      }
      //===============================
      
    }
  }
  //This is where we update all the values at once
  cellMatrix = cellMatrix2;
}
//Enable drawing
public void mousePressed() {
  drawMode = true;
}
//Disable drawing
public void mouseReleased() {
  drawMode = false;
}

//Code for pausing and changing brush size
public void keyPressed() {
  if (key == ' ')
    paused = !paused; 
  
  if (key == '[' && brushSize > 1) 
    brushSize--; 
  
  if (key == ']' && brushSize < 50) 
    brushSize++; 
  
  if (key == 'x') 
    fillMat(cellMatrix, 0); 
 
  if (key == 's')
    updateDelay+=10;

  if (key == 'f' && updateDelay > 10)
    updateDelay-=10;
}

//Main Loop
public void draw() {
  //Draw the values from cellMatrix onto the screen
  for (int i = 0; i < NUM_CELL_X; i++) {
    for (int j = 0; j < NUM_CELL_Y; j++) {
      int pVal = cellMatrix[i][j];
      fill(pVal);
      rect(i*CELL_SIZE, j*CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }
  }
  //Mouse Drawing here
  if (drawMode) {
    int x = (int)(mouseX/CELL_SIZE);
    int y = (int)(mouseY/CELL_SIZE);
    int brush = (int)(brushSize/2);
    for (int i = 0; i <= brush; i++) {
      for (int j = 0; j <= brush; j++) {
        writecell(cellMatrix, x+i, y+j, 1);
      }
    }
  } 
  //Update if not paused
  if (!paused) {
     updateState();
     delay(updateDelay);
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "ConwayGOL" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}

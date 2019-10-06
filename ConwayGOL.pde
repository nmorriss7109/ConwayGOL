import java.util.Map;
import java.util.Random;

static final int SCREEN_SIZE_X = 500; //Height and width of the screen in screen pixels
static final int SCREEN_SIZE_Y = 500;
static final int BACKGROUND = 0;
static final int PIX_SIZE = 10;       //number of screen pixels per virtual game pixel
int pixMatrix[][] = new int[SCREEN_SIZE_X/PIX_SIZE][SCREEN_SIZE_Y/PIX_SIZE];

void setup() {
  size(SCREEN_SIZE_X, SCREEN_SIZE_Y);
  background(BACKGROUND);
  fillMat(pixMatrix, 0);
  randFill(pixMatrix, 10);
}

//Fill a matrix with n's
void fillMat(int[][] matrix, int n) {
  for (int i = 0; i < matrix.length; i++) {
    for (int j = 0; j < matrix[0].length; j++) {
      matrix[i][j] = 0;
    }
  }
}

void randFill(int[][] matrix, int n) {
  HashMap<String,Integer> hm = new HashMap<String,Integer>();
  for (int i = 0; i < n; i++) {
    int x = Math.randInt();
  }
}

//Writes a value to a certain desired pixel in the matrix
void drawPix(int[][] matrix, int x, int y, float val) {
  int pixVal = (int)map(val, 0, 1, 0, 255);
  matrix[x][y] = pixVal;
}

void draw() {
  for (int i = 0; i < pixMatrix.length; i++) {
    for (int j = 0; j < pixMatrix[0].length; j++) {
      int pVal = pixMatrix[i][j];
      fill(pVal);
      rect(i*PIX_SIZE, j*PIX_SIZE, PIX_SIZE, PIX_SIZE);
    }
  }
}

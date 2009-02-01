
package test;
        
class ShapeHelper{
    static int BOARD_WIDTH  = 5; 
    static int BOARD_HEIGHT = 60 / BOARD_WIDTH;
    static int BOARD_WIDTH_MASK = 31;//(int)Math.round(Math.pow(2, BOARD_WIDTH)) - 1;
    static int stringToShape(String str) {
        //Initiailse s by a 5*5 string
        //The hack for a 3*20/4*15 board to work is ensuring the width of this
        //shape does not exceed 3. See rotate() and constructor for Shape class for details
        int r = 0;
        int t = 1;
        for (int i=0;i<str.length();i++){
            if (i%5 >= BOARD_WIDTH) //We should meet empty cells in str now because the current
                continue;           //position exceeds the board's width.
            if ( i % 5 == 0 && i != 0 && BOARD_WIDTH>=5) //Encounter a new row so extra empty cells         e.g.   11100
                t = t<<(BOARD_WIDTH - 5);                //above the current rowns have to be filled.              110^^ 
                                                        //This only applies when BOARD_WIDTH >  5                  XXXXX
            if(str.charAt(i) == '1')
                r = r|t;
            t = t*2;
        }
        //print(r);
        return r;
    }
    static int getWidth(int s) {
        //find the width of the bounding box of s
        int maxW = -1;
        int w;
        for(int i=0;i<5;i++) {
            int l = (int)(s>>(BOARD_WIDTH*i))&BOARD_WIDTH_MASK;
            if ((l & 16) != 0) w = 5;
            else if ((l & 8) != 0) w = 4;
            else if ((l & 4) != 0) w = 3;
            else if ((l & 2) != 0) w = 2;
            else if ((l & 1) != 0) w = 1;
            else w  = 0;
            if (w > maxW ) maxW = w;
        }
        return maxW;
    }
    static int getHeight(int s) {
        //find the height of the bounding box of s
        int maxH = -1;
        int w = 0;
        for(int i=0;i<BOARD_WIDTH;i++) {
            int l = (int)(
                    (((s>>(BOARD_WIDTH*0+i))&1)*1)|
                    (((s>>(BOARD_WIDTH*1+i))&1)*2)|
                    (((s>>(BOARD_WIDTH*2+i))&1)*4)|
                    (((s>>(BOARD_WIDTH*3+i))&1)*8)|
                    (((s>>(BOARD_WIDTH*4+i))&1)*16)
                    );
            if ((l & 16) != 0) w = 5;
            else if ((l & 8) != 0) w = 4;
            else if ((l & 4) != 0) w = 3;
            else if ((l & 2) != 0) w = 2;
            else if ((l & 1) != 0) w = 1;
            else w = 0;
            if (w > maxH ) maxH = w;
        }
        return maxH;
    }
    static int rotate(int s) {
        //rotate s clockwise for 90 degrees
        int w = getWidth(s);
        int h = getHeight(s);
        if (h > BOARD_WIDTH) //HACK: after rotation this would be a invalid shape.
            return 0;
        int r = 0;
        for(int i=0;i<h;i++)
            for(int j=0;j<w;j++)
                if ((s>>(BOARD_WIDTH*i+j)) % 2 == 1)
                r |= (((int)1)<<(BOARD_WIDTH*(w-j-1)+i));
        //System.out.println("rotate:"+w+" "+h+" " + s + " " +r);
        return r;
    }
    static int flipX(int s) {
        //Flip s along the horizontal axis
        int h = getHeight(s);
        int r = 0;
        for (int i=0;i<h;i++)
            r |= ((s >> (BOARD_WIDTH*i))&BOARD_WIDTH_MASK)<<(BOARD_WIDTH*(h-i-1));
       // System.out.println("flip:" + s + " " +r);
        return r;
    }
    static int flipY(int s) {
        //Flip s along the vertical axis
        int w = getWidth(s);
        int h = getHeight(s);
        int r = 0;
        for(int i=0;i<h;i++)
            for(int j=0;j<w;j++)
                if ((s>>(BOARD_WIDTH*i+j)) % 2 == 1)
                r |= (((int)1)<<(BOARD_WIDTH*i+w-1-j));
        return r;
    }
    static int shiftRight(int s) {
        //shift s to its right for 1 cell's distance
        int r = 0;
        for(int i=0;i<5;i++)
            r |= ((s>>(i*BOARD_WIDTH))&BOARD_WIDTH_MASK)<<1 <<(i*BOARD_WIDTH);
        return r;
    }
    static int topEdgeLen(int s) {
        //Get the number of leftmost consecutive blocks at the top edge. 
        //assert(s != 0);
        int r = 0;
        s = s & BOARD_WIDTH_MASK;
        while ((s&1) == 0) s = s >> 1;
        while ((s&1) == 1) {
            r ++;
            s = s >> 1;
        }
        return r;
    }  
    static int topEdgeStartPos(int s) {
        //get the starting position of the leftmost block at the top edge
        //assert(s != 0);
        s = s & BOARD_WIDTH_MASK;
        int r = 0;
        while (s % 2 == 0) { 
            r++; s = s /2 ;}
        return r;
    }
    static void print(long s) {
        for (int i=0;i<BOARD_HEIGHT;i++){
            for(int j=0;j<BOARD_WIDTH;j++){
                if (s % 2 == 0)
                    System.out.print('.');
                else System.out.print('*');
                s =s / 2;
            }
           System.out.println();
        }
    }
    static void fillBoard(char[] board, int s, int x, int y, char c) {
        for(int i=0;i<getHeight(s);i++)
            for(int j=0;j<getWidth(s);j++)
                if ((s>>(BOARD_WIDTH*i+j)) % 2 == 1)
                   board[(i+x)*BOARD_WIDTH+j+y] = c;
    }
}
class ShapeInfo{
    //A 5*5 piece can be stored as a 32-bit integer number.
    //int shape;
    int shape_at_offset[];
    int w;
    int h;
    int topEdgeLen;
    int topEdgePos;
    int MaxStartPos; //Maximal position for the top edge to be placed
}
class Shape{
    //Class for a specific pentomino containing all the flipped and 
    //rotated variations of the given pentomino.
    int variationCount;
    int BOARD_WIDTH, BOARD_HEIGHT;
    ShapeInfo[] variations;
    boolean duplicatedVariation(int s){
    //Check is s exists in variation
        for(int i=0;i<variationCount;i++)
            if (variations[i].shape_at_offset[0] == s)
                return true;
        return false;
    }
    Shape(String shapeStr) {
        BOARD_WIDTH = ShapeHelper.BOARD_WIDTH;
        BOARD_HEIGHT = ShapeHelper.BOARD_HEIGHT;
        variationCount = 1;
        variations = new ShapeInfo[8];
        int s;
        s = ShapeHelper.stringToShape(shapeStr);
        variations[0] = new ShapeInfo();
        //variations[0].shape = s;
        variations[0].w     = ShapeHelper.getWidth(s); 
        variations[0].h     = ShapeHelper.getHeight(s);
        variations[0].topEdgeLen     = ShapeHelper.topEdgeLen(s);
        /*                         
        System.out.println(variations[0].w);
        System.out.println(variations[0].h);
        ShapeHelper.print(s);
        System.out.println(ShapeHelper.topEdgeLen(s));
        System.out.println(ShapeHelper.topEdgeStartPos(s));
        */
        variations[0].topEdgePos     = ShapeHelper.topEdgeStartPos(s);
        variations[0].MaxStartPos =  BOARD_WIDTH - variations[0].w + variations[0].topEdgePos;
        variations[0].shape_at_offset = new int[BOARD_WIDTH  - variations[0].w+1];
        variations[0].shape_at_offset[0] = s;
        for (int k=1;k< variations[0].shape_at_offset.length;k++) 
            variations[0].shape_at_offset[k] = ShapeHelper.shiftRight(
                                variations[0].shape_at_offset[k-1]);
        
        for( int j=0;j<2;j++){ //Loop to generate flipped shape based on s
        for (int i=0;i<4;i++) {//Loop to generate rotated shape based on s
            if (ShapeHelper.rotate(s) != 0) 
                s = ShapeHelper.rotate(s);
            else   //HACK: For smaller board, rotating s for 90 degree would generate a invalid piece
                   //but rotating it 180 degree is fine. 
                s = ShapeHelper.flipY(s);  
            if (s != 0 && !duplicatedVariation(s)) {//Found distinct&valid shape
                /*      
                ShapeHelper.print(s);
                System.out.println(ShapeHelper.topEdgeLen(s));
                System.out.println(ShapeHelper.topEdgeStartPos(s));
                */        
                variations[variationCount] = new ShapeInfo();
                //variations[variationCount].shape = s;
                variations[variationCount].w     = ShapeHelper.getWidth(s);
                variations[variationCount].h     = ShapeHelper.getHeight(s);
                variations[variationCount].topEdgeLen  = ShapeHelper.topEdgeLen(s);
                variations[variationCount].topEdgePos     = ShapeHelper.topEdgeStartPos(s);
                variations[variationCount].shape_at_offset = new int[1+BOARD_WIDTH-ShapeHelper.getWidth(s)];
                variations[variationCount].shape_at_offset[0] = s;
                variations[variationCount].MaxStartPos =  BOARD_WIDTH  - variations[variationCount].w + variations[variationCount].topEdgePos;
                for (int k=1;k<variations[variationCount].shape_at_offset.length;k++) 
                    variations[variationCount].shape_at_offset[k] = ShapeHelper.shiftRight(
                                        variations[variationCount].shape_at_offset[k-1]);
              variationCount ++;
            }
        }
        s = ShapeHelper.flipX(s);
        }
        
        //Sort in ascending order of topEdgeLen
        for(int i=1; i<variationCount; i++)
            for(int j=i;j>0;j--)
                if (variations[j].topEdgeLen  < variations[j-1].topEdgeLen) {
                    ShapeInfo t = variations[j];
                    variations[j] = variations[j-1];
                    variations[j-1] = t;
                }
        /*
        System.out.println("Shape Created:");
        for(int i=0; i<variationCount; i++)
            for(int j=0;j<variations[i].shape_at_offset.length;j++)
                ShapeHelper.print(variations[i].shape_at_offset[j]);
        */ 
        return;
    }
}
 
    
public class Pentomino {
    static int BOARD_WIDTH = ShapeHelper.BOARD_WIDTH;
    static int BOARD_HEIGHT = ShapeHelper.BOARD_HEIGHT;
    //static long board = 0;
    //static char solution[];
    static int lineStartPos[],lineEmptyLen[];
    static int dfs(Shape[] shape, long board, int line, int r){
    //DO dfs on board row by row, from left to right, r indicates the remaining 
    //number of available pentominoes
    //On Entry (line) is the first POSSIBLE row that MAY still be empty.
    //Note there is no guarantee that this row is always empty so additonal 
    //probe is still required.
        //System.out.println(line);
        //ShapeHelper.print(board);
        if (line  == BOARD_HEIGHT) {
        	//System.out.print(".");
            return 1;
            /*
            System.out.println(board);
                for (int i=0;i<5;i++){
                    for(int j=0;j<12;j++)
                          System.out.print(solution[i*12+j]);
                   System.out.println();
                }
                   System.exit(0);
             */
        }
        int s = 0, emptyStartPos  , emptyLen , curLine;
        s = (int)(board>>(BOARD_WIDTH*line));
        curLine = s & ShapeHelper.BOARD_WIDTH_MASK;
        //Fetch precomputed information about curLine.
        emptyStartPos = lineStartPos[curLine];
        emptyLen = lineEmptyLen[curLine];
        
        //System.out.println("empty cell probe:"+emptyStartPos+" "+emptyLen);
        if (emptyStartPos == -1){ //There is no empty space on this line
            return dfs(shape, board, line+1, r);    //Start search on next line.
        }
        
        
        //Now a consecutive number(emptyLen) of empty cells found starting at emptyStartPos
        //Try to fill available pentominoes into this space
        int solutions = 0;
        for(int i=0;i<r;i++){
            //Obtain the current shape and move it to the rear of the arrary
            //so when a recursive dfs is called it will not be picked up again.
            Shape curShape = shape[i];
            shape[i] = shape[r-1];
            shape[r-1] = curShape;
            for(int j=0;j<curShape.variationCount;j++) {
                //Try shape[i].variation[j];
                ShapeInfo si = curShape.variations[j];
               
                if (emptyLen < si.topEdgeLen) //this piece is too big, empty slot too small
                        //Also, no need to try other variaitons for this piece since 
                        //variations[] are sorted in ascending order of topEdgeLen
                {break;}
                if (emptyStartPos > si.MaxStartPos ) //Right boundary exceeded
                {continue;}
                if (emptyStartPos < si.topEdgePos) //Left boundary exceeded
                {continue;}
                if (line + si.h > BOARD_HEIGHT) //exceeding height
                {continue;}
 
                //Try ti fit this block into the board at 
                //position (line, emptyStartPos -  si.topEdgePos)
                if ((s&si.shape_at_offset[emptyStartPos -  si.topEdgePos])!=0)
                // There is some overlap between si and board
                    continue;
                //Now it passes all tests. fit it and dfs recursively.
                long oldboard = board;
                //char[] olds = (char[])solution.clone();
                //ShapeHelper.fillBoard(solution,si.shape, line, emptyStartPos -  si.topEdgePos , (char)(65+i));
                board |= ((long)si.shape_at_offset[emptyStartPos -  si.topEdgePos] <<(BOARD_WIDTH*line));
                if (emptyStartPos + si.topEdgeLen == BOARD_WIDTH) 
                    solutions += dfs(shape, board, line+1, r-1);
                else
                    solutions += dfs(shape, board, line, r-1);
                board = oldboard;
                //solution = olds;
            }
            //Restore curShape to its original position
            shape[r-1] = shape[i];
            shape[i] = curShape;
        }
        return solutions;
    }

    public static void main(String[] args) {
        Shape[] shape = new Shape[12];
        long t1, t2, t3,t4 = 0, t5 = 0;
        t1 = System.currentTimeMillis();
       shape[0] =new Shape("10000" + 
                            "10000" + 
                            "10000" + 
                            "10000" + 
                            "10000");
        
        shape[1] =new Shape("10000" + 
                            "10000" + 
                            "10000" + 
                            "11000" + 
                            "00000");
        if (BOARD_WIDTH >3)
            shape[1].variationCount = 2; //Consider only 2 possible vairaitons for L-shaped
                                         //piece to avoid duplication.
        else 
            shape[1].variationCount = 1; //HACK:If the board is too small, only one
                                         //possibility is available.
        shape[2] =new Shape("10000" + 
                            "10000" + 
                            "11000" + 
                            "10000" + 
                            "00000");
        shape[3] =new Shape("01000" + 
                            "11100" + 
                            "10000" + 
                            "00000" + 
                            "00000");
        shape[4] =new Shape("11100" + 
                            "00100" + 
                            "00100" + 
                            "00000" + 
                            "00000");
        shape[5] =new Shape("11100" + 
                            "01000" + 
                            "01000" + 
                            "00000" + 
                            "00000");
        shape[6] =new Shape("11000" + 
                            "11000" + 
                            "01000" + 
                            "00000" + 
                            "00000");
        shape[7] =new Shape("01000" + 
                            "11100" + 
                            "01000" + 
                            "00000" + 
                            "00000");
        shape[8] =new Shape("00100" + 
                            "01100" + 
                            "11000" + 
                            "00000" + 
                            "00000");
        shape[9] =new Shape("10000" + 
                            "10000" + 
                            "11000" + 
                            "01000" + 
                            "00000");
        shape[10]=new Shape("11100" + 
                            "10100" + 
                            "00000" + 
                            "00000" + 
                            "00000");
        shape[11]=new Shape("10000" + 
                            "11100" + 
                            "00100" + 
                            "00000" + 
                            "00000");
        for (int i=0;i<12;i++)
            for(int j=i;j>0;j--)
                if(shape[j].variationCount < shape[j-1].variationCount)
                {
                    Shape s = shape[j];  shape[j] = shape[j-1];  shape[j-1] = s;        
                }
        lineStartPos = new int[ShapeHelper.BOARD_WIDTH_MASK+1];
        lineEmptyLen = new int[ShapeHelper.BOARD_WIDTH_MASK+1];
        //The following code probes the curLine to find 
        // a. the index of the first empty cell.
        // b. the number of consecutive empty cells after that.
        // Here I precompute them to reduce the cost of repeatly calculating them in dfs().
        int mask[] = {1,2,4,8,16,32};
        for(int curLine=0; curLine <= ShapeHelper.BOARD_WIDTH_MASK; curLine++){
            int emptyStartPos = -1 , emptyLen = 1;
            for(int j=0;j<BOARD_WIDTH;j++)
                if ((curLine&mask[j]) == 0 ) {//Found a empty cell 
                    if (emptyStartPos == -1 ) //First empty cell
                        emptyStartPos = j;
                    else                    //Find consecutive empty cells
                        emptyLen ++;
                } else  //Find a filled cell
                    if (emptyStartPos != -1) //Already found consecutive empty cells 
                           break;            //previously, so now time to jump out.
            lineStartPos[curLine] =    emptyStartPos;
            lineEmptyLen[curLine] =    emptyLen;
        }
        
        int solutions;
        solutions = dfs(shape, 0, 0, 12);
        t2 = System.currentTimeMillis();

        System.out.println("Number of solutions: " + solutions);
        System.out.println("Time: " + (t2-t1) + " ms");
    }

}

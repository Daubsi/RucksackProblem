public class RucksackMain {

    // Modeling space with an array
    // Size of space
    static int lengthOfSpace = 30;
    static int widthOfSpace  = 30;
    static int[][] Space = new int[widthOfSpace+2][lengthOfSpace+2];

    // List of the given packages
    static Paket[] packageList = new Paket[20];

    // testMode saves on some print Statements
    static boolean testMode = false;

    static int[] placingPoint = {1, 1};

    static boolean rotation;

    // General Inforamtion for later
    static int nPlacedPackages = 0;
    static int nSkippedPackages = 0;
    static int valueOfAllPackages = 0;
    static int areaOfAllPackages = 0;
    static int valueOfPlacedPackages = 0;
    static int areaOfPlacedPackages = 0;

    // Creates random sample packages
    static void prepareSamples(Paket[] packageList, int min, int max){
        for(int i = 0; i < packageList.length; i++){
            int name = i + 1;
            int value  = (int)(Math.random()*100)+1;
            int width  = (int)(Math.random()*10)+1;
            int length = (int)(Math.random()*10)+1;
            packageList[i] = new Paket(name, value, width, length);
        }
    }
    
    // Fills the Array with 0's except the borders they get filled with 1's
    static void preapareSpace(int [][] space){
        for (int i = 0; i < space.length; i++) {
            for (int j = 0; j < space[i].length; j++) {
                // If at a border fill 1 else fill 0
                if((i == space.length-1 || i == 0) || (j == space[i].length-1 || j == 0)){
                    space[i][j] = 1;
                }
                else space[i][j] = 0;
            }
        }
    }

    // Prints the space array in the console
    static void printSpace(int[][] space, int max){
        int Spaces = 4;
        if(max > 9) Spaces = 3;
        if(max > 99) Spaces = 2;
        if(max > 999) Spaces = 1;
        
        for(int i = 1; i < space.length-1; i++){
            for(int j = 1; j < space[i].length-1; j++){
                int spaceNeeded = Spaces;
                String printMessage = "";
                if(space[i][j] < 1000) spaceNeeded = Spaces - 2;
                if(space[i][j] < 100 ) spaceNeeded = Spaces - 1;
                if(space[i][j] < 10  ) spaceNeeded = Spaces - 0;
                
                for(int nSpaces = 0; nSpaces < spaceNeeded; nSpaces++){
                    printMessage += " ";
                }
                System.out.print(space[i][j] + printMessage + " ");
            }
        System.out.println();
        }
        System.out.println();
    }

    // Checks the amount of 1's sourrounding a Cell
    static int checkSourroundings(int[][] space, int xValue, int yValue){
        int nFreeCells = 0;
        if(space[yValue+1][xValue  ]==0) nFreeCells++;
        if(space[yValue-1][xValue  ]==0) nFreeCells++;
        if(space[yValue  ][xValue+1]==0) nFreeCells++;
        if(space[yValue  ][xValue-1]==0) nFreeCells++;
        return nFreeCells;
    }

    // Sorts the Packages according to the attribut ValuePerArea
    static void sortPackages(Paket[] packages){
        int tallest;
        Paket temp;
        for(int a = 0; a < packages.length - 1; a++) {
            tallest = a;
            for(int b = a + 1; b < packages.length; b++)
                if(packages[b].getValuePerArea() > packages[tallest].getValuePerArea())
                tallest = b;
            temp = packages[a];
            packages[a] = packages[tallest];
            packages[tallest] = temp;
        }
    }


    // Checks the amount of available spaces around a package
    static int freeCellsAroundPackage(int[][] space, int currentPaketLength, int currentPaketWidth, int[] startPoint){
        fillSpace(space, currentPaketLength, currentPaketWidth, startPoint, true);
        int nAllAvailableCells = 0;
        for(int i = startPoint[0]; i < startPoint[0] + currentPaketLength && i < space.length-1; i++){
            for (int j = startPoint[1]; j < startPoint[1] + currentPaketWidth && j < space.length-1; j++){
                nAllAvailableCells += checkSourroundings(space, j, i);
            }
        }
        fillSpace(space, currentPaketLength, currentPaketWidth, startPoint, false);
        return nAllAvailableCells;
    }

    // Fills the area of a package with 0's or 1's in the space array
    static void fillSpace(int[][] space, int length, int width, int[] startPoint, boolean fill){
        for(int i = startPoint[0]; i < startPoint[0] + length && i < space.length-1; i++){
            for (int j = startPoint[1]; j < startPoint[1] + width && j < space.length-1; j++){
                
                if(fill)  space[i][j] = 1;
                else      space[i][j] = 0;
            }
        }
    }


    // Checking if the package would fit in both orientations
    static boolean[] doesFit(int[][] space, Paket currentPaket, int[] startPoint){
        boolean orientationA = true;
        boolean orientationB = true;
        
        for(int i = startPoint[0]; i < startPoint[0] + currentPaket.length; i++){
            for (int j = startPoint[1]; j < startPoint[1] + currentPaket.width; j++){
                if (i <= widthOfSpace && j <= lengthOfSpace){
                    if(space[i][j] != 0) orientationA = false;
                }else orientationA = false;   
            }
        }

        for(int i = startPoint[0]; i < startPoint[0] + currentPaket.width; i++){
            for (int j = startPoint[1]; j < startPoint[1] + currentPaket.length; j++){
                if(i <= widthOfSpace && j <= lengthOfSpace){
                    if (space[i][j] != 0) orientationB = false;
                }else orientationB = false;
            }
        }
        // Returning the orientations that fit
        return new boolean[] {orientationA, orientationB};
    }

    // Prints a package in the Space Array
    static void printPackage(int[][] space, Paket currentPaket, int[] startPoint, boolean rotate){
        if(rotate){
            for(int i = startPoint[0]; i < startPoint[0] + currentPaket.width; i++){
                for (int j = startPoint[1]; j < startPoint[1] + currentPaket.length; j++){
                    space[i][j] = currentPaket.name;
                }
            }
        }else{
            for(int i = startPoint[0]; i < startPoint[0] + currentPaket.length; i++){
                for (int j = startPoint[1]; j < startPoint[1] + currentPaket.width; j++){
                    space[i][j] = currentPaket.name;
                }
            }
        }
    }

    static void printPackageInfo(Paket currentPaket){
        System.out.println(currentPaket.toString());
    }

    // Counts the amount of empty Cells
    static int emptyCells(int[][] space){
        int emptyCells = 0;
        for(int i = 1; i < space.length-1; i++){
            for(int j = 1; j < space[i].length-1; j++){
                if(space[i][j] == 0)
                    emptyCells ++;
            }
        }
        return emptyCells;
    }

    static void endInfo(){
        System.out.println("Finale Lösung:");
        printSpace(Space, packageList.length);
        System.out.println("Menge an Platzierten Paketen:                             " + nPlacedPackages);
        System.out.println("Menge an Übersprungenen Paketen:                          " + nSkippedPackages);
        System.out.println("Wert von allen Paketen:                                   " + valueOfAllPackages);
        System.out.println("Größe von allen Paketen:                                  " + areaOfAllPackages);
        System.out.println("Wert von allen platzierten Paketen:                       " + valueOfPlacedPackages);
        System.out.println("Größe von allen platzierten Paketen:                      " + areaOfPlacedPackages);
        System.out.println("Gewichts/Größenverhältniss von allen Paketen:             " + (float)valueOfAllPackages/areaOfAllPackages);
        System.out.println("Gewichts/Größenverhältniss von allen platzierten Paketen: " + (float)valueOfPlacedPackages/areaOfPlacedPackages);
        System.out.println("Anzahl an Zellen                                          " + lengthOfSpace*widthOfSpace);
        System.out.println("Anzahl an Freie Zellen:                                   " + emptyCells(Space));
        System.out.println("Prozentsatz an freien Zellen:                             " + (float)emptyCells(Space)/(lengthOfSpace*widthOfSpace)*100 + " %");
    }

    public static void main(String[] args){
        prepareSamples(packageList, 100, 100);
        preapareSpace(Space);
        sortPackages(packageList);

        for(int e = 0; e < packageList.length; e++){
            valueOfAllPackages += packageList[e].value;
            areaOfAllPackages += packageList[e].getArea();

            int mostFreeCells = 1000;
            int[] bestPlacingPosition = {-1,0};
            boolean rotation = false;

            for (int i = 1; i < Space.length-1; i++) {
                for (int j = 1; j < Space[i].length-1; j++) {
                    
                    int[] currentPosition = {i, j};
                    // If the given point is empty
                    if(Space[i][j]==0){
                        // Checking the better Rotation
                        if(doesFit(Space, packageList[e], currentPosition)[0]){
                            if(freeCellsAroundPackage(Space, packageList[e].length, packageList[e].width, currentPosition) <
                                mostFreeCells){
                                bestPlacingPosition = currentPosition;
                                rotation = false;
                                mostFreeCells = freeCellsAroundPackage(Space, packageList[e].length, packageList[e].width, currentPosition);
                            }
                        }else if(doesFit(Space, packageList[e], currentPosition)[1]){
                            if(freeCellsAroundPackage(Space, packageList[e].width, packageList[e].length, currentPosition) <
                                mostFreeCells){
                                bestPlacingPosition = currentPosition;
                                rotation = true;
                                mostFreeCells = freeCellsAroundPackage(Space, packageList[e].length, packageList[e].width, currentPosition);
                            }
                        }
                    }
                }
            }


            if(bestPlacingPosition[0] != -1){
                printPackage(Space, packageList[e], bestPlacingPosition, rotation);

                nPlacedPackages ++;
                valueOfPlacedPackages += packageList[e].value;
                areaOfPlacedPackages += packageList[e].getArea();

            }else{
                if(!testMode)
                System.out.println("Dieses Paket konnte nicht platziert werden:");
                nSkippedPackages ++;
            }
            
            if(!testMode){
                printPackageInfo(packageList[e]);
                printSpace(Space, packageList.length);
            }
                
        }

        endInfo();

        

    }
}
public class RucksackMain {

    // Modeling space with an array
    // Size of space
    static int lengthOfSpace = 100;
    static int widthOfSpace  = 100;
    static int[][] Space = new int[widthOfSpace+2][lengthOfSpace+2];

    // List of the given packages
    static Paket[] packageList = new Paket[100];

    static boolean testMode = true;

    static int[] placingPoint = {0, 0};

    static boolean rotation;

    static int nPlacedPackages = 0;
    static int nSkippedPackages = 0;
    static int valueOfAllPackages = 0;
    static int areaOfAllPackages = 0;
    static int valueOfPlacedPackages = 0;
    static int areaOfPlacedPackages = 0;

    static void prepareSamples(Paket[] packageList, int min, int max){
        for(int i = 0; i < packageList.length; i++){
            int name = i + 1;
            int value  = (int)(Math.random()*100)+1;
            int width  = (int)(Math.random()*10)+1;
            int length = (int)(Math.random()*10)+1;
            packageList[i] = new Paket(name, value, width, length);
        }
    }

    // Y/X Coordinate of the point to continue placing packages from
    //static int[] startPoint = {0,0};
    
    // Fills the Array with 0's except the borders they get filled with 1's
    static void preapareSpace(int [][] space){
        for (int i = 0; i < space.length; i++) {
            for (int j = 0; j < space[i].length; j++) {
                if((i == space.length-1 || i == 0) || (j == space[i].length-1 || j == 0)){
                    space[i][j] = 1;
                }
                else space[i][j] = 0;
            }
        }
    }

    // Prints the space array in the console
    static void printSpace(int[][] space){
        for(int i = 1; i < space.length-1; i++){
            for(int j = 1; j < space[i].length-1; j++){
                System.out.print(space[i][j] + " ");
            }
        System.out.println();
        }
        System.out.println();
    }

    // Checks the amount of 1's sourrounding a Cell
    static int checkSourroundings(int[][] space, int xValue, int yValue){
        int nOccupiedCells = 0;
        if(space[yValue+1][xValue  ]==1) nOccupiedCells++;
        if(space[yValue-1][xValue  ]==1) nOccupiedCells++;
        if(space[yValue  ][xValue+1]==1) nOccupiedCells++;
        if(space[yValue  ][xValue-1]==1) nOccupiedCells++;
        return nOccupiedCells;
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

    // Finds the point where to continue placing packages
    static int[] placePackage(int[][] space, Paket currentPaket){

        int mostOccupiedCells = 0;
        int[] bestPlacingPosition = {0,0};
        boolean rotation = false;

        for (int i = 1; i < space.length-1; i++) {
            for (int j = 1; j < space[i].length-1; j++) {
                int[] currentPosition = {i, j};
                if(space[i][j]==0){
                    if(doesFit(space, currentPaket, currentPosition)[0]){
                        if(occupiedCellsAroundPackage(space, currentPaket.length, currentPaket.width, currentPosition) >
                            mostOccupiedCells){
                            bestPlacingPosition = currentPosition;
                            rotation = false;
                            break;
                        }
                    }else if(doesFit(space, currentPaket, currentPosition)[1]){
                        if(occupiedCellsAroundPackage(space, currentPaket.width, currentPaket.length, currentPosition) >
                            mostOccupiedCells){
                            bestPlacingPosition = currentPosition;
                            rotation = true;
                            break;
                        }
                    }
                }
            }
        }
        // if kein platz gefunden
        // gebe -1 aus
        if(bestPlacingPosition[0] == 0){
            bestPlacingPosition[0] = -1;
        }

        int[] returnList = {0,0,0,0};

        // wenn kein fehler
        // gebe 1 aus
        if(bestPlacingPosition[0] != -1)
            returnList[0] = 1;

        // rotation als 0 oder 1 an stelle 1 speichern
        if(rotation) returnList[1] = 1;
        else returnList[1] = 0;

        returnList[2] = bestPlacingPosition[0];
        returnList[3] = bestPlacingPosition[1];

        // stelle 0: ob es passt oder nicht
        // stelle 1: drehen ja oder nein
        // stelle 2,3: koordinaten des punktes
        return returnList;
    }

    // Checks the amount of available spaces around a package
    static int occupiedCellsAroundPackage(int[][] space, int currentPaketLength, int currentPaketWidth, int[] startPoint){
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

    // Checking if the given orientation is good
    static boolean evaluateOrientation(int[][] space, Paket currentPaket, int[] startPoint){
        if(occupiedCellsAroundPackage(space, currentPaket.length, currentPaket.width, startPoint) < 
           occupiedCellsAroundPackage(space, currentPaket.width, currentPaket.length, startPoint)){
              return false;
        }else return true;
    }

    // Checking if the package would fit in both orientations
    static boolean[] doesFit(int[][] space, Paket currentPaket, int[] startPoint){
        boolean orientationA = true;
        boolean orientationB = true;
        
        for(int i = startPoint[0]; i < startPoint[0] + currentPaket.length; i++){
            for (int j = startPoint[1]; j < startPoint[1] + currentPaket.width; j++){
                if (i <= widthOfSpace && j <= lengthOfSpace){
                    if(space[i][j] == 1) orientationA = false;
                }else orientationA = false;   
            }
        }

        for(int i = startPoint[0]; i < startPoint[0] + currentPaket.width; i++){
            for (int j = startPoint[1]; j < startPoint[1] + currentPaket.length; j++){
                if(i <= widthOfSpace && j <= lengthOfSpace){
                    if (space[i][j] == 1) orientationB = false;
                }else orientationB = false;
            }
        }

        return new boolean[] {orientationA, orientationB};
    }

    static void printPackage(int[][] space, Paket currentPaket, int[] startPoint, boolean rotate){
        if(rotate){
            for(int i = startPoint[0]; i < startPoint[0] + currentPaket.width; i++){
                for (int j = startPoint[1]; j < startPoint[1] + currentPaket.length; j++){
                    space[i][j] = 1;
                }
            }
        }else{
            for(int i = startPoint[0]; i < startPoint[0] + currentPaket.length; i++){
                for (int j = startPoint[1]; j < startPoint[1] + currentPaket.width; j++){
                    space[i][j] = 1;
                }
            }
        }
    }

    static void printPackageInfo(Paket currentPaket){
        System.out.println(currentPaket.toString());
    }

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
        printSpace(Space);
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

        for(int i = 0; i < packageList.length; i++){
            valueOfAllPackages += packageList[i].value;
            areaOfAllPackages += packageList[i].getArea();
            // if Paket passt
            if(placePackage(Space, packageList[i])[0] == 1){

                placingPoint[0] = placePackage(Space, packageList[i])[2];
                placingPoint[1] = placePackage(Space, packageList[i])[3];

                if(placePackage(Space, packageList[i])[1] == 1)
                    rotation = true;
                else rotation = false;
                    printPackage(Space, packageList[i], placingPoint, rotation);

                nPlacedPackages ++;
                valueOfPlacedPackages += packageList[i].value;
                areaOfPlacedPackages += packageList[i].getArea();

            }else{
                if(!testMode)
                System.out.println("Dieses Paket konnte nicht platziert werden:");
                nSkippedPackages ++;
            }
            
            if(!testMode){
                printPackageInfo(packageList[i]);
                printSpace(Space);
            }
                
            
        }

        endInfo();
    }
}
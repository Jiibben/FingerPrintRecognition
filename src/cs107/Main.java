package cs107;

import java.util.ArrayList;
import java.util.List;


/**
 * This class will not be graded. You can use it to test your program.
 */
public class Main {

    /**
     * Main entry point of the program.
     *
     * @param args the command lines arguments of the program.Configure
     */
    public static void main(String[] args) {

        //---------------------------
        // Tests functions separately
        //---------------------------
        System.out.println("Uncomment the function calls in Main.main to test your implementation.");
        System.out.println("The provided tests are not complete. You have to write your own tests.");
// -----------------------------> works <-----------------------------------------------------
//        testExtractSpe();
//        testExtract();
//        testSlope();
//        testAngle();
//        testGetNeighbours();
//        testBlackNeighbours();
//        testTransition();
//        testIdentical();
//        testConnectedPixelsF();
//        testConnectedPixels2();
//        testConnectedPixels3();
//        testOrientation();
//        testApplyTranslation();
//        testThin();
//        testWithSkeleton();
//        testApplyRotation();
//         testDrawSkeleton("1_1"); //draw skeleton of fingerprint 1_1.png
//         testDrawSkeleton("1_2"); //draw skeleton of fingerprint 1_2.png
//         testDrawSkeleton("2_1"); //draw skeleton of fingerprint 2_1.png
//
//         testDrawMinutiae("1_1"); //draw minutiae of fingerprint 1_1.png
//         testDrawMinutiae("1_6"); //draw minutiae of fingerprint 1_1.png
//
//         testDrawMinutiae("1_5"); //draw minutiae of fingerprint 1_2.png
//         testDrawMinutiae("2_1"); //draw minutiae of fingerprint 2_1.png
//         testApplyRotation();
//      testExtract2();
//        testFinger("1_1", "1_6", true);
//         testFinger("2_1", "2_1", true);
//         testAllPossibleFingerprints();
//        for (int f = 3; f <= 16; f++) {
//            testCompareAllFingerprints("1_1", f, false);
//        }
//
    }

    public static void testExtractSpe() {
        int[] m2 = {194, 62, 74};
        int[] m1 = {221, 29, 75};
        int rowTranslation = m2[0] - m1[0];
        int colTranslation = m2[1] - m1[1];
        int centerRow = m1[0];
        int centerCol = m1[1];
        System.out.print(rowTranslation + " ");
        System.out.print(colTranslation + " ");
        System.out.print(centerCol + " ");
        System.out.print(centerRow + " ");
        System.out.println("");
        List<int[]> minutiae1 = Fingerprint.extract(Fingerprint.thin(Helper.readBinary("resources/fingerprints/1_6.png")));
        List<int[]> minutiae2 = Fingerprint.extract(Fingerprint.thin(Helper.readBinary("resources/fingerprints/1_1.png")));
        // printMinutiae(minutiae1);
        System.out.println("len1a" + minutiae1.size());
        System.out.println("len2a" + minutiae2.size());

        List<int[]> modifiedMinutiae2 = Fingerprint.applyTransformation(minutiae2, centerRow, centerCol, rowTranslation, colTranslation, 0);

        System.out.println(Fingerprint.matchingMinutiaeCount(minutiae1, modifiedMinutiae2, Fingerprint.DISTANCE_THRESHOLD, Fingerprint.ORIENTATION_THRESHOLD));
    }


    public static void testExtractSpecial(int[] m1, int[] m2, String finger1name, String finger2name, int rotation) {
        int rowTranslation = m2[0] - m1[0];
        int colTranslation = m2[1] - m1[1];
        int centerRow = m1[0];
        int centerCol = m1[1];
        System.out.print(rowTranslation + " ");
        System.out.print(colTranslation + " ");
        System.out.print(centerCol + " ");
        System.out.print(centerRow + " ");
        System.out.println("");
        List<int[]> minutiae1 = Fingerprint.extract(Fingerprint.thin(Helper.readBinary("resources/fingerprints/" + finger1name + ".png")));
        List<int[]> minutiae2 = Fingerprint.extract(Fingerprint.thin(Helper.readBinary("resources/fingerprints/" + finger2name + ".png")));
        List<int[]> modifiedMinutiae2 = Fingerprint.applyTransformation(minutiae2, centerRow, centerCol, rowTranslation, colTranslation, rotation);

        System.out.println("number of match : " + Fingerprint.matchingMinutiaeCount(minutiae1, modifiedMinutiae2, Fingerprint.DISTANCE_THRESHOLD, Fingerprint.ORIENTATION_THRESHOLD));
    }

    public static void testAllPossibleFingerprints() {
        boolean expectedResult = false;
        for (int a = 1; a <= 16; a++) {
            for (int b = 1; b <= 16; b++) {
                for (int c = 1; c <= 8; c++) {
                    for (int d = 1; d <= 8; d++) {
                        if (a == b) {
                            expectedResult = true;
                        } else {
                            expectedResult = false;
                        }
                        testFinger((a + "_" + c), (b + "_" + d), expectedResult);
                    }
                }
            }
        }
    }

    public static void testMatchingMinutiae() {
        System.out.print("testMatchingMinutiae: ");
        int a = Fingerprint.matchingMinutiaeCount(Fingerprint.extract(Helper.readBinary("resources/test_outputs/skeleton_1_1_small.png")), Fingerprint.extract(Helper.readBinary("resources/test_outputs/skeleton_1_1_small.png")), Fingerprint.DISTANCE_THRESHOLD, Fingerprint.ORIENTATION_THRESHOLD);
        if (a == 10) {
            System.out.println("ok");
        } else {
            System.out.println("error");
        }
    }

    public static void testExtract2() {
        System.out.print("testExtract2: ");
        int a = Fingerprint.extract(Fingerprint.thin(Helper.readBinary("resources/fingerprints/1_2.png"))).size();
        if (a == 56) {
            System.out.println("ok");
        } else {
            System.out.println(a);
            System.out.println("error");
        }
    }

    public static void testExtract() {
        System.out.println("testExtract: ");

        List<int[]> liste = Fingerprint.extract(Helper.readBinary("resources/test_outputs/skeleton_1_1_small.png"));
        for (int[] i : liste) {
            System.out.println(i[0] + " " + i[1]);
        }

        System.out.println("found " + liste.size() + " minutiae");
        System.out.println("-----------------------------");
    }

    public static void testGetNeighbours() {
        System.out.print("testGetNeighbours 1: ");
        boolean[][] image = {{true}};
        boolean[] neighbours = Fingerprint.getNeighbours(image, 0, 0);
        boolean[] expected = {false, false, false, false,
                false, false, false, false};
        if (arrayEqual(neighbours, expected)) {
            System.out.println("OK");
        } else {
            System.out.println("ERROR");
            System.out.print("Expected: ");
            printArray(expected);
            System.out.print("Computed: ");
            printArray(neighbours);
        }

        System.out.print("testGetNeighbours 2: ");
        boolean[][] image2 = {{true, true}};
        boolean[] neighbours2 = Fingerprint.getNeighbours(image2, 0, 0);
        boolean[] expected2 = {false, false, true, false,
                false, false, false, false};
        if (arrayEqual(neighbours, expected)) {
            System.out.println("OK");
        } else {
            System.out.println("ERROR");
            System.out.print("Expected: ");
            printArray(expected2);
            System.out.print("Computed: ");
            printArray(neighbours2);
        }
    }

    public static void testBlackNeighbours() {
        System.out.print("testBlackNeighbours 1: ");
        boolean[] neighbours = {false, true, true,
                false, false, false,
                true, false};
        if (Fingerprint.blackNeighbours(neighbours) == 3) {
            System.out.println(" ok ");
        } else {
            System.out.println("error");
        }
        System.out.print("testBlackNeighbours 2: ");
        boolean[] neighbours2 = {false, false, false,
                false, false, false,
                false, false};
        if (Fingerprint.blackNeighbours(neighbours2) == 0) {
            System.out.println(" ok ");
        } else {
            System.out.println("error");
        }
        System.out.print("testBlackNeighbours 3: ");
        boolean[] neighbours3 = {true, false, false,
                false, false, true,
                false, false};
        if (Fingerprint.blackNeighbours(neighbours3) == 2) {
            System.out.println(" ok ");
        } else {
            System.out.println("error");
        }

    }

    public static void testTransition() {
        System.out.print("testTransition 1: ");
        boolean[] neighbours = {false, false, false,
                false, false, false,
                false, false};
        if (Fingerprint.transitions(neighbours) == 0) {
            System.out.println(" ok ");
        } else {
            System.out.println("error");
        }
        System.out.print("testTransition 2: ");
        boolean[] neighbours2 = {true, false, true,
                false, false, false,
                false, true};
        if (Fingerprint.transitions(neighbours2) == 2) {
            System.out.println(" ok ");
        } else {
            System.out.println("error");
        }
        System.out.print("testTransition 3: ");
        boolean[] neighbours3 = {true, false, false,
                false, false, false,
                false, false};
        if (Fingerprint.transitions(neighbours3) == 1) {
            System.out.println(" ok ");
        } else {
            System.out.println("error");
        }

    }

    public static void testIdentical() {
        System.out.print("testIdentical 1: ");
        boolean[][] a = {{false, true, true},
                {false, false, false},
                {true, false, true}};
        boolean[][] b = {{false, true, true},
                {false, false, false},
                {true, false, true}};
        if (Fingerprint.identical(a, b)) {
            System.out.println("ok");
        } else {
            System.out.println("error");
        }
        System.out.print("testIdentical 2: ");
        boolean[][] d = {{false, false, true},
                {false, false, false},
                {true, false, true}};

        boolean[][] c = {{false, true, true},
                {false, false, false},
                {true, false, true}};
        if (!(Fingerprint.identical(d, c))) {
            System.out.println("ok");
        } else {
            System.out.println("error");
        }
    }


    /**
     * This function is here to help you test the functionalities of
     * connectedPixels. You are free to modify and/or delete it.
     */
    public static void testConnectedPixels1() {
        System.out.print("testConnectedPixels1: ");
        boolean[][] image = {{true, false, false, true},
                {false, false, true, true},
                {false, true, true, false},
                {false, false, false, false}};
        boolean[][] expected = {{false, false, false, true},
                {false, false, true, true},
                {false, true, true, false},
                {false, false, false, false}};
        boolean[][] connectedPixels = Fingerprint.connectedPixels(image, 2, 1, 1);
        if (arrayEqual(connectedPixels, expected)) {
            System.out.println("OK");
        } else {
            System.out.println("ERROR");
            System.out.print("Expected: ");
            printArray(expected);
            System.out.print("Computed: ");
            printArray(connectedPixels);
        }
    }

    public static void testConnectedPixels() {
        System.out.print("testConnectedPixels :=): ");
        boolean[][] image = {{true, false, false, true, true},
                {false, false, true, true, true},
                {false, true, true, false, true},
                {false, false, false, false, false}};

        boolean[][] expected = {{true, false, false, true, false},
                {false, false, true, true, false},
                {false, true, true, false, false},
                {false, false, false, false, false}};

        boolean[][] connectedPixels = Fingerprint.connectedPixels(image, 2, 1, 2);
        if (Fingerprint.identical(connectedPixels, expected)) {
            System.out.println("OK");
        } else {
            System.out.println("ERROR");
            System.out.print("Expected: ");
            printArray(expected);
            System.out.print("Computed: ");
            printArray(connectedPixels);
        }
    }

    public static void testSlope() {
        boolean[][] connectedMachin =
                {{false, false, false, true, false},
                        {false, false, true, true, false},
                        {false, true, true, false, false},
                        {false, false, false, false, false}};
        System.out.println("-----------------------------------");
        System.out.print("testSlope1 :");
        if (Fingerprint.computeSlope(connectedMachin, 2, 1) == 0.7) {
            System.out.println("ok");
        } else {
            System.out.println(" wrong");
            System.out.println("got " + Fingerprint.computeSlope(connectedMachin, 2, 1) + " expected " + 0.7);
            System.out.println("-----------------------------------");
        }
        ;
    }

    public static void testAngle() {
        boolean[][] connectedMachin =
                {{false, false, false, true, false},
                        {false, false, true, true, false},
                        {false, true, true, false, false},
                        {false, false, false, false, false}};
        System.out.println("-----------------------------------");
        System.out.print("testAngle :");
        double slope = Fingerprint.computeSlope(connectedMachin, 2, 1);
        System.out.println(Fingerprint.computeAngle(connectedMachin, 2, 1, slope));

    }

    public static void testConnectedPixelsF() {
        System.out.print("testConnectedPixelsF: ");
        boolean[][] image = {{true, false, false, true, true},
                {false, false, true, true, false},
                {false, true, true, false, false},
                {false, false, false, false, false}};

        boolean[][] expected = {{false, false, false, true, false},
                {false, false, true, true, false},
                {false, true, true, false, false},
                {false, false, false, false, false}};

        boolean[][] connectedPixels = Fingerprint.connectedPixels(image, 2, 1, 2);
        if (Fingerprint.identical(connectedPixels, expected)) {
            System.out.println("OK");
        } else {
            System.out.println("ERROR");
            System.out.print("Expected: ");
            printArray(expected);
            System.out.print("Computed: ");
            printArray(connectedPixels);
        }
    }

    /**
     * This function is here to help you test the functionalities of
     * connectedPixels. You are free to modify and/or delete it.
     */
    public static void testConnectedPixels2() {
        System.out.print("testConnectedPixels2: ");
        boolean[][] image = {{true, false, false, true},
                {false, false, true, true},
                {false, true, true, false},
                {false, false, false, false}};
        boolean[][] expected = {{false, false, false, false},
                {false, false, true, false},
                {false, true, true, false},
                {false, false, false, false}};
        boolean[][] connectedPixels = Fingerprint.connectedPixels(image, 2, 1, 1);
        if (arrayEqual(connectedPixels, expected)) {
            System.out.println("OK");
        } else {
            System.out.println("ERROR");
            System.out.print("Expected: ");
            printArray(expected);
            System.out.print("Computed: ");
            printArray(connectedPixels);
        }
    }

    /**
     * This function is here to help you test the functionalities of
     * connectedPixels. You are free to modify and/or delete it.
     */
    public static void testConnectedPixels3() {
        System.out.print("testConnectedPixels3: ");
        boolean[][] image = {{true, false, false, true, true},
                {true, false, true, true, false},
                {true, true, false, false, false},
                {false, true, false, true, false}};
        boolean[][] expected = {{true, false, false, true, false},
                {true, false, true, true, false},
                {true, true, false, false, false},
                {false, true, false, false, false}};
        boolean[][] connectedPixels = Fingerprint.connectedPixels(image, 2, 1, 2);

        if (arrayEqual(connectedPixels, expected)) {
            System.out.println("OK");
        } else {
            System.out.println("ERROR");
            System.out.print("Expected: ");
            printArray(expected);
            System.out.print("Computed: ");
            printArray(connectedPixels);
        }
    }

    /**
     * This function is here to help you test the functionalities of
     * computeOrientation. You are free to modify and/or delete it.
     */
    public static void testOrientation() {
        boolean[][] image = {{true, false, false, true},
                {false, false, true, true},
                {false, true, true, false},
                {false, false, false, false}};
        int angle = Fingerprint.computeOrientation(image, 2, 1, 3);
        System.out.println("Expected angle: 35\t Computed angle: " + angle);
    }

    /**
     * This function is here to help you test the functionalities of
     * applyRotation. You are free to modify and/or delete it.
     */
    public static void testApplyRotation() {
        // minutia, centerRow, centerCol, rotation)
        System.out.println("testApplyRotation");
        int[] minutia = new int[]{1, 3, 10};
        int[] result = Fingerprint.applyRotation(minutia, 0, 0, 0);
        System.out.println("expected 1 : 1,3,10");
        System.out.print("Computed 1 : ");
        printArray(result);

        result = Fingerprint.applyRotation(minutia, 10, 5, 0);
        System.out.println("Expected 2: 1,3,10");
        System.out.print("Computed 2 : ");
        printArray(result);

        result = Fingerprint.applyRotation(minutia, 0, 0, 90);
        System.out.println("Expected 3 : -3,1,100");
        System.out.print("Computed 3 : ");
        printArray(result);

        result = Fingerprint.applyRotation(new int[]{0, 3, 10}, 0, 0, 90);
        System.out.println("Expected 4 : -3,0,100");
        System.out.print("Computed 4 : ");
        printArray(result);

        result = Fingerprint.applyRotation(new int[]{3, 0, 10}, 0, 0, 90);
        System.out.println("Expected 5 : 0,3,100");
        System.out.print("Computed 5 : ");
        printArray(result);
    }

    /**
     * This function is here to help you test the functionalities of
     * applyTranslation. You are free to modify and/or delete it.
     */
    public static void testApplyTranslation() {
        int[] result = Fingerprint.applyTranslation(new int[]{1, 3, 10}, 0, 0);
        System.out.println("Expected 0: 1,3,10");
        System.out.print("Computed 0: ");
        printArray(result);

        result = Fingerprint.applyTranslation(new int[]{1, 3, 10}, 10, 5);
        System.out.println("Expected: -9,-2,10");
        System.out.print("Computed: ");
        printArray(result);
    }

    /**
     * This function is here to help you test the functionalities of extract.
     * It will read the first fingerprint and extract the minutiae. It will save
     * the thinned version as skeleton_1_1.png and a version where the minutiae
     * are drawn on top as minutiae_1_1.png. You are free to modify and/or delete
     * it.
     */
    public static void testThin() {
        boolean[][] image1 = Helper.readBinary("resources/test_inputs/1_1_small.png");
        boolean[][] skeleton1 = Fingerprint.thin(image1);
        Helper.writeBinary("skeleton_1_1_small.png", skeleton1);
    }

    public static void testDrawSkeleton(String name) {
        boolean[][] image1 = Helper.readBinary("resources/fingerprints/" + name + ".png");
        boolean[][] skeleton1 = Fingerprint.thin(image1);
        Helper.writeBinary("skeleton_" + name + ".png", skeleton1);
    }

    public static void testDrawMinutiae(String name) {
        boolean[][] image1 = Helper.readBinary("resources/fingerprints/" + name + ".png");
        boolean[][] skeleton1 = Fingerprint.thin(image1);
        List<int[]> minutia1 = Fingerprint.extract(skeleton1);
        int[][] colorImageSkeleton1 = Helper.fromBinary(skeleton1);
        Helper.drawMinutia(colorImageSkeleton1, minutia1);
        Helper.writeARGB("minutiae_" + name + ".png", colorImageSkeleton1);
    }

    /**
     * This function is here to help you test the functionalities of extract
     * without using the function thin. It will read the first fingerprint and
     * extract the minutiae. It will save a version where the minutiae are drawn
     * on top as minutiae_skeletonTest.png. You are free to modify and/or delete
     * it.
     */
    public static void testWithSkeleton() {
        boolean[][] skeleton1 = Helper.readBinary("resources/test_inputs/skeletonTest.png");
        List<int[]> minutiae1 = Fingerprint.extract(skeleton1);
        List<int[]> expected = new ArrayList<int[]>();
        expected.add(new int[]{39, 21, 264});
        expected.add(new int[]{53, 33, 270});

        System.out.print("Expected minutiae: ");
        printMinutiae(expected);
        System.out.print("Computed minutiae: ");
        printMinutiae(minutiae1);

        // Draw the minutiae on top of the thinned image
        int[][] colorImageSkeleton1 = Helper.fromBinary(skeleton1);
        Helper.drawMinutia(colorImageSkeleton1, minutiae1);
        Helper.writeARGB("minutiae_skeletonTest.png", colorImageSkeleton1);
    }

    public static void printMinutiae(List<int[]> minutiae) {
        for (int[] minutia : minutiae) {
            System.out.print("[");
            for (int j = 0; j < minutia.length; j++) {
                System.out.print(minutia[j]);
                if (j != minutia.length - 1)
                    System.out.print(", ");
            }
            System.out.println("],");
        }
    }

    /**
     * This function is here to help you test the overall functionalities. It will
     * compare the fingerprint in the file name1.png with the fingerprint in the
     * file name2.png. The third parameter indicates if we expected a match or not.
     */
    public static void testCompareFingerprints(String name1, String name2, boolean expectedResult) {
        boolean[][] image1 = Helper.readBinary("resources/fingerprints/" + name1 + ".png");
        // Helper.show(Helper.fromBinary(image1), "Image1");
        boolean[][] skeleton1 = Fingerprint.thin(image1);
        //Helper.writeBinary("skeleton_" + name1 + ".png", skeleton1);
        List<int[]> minutiae1 = Fingerprint.extract(skeleton1);
        //printMinutiae(minutiae1);

        //int[][] colorImageSkeleton1 = Helper.fromBinary(skeleton1);
        //Helper.drawMinutia(colorImageSkeleton1, minutiae1);
        //Helper.writeARGB("./minutiae_" + name1 + ".png", colorImageSkeleton1);

        boolean[][] image2 = Helper.readBinary("resources/fingerprints/" + name2 + ".png");
        boolean[][] skeleton2 = Fingerprint.thin(image2);
        List<int[]> minutiae2 = Fingerprint.extract(skeleton2);

        //int[][] colorImageSkeleton2 = Helper.fromBinary(skeleton2);
        //Helper.drawMinutia(colorImageSkeleton2, minutiae2);
        //Helper.writeARGB("./minutiae_" + name2 + ".png", colorImageSkeleton2);

        boolean isMatch = Fingerprint.match(minutiae1, minutiae2);
        System.out.print("Compare " + name1 + " with " + name2);
        System.out.print(". Expected match: " + expectedResult);
        System.out.println(" Computed match: " + isMatch);
    }

    public static void testFinger(String name1, String name2, boolean expectedResult) {
        boolean[][] image1 = Helper.readBinary("resources/fingerprints/" + name1 + ".png");
        // Helper.show(Helper.fromBinary(image1), "Image1");
        boolean[][] skeleton1 = Fingerprint.thin(image1);
        //Helper.writeBinary("skeleton_" + name1 + ".png", skeleton1);
        List<int[]> minutiae1 = Fingerprint.extract(skeleton1);
        //printMinutiae(minutiae1);

        //int[][] colorImageSkeleton1 = Helper.fromBinary(skeleton1);
        //Helper.drawMinutia(colorImageSkeleton1, minutiae1);
        //Helper.writeARGB("./minutiae_" + name1 + ".png", colorImageSkeleton1);

        boolean[][] image2 = Helper.readBinary("resources/fingerprints/" + name2 + ".png");
        boolean[][] skeleton2 = Fingerprint.thin(image2);
        List<int[]> minutiae2 = Fingerprint.extract(skeleton2);

        //int[][] colorImageSkeleton2 = Helper.fromBinary(skeleton2);
        //Helper.drawMinutia(colorImageSkeleton2, minutiae2);
        //Helper.writeARGB("./minutiae_" + name2 + ".png", colorImageSkeleton2);

        boolean isMatch = Fingerprint.match(minutiae1, minutiae2);
        if (isMatch == expectedResult) {
            System.out.println(name1 + " test with + " + name2 + " ok");
        } else {
            System.out.println(name1 + " test with + " + name2 + " error got : " + isMatch + " expected " + expectedResult);

        }
    }

    /**
     * This function is here to help you test the overall functionalities. It will
     * compare the fingerprint in the file <code>name1.png</code> with all the eight
     * fingerprints of the given finger (second parameter).
     * The third parameter indicates if we expected a match or not.
     */
    public static void testCompareAllFingerprints(String name1, int finger, boolean expectedResult) {
        for (int i = 1; i <= 8; i++) {
            testCompareFingerprints(name1, finger + "_" + i, expectedResult);
        }
    }

    /*
     * Helper functions to print and compare arrays
     */
    public static boolean arrayEqual(boolean[] array1, boolean[] array2) {
        if (array1 == null && array2 == null)
            return true;
        if (array1 == null || array2 == null)
            return false;
        if (array1.length != array2.length)
            return false;

        for (int i = 0; i < array1.length; i++) {
            if (array1[i] != array2[i])
                return false;
        }
        return true;
    }

    public static boolean arrayEqual(boolean[][] array1, boolean[][] array2) {
        if (array1 == null && array2 == null)
            return true;
        if (array1 == null || array2 == null)
            return false;
        if (array1.length != array2.length)
            return false;

        for (int i = 0; i < array1.length; i++) {
            if (!arrayEqual(array1[i], array2[i]))
                return false;
        }
        return true;
    }

    public static void printArray(boolean[][] array) {
        for (boolean[] row : array) {
            for (boolean pixel : row) {
                System.out.print(pixel + ",");
            }
            System.out.println();
        }
    }

    public static void printArray(boolean[] array) {
        for (boolean pixel : array) {
            System.out.print(pixel + ",");
        }
        System.out.println();
    }

    public static void printArray(int[] array) {
        for (int pixel : array) {
            System.out.print(pixel + ",");
        }
        System.out.println();
    }
}

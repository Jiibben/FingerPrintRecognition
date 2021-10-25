package cs107;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Provides tools to compare fingerprint.
 */
public class Fingerprint {

    /**
     * The number of pixels to consider in each direction when doing the linear
     * regression to compute the orientation.
     */
    public static final int ORIENTATION_DISTANCE = 16;

    /**
     * The maximum distance between two minutiae to be considered matching.
     */
    public static final int DISTANCE_THRESHOLD = 5;

    /**
     * The number of matching minutiae needed for two fingerprints to be considered
     * identical.
     */
    public static final int FOUND_THRESHOLD = 20;

    /**
     * The distance between two angle to be considered identical.
     */
    public static final int ORIENTATION_THRESHOLD = 20;

    /**
     * The offset in each direction for the rotation to test when doing the
     * matching.
     */
    public static final int MATCH_ANGLE_OFFSET = 2;

    /**
     * Returns an array containing the value of the 8 neighbours of the pixel at
     * coordinates <code>(row, col)</code>.
     * <p>
     * The pixels are returned such that their indices corresponds to the following
     * diagram:<br>
     * ------------- <br>
     * | 7 | 0 | 1 | <br>
     * ------------- <br>
     * | 6 | _ | 2 | <br>
     * ------------- <br>
     * | 5 | 4 | 3 | <br>
     * ------------- <br>
     * <p>
     * If a neighbours is out of bounds of the image, it is considered white.
     * <p>
     * If the <code>row</code> or the <code>col</code> is out of bounds of the
     * image, the returned value should be <code>null</code>.
     *
     * @param image array containing each pixel's boolean value.
     * @param row   the row of the pixel of interest, must be between
     *              <code>0</code>(included) and
     *              <code>image.length</code>(excluded).
     * @param col   the column of the pixel of interest, must be between
     *              <code>0</code>(included) and
     *              <code>image[row].length</code>(excluded).
     * @return An array containing each neighbours' value.
     */
    public static boolean[] getNeighbours(boolean[][] image, int row, int col) {
        assert (image != null); // special case that is not expected (the image is supposed to have been checked
        // earlier)
        //row to consider row -1, row, row + 1
        //column to consider
        try {
            boolean a = image[row][col];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
        return new boolean[]{getPixel(image, row - 1, col),
                getPixel(image, row - 1, col + 1),
                getPixel(image, row, col + 1),
                getPixel(image, row + 1, col + 1),
                getPixel(image, row + 1, col),
                getPixel(image, row + 1, col - 1),
                getPixel(image, row, col - 1),
                getPixel(image, row - 1, col - 1)};
    }

    static boolean getPixel(boolean[][] image, int row, int col) {
        try {
            return image[row][col];
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }

    }

    /**
     * Computes the number of black (<code>true</code>) pixels among the neighbours
     * of a pixel.
     *
     * @param neighbours array containing each pixel value. The array must respect
     *                   the convention described in
     *                   {@link #getNeighbours(boolean[][], int, int)}.
     * @return the number of black neighbours.
     */
    public static int blackNeighbours(boolean[] neighbours) {
        int acc = 0;
        for (boolean i : neighbours) {
            if (i) {
                acc++;
            }
        }
        return acc;
    }

    /**
     * Computes the number of white to black transitions among the neighbours of
     * pixel.
     *
     * @param neighbours array containing each pixel value. The array must respect
     *                   the convention described in
     *                   {@link #getNeighbours(boolean[][], int, int)}.
     * @return the number of white to black transitions.
     */
    public static int transitions(boolean[] neighbours) {
        int acc = 0;
        for (int i = 0; i < neighbours.length - 1; i++) {
            if (!neighbours[i] && neighbours[i + 1]) {
                acc++;
            }
        }
        return acc;
    }

    /**
     * Returns <code>true</code> if the images are identical and false otherwise.
     *
     * @param image1 array containing each pixel's boolean value.
     * @param image2 array containing each pixel's boolean value.
     * @return <code>True</code> if they are identical, <code>false</code>
     * otherwise.
     */
    public static boolean identical(boolean[][] image1, boolean[][] image2) {
        if (image1.length != image2.length || image1[0].length != image2[0].length) {
            return false;
        } else {
            for (int y = 0; y < image1.length; y++) {
                for (int x = 0; x < image1[0].length; x++) {
                    if (image1[y][x] != image2[y][x]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Internal method used by {@link #thin(boolean[][])}.
     *
     * @param image  array containing each pixel's boolean value.
     * @param //step the step to apply, Step 0 or Step 1.
     * @return A new array containing each pixel's value after the step.
     */
    static boolean checkAllStep(boolean[][] image, int row, int col) {
        boolean[] pixelNeighbours = getNeighbours(image, row, col);
        int pixelBlackNeighbours = blackNeighbours(pixelNeighbours);
        boolean isPixelBlack = image[row][col];
        boolean isNeighbourTabNonNull = pixelBlackNeighbours >= 1;
        boolean IntervalBlackNeigh = pixelBlackNeighbours >= 2 && pixelBlackNeighbours <= 6;
        boolean white024 = !(pixelNeighbours[0] && pixelNeighbours[2] && pixelNeighbours[3]);
        boolean white246 = !(pixelNeighbours[2] && pixelNeighbours[4] && pixelNeighbours[6]);
        if (isPixelBlack && isNeighbourTabNonNull && IntervalBlackNeigh & white024 && white246) {
            return true;
        }
        return false;
    }

    public static boolean[][] thinningStep(boolean[][] image, int step) {
        for (int row = 0; row < image.length; row++) {
            for (int col = 0; col < image.length; col++) {
                if (checkAllStep(image, row, col)) {
                    image[row][col] = false;
                }
            }
        }
        return image;
    }

    /**
     * Compute the skeleton of a boolean image.
     *
     * @param image array containing each pixel's boolean value.
     * @return array containing the boolean value of each pixel of the image after
     * applying the thinning algorithm.
     */
    static boolean[][] copyImage(boolean[][] image) {
        boolean[][] newImage = new boolean[image.length][image[0].length];
        for (int row = 0; row < image.length; row++) {
            for (int col = 0; col < image[0].length; col++) {
                newImage[row][col] = image[row][col];
            }
        }
        return newImage;
    }

    public static boolean[][] thin(boolean[][] image) {
        boolean[][] imageNotMod = copyImage(image);
        thinningStep(image, 0);
        if (identical(imageNotMod, image)) {
            return image;
        } else {
            return thin(image);
        }
    }

    /**
     * Computes all pixels that are connected to the pixel at coordinate
     * <code>(row, col)</code> and within the given distance of the pixel.
     *
     * @param image    array containing each pixel's boolean value.
     * @param row      the first coordinate of the pixel of interest.
     * @param col      the second coordinate of the pixel of interest.
     * @param distance the maximum distance at which a pixel is considered.
     * @return An array where <code>true</code> means that the pixel is within
     * <code>distance</code> and connected to the pixel at
     * <code>(row, col)</code>.
     */
    public static boolean[][] connectedPixels(boolean[][] image, int row, int col, int distance) {
        //TODO implement
        return null;
    }

    /**
     * Computes the slope of a minutia using linear regression.
     *
     * @param connectedPixels the result of
     *                        {@link #connectedPixels(boolean[][], int, int, int)}.
     * @param row             the row of the minutia.
     * @param col             the col of the minutia.
     * @return the slope.
     */
    public static double computeSlope(boolean[][] connectedPixels, int row, int col) {
        //TODO implement
        return 0;
    }

    /**
     * Computes the orientation of a minutia in radians.
     *
     * @param connectedPixels the result of
     *                        {@link #connectedPixels(boolean[][], int, int, int)}.
     * @param row             the row of the minutia.
     * @param col             the col of the minutia.
     * @param slope           the slope as returned by
     *                        {@link #//computeSlope(boolean[][], int, int, int)}.
     * @return the orientation of the minutia in radians.
     */
    public static double computeAngle(boolean[][] connectedPixels, int row, int col, double slope) {
        //TODO implement
        return 0;
    }

    /**
     * Computes the orientation of the minutia that the coordinate <code>(row,
     * col)</code>.
     *
     * @param image    array containing each pixel's boolean value.
     * @param row      the first coordinate of the pixel of interest.
     * @param col      the second coordinate of the pixel of interest.
     * @param distance the distance to be considered in each direction to compute
     *                 the orientation.
     * @return The orientation in degrees.
     */
    public static int computeOrientation(boolean[][] image, int row, int col, int distance) {
        //TODO implement
        return 0;
    }

    /**
     * Extracts the minutiae from a thinned image.
     *
     * @param image array containing each pixel's boolean value.
     * @return The list of all minutiae. A minutia is represented by an array where
     * the first element is the row, the second is column, and the third is
     * the angle in degrees.
     * @see #thin(boolean[][])
     */
    public static List<int[]> extract(boolean[][] image) {
        //TODO implement
        return null;
    }

    /**
     * Applies the specified rotation to the minutia.
     *
     * @param minutia   the original minutia.
     * @param centerRow the row of the center of rotation.
     * @param centerCol the col of the center of rotation.
     * @param rotation  the rotation in degrees.
     * @return the minutia rotated around the given center.
     */
    public static int[] applyRotation(int[] minutia, int centerRow, int centerCol, int rotation) {
        //TODO implement
        return null;
    }

    /**
     * Applies the specified translation to the minutia.
     *
     * @param minutia        the original minutia.
     * @param rowTranslation the translation along the rows.
     * @param colTranslation the translation along the columns.
     * @return the translated minutia.
     */
    public static int[] applyTranslation(int[] minutia, int rowTranslation, int colTranslation) {
        //TODO implement
        return null;
    }

    /**
     * Computes the row, column, and angle after applying a transformation
     * (translation and rotation).
     *
     * @param minutia        the original minutia.
     * @param centerCol      the column around which the point is rotated.
     * @param centerRow      the row around which the point is rotated.
     * @param rowTranslation the vertical translation.
     * @param colTranslation the horizontal translation.
     * @param rotation       the rotation.
     * @return the transformed minutia.
     */
    public static int[] applyTransformation(int[] minutia, int centerRow, int centerCol, int rowTranslation,
                                            int colTranslation, int rotation) {
        //TODO implement
        return null;
    }

    /**
     * Computes the row, column, and angle after applying a transformation
     * (translation and rotation) for each minutia in the given list.
     *
     * @param minutiae       the list of minutiae.
     * @param centerCol      the column around which the point is rotated.
     * @param centerRow      the row around which the point is rotated.
     * @param rowTranslation the vertical translation.
     * @param colTranslation the horizontal translation.
     * @param rotation       the rotation.
     * @return the list of transformed minutiae.
     */
    public static List<int[]> applyTransformation(List<int[]> minutiae, int centerRow, int centerCol, int rowTranslation,
                                                  int colTranslation, int rotation) {
        //TODO implement
        return null;
    }

    /**
     * Counts the number of overlapping minutiae.
     *
     * @param minutiae1      the first set of minutiae.
     * @param minutiae2      the second set of minutiae.
     * @param maxDistance    the maximum distance between two minutiae to consider
     *                       them as overlapping.
     * @param maxOrientation the maximum difference of orientation between two
     *                       minutiae to consider them as overlapping.
     * @return the number of overlapping minutiae.
     */
    public static int matchingMinutiaeCount(List<int[]> minutiae1, List<int[]> minutiae2, int maxDistance,
                                            int maxOrientation) {
        //TODO implement
        return 0;
    }

    /**
     * Compares the minutiae from two fingerprints.
     *
     * @param minutiae1 the list of minutiae of the first fingerprint.
     * @param minutiae2 the list of minutiae of the second fingerprint.
     * @return Returns <code>true</code> if they match and <code>false</code>
     * otherwise.
     */
    public static boolean match(List<int[]> minutiae1, List<int[]> minutiae2) {
        //TODO implement
        return false;
    }
}

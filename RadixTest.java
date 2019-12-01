/**
 * Greyson Cabrera 014121118
 * Dustin Martin 015180085
 * Project 4
 */

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RadixTest {

    public static void main(String args[]) {

        Scanner in = new Scanner(System.in);
        String input = "";

        while (!input.equals("/exit")) {

            System.out.println("\n1: Convert a Decimal to a Binary value.\n2: Convert a Binary to a Decimal value." +
                    "\n3: Compare RadixSort with Quick Sort of a given Array\n4: Compare Radix Sort with Quick Sort " +
                    "of a Random Array\n/exit: to end the program\n");
            input = in.nextLine();

            switch (input) {
                case "1":
                    try {
                        System.out.print("Enter a positive decimal number: ");
                        int dec = in.nextInt();
                        if (dec < 0) throw new IllegalArgumentException();
                        System.out.print("Enter the number of bits: ");
                        int bits = in.nextInt();
                        in.nextLine();
                        System.out.println("Binary Value: " + decToBin(dec, bits));
                    } catch (IllegalArgumentException iae) {
                        in.nextLine();
                        System.out.println("Cannot be negative.");
                    } catch (InputMismatchException ime) {
                        in.nextLine();
                        System.out.println("Incorrect value entered...");
                    }
                    break;
                case "2":
                    try {
                        System.out.print("Enter a binary value: ");
                        input = in.nextLine();
                        if (!input.matches("[10]*")) throw new IllegalArgumentException();
                        System.out.println("Decimal Value: " + binToDec(input));
                        input = "2";
                    } catch (IllegalArgumentException e) {
                        System.out.println("Enter only 1's or 0's...");
                    }
                    break;
                case "3":
                    System.out.print("Enter the comma de-limited array: ");
                    compareRadixQuick(inputIntArray(in.nextLine()));
                    break;
                case "4":
                    try {
                        System.out.print("Enter the number of items in the array: ");
                        int n = in.nextInt();
                        if (n < 0) throw new IllegalArgumentException();
                        System.out.print("Enter the maximum value that may be generated: ");
                        int m = in.nextInt();
                        if (m < 0) throw new IllegalArgumentException();
                        in.nextLine();
                        compareRadixQuick(n, m);
                    } catch (IllegalArgumentException iae) {
                        in.nextLine();
                        System.out.println("Cannot be negative.");
                    } catch (InputMismatchException ime) {
                        in.nextLine();
                        System.out.println("Incorrect value entered...");
                    }
                    break;
                case "/exit":
                    break;
                default:
                    break;
            }

        }

    }

    /**
     * takes a binary representation and converts it to a decimal value
     * @param binValue binary number
     * @return the binary number as a decimal number
     */
    public static int binToDec(String binValue) {

        int decimal = 0;// the binary number in decimal form
        int power = 0;// the powers of two to increment by

        for (int i = binValue.length()-1; i >= 0; i--) {

            // if the bit is 1 add this power of 2 to the decimal number
            if (binValue.charAt(i) == '1') {
                decimal += Math.pow(2, power);
            }

            power++;

        }

        return decimal;

    }

    /**
     * performs the convertion of a decimal value to a binary value
     * @param num decimal number
     * @param k number of bits for the binary representation
     * @return the binary representation of the decimal number
     */
    public static String decToBin(int num, int k) {

        StringBuilder binValue = new StringBuilder();
        int neededBits = 0;//number of bits needed to represent this number
        int next = num;//keeps track of the quotients

        //if the number is 0 set the binValue to 0
        if (next == 0) {
            binValue.append(0);
            neededBits = 1;
        } else {
            //divide the number by 2 until the quotient is 0
            while (next > 0) {
                binValue.insert(0, (next % 2));//if the number is odd insert a 1 in the beginning
                next /= 2;
            }
            // calculate the required bits to represent the decimal in binary
            neededBits = (int) (Math.floor(Math.log(num) / Math.log(2)) + 1);
        }

        //insert extra 0's for the binary representation
        while (k > neededBits) {
            binValue.insert(0, 0);
            k--;
        }

        return binValue.toString();

    }

    /**
     * takes a string of comma separated numbers and converts it to an integer array
     * @param input the comma separated numbers
     * @return an integer array of the given values
     */
    public static int[] inputIntArray(String input) {

        Pattern pattern = Pattern.compile("[^0-9,]");
        Matcher matcher = pattern.matcher(input);
        String[] inputs = matcher.replaceAll("").split(",");//clear garbage input
        int[] arr = new int[inputs.length];
        //convert strings to ints
        for (int i = 0; i < inputs.length; i++) {
            arr[i] = Integer.parseInt(inputs[i]);
        }

        return arr;

    }

    /**
     * takes an unsorted array, sorts it using both radix and quick sort then prints both results
     * @param arr the int array to be sorted
     */
    public static void compareRadixQuick(int[] arr) {

        //m holds the largest value of the array
        int m = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (m < arr[i]) {
                m = arr[i];
            }
        }

        int[] radixSorted = radixSort(arr, m);
        int[] quickSorted = quickSort(arr, 0, arr.length-1);

        //prints both sorted arrays
        for (int i = 0; i < radixSorted.length; i++) {
            System.out.println("Radix " + i + ": \t" + radixSorted[i] + "\t\t\t\t\t" + quickSorted[i] + " :" + i + " Quick");
        }

    }

    /**
     * compares the speed of both the radix and quick sorting algorithms
     * @param n
     * @param m
     */
    public static void compareRadixQuick(int n, int m) {

        int[] arr = randomGenArray(n, m);

        //times the radix sort
        long startTime = System.nanoTime();
        int[] radixSorted = radixSort(arr, m);
        long endTime = System.nanoTime();
        System.out.println("\nRadix Sort runtime, " + (endTime - startTime) + " nano seconds.");

        //times the quick sort
        startTime = System.nanoTime();
        int[] quickSorted = quickSort(arr, 0, n-1);
        endTime = System.nanoTime();
        System.out.println("Quick Sort runtime, " + (endTime - startTime) + " nano seconds.\n");

        // prints the sorted arrays if there are fewer than 100 items
        if (n <= 100) {

            for (int i = 0; i < radixSorted.length; i++) {
                System.out.println("Radix " + i + ": \t" + radixSorted[i] + "\t\t\t\t\t" + quickSorted[i] + " :" + i + " Quick");
            }

        }

    }

    /**
     * takes an array of positive integers with any amount of digits and sorts them according to their digits. Starts
     * comparing digits from the Least Significant Digit to the Most Significant Digit.
     * @param arr the array to be sorted
     * @param k the maximum
     * @return the array in ascending order
     */
    public static int[] radixSort(int[] arr, int k) {

        int[] sorted = new int[arr.length];

        // j is the digit to sort by starting at the 1's place then moving to 10's, 100's, 1000's, ...
        for (int j = 1; j <= k; j *= 10) {

            int[] sortCnt = new int[10];//keeps track of the # of occurences of numbers between [0 - 9]

            //initialize values to 0
            for (int i = 0; i < sortCnt.length; i++) {
                sortCnt[i] = 0;
            }

            //increment each index if the digit appears in the array to be sorted
            for (int i = 0; i < arr.length; i++) {
                sortCnt[((arr[i] / j) % 10)]++;
            }

            //keeps track of every number that has occurred up to the index
            int sum = 0;
            for (int i = 0; i < sortCnt.length; i++) {
                sum += sortCnt[i];
                sortCnt[i] = sum;
            }

            //grabs a value from the array to be sorted, finds its sorted index, places the value to its sorted index,
            // decrement the total numbers at a given index array
            for (int i = arr.length-1; i >= 0; i--) {
                int a = ((arr[i] / j) % 10);
                int b = sortCnt[a];
                sorted[b-1] = arr[i];
                sortCnt[((arr[i] / j) % 10)]--;
            }

            //update the original array with the latest sorted order, maintains this digit order for sorting the next
            // digit
            for (int i = 0; i < sorted.length; i++) {
                arr[i] = sorted[i];
            }

        }

        return arr;

    }

    /**
     * recursively sorts the array by breaking it up between the pivot point
     * @param array array to be sorted
     * @param p left value
     * @param r right value
     * @return the sorted array in ascending order
     */
    public static int[] quickSort(int[] array, int p, int r) {

        //if the array has at least two values sort it
        if (p < r) {

            int q = partition(array, p, r);//get the partition value, which is the most recently sorted value
            quickSort(array, p, q-1);//sort values to the left of the partition
            quickSort(array, q+1, r);//sort values to the right of the partition

        }

        return array;

    }

    /**
     * takes the value at the end of the given array and finds that elements sorted index
     * in doing so this splits the array into 3 pieces, left elements are less than the partition,
     * the partition is in the middle, right elements are greater than the partition
     * @param array array to be sorted
     * @param p the smallest index to access
     * @param r the largest index to access
     * @return the last elements sorted index
     */
    public static int partition(int[] array, int p, int r) {

        int x = array[r];//store the value to be sorted
        int i = p-1;//get a starting index

        //iterate through the elements up to the partition value
        for (int j = p; j <= r-1; j++) {

            //if the element is less than the partition
            if (array[j] <= x) {
                i += 1;
                //swap the element with one towards the low end of the array
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }

        }

        //after swapping all values that are less than the partition to the left side of the array, place the
        // partition value in the next index
        int temp = array[i+1];
        array[i+1] = array[r];
        array[r] = temp;

        return i+1;//return the partitions index

    }

    /**
     * generates a list of user defined length n filled with random integers in the range of [0 - m]
     * @return a list of integers
     */
    public static int[] randomGenArray(int n, int m) {

        Random rand = new Random();
        int[] intArray = new int[n];

        //generate random values for the array
        for (int i = 0; i < intArray.length; i++) {

            intArray[i] = rand.nextInt(m + 1);

        }

        return intArray;

    }

}

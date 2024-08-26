import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Functions {
    public static int[] takeInput(String fileName, int size) throws IOException {
        int[] input = new int[size];
        String line = "";
        String splitBy = ",";

        BufferedReader br = new BufferedReader(new FileReader(fileName));
        int i = 0;
        line = br.readLine();
        while ((line = br.readLine()) != null && i < size)
        {
            String[] cell = line.split(splitBy);
            input[i] = Integer.parseInt(cell[6]);
            i++;
        }
        return input;
    }

    public static void insertionSort(int[] a) {
        for (int j = 1; j < a.length; j++) {
                int key = a[j];
                int i = j - 1;
                while (i >= 0 && a[i] > key) {
                    a[i + 1] = a[i];
                    i = i - 1;
                }
                a[i + 1] = key;
        }
    }

    private static void merge(int[] a, int[] aux, int lo, int mid, int hi)
    {
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k];
        }
        int i = lo, j = mid+1;
        for (int k = lo; k <= hi; k++)
        {
            if (i > mid) a[k] = aux[j++];
            else if (j > hi) a[k] = aux[i++];
            else if (aux[j] < aux[i]) a[k] = aux[j++];
            else a[k] = aux[i++];
        }
    }

    private static void sort(int[] a, int[] aux, int lo, int hi)
    {
        if (hi <= lo) return;
        int mid = lo + (hi - lo) / 2;
        sort(a, aux, lo, mid);
        sort(a, aux, mid+1, hi);
        merge(a, aux, lo, mid, hi);
    }

    public static void mergeSort(int[] a)
    {
        int[] aux = new int[a.length];
        sort(a, aux, 0, a.length - 1);
    }

    public static int findK(int[] a) {
        int k = a[0];
        for (int b : a) {
            if (k < b) k = b;
        }
        return k;
    }

    public static int[] countingSort(int[] a, int k) {
        int size = a.length;
        int[] output = new int[size];
        int[] count = new int[k + 1];

        for (int i = 0; i < size; i++) {
            count[a[i]]++;
        }
        for (int i = 1; i <= k; i++) {
            count[i] += count[i - 1];
        }
        for (int i = size - 1; i >= 0; i--) {
            output[count[a[i]] - 1] = a[i];
            count[a[i]]--;
        }
        return output;
    }

    public static int linearSearch(int[] a, int x) {
        int size = a.length;
        for (int i = 0; i < size; i++) {
            if (a[i] == x) return i;
        }
        return -1;
    }

    public static int binarySearch(int[] a, int x) {
        int low = 0;
        int high = a.length - 1;
        while ((high - low) > 1) {
            int mid = (high + low) / 2;
            if (a[mid] < x) low = mid + 1;
            else high = mid;
        }
        if (a[low] == x) return low;
        else if (a[high] == x) return high;
        return -1;
    }

    public static void shuffle(int[] a) {
        Random rand = new Random();

        for (int i = 0; i < a.length; i++) {
            int randomIndexToSwap = rand.nextInt(a.length);
            int temp = a[randomIndexToSwap];
            a[randomIndexToSwap] = a[i];
            a[i] = temp;
        }
    }

    public static int[] reverse(int[] a)
    {
        int size = a.length;
        int[] b = new int[size];
        int j = size;
        for (int k : a) {
            b[j - 1] = k;
            j = j - 1;
        }
        return b;
    }

    public static double[] test(int[] inputAxis, int[] input, String type, boolean shuffle) {
        long init, last, diff, sum;
        diff = 0;
        double[] averages = new double[10];
        double average = 0;
        Random generator = new Random();
        int randomIndex;
        for (int i = 0; i < inputAxis.length; i++) {
            sum = 0;
            int[] sample = Arrays.copyOfRange(input, 0, inputAxis[i]);
            switch (type) {
                case "Insertion":
                    for (int j = 0; j < 10; j++) {
                        if (shuffle) shuffle(sample);
                        init = System.currentTimeMillis();
                        insertionSort(sample);
                        last = System.currentTimeMillis();
                        diff = last - init;
                        sum += diff;
                    }
                    average = sum / (double) 10;
                    System.out.println("Average Time with " + inputAxis[i] + " input: " + average + "ms");
                    break;
                case "Merge":
                    for (int j = 0; j < 10; j++) {
                        if (shuffle) shuffle(sample);
                        init = System.currentTimeMillis();
                        mergeSort(sample);
                        last = System.currentTimeMillis();
                        diff = last - init;
                        sum += diff;
                    }
                    average = sum / (double) 10;
                    System.out.println("Average Time with " + inputAxis[i] + " input: " + average + "ms");
                    break;
                case "Counting":
                    int k = findK(sample);
                    for (int j = 0; j < 10; j++) {
                        if (shuffle) shuffle(sample);
                        init = System.currentTimeMillis();
                        countingSort(sample, k);
                        last = System.currentTimeMillis();
                        diff = last - init;
                        sum += diff;
                    }
                    average = sum / (double) 10;
                    System.out.println("Average Time with " + inputAxis[i] + " input: " + average + "ms");
                    break;
                case "Linear":
                    for (int j = 0; j < 1000; j++) {
                        if (shuffle) shuffle(sample);
                        randomIndex = generator.nextInt(sample.length - 1);
                        int x = sample[randomIndex];
                        init = System.nanoTime();
                        linearSearch(sample, x);
                        last = System.nanoTime();
                        diff = last - init;
                        sum += diff;
                    }
                    average = sum / (double) 1000;
                    System.out.println("Average Time with " + inputAxis[i] + " input: " + average + "ns");
                    break;
                case "Binary":
                    for (int j = 0; j < 1000; j++) {
                        if (shuffle) shuffle(sample);
                        randomIndex = generator.nextInt(sample.length - 1);
                        int x = sample[randomIndex];
                        init = System.nanoTime();
                        binarySearch(sample, x);
                        last = System.nanoTime();
                        diff = last - init;
                        sum += diff;
                    }
                    average = sum / (double) 1000;
                    System.out.println("Average Time with " + inputAxis[i] + " input: " + average + "ns");
                    break;
                default:
                    break;
            }
            averages[i] = average;
        }
        return averages;
    }
}
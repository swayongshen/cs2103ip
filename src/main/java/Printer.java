public class Printer {

    /**
     * Prints each string in output array in a new line, using duke's style.
     * @param output
     */
    public static void printWithStyle(String[] output) {
        System.out.println("    ________________________________________________________________");
        for (String str : output) {
            System.out.println("    " + str);
        }
        System.out.println("    ________________________________________________________________");
    }

    /**
     * Prints the output string using duke's style.
     * @param output
     */
    public static void printWithStyle(String output) {
        System.out.println("    ________________________________________________________________");
        System.out.println("    " + output );
        System.out.println("    ________________________________________________________________");
    }
}

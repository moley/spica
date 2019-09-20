package org.spica.javaclient.utils;

public class LogUtil {

    /**
     * Escape Sequence
     */
    private static String escape = String.valueOf((char) 27);

    /**
     * Escape sequences for terminal and console: switch on GREEN
     */
    private static final String YELLOW_ON = escape + "[22;33m";

    /**
     * Escape sequences for terminal and console: switch on RED
     */
    private static final String RED_ON = escape + "[22;31m";

    /**
     * Escape sequences for terminal and console: switch on GREEN
     */
    private static final String GREEN_ON = escape + "[22;32m";

    /**
     * Escape sequences for terminal and console: switch on BLUE
     */
    private static final String BLUE_ON = escape + "[22;34m";

    /**
     * Escape sequences for terminal and console: switch on CYAN
     */
    private static final String CYAN_ON = escape + "[22;36m";

    /**
     * Escape sequences for terminal and console: switch on GRAY
     */
    private static final String GRAY_ON = escape + "[22;37m";

    /**
     * Escape sequences for terminal and console: switch off all
     */
    private static final String ESC_OFF = escape + "[0m";

    /**
     * System line separator character(s)
     */
    static final String lineSep = System.getProperty("line.separator");

    /**
     * Input prompt
     */
    static final String inputPrompt = "${lineSep}??>";

    /**
     * Renders a line in the default Gradle Yellow style.
     *
     * @param line Line
     * @return Line with Escape Sequences
     */
    public static String yellow(String line) {
        return YELLOW_ON + line + ESC_OFF;
    }

    /**
     * Renders a line in the default Gradle Green style.
     *
     * @param line Line
     * @return Line with Escape Sequences
     */
    public static String green(String line) {
        return GREEN_ON + line + ESC_OFF;
    }

    /**
     * Renders a line in the blue style.
     *
     * @param line Line
     * @return Line with Escape Sequences
     */
    public static String blue(String line) {
        return BLUE_ON + line + ESC_OFF;
    }

    /**
     * Renders a line in the gray style.
     *
     * @param line Line
     * @return Line with Escape Sequences
     */
    public static String gray(String line) {
        return GRAY_ON + line + ESC_OFF;
    }

    /**
     * Renders a line in the default Cyan style.
     *
     * @param line Line
     * @return Line with Escape Sequences
     */
    public static String cyan(String line) {
        return CYAN_ON + line + ESC_OFF;
    }

    /**
     * Renders a line in the default Red style.
     *
     * @param line Line
     * @return Line with Escape Sequences
     */
    public static String red(String line) {
        return RED_ON + line + ESC_OFF;
    }


}

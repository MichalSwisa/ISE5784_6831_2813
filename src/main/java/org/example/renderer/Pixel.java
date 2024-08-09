package org.example.renderer;

record Pixel(int row, int col) {
    private static final String PRINT_FORMAT = "%5.1f%%\r\n";
    private static final Object mutexNext = new Object();
    private static final Object mutexPixels = new Object();
    private static int maxRows = 0;
    private static int maxCols = 0;
    private static long totalPixels = 0L;
    private static volatile int cRow = 0;
    private static volatile int cCol = -1;
    private static volatile long pixels = 0L;
    private static volatile int lastPrinted = 0;
    private static boolean print = false;
    private static long printInterval = 100L;

    static void initialize(int maxRows, int maxCols, double interval) {
        Pixel.maxRows = maxRows;
        Pixel.maxCols = maxCols;
        Pixel.totalPixels = (long) maxRows * maxCols;
        Pixel.printInterval = (int) (interval * 10);
        Pixel.print = Pixel.printInterval != 0;
        if (Pixel.print) {
            System.out.printf(PRINT_FORMAT, 0d);
        }
    }

    static Pixel nextPixel() {
        synchronized (mutexNext) {
            if (cRow == maxRows) {
                return null;
            }
            ++cCol;
            if (cCol < maxCols) {
                return new Pixel(cRow, cCol);
            }
            cCol = 0;
            ++cRow;
            if (cRow < maxRows) {
                return new Pixel(cRow, cCol);
            }
        }
        return null;
    }

    static void pixelDone() {
        boolean flag = false;
        int percentage = 0;
        synchronized (mutexPixels) {
            ++pixels;
            if (print) {
                percentage = (int) (1000L * pixels / totalPixels);
                if (percentage - lastPrinted >= printInterval) {
                    lastPrinted = percentage;
                    flag = true;
                }
            }
        }
        if (flag) {
            System.out.printf(PRINT_FORMAT, percentage / 10d);
            System.out.flush();
        }
    }

}

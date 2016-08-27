package com.bitdecay.ludum.dare.control;

public enum Xbox360Pad {
    // -1 = Not supported
    UP(0, -1),
    DOWN(1, -1),
    LEFT(2, -1),
    RIGHT(3, -1),
    START(4, 7),
    BACK(5, 6),
    LS(6, 8),
    RS(7, 9),
    LB(8, 4),
    RB(9, 5),
    XBOX(10, -1),
    A(11, 0),
    B(12, 1),
    X(13, 2),
    Y(14, 3),
    LT(0, -1),
    RT(1, -1),
    LS_LEFT(2, 1),
    LS_RIGHT(2, 1),
    LS_UP(3, 0),
    LS_DOWN(3, 0),
    RS_LEFT(4, 3),
    RS_RIGHT(4, 3),
    RS_UP(5, 2),
    RS_DOWN(5, 2),;

    public int val;

    Xbox360Pad(int macVal, int winVal) {
        // Mappings are different for different OSs.
        String osName = System.getProperty("os.name");
        if (osName.contains("Mac")) {
            val = macVal;
        } else if (osName.contains("Windows")) {
            val = winVal;
        } else {
            val = macVal;
        }
    }
}


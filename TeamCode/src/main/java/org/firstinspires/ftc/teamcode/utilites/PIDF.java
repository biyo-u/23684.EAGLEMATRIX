package org.firstinspires.ftc.teamcode.utilites;

public class PIDF {
    private final double P;
    private final double I;
    private final double D;
    private final double F;

    public PIDF(double p, double i, double d, double f) {
        P = p;
        I = i;
        D = d;
        F = f;
    }

    public double getP() {
        return P;
    }

    public double getI() {
        return I;
    }

    public double getD() {
        return D;
    }

    public double getF() {
        return F;
    }
}

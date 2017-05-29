package wbif.sjx.common.Object;

import org.apache.commons.math3.stat.descriptive.summary.Sum;
import wbif.sjx.common.Analysis.*;
import wbif.sjx.common.MathFunc.CumStat;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by sc13967 on 03/02/2017.
 */
public class Track {
    double[] x;
    double[] y;
    double[] z;
    int[] f;


    // CONSTRUCTORS

    public Track(double[] x, double[] y, double[] z, int[] f) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.f = f;

    }

    public Track(double[] x, double[] y, int[] f) {
        this.x = x;
        this.y = y;
        this.z = new double[x.length];
        Arrays.fill(z, 1);
        this.f = f;

    }

    public Track(ArrayList<Integer> xArray, ArrayList<Integer> yArray, ArrayList<Integer> zArray, ArrayList<Integer> fArray) {
        for (int i=0;i<x.length;i++) {
            x[i] = xArray.get(i);
            y[i] = yArray.get(i);
            z[i] = zArray.get(i);
            f[i] = fArray.get(i);

        }
    }

    // PUBLIC METHODS

    public CumStat getDirectionalPersistence() {
        return DirectionalPersistenceCalculator.calculate(f,x,y,z);

    }

    public CumStat getMSD() {
        return MSDCalculator.calculate(f,x,y,z);

    }

    public double[] getInstantaneousVelocity() {
        return InstantaneousVelocityCalculator.calculate(f,x,y,z);

    }

    public double[] getStepSizes() {
        return StepSizeCalculator.calculator(x,y,z);

    }

    public double getEuclideanDistance() {
        double dx = x[x.length-1]-x[0];
        double dy = y[x.length-1]-y[0];
        double dz = z[x.length-1]-z[0];

        return Math.sqrt(dx * dx + dy * dy + dz * dz);

    }

    public double getTotalPathLength() {
        double[] steps = getStepSizes();

        return new Sum().evaluate(steps);

    }

    public double getDirectionalityRatio() {
        return getEuclideanDistance()/getTotalPathLength();

    }

    /**
     * Returns a double[] containing the Euclidean distance at all time steps
     */
    public double[] getRollingEuclideanDistance() {
        return EuclideanDistanceCalculator.calculate(x,y,z);

    }

    /**
     * Returns a double[] containing the total path length up to each time step
     */
    public double[] getRollingTotalPathLength() {
        return TotalPathLengthCalculator.calculate(x,y,z);

    }

    /**
     * Returns a double[] containing the directionality ratio at all time steps
     */
    public double[] getRollingDirectionalityRatio() {
        return DirectionalityRatioCalculator.calculate(x,y,z);

    }

    public int getDuration() {
        return f[f.length-1]-f[0];

    }


    // GETTERS AND SETTERS


    public double[] getX() {
        return x;
    }

    public void setX(double[] x) {
        this.x = x;
    }

    public double[] getY() {
        return y;
    }

    public void setY(double[] y) {
        this.y = y;
    }

    public double[] getZ() {
        return z;
    }

    public void setZ(double[] z) {
        this.z = z;
    }

    public int[] getF() {
        return f;
    }

    public void setF(int[] f) {
        this.f = f;
    }
}
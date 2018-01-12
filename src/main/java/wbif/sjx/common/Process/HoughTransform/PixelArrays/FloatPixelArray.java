package wbif.sjx.common.Process.HoughTransform.PixelArrays;

/**
 * Created by sc13967 on 12/01/2018.
 */
public class FloatPixelArray extends PixelArray {
    float[] pixels;

    public FloatPixelArray(float[] pixels, int[] dim) {
        super(dim);
        this.pixels = pixels;

    }

    @Override
    public int length() {
        return pixels.length;
    }

    @Override
    public double get(int i) {
        return pixels[i];
    }
}
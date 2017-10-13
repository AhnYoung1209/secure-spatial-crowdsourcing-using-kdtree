import java.math.BigInteger;

/**
 * The {@code Point2D} class is an immutable data type to encapsulate a
 * two-dimensional point with real-value coordinates.
 * <point>
 * Note: in order to deal with the difference behavior of double and
 * Double with respect to -0.0 and +0.0, the Point2D constructor converts
 * any coordinates that are -0.0 to +0.0.
 *
 * @author sunyue
 * @version 1.0
 * @createdOn 2017/7/29 14:01
 */
public final class Point2D implements Comparable<Point2D> {
    public final BigInteger id;
    /**
     * x-coordinate
     */
    public final BigInteger x;
    /**
     * y-coordinate
     */
    public final BigInteger y;

    /**
     * Initializes a new point (x, y).
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @throws IllegalArgumentException if either {@code x} or {@code y}
     *                                  is {@code Double.NaN}, {@code Double.POSITIVE_INFINITY} or
     *                                  {@code Double.NEGATIVE_INFINITY}
     */
    public Point2D(String id, String x, String y) {
        this.id = new BigInteger(id);
        this.x = new BigInteger(x);
        this.y = new BigInteger(y);
    }

    /**
     * Returns the Euclidean distance between this point and that point.
     *
     * @param that the other point
     * @return the Euclidean distance between this point and that point
     */
    public double distanceTo(Point2D that) {
        return Math.sqrt(this.distanceSquaredTo(that).doubleValue());
    }

    /**
     * Returns the square of the Euclidean distance between this point and that point.
     *
     * @param that the other point
     * @return the square of the Euclidean distance between this point and that point
     */
    public BigInteger distanceSquaredTo(Point2D that) {
        BigInteger dx = this.x.subtract(that.x);
        BigInteger dy = this.y.subtract(that.y);
        return dx.multiply(dx).add(dy.multiply(dy));
    }

    @Override
    public int compareTo(Point2D that) {
        if (this.y.compareTo(that.y) < 0) return -1;
        if (this.y.compareTo(that.y) > 0) return +1;
        if (this.x.compareTo(that.x) < 0) return -1;
        if (this.x.compareTo(that.x) > 0) return +1;
        return 0;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point2D)) return false;

        Point2D point2D = (Point2D) o;

        if (!x.equals(point2D.x)) return false;
        return y.equals(point2D.y);
    }

    @Override
    public int hashCode() {
        int result = x.hashCode();
        result = 31 * result + y.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "P["+id+"](" + x + "," + y + ")";
    }
}

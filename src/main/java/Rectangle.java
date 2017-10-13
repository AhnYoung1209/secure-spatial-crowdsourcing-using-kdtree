import java.math.BigInteger;

/**
 * The {@code Rectangle} class is an immutable data type to encapsulate a
 * two-dimensional axis-aligned rectagle with real-value coordinates.
 * The rectangle is <em>closed</em>—it includes the points on the boundary.
 *
 * @author sunyue
 * @version 1.0
 * @createdOn 2017/7/29 14:43
 */
public final class Rectangle {
    /**
     * minimum x-coordinates
     */
    public final BigInteger xMin;
    /**
     * maximum x-coordinates
     */
    public final BigInteger xMax;
    /**
     * minimum y-coordinates
     */
    public final BigInteger yMin;
    /**
     * maximum y-coordinates
     */
    public final BigInteger yMax;


    /**
     * Initializes a new rectangle [<em>xmin</em>, <em>xmax</em>]
     * x [<em>ymin</em>, <em>ymax</em>].
     *
     * @param xMin the <em>x</em>-coordinate of the lower-left endpoint
     * @param xMax the <em>x</em>-coordinate of the upper-right endpoint
     * @param yMin the <em>y</em>-coordinate of the lower-left endpoint
     * @param yMax the <em>y</em>-coordinate of the upper-right endpoint
     * @throws IllegalArgumentException if any of {@code xmin},
     *                                  {@code xmax}, {@code ymin}, or {@code ymax}
     *                                  is {@code Double.NaN}.
     * @throws IllegalArgumentException if {@code xmax < xmin} or {@code ymax < ymin}.
     */
    public Rectangle(BigInteger xMin, BigInteger xMax, BigInteger yMin, BigInteger yMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    /**
     * Returns the width of this rectangle
     *
     * @return the width of this rectangle {@code xMax - xMin}
     */
    public BigInteger getWidth() {
        return xMax.subtract(xMin);
    }

    /**
     * Returns the height of this rectangle.
     *
     * @return the height of this rectangle {@code yMax - yMin}
     */
    public BigInteger getHeight() {
        return yMax.subtract(yMin);
    }

    /**
     * Returns true if the two rectangles intersect. This includes <em>improper intersections</em> (at points on the
     * boundary of each rectangle) and <em>nested intersctions</em> (when one rectangle is contained inside the other)
     *
     * @param that the other rectangle
     * @return {@code true} if this rectangle intersect the argument rectangle at one or more points
     */
    public boolean intersects(Rectangle that) {
        return true;
        // todo
//        return this.xMax >= that.xMin && this.yMax >= that.yMin
//                && that.xMax >= this.xMin && that.yMax >= this.yMin;
    }

    /**
     * Returns true if this rectangle contain the point.
     *
     * @param p the point
     * @return {@code true} if this rectangle contain the point {@code point},
     * possibly at the boundary; {@code false} otherwise
     */
    public boolean contains(Point2D p) {
        // TODO
//        return (p.getX() >= xMin && p.getX() <= xMax)
//                && (p.getY() >= yMin && p.getY() <= yMax);
        return true;
    }

    /**
     * Returns the Euclidean distance between this rectangle and the point {@code point}.
     *
     * @param p the point
     * @return the Euclidean distance between the point {@code point} and the closest point on this rectangle; 0 if the
     * point is contained in this rectangle
     */
    public double distanceTo(Point2D p) {
        return Math.sqrt(distanceSquaredTo(p).doubleValue());
    }

    /**
     * Returns the square of the Euclidean distance between this rectangle and the point {@code point}.
     *
     * @param p the point
     * @return the square of the Euclidean distance between the point {@code point} and
     * the closest point on this rectangle; 0 if the point is contained in this rectangle
     */
    public BigInteger distanceSquaredTo(Point2D p) {
        BigInteger dx = BigInteger.ZERO;
        BigInteger dy = BigInteger.ZERO;

        // TODO
//        if (p.getX() < xMin) dx = p.getX() - xMin;
//        else if (p.getX() > xMax) dx = p.getX() - xMax;
//
//        if (p.getY() < yMin) dy = p.getY() - yMin;
//        else if (p.getY() > yMax) dy = p.getY() - yMax;

        return dx.multiply(dx).add(dy.multiply(dy));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rectangle)) return false;

        Rectangle rectangle = (Rectangle) o;

        if (!xMin.equals(rectangle.xMin)) return false;
        if (!xMax.equals(rectangle.xMax)) return false;
        if (!yMin.equals(rectangle.yMin)) return false;
        return yMax.equals(rectangle.yMax);
    }

    @Override
    public int hashCode() {
        int result = xMin.hashCode();
        result = 31 * result + xMax.hashCode();
        result = 31 * result + yMin.hashCode();
        result = 31 * result + yMax.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Rectangle{[" + xMin + "," + xMax + "] x [" + yMin + "," + yMax + "]}";
    }
}

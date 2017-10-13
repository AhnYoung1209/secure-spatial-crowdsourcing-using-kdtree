//import java.util.HashSet;
//import java.util.LinkedList;
//import java.util.Queue;
//import java.util.Set;
//
///**
// * @author sunyue
// * @version 1.0
// * @createdOn 2017/7/29 13:44
// */
//public class KdTree {
//    /**
//     * The canvas for all data points.
//     */
//    private final Rectangle canvas;
//    private Node root;
//    private int count;
//
//    /**
//     * Initializes a new rectangle
//     *
//     * @param canvas the canvas contains all points
//     */
//    public KdTree(Rectangle canvas) {
//        this.canvas = canvas;
//        this.root = null;
//        this.count = 0;
//    }
//
//    /**
//     * Is empty?
//     *
//     * @return true or false
//     */
//    public boolean isEmpty() {
//        return count == 0;
//    }
//
//    /**
//     * Number of points in the tree
//     *
//     * @return
//     */
//    public int size() {
//        return count;
//    }
//
//    /**
//     * Insert the point into the tree (if it is not already in the tree).
//     * The time complexity is O(log n)
//     *
//     * @param p the point to be inserted
//     */
//    public void insert(Point2D p) {
//        if (null == p) throw new NullPointerException("Point cannot be null");
//        root = insert(p, root, true, canvas);
//    }
//
//    /**
//     * Check whether the tree contain point point
//     * The time complexity is O(log n)
//     *
//     * @param p the point
//     * @return true if the tree contains point
//     */
//    public boolean contains(Point2D p) {
//        if (null == p) throw new NullPointerException("Point cannot be null");
//        return get(p, root) != null;
//    }
//
//    /**
//     * Get all points that are inside the rectangle
//     * The time complexity is O(n)
//     *
//     * @param rect the rectangle
//     * @return
//     */
//    public Iterable<Point2D> range(Rectangle rect) {
//        if (null == rect) throw new NullPointerException("Rectangle cannot be null");
//        Set<Point2D> set = new HashSet<>();
//        range(set, rect, root);
//        return set;
//    }
//
//    /**
//     * Find the nearest neighbor in the tree to point point.
//     * null if the tree is empty;
//     * The time complexity is O(n)
//     *
//     * @param p the point
//     * @return the nearest neighbor
//     */
//    public Point2D nearest(Point2D p) {
//        if (null == p) throw new NullPointerException("Point cannot be null");
//        if (null == root) return null;
//        Point2D retP = null;
//        double min = Double.MAX_VALUE;
//        Queue<Node> queue = new LinkedList<>();
//        queue.add(root);
//        // Queue other than recursion
//        while (!queue.isEmpty()) {
//            Node x = queue.poll();
//            double dis = p.distanceSquaredTo(x.p);
//            if (dis < min) {
//                retP = x.p;
//                min = dis;
//            }
//            // Left is not null && rectangle of left tree is close to point
//            if (null != x.left && x.left.rect.distanceSquaredTo(p) < min) {
//                queue.add(x.left);
//            }
//            // Right is not null && rectangle of Right tree is close to point
//            if (null != x.right && x.right.rect.distanceSquaredTo(p) < min) {
//                queue.add(x.right);
//            }
//        }
//        return retP;
//    }
//
//    /**
//     * @param p          the {@code Point2D} to be inserted
//     * @param n          the node to be measured
//     * @param coordinate partition by x-coordinate or y-coordinate
//     * @param rect       the rectangle
//     * @return the node
//     */
//    private Node insert(Point2D p, Node n, boolean coordinate, Rectangle rect) {
//        // Case 1: n is null
//        if (null == n) {
//            count++;
//            return new Node(p, rect, coordinate);
//        }
//        // Case 2: point equals n.point
//        if (p.equals(n.p)) return n;
//
//        if (n.compareTo(p) > 0) {  // Case 3: point is on the left or bottom of n.point
//            n.left = insert(p, n.left, !coordinate, childRect(n, true));
//        } else {  // Case 4: point is on the right or top of n.point
//            n.right = insert(p, n.right, !coordinate, childRect(n, false));
//        }
//
//        return n;
//    }
//
//    /**
//     * Generate child-rectangle
//     *
//     * @param n      tree node
//     * @param isLeft whether is in left
//     * @return the rectangle
//     */
//    private Rectangle childRect(Node n, boolean isLeft) {
//        Rectangle rect;
//        Rectangle temp = n.rect;
//
//        if (isLeft) {  // left child
//            if (null != n.left) return n.left.rect;
//
//            if (n.coordinate) {  // partition by x-coordinate, generate the left-sub-rectangle
//                rect = new Rectangle(temp.getxMin(), n.p.getX(), temp.getyMin(), temp.getyMax());
//            } else {  // partition by y-coordinate, generate the bottom-sub-rectangle
//                rect = new Rectangle(temp.getxMin(), temp.getxMax(), temp.getyMin(), n.p.getY());
//            }
//        } else {  // right child
//            if (null != n.right) {  // partition by x-coordinate, generate the right-sub-rectangle
//                rect = new Rectangle(n.p.getX(), temp.getxMax(), temp.getyMin(), temp.getyMax());
//            } else {  // partition by y-coordinate, generate the top-sub-rectangle
//                rect = new Rectangle(temp.getxMin(), temp.getxMax(), n.p.getY(), temp.getyMax());
//            }
//        }
//        return rect;
//    }
//
//    /**
//     * get point point from the tree
//     *
//     * @param p
//     * @param n
//     * @return
//     */
//    private Node get(Point2D p, Node n) {
//        // Case 1: n is null
//        if (null == n) return null;
//        // Case 2: point equals n.point
//        if (n.p.equals(p)) return n;
//        if (n.compareTo(p) > 0) {  // Case 3: point is on the left or bottom of n.point
//            return get(p, n.left);
//        } else {  // Case 4: point is on the right or top of n.point
//            return get(p, n.right);
//        }
//    }
//
//    /**
//     * Get all the points which are inside the rectangle rect
//     *
//     * @param set
//     * @param rect
//     * @param n
//     */
//    private void range(Set<Point2D> set, Rectangle rect, Node n) {
//        if (null == n || !n.rect.intersects(rect)) return;
//        // sub-rectangle exists on the left or bottom of n
//        boolean isLeft = (n.coordinate && rect.getxMin() < n.p.getX())
//                || (!n.coordinate && rect.getyMin() < n.p.getY());
//        // sub-rectangle exists on the right or top of n
//        boolean isRight = (n.coordinate && rect.getxMax() >= n.p.getX())
//                || (!n.coordinate && rect.getxMax() >= n.p.getY());
//
//        if (rect.contains(n.p)) {
//            set.add(n.p);
//        }
//        if (isLeft) {
//            range(set, rect, n.left);
//        }
//        if (isRight) {
//            range(set, rect, n.right);
//        }
//    }
//
//    private static class Node {
//        private Point2D p;
//        private Rectangle rect;
//        private Node left;
//        private Node right;
//
//        /**
//         * true for x-coordinate, false for y-coordinate
//         */
//        private boolean coordinate;
//
//        public Node(Point2D p, Rectangle rect, boolean coordinate) {
//            this.p = p;
//            this.rect = rect;
//            this.coordinate = coordinate;
//        }
//
//        /**
//         * Compare to given {@code Point2D}
//         *
//         * @param that given {@code Point2D}
//         * @return
//         */
//        public int compareTo(Point2D that) {
//            if (coordinate) {
//                if (this.p.getX() < that.getX()) return -1;
//                if (this.p.getX() > that.getX()) return +1;
//            } else {
//                if (this.p.getY() < that.getY()) return -1;
//                if (this.p.getY() > that.getY()) return +1;
//            }
//            return 0;
//        }
//    }
//}

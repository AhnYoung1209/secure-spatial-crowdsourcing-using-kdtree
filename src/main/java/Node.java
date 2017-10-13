import java.math.BigInteger;

/**
 * @author sunyue
 * @version 1.0
 * @createdOn 2017/8/1 22:22
 */
public class Node {
    public BigInteger id;
    public Point2D point;
    public Node left;
    public Node right;

    public Node(Point2D p) {
        this.id = p.id;
        this.point = p;
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }

    @Override
    public String toString() {
        return "Node{" + "id=" + id + "," + point.toString() + '}';
    }
}

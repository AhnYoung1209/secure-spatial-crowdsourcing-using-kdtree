import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author sunyue
 * @version 1.0
 * @createdOn 2017/8/1 22:09
 */
public class SkdTree {
    public static final Logger logger = LoggerFactory.getLogger(SkdTree.class);
    public Node root;
    public int count;

    public SkdTree() {
        root = null;
        count = 0;
    }

    /**
     * @param c          current node
     * @param n          new node
     * @param coordinate true for x; false for y
     * @return the current node
     */
    public Node insert(Node c, Node n, boolean coordinate) {
        // empty tree
        if (c == null) {
            count++;
            return n;
        }

        // n.p = c.p
        if (n.point.equals(c.point)) {
            logger.error("{} == {}", n.toString(), c.toString());
            return c;
        }

        if (coordinate) {
            assert c.left == null;
            count++;
            c.left = n;
        } else {
            assert c.right == null;
            count++;
            c.right = n;
        }
        return c;
    }

    /**
     * Level Order Traversal and Print
     */
    public void print() {
        Node dummy = new Node(null);

        if (null == root) return;
        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);
        int start = 0;
        int end = 1;
        while (!queue.isEmpty()) {
            Node cur = queue.poll();
            System.out.print(cur.point.toString() + " ");
            start++;
            if (cur.left != null) {
                queue.offer(cur.left);
            }
            if (cur.right != null) {
                queue.offer(cur.right);
            }

            if (start == end) {
                end = queue.size();
                start = 0;
                System.out.println();
            }
        }
    }
}

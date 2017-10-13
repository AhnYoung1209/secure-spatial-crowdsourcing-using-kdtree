import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author sunyue
 * @version 1.0
 * @createdOn 2017/8/1 21:37
 */
public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);
    public ComputingServer sc;
    public LogisticServer sl;
    public Set<Point2D> workerSet;
    public Set<Point2D> taskSet;

    public static void main(String[] args) {
        boolean isLocal = true;

        Main m = new Main();
        m.init(isLocal);

        long begin = System.currentTimeMillis();
        m.buildTree();
        long end = System.currentTimeMillis();
        System.out.println("Tree Node count: " + m.sl.tree.count);
        System.out.println("Build index cost: " + (end - begin) / 1000 + " s");
        System.out.println("Average build index cost: " + (end - begin) / 1000 / m.sl.tree.count + " s");

        long totalSearchTime = 0;
        // 1 lat = 111km， 10km = 0.091
        // 0.091 * 10 000 000 000 = 910 000 000
        for (Point2D task : m.taskSet) {
            System.out.println("Task " + task.id);
            begin = System.currentTimeMillis();
            Rectangle rect = new Rectangle(task.x.subtract(new BigInteger("910000000")), task.x.add(new BigInteger
                    ("910000000")), task.y.subtract(new BigInteger("910000000")), task.y.add(new BigInteger
                    ("910000000")));
            Set<Point2D> candidateSet = new HashSet<>();
            m.secSearch(m.sl.tree.root, rect, candidateSet);


            if (candidateSet.size() != 0) {
                BigInteger min = new BigInteger("9999999999999999999");
                Point2D target = null;
                for (Point2D candidate : candidateSet) {
                    BigInteger dis = m.secDisCal(m.sc.encrypt(task.x), m.sc.encrypt(task.y),
                            m.sc.encrypt(candidate.x), m.sc.encrypt(candidate.y));
                    if (dis.compareTo(min) < 0) {
                        min = dis;
                        target = candidate;
                    }
                }
                System.out.println("For " + task.id + " found target worker " + (target != null ? target.id : null));
            } else {
                System.out.println("For " + task.id + " found 0 candidates");
            }
            end = System.currentTimeMillis();
            long delta = (end - begin) / 1000;
            totalSearchTime += delta;
            System.out.println("Search target worker cost: " + delta + " s");
            System.out.println("=============================");
        }

    }

    /**
     * @param flag true for local test, false for server test
     */
    public void init(boolean flag) {
        if (flag) {
            workerSet = FileUtils.generatePointSet("src/main/resources/worker.txt", 8);
            taskSet = FileUtils.generatePointSet("src/main/resources/task.txt", 3);
        } else {
            workerSet = FileUtils.generatePointSet("worker.txt", 9000);
            taskSet = FileUtils.generatePointSet("task.txt", 5000);
        }
        logger.debug("Worker num: {}", workerSet.size());
        logger.debug("Task num: {}", taskSet.size());

        sc = new ComputingServer(workerSet);
        sl = new LogisticServer();
    }


    public void buildTree() {
        for (Point2D p : workerSet) {
            if (sl.tree.root == null) {
                sl.tree.root = new Node(p);
                sl.tree.count++;
                continue;
            }
            secInsert(sl.tree.root, new Node(p));
        }

        sl.printTree();
    }

    /**
     * @param c current node
     * @param n new node
     */
    public void secInsert(Node c, Node n) {
        boolean side = secVerifySide(c, n);
        while (!c.isLeaf()) {
            side = secVerifySide(c, n);
            if (side) {
                if (c.left == null) break;
                c = c.left;

            } else {
                if (c.right == null) break;
                c = c.right;

            }
        }
        sl.insert(c, n, side);
    }

    /**
     * @param c current node
     * @param p inserted node
     * @return
     */
    public boolean secVerifySide(Node c, Node p) {
        // Sl end
        Random ran = ThreadLocalRandom.current();
        BigInteger r_x = new BigInteger(3, ran);
        BigInteger r_y = new BigInteger(3, ran);
        boolean coin = ran.nextBoolean();
        BigInteger p_x = sc.encrypt(p.point.x).multiply(sc.encrypt(r_x));
        BigInteger p_y = sc.encrypt(p.point.y).multiply(sc.encrypt(r_y));
        BigInteger c_x = sc.encrypt(c.point.x).multiply(sc.encrypt(r_x));
        BigInteger c_y = sc.encrypt(c.point.y).multiply(sc.encrypt(r_y));
        BigInteger[] tetrad;
        if (coin) {
            tetrad = new BigInteger[]{p_x, c_x, p_y, c_y};
        } else {
            tetrad = new BigInteger[]{c_x, p_x, c_y, p_y};
        }
        BigInteger e_c_id = sc.encrypt(c.id);

        // Sc end
        BigInteger c_id = sc.decrypt(e_c_id);
        assert c_id.equals(c.id);
        BigInteger delta;
        if (sc.dict.get(c_id)) {
            delta = sc.decrypt(tetrad[0]).subtract(sc.decrypt(tetrad[1]));
        } else {
            delta = sc.decrypt(tetrad[2]).subtract(sc.decrypt(tetrad[3]));
        }
        BigInteger e_q;
        if (delta.compareTo(BigInteger.ZERO) < 0) {
            e_q = sl.encrypt(BigInteger.ONE);
        } else {
            e_q = sl.encrypt(BigInteger.ZERO);
        }

        // Sl end
        BigInteger q = sl.decrypt(e_q);
        if (coin) {
            return q.equals(BigInteger.ONE);
        } else {
            return q.equals(BigInteger.ZERO);
        }
    }

    public void secSearch(Node c, Rectangle rect, Set<Point2D> workerSet) {
        // Sl end
        boolean b1 = secComp(sc.encrypt(c.point.x), sc.encrypt(rect.xMin));
        boolean b2 = secComp(sc.encrypt(c.point.x), sc.encrypt(rect.xMax));
        boolean b3 = secComp(sc.encrypt(c.point.y), sc.encrypt(rect.yMin));
        boolean b4 = secComp(sc.encrypt(c.point.y), sc.encrypt(rect.yMax));

        boolean dim = sc.dict.get(c.id);
        if (dim) {
            if (b1 && c.right != null)
                secSearch(c.right, rect, workerSet);
            if (!b2 && c.left != null) secSearch(c.left, rect, workerSet);
            else {
                if (b4 && !b3) workerSet.add(c.point);
                if (c.left != null) secSearch(c.left, rect, workerSet);
                if (c.right != null) secSearch(c.right, rect, workerSet);
            }
        } else {
            if (b3 && c.right != null) secSearch(c.right, rect, workerSet);
            if (!b4 && c.left != null) secSearch(c.left, rect, workerSet);
            else {
                if (b2 && !b1) workerSet.add(c.point);
                if (c.left != null) secSearch(c.left, rect, workerSet);
                if (c.right != null) secSearch(c.right, rect, workerSet);
            }
        }
    }

    public boolean secComp(BigInteger e_a, BigInteger e_b) {
        // Sl end
        Random ran = ThreadLocalRandom.current();
        BigInteger r = new BigInteger(3, ran);
        BigInteger aa = e_a.multiply(r);
        BigInteger bb = e_b.multiply(r);
        boolean coin = ran.nextBoolean();
        BigInteger[] two_tuple;
        if (coin) {
            two_tuple = new BigInteger[]{aa, bb};
        } else {
            two_tuple = new BigInteger[]{bb, aa};
        }

        // Sc end
        BigInteger delta = sc.decrypt(two_tuple[0]).subtract(sc.decrypt(two_tuple[1]));
        BigInteger e_q;
        if (delta.compareTo(BigInteger.ZERO) < 0) {
            e_q = sl.encrypt(BigInteger.ONE);
        } else {
            e_q = sl.encrypt(BigInteger.ZERO);
        }

        // Sl end
        BigInteger q = sl.decrypt(e_q);
        if (coin) {
            return q.equals(BigInteger.ONE);
        } else {
            return q.equals(BigInteger.ZERO);
        }

    }

    public BigInteger secDisCal(BigInteger e_p1_x, BigInteger e_p1_y, BigInteger e_p2_x, BigInteger e_p2_y) {
        // Sl end
//        BigInteger e_delta_x = e_p1_x.multiply(e_p2_x.pow(sc.paillier.n.subtract(BigInteger.ONE).intValue()));
        BigInteger e_delta_x = e_p1_x.multiply(e_p2_x.modPow(sc.paillier.n.subtract(BigInteger.ONE),
                sc.paillier.nSquare));
//        BigInteger e_delta_y = e_p1_y.multiply(e_p2_y.pow(sc.paillier.n.subtract(BigInteger.ONE).intValue()));
        BigInteger e_delta_y = e_p1_y.multiply(e_p2_y.modPow(sc.paillier.n.subtract(BigInteger.ONE),
                sc.paillier.nSquare));

        // Sc and Sl end
        BigInteger d_x = secMul(e_delta_x, e_delta_x);
        BigInteger d_y = secMul(e_delta_y, e_delta_y);

        // Sl end
        BigInteger d = d_x.multiply(d_y);
        return d;

    }

    public BigInteger secMul(BigInteger e_x1, BigInteger e_x2) {
        // Sl end
        Random ran = ThreadLocalRandom.current();
        BigInteger r1 = new BigInteger(3, ran);
        BigInteger r2 = new BigInteger(3, ran);
        BigInteger xx1 = e_x1.multiply(sc.encrypt(r1));
        BigInteger xx2 = e_x2.multiply(sc.encrypt(r2));

        // Sc end
        BigInteger m1 = sc.decrypt(xx1);
        BigInteger m2 = sc.decrypt(xx2);
        BigInteger m = m1.multiply(m2).mod(sc.paillier.n);
        BigInteger e_m = sc.encrypt(m);

        // Sl end
        BigInteger s1 = e_x1.modPow(sc.paillier.n.subtract(r2), sc.paillier.nSquare);
        BigInteger s2 = e_x2.modPow(sc.paillier.n.subtract(r1), sc.paillier.nSquare);
        BigInteger s3 = sc.encrypt(r1.multiply(r2)).modPow(sc.paillier.n.subtract(BigInteger.ONE), sc.paillier.nSquare);
        BigInteger res = e_m.multiply(s1).multiply(s2).multiply(s3);
        return res;
    }
}

//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//
///**
// * @author sunyue
// * @version 1.0
// * @create 2017/7/29 20:00
// */
//public class KdTreeTest {
//    private KdTree tree;
//    private Point2D p1 = new Point2D(1, 3);
//    private Point2D p2 = new Point2D(4, 2);
//    private Point2D p3 = new Point2D(2, 5);
//    private Point2D p4 = new Point2D(3, 7);
//    private Point2D p5 = new Point2D(1, 6);
//    private Point2D p6 = new Point2D(10, 2);
//
//    @Before
//    public void setUp() throws Exception {
//        tree = new KdTree(new Rectangle(0, 100, 0, 100));
//        tree.insert(p1);
//        tree.insert(p2);
//        tree.insert(p3);
//        tree.insert(p4);
//        tree.insert(p5);
//        tree.insert(p6);
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        tree = null;
//    }
//
//    @Test
//    public void nearestTest() {
//        Assert.assertEquals(p4, tree.nearest(new Point2D(3, 6)));
//        Assert.assertEquals(p6, tree.nearest(new Point2D(10, 1)));
//        Assert.assertEquals(p2, tree.nearest(new Point2D(3, 3)));
//        Assert.assertEquals(p1, tree.nearest(new Point2D(0, 0)));
//    }
//
//    @Test
//    public void containsTest() {
//        Assert.assertEquals(true, tree.contains(new Point2D(1, 3)));
//        Assert.assertEquals(false, tree.contains(new Point2D(2, 3)));
//        Assert.assertEquals(false, tree.contains(new Point2D(3, 4)));
//    }
//}
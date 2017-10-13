import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;

/**
 * @author sunyue
 * @version 1.0
 * @createdOn 2017/8/1 22:04
 */
public class LogisticServer {
    public static final Logger logger = LoggerFactory.getLogger(LogisticServer.class);
    public SkdTree tree;
    public Paillier paillier;

    public LogisticServer() {
        tree = new SkdTree();
        paillier = new Paillier();
        logger.debug("LogisticServer's paillier: {}", paillier.toString());
    }


    public BigInteger encrypt(BigInteger m) {
        return paillier.encrypt(m);
    }

    public BigInteger decrypt(BigInteger c) {
        return paillier.decrypt(c);
    }

    public Node insert(Node c, Node n, boolean coordinate) {
        return tree.insert(c, n, coordinate);
    }

    public void printTree() {
        tree.print();
    }
}

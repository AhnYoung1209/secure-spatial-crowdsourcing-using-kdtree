import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author sunyue
 * @version 1.0
 * @createdOn 2017/8/1 22:05
 */
public class ComputingServer {
    private static Logger logger = LoggerFactory.getLogger(ComputingServer.class);
    public Map<BigInteger, Boolean> dict;
    public Paillier paillier;

    public ComputingServer(Set<Point2D> workerSet) {
        paillier = new Paillier();
        initDimensionalDict(workerSet);
        logger.debug("ComputingServer's paillier: {}", paillier.toString());
    }

    private void initDimensionalDict(Set<Point2D> workerSet) {
        dict = new HashMap<>(workerSet.size());
        Random ran = ThreadLocalRandom.current();
        for (Point2D p : workerSet) {
            dict.put(p.id, ran.nextBoolean());
        }

    }

    public BigInteger encrypt(BigInteger m) {
        return paillier.encrypt(m);
    }

    public BigInteger decrypt(BigInteger c) {
        return paillier.decrypt(c);
    }

}

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;

/**
 * @author sunyue
 * @version 1.0    2017/7/29 11:55
 */
public class PaillierTest {
    private BigInteger b0 = BigInteger.ZERO;
    private BigInteger b1 = BigInteger.ONE;
    private BigInteger b20 = new BigInteger("20");
    private BigInteger b100 = new BigInteger("100");
    private Paillier p;

    @Before
    public void setUp() throws Exception {
        p = new Paillier();
    }

    @After
    public void tearDown() throws Exception {
        p = null;
    }

    @Test
    public void basicTest() {
        Assert.assertEquals(BigInteger.ZERO, p.decrypt(p.encrypt(BigInteger.ZERO)));
        Assert.assertEquals(BigInteger.ONE, p.decrypt(p.encrypt(BigInteger.ONE)));
        Assert.assertEquals(new BigInteger("10"), p.decrypt(p.encrypt(new BigInteger("10"))));
        Assert.assertEquals(new BigInteger("1000"), p.decrypt(p.encrypt(new BigInteger("1000"))));
        Assert.assertEquals(new BigInteger("10"), p.decrypt(p.encrypt(new BigInteger("10"))));
    }

    @Test
    public void addTest() {
        Assert.assertEquals(b0.add(b1), p.decrypt(p.homomorphicAdd(b0, b1)).mod(p.n));
        Assert.assertEquals(b0.add(b20), p.decrypt(p.homomorphicAdd(b0, b20)).mod(p.n));
        Assert.assertEquals(b1.add(b20), p.decrypt(p.homomorphicAdd(b1, b20)).mod(p.n));
        Assert.assertEquals(b20.add(b100), p.decrypt(p.homomorphicAdd(b20, b100)).mod(p.n));
    }

    @Test
    public void multiplyTest() {
        Assert.assertEquals(b0.multiply(b1), p.decrypt(p.homomorphicMultiply(b0, b1)).mod(p.n));
        Assert.assertEquals(b0.multiply(b20), p.decrypt(p.homomorphicMultiply(b0, b20)).mod(p.n));
        Assert.assertEquals(b1.multiply(b20), p.decrypt(p.homomorphicMultiply(b1, b20)).mod(p.n));
        Assert.assertEquals(b20.multiply(b100), p.decrypt(p.homomorphicMultiply(b20, b100)).mod(p.n));
    }
}
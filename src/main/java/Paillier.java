import com.google.common.base.MoreObjects;

import java.math.BigInteger;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Paillier cryptosystem
 * Public Key is given by (n,g)
 * Secure Key is given by lambda
 * Reference link：https://en.wikipedia.org/wiki/Paillier_cryptosystem
 *
 * @author sunyue
 * @version 1.0
 * @createdOn 2017/7/27 21:18
 */
public class Paillier {
    /**
     * n = p*q, where p and q are two large primes.
     */
    public BigInteger n;
    /**
     * Square = n*n
     */
    public BigInteger nSquare;
    /**
     * a random integer in Z*_{n^2} where gcd (L(g^lambda mod n^2), n) = 1.
     */
    public BigInteger g;
    /**
     * big prime
     */
    private BigInteger p;
    /**
     * big prime
     */
    private BigInteger q;
    /**
     * lambda = lcm(p-1, q-1) = (p-1)*(q-1)/gcd(p-1, q-1).
     */
    private BigInteger lambda;
    /**
     * number of bits of modulus
     */
    private int bitLength;


    /**
     * Constructs an instance of the Paillier cryptosystem.
     *
     * @param bitLengthVal number of bits of modulus
     * @param certainty    The probability that the new BigInteger represents a prime
     *                     number will exceed (1 - 2^(-certainty)). The execution time of
     *                     this constructor is proportional to the value of this
     *                     parameter.
     */
    public Paillier(int bitLengthVal, int certainty) {
        keyGeneration(bitLengthVal, certainty);
    }

    /**
     * Constructs an instance of the Paillier cryptosystem with 512 bits of
     * modulus and at least 1-2^(-64) certainty of primes generation.
     */
    public Paillier() {
        keyGeneration(512, 64);
    }

    /**
     * Sets up the public key and private key.
     *
     * @param bitLengthVal number of bits of modulus.
     * @param certainty    The probability that the new BigInteger represents a prime
     *                     number will exceed (1 - 2^(-certainty)). The execution time of
     *                     this constructor is proportional to the value of this
     *                     parameter.
     */
    private void keyGeneration(int bitLengthVal, int certainty) {
        bitLength = bitLengthVal;
        p = new BigInteger(bitLength / 2, certainty, ThreadLocalRandom.current());
        q = new BigInteger(bitLength / 2, certainty, ThreadLocalRandom.current());
        n = p.multiply(q);
        nSquare = n.multiply(n);
        g = new BigInteger("2");
        lambda = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE))
                .divide(p.subtract(BigInteger.ONE).gcd(q.subtract(BigInteger.ONE)));

        if (g.modPow(lambda, nSquare).subtract(BigInteger.ONE).divide(n).gcd(n).intValue() != 1) {
            System.out.println();
        }
    }

    /**
     * Encrypts plaintext m.
     * ciphertext c = g^m * r^n mod n^2.
     * This function explicitly requires random input r to help with encryption.
     *
     * @param m plaintext as a BigInteger
     * @param r random plaintext top help with encryption
     * @return ciphertext as a BigInteger
     */
    private BigInteger encrypt(BigInteger m, BigInteger r) {
        return g.modPow(m, nSquare).multiply(r.modPow(n, nSquare)).mod(nSquare);
    }

    /**
     * Encrypts plaintext m which c = g^m * r^n mod n^2.
     * This function automatically generates random input r to help with encryption.
     *
     * @param m plaintext as a BigInteger
     * @return ciphertext as a BigInteger
     */
    public BigInteger encrypt(BigInteger m) {
        BigInteger r = new BigInteger(bitLength, ThreadLocalRandom.current());
        return encrypt(m, r);
    }

    /**
     * Decrypts ciphertext c.
     * plaintext m = L(c^lambda mod n^2) *u mod n, where
     * u = (L(g^lambda mod n^2))^(-1) mod n.
     *
     * @param c ciphertext as a BigInteger
     * @return plaintext as a BigInteger
     */
    public BigInteger decrypt(BigInteger c) {
        BigInteger u = g.modPow(lambda, nSquare).subtract(BigInteger.ONE).divide(n).modInverse(n);
        return c.modPow(lambda, nSquare).subtract(BigInteger.ONE).divide(n).multiply(u).mod(n);
    }

    /**
     * Homomorphic addition of plaintexts.
     * The product of two ciphertexts will decrypt to the sum of their corresponding plaintexts,
     *
     * @param m1 one plaintext as a BigInteger
     * @param m2 another plaintext as a BigInteger
     * @return Product of two ciphertext as a BigInteger
     */
    public BigInteger homomorphicAdd(BigInteger m1, BigInteger m2) {
        return encrypt(m1).multiply(encrypt(m2)).mod(nSquare);
    }


    /**
     * Homomorphic multiplication of plaintexts.
     * An encrypted plaintext raised to the power of another plaintext will decrypt to the product of the two
     * plaintexts.
     *
     * @param m1 one plaintext as a BigInteger
     * @param m2 another plaintext as a BigInteger
     * @return E(m1)^m2 mod n^2. A ciphertext raised to the power of another plaintext
     */
    public BigInteger homomorphicMultiply(BigInteger m1, BigInteger m2) {
        return encrypt(m1).modPow(m2, nSquare);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("n", n)
                .add("nSquare", nSquare)
                .add("g", g)
                .add("p", p)
                .add("q", q)
                .add("lambda", lambda)
                .add("bitLength", bitLength)
                .toString();
    }
}


package com.bizblock.library.security;

import org.jasypt.digest.StandardStringDigester;

/**
 * <p>
 * This <i>Class</i> is used during authentication to match a user's password with the user's encrypted password. The <i>Class</i> does this by taking the encrypted password (also called digest) of
 * the user (usually retrieved with a database query), and then determines if this password is the same password that produced the retrieved digest. This process is called <i>Matching</i>
 * and if it succeeds, then the password is said to match the given digest.
 * </p>
 *
 * @author Praise
 *
 * @version 1.1
 *
 * @see Digester
 */
public final class DigestMatcher
{
    /**
     * The <code>StandardStringDigester Object</code> contained in the Jasypt API package libraries.
     */
    private StandardStringDigester SSD = new StandardStringDigester();

    /**
     * The Client's user password retrieved at runtime during a login process.
     */
    protected String PASSWORD;

    /**
     * The salt that was appended to the digest during encryption.
     */
    public String SALT;

    /**
     * The actual end product of the encryption digest plus the salt before it was reversed.
     */
    protected String DIGEST;

    /**
     * Method used in retrieving the salt from a given digest.
     * <p>
     * This method should be called prior to the calling of the <code>doMatch</code> method, and the parameter passed in should be the encrypted password of the user usually retrieved with a database
     * query.
     * </p>
     *
     * @param reverseDigest The encrypted password of the user that was stored in the database.
     * @return The salt that will be used by the <code>doMatch</code> method.
     * @see Digester
     */
    public String getSalt(String reverseDigest)
    {
        this.DIGEST = reverseDigest;
        this.SALT = new String("");
        String restoreDigest = new StringBuffer(reverseDigest).reverse().toString();
        for(int i = 0; i <= restoreDigest.length() - 41; i++)
            this.SALT += Character.toString(restoreDigest.charAt(i));

        return this.SALT;
    }

    /**
     * Method used in retrieving the true end product of the last encryption iteration process.
     *
     * @return The true digest that will be used in matching the password in the <code>doMatch</code> method.
     * @see Digester
     */
    protected String getDigest()
    {
        String realDigest = new String("");
        for(int i = 0; i <= 39; i++)
            realDigest += Character.toString(this.DIGEST.charAt(i));
        String reverseDigest = new StringBuffer(realDigest).reverse().toString();

        return reverseDigest;
    }

    /**
     * Method used to determine if a password matches a specified digest.
     * <p>
     * The <code>getSalt</code> method should be called before calling this method.<br>
     * The following takes place when this method is called:<br>
     * <ul>
     * <li>The method gets the length of the password <code>String.</code></li>
     * <li>The encryption algorihm is set to SHA-1.</li>
     * <li>The number of iterations is set to the password length multiplied by 256 multiplied by 11.</li>
     * <li>The <code>StandardStringDigester.matches</code> method performs the matching process on the reverse of the password+salt <code>String</code> concatenation with the true digest retrieved
     * from the <code>getDigest</code> method.
     * </li>
     * <li>The method returns <font color="green"><b><i>true</i></b></font> if the combination matches else it returns <font color="red"><b><i>false</i></b></font>.
     * </li>
     * <ul>
     * </p>
     *
     * @param password The user's password usually provided by the user at login process.
     * @param salt The salt that was initialy generated and appended to the final digest.
     * @return              <font color="green"><b><i>true</i></b></font> if the password matches the given digest, else it returns <font color="red"><b><i>false</i></b></font>.
     * @see Digester
     */
    public boolean doMatch(String password, String salt)
    {
        this.PASSWORD = new String(password);
        int passLength = this.PASSWORD.length();
        this.SSD.setAlgorithm("SHA-1");
        this.SSD.setIterations(passLength * 256 * 11);
        boolean isMatched = this.SSD.matches(new String(new StringBuffer(this.PASSWORD + salt).reverse()), this.getDigest());

        return isMatched;
    }
}

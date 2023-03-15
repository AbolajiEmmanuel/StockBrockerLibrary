package com.bizblock.library.security;

import javax.swing.JOptionPane;
import org.jasypt.digest.StandardStringDigester;
import org.jasypt.salt.RandomSaltGenerator;

/**
 * <p>
 * This <i>Class</i> is used to encrypt user passwords through digestion cryptography according to the JASYPT (JAva Simplified encrYPTion) standard API. <b>See
 * <a href="http://www.jasypt.org">www.jasypt.org</a></b>
 * Classes and servlets that do sign-up processes must instantiate an instance of this <i>Class</i>
 * in order to encrypt/digest the user password before storing it in the database. Programmers are advised to simply call the <code>doDigest</code> method of this class with the user's password as the
 * single <code>String</code> argument. The <code>doDigest</code> method will then take in the argument that was passed in and perform a <i>one-way</i> encryption (called digestion) on it using the
 * widely used <b>SHA-1</b> encryption algorithm.
 * </p>
 * <p>
 * The following details illustrates the processes and techniques used with this class:<br><br>
 * <ol>
 * <li>The user requests for a sign-up form, fills the form including his/her choosen password and then submits the form.</li>
 * <li>A <i>Servlet</i> receives the request as a <b>POST</b> request and processes it for validation.</li>
 * <li>If the form is validated, an instance of this <i>Class</i> should/would be called.</li>
 * <li>The password should be passed as a single <code>String</code> argument on the <code>doDigest</code> method</li>
 * <li>This method then returns the encrypted version of the password which should be received by a <code>String</code> object.</li>
 * <li>Finally the <code>String</code> object is concatenated in a dynamic database query for data persistence storage</li>
 * </ol>
 * <br>
 * <br>
 * <b>Note:</b> The <code>DigestMatcher.doMatch</code> method must be used to handle the user authentication for login validation processes.
 * </p>
 * <p>
 * The <code>getPassword</code> method is an optional method, which may be useful in a Java console based or GUI application/<i>Class</i> which must also instantiate an instance of this <i>Class</i>.
 * </p>
 *
 * @author Praise
 *
 * @version 1.1
 *
 * @see DigestMatcher
 */
public final class Digester
{
    /**
     * The <code>StandardStringDigester Object</code> contained in the Jasypt API package libraries.
     */
    private StandardStringDigester SSD = new StandardStringDigester();

    /**
     * The <code>RandomSaltGenerator Object</code> contained in the Jasypt API package libraries.
     */
    private RandomSaltGenerator RSG = new RandomSaltGenerator();

    /**
     * The Client's user password retrieved at runtime during a sign-up process.
     */
    protected String PASSWORD;

    /**
     * A salt is a <code>String</code> of characters generated to aid in the digestion process.
     */
    protected String SALT;

    /**
     * This field is active in the optional <code>getPassword</code> method. It is used to hold the specified password for digestion tests.
     *
     * @deprecated <font color="red">This field is deprecated and may be removed in the future or replaced.</font>
     */
    @Deprecated
    protected static String rawPassword;

    /**
     * This method is strictly for the Java Developer in a development environment, be it a console based or GUI application. It cannot be conviniently called by a <i>Servlet</i>, <i>Scriptlet</i>,
     * <i>Facelet</i>, <i>Applet</i> or any of the Java web form classes. Developers are advised not to use this method except for testing purposes. Also, this method is highly associated with the <code>RunDigester
     * </code><i>Class.</i>
     *
     * @return The password used for testing in the <code>JOptionPane</code> input dialog.
     * @see RunDigester
     * @deprecated  <font color="red">This method is marked for future replacement with a more convenient method.</font>
     */
    @Deprecated
    protected static String getPassword()
    {
        rawPassword = new String(JOptionPane.showInputDialog("Enter your password"));

        return rawPassword;
    }

    /**
     * Method used in performing a <i>one-way</i> encryption/digestion on passwords.
     * <p>
     * The following takes place when this method is called:<br>
     * <ul>
     * <li>This method takes in the user password as a single <code>String</code> argument.</li>
     * <li>The password length is retrieved.</li>
     * <li>A random <code>byte</code> character is generated and converted to a <code>String</code> called <i>SALT.</i></li>
     * <li>The encryption algorithm is chosen as SHA-1.</li>
     * <li>The number of iterations refers to the number of times the digestion will take place on this particular password.</li>
     * <li>The number of iterations is set using the password length multiplied by 256 which is further multiplied by 11.</li>
     * <li>The number of iterations is unknown to the developer since he/she does not know the user's password. (Take a wild Guess!)</li>
     * <li>The <i>SALT</i> is concatenated to the password and the resulting <code>String</code> is reversed using the <code>StringBuffer.reverse()</code> method.</li>
     * <li>The <code>StringBuffer</code> is passed in as an argument to a <code>String</code> constructor which instantiates a new <code>String</code> <code>Object.</code></li>
     * <li>The <code>StandardStringDigester.digest</code> method then performs a digest on the password+<i>SALT</i> reverse combination.</li>
     * <li>The digestion/encryption is repeated according to the number of iterations specified above.</li>
     * <li>A newly instantiated <code>StringBuffer</code> <code>Object</code> then reverses the last result of the encrypted <code>String</code>+<i>SALT.</i></li>
     * <li>Finally the <code>StringBuffer Object</code> is converted to a <code>String Object</code> and retured to this method's caller <code>Object</code></li>
     * </ul>
     * </p>
     *
     * @param password The user's password choosen by the user.
     * @return The end product of the digestion processes.
     * @see DigestMatcher
     */
    public String doDigest(String password)
    {
        this.PASSWORD = new String(password);
        int passLength = this.PASSWORD.length();
        this.SALT = new String(this.RSG.generateSalt(11).toString());
        this.SSD.setAlgorithm("SHA-1");
        this.SSD.setIterations(passLength * 256 * 11);
        String digest = this.SSD.digest(new String(new StringBuffer(this.PASSWORD + this.SALT).reverse()));

        return new StringBuffer(this.SALT + digest).reverse().toString();
    }
}

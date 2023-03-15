 
package com.bizblock.library.user;

import static com.bizblock.library.user.User.USERS;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Praise
 * @since Mar 13, 2023 12:11:18 PM
 */
@Entity
@Table(name = USERS)
public class User implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    private String userName;
    private String email;
    @Column(name = PASSWORD)
    private String encrytedPassword;
    private String firstName;
    private String lastName;
    private Timestamp dateTimeCreated;

    public User()
    {
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEncrytedPassword()
    {
        return encrytedPassword;
    }

    public void setEncrytedPassword(String encrytedPassword)
    {
        this.encrytedPassword = encrytedPassword;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public Timestamp getDateTimeCreated()
    {
        return dateTimeCreated;
    }

    public void setDateTimeCreated(Timestamp dateTimeCreated)
    {
        this.dateTimeCreated = dateTimeCreated;
    }

    public static final String USERS = "users";
    public static final String USER_NAME = "userName";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String DATE_TIME_CREATED = "dateTimeCreated";

}

 
package com.bizblock.library.user;

import static com.bizblock.library.user.UserToken.USER_TOKEN;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Praise
 * @since Mar 13, 2023 2:27:47 PM
 */
@Entity
@Table(name = USER_TOKEN)
public class UserToken implements Serializable
{
    private String userName;
    @Id
    private String token;
    private Timestamp expiryDate;
    private int lifeSpan;

    public UserToken()
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

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public Timestamp getExpiryDate()
    {
        return expiryDate;
    }

    public void setExpiryDate(Timestamp expiryDate)
    {
        this.expiryDate = expiryDate;
    }

    public int getLifeSpan()
    {
        return lifeSpan;
    }

    public void setLifeSpan(int lifeSpan)
    {
        this.lifeSpan = lifeSpan;
    }

    public static final String USER_TOKEN = "userTokens";
    public static final String USER_NAME = "userName";
    public static final String TOKEN = "token";
    public static final String EXPIRY_DATE = "expiryDate";
    public static final String LIFE_SPAN = "lifeSpan";
}

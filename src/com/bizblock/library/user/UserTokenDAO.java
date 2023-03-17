package com.bizblock.library.user;

import com.bizblock.library.database.DBConfiguration;
import static com.bizblock.library.user.UserToken.*;
import com.bizblock.user.util.RandomNumberGenerator;
import java.util.List;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Praise
 * @since Mar 13, 2023 2:31:02 PM
 */
public class UserTokenDAO
{
    public static void registerNewUserToken(UserToken userToken) throws Exception, EntityExistsException
    {
        try( DBConfiguration dbConfig = new DBConfiguration())
        {
            EntityManager em = dbConfig.getEntityManager();
            em.getTransaction().begin();
            em.persist(userToken);
            em.getTransaction().commit();
        }
    }

    public static UserToken getUserTokenByUserName(String userName) throws Exception
    {
        try( DBConfiguration dbConfig = new DBConfiguration())
        {
            EntityManager em = dbConfig.getEntityManager();
            String sql = "SELECT * FROM " + USER_TOKEN + " WHERE " + USER_NAME + " = ? ORDER BY " + EXPIRY_DATE + " DESC";
            Query q = em.createNativeQuery(sql, UserToken.class);
            q.setParameter(1, userName);
            List<UserToken> tokens = q.getResultList();
            if(tokens.isEmpty())
                return null;
            else
                return tokens.get(0);
        }
    }

    public static String generateUniqueUserToken() throws Exception
    {
        String token = null;
        try( DBConfiguration dbConfig = new DBConfiguration())
        {
            EntityManager em = dbConfig.getEntityManager();
            UserToken userToken;
            do
            {
                token = RandomNumberGenerator.generateRandomAlphanumericCharacters(100, true);
                userToken = em.find(UserToken.class, token);
            }
            while(userToken != null);
            return token;
        }
    }

    public static void updateUserTokenExpiryDateAndLifeSpan(UserToken userToken)
    {
        EntityManager em = DBConfiguration.createEntityManager();
        try
        {
            userToken = em.find(UserToken.class, userToken.getToken());
            em.getTransaction().begin();
            userToken.setExpiryDate(userToken.getExpiryDate());
            userToken.setLifeSpan(userToken.getLifeSpan());
            em.getTransaction().commit();
        }
        finally
        {
            em.close();
        }
    }
}

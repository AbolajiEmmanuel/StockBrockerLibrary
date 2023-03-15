package com.bizblock.library.user;

import com.bizblock.library.database.DBConfiguration;
import com.bizblock.library.security.DigestMatcher;
import static com.bizblock.library.user.User.*;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author Praise
 * @since Mar 13, 2023 12:31:28 PM
 */
public class UserDAO
{

    public static void registerNewUser(User user) throws Exception, EntityExistsException
    {
        try( DBConfiguration dbConfig = new DBConfiguration())
        {
            EntityManager em = dbConfig.getEntityManager();
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        }
    }

    public static User loginUser(String userName, String password) throws Exception
    {
        User user = getUserByUserNameOrEmail(userName);
        if(user != null)
        {
            DigestMatcher matcher = new DigestMatcher();
            String salt = matcher.getSalt(user.getEncrytedPassword());
            boolean matched = matcher.doMatch(password, salt);
            if(matched)
            {
                user.setEncrytedPassword(null);
                return user;
            }
            else
                return null;
        }
        else
            return null;
    }

    public static User getUserByUserNameOrEmail(String userNameOrEmail) throws Exception
    {
        try( DBConfiguration dbConfig = new DBConfiguration())
        {
            EntityManager em = dbConfig.getEntityManager();
            String sql = "SELECT * FROM " + USERS + " WHERE " + EMAIL + " = ? OR " + USER_NAME + " = ?";
            Query q = em.createNativeQuery(sql, User.class);
            q.setParameter(1, userNameOrEmail);
            q.setParameter(2, userNameOrEmail);
            User user = (User)q.getSingleResult();
            return user;
        }
        catch(NoResultException nre)
        {
            return null;
        }
    }
}

package com.bizblock.library.user;

import com.bizblock.library.company.CompanyStock;
import com.bizblock.library.database.DBConfiguration;
import static com.bizblock.library.user.UserStock.*;
import com.bizblock.user.util.RandomNumberGenerator;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.List;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Praise
 * @since Mar 13, 2023 12:40:15 PM
 */
public class UserStockDAO
{
    public static void registerNewUserStock(UserStock userStock) throws Exception, EntityExistsException
    {
        try(DBConfiguration dbConfig = new DBConfiguration())
        {
            EntityManager em = dbConfig.getEntityManager();
            em.getTransaction().begin();
            em.persist(userStock);
            em.getTransaction().commit();
        }
    }

    public static UserStock getUserStockByUserNameAndCompanySymbol(String userName, String companySymbol) throws Exception
    {
        try(DBConfiguration dbConfig = new DBConfiguration())
        {
            EntityManager em = dbConfig.getEntityManager();
            String sql = "SELECT * FROM " + USER_STOCKS + " WHERE " + USER_NAME + " = ? AND " + SYMBOL + " = ?";
            Query q = em.createNativeQuery(sql, UserStock.class);
            q.setParameter(1, userName);
            q.setParameter(2, companySymbol);
            UserStock userStock = (UserStock)q.getSingleResult();
            return userStock;
        }
        catch(NoResultException nre)
        {
            return null;
        }
    }

    public static CompanyStock getStockByCompanySymbol(String companySymbol) throws Exception
    {
        try(DBConfiguration dbConfig = new DBConfiguration())
        {
            EntityManager em = dbConfig.getEntityManager();
            String sql = "SELECT * FROM " + CompanyStock.COMPANY_STOCKS + " WHERE " + CompanyStock.SYMBOL + " = ?";
            Query q = em.createNativeQuery(sql, CompanyStock.class);
            q.setParameter(1, companySymbol);
            CompanyStock stock = (CompanyStock)q.getSingleResult();
            return stock;
        }
        catch(NoResultException nre)
        {
            return null;
        }
    }

    public static void updateUserStock(UserStock userStock, int amount)
    {
        EntityManager em = DBConfiguration.createEntityManager();
        try
        {
            userStock = em.find(UserStock.class, userStock.getId());
            em.getTransaction().begin();
            userStock.setNumberOfShares(amount);
            System.out.println(userStock.getUserName());
            em.getTransaction().commit();
        }
        finally
        {
            em.close();
        }
    }

    public static void sellUserStock(String userName, String companySymbol, int noOfShares) throws Exception
    {
        UserStock userStock = getUserStockByUserNameAndCompanySymbol(userName, companySymbol);
        if(userStock != null)
            if(userStock.getNumberOfShares() > 0 && userStock.getNumberOfShares() >= noOfShares)
            {
                int newNumberOfShares = userStock.getNumberOfShares() - noOfShares;
                System.out.println(userStock.getNumberOfShares());
                updateUserStock(userStock, newNumberOfShares);
            }
            else
                throw new IllegalArgumentException("Insufficient stocks");
        else
            throw new IllegalArgumentException("user not found");

    }

    public static void buyUserStock(String userName, int noOfShares, String symbol) throws Exception
    {
        UserStock userStock = getUserStockByUserNameAndCompanySymbol(userName, symbol);
        if(userStock != null)
        {
            int newNumberOfShares = userStock.getNumberOfShares() + noOfShares;
            updateUserStock(userStock, newNumberOfShares);
        }
        else
        {
            CompanyStock stockName = getStockByCompanySymbol(symbol);
            UserStock newUserStock = new UserStock();
            newUserStock.setCompanyName(stockName.getName());
            newUserStock.setNumberOfShares(noOfShares);
            newUserStock.setSymbol(symbol);
            newUserStock.setUserName(userName);
            newUserStock.setId(generateUniqueUserID());
            registerNewUserStock(newUserStock);
        }
    }

    public static String generateUniqueUserID() throws Exception
    {
        String userId = null;
        try(DBConfiguration dbConfig = new DBConfiguration())
        {
            EntityManager em = dbConfig.getEntityManager();
            User user;
            do
            {
                userId = RandomNumberGenerator.generateRandomAlphanumericCharacters(7, false);
                user = em.find(User.class, userId);
            }
            while(user != null);
            return userId;
        }
    }

    public static List<UserStock> getAllUserStockByUserName(String username) throws Exception
    {

        try(DBConfiguration dbConfig = new DBConfiguration())
        {
            EntityManager em = dbConfig.getEntityManager();
            String sql = "SELECT * FROM " + USER_STOCKS + " WHERE " + USER_NAME + " = ?";
            Query q = em.createNativeQuery(sql, UserStock.class);
            q.setParameter(1, username);
            List<UserStock> userStocklist = q.getResultList();
            return userStocklist;
        }
    }

    public static double convertCurrency(double amount, String currencyPayment, String companyPayment) throws UnirestException, JSONException
    {
        HttpResponse<JsonNode> jsonResponse = Unirest.get("https://api.apilayer.com/exchangerates_data/latest")
                .header("apikey", "7Ct4899ogYI4n73hCpQ0RaNEgDTbzILC")
                .queryString("symbol", currencyPayment)
                .queryString("base", companyPayment)
                .asJson();
        JsonNode jsonNode = jsonResponse.getBody();
        JSONObject jsonoObject = jsonNode.getObject();
        JSONObject rates = jsonoObject.getJSONObject("rates");
        double rate = rates.getDouble(currencyPayment);
        double nairaValue = amount * rate;
        return nairaValue;

    }
}

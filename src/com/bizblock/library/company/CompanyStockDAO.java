package com.bizblock.library.company;

import com.bizblock.library.database.DBConfiguration;
import static com.bizblock.library.company.CompanyStock.COMPANY_STOCKS;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import java.util.Currency;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Praise
 * @since Mar 13, 2023 9:05:20 AM
 */
public class CompanyStockDAO
{
    public static void insertCompanyStock(CompanyStock companyStock) throws Exception
    {
        try( DBConfiguration dbConfig = new DBConfiguration())
        {
            EntityManager em = dbConfig.getEntityManager();
            em.getTransaction().begin();
            em.persist(companyStock);
            em.getTransaction().commit();
        }
    }

    public static List<CompanyStock> getAllCompanyStocks() throws Exception
    {
        try( DBConfiguration dbConfig = new DBConfiguration())
        {
            EntityManager em = dbConfig.getEntityManager();
            String sql = "SELECT * FROM " + COMPANY_STOCKS;
            Query q = em.createNativeQuery(sql, CompanyStock.class);
            List<CompanyStock> companyStocks = q.getResultList();
            return companyStocks;
        }
    }

    public static CompanyStock getCompanyStock(String symbol) throws Exception
    {
        try( DBConfiguration dbConfig = new DBConfiguration())
        {
            EntityManager em = dbConfig.getEntityManager();
            CompanyStock companyStock = em.find(CompanyStock.class, symbol);
            return companyStock;
        }
    }

    public static void updateCompanyStock(CompanyStock companyStock, int amountOfShares) throws Exception
    {
        try( DBConfiguration dbConfig = new DBConfiguration())
        {
            EntityManager em = dbConfig.getEntityManager();
            em.getTransaction().begin();
            CompanyStock cs = em.find(CompanyStock.class, companyStock.getSymbol());
            cs.setNumberOfShares(amountOfShares);
            em.getTransaction().commit();
        }
    }

    public static void loadCompanyStocks() throws Exception
    {
        HttpResponse<JsonNode> jsonResponse = Unirest.get("https://cloud.iexapis.com/stable/stock/market/list/mostactive")
                .queryString("token", "pk_41d531e4fdaa4dd8b75a2d0603937581").asJson();
        JsonNode jsonNode = jsonResponse.getBody();
        JSONArray companiesArray = jsonNode.getArray();
        for(int i = 0; i < companiesArray.length(); i++)
        {
            JSONObject jSONObject = companiesArray.getJSONObject(i);
            CompanyStock companyStock = new CompanyStock();
            companyStock.setSymbol(jSONObject.getString("symbol"));
            companyStock.setName(jSONObject.getString("companyName"));
            companyStock.setSharePrice(jSONObject.getDouble("latestPrice"));
            companyStock.setCurrencyName(jSONObject.getString("currency"));
            companyStock.setNumberOfShares(jSONObject.getInt("previousVolume"));
            companyStock.setCurrency(Currency.getInstance(jSONObject.getString("currency")).getSymbol());
            companyStock.setLogo("");
            System.out.println("Company Inserting...: " + jSONObject.getString("companyName"));
            insertCompanyStock(companyStock);
        }
    }
}

package com.bizblock.library.company;

import static com.bizblock.library.company.CompanyStock.COMPANY_STOCKS;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Praise
 * @since Mar 13, 2023 8:56:36 AM
 */
@Entity
@Table(name = COMPANY_STOCKS)
public class CompanyStock implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    private String symbol;
    private String name;
    private String logo;
    private String currency;
    private String currencyName;
    private double sharePrice;
    private int numberOfShares;

    public CompanyStock()
    {
    }

    public String getSymbol()
    {
        return symbol;
    }

    public void setSymbol(String symbol)
    {
        this.symbol = symbol;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getLogo()
    {
        return logo;
    }

    public void setLogo(String logo)
    {
        this.logo = logo;
    }

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    public String getCurrencyName()
    {
        return currencyName;
    }

    public void setCurrencyName(String currencyName)
    {
        this.currencyName = currencyName;
    }

    public double getSharePrice()
    {
        return sharePrice;
    }

    public void setSharePrice(double sharePrice)
    {
        this.sharePrice = sharePrice;
    }

    public int getNumberOfShares()
    {
        return numberOfShares;
    }

    public void setNumberOfShares(int numberOfShares)
    {
        this.numberOfShares = numberOfShares;
    }

    public static final String COMPANY_STOCKS = "companyStocks";
    public static final String SYMBOL = "symbol";
    public static final String NAME = "name";
    public static final String LOGO = "logo";
    public static final String CURRENCY = "currency";
    public static final String CURRENCY_NAME = "currencyName";
    public static final String SHARE_PRICE = "sharePrice";
    public static final String NUMBER_OF_SHARES = "numberOfShares";
}

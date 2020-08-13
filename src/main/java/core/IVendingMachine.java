package core;

import java.util.List;

public interface IVendingMachine {

    /**
     *  Vending Machine manufacturer.
     */
    String getManufacturer();

    /**
     * Amount of money inserted in vending machine.
     */
    Money getAmount();

    /**
     * Products that are sold.
     * @return array of products
     */
    List<Product> getProduct();

    /**
     * Set product array
     */
    void setProduct(List<Product> products);

    /**
     * Inserts coins into vending machine.
     * @param amount coin amount
     * @return sum of inserted coin values
     */
    Money insertCoin(Money amount);

    /**
     * Returns all inserted coins to user.
     * @return sum of inserted coin values
     */
    Money returnMoney();

    /**
     * Buys product from the list of product.
     * @param productNumber product number in vending machine product list
     * @return Product
     */
    Product buy(int productNumber);
}

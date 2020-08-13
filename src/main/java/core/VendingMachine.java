package core;

import exceptions.InsufficientMoneyAmountException;
import exceptions.ProductListIsEmptyException;
import exceptions.ProductUnavailableException;
import exceptions.UnacceptableCoinException;

import java.util.Arrays;
import java.util.List;

public class VendingMachine implements IVendingMachine {
    private static final String MANUFACTURER = "Vending machine LTD";
    private Money amount = new Money(0, 0);
    private List<Product> products;

    /**
     * Vending Machine manufacturer.
     */
    public String getManufacturer() {
        return MANUFACTURER;
    }

    /**
     * Amount of money inserted in vending machine.
     */
    public Money getAmount() {
        return amount;
    }

    /**
     * Products that are sold.
     *
     * @return array of products
     */
    public List<Product> getProduct() {
        return products;
    }

    /**
     * Set product array
     *
     * @param products list of products
     */
    public void setProduct(List<Product> products) {
        this.products = products;
    }

    /**
     * Inserts coins into vending machine.
     *
     * @param amount coin amount
     * @return sum of inserted coin values
     */
    public Money insertCoin(Money amount) {
        int previouslyAddedEuros = this.amount.getEuros();
        int previouslyAddedCents = this.amount.getCents();
        int addedEuros = amount.getEuros();
        int addedCents = amount.getCents();

        if (!isCoinAcceptable(amount)) {
            throw new UnacceptableCoinException("Coin is not acceptable!");
        }
        this.amount = new Money(previouslyAddedEuros + addedEuros, previouslyAddedCents + addedCents);
        return this.amount;
    }

    /**
     * Returns all inserted coins to user.
     *
     * @return sum of inserted coin values
     */
    public Money returnMoney() {
        return this.amount;
    }

    /**
     * Buys product from the list of product.
     *
     * @param productNumber product number in vending machine product list
     * @return Product
     */
    public Product buy(int productNumber) {
        if (products == null) {
            throw new ProductListIsEmptyException("There are no products in vending machine");
        } else if (productNumber >= products.size()) {
            throw new ProductUnavailableException(String.format("Product with id '%d' is not available in product list", productNumber));
        }
        Product product = products.get(productNumber);
        int productAmount = product.getAvailable();
        Money productPrice = product.getPrice();
        if (productAmount == 0) {
            throw new ProductUnavailableException(String.format("Product with id '%d' is not available", productNumber));
        }
        if (!canAffordProduct(product)) {
            throw new InsufficientMoneyAmountException(String.format(
                    "Insufficient money amount. Product price %d.%d, current balance %d.%d.",
                    productPrice.getEuros(), productPrice.getCents(), amount.getEuros(), amount.getCents()));
        }
        // decrease amount of available products
        product.setAvailable(productAmount - 1);
        // decrease amount of available money
        amount = subtractPrice(productPrice);
        return product;
    }

    private int convertMoneyToCents(Money money) {
        return money.getCents() + money.getEuros() * 100;
    }

    private boolean canAffordProduct(Product product) {
        return convertMoneyToCents(product.getPrice()) <= convertMoneyToCents(amount);
    }

    private boolean isCoinAcceptable(Money coin) {
        List<Integer> euroWhiteList = Arrays.asList(0, 1, 2);
        List<Integer> centWhiteList = Arrays.asList(0, 5, 10, 20, 50);
        int cents = coin.getCents();
        int euros = coin.getEuros();
        return euroWhiteList.contains(euros) && centWhiteList.contains(cents);
    }

    private Money subtractPrice(Money price) {
        int productPriceInCents = convertMoneyToCents(price);
        int amountInCents = convertMoneyToCents(amount);
        int reminderInCents = amountInCents - productPriceInCents;
        int euros = reminderInCents / 100;
        int cents = reminderInCents - euros * 100;
        return new Money(euros, cents);
    }
}

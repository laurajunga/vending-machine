package core;

import exceptions.InsufficientMoneyAmountException;
import exceptions.ProductListIsEmptyException;
import exceptions.ProductUnavailableException;
import exceptions.UnacceptableCoinException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class IVendingMachineTest {
    String[] productNames = {"Coca Cola", "Pepsi", "Fanta", "KitKat", "Milka Chocolate", "BelVita"};
    double[] productPrices = {0.75, 0.65, 0.50, 1.30, 3.50, 0.40};
    int[] availability = {2, 5, 5, 4, 3, 0};

    private List<Product> createProducts(int size) {
        List<Product> list = new ArrayList<Product>();
        for (int i = 0; i < size; i++) {
            Product product = new Product();
            product.setName(productNames[i]);
            double productPrice = productPrices[i];
            Money money = new Money((int) productPrice, (int) ((productPrice - (int) productPrice) * 100));
            product.setPrice(money);
            product.setAvailable(availability[i]);
            list.add(product);
        }
        return list;
    }

    @Test
    public void setProduct() {
        List<Product> products = createProducts(6);
        VendingMachine vm = new VendingMachine();
        vm.setProduct(products);
        assertEquals(products, vm.getProduct());
    }

    @Test
    public void insertCoinTwentyCents() {
        Money money = new Money(0, 20);
        VendingMachine vm = new VendingMachine();
        vm.insertCoin(money);
        assertEquals(0, vm.getAmount().getEuros());
        assertEquals(20, vm.getAmount().getCents());
    }

    @Test
    public void insertCoinZeroZero() {
        Money money = new Money(0, 0);
        VendingMachine vm = new VendingMachine();
        vm.insertCoin(money);
        assertEquals(0, vm.getAmount().getEuros());
        assertEquals(0, vm.getAmount().getCents());
    }

    @Test
    public void insertCoinSeventyFiveCents() {
        Money money = new Money(0, 50);
        VendingMachine vm = new VendingMachine();
        vm.insertCoin(money);
        money.setCents(20);
        vm.insertCoin(money);
        money.setCents(5);
        vm.insertCoin(money);
        assertEquals(0, vm.getAmount().getEuros());
        assertEquals(75, vm.getAmount().getCents());
    }

    @Test(expected = UnacceptableCoinException.class)
    public void insertCoinFiftyFourCents() {
        Money money = new Money(0, 54);
        VendingMachine vm = new VendingMachine();
        vm.insertCoin(money);
    }

    @Test(expected = UnacceptableCoinException.class)
    public void insertCoinOneCent() {
        Money money = new Money(0, 1);
        VendingMachine vm = new VendingMachine();
        vm.insertCoin(money);
    }

    @Test(expected = UnacceptableCoinException.class)
    public void insertCoinMinusTwoEuro() {
        Money money = new Money(-2, 20);
        VendingMachine vm = new VendingMachine();
        vm.insertCoin(money);
    }

    @Test
    public void returnMoneyZeroZero() {
        VendingMachine vm = new VendingMachine();
        Money returnedMoney = vm.returnMoney();
        assertEquals(returnedMoney.getCents(), 0);
        assertEquals(returnedMoney.getEuros(), 0);
    }

    @Test
    public void returnMoneyTwoEuros() {
        VendingMachine vm = new VendingMachine();
        Money insertedMoney = new Money(2, 0);
        vm.insertCoin(insertedMoney);

        Money returnedMoney = vm.returnMoney();
        assertEquals(returnedMoney.getCents(), 0);
        assertEquals(returnedMoney.getEuros(), 2);
    }

    @Test
    public void returnMoneyReminder() {
        // set products
        List<Product> products = createProducts(6);
        VendingMachine vm = new VendingMachine();
        vm.setProduct(products);
        // insert coins
        Money money = new Money(2, 0);
        vm.insertCoin(money);
        money.setEuros(2);
        vm.insertCoin(money);
        // buy Milka chocolate product
        vm.buy(4);

        // check that amount of remaining money has decreased
        Money remainingAmountOfMoney = vm.returnMoney();
        assertEquals(50, remainingAmountOfMoney.getCents());
        assertEquals(0, remainingAmountOfMoney.getEuros());
    }

    @Test
    public void buyHappyFlow() {
        // set products
        List<Product> products = createProducts(6);
        VendingMachine vm = new VendingMachine();
        vm.setProduct(products);
        // insert coins
        Money money = new Money(0, 50);
        vm.insertCoin(money);
        // buy Fanta product
        Product fanta = vm.buy(2);

        // check that amount of remaining products has decreased
        assertEquals(4, fanta.getAvailable());
        // check that amount of remaining money has decreased
        Money remainingAmountOfMoney = vm.getAmount();
        assertEquals(0, remainingAmountOfMoney.getCents());
        assertEquals(0, remainingAmountOfMoney.getEuros());
    }

    @Test(expected = ProductListIsEmptyException.class)
    public void buyWithoutProducts() {
        VendingMachine vm = new VendingMachine();
        // insert coins
        Money money = new Money(0, 50);
        vm.insertCoin(money);
        // buy first product
        vm.buy(0);
    }

    @Test(expected = ProductUnavailableException.class)
    public void buyShortProductList() {
        // set products
        List<Product> products = createProducts(5);
        VendingMachine vm = new VendingMachine();
        vm.setProduct(products);
        // insert coins
        Money money = new Money(0, 50);
        vm.insertCoin(money);
        // buy non existing product
        vm.buy(5);
    }

    @Test(expected = ProductUnavailableException.class)
    public void buyUnavailableProduct() {
        // set products
        List<Product> products = createProducts(6);
        VendingMachine vm = new VendingMachine();
        vm.setProduct(products);
        // insert coins
        Money money = new Money(0, 50);
        vm.insertCoin(money);
        // buy BelVita product
        vm.buy(5);
    }

    @Test(expected = InsufficientMoneyAmountException.class)
    public void buyUnaffordableProduct() {
        // set products
        List<Product> products = createProducts(6);
        VendingMachine vm = new VendingMachine();
        vm.setProduct(products);
        // insert coins
        Money money = new Money(0, 50);
        vm.insertCoin(money);
        // buy Pepsi product
        vm.buy(1);
    }
}
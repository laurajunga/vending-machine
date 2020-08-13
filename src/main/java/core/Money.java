package core;

public class Money {
    private int euros;
    private int cents;

    public Money(int euros, int cents) {
        setEuros(euros);
        setCents(cents);
    }

    public int getEuros() {
        return euros;
    }

    public void setEuros(int euros) {
        this.euros = euros;
    }

    public int getCents() {
        return cents;
    }

    public void setCents(int cents) {
        this.cents = cents;
    }
}

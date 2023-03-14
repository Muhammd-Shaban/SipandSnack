package com.cust.sipnsnack.ManagerDashboard;

public class ReportsModel {

    private String special;
    private String pizza;
    private String burgers;
    private String fries;
    private String snacks;
    private String chilledDrinks;
    private String seaFoods;
    private String coffees;
    private String netSale;
    private String month;

    private String specialsAmount;
    private String pizzaAmount;
    private String burgersAmount;
    private String friesAmount;
    private String snacksAmount;
    private String chilledDrinksAmount;
    private String seaFoodsAmount;
    private String coffeesAmount;

    public ReportsModel(String special, String pizza, String burgers, String fries, String snacks,
                        String chilledDrinks, String seaFoods, String coffees, String netSale,
                        String month, String specialsAmount, String pizzaAmount, String burgersAmount,
                        String friesAmount, String snacksAmount, String chilledDrinksAmount,
                        String seaFoodsAmount, String coffeesAmount) {
        this.special = special;
        this.pizza = pizza;
        this.burgers = burgers;
        this.fries = fries;
        this.snacks = snacks;
        this.chilledDrinks = chilledDrinks;
        this.seaFoods = seaFoods;
        this.coffees = coffees;
        this.netSale = netSale;
        this.month = month;
        this.specialsAmount = specialsAmount;
        this.pizzaAmount = pizzaAmount;
        this.burgersAmount = burgersAmount;
        this.friesAmount = friesAmount;
        this.snacksAmount = snacksAmount;
        this.chilledDrinksAmount = chilledDrinksAmount;
        this.seaFoodsAmount = seaFoodsAmount;
        this.coffeesAmount = coffeesAmount;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getPizza() {
        return pizza;
    }

    public void setPizza(String pizza) {
        this.pizza = pizza;
    }

    public String getBurgers() {
        return burgers;
    }

    public void setBurgers(String burgers) {
        this.burgers = burgers;
    }

    public String getFries() {
        return fries;
    }

    public void setFries(String fries) {
        this.fries = fries;
    }

    public String getSnacks() {
        return snacks;
    }

    public void setSnacks(String snacks) {
        this.snacks = snacks;
    }

    public String getChilledDrinks() {
        return chilledDrinks;
    }

    public void setChilledDrinks(String chilledDrinks) {
        this.chilledDrinks = chilledDrinks;
    }

    public String getSeaFoods() {
        return seaFoods;
    }

    public void setSeaFoods(String seaFoods) {
        this.seaFoods = seaFoods;
    }

    public String getCoffees() {
        return coffees;
    }

    public void setCoffees(String coffees) {
        this.coffees = coffees;
    }

    public String getNetSale() {
        return netSale;
    }

    public void setNetSale(String netSale) {
        this.netSale = netSale;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getSpecialsAmount() {
        return specialsAmount;
    }

    public void setSpecialsAmount(String specialsAmount) {
        this.specialsAmount = specialsAmount;
    }

    public String getPizzaAmount() {
        return pizzaAmount;
    }

    public void setPizzaAmount(String pizzaAmount) {
        this.pizzaAmount = pizzaAmount;
    }

    public String getBurgersAmount() {
        return burgersAmount;
    }

    public void setBurgersAmount(String burgersAmount) {
        this.burgersAmount = burgersAmount;
    }

    public String getFriesAmount() {
        return friesAmount;
    }

    public void setFriesAmount(String friesAmount) {
        this.friesAmount = friesAmount;
    }

    public String getSnacksAmount() {
        return snacksAmount;
    }

    public void setSnacksAmount(String snacksAmount) {
        this.snacksAmount = snacksAmount;
    }

    public String getChilledDrinksAmount() {
        return chilledDrinksAmount;
    }

    public void setChilledDrinksAmount(String chilledDrinksAmount) {
        this.chilledDrinksAmount = chilledDrinksAmount;
    }

    public String getSeaFoodsAmount() {
        return seaFoodsAmount;
    }

    public void setSeaFoodsAmount(String seaFoodsAmount) {
        this.seaFoodsAmount = seaFoodsAmount;
    }

    public String getCoffeesAmount() {
        return coffeesAmount;
    }

    public void setCoffeesAmount(String coffeesAmount) {
        this.coffeesAmount = coffeesAmount;
    }
}

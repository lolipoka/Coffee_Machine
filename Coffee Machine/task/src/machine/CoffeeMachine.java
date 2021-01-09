package machine;

import java.util.Scanner;

public class CoffeeMachine {
    private static final Scanner scanner = new Scanner(System.in);

    private int waterInMilliliters;
    private int milkInMilliliters;
    private int coffeeBeansInGrams;
    private int disposableCups;
    private int money;

    public CoffeeMachine(int initialWater, int initialMilk, int initialCoffeeBeans, int initialCups, int initialMoney) {
        waterInMilliliters = initialWater;
        milkInMilliliters = initialMilk;
        coffeeBeansInGrams = initialCoffeeBeans;
        disposableCups = initialCups;
        money = initialMoney;
    }

    public int getWater() {
        return waterInMilliliters;
    }

    public void setWater(int waterInMilliliters) {
        this.waterInMilliliters = waterInMilliliters;
    }

    public int getMilk() {
        return milkInMilliliters;
    }

    public void setMilk(int milkInMilliliters) {
        this.milkInMilliliters = milkInMilliliters;
    }

    public int getBeans() {
        return coffeeBeansInGrams;
    }

    public void setBeans(int coffeeBeansInGrams) {
        this.coffeeBeansInGrams = coffeeBeansInGrams;
    }

    public int getCups() {
        return disposableCups;
    }

    public void setCups(int disposableCups) {
        this.disposableCups = disposableCups;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void process(String userInput) {
        Action userAction = Action.getRefByValue(userInput);

        if (userAction == null) {
            return;
        }

        switch (userAction) {
            case BUY:
                processOrder();
                break;
            case FILL:
                fillCoffeeMachine();
                break;
            case TAKE:
                takeMoney();
                break;
            case REMAINING:
                printState();
                break;
            default:
                break;
        }
    }

    private enum Action {
        BUY ("buy"),
        FILL ("fill"),
        TAKE ("take"),
        REMAINING ("remaining"),
        EXIT ("exit"),
        BACK ("back");

        private final String input;

        Action(String input) {
            this.input = input;
        }

        static Action getRefByValue(String input) {
            for (Action action : Action.values()) {
                if (action.input.equals(input)) {
                    return action;
                }
            }
            return null;
        }
    }

    private enum CoffeeType {
        ESPRESSO (1, 250, 0, 16, 4),
        LATTE (2, 350, 75, 20, 7),
        CAPPUCCINO (3, 200, 100, 12, 6);

        private final int type;
        private final int reqWater;
        private final int reqMilk;
        private final int reqCoffeeBeans;
        private final int price;

        CoffeeType(int type, int reqWater, int reqMilk, int reqCoffeeBeans, int price) {
            this.type = type;
            this.reqWater = reqWater;
            this.reqMilk = reqMilk;
            this.reqCoffeeBeans = reqCoffeeBeans;
            this.price = price;
        }

        static CoffeeType getRefByValue(int value) {
            for (CoffeeType coffeeType : CoffeeType.values()) {
                if (coffeeType.type == value) {
                    return coffeeType;
                }
            }
            return null;
        }
    }

    private void processOrder() {

        System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino:");
        int userChoice = 0;
        try {
            userChoice = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            if (scanner.nextLine().equals(Action.BACK.input)) {
                return;
            }
        }

        CoffeeType coffeeType = CoffeeType.getRefByValue(userChoice);
        makeCoffee(coffeeType);
    }

    private void makeCoffee(CoffeeType coffeeType) {

        if (coffeeType == null) {
            System.out.println("Wrong input!");
            return;
        }

        if (getWater() < coffeeType.reqWater) {
            System.out.println("Sorry, not enough water!");
            return;
        }

        if (getMilk() < coffeeType.reqMilk) {
            System.out.println("Sorry, not enough milk!");
            return;
        }

        if (getBeans() < coffeeType.reqCoffeeBeans) {
            System.out.println("Sorry, not enough coffee beans!");
            return;
        }

        if (getCups() < 1) {
            System.out.println("Sorry, not enough disposable cups!");
            return;
        }

        System.out.println("I have enough resources, making you a coffee!\n");
        setWater(getWater() - coffeeType.reqWater);
        setMilk(getMilk() - coffeeType.reqMilk);
        setBeans(getBeans() - coffeeType.reqCoffeeBeans);
        setCups(getCups() - 1);
        setMoney(getMoney() + coffeeType.price);
    }

    private void fillCoffeeMachine() {
        addWater();
        addMilk();
        addBeans();
        addCups();
        scanner.nextLine();
    }

    private void addWater() {
        System.out.println("Write how many ml of water do you want to add:");
        int addWater = scanner.nextInt();
        setWater(getWater() + addWater);
    }

    private void addMilk() {
        System.out.println("Write how many ml of milk do you want to add:");
        int addMilk = scanner.nextInt();
        setMilk(getMilk() + addMilk);
    }

    private void addBeans() {
        System.out.println("Write how many grams of coffee beans do you want to add:");
        int addCoffeeBeans = scanner.nextInt();
        setBeans(getBeans() + addCoffeeBeans);
    }

    private void addCups() {
        System.out.println("Write how many disposable cups of coffee do you want to add:");
        int addCups = scanner.nextInt();
        setCups(getCups() + addCups);
    }

    private void takeMoney() {
        System.out.printf("I gave you $%d\n\n", getMoney());
        setMoney(0);
    }

    private void printState() {
        System.out.println("The coffee machine has:");
        System.out.printf("%d of water\n", getWater());
        System.out.printf("%d of milk\n", getMilk());
        System.out.printf("%d of coffee beans\n", getBeans());
        System.out.printf("%d of disposable cups\n", getCups());
        System.out.printf("$%d of money\n\n", getMoney());
    }

    private static void serveClients(CoffeeMachine machine) {
        String userInput;

        do {
            System.out.println("Write action (buy, fill, take, remaining, exit):");
            userInput = scanner.nextLine();
            machine.process(userInput);
        } while (!userInput.equals(Action.EXIT.input));
    }

    public static void main(String[] args) {

        CoffeeMachine machine = new CoffeeMachine(400, 540, 120, 9, 550);
        serveClients(machine);
        scanner.close();
    }
}
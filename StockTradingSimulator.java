import java.util.*;

class Stock {
    String symbol;
    double price;

    Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    void updatePrice() {
        // Simulate price change between -5% and +5%
        double changePercent = (Math.random() - 0.5) * 0.1;
        price += price * changePercent;
        price = Math.round(price * 100.0) / 100.0;
    }
}

class Holding {
    String symbol;
    int quantity;

    Holding(String symbol, int quantity) {
        this.symbol = symbol;
        this.quantity = quantity;
    }
}

public class StockTradingSimulator {
    static final Scanner scanner = new Scanner(System.in);
    static final Map<String, Stock> market = new HashMap<>();
    static final Map<String, Holding> portfolio = new HashMap<>();
    static double cashBalance = 10000.00;

    public static void main(String[] args) {
        initializeMarket();

        int choice;
        do {
            System.out.println("\n=== Stock Trading Simulator ===");
            System.out.println("1. View Market");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            updateMarketPrices();

            switch (choice) {
                case 1 -> displayMarket();
                case 2 -> buyStock();
                case 3 -> sellStock();
                case 4 -> viewPortfolio();
                case 5 -> System.out.println("Thank you for trading!");
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 5);
    }

    static void initializeMarket() {
        market.put("AAPL", new Stock("AAPL", 150.00));
        market.put("GOOGL", new Stock("GOOGL", 2800.00));
        market.put("AMZN", new Stock("AMZN", 3300.00));
        market.put("TSLA", new Stock("TSLA", 700.00));
        market.put("INFY", new Stock("INFY", 1500.00));
    }

    static void updateMarketPrices() {
        for (Stock stock : market.values()) {
            stock.updatePrice();
        }
    }

    static void displayMarket() {
        System.out.println("\n--- Market Prices ---");
        for (Stock stock : market.values()) {
            System.out.printf("%s: ₹%.2f\n", stock.symbol, stock.price);
        }
    }

    static void buyStock() {
        displayMarket();
        System.out.print("Enter stock symbol to buy: ");
        String symbol = scanner.nextLine().toUpperCase();

        if (!market.containsKey(symbol)) {
            System.out.println("Stock not found.");
            return;
        }

        System.out.print("Enter quantity to buy: ");
        int qty = scanner.nextInt();
        scanner.nextLine(); // consume newline

        double cost = market.get(symbol).price * qty;

        if (cost > cashBalance) {
            System.out.println("Insufficient funds.");
        } else {
            cashBalance -= cost;
            portfolio.put(symbol, new Holding(symbol,
                portfolio.getOrDefault(symbol, new Holding(symbol, 0)).quantity + qty));
            System.out.printf("Bought %d shares of %s for ₹%.2f\n", qty, symbol, cost);
        }
    }

    static void sellStock() {
        System.out.print("Enter stock symbol to sell: ");
        String symbol = scanner.nextLine().toUpperCase();

        if (!portfolio.containsKey(symbol)) {
            System.out.println("You don't own this stock.");
            return;
        }

        Holding holding = portfolio.get(symbol);
        System.out.printf("You own %d shares of %s\n", holding.quantity, symbol);
        System.out.print("Enter quantity to sell: ");
        int qty = scanner.nextInt();
        scanner.nextLine();

        if (qty > holding.quantity) {
            System.out.println("Not enough shares to sell.");
            return;
        }

        double revenue = market.get(symbol).price * qty;
        holding.quantity -= qty;
        cashBalance += revenue;

        if (holding.quantity == 0) {
            portfolio.remove(symbol);
        }

        System.out.printf("Sold %d shares of %s for ₹%.2f\n", qty, symbol, revenue);
    }

    static void viewPortfolio() {
        System.out.println("\n--- Your Portfolio ---");
        if (portfolio.isEmpty()) {
            System.out.println("No stocks owned.");
        } else {
            for (Holding h : portfolio.values()) {
                double currentPrice = market.get(h.symbol).price;
                double value = currentPrice * h.quantity;
                System.out.printf("%s - %d shares @ ₹%.2f = ₹%.2f\n",
                        h.symbol, h.quantity, currentPrice, value);
            }
        }
        System.out.printf("Cash Balance: ₹%.2f\n", cashBalance);
    }
}


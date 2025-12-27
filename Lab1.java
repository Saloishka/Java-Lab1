/**
 *  Thread-safe bank account supporting deposit and withdraw operations
 */
class BankAccount {
    private int balance = 0;
    public synchronized void deposit(int amount) {
        balance += amount;
        System.out.println("Поповнення на " + amount +
                ". Баланс = " + balance);
        notifyAll();
    }

    public synchronized void withdraw(int amount) {
        while (balance < amount) {
            System.out.println("Недостатньо коштів для зняття " + amount +
                    ". Очікування поповнення...");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        balance -= amount;
        System.out.println("Знято " + amount +
                ". Баланс = " + balance);
    }
}

/**
 * Thread that performs a single deposit after a short delay
 */
class DepositThread extends Thread {
    private final BankAccount account;
    public DepositThread(BankAccount account) {
        this.account = account;
    }
    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        account.deposit(100);
    }
}

/**
 * Thread that attempts to withdraw an amount   
 */
class WithdrawThread extends Thread {
    private final BankAccount account;
    public WithdrawThread(BankAccount account) {
        this.account = account;
    }
    @Override
    public void run() {
        account.withdraw(100);
    }
}

/**
 * Starts withdraw and deposit threads
 */
public class Lab1 {
    public static void main(String[] args) {
        BankAccount account = new BankAccount();
        WithdrawThread withdrawThread = new WithdrawThread(account);
        DepositThread depositThread = new DepositThread(account);
        withdrawThread.start();
        depositThread.start();
    }
}

package bankservice;

public class BankAccount {
    protected String customerName;
    protected String password;
    protected double balance;

    public BankAccount(String customerName, String password, double initialDeposit) {//생성자
        this.customerName = customerName;
        this.password = password;
        this.balance = initialDeposit;
    }

    public String getCustomerName() {
        return customerName;
    }

    public double getBalance() {
        return balance;
    }

    public boolean deposit(double amount) {
        if (amount <= 0 || amount % 10000 != 0) {
            System.out.println("입금은 만원(10000원) 단위로 가능합니다.");
            return false;
        }
        balance += amount;
        System.out.println("입금 완료. 현재 잔액: " + balance);
        return true;
    }

    public boolean withdraw(double amount) {
        if (amount <= 0 || amount % 10000 != 0) {
            System.out.println("출금은 만원(10000원) 단위로 가능합니다.");
            return false;
        }
        if (balance - amount < 0) {
            System.out.println("잔액이 부족합니다.");
            return false;
        }
        balance -= amount;
        System.out.println("출금 완료. 현재 잔액: " + balance);
        return true;
    }

    public boolean closeAccount() {
        System.out.println("계좌 해지 완료. " + customerName + "님의 계좌가 닫혔습니다.");
        return true;
    }

    public boolean authenticate(String customerName, String password) {
        return this.customerName.equals(customerName) && this.password.equals(password);
    }
    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }

}

class CorporateAccount extends BankAccount{
	private String ceo;
	private String manager;
	
	public CorporateAccount(String customerName, String password, double initialDeposit, String ceo, String manager) {
		super(customerName, password, initialDeposit);
        this.ceo = ceo;
        this.manager = manager;
	}
	public String getCeo() {
        return ceo;
    }

    public void setCeo(String ceo) {
        this.ceo = ceo;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }
    @Override
    public boolean deposit(double amount) {
        if (amount <= 0 || amount % 10000 != 0) {
            System.out.println("입금은 만원(10000원) 단위로 가능합니다.");
            return false;
        }
        balance += amount;
        System.out.println("입금 완료. 현재 잔액: " + balance);
        return true;
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0 || amount % 10000 != 0) {
            System.out.println("출금은 만원(10000원) 단위로 가능합니다.");
            return false;
        }
        if (balance - amount < 0) {
            System.out.println("잔액이 부족합니다.");
            return false;
        }
        balance -= amount;
        System.out.println("출금 완료. 현재 잔액: " + balance);
        return true;
    }

    @Override
    public boolean closeAccount() {
        System.out.println("계좌 해지 완료. " + customerName + "님의 계좌가 닫혔습니다.");
        return true;
    }

    @Override
    public boolean authenticate(String customerName, String password) {
        return this.customerName.equals(customerName) && this.password.equals(password);
    }

    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }
}

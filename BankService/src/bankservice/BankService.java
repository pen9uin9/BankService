package bankservice;

import java.util.HashMap;
import java.util.Map;

public class BankService {
    private Map<String, BankAccount> accounts;

    public BankService() {
        this.accounts = new HashMap<>();
    }

    public void openAccount(String customerName, String password, double initialDeposit) {// 개인 계좌 개설 매소드
        if (accounts.containsKey(customerName)) {
            System.out.println("이미 개설된 계좌가 있습니다.");
            return;
        }
        if (initialDeposit < 10000) {
            System.out.println("최소 입금액은 만원(10000원) 이상이어야 합니다.");
            return;
        }
        BankAccount account = new BankAccount(customerName, password, initialDeposit);
        accounts.put(customerName, account);
        System.out.println("계좌가 개설되었습니다.");
    }
    
    public void openAccount(String customerName, String password, double initialDeposit, String ceo, String manager) { // 법인 계좌 개설 메소드
        if (accounts.containsKey(customerName)) {
            System.out.println("이미 개설된 계좌가 있습니다.");
            return;
        }
        if (initialDeposit < 10000) {
            System.out.println("최소 입금액은 만원(10000원) 이상이어야 합니다.");
            return;
        }
        BankAccount account = new BankAccount(customerName, password, initialDeposit);
        accounts.put(customerName, account);
        System.out.println("계좌가 개설되었습니다.");
    }

    public void deposit(String customerName, String password, double amount) {
        if (amount % 10000 != 0) {
            System.out.println("입금 금액은 만원 단위로만 가능합니다.");
            return;
        }
        
        BankAccount account = findAccount(customerName, password);
        if (account != null) {
            account.deposit(amount);
            System.out.println(Math.round(amount) + "원이 입금되었습니다. 잔액은 " + Math.round(account.getBalance()) + "원입니다.");
        } else {
            System.out.println("계좌 정보가 일치하지 않거나 계좌를 찾을 수 없습니다.");
        }
    }

    public void withdraw(String customerName, String password, double amount) {
        if (amount % 10000 != 0) {
            System.out.println("인출 금액은 만원 단위로만 가능합니다.");
            return;
        }
        
        BankAccount account = findAccount(customerName, password);
        if (account != null && account.withdraw(amount)) {
            System.out.println(Math.round(amount) + "원이 인출되었습니다. 잔액은 " + Math.round(account.getBalance()) + "원입니다.");
        } else if (account == null) {
            System.out.println("계좌 정보가 일치하지 않거나 계좌를 찾을 수 없습니다.");
        } else {
            System.out.println("잔액 부족으로 인출할 수 없습니다.");
        }
    }

    public void checkBalance(String customerName, String password) {
        BankAccount account = accounts.get(customerName);
        if (account == null) {
            System.out.println("존재하지 않는 계좌입니다.");
            return;
        }
        if (!account.authenticate(customerName, password)) {
            System.out.println("잘못된 비밀번호입니다.");
            return;
        }
        System.out.println("현재 잔액: " + Math.round(account.getBalance()));
    }

    public void closeAccount(String customerName, String password) {
        BankAccount account = accounts.get(customerName);
        if (account == null) {
            System.out.println("존재하지 않는 계좌입니다.");
            return;
        }
        if (!account.authenticate(customerName, password)) {
            System.out.println("잘못된 비밀번호입니다.");
            return;
        }
        accounts.remove(customerName);
        account.closeAccount();
    }
    
    private BankAccount findAccount(String customerName, String password) {
    	 BankAccount account = accounts.get(customerName);
         if (account != null && account.checkPassword(password)) {
             return account;
         }
         return null;
     }
}

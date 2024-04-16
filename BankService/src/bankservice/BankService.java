package bankservice;

import java.security.SecureRandom;
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
        
        String accountNumber = generateAccountNumber(); // 새로운 계좌번호 생성
        BankAccount account = new BankAccount(customerName, password, initialDeposit);
        account.setAccountNumber(accountNumber); // 계좌번호 설정
        accounts.put(customerName, account); // 계좌를 맵에 저장
        System.out.println("계좌가 개설되었습니다. 계좌번호: " + accountNumber);
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
    
    private String generateAccountNumber() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 13; i++) {
            sb.append(random.nextInt(10));  // 숫자 (0-9)를 계좌번호에 추가
        }
        return sb.toString();
    }

    // 이체서비스 핵심 메서드
    public void transfer(String senderName, String senderPassword, String receiverIdentifier, double amount) {
        if (amount % 10000 != 0) {
            System.out.println("이체 금액은 만원(10000원) 단위로 가능합니다.");
            return;
        }

        BankAccount senderAccount = findAccount(senderName, senderPassword);
        BankAccount receiverAccount = findAccountByNumberOrName(receiverIdentifier);

        if (senderAccount == null || receiverAccount == null) {
            System.out.println("계좌 정보가 일치하지 않습니다.");
            return;
        }

        double totalAmount = amount + 1000; // 이체 금액 + 수수료
        if (senderAccount.getBalance() >= totalAmount) {
            if (senderAccount.withdraw(totalAmount)) { // 여기에서 만원 단위 검사 제거 필요
                receiverAccount.deposit(amount);
                System.out.println("이체 완료: " + Math.round(amount) + "원, 수수료: 1000원");
                System.out.println("송금자 잔액: " + Math.round(senderAccount.getBalance()) + "원");
            } else {
                System.out.println("잔액 부족으로 인출할 수 없습니다.");
            }
        } else {
            System.out.println("잔액 부족으로 인출할 수 없습니다.");
        }
    }


    // 이름 또는 계좌번호로 계좌 찾기
    private BankAccount findAccountByNumberOrName(String identifier) {
        for (BankAccount account : accounts.values()) {
            if (account.getAccountNumber().equals(identifier) || account.getCustomerName().equals(identifier)) {
                return account;
            }
        }
        return null;
    }


}
package bankservice;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;

public class BankService {
     private Map<String, BankAccount> accounts = new HashMap<>();
       private Map<String, Integer> failedAttempts = new HashMap<>();
       private Map<String, LocalDateTime> lockoutTime = new HashMap<>();

    private AdminService adminService;
    
    public int getTotalAccountCount() {
        return accounts.size();
    }
 // BankService 클래스 내의 메소드
    public int getIndividualAccountCount() {
        return (int) accounts.values().stream()
            .filter(acc -> "individual".equals(acc.getAccountType()))
            .count();
    }

    public int getCorporateAccountCount() {
        return (int) accounts.values().stream()
            .filter(acc -> acc instanceof CorporateAccount) // CorporateAccount의 인스턴스인지 확인
            .count();
    }



    public BankService(AdminService adminService) {
        this.accounts = new HashMap<>();
        this.adminService = adminService;
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
        String accountType = "individual";
        BankAccount account = new BankAccount(customerName, password, initialDeposit, accountType);
        account.setAccountNumber(accountNumber); // 계좌번호 설정
        accounts.put(customerName, account); // 계좌를 맵에 저장
       
    }
    
 // openCorporateAccount 메서드 수정
    public void openCorporateAccount(String customerName, String password, double initialDeposit, String ceo, String manager) {
        if (accounts.containsKey(customerName)) {
            System.out.println("이미 개설된 계좌가 있습니다.");
            return;
        }
        CorporateAccount account = new CorporateAccount(customerName, password, initialDeposit, ceo, manager);
        account.setAccountType("corporate"); // 계좌 유형을 "corporate"로 설정
        String accountNumber = generateAccountNumber(); // 새로운 계좌번호 생성
        account.setAccountNumber(accountNumber); // 계좌번호 설정
        accounts.put(accountNumber, account); // 계좌번호를 키로 하여 맵에 저장
        System.out.println("계좌가 개설되었습니다.");
    }

    

    public void deposit(String customerName, String password, double amount) {
        BankAccount account = findAccount(customerName, password);
        if (account != null && account.deposit(amount)) {
            Transaction transaction = new Transaction("deposit", amount, "Deposit by " + customerName);
            adminService.logTransaction(transaction);
            System.out.println(amount + "원이 입금되었습니다. 잔액은 " + account.getBalance() + "원입니다.");
        } else {
            System.out.println("계좌 정보가 일치하지 않거나 계좌를 찾을 수 없습니다.");
        }
    }
    
    public void withdraw(String customerName, String password, double amount) {
        BankAccount account = findAccount(customerName, password);
        if (account != null && account.withdraw(amount, false)) { // 만원 단위 검사를 수행하도록 false 전달
            Transaction transaction = new Transaction("withdraw", amount, "Withdrawal by " + customerName);
            adminService.logTransaction(transaction);
            System.out.println(amount + "원이 출금되었습니다. 잔액은 " + account.getBalance() + "원입니다.");
        } else {
            System.out.println("계좌 정보가 일치하지 않거나 계좌를 찾을 수 없습니다.");
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
        // 만원 단위 검사
        if (amount % 10000 != 0) {
            System.out.println("이체 금액은 만원 단위로만 가능합니다.");
            return;
        }

        BankAccount senderAccount = findAccount(senderName, senderPassword);
        BankAccount receiverAccount = findAccountByNumberOrName(receiverIdentifier);

        if (senderAccount != null && receiverAccount != null) {
            double totalAmount = amount + 1000; // 이체 금액 + 수수료
            if (senderAccount.withdraw(totalAmount, true)) { // 만원 단위 검사를 건너뛰기
                receiverAccount.deposit(amount);
                Transaction transaction = new Transaction("transfer", amount, "Transfer from " + senderName + " to " + receiverIdentifier);
                adminService.logTransaction(transaction);
                System.out.println("이체 완료: " + amount + "원, 수수료: 1000원");
                System.out.println("송금자 잔액: " + Math.round(senderAccount.getBalance()) + "원");
            } else {
                System.out.println("잔액 부족으로 이체할 수 없습니다.");
            }
        } else {
            System.out.println("계좌 정보가 일치하지 않습니다.");
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
    
    private boolean isAccountLocked(String customerName) {
        if (!lockoutTime.containsKey(customerName)) {
            return false;
        }
        LocalDateTime lockedUntil = lockoutTime.get(customerName);
        if (lockedUntil.isAfter(LocalDateTime.now())) {
            return true;
        } else {
            lockoutTime.remove(customerName); // Unlock the account as the lockout period has expired
            return false;
        }
    }
    public boolean authenticate(String customerName, String password) {
        if (isAccountLocked(customerName)) {
            System.out.println("Your account is temporarily locked. Please try again in 5 minutes.");
            return false;
        }

        BankAccount account = accounts.get(customerName);
        if (account != null && account.checkPassword(password)) {
            failedAttempts.put(customerName, 0); // Reset attempts after successful login
            return true;
        } else {
            int attempts = failedAttempts.getOrDefault(customerName, 0);
            attempts++;
            failedAttempts.put(customerName, attempts);
            if (attempts >= 5) {
                lockoutTime.put(customerName, LocalDateTime.now().plusMinutes(5)); // Lock the account for 5 minutes
                System.out.println("Too many failed attempts. Your account has been locked for 5 minutes.");
            } else {
                System.out.println("Incorrect password. Attempts left: " + (5 - attempts));
            }
            return false;
        }
    }
    public BankAccount findAccount(String customerName) {
        return accounts.get(customerName);
    }
    
    

}
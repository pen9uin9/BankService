package bankservice;

public class BankAccount1 {
    private String customerName;
    private String password;
    private double balance;

    // BankAccount 생성자
    public BankAccount1(String customerName, String password, double initialDeposit) {
        this.customerName = customerName;
        this.password = password;
        this.balance = initialDeposit;
    }

    // 잔액 조회
    public double getBalance() {
        return balance;
    }

    // 고객 이름 조회
    public String getCustomerName() {
        return customerName;
    }
    
 // 비밀번호 확인 메서드
    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }
    

    // 비밀번호 반환 메서드 추가
    public String getPassword() {
        return password;
    }
}
	
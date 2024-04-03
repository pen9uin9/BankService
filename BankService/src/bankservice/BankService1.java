package bankservice;

public class BankService1 {
    private BankAccount1 account;

    // 계좌 개설
    public void openAccount(String customerName, String password, double initialDeposit, boolean isStudent) {
        if (isStudent) {
             initialDeposit += 10000; // 학생인 경우, 10,000원 추가
         }
         account = new BankAccount1(customerName, password, initialDeposit);
         System.out.println("계좌가 성공적으로 개설되었습니다. 고객명: " + customerName + ", 초기 입금액: " + initialDeposit);
     }


    // 계좌 해지
    public void closeAccount() {
        if (account != null) {
            account = null;
            System.out.println("계좌가 성공적으로 해지되었습니다.");
        } else {
            System.out.println("해지할 계좌가 없습니다.");
        }
    }

    // 계좌 정보 조회
    public void showAccountInfo() {
        if (account != null) {
            System.out.println("고객명: " + account.getCustomerName() + ", 잔액: " + account.getBalance());
        } else {
            System.out.println("개설된 계좌가 없습니다.");
        }
    }
    
    public void closeAccount(String customerName, String password) {
        if (account != null && account.getCustomerName().equals(customerName) && account.checkPassword(password)) {
            account = null;
            System.out.println("계좌가 성공적으로 해지되었습니다.");
        } else {
            System.out.println("계좌 해지를 실패했습니다 정보를 확인해주세요.");
        }
    }
    
 // 계좌 존재 여부 확인 메서드
    public boolean isAccountExist(String customerName, String password) {
        if (account != null && account.getCustomerName().equals(customerName) && account.getPassword().equals(password)) {
            return true;
        }
        return false;
    }
    
 
    
}

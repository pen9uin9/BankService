package bankservice;
////수정
public class BankService1 {
    private BankAccount1 account;

    // ���� ����
    public void openAccount(String customerName, String password, double initialDeposit, boolean isStudent) {
        if (isStudent) {
             initialDeposit += 10000; // �л��� ���, 10,000�� �߰�
         }
         account = new BankAccount1(customerName, password, initialDeposit);
         System.out.println("���°� ���������� �����Ǿ����ϴ�. ����: " + customerName + ", �ʱ� �Աݾ�: " + initialDeposit);
     }


    // ���� ����
    public void closeAccount() {
        if (account != null) {
            account = null;
            System.out.println("���°� ���������� �����Ǿ����ϴ�.");
        } else {
            System.out.println("������ ���°� �����ϴ�.");
        }
    }

    // ���� ���� ��ȸ
    public void showAccountInfo() {
        if (account != null) {
            System.out.println("����: " + account.getCustomerName() + ", �ܾ�: " + account.getBalance());
        } else {
            System.out.println("������ ���°� �����ϴ�.");
        }
    }
    
    public void closeAccount(String customerName, String password) {
        if (account != null && account.getCustomerName().equals(customerName) && account.checkPassword(password)) {
            account = null;
            System.out.println("���°� ���������� �����Ǿ����ϴ�.");
        } else {
            System.out.println("���� ������ �����߽��ϴ� ������ Ȯ�����ּ���.");
        }
    }
    
 // ���� ���� ���� Ȯ�� �޼���
    public boolean isAccountExist(String customerName, String password) {
        if (account != null && account.getCustomerName().equals(customerName) && account.getPassword().equals(password)) {
            return true;
        }
        return false;
    }
    
 
    
}

package bankservice;

public class BankAccount1 {
    private String customerName;
    private String password;
    private double balance;

    // BankAccount ������
    public BankAccount1(String customerName, String password, double initialDeposit) {
        this.customerName = customerName;
        this.password = password;
        this.balance = initialDeposit;
    }

    // �ܾ� ��ȸ
    public double getBalance() {
        return balance;
    }

    // �� �̸� ��ȸ
    public String getCustomerName() {
        return customerName;
    }
    
 // ��й�ȣ Ȯ�� �޼���
    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }
    

    // ��й�ȣ ��ȯ �޼��� �߰�
    public String getPassword() {
        return password;
    }
}
	
package bankservice;

import java.util.Scanner;

public class MainBankApplication1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BankService1 bankService = new BankService1();

        while (true) { // ���� ����
            System.out.println("\n ������ ���� ���񽺿� ���� ���� ȯ���մϴ�. ���Ͻô� ���񽺸� �������ּ���.��");
            System.out.println("1. ���� ����");
            System.out.println("2. ���� ����");
            System.out.println("3. ���� ���� Ȯ��");
            System.out.println("4. ���α׷� ����");
            System.out.print("����: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // �Է� ���� �����

            switch (choice) {
                case 1: // ���� ����
                    System.out.print("�� �̸��� �Է��ϼ���: ");
                    String customerName = scanner.nextLine();

                    System.out.print("���� ��й�ȣ�� �����ϼ���: ");
                    String password = scanner.nextLine();

                    System.out.print("���� �Աݾ��� �Է��ϼ���: ");
                    double initialDeposit = scanner.nextDouble();

                    System.out.print("�л��̽ʴϱ�? (��: 1, �ƴϿ�: 0): ");
                    int studentResponse = scanner.nextInt();
                    boolean isStudent = studentResponse == 1;
                    
                    bankService.openAccount(customerName, password, initialDeposit, isStudent);
                   
                    break;
                case 2: // ���� ����
                    System.out.print("�� �̸��� �Է��ϼ���: ");
                    customerName = scanner.nextLine();

                    System.out.print("���� ��й�ȣ�� �Է��ϼ���: ");
                    password = scanner.nextLine();

                    bankService.closeAccount(customerName, password);
                    break;
                case 3: // ���� ���� Ȯ��
                    System.out.print("�� �̸��� �Է��ϼ���: ");
                    String checkcustomerName = scanner.nextLine();

                    System.out.print("���� ��й�ȣ�� �Է��ϼ���: ");
                    String checkpassword = scanner.nextLine();

                    boolean exists = bankService.isAccountExist(checkcustomerName, checkpassword);
                    if (exists) {
                        System.out.println("���°� �����մϴ�.");
                    } else {
                        System.out.println("���°� �������� �ʽ��ϴ�.");
                    }
                    break;
                case 4: // ���α׷� ����
                    System.out.println("���� ���񽺸� �����մϴ�.");
                    scanner.close();
                    return;
                default:
                    System.out.println("�߸��� �����Դϴ�. �ٽ� �������ּ���.");
            }
        }
    }
}
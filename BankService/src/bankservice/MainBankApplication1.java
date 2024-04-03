package bankservice;

import java.util.Scanner;

public class MainBankApplication1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BankService1 bankService = new BankService1();

        while (true) { // 무한 루프
            System.out.println("\n 『지누 은행 서비스에 오신 것을 환영합니다. 원하시는 서비스를 선택해주세요.』");
            System.out.println("1. 계좌 개설");
            System.out.println("2. 계좌 해지");
            System.out.println("3. 계좌 존재 확인");
            System.out.println("4. 프로그램 종료");
            System.out.print("선택: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 입력 버퍼 지우기

            switch (choice) {
                case 1: // 계좌 개설
                    System.out.print("고객 이름을 입력하세요: ");
                    String customerName = scanner.nextLine();

                    System.out.print("계좌 비밀번호를 설정하세요: ");
                    String password = scanner.nextLine();

                    System.out.print("최초 입금액을 입력하세요: ");
                    double initialDeposit = scanner.nextDouble();

                    System.out.print("학생이십니까? (네: 1, 아니오: 0): ");
                    int studentResponse = scanner.nextInt();
                    boolean isStudent = studentResponse == 1;
                    
                    bankService.openAccount(customerName, password, initialDeposit, isStudent);
                   
                    break;
                case 2: // 계좌 해지
                    System.out.print("고객 이름을 입력하세요: ");
                    customerName = scanner.nextLine();

                    System.out.print("계좌 비밀번호를 입력하세요: ");
                    password = scanner.nextLine();

                    bankService.closeAccount(customerName, password);
                    break;
                case 3: // 계좌 존재 확인
                    System.out.print("고객 이름을 입력하세요: ");
                    String checkcustomerName = scanner.nextLine();

                    System.out.print("계좌 비밀번호를 입력하세요: ");
                    String checkpassword = scanner.nextLine();

                    boolean exists = bankService.isAccountExist(checkcustomerName, checkpassword);
                    if (exists) {
                        System.out.println("계좌가 존재합니다.");
                    } else {
                        System.out.println("계좌가 존재하지 않습니다.");
                    }
                    break;
                case 4: // 프로그램 종료
                    System.out.println("은행 서비스를 종료합니다.");
                    scanner.close();
                    return;
                default:
                    System.out.println("잘못된 선택입니다. 다시 선택해주세요.");
            }
        }
    }
}
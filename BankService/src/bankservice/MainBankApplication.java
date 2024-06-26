package bankservice;

import java.util.Scanner;

public class MainBankApplication {
    private static Scanner scanner = new Scanner(System.in);
    private static BankService bankService = new BankService();

    public static void main(String[] args) {
        while (true) {
        	System.out.println("『지누 은행에 오신걸 환영합니다. 원하시는 서비스를 입력해 주세요.』");
            System.out.println("1. 계좌 개설");
            System.out.println("2. 입금");
            System.out.println("3. 출금");
            System.out.println("4. 잔액 조회");
            System.out.println("5. 계좌 해지");
            System.out.println("6. 종료");
            System.out.print("원하시는 작업을 선택하세요: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // 개행 문자 제거

            switch (choice) {
                case 1:
                    checkType();
                    break;
                case 2:
                    depositMoney();
                    break;
                case 3:
                    withdrawMoney();
                    break;
                case 4:
                    checkBalance();
                    break;
                case 5:
                    closeAccount();
                    break;
                case 6:
                    System.out.println("프로그램을 종료합니다.");
                    System.exit(0);
                default:
                    System.out.println("유효하지 않은 선택입니다. 다시 시도해주세요.");
            }
        }
    }
    
    private static void checkType() {
    	System.out.println("1. 개인 2. 법인");
    	System.out.print("선택: ");
    	
    	 int typeChoice = scanner.nextInt();
    	    scanner.nextLine(); // 개행 문자 제거

    	    if (typeChoice == 1) {
    	        openIndividualAccount(); // 개인 계좌 개설 메서드 호출
    	    } else if (typeChoice == 2) {
    	        openCorporateAccount();
    	    }
    	}
    private static void openIndividualAccount() {
        System.out.println("개인 고객 중 학생이십니까?");
        System.out.println("1. 학생");
        System.out.println("2. 학생이 아님");
        System.out.print("선택: ");
        int studentChoice = scanner.nextInt();
        scanner.nextLine();  // 개행 문자 제거

        System.out.print("이름을 입력하세요: ");
        String name = scanner.nextLine();

        String password;
        while (true) {
            System.out.print("비밀번호를 입력하세요 (4자리 숫자): ");
            password = scanner.nextLine();
            if (password.matches("\\d{4}")) {
                break;  // 유효한 입력 시 루프 탈출
            } else {
                System.out.println("비밀번호는 숫자 4자리여야 합니다. 다시 입력해주세요.");
            }
        }

        double initialDeposit = 0;
        if (studentChoice == 1) {
            System.out.print("학생 계좌의 최소 입금액을 입력하세요 (만원 단위, 최소 10,000원): ");
        } else {
            System.out.print("최소 입금액을 입력하세요 (만원 단위, 최소 10,000원): ");
        }
        while (true) {
            initialDeposit = scanner.nextDouble();
            scanner.nextLine();  // 개행 문자 제거
            if (initialDeposit < 10000) {
                System.out.println("최소 입금액은 10,000원 이상이어야 합니다. 다시 입력해주세요.");
            } else {
                break;  // 유효한 입금액 시 루프 탈출
            }
        }

        if (studentChoice == 1) {
            initialDeposit += 10000;  // 학생일 경우 추가 보너스
            System.out.println("학생신규가입 시 10,000원이 추가됩니다.");
        }

        bankService.openAccount(name, password, initialDeposit);
        System.out.println( name + "님의 입금액: " + initialDeposit + "원, 잔액은 " + initialDeposit + "원입니다.");
    }
    
    
    private static void openCorporateAccount() {
        System.out.print("법인 이름을 입력하세요: ");
        String name = scanner.nextLine();
        System.out.print("비밀번호를 입력하세요: ");
        String password = scanner.nextLine();
        System.out.print("입금액을 입력하세요 (만원 단위): ");
        double initialDeposit = scanner.nextDouble();
        scanner.nextLine();  // 개행 문자 제거

        bankService.openAccount(name, password, initialDeposit);
    }
    private static void depositMoney() {
        System.out.print("이름을 입력하세요: ");
        String name = scanner.nextLine();
        System.out.print("비밀번호를 입력하세요: ");
        String password = scanner.nextLine();
        System.out.print("입금액을 입력하세요 (만원 단위): ");
        double amount = scanner.nextDouble();
        scanner.nextLine();  // 개행 문자 제거

        bankService.deposit(name, password, amount);
    }

    private static void withdrawMoney() {
        System.out.print("이름을 입력하세요: ");
        String name = scanner.nextLine();
        System.out.print("비밀번호를 입력하세요: ");
        String password = scanner.nextLine();
        System.out.print("출금액을 입력하세요 (만원 단위): ");
        double amount = scanner.nextDouble();
        scanner.nextLine();  // 개행 문자 제거

        bankService.withdraw(name, password, amount);
    }

    private static void checkBalance() {
        System.out.print("이름을 입력하세요: ");
        String name = scanner.nextLine();
        System.out.print("비밀번호를 입력하세요: ");
        String password = scanner.nextLine();

        bankService.checkBalance(name, password);
    }

    private static void closeAccount() {
        System.out.print("이름을 입력하세요: ");
        String name = scanner.nextLine();
        System.out.print("비밀번호를 입력하세요: ");
        String password = scanner.nextLine();

        bankService.closeAccount(name, password);
    }
}

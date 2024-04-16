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
            System.out.println("6.개인 고객간 이체");
            System.out.println("7. 종료");
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
                transferBetweenIndividuals();
                break;
                case 7:
                    System.out.println("프로그램을 종료합니다.");
                    System.exit(0);
                default:
                    System.out.println("유효하지 않은 선택입니다. 다시 시도해주세요.");
            }
        }
    }
    
    
    
    private static void checkType() { // 개인 법인 확인
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
    private static void openIndividualAccount() { // 개인 계좌 개설
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
        

        long accountNumber = BankAccount.generateAccountNumber(); // 계좌번호 생성
        
        // 개인 계좌 개설
        bankService.openAccount(name, password, initialDeposit);
        System.out.println(name + "님의 계좌가 개설되었습니다. 계좌번호: " + accountNumber + ", 입금액: " + Math.round(initialDeposit) + "원");
    }
    private static void openCorporateAccount() {// 법인 계좌 개설 // 대표자, 담당직원 이름 추가
        System.out.print("법인 이름을 입력하세요: ");
        String name = scanner.nextLine();
        
        System.out.print("비밀번호를 입력하세요: ");
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
        System.out.print("입금액을 입력하세요 (만원 단위, 최소 10,000원): ");
        while (true) {
            initialDeposit = scanner.nextDouble();
            scanner.nextLine();  // 개행 문자 제거
            if (initialDeposit < 10000) {
                System.out.println("최소 입금액은 10,000원 이상이어야 합니다. 다시 입력해주세요.");
            } else {
                break;  // 유효한 입금액 시 루프 탈출
            }
        }
        System.out.println("신규 가입 감사합니다. 법인신규가입 시 100,000원이 추가됩니다.");
        
        System.out.print("대표자 이름을 입력하세요: ");
        String ceo = scanner.nextLine();
        
        System.out.print("담당자 이름을 입력하세요: ");
        String manager = scanner.nextLine();
        //scanner.nextLine();  // 개행 문자 제거
        
        initialDeposit += 100000;
        bankService.openAccount(name, password, initialDeposit, ceo, manager);
        System.out.println( "법인명 :" + name + "대표자 : " + ceo + "직원 : " + manager + "님의 입금액 : " + Math.round(initialDeposit-100000) + "원, 잔액은 " + Math.round(initialDeposit) + "원입니다.");
    }
    
    private static void depositMoney() { // 입금
        System.out.print("이름을 입력하세요: ");
        String name = scanner.nextLine();
        System.out.print("비밀번호를 입력하세요: ");
        String password = scanner.nextLine();
        System.out.print("입금액을 입력하세요 (만원 단위): ");
        double amount = scanner.nextDouble();
        scanner.nextLine();  // 개행 문자 제거

        bankService.deposit(name, password, amount);
    }

    private static void withdrawMoney() { // 출금
        System.out.print("이름을 입력하세요: ");
        String name = scanner.nextLine();
        System.out.print("비밀번호를 입력하세요: ");
        String password = scanner.nextLine();
        System.out.print("출금액을 입력하세요 (만원 단위): ");
        double amount = scanner.nextDouble();
        scanner.nextLine();  // 개행 문자 제거

        bankService.withdraw(name, password, amount);
    }

    private static void checkBalance() { // 확인
        System.out.print("이름을 입력하세요: ");
        String name = scanner.nextLine();
        System.out.print("비밀번호를 입력하세요: ");
        String password = scanner.nextLine();

        bankService.checkBalance(name, password);
    }
    
    private static void transferBetweenIndividuals() {
        System.out.print("송금자 이름을 입력하세요: ");
        String senderName = scanner.nextLine();
        System.out.print("송금자 비밀번호를 입력하세요: ");
        String senderPassword = scanner.nextLine();
        System.out.print("수금자 이름 또는 계좌번호를 입력하세요: ");
        String receiverIdentifier = scanner.nextLine();
        System.out.print("이체할 금액을 입력하세요: ");
        long amount = scanner.nextLong();
        scanner.nextLine(); // 개행 문자 제거

        bankService.transfer(senderName, senderPassword, receiverIdentifier, amount);
    }
    private static void closeAccount() { // 해지
        System.out.print("이름을 입력하세요: ");
        String name = scanner.nextLine();
        System.out.print("비밀번호를 입력하세요: ");
        String password = scanner.nextLine();

        bankService.closeAccount(name, password);
    }
}
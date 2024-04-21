package bankservice;
import java.util.Scanner;
import java.security.SecureRandom; // 난수생성을 위한 import

public class MainBankApplication {
    private static Scanner scanner = new Scanner(System.in);
    private static BankService bankService = new BankService(new AdminService(null));  // AdminService 생성과 함께 BankService에 전달
    private static AdminService adminService = new AdminService(bankService);  
    private static SecureRandom random = new SecureRandom(); // 난수객체 생성

    public static void main(String[] args) {
    	
        while (true) {
            System.out.println("『지누 은행에 오신걸 환영합니다. 원하시는 서비스를 입력해 주세요.』");
            System.out.println("1. 계좌 개설");
            System.out.println("2. 입금");
            System.out.println("3. 출금");
            System.out.println("4. 잔액 조회");
            System.out.println("5. 계좌 해지");
            System.out.println("6. 개인고객 간 이체");
            System.out.println("7. 종료");
            System.out.println("8. 관리자 보고서 보기");  // 새로운 메뉴 항목 추가
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
                    transferMoney();
                    break;
                case 7:
                    System.out.println("프로그램을 종료합니다.");
                    System.exit(0);
                    break;
                case 8:
                    adminService.generateDailyReport();  // 관리자 보고서 생성 및 표시
                    break;
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
        while (true) {
            System.out.print("최소 입금액을 입력하세요 (만원 단위, 최소 10,000원): ");
            initialDeposit = scanner.nextDouble();
            scanner.nextLine();  // 개행 문자 제거
            if (initialDeposit >= 10000) {
                break;  // 유효한 입금액 시 루프 탈출
            } else {
                System.out.println("최소 입금액은 10,000원 이상이어야 합니다. 다시 입력해주세요.");
            }
        }

     // 13자리 랜덤 계좌번호 생성
        String accountNumber = generateAccountNumber();
        if (studentChoice == 1) {
            initialDeposit += 10000;  // 학생일 경우 추가 보너스
            System.out.println("학생신규가입 시 10,000원이 추가됩니다.");
        }
        bankService.openAccount(name, password, initialDeposit);
        System.out.println(name + "님의 계좌번호는 " + accountNumber + "이며, 입금액: " + Math.round(initialDeposit) + "원, 잔액은 " + Math.round(initialDeposit) + "원입니다.");
    }
    //개인계좌번호 생성 메소드
    private static String generateAccountNumber() {
        String accountNumber = "";
        for (int i = 0; i < 13; i++) {
            accountNumber += random.nextInt(10);  // 0부터 9 사이의 숫자를 추가
        }
        return accountNumber;
    }
    private static void openCorporateAccount() {
        System.out.print("법인 이름을 입력하세요: ");
        String name = scanner.nextLine();
        
        System.out.print("비밀번호를 입력하세요: ");
        String password;
        while (true) {
            password = scanner.nextLine();
            if (password.matches("\\d{4}")) {
                break;  // 유효한 입력 시 루프 탈출
            } else {
                System.out.println("비밀번호는 숫자 4자리여야 합니다. 다시 입력해주세요.");
            }
            
        }
        
       double initialDeposit = 0;
          while (true) {
              System.out.print("입금액을 입력하세요 (만원 단위, 최소 10,000원): ");
              initialDeposit = scanner.nextDouble();
              scanner.nextLine();  // 개행 문자 제거
              if (initialDeposit >= 10000) {
                  break;  // 유효한 입금액 시 루프 탈출
              } else {
                  System.out.println("최소 입금액은 10,000원 이상이어야 합니다. 다시 입력해주세요.");
              }
          }
          
        System.out.println("신규 가입 감사합니다. 법인신규가입 시 100,000원이 추가됩니다.");
        
        System.out.print("대표자 이름을 입력하세요: ");
        String ceo = scanner.nextLine();
        
        System.out.print("담당자 이름을 입력하세요: ");
        String manager = scanner.nextLine();
        //scanner.nextLine();  // 개행 문자 제거
        
        initialDeposit += 100000;
        bankService.openCorporateAccount(name, password, initialDeposit, ceo, manager); // 수정된 부분
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

   
    

    private static void closeAccount() { // 해지
        System.out.print("이름을 입력하세요: ");
        String name = scanner.nextLine();
        System.out.print("비밀번호를 입력하세요: ");
        String password = scanner.nextLine();

        bankService.closeAccount(name, password);
    }
    
    private static void transferMoney() { // 이체
        System.out.print("보내는 고객의 이름을 입력하세요: ");
        String senderName = scanner.nextLine();
        System.out.print("보내는 고객의 비밀번호를 입력하세요: ");
        String senderPassword = scanner.nextLine();
        System.out.print("받는 고객의 계좌번호 또는 이름을 입력하세요: ");
        String receiverIdentifier = scanner.nextLine();
        System.out.print("이체할 금액을 입력하세요 (만원 단위): ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        bankService.transfer(senderName, senderPassword, receiverIdentifier, amount);
    }
    
    public static void checkBalance() {
        System.out.print("이름을 입력하세요: ");
        String name = scanner.nextLine();  // 변수 이름 'name'으로 변경
        System.out.print("비밀번호를 입력하세요: ");
        String password = scanner.nextLine();

        if (bankService.authenticate(name, password)) {
            BankAccount account = bankService.findAccount(name); // name 변수 사용
            if (account != null) {
                System.out.println("잔액: " + account.getBalance());
            } else {
                System.out.println("계좌가 존재하지 않습니다.");
            }
        } else {
            System.out.println("인증이 실패하였습니다. 이름과 계좌번호를 다시 확인해 주세요.");
        }
    }
}
package bankservice;

import java.time.LocalTime;
import java.time.ZoneId;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AdminService {
    private static final String TRANSACTION_FILE = "transactions.txt";
    private BankService bankService;
    private final static double INITIAL_BANK_BALANCE = 30000000000L; // 은행 초기 자본은 300억으로 설정
    private double bankBalance = INITIAL_BANK_BALANCE;

    public AdminService(BankService bankService) {
        this.bankService = bankService;
        clearTransactionFile(); // 파일 초기화 메소드 호출
    }

    // 파일 초기화
    private void clearTransactionFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTION_FILE, false))) {
            writer.write(""); // 파일 내용을 비움
        } catch (IOException e) {
            System.out.println("거래 기록 파일 초기화에 실패했습니다: " + e.getMessage());
        }
    }
    
    // 거래 로그 기록
    public void logTransaction(Transaction transaction) {
        updateBalance(transaction);
        writeTransactionToFile(transaction);
    }

    // 거래 로그 파일에 쓰기
    private void writeTransactionToFile(Transaction transaction) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTION_FILE, true))) {
            writer.write(transaction.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("거래 기록을 파일에 쓰는데 실패했습니다: " + e.getMessage());
        }
    }

  
 // 은행 잔액 업데이트
    private void updateBalance(Transaction transaction) {
        if ("deposit".equals(transaction.getType())) {
            bankBalance += transaction.getAmount();
        } else if ("withdraw".equals(transaction.getType())) {
            bankBalance -= transaction.getAmount();
        } else if ("transfer".equals(transaction.getType())) {
            // 이체 시, 수수료 1000원도 고려하여 감소
            bankBalance -= (transaction.getAmount() + 1000);
        }
    }

    // 일일 보고서 생성
    public void generateDailyReport() {
        updateBalanceFromFile(); // 파일에서 잔고를 업데이트

        LocalTime now = LocalTime.now(ZoneId.of("Asia/Seoul"));
        if (now.isBefore(LocalTime.of(0, 0))) {
            System.out.println("보고서는 6시 이후에만 열람 가능합니다.");
            return;
        }

        int totalCustomers = bankService.getTotalAccountCount();
        int individualCustomers = bankService.getIndividualAccountCount();
        int corporateCustomers = bankService.getCorporateAccountCount();

        System.out.println("『보고서』");
        System.out.println("전체 고객 수: " + totalCustomers);
        System.out.println("개인 고객 수: " + individualCustomers);
        System.out.println("법인 고객 수: " + corporateCustomers);
        System.out.println("은행 잔고: " + String.format("%,.0f", bankBalance) + "원");
        System.out.println("[거래내역]");
        try {
            Files.lines(Paths.get(TRANSACTION_FILE)).forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("거래 내역 파일을 읽는데 실패했습니다: " + e.getMessage());
        }
    }
    
    private void updateBalanceFromFile() {
        bankBalance = INITIAL_BANK_BALANCE; // 초기 잔액으로 리셋
        try {
            Files.lines(Paths.get(TRANSACTION_FILE))
                 .map(this::parseTransaction)
                 .forEach(this::applyTransactionToBalance);
        } catch (IOException e) {
            System.out.println("거래 내역 파일을 읽는데 실패했습니다: " + e.getMessage());
        }
    }
    
    private Transaction parseTransaction(String transactionLine) {
        String[] parts = transactionLine.split(", ");
        String type = parts[0].split(": ")[1];
        double amount = Double.parseDouble(parts[1].split(": ")[1]);
        return new Transaction(type, amount, "");
    }
    
    private void applyTransactionToBalance(Transaction transaction) {
        if ("deposit".equals(transaction.getType())) {
            bankBalance += transaction.getAmount();
        } else if ("withdraw".equals(transaction.getType())) {
            bankBalance -= transaction.getAmount();
        } else if ("transfer".equals(transaction.getType())) {
            // 이체 시, 수수료 1000원도 고려하여 감소
            bankBalance -= transaction.getAmount() + 1000;
        }
    }
    
}


class Transaction {
    private String type; // "deposit", "withdraw", "transfer"
    private double amount;
    private String details;

    public Transaction(String type, double amount, String details) {
        this.type = type;
        this.amount = amount;
        this.details = details;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Type: " + type + ", Amount: " + amount + ", Details: " + details;
    }
}

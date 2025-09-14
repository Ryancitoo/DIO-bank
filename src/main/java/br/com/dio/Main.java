package br.com.dio;

import br.com.dio.exception.AccountNotFoundException;
import br.com.dio.exception.NoFundsEnoughException;
import br.com.dio.model.AccountWallet;
import br.com.dio.model.Investment;
import br.com.dio.repository.AccountRepository;
import br.com.dio.repository.InvestmentRepository;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Main {

    private final static AccountRepository accountRepository = new AccountRepository();
    private final static InvestmentRepository investmentRepository = new InvestmentRepository();

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Ola seja bem vindo ao DIO Bank");
        while(true){
            System.out.println("Selecione a operacao desejada");
            System.out.println("1 - Criar uma conta");
            System.out.println("2 - Criar um investimento");
            System.out.println("3 - Fazer um investimento");
            System.out.println("4 - Depositar na conta");
            System.out.println("5 - Sacar da conta");
            System.out.println("6 - Tranferencia entre contas");
            System.out.println("7 - Investir");
            System.out.println("8 - Sacar investimento");
            System.out.println("9 - Listar contas");
            System.out.println("10 - Listar Investimentos");
            System.out.println("11 - Listar carteiras de investimentos");
            System.out.println("12 - Atualizar investimentos");
            System.out.println("13 - Historico de conta");
            System.out.println("14 - sair");
            var option = scanner.nextInt();
            switch (option){
                case 1: createAccount(); break;
                case 2: createInvestment(); break;
                case 3: createWalletInvestment(); break;
                case 4: deposit(); break;
                case 5: withdraw(); break;
                case 6: trasferToAccount(); break;
                case 7: incInvestment(); break;
                case 8: rescueInvestment(); break;
                case 9: accountRepository.list().forEach(System.out::println); break;
                case 10: investmentRepository.list().forEach(System.out::println); break;
                case 11: investmentRepository.listWallets().forEach(System.out::println); break;
                case 12:{
                    investmentRepository.updateAmount();
                    System.out.println("Investimentos reajustados");
                    break;
                }
                case 13: checkHistory(); break;
                case 14: System.exit(0); break;
                default: System.out.println("Opção inválida");
            }
        }

    }

    private static void createAccount(){
        System.out.println("Informe as chaves pix (separadas por ';' ");
        var pix = Arrays.stream(scanner.next().split(";")).toList();
        System.out.println("Informe o valor inicial de depósito");
        var amount = scanner.nextLong();
        var wallet = accountRepository.create(pix, amount);
        System.out.println("Conta criada:" + wallet);
    }

    private static void createInvestment(){
        System.out.println("Informe a taxa do investimento");
        var tax = scanner.nextInt();
        System.out.println("Informe o valor inicial de depósito");
        var initialFunds = scanner.nextLong();
        var investment = investmentRepository.create(tax, initialFunds);
        System.out.println("investmento criado" + investment);
    }

    private static void withdraw(){
        System.out.println("Informe a chave pix da conta para saque");
        var pix = scanner.next();
        System.out.println("Informe o valor que será sacado: ");
        var amount = scanner.nextLong();
        try {

            accountRepository.withdraw(pix, amount);
        } catch (NoFundsEnoughException | AccountNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void deposit(){
        System.out.println("Informe a chave pix da conta para depósito:");
        var pix = scanner.next();
        System.out.println("Informe o valor que será depositado: ");
        var amount = scanner.nextLong();
        try{
            accountRepository.deposit(pix, amount);
        } catch (AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }
    private static void trasferToAccount(){
        System.out.println("Informe a chave pix da conta de origem:");
        var source = scanner.next();
        System.out.println("Informe a chave pix da conta de destino:");
        var target = scanner.next();
        System.out.println("Informe o valor que será depositado: ");
        var amount = scanner.nextLong();
        try{
            accountRepository.transferMoney(source, target, amount);
        } catch (AccountNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void createWalletInvestment(){
        System.out.println("Informe a chave pix da conta: ");
        var pix = scanner.next();
        var account = accountRepository.findByPix(pix);
        System.out.println("Informe o identificador do investimento");
        var investmentId = scanner.nextInt();
        var investmentWallet = investmentRepository.initInvestment(account, investmentId);
        System.out.println("Conta de investimento criada: " + investmentWallet);
    }

    private static void incInvestment() {
        System.out.println("Informe a chave pix da conta para investimento:");
        var pix = scanner.next();
        System.out.println("Informe o valor que será investido: ");
        var amount = scanner.nextLong();
        try {
            investmentRepository.deposit(pix, amount);
        } catch (AccountNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void rescueInvestment(){
        System.out.println("Informe a chave pix da conta para resgate do investimento: ");
        var pix = scanner.next();
        System.out.println("Informe o valor que será sacado: ");
        var amount = scanner.nextLong();
        try {

            investmentRepository.withdraw(pix, amount);
        } catch (NoFundsEnoughException | AccountNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void checkHistory() {
        System.out.println("Informe a chave pix da conta para verificar extrato: ");
        var pix = scanner.next();
        AccountWallet wallet;
        try {
            wallet = accountRepository.findByPix(pix);
            var audit = wallet.getFinancialTransactions();
            var group = audit.stream()
                    .collect(Collectors.groupingBy(t -> t.createdAt().truncatedTo(java.time.temporal.ChronoUnit.SECONDS)));
        } catch (AccountNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

    }




}
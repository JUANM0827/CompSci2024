import java.util.*;

abstract class Employee {
    String name;
    String classification;
    double hourlyRate;
    double hoursWorked;
    double overtimeRate;
}

interface PayCalculator {
    double regularPay(double hoursWorked, double hourlyRate);
    double overtimePay(double hoursWorked, double overtimeRate);
}

class PartTimeEmployee extends Employee implements PayCalculator {
    @Override
    public double regularPay(double hoursWorked, double hourlyRate) {
        return (hoursWorked > 20) ? 20 * hourlyRate : hourlyRate * hoursWorked;
    }

    @Override
    public double overtimePay(double hoursWorked, double overtimeRate) {
        return (hoursWorked > 20) ? (hoursWorked - 20) * overtimeRate : 0;
    }
}

class FullTimeEmployee extends Employee implements PayCalculator {
    @Override
    public double regularPay(double hoursWorked, double hourlyRate) {
        return (hoursWorked > 40) ? 40 * hourlyRate : hourlyRate * hoursWorked;
    }

    @Override
    public double overtimePay(double hoursWorked, double overtimeRate) {
        return (hoursWorked > 40) ? (hoursWorked - 40) * overtimeRate : 0;
    }
}

class Timesheet {
    String name;
    String classification;
    double hourlyRate;
    double hoursWorked;
    double regularPay;
    double overtimePay;
    double totalPay;

    public Timesheet(String name, String classification, double hourlyRate, double hoursWorked,
                     double regularPay, double overtimePay, double totalPay) {
        this.name = name;
        this.classification = classification;
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
        this.regularPay = regularPay;
        this.overtimePay = overtimePay;
        this.totalPay = totalPay;
    }

    public void display() {
        System.out.printf("\nName: %s\nClassification: %s\nHourly Rate: $%.2f\nHours Worked: %.2f\n", name, classification, hourlyRate, hoursWorked);
        System.out.printf("Regular Pay: $%.2f\nOvertime Pay: $%.2f\nTotal Pay: $%.2f\n", regularPay, overtimePay, totalPay);
    }
}

class Admin {
    public void viewTimesheets(List<Timesheet> timesheets) {
        if (timesheets.isEmpty()) {
            System.out.println("No timesheets available.");
        } else {
            System.out.println("\n--- Timesheet Records ---");
            for (Timesheet t : timesheets) {
                t.display();
            }
        }
    }
}

public class EmployeePayrollSystem2 {
    static Scanner sc = new Scanner(System.in);
    static List<Timesheet> timesheetRecords = new ArrayList<>();

    public static void main(String[] args) {
        // Load default employees
        preloadDefaultEmployees();

        System.out.println("Welcome to the Payroll System");
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();

        if (username.equals("admin") && password.equals("admin123")) {
            Admin admin = new Admin();
            System.out.println("Admin login successful.");
            admin.viewTimesheets(timesheetRecords);
        } else {
            System.out.println("Employee login successful.\n");
            processEmployeePayroll();
        }
    }

    public static void processEmployeePayroll() {
        System.out.print("Enter the name of the employee: ");
        String name = sc.nextLine();
        System.out.print("Enter the classification of the employee (PartTime or FullTime): ");
        String classification = sc.nextLine();
        System.out.print("Enter the hourly rate: ");
        double hourlyRate = sc.nextDouble();
        System.out.print("Enter hours worked: ");
        double hoursWorked = sc.nextDouble();
        sc.nextLine(); // consume newline

        Employee employee;
        if (classification.equalsIgnoreCase("PartTime")) {
            employee = new PartTimeEmployee();
        } else if (classification.equalsIgnoreCase("FullTime")) {
            employee = new FullTimeEmployee();
        } else {
            System.err.println("Invalid classification.");
            return;
        }

        employee.name = name;
        employee.classification = classification;
        employee.hourlyRate = hourlyRate;
        employee.hoursWorked = hoursWorked;
        employee.overtimeRate = hourlyRate * 1.5;

        PayCalculator payCalc = (PayCalculator) employee;
        double regularPay = payCalc.regularPay(hoursWorked, hourlyRate);
        double overtimePay = payCalc.overtimePay(hoursWorked, employee.overtimeRate);
        double totalPay = regularPay + overtimePay;

        Timesheet timesheet = new Timesheet(name, classification, hourlyRate, hoursWorked, regularPay, overtimePay, totalPay);
        timesheet.display();
        timesheetRecords.add(timesheet);
    }

    public static void preloadDefaultEmployees() {
        addEmployee("Alice", "PartTime", 15.0, 25);
        addEmployee("Bob", "FullTime", 20.0, 45);
        addEmployee("Charlie", "PartTime", 18.0, 18);
        addEmployee("Diana", "FullTime", 22.5, 38);
    }

    private static void addEmployee(String name, String classification, double hourlyRate, double hoursWorked) {
        Employee employee;
        if (classification.equalsIgnoreCase("PartTime")) {
            employee = new PartTimeEmployee();
        } else {
            employee = new FullTimeEmployee();
        }

        employee.name = name;
        employee.classification = classification;
        employee.hourlyRate = hourlyRate;
        employee.hoursWorked = hoursWorked;
        employee.overtimeRate = hourlyRate * 1.5;

        PayCalculator payCalc = (PayCalculator) employee;
        double regularPay = payCalc.regularPay(hoursWorked, hourlyRate);
        double overtimePay = payCalc.overtimePay(hoursWorked, employee.overtimeRate);
        double totalPay = regularPay + overtimePay;

        Timesheet timesheet = new Timesheet(name, classification, hourlyRate, hoursWorked, regularPay, overtimePay, totalPay);
        timesheetRecords.add(timesheet);
    }
}

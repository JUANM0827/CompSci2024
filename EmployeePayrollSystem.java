import java.util.Scanner;

abstract class Employee{//general employee class ( holds universal parameters)
    String name;
    String classification;
    double hourlyRate;
    double hoursWorked;
    double overtimeRate = hourlyRate * 1.5;
}
class PartTimeEmployee extends Employee implements PayCalculator{//part time employee uses 20 hrs as cap for regular pay

    @Override
    public double regularPay(double hoursWorked, double hourlyRate) {
        if (hoursWorked > 20){
            return 20 * hourlyRate;
        }
        else{
            return hourlyRate * hoursWorked;
        }
    }//calculate regular pay, 20 hr cap (anything more is illegal)

    @Override
    public double overtimePay(double hoursWorked, double overtimeRate) {
        if (hoursWorked <= 20){
            return 0;
        }
        else{
            return (hoursWorked - 20) * overtimeRate;
        }
    }//check if in OT first, then if you are calculate pay
}
class FullTimeEmployee extends Employee implements PayCalculator{//full time employee uses 40 hrs as cap for regular pay
    public boolean overtime (int hoursWorked){
        return hoursWorked > 40;
    }

    @Override
    public double regularPay(double hoursWorked, double hourlyRate) {
        if (hoursWorked > 40){
            return 40 * hourlyRate;
        }
        else{
            return hourlyRate * hoursWorked;
        }
    }

    @Override
    public double overtimePay(double hoursWorked, double overtimeRate) {
        if (hoursWorked <= 40){
            return 0;
        }
        else{
            return (hoursWorked - 40) * overtimeRate;
        }
    }//same calculations
}
interface PayCalculator{
    double regularPay (double hoursWorked,double hourlyRate);
    double overtimePay (double hoursWorked,double hourlyRate);
}//interface to be used by FT and PT employee
class EmployeePayrollSystem{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the name of the employee: ");
        String name = sc.nextLine();
        System.out.println("Enter the classification of the employee: ");
        String classification = sc.nextLine();
        System.out.println("Enter the hourly rate of the employee: ");
        double hourlyRate = sc.nextDouble();
        System.out.println("Enter the hours worked by the employee: ");
        double hoursWorked = sc.nextDouble();
        Employee employee;

        if (classification.equalsIgnoreCase("PartTime")) {
            employee = new PartTimeEmployee();
        } else if (classification.equalsIgnoreCase("FullTime")) {
            employee = new FullTimeEmployee();
        }//use equalsIgnoreCase for forgiveness in capitalization
        else {
            System.err.println("Invalid classification. Exiting program.");
            return;
        }

        employee.name = name;
        employee.classification = classification;
        employee.hourlyRate = hourlyRate;
        employee.hoursWorked = hoursWorked;
        employee.overtimeRate = employee.hourlyRate * 1.5;

        PayCalculator payCalculator = (PayCalculator) employee;

        double regularPay = payCalculator.regularPay(hoursWorked, hourlyRate);
        double overtimePay = payCalculator.overtimePay(hoursWorked, employee.overtimeRate);
        double totalPay = regularPay + overtimePay;

        System.out.println("\nEmployee Details:");
        System.out.println("Name: " + employee.name);
        System.out.println("Classification: " + employee.classification);
        System.out.println("Hourly Rate: $" + employee.hourlyRate);
        System.out.println("Hours Worked: " + employee.hoursWorked);
        System.out.println("Regular Pay: $" + regularPay);
        System.out.println("Overtime Pay: $" + overtimePay);
        System.out.println("Total Pay: $" + totalPay);
        sc.close();
    }
}
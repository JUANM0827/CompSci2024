import java.io.*;
import java.util.*;

// Employee class stores user credentials, personal info, classification, pay rate, and weekly hours
class Employee implements Serializable {
    String username, password, firstName, lastName, classification;
    double payRate;
    double[] hours = new double[7];

        // Constructor splits full name and initializes employee attributes
    public Employee(String username, String password, String fullName, String classification, double payRate) {
        this.username = username;
        this.password = password;
        String[] names = fullName.trim().split(" ", 2);
        this.firstName = names[0];
        this.lastName = (names.length > 1) ? names[1] : "";
        this.classification = classification;
        this.payRate = payRate;
    }

        // Calculates regular hours (up to 8/day for full-time or 4/day for part-time)
    public double getRegularHours() {
        double total = 0;
        for (double h : hours) total += Math.min(h, classification.equalsIgnoreCase("FullTime") ? 8 : 4);
        return total;
    }

        // Calculates overtime hours based on classification
    public double getOvertimeHours() {
        double total = 0;
        for (double h : hours) total += Math.max(0, h - (classification.equalsIgnoreCase("FullTime") ? 8 : 4));
        return total; // Overtime is no longer calculated separately

    }

        // Calculates total pay including overtime
    public double getTotalPay() {
        double reg = getRegularHours() * payRate;
        double ot = getOvertimeHours() * payRate * 1.5;
        return reg + ot;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}

public class EmployeePayrollSystem3 {
    static Scanner sc = new Scanner(System.in);
    static HashMap<String, Employee> employees = new HashMap<>();
    static final String FILE = "employeeinfo.txt";

        // Main program loop - shows menu and routes to features
    public static void main(String[] args) {
        loadData();
        while (true) {
            System.out.println("\n--- EmployeesPayrollSystem3 ---");
            System.out.println("1) Input Time");
            System.out.println("2) Check Payroll (Admin Only)");
            System.out.println("3) Create New User");
            System.out.print("Enter option: ");
            String opt = sc.nextLine();

            switch (opt) {
                case "1":
                    inputTime();
                    break;
                case "2":
                    checkPayroll();
                    break;
                case "3":
                    createUser();
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

        // Allows a logged-in employee to input hours for each day
    static void inputTime() {
        Employee emp = loginUser(false);
        if (emp == null) return;

        while (true) {
            System.out.printf("\n--- Input Time for %s ---%n", emp.getFullName());
            System.out.printf("Pay Rate: $%.2f%n%n", emp.payRate);
            String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
            for (int i = 0; i < 7; i++) {
                System.out.printf("%s: %.1f hrs%n", days[i], emp.hours[i]);
            }

            System.out.println("\n1) Edit Monday\n2) Edit Tuesday\n3) Edit Wednesday\n4) Edit Thursday");
            System.out.println("5) Edit Friday\n6) Edit Saturday\n7) Edit Sunday\n8) Logout");
            System.out.print("Enter option: ");
            String choice = sc.nextLine();

            try {
                int day = Integer.parseInt(choice);
                if (day >= 1 && day <= 7) {
                    System.out.print("Enter hours worked: ");
                    double hrs = Double.parseDouble(sc.nextLine());
                    if (hrs < 0) throw new NumberFormatException();
                    emp.hours[day - 1] = hrs;
                    saveData();
                    System.out.println("[Hours updated successfully]");
                } else if (day == 8) {
                    System.out.println("[Logged out]");
                    return;
                } else {
                    System.out.println("Invalid option.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Try again.");
            }
        }
    }

        // Admin-only feature to view employee hours and generate paychecks
    static void checkPayroll() {
        Employee admin = loginUser(true);
        if (admin == null) return;

        while (true) {
            System.out.println("\n1) View Employee Details");
            System.out.println("2) Print Paychecks");
            System.out.println("3) Logout");
            System.out.print("Enter option: ");
            String opt = sc.nextLine();

            switch (opt) {
                case "1":
                    for (Employee e : employees.values()) {
                        System.out.printf("\nName: %s\nPay Rate: $%.2f\nRegular Hours: %.1f\nOvertime Hours: %.1f\n",
                                e.getFullName(), e.payRate, e.getRegularHours(), e.getOvertimeHours());
                    }
                    break;
                case "2":
                    System.out.println("\n--- Weekly Paychecks ---");
                    for (Employee e : employees.values()) {
                        System.out.printf("%s: $%.2f%n", e.getFullName(), e.getTotalPay());
                    }
                    break;
                case "3":
                    System.out.println("[Admin logged out]");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

        // Allows creation of a new user account
    static void createUser() {
        System.out.print("Choose a username: ");
        String username = sc.nextLine();
        if (employees.containsKey(username)) {
            System.out.println("Username already exists.");
            return;
        }

        System.out.print("Choose a password: ");
        String password = sc.nextLine();
        System.out.print("Enter full name (First Last): ");
        String fullName = sc.nextLine();

        double payRate = 0;
        while (true) {
            try {
                System.out.print("Enter pay rate: ");
                payRate = Double.parseDouble(sc.nextLine());
                if (payRate <= 0) throw new NumberFormatException();
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid rate. Must be a positive number.");
            }
        }

        System.out.print("Enter classification (PartTime/FullTime): ");
        String classification = sc.nextLine();
        Employee newEmp = new Employee(username, password, fullName, classification, payRate);
        employees.put(username, newEmp);
        saveData();
        System.out.println("User created and logged out.");
    }

        // Authenticates users; blocks non-admins if adminOnly is true
    static Employee loginUser(boolean adminOnly) {
        System.out.print("Enter username: ");
        String user = sc.nextLine();
        System.out.print("Enter password: ");
        String pass = sc.nextLine();

        Employee e = employees.get(user);
        if (e == null || !e.password.equals(pass)) {
            System.out.println("Login failed.");
            return null;
        }
        if (adminOnly && !user.equals("admin")) {
            System.out.println("Access denied. Admin only.");
            return null;
        }
        return e;
    }

        // Loads employee data from plain text file or creates default admin
    static void loadData() {
        employees = new HashMap<>();
        File file = new File(FILE);
        if (!file.exists()) {
            Employee admin = new Employee("admin", "admin123", "System Admin", "FullTime", 0);
            employees.put("admin", admin);
            saveData();
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 13) {
                    String username = parts[0];
                    String password = parts[1];
                    String firstName = parts[2];
                    String lastName = parts[3];
                    String classification = parts[4];
                    double payRate = Double.parseDouble(parts[5]);
                    Employee e = new Employee(username, password, firstName + " " + lastName, classification, payRate);
                    for (int i = 0; i < 7; i++) {
                        e.hours[i] = Double.parseDouble(parts[6 + i]);
                    }
                    employees.put(username, e);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading data.");
        }
    }

        // Saves all employee information to file as plain text (CSV-style)
    static void saveData() {
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE))) {
            for (Employee e : employees.values()) {
                out.printf("%s,%s,%s,%s,%s,%.2f", e.username, e.password, e.firstName, e.lastName, e.classification, e.payRate);
                for (double h : e.hours) {
                    out.printf(",%.2f", h);
                }
                out.println();
            }
        } catch (IOException e) {
            System.out.println("Error saving data.");
        }
    }
}

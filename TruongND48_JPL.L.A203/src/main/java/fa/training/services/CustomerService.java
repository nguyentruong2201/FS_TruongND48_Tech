package TruongND48_JPL.L.A203.src.main.java.fa.training.services;

import TruongND48_JPL.L.A203.src.main.java.fa.training.entities.Customer;
import TruongND48_JPL.L.A203.src.main.java.fa.training.entities.Order;
import TruongND48_JPL.L.A203.src.main.java.fa.training.utils.Constants;
import TruongND48_JPL.L.A203.src.main.java.fa.training.utils.Validator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CustomerService {
    static Scanner scanner = new Scanner(System.in);

    public List<Customer> createCustomers(){
        // Lấy toàn bộ customers đã có (bao gồm cả trong file)
        List<Customer> allCustomers = findAll();
        List<Customer> newCustomers = new ArrayList<>();
        String continueInput;

        do {
            System.out.print("Enter customer name: ");
            String name = scanner.nextLine();

            // Lấy tất cả phone number đã có (bao gồm cả vừa nhập trong session này)
            List<String> existingPhoneNumbers = new ArrayList<>();
            for (Customer c : allCustomers) existingPhoneNumbers.add(c.getPhoneNumber());
            for (Customer c : newCustomers) existingPhoneNumbers.add(c.getPhoneNumber());

            String phone;
            while (true) {
                System.out.print("Enter phone number (10 digits): ");
                phone = scanner.nextLine();
                if (Validator.isValidPhoneNumber(phone, existingPhoneNumbers)) break;
                System.out.println("Invalid or duplicate phone number!");
            }

            System.out.print("Enter address: ");
            String address = scanner.nextLine();

            List<Order> orders = new ArrayList<>();
            // Lấy tất cả order number đã có (bao gồm cả vừa nhập trong session này)
            List<String> existingOrderNumbers = new ArrayList<>();
            for (Customer c : allCustomers) {
                if (c.getOrders() != null) {
                    for (Order o : c.getOrders()) existingOrderNumbers.add(o.getNumber());
                }
            }
            // Thêm order number của các customer mới nhập trong session này
            for (Customer c : newCustomers) {
                if (c.getOrders() != null) {
                    for (Order o : c.getOrders()) existingOrderNumbers.add(o.getNumber());
                }
            }

            while (true) {
                System.out.print("Enter order number (10 chars): ");
                String number = scanner.nextLine();
                if (!Validator.isValidOrderNumber(number, existingOrderNumbers)) {
                    System.out.println("Invalid or duplicate order number!");
                    continue;
                }
                existingOrderNumbers.add(number);

                System.out.print("Enter order date (yyyy-MM-dd): ");
                String dateStr = scanner.nextLine();
                Date date;
                try {
                    java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
                    date = new Date(utilDate.getTime());
                } catch (Exception e) {
                    System.out.println("Invalid date format!");
                    continue;
                }

                orders.add(new Order(number, date));

                System.out.print("Add another order? (y/n): ");
                if (!scanner.nextLine().equalsIgnoreCase("y")) break;
            }

            newCustomers.add(new Customer(name, phone, address, orders));

            System.out.print("Add another customer? (y/n): ");
            continueInput = scanner.nextLine();

        } while (!continueInput.equalsIgnoreCase("n"));

        return newCustomers;
    }

    public void save(List<Customer> customers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Constants.FILE_NAME))) {
            for (Customer customer : customers) {
                StringBuilder sb = new StringBuilder();
                sb.append(customer.getName()).append(",");
                sb.append(customer.getPhoneNumber()).append(",");
                sb.append(customer.getAddress()).append(",");
                if (customer.getOrders() != null && !customer.getOrders().isEmpty()) {
                    for (Order order : customer.getOrders()) {
                        sb.append(order.getNumber()).append(":").append(order.getDate()).append(";");
                    }
                    sb.deleteCharAt(sb.length() - 1); // remove last ;
                }
                writer.write(sb.toString());
                writer.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void display(List<Customer> customers) {
        if (customers.isEmpty()) {
            System.out.println("No customers to display.");
            return;
        }
        for (Customer customer : customers) {
            System.out.println(customer);
        }
    }

    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(Constants.FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 4);
                if (parts.length < 4) continue;
                String name = parts[0];
                String phone = parts[1];
                String address = parts[2];
                List<Order> orders = new ArrayList<>();
                if (!parts[3].isEmpty()) {
                    String[] orderParts = parts[3].split(";");
                    for (String op : orderParts) {
                        String[] orderFields = op.split(":");
                        if (orderFields.length == 2) {
                            String number = orderFields[0];
                            Date date = Date.valueOf(orderFields[1]);
                            orders.add(new Order(number, date));
                        }
                    }
                }
                customers.add(new Customer(name, phone, address, orders));
            }
        } catch (Exception e) {
            // file có thể chưa tồn tại, trả về list rỗng
        }
        return customers;
    }

    public List<Customer> searchByPhoneNumber(String phoneNumber) {
        List<Customer> customers = findAll();
        List<Customer> result = new ArrayList<>();
        for (Customer customer : customers) {
            if (customer.getPhoneNumber().equals(phoneNumber)) {
                result.add(customer);
            }
        }
        return result;
    }

    public boolean deleteByPhoneNumber(String phoneNumber) {
        List<Customer> customers = findAll();
        boolean found = false;
        for (Customer customer : customers) {
            if (customer.getPhoneNumber().equals(phoneNumber)) {
                customers.remove(customer);
                found = true;
                break;
            }
        }
        if (found) {
            // Save the updated list back to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(Constants.FILE_NAME))) {
                for (Customer customer : customers) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(customer.getName()).append(",");
                    sb.append(customer.getPhoneNumber()).append(",");
                    sb.append(customer.getAddress()).append(",");
                    if (customer.getOrders() != null && !customer.getOrders().isEmpty()) {
                        for (Order order : customer.getOrders()) {
                            sb.append(order.getNumber()).append(":").append(order.getDate()).append(";");
                        }
                        sb.deleteCharAt(sb.length() - 1); // remove last ;
                    }
                    writer.write(sb.toString());
                    writer.newLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return found;
    }

}

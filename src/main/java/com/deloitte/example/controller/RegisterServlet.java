package com.deloitte.example.controller;

import com.deloitte.example.dao.UserDAO;
import com.deloitte.example.model.User;
import com.deloitte.example.util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^\\d{10}$");
    private static final Pattern IDPROOF_PATTERN = Pattern.compile("^[A-Za-z0-9]{12}$");
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to login.jsp when GET request is made
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String dobStr = request.getParameter("dob");
        String mobile = request.getParameter("mobile");
        String idProof = request.getParameter("idProof");

        // Validation
        if (firstName == null || lastName == null || dobStr == null || mobile == null || idProof == null ||
            firstName.isEmpty() || lastName.isEmpty() || dobStr.isEmpty() || mobile.isEmpty() || idProof.isEmpty()) {
            request.setAttribute("error", "All fields are required.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        if (!MOBILE_PATTERN.matcher(mobile).matches()) {
            request.setAttribute("error", "Mobile must be 10 digits.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        if (!IDPROOF_PATTERN.matcher(idProof).matches()) {
            request.setAttribute("error", "ID Proof must be 12 alphanumeric characters.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        Date dob;
        try {
            dob = Date.valueOf(dobStr);
        } catch (Exception e) {
            request.setAttribute("error", "Invalid DOB format. Use yyyy-MM-dd.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // Generate userId and password
        String userId = generateUserId(firstName, dob, mobile, idProof);
        String password = generatePassword(firstName, lastName, dob, mobile, idProof);

        User user = new User();
        user.setUserId(userId);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setDob(dob);
        user.setMobile(mobile);
        user.setIdProof(idProof);

        try (Connection conn = DBConnection.getConnection()) {
            UserDAO userDAO = new UserDAO(conn);
            if (userDAO.idProofExists(idProof)) {
                request.setAttribute("error", "ID Proof already exists. Please use another.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }
            boolean registered = userDAO.registerUser(user);
            if (registered) {
                request.setAttribute("message", "Registration successful!<br>Your User ID: <b>" + userId + "</b><br>Your Password: <b>" + password + "</b><br>Please note them down.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Registration failed. Try again.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }

    // User ID: FirstName + year in DOB + first and last digit of mobile + digits in id_proof
    private String generateUserId(String firstName, Date dob, String mobile, String idProof) {
        String year = dob.toLocalDate().getYear() + "";
        String firstDigitMobile = mobile.substring(0, 1);
        String lastDigitMobile = mobile.substring(mobile.length() - 1);
        String digitsInIdProof = idProof.replaceAll("\\D", "");
        return firstName + year + firstDigitMobile + lastDigitMobile + digitsInIdProof;
    }

    // Password: last char of firstName + first char of lastName + sum of digits in DOB (one digit) + odd places digits in mobile + sum of even places digits in mobile + consonants/vowels logic + sum of digits in id_proof (2 digits)
    private String generatePassword(String firstName, String lastName, Date dob, String mobile, String idProof) {
        // Last character of FirstName
        char lastCharFirstName = firstName.charAt(firstName.length() - 1);
        // First character of LastName
        char firstCharLastName = lastName.charAt(0);

        // Sum of digits in DOB (yyyy-MM-dd), reduce to 1 digit
        String dobStr = dob.toString().replaceAll("-", "");
        int dobSum = 0;
        for (char c : dobStr.toCharArray()) {
            if (Character.isDigit(c)) dobSum += c - '0';
        }
        while (dobSum > 9) dobSum = (dobSum / 10) + (dobSum % 10);

        // Odd places digits in mobile (1st, 3rd, 5th, 7th, 9th)
        StringBuilder oddDigits = new StringBuilder();
        for (int i = 0; i < mobile.length(); i += 2) {
            oddDigits.append(mobile.charAt(i));
        }
        // Sum of even places digits in mobile (2nd, 4th, 6th, 8th, 10th)
        int evenSum = 0;
        for (int i = 1; i < mobile.length(); i += 2) {
            evenSum += mobile.charAt(i) - '0';
        }

        // Consonants/vowels logic for id_proof
        String vowels = "AEIOUaeiou";
        char firstCharIdProof = idProof.charAt(0);
        StringBuilder idProofPart = new StringBuilder();
        if (!vowels.contains("" + firstCharIdProof)) { // consonant
            for (char c : idProof.toCharArray()) {
                if (Character.isLetter(c) && !vowels.contains("" + c)) idProofPart.append(c);
            }
        } else { // vowel
            for (char c : idProof.toCharArray()) {
                if (Character.isLetter(c) && vowels.contains("" + c)) idProofPart.append(c);
            }
        }

        // Sum of digits in id_proof (2 digits, pad with 0 if needed)
        int idProofDigitSum = 0;
        for (char c : idProof.toCharArray()) {
            if (Character.isDigit(c)) idProofDigitSum += c - '0';
        }
        String idProofDigitSumStr = String.format("%02d", idProofDigitSum);

        return "" + lastCharFirstName + firstCharLastName + dobSum + oddDigits + evenSum + idProofPart + idProofDigitSumStr;
    }
}

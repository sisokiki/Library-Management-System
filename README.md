# 📚 Library Management System

A robust, production-grade desktop application designed to automate library operations. Built using **Java Swing**, **MySQL**, and **JDBC**, following the MVC (Model-View-Controller) and DAO (Data Access Object) architectural patterns.

## 🚀 Features

### 🔐 **Access Control**

  * **Role-Based Login:** Secure login for **Admins** and **Standard Users**.
  * **Hidden Credentials:** Password masking implemented for security.
  * **Admin Privileges:** Access to Maintenance modules (Add Books/Members).
  * **User Privileges:** Restricted to Transactions and Reports only.

### 🛠 **Maintenance (Admin Only)**

  * **Add Books:** Register new books into the system with auto-generated IDs.
  * **Add Members:** Register new students/faculty.
  * **Validation:** Strict input validation to prevent empty or invalid data entry.

### 🔄 **Transactions**

  * **Issue Book:**
      * Validates Member and Book availability.
      * **Date Logic:** Prevents issuing books with a return date \> 15 days.
  * **Return Book:**
      * Auto-fetches issue details.
      * Calculates overdue days automatically.
  * **Pay Fine:**
      * Auto-calculates fines (Rs. 10/day) for overdue books.
      * Enforces payment confirmation before closing the transaction.

### 📊 **Reports**

  * **Master Lists:** View all Books and Members in a tabular format.
  * **Search:** Real-time dynamic search by ID or Name.

### 🎨 **UI/UX**

  * **Modern Theme:** Integrated **FlatLaf** for a professional Dark Mode look.
  * **Date Pickers:** Integrated `JCalendar` for intuitive date selection.
  * **Responsive:** Windows open in full screen or centered modes.

-----

## 🛠 Tech Stack

  * **Language:** Java (JDK 17+)
  * **GUI Framework:** Swing (JInternalFrame, JDesktopPane)
  * **Database:** MySQL Server
  * **Database Connectivity:** JDBC
  * **IDE:** IntelliJ IDEA / Eclipse
  * **Build Tool:** Default Java Build System

### 📦 External Libraries (JARs)

1.  `mysql-connector-java-8.0.28.jar` - Database Driver.
2.  `jcalendar-tz-1.3.3-4.jar` - Date Picker Component.
3.  `rs2xml.jar` - Util to convert SQL ResultSet to JTable Model.
4.  `flatlaf-3.6.jar` - Modern Look and Feel (Theme).

-----

## ⚙️ Installation & Setup

### 1\. Database Setup

Open **MySQL Workbench** and run the following script to initialize the schema:

```sql
CREATE DATABASE library_management_system;
USE library_management_system;

-- 1. Login Table
CREATE TABLE login(username varchar(20), password varchar(20), role varchar(20));
INSERT INTO login VALUES('admin', 'admin123', 'admin');
INSERT INTO login VALUES('user', 'user123', 'user');

-- 2. Core Tables
CREATE TABLE book(book_id varchar(20), name varchar(50), author varchar(50), category varchar(20), status varchar(20), cost varchar(20), date varchar(20));
CREATE TABLE member(member_id varchar(20), name varchar(30), father_name varchar(30), dob varchar(20), address varchar(50), phone varchar(20), email varchar(30), aadhar varchar(20));
CREATE TABLE issuebook(book_id varchar(20), member_id varchar(20), book_name varchar(50), member_name varchar(50), issue_date varchar(20), return_date varchar(20));
CREATE TABLE returnbook(book_id varchar(20), member_id varchar(20), bname varchar(50), mname varchar(50), issue_date varchar(20), return_date varchar(20), fine varchar(20));
```

### 2\. Project Configuration

1.  Clone or Download this repository.
2.  Open the project in **IntelliJ IDEA**.
3.  Create a folder named `lib` in the project root.
4.  Add the 4 JAR files listed above into `lib` and **Add as Library** (Right-click -\> Add as Library).
5.  Update Database Credentials:
      * Open `src/library/management/system/Conn.java`.
      * Change the password field to your MySQL password:
        ```java
        c = DriverManager.getConnection("jdbc:mysql:///library_management_system", "root", "YOUR_PASSWORD");
        ```

### 3\. Running the Application

1.  Navigate to `src/library/management/system/Login.java`.
2.  Right-click and select **Run 'Login.main()'**.
3.  **Credentials:**
      * **Admin:** `admin` / `admin123`
      * **User:** `user` / `user123`

-----

## 📂 Project Structure

```text
LibraryManagementSystem/
├── .idea/
├── lib/                     # External JARs
│   ├── flatlaf.jar
│   ├── jcalendar.jar
│   ├── mysql-connector.jar
│   └── rs2xml.jar
├── out/                     # Compiled classes
├── src/
│   ├── icons/               # Images and Backgrounds
│   └── library/management/system/
│       ├── AddBook.java
│       ├── AddMember.java
│       ├── BookDetails.java
│       ├── BookIssue.java
│       ├── Conn.java        # DB Connection
│       ├── Login.java       # Entry Point
│       ├── Main.java        # Dashboard
│       ├── MemberDetails.java
│       ├── PayFine.java
│       └── ReturnBook.java
└── LibraryManagementSystem.iml
```

-----

## 📸 Usage Guide

1.  **Login:** Start the app. Use Admin credentials to set up data.
2.  **Add Data:** Go to `Maintenance` -\> Add a Book and a Member.
3.  **Issue:** Go to `Transactions` -\> `Issue Book`. Ensure the return date is within 15 days.
4.  **Return:** Go to `Transactions` -\> `Return Book`. Select the book to return.
5.  **Fine:** If the book is overdue, the system will prompt to pay the calculated fine before finalizing the return.

-----

## 🤝 Contribution

Feel free to fork this project and submit Pull Requests.

1.  Fork the repo.
2.  Create your feature branch (`git checkout -b feature/NewFeature`).
3.  Commit your changes (`git commit -m 'Add NewFeature'`).
4.  Push to the branch (`git push origin feature/NewFeature`).
5.  Open a Pull Request.

-----

**Developed with ❤️ using Java Swing.**

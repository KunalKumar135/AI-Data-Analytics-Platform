# 🤖 AI Data Analytics Platform

An AI-powered data analytics platform that allows users to upload CSV datasets and query them using natural language. The application converts user questions into SQL using AI (Groq/Ollama), executes the query on PostgreSQL, and returns the results.

---

## 🚀 Features

- 📂 Upload CSV datasets
- 🗄️ Dynamically create database tables from uploaded files
- 💬 Ask questions in plain English
- 🤖 AI converts natural language to SQL
- ⚡ Execute SQL on PostgreSQL
- 📊 Display query results in a user-friendly interface
- 🔄 Supports both **Groq API** and **Ollama (Local LLM)**

---

## 🏗️ Architecture

```
                +-------------------+
                |   React Frontend  |
                +---------+---------+
                          |
                          | REST API
                          |
                +---------v---------+
                | Spring Boot Backend|
                +---------+---------+
                          |
          +---------------+---------------+
          |                               |
+---------v---------+          +----------v----------+
| PostgreSQL         |          | AI Engine            |
| Dynamic Tables     |          | Groq / Ollama         |
+--------------------+          +-----------------------+
```

---

## 🛠️ Tech Stack

**Backend**
- Java 17
- Spring Boot
- Spring Data JPA
- REST APIs
- Maven

**Frontend**
- React.js
- HTML
- CSS
- JavaScript

**Database**
- PostgreSQL

**AI**
- Groq API
- Ollama
- Llama 3.3
- Qwen2.5

**Tools**
- VS Code
- Git
- GitHub
- Postman

---

## 📁 Project Structure

```
AI-Data-Analytics-Platform
│
├── ai-analytics-ui/          # React Frontend
│
├── src/                       # Spring Boot Backend
│
├── uploads/                   # Uploaded CSV files
│
├── pom.xml
├── README.md
└── .gitignore
```

---

## ⚙️ Installation

### 1. Clone Repository

```bash
git clone https://github.com/KunalKumar135/AI-Data-Analytics-Platform.git
cd AI-Data-Analytics-Platform
```

### 2. Backend Setup

```bash
mvn spring-boot:run
```

### 3. Frontend Setup

```bash
cd ai-analytics-ui
npm install
npm start
```

---

## 🗄️ PostgreSQL Configuration

Create a PostgreSQL database, for example:

```sql
CREATE DATABASE analytics_db;
```

Update your local configuration:

```
DB_URL=jdbc:postgresql: postgre_url
DB_USERNAME=your_username
DB_PASSWORD=your_password
GROQ_API_KEY=your_groq_api_key

For the password and api keys I have specified locallhy using set environment to set the
parameters in my local system
setx DB_URL "postgre_url"
```

---

## 📤 Upload Dataset

Upload any CSV file through the UI.

Example datasets:
- Employees
- Sales
- Products

The application automatically:
- Reads CSV columns
- Creates a PostgreSQL table
- Stores metadata

---

## 💬 Example Queries

```
How many employees are in each department?
Which department has the highest average salary?
What is the total salary paid to all employees?
Display the first names and salaries of the five employees with the lowest salaries?
```

---

## 📸 Screenshots

> Add screenshots here.

```
screenshots/
-->I have added the referenced result screenshot of the final output.
--> With the SQL Query, Explanation and output from the dataset which the sql query executes 
    from backend PostGreSql.
```

---

## 🔮 Future Enhancements

- [ ] User Authentication
- [ ] Chat History
- [ ] Multiple Database Support
- [ ] Data Visualization (Charts)
- [ ] Export Results to Excel/PDF
- [ ] Query Suggestions
- [ ] Dashboard Analytics

---

## 👨‍💻 Author

**Kunal Kumar**
GitHub: [@KunalKumar135](https://github.com/KunalKumar135)

---

## 📄 License

This project is for learning and portfolio purposes.

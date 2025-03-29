# **Concurrency Control Locking in Spring Boot** 🚀  

This project demonstrates **pessimistic locking** and **skip locking** for concurrency control in a **Spring Boot** application using **MySQL** and **Docker Compose**. It processes tasks by selecting **1,000 unprocessed tasks** at startup and  handling them in parallel **10 at a time**.  

## 📌 **How It Works**  
1. **At startup, the system fetches 1,000 unprocessed tasks** from `tbl_process`.  
2. **Parallel processing begins**, handling 10 tasks at a time.  
3. **Pessimistic Locking** ensures no two workers process the same task.  
4. **Skip Locking** allows efficient handling of remaining tasks.  

---

## 📊 **Concurrency Workflow**  

```plaintext
+-------------------------------+
|         Unprocessed Tasks     |
|        (Selected 1000)        |
+-------------------------------+
           || (Parallel 100 workers)
           \/
+-------------------------------+
| Worker 1  | Worker 2 | ... 100 |
| (Locks row & processes task) |
+-------------------------------+
           || (Skip Locked)
           \/
+-------------------------------+
|        Remaining Tasks        |
|     (Only unlocked tasks)     |
+-------------------------------+
```

---

## 🛠 **Tech Stack**  
- **Spring Boot 3**  
- **Spring Data JPA**  
- **MySQL (Docker)**  
- **Pessimistic Locking (`@Lock(PESSIMISTIC_WRITE)`)**  
- **Skip Locking (`FOR UPDATE SKIP LOCKED`)**  

---

## 🚀 **Running the Project**  

### **1️⃣ Clone the Repository**  
```sh
git clone https://github.com/your-repo/concurrency-control-locking.git
cd concurrency-control-locking
```

### **2️⃣ Start MySQL with Docker Compose**  
```sh
docker-compose up -d
```

✅ **This will create:**  
- A **MySQL database** named `task_db`.  
- A table `tbl_process` with sample **processed** and **unprocessed** tasks.  

### **3️⃣ Build & Run the Spring Boot Application**  
```sh
mvn clean install
mvn spring-boot:run
```

🔹 The system **fetches 1,000 tasks** and starts processing **10 at a time** using parallel threads.  

---

## 🗄 **Database Schema (`tbl_process`)**  
```sql
CREATE TABLE tbl_process (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    processed BOOLEAN NOT NULL
);
```

✅ **Sample Data (Automatically Inserted at Startup)**  

| id  | processed |
|-----|----------|
| 1   | FALSE    |
| 2   | FALSE    |
| 3   | TRUE     |
| ... | ...      |

---

## 🔄 **How the Locking Works**  

### **🔹 Pessimistic Locking (`@Lock(PESSIMISTIC_WRITE)`)**
- Ensures **no two workers** process the same task.  
- Prevents conflicts by **locking the row** until the transaction is complete.  

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
@QueryHints({
    @QueryHint(name = "javax.persistence.lock.timeout", value = "-2") // Skip locked rows
})
@Query("SELECT t FROM Process t WHERE t.processed = false")
List<Process> findUnprocessedWithLock();
```

---

### **🔹 Skip Locking (`FOR UPDATE SKIP LOCKED`)**
- **Skips already locked rows** to avoid blocking other transactions.  
- Used for **efficient parallel task processing**.  

```sql
SELECT * FROM tbl_process WHERE processed = FALSE 
FOR UPDATE SKIP LOCKED LIMIT 10;
```

---

1️⃣ **Start multiple instances** of the application:  
```sh
mvn spring-boot:run
```
2️⃣ **Observe logs** to see how tasks are processed in parallel.  
3️⃣ **Check the database** to verify processed tasks:  
```sql
SELECT COUNT(*) FROM tbl_process WHERE processed = TRUE;
```

---

## 📌 **Conclusion**  
🔹 **Pessimistic Locking** ensures safe, conflict-free processing.  
🔹 **Skip Locking** improves efficiency by avoiding locked rows.  
🔹 **Parallel processing** speeds up task execution.  

# 🏨 Smart Hostel Room Allocation System

A desktop-based Smart Hostel Room Allocation System built with **Java Swing**, designed around four core Data Structures & Algorithms (DSA) — each mapped to a real feature rather than used as a generic container. The system automates hostel room allocation, student management, waiting lists, and allocation history.

## ✨ Features

- 🔐 Secure Login System
- 👨‍🎓 Student Registration & Management
- 🏠 Smart Room Allocation
- 📋 Waiting List Management
- 🤝 Roommate Compatibility Matching
- 📊 Reports & Allocation History
- 💾 File-Based Data Storage
- 🖥️ Interactive Java Swing GUI

## 🧠 Data Structures Used

| Feature                | Data Structure         | Where                                     |
|-------------------------|--------------------------|--------------------------------------------|
| Student registry         | Doubly Linked List       | `datastructures/StudentLinkedList.java`   |
| Room lookup              | HashMap                  | `manager/RoomManager.java`                |
| Waiting list priority    | Priority Queue (heap)    | `manager/WaitingListManager.java`         |
| Allocation history       | Stack                    | `datastructures/AllocationHistory.java`   |

Roommate compatibility (food preference, study habit, cleanliness, AC preference, academic year) is scored by `service/RoommateMatcher.java` and used both for Smart Allocation and the standalone Compatibility view.

## 🛠️ Technologies Used

- Java
- Java Swing
- Object-Oriented Programming (OOP)
- File Handling
- Collections Framework
- Custom DSA Implementations

## 📂 Project Structure

```
src/
  Main.java              Entry point
  model/                 Student, Room, AllocationRecord
  datastructures/         StudentLinkedList, StudentNode, AllocationHistory
  manager/                RoomManager, WaitingListManager, StudentPriorityComparator
  service/                HostelService, RoommateMatcher
  util/                   Validation, FileManager, UITheme
  ui/                     LoginFrame, MainFrame, SidebarPanel, DashboardPanel,
                          StudentPanel, RoomPanel, CompatibilityPanel,
                          WaitingListPanel, HistoryPanel, ReportsPanel
data/                    Exported students.txt / rooms.txt / waitingList.txt / history.txt
report/                  Project report
screenshots/             App screenshots
```

## 🚀 How to Run

**Clone the repository**
```bash
git clone https://github.com/SOUMYASHARMA03/Smart-Hostel-Room-Allocation-System.git
```

**Compile and run**
```bash
cd src
javac -d ../out $(find . -name "*.java")
java -cp ../out Main
```

Or open the project folder in any IDE that reads a plain `src/` layout (IntelliJ IDEA, Eclipse, NetBeans, or VS Code with the Java Extension Pack) and run `Main.java` directly.

## 🖼️ Screens

Login → Dashboard → Students → Smart Allocation → Compatibility → Waiting List → History → Reports

## 📸 Screenshots

Screenshots are available in the `screenshots` folder.

## 📈 Complexity Notes

- **Doubly Linked List** — O(1) insert, O(n) search/remove
- **HashMap** — O(1) average lookup/insert
- **Priority Queue (heap)** — O(log n) insert/remove, O(1) peek
- **Stack** — O(1) push/pop

## 👨‍💻 Author

Soumya Sharma

## 📄 License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.

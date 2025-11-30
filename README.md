# task-tracker

Hi guys, this is a CLI App to track your tasks and manage your to-do list.
I'm doing this as a project in the [roadmap.sh](https://roadmap.sh) website.
Here is the link to the Project Detail page: https://roadmap.sh/projects/task-tracker

## Features
- Add, Update, and Delete tasks
- Mark a task as in progress or done
- List all tasks
- List all tasks that are done
- List all tasks that are not done
- List all tasks that are in progress

## Installation
### prerequisites:
JDK 17, Maven
### Installation steps
1. Clone the repo
    ```bash
    git clone https://github.com/vinayroffl/task-tracker.git
    cd task-tracker
    ```
2. Build the jar file
    ```bash
    mvn clean install
    ```
    ```task-tracker.jar``` will be generated in ```task-tracker\target``` folder

3. Copy ```task-tracker.jar``` from your the target folder paste it in any folder where you want to install the application.

## Usage
To use the application, navigate to the installation directory (where you have your ```task-tracker.jar```)  
Open your terminal application and start using the application
```bash
# Adding a new task
java -jar task-tracker.jar add "Buy groceries"
# Output: Task added successfully (ID: 1)

# Updating a task
java -jar task-tracker.jar update 1 "Buy groceries and cook dinner"
# Output: Task updated successfully (ID: 1)

# Deleting a task
java -jar task-tracker.jar delete 1
# Output: Task deleted successfully (ID: 1)

# Marking a task as in progress
java -jar task-tracker.jar mark-in-progress 1
# Output: Task marked as in progress (ID: 1)

# Marking a task as done
java -jar task-tracker.jar mark-done 1
# Output: Task marked as done (ID: 1)

# Listing all tasks
java -jar task-tracker.jar list
# Output: List of all tasks

# Listing tasks by status
java -jar task-tracker.jar list todo
java -jar task-tracker.jar list in-progress
java -jar task-tracker.jar list done
```

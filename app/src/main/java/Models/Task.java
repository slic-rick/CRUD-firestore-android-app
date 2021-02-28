package Models;

import com.google.firebase.firestore.Exclude;

public class Task {

    private String taskId;
    private String moduleName;
    private String instruction;
    private String dueDay;
    private String courseName;
    private String courseID;
    private String dueTime,moduleId;

    public Task() {}

    public Task(String courseID,String courseName,String moduleId,String moduleName, String instruction, String dueDay, String dueTime) {

        this.courseID = courseID;
        this.courseName = courseName;
        this.moduleName = moduleName;
        this.instruction = instruction;
        this.dueDay = dueDay;
        this.dueTime = dueTime;
        this.moduleId = moduleId;
    }
    @Exclude
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getDueDay() {
        return dueDay;
    }

    public void setDueDay(String dueDay) {
        this.dueDay = dueDay;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getDueTime() {
        return dueTime;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }
}

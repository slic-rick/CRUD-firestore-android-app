package Models;

import com.google.firebase.firestore.Exclude;

import java.util.List;

public class Module {

    private String moduleId;
    private String moduleName;
    private String courseName;
    private int yearCategory;


    public Module() {
    }

    public Module(String courseName,String moduleName,int yearCategory) {
        this.moduleName = moduleName;
        this.courseName = courseName;
        this.yearCategory = yearCategory;
    }
    @Exclude
    public String getModuleId() {
        return moduleId;
    }


    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getYearCategory() {
        return yearCategory;
    }

    public void setYearCategory(int yearCategory) {
        this.yearCategory = yearCategory;
    }
}

package Models;

import com.google.firebase.firestore.Exclude;

import java.util.List;

public class Course {
    private String courseId;
    private String courseName;



    public Course() {}

    public Course(String courseName) {
        this.courseName = courseName;

    }
    @Exclude
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }


    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

}

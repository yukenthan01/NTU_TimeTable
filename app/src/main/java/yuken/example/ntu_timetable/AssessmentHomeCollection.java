package yuken.example.ntu_timetable;

import java.util.ArrayList;

public class AssessmentHomeCollection {
    public String moduleId = "";
    public String credits ="";
    public String publishedDate="";
    public String publishedTime ="";
    public String dueDate ="";
    public String dueTime ="";
    public String status ="";
    public String lecturerId ="";

    public static ArrayList<AssessmentHomeCollection> date_collection_arr;

    public AssessmentHomeCollection(String moduleId, String credits, String publishedDate, String publishedTime, String dueDate, String dueTime, String lecturerId) {
        this.moduleId = moduleId;
        this.credits = credits;
        this.publishedDate = publishedDate;
        this.publishedTime = publishedTime;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.lecturerId = lecturerId;
    }
}

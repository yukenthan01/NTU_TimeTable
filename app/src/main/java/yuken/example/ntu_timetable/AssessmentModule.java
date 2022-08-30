package yuken.example.ntu_timetable;

public class AssessmentModule {
    String moduleId,credits,publishedDate,publishedTime,dueDate,dueTime,status,lecturerId;
    public AssessmentModule() {
    }

    public AssessmentModule(String moduleId, String credits, String publishedDate,
                            String publishedTime, String dueDate, String dueTime,
                            String lecturerId, String status
                            ) {
        this.moduleId = moduleId;
        this.credits = credits;
        this.publishedDate = publishedDate;
        this.publishedTime = publishedTime;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.lecturerId = lecturerId;
        this.status = status;

    }

    public String getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(String lecturerId) {
        this.lecturerId = lecturerId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getPublishedTime() {
        return publishedTime;
    }

    public void setPublishedTime(String publishedTime) {
        this.publishedTime = publishedTime;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDueTime() {
        return dueTime;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

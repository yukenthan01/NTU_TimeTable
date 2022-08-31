package yuken.example.ntu_timetable;

public class UserModel {
    String batchId,email,firstname,lastname,status,universityId,userRole,courseId,degreeLevel;

    public UserModel() {
    }

    public UserModel(String batchId, String email, String firstname, String lastname,
                     String status, String universityId, String userRole, String courseId,String degreeLevel) {
        this.batchId = batchId;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.status = status;
        this.universityId = universityId;
        this.userRole = userRole;
        this.courseId = courseId;
        this.degreeLevel = degreeLevel;
    }

    public String getDegreeLevel() {
        return degreeLevel;
    }

    public void setDegreeLevel(String degreeLevel) {
        this.degreeLevel = degreeLevel;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUniversityId() {
        return universityId;
    }

    public void setUniversityId(String universityId) {
        this.universityId = universityId;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}

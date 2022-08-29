package yuken.example.ntu_timetable;


import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TimeTableModel {
    String lecturerId,moduleId,date,startTime,endTime,classType,location,batchNo,status,
    termStartDate,termEndDate;
    DataSeeds dataSeeds = new DataSeeds();
    Map<String,Object> lectureName = new HashMap<>();;

    public TimeTableModel() {

    }

    public TimeTableModel(String lecturerId, String moduleId, String date, String startTime, String endTime, String classType, String location, String batchNo, String status, String termStartDate, String termEndDate) {
        this.lecturerId = lecturerId;
        this.moduleId = moduleId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.classType = classType;
        this.location = location;
        this.batchNo = batchNo;
        this.status = status;
        this.termStartDate = termStartDate;
        this.termEndDate = termEndDate;
    }

    public String getLecturerId() {


        //Log.d("TAG", "TimeTableModel2: "+getLecturerId());
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

    public String getDate() {return date;}

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTermStartDate() {
        return termStartDate;
    }

    public void setTermStartDate(String termStartDate) {
        this.termStartDate = termStartDate;
    }

    public String getTermEndDate() {
        return termEndDate;
    }

    public void setTermEndDate(String termEndDate) {
        this.termEndDate = termEndDate;
    }
}

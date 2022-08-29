package yuken.example.ntu_timetable;

import java.util.ArrayList;

public class HomeCollection {
    String lecturerId,moduleId,date,startTime,endTime,classType,location,batchNo;


    public static ArrayList<HomeCollection> date_collection_arr;
    public HomeCollection(String lecturerId, String moduleId, String date, String startTime, String endTime, String classType, String location, String batchNo){

        this.lecturerId = lecturerId;
        this.moduleId = moduleId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.classType = classType;
        this.location = location;
        this.batchNo = batchNo;

    }
}

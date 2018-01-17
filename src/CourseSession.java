public class CourseSession {

    private String section, Component, ClassNbr, Days, time, end_time, location, instructor, courseType, status;

    public CourseSession(String section, String Component, String ClassNbr, String Days, String time, String end_time,
                         String location, String instructor, String courseType, String status) {

        this.section = section;
        this.Component = Component;
        this.ClassNbr = ClassNbr;
        this.Days = Days;
        this.time = time;
        this.end_time = end_time;
        this.location = location;
        this.instructor = instructor;
        this.courseType = courseType;
        this.status = status;
    }

    public String getSection() {
        return section;
    }

    public String getComponent() {
        return Component;
    }

    public String getClassNbr() {
        return ClassNbr;
    }

    public String getDays() {
        return Days;
    }

    public String getTime() {
        return time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getLocation() {
        return location;
    }

    public String getInstructor() {
        return instructor;
    }

    public String getCourseType() {
        return courseType;
    }

    public String getStatus() {
        return status;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setComponent(String component) {
        this.Component = component;
    }

    public void setClassNbr(String classNbr) {
        this.ClassNbr = classNbr;
    }

    public void setDays(String days) {
        this.Days = days;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toString() {
        return getSection() + " is " + getStatus();
    }
}
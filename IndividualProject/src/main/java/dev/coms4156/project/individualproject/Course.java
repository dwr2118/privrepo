package dev.coms4156.project.individualproject;

import java.io.Serial;
import java.io.Serializable;


/**
 * The Course class allows for us to dynamically assign different attributes to different classes.
 */
public class Course implements Serializable {
  
  /**
   * Constructs a new Course object with the given parameters. Initial count starts at 0.
   *
   * @param instructorName The name of the instructor teaching the course.
   * @param courseLocation The location where the course is held.
   * @param timeSlot       The time slot of the course.
   * @param capacity       The maximum number of students that can enroll in the course.
   */
  public Course(String instructorName, String courseLocation, String timeSlot,
                int capacity) {
    this.instructorName = instructorName;
    this.courseLocation = courseLocation;
    this.courseTimeSlot = timeSlot;
    this.enrollmentCapacity = capacity;
    this.enrolledStudentCount = 500;
  }
  
  /**
   * Enrolls a student in the course if there is space available.
   *
   * @return true if the student is successfully enrolled, false otherwise.
   */
  public boolean enrollStudent() {
    if (isCourseFull()) {
      return false;
    } else {
      enrolledStudentCount++;
      return true;
    }
  }
  
  /**
   * Drops a student from the course if a student is enrolled.
   *
   * @return true if the student is successfully dropped, false otherwise.
   */
  public boolean dropStudent() {
    
    if (enrolledStudentCount == 0) {
      return false;
    } else {
      enrolledStudentCount--;
      return true;
    }
  }

  public String getCourseLocation() {
    return this.courseLocation;
  }

  public String getInstructorName() {
    return this.instructorName;
  }
  
  
  public String getCourseTimeSlot() {
    return this.courseTimeSlot;
  }
  
  @Override
  public String toString() {
    return "\nInstructor: " + instructorName + "; Location: " + courseLocation
           + "; Time: " + courseTimeSlot;
  }
  
  
  public void reassignInstructor(String newInstructorName) {
    this.instructorName = newInstructorName;
  }
  
  
  public void reassignLocation(String newLocation) {
    this.courseLocation = newLocation;
  }
  
  
  public void reassignTime(String newTime) {
    this.courseTimeSlot = newTime;
  }
  

  /**
   * Sets the number of students enrolled in the course.
   *
   * @param count The number of students enrolled in the course.
   * @return true if the count is successfully set, false otherwise.
   */
  public boolean setEnrolledStudentCount(int count) {
    if (count <= enrollmentCapacity) {
      this.enrolledStudentCount = count;
      return true;
    } else {
      return false;
    }
  }
  
  
  public boolean isCourseFull() {

    return this.enrolledStudentCount >= enrollmentCapacity;
  }
  
  @Serial
  private static final long serialVersionUID = 123456L;
  private final int enrollmentCapacity;
  private int enrolledStudentCount;
  private String courseLocation;
  private String instructorName;
  private String courseTimeSlot;
}

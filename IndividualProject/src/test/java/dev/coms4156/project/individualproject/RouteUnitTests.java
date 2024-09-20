package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;


/**
 * This is just testing if we can read our database from the ./data.txt file
 * and extract departments & courses from the database.
 */
@SpringBootTest
@ContextConfiguration
public class RouteUnitTests {
  
  /**
   * We are creating a routeController instance to test the routes.
   */
  @BeforeAll
  public static void setupTestRouteController() {

    testRouteController = new RouteController();
    
  }
  
  /**
   * Instantiating our test department that we'll be working with throughout testing.
   */
  @BeforeAll
  public static void setupDepartmentForTesting() {
    Course coms4156 = new Course("Gail Kaiser", "501 NWC", "11:40-12:55", 120);
    coms4156.setEnrolledStudentCount(109);
    courses = new HashMap<>();
    courses.put("4156", coms4156);
    testDept = new Department("COMS", courses, "Luca Carloni", 2700);
  }

  /**
   * We are testing if we can get the expected string returned by the index, home
   * and/or the root route. 
   */
  @Test
  public void indexRouteTest() {
    String expectedResult = "Welcome, in order to make an API call direct your browser or"
        + " Postman to an endpoint "
        + "\n\n This can be done using the following format: \n\n http:127.0.0"
        + ".1:8080/endpoint?arg=value";
    assertEquals(expectedResult, testRouteController.index());
  }

  /**
   * We are testing if we can receive Department Not Found when we try to retrieve a department
   * that doesn't exist within data.txt
   */
  @Test
  public void retrieveInvalidDepartmentTest() {
    String expectedResult = "Department Not Found";
    ResponseEntity<?> response = testRouteController.retrieveDepartment("cheese");
    assertEquals(expectedResult, response.getBody());
  }

  /**
   * We are testing if we can receive an HTTP 200 response when we try to retrieve a valid 
   * department from our database. 
   */
  @Test
  public void retrieveValidDepartmentTest() {
    String expectedResult = "200 OK";
    ResponseEntity<?> response = testRouteController.retrieveDepartment("COMS");
    HttpStatusCode responseStatus = response.getStatusCode();
    assertEquals(expectedResult, responseStatus.toString());
  }

  /**
   * We are testing if we can generate an exception by inputting a null pointer
   * within the String deptCode argument passed into retrieveDepartment. 
   */
  @Test
  public void retrieveDepartmentInvalidInputTest() {
    String expectedResult = "An error has occurred";
    ResponseEntity<?> response = testRouteController.retrieveDepartment(null);
    assertEquals(expectedResult, response.getBody());
  }

  /**
   * We are testing to see if we can input a valid department and course within said department
   * to retrieve a course from the database. We will ensure our course was retrieved appropriately
   * by checking the course's toString method.
   */
  @Test
  public void retrieveValidCourseTest() {
    String expectedResult = "\nInstructor: Gail Kaiser; Location: 501 NWC; Time: 10:10-11:25";
    ResponseEntity<?> response = testRouteController.retrieveCourse("COMS", 4156);
    assertEquals(expectedResult, response.getBody());
  }

  /**
   * We are testing to see if we can input a valid department and invalid course within 
   * said department to retrieve an error message from the database stating that the course 
   * was not found. 
   */
  @Test
  public void retrieveInvalidCourseValidDepartmentTest() {
    String expectedResult = "Course Not Found";
    ResponseEntity<?> response = testRouteController.retrieveCourse("COMS", 4995);
    assertEquals(expectedResult, response.getBody());
  }

  /**
   * We are testing to see if we can input an invalid department but valid course within another 
   * department to retrieve an error message from the database stating that the department was not 
   * found. 
   */
  @Test
  public void retrieveValidCourseInvalidDepartmentTest() {
    String expectedResult = "Department Not Found";
    ResponseEntity<?> response = testRouteController.retrieveCourse("cheese", 4156);
    assertEquals(expectedResult, response.getBody());
  }

  /**
   * We are testing to see if we can cause an exception from within the retriveCourse method by
   * inputting a valid department but a null courseCode. 
   */
  @Test
  public void retrieveCourseInvalidInputTest() {
    String expectedResult = "An error has occurred";
    ResponseEntity<?> response = testRouteController.retrieveCourse("COMS", null);
    assertEquals(expectedResult, response.getBody());
  }

  /**
   * We are testing to see if a valid course within a valid department is at capacity or not. 
   */
  @Test
  public void isCourseFullValidTest() {
    String expectedResult = "200 OK";
    ResponseEntity<?> response = testRouteController.isCourseFull("IEOR", 3404);
    HttpStatusCode responseStatus = response.getStatusCode();
    assertEquals(expectedResult, responseStatus.toString());
  }

  /**
   * Testing to see if we can receive a 404 NOT_FOUND response when we try to retrieve an invalid
   * course from a valid department. 
   */
  @Test
  public void isCourseFullInvalidTest() {
    String expectedResult = "Course Not Found 404 NOT_FOUND";
    ResponseEntity<?> response = testRouteController.isCourseFull("IEOR", 12345);
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();
    assertEquals(expectedResult, responseString);
  }

  /**
   * Testing to see if we cause an exception to the isCourseFull method by inputting a valid
   * courseCode with an invalid department code.
   */
  @Test
  public void isCourseFullInvalidInputTest() {
    String expectedResult = "An error has occurred 500 INTERNAL_SERVER_ERROR";
    ResponseEntity<?> response = testRouteController.isCourseFull("COMS", null);
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();
    assertEquals(expectedResult, responseString);
  }

  /**
   * Testing to see if we can grab the number of compsci majors within our 
   * database. 
   */
  @Test
  public void getMajorCtFromValidDeptTest() {
    String expectedResult = "There are: 2345 majors in the department 200 OK";
    ResponseEntity<?> response = testRouteController.getMajorCtFromDept("ECON");
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();
    assertEquals(expectedResult, responseString);
  }

  /**
   * Testing to see if we can grab the number of philosophy majors within our 
   * database. 
   */
  @Test
  public void getMajorCtFromInvalidDeptTest() {
    String expectedResult = "Department Not Found 404 NOT_FOUND";
    ResponseEntity<?> response = testRouteController.getMajorCtFromDept("PHIL");
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();
    assertEquals(expectedResult, responseString);
  }

  /**
   * Testing to see if we can grab the CS's department's Department Chair.
   */
  @Test
  public void getValidDeptChairTest() {
    String expectedResult =  "Luca Carloni is the department chair. 200 OK";
    ResponseEntity<?> response = testRouteController.identifyDeptChair("COMS");
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();
    assertEquals(expectedResult, responseString);
  }

  /**
   * Testing to see if we can grab the philosophy department's chair. 
   */
  @Test
  public void getInvalidDeptChairTest() {
    String expectedResult =  "Department Not Found 404 NOT_FOUND";
    ResponseEntity<?> response = testRouteController.identifyDeptChair("PHIL");
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();
    assertEquals(expectedResult, responseString);
  }

  /**
   * Testing to see if we can grab the course location for a student taking 
   * 1004 within the compsci department. 
   */
  @Test
  public void getValidCourseLocationTest() {
    String expectedResult =  "417 IAB is where the course is located. 200 OK";
    ResponseEntity<?> response = testRouteController.findCourseLocation("COMS", 1004);
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();
    assertEquals(expectedResult, responseString);
  }

  /**
   * Testing to see if we can grab the course location for a student taking 
   * 4995, which is not within our database, within the compsci department. 
   */
  @Test
  public void getInvalidCourseLocationTest() {
    String expectedResult =  "Course Not Found 404 NOT_FOUND";
    ResponseEntity<?> response = testRouteController.findCourseLocation("COMS", 4995);
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();
    assertEquals(expectedResult, responseString);
  }

  /**
   * Testing to see if we can grab the instructor teachin COMS 1004 within our database. 
   */
  @Test
  public void getValidCourseInstructorTest() {
    String expectedResult =  "Adam Cannon is the instructor for the course. 200 OK";
    ResponseEntity<?> response = testRouteController.findCourseInstructor("COMS", 1004);
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();
    assertEquals(expectedResult, responseString);
  }

  /**
   * Testing to see if we can grab the instructor teachin COMS 4995
   * which is not within our database. 
   */
  @Test
  public void getInvalidCourseInstructorTest() {
    String expectedResult =  "Course Not Found 404 NOT_FOUND";
    ResponseEntity<?> response = testRouteController.findCourseInstructor("COMS", 4995);
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();
    assertEquals(expectedResult, responseString);
  }

  /**
   * Testing to see if we can grab the course time for a course within our database. 
   */
  @Test
  public void getValidCourseTimeTest() {
    String expectedResult =  "The course meets at: 11:40-12:55 200 OK";
    ResponseEntity<?> response = testRouteController.findCourseTime("COMS", 1004);
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();
    assertEquals(expectedResult, responseString);
  }

  /**
   * Testing to see if we can grab the course time for a course NOT within our database. 
   */
  @Test
  public void getInvalidCourseTimeTest() {
    String expectedResult =  "Course Not Found 404 NOT_FOUND";
    ResponseEntity<?> response = testRouteController.findCourseTime("COMS", 4995);
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();
    assertEquals(expectedResult, responseString);
  }

  /**
   * Testing to see if we can add a new student major to an existing department. 
   * We then drop this student major to prevent issues with other tests.
   */
  @Test
  public void addMajorToValidDeptTest() {
    String expectedResult =  "Attribute was updated successfully. 200 OK";
    ResponseEntity<?> response = testRouteController.addMajorToDept("COMS");
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();

    assertEquals(expectedResult, responseString);
    testRouteController.removeMajorFromDept("COMS");
  }

  /**
   * Testing to see if we can add a new student major to an nonexisting department. 
   * 
   */
  @Test
  public void addMajorToInvalidDeptTest() {
    String expectedResult =  "Department Not Found 404 NOT_FOUND";
    ResponseEntity<?> response = testRouteController.addMajorToDept("PHIL");
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();
    assertEquals(expectedResult, responseString);
  }

  /**
   * Testing to see if we can remove an old student major from an existing department. 
   * We then add this student major to prevent issues with other tests.
   */
  @Test
  public void removeMajorFromValidDeptTest() {
    String expectedResult =  "Attribute was updated or is at minimum 200 OK";
    ResponseEntity<?> response = testRouteController.removeMajorFromDept("COMS");
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();

    assertEquals(expectedResult, responseString);
    testRouteController.addMajorToDept("COMS");
  }

  /**
   * Testing to see if we can remove an old student major from a nonexisting department. 
   * 
   */
  @Test
  public void removeMajorFromInvalidDeptTest() {
    String expectedResult =  "Department Not Found 404 NOT_FOUND";
    ResponseEntity<?> response = testRouteController.removeMajorFromDept("PHIL");
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();

    assertEquals(expectedResult, responseString);
  }

  /**
   * Test to ensure ability to dropStudents from a course. 
   */
  @Test
  public void dropStudentValidCourseTest() {
    String expectedResult =  "Student has been dropped. 200 OK";
    ResponseEntity<?> response = testRouteController.dropStudent("COMS", 1004);
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();

    assertEquals(expectedResult, responseString);
  }

  /**
   * Test to ensure ability to dropStudents from a nonexisiting course. 
   */
  @Test
  public void dropStudentInvalidCourseTest() {
    String expectedResult =  "Course Not Found 404 NOT_FOUND";
    ResponseEntity<?> response = testRouteController.dropStudent("COMS", 4995);
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();

    assertEquals(expectedResult, responseString);
  }

  /**
   * Testing to ensure there's no functionality 
   * to drop students from an empty course. Test must return the enrollment count
   * to an integer value to avoid issues with other tests. 
   */
  @Test
  public void dropStudentEmptyCourseTest() {
    String expectedResult =  "Student has not been dropped. 400 BAD_REQUEST";
    testRouteController.setEnrollmentCount("COMS", 1004, 0);
    ResponseEntity<?> response = testRouteController.dropStudent("COMS", 1004);
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();

    assertEquals(expectedResult, responseString);
    testRouteController.setEnrollmentCount("COMS", 1004, 250);
  }

  /**
   * Testing to ensure we can edit the enrollment count of a course.  
   */
  @Test
  public void setValidEnrollmentCountTest() {
    String expectedResult =  "Attribute was updated successfully. 200 OK";
    ResponseEntity<?> response = testRouteController.setEnrollmentCount("COMS", 1004, 100);
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();

    assertEquals(expectedResult, responseString);
  }

  /**
   * Testing to ensure we will not be able to edit enrollment count
   * for a course that doesn't exist. 
   */
  @Test
  public void setInvalidEnrollmentCountTest() {
    String expectedResult =  "Course Not Found 404 NOT_FOUND";
    ResponseEntity<?> response = testRouteController.setEnrollmentCount("COMS", 4995, 1000);
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();

    assertEquals(expectedResult, responseString);
  }

  /**
   * Testing to ensure we can change the course time of a valid course. 
   */
  @Test
  public void changeValidCourseTimeTest() {
    String expectedResult =  "Attribute was updated successfully. 200 OK";
    ResponseEntity<?> response = testRouteController.changeCourseTime("ELEN", 1201, "4:10-6:40");
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();

    assertEquals(expectedResult, responseString);
  }

  /**
   * Testing to ensure we are not able to change the course time of an invalid course. 
   */
  @Test
  public void changeInvalidCourseTimeTest() {
    String expectedResult =  "Course Not Found 404 NOT_FOUND";
    ResponseEntity<?> response = testRouteController.changeCourseTime("ELEN", 4995, "4:10-6:40");
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();

    assertEquals(expectedResult, responseString);
  }

  /**
   * Testing to make sure we can change the instructor of a valid course. 
   */
  @Test
  public void changeValidCourseTeacherTest() {
    String expectedResult =  "Attribute was updated successfully. 200 OK";
    ResponseEntity<?> response = testRouteController.changeCourseTeacher("PSYC", 1001, "Darwin");
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();

    assertEquals(expectedResult, responseString);
  }

  /**
   * Testing to make sure we can change the instructor of an invalid course. 
   */
  @Test
  public void changeInvalidCourseTeacherTest() {
    String expectedResult =  "Course Not Found 404 NOT_FOUND";
    ResponseEntity<?> response = testRouteController.changeCourseTeacher("PSYC", 4995, "Darwin");
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();

    assertEquals(expectedResult, responseString);
  }

  /**
   * Testing to make sure we can change the instructor of an invalid course. 
   */
  @Test
  public void changeCourseInvalidTeacherTest() {
    String expectedResult =  "Teacher name cannot be empty. 403 FORBIDDEN";
    ResponseEntity<?> response = testRouteController.changeCourseTeacher("COMS", 4156, null);
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();

    assertEquals(expectedResult, responseString);
  }

  /**
   * Testing to make sure we can change the course location of a valid course. 
   */
  @Test
  public void changeValidCourseLocationTest() {
    String expectedResult =  "Attribute was updated successfully. 200 OK";
    ResponseEntity<?> response = testRouteController.changeCourseLocation("PSYC", 4493, "417 IAB");
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();

    assertEquals(expectedResult, responseString);
  }
  
  /**
   * Testing to make sure we can change the course location of a valid course. 
   */
  @Test
  public void changeInvalidCourseLocationTest() {
    String expectedResult =  "Course Not Found 404 NOT_FOUND";
    ResponseEntity<?> response = testRouteController.changeCourseLocation("PSYC", 1004, "417 IAB");
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();

    assertEquals(expectedResult, responseString);
  }

  /**
   * Testing to make sure we can actually handle exceptions. 
   */
  @Test
  public void handleExceptionTesting() {
    String expectedResult = "An error has occurred 500 INTERNAL_SERVER_ERROR";
    ResponseEntity<?> response = testRouteController.isCourseFull("COMS", null);
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();

    assertEquals(expectedResult, responseString);
  }

  /**
   * Testing to ensure we can retrieve multiple instance of a valid 
   * course code from our database. 
   */
  @Test
  public void retrieveValidCourseCode() {
    String expectedResult = "Here are all of the courses with course code: 1001\n\n" 
        + "ELEN: Course code not found in this department.\n\n" 
        + "CHEM: Course code not found in this department.\n\n" 
        + "PHYS:\n" + "Instructor: Szabolcs Marka; Location: 301 PUP; Time: 2:40-3:55\n\n" 
        + "PSYC:\n" + "Instructor: Patricia G Lindemann; Location: 501 SCH; Time: 1:10-2:25\n\n" 
        + "COMS: Course code not found in this department.\n\n" 
        + "ECON: Course code not found in this department.\n\n" 
        + "IEOR: Course code not found in this department. 200 OK";
    ResponseEntity<?> response = testRouteController.retrieveCourses(1001);
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();

    assertEquals(expectedResult, responseString);
  }

  /**
   * Testing to ensure we can retrieve multiple instance of an invalid 
   * course code from our database. 
   */
  @Test
  public void retrieveInvalidCourseCode() {
    String expectedResult = "Here are all of the courses with course code: 1000\n\n" 
        + "ELEN: Course code not found in this department.\n\n" 
        + "CHEM: Course code not found in this department.\n\n" 
        + "PHYS: Course code not found in this department.\n\n" 
        + "PSYC: Course code not found in this department.\n\n" 
        + "COMS: Course code not found in this department.\n\n" 
        + "ECON: Course code not found in this department.\n\n" 
        + "IEOR: Course code not found in this department. 404 NOT_FOUND";
    ResponseEntity<?> response = testRouteController.retrieveCourses(1000);
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();

    assertEquals(expectedResult, responseString);
  }

  /**
   * Testing to ensure we get an appropriate response if we pass null into 
   * the course code parameter. 
   */
  @Test
  public void retrieveInvalidInputCourse() {
    String expectedResult = "An error has occurred 500 INTERNAL_SERVER_ERROR";
    ResponseEntity<?> response = testRouteController.retrieveCourses(null);
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();

    assertEquals(expectedResult, responseString);
  }

  /**
   * Testing to ensure we can enroll a student in an open course. 
   */
  @Test
  public void enrollStudentInValidCourse() {
    String expectedResult = "Student has been enrolled in the course. 200 OK";
    ResponseEntity<?> response = testRouteController.enrollStudentInCourse("COMS", 1004);
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();

    assertEquals(expectedResult, responseString);
  }

  /**
   * Testing to ensure we can cannot enroll a student in a course that is full.
   * First try to change the enrollment count to be larger than class capacity. 
   */
  @Test
  public void enrollStudentInFullCourse() {
    
    ResponseEntity<?> filledCourse = 
        testRouteController.setEnrollmentCount("COMS", 4156, 120);
    assertEquals(filledCourse.getStatusCode(), HttpStatus.FORBIDDEN);
    filledCourse = 
        testRouteController.setEnrollmentCount("COMS", 4156, 119);

    assertEquals(filledCourse.getStatusCode(), HttpStatus.OK);
    testRouteController.enrollStudentInCourse("COMS", 4156);

    String expectedResult = "Course is full; unable to enroll student. 403 FORBIDDEN";
    ResponseEntity<?> response = testRouteController.enrollStudentInCourse("COMS", 4156);
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();

    assertEquals(expectedResult, responseString);
  }

  /**
   * Testing to ensure we can cannot enroll a student in a course that is nonexistent. 
   */
  @Test
  public void enrollStudentInInvalidCourse() {
    String expectedResult = "Course Not Found 404 NOT_FOUND";
    ResponseEntity<?> response = testRouteController.enrollStudentInCourse("COMS", 3265);
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();

    assertEquals(expectedResult, responseString);
  }

  /**
   * Testing to ensure we can cannot enroll a student in a department that doesn't exist.
   */
  @Test
  public void enrollStudentInInvalidDept() {
    String expectedResult = "Department Not Found 404 NOT_FOUND";
    ResponseEntity<?> response = testRouteController.enrollStudentInCourse("PHIL", 3265);
    HttpStatusCode responseStatus = response.getStatusCode();
    String responseString = response.getBody() + " " + responseStatus.toString();

    assertEquals(expectedResult, responseString);
  }

  /**
   * Testing to ensure we can handle errors gracefully.
   */
  @Test
  public void enrollStudentInInvalidInput() {
    String expectedResult = "An error has occurred 500 INTERNAL_SERVER_ERROR";
    ResponseEntity<?> responseDept = testRouteController.enrollStudentInCourse(null, 3265);
    HttpStatusCode responseStatusDept = responseDept.getStatusCode();
    String responseStringDept = responseDept.getBody() + " " + responseStatusDept.toString();

    ResponseEntity<?> responseCourse = testRouteController.enrollStudentInCourse("COMS", null);
    HttpStatusCode responseStatusCourse = responseDept.getStatusCode();
    String responseStringCourse = responseCourse.getBody() + " " + responseStatusCourse.toString();

    assertEquals(expectedResult, responseStringDept);
    assertEquals(expectedResult, responseStringCourse);
  }
  
  /**
   * The test department instance used for testing and the courses held within the Department.
   * RouteController instance created for testing the route responses. 
   */
  public static RouteController testRouteController;
  public static Department testDept;
  public static Map<String, Course> courses;
}


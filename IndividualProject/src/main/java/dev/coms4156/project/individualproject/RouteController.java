package dev.coms4156.project.individualproject;

import java.util.Locale;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



/**
 * This class contains all the API routes for the system.
 */
@RestController
public class RouteController {
  
  /**
   * Redirects to the homepage.
   *
   * @return A String containing the name of the html file to be loaded.
   */
  @GetMapping({"/", "/index", "/home"})
  public String index() {
    return
        "Welcome, in order to make an API call direct your browser or Postman to an endpoint "
        + "\n\n This can be done using the following format: \n\n http:127.0.0"
        + ".1:8080/endpoint?arg=value";
  }
  
  /**
   * Returns the details of the specified department.
   *
   * @param deptCode A {@code String} representing the department the user
   *                 wishes to retrieve.
   * @return A {@code ResponseEntity} object containing either the details of
   *     the Department and an HTTP 200 response or, an appropriate message
   *     indicating the proper response.
   */
  @GetMapping(value = "/retrieveDept",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> retrieveDepartment(
      @RequestParam("deptCode") String deptCode) {
    try {
      Map<String, Department> departmentMapping;
      departmentMapping = 
          IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
      
      String localeDeptCode = deptCode.toUpperCase(Locale.ROOT);
      if (departmentMapping.containsKey(localeDeptCode)) {
        return new ResponseEntity<>(
            departmentMapping.get(localeDeptCode).toString(),
            HttpStatus.OK);
      } else {
        return new ResponseEntity<>("Department Not Found", HttpStatus.NOT_FOUND);
      }
      
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Returns the Spring representation of all the courses with the specific
   * course code. If the course code is not found within any departments,
   * the response will be an HTTP 404 NOT_FOUND.
   *
   * @param courseCode A {@code Integer} representing the course code the user 
   *     is looking for. 
   * @return A {@code ResponseEntity} object containing either the details of
   *     the course code within each department and an HTTP 200 response, HTTP 404 response 
   *     if the course was not found in any department or an appropriate message
   *     indicating the proper response.
   */
  @GetMapping(value = "/retrieveCourses", 
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> retrieveCourses(
      @RequestParam("courseCode") Integer courseCode) {
    
    String retrievedCourses = "";
    Integer courseNotFoundCount = 0;
  
    try {
      Map<String, Department> departmentMapping;
      departmentMapping = 
          IndividualProjectApplication.myFileDatabase.getDepartmentMapping();

      // Search through all of the departments first 
      for (Map.Entry<String, Department> departments : departmentMapping.entrySet()) {
        Map<String, Course> coursesMapping;
        coursesMapping =
            departmentMapping.get(departments.getKey()).getCourseSelection();
        
        // check if the course code is in the department
        if (coursesMapping.containsKey(Integer.toString(courseCode))) {
          String courseDetails = coursesMapping.get(Integer.toString(courseCode)).toString();
          retrievedCourses += "\n\n" + departments.getKey() + ":" + courseDetails;
        } else {
          retrievedCourses += "\n\n" + departments.getKey() 
              + ": Course code not found in this department.";
          courseNotFoundCount++;
        }
      }
      // format the string we are going to return 
      String coursesRetrieved = "Here are all of the courses with course code: " 
          + courseCode + retrievedCourses;
      
      if (courseNotFoundCount == departmentMapping.size()) {
        return new ResponseEntity<>(coursesRetrieved, HttpStatus.NOT_FOUND);
      } else {
        return new ResponseEntity<>(coursesRetrieved, HttpStatus.OK);
      }
      
    } catch (Exception e) {
      return handleException(e);
    }
  }


  /**
   * Displays the details of the requested course to the user or displays the proper error
   * message in response to the request.
   *
   * @param deptCode   A {@code String} representing the department the user wishes
   *                   to find the course in.
   * @param courseCode A {@code int} representing the course the user wishes
   *                   to retrieve.
   * @return A {@code ResponseEntity} object containing either the details of the
   *     course and an HTTP 200 response or, an appropriate message indicating the
   *     proper response.
   */
  @GetMapping(value = "/retrieveCourse",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> retrieveCourse(
      @RequestParam("deptCode") String deptCode,
      @RequestParam("courseCode") Integer courseCode) {
    
    try {
      boolean doesDepartmentExists =
          retrieveDepartment(deptCode).getStatusCode() == HttpStatus.OK;
      if (doesDepartmentExists) {
        Map<String, Department> departmentMapping;
        departmentMapping =
            IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
        Map<String, Course> coursesMapping;
        coursesMapping =
            departmentMapping.get(deptCode).getCourseSelection();
        
        if (coursesMapping.containsKey(Integer.toString(courseCode))) {
          return new ResponseEntity<>(
              coursesMapping.get(Integer.toString(courseCode)).toString(),
              HttpStatus.OK);
          
        } else {
          return new ResponseEntity<>("Course Not Found", HttpStatus.NOT_FOUND);

        }
        
      }
      return new ResponseEntity<>("Department Not Found", HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return handleException(e);
    }
  }
  
  /**
   * Displays whether the course has at minimum reached its enrollmentCapacity.
   *
   * @param deptCode   A {@code String} representing the department the user wishes
   *                   to find the course in.
   * @param courseCode A {@code int} representing the course the user wishes
   *                   to retrieve.
   * @return A {@code ResponseEntity} object containing either the requested information
   *     and an HTTP 200 response or, an appropriate message indicating the proper
   *     response.
   */
  @GetMapping(value = "/isCourseFull",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> isCourseFull(
      @RequestParam("deptCode") String deptCode,
      @RequestParam("courseCode") Integer courseCode) {
    
    try {
      ResponseEntity<?> retrievedCourseResponse = retrieveCourse(deptCode, courseCode);
      Integer retrieveCourseStatus = retrievedCourseResponse.getStatusCode().value();
      
      if (retrieveCourseStatus == 200) {
        Map<String, Department> departmentMapping;
        departmentMapping =
            IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
        Map<String, Course> coursesMapping;
        coursesMapping =
            departmentMapping.get(deptCode).getCourseSelection();
        
        Course requestedCourse =
            coursesMapping.get(Integer.toString(courseCode));
        return new ResponseEntity<>(requestedCourse.isCourseFull(),
            HttpStatus.OK);
      } else if (retrieveCourseStatus == 404) {
        return new ResponseEntity<>("Course Not Found", HttpStatus.NOT_FOUND);

      } else {
        throw new NullPointerException("Department and course are not available options.");
      }
      
    } catch (Exception e) {
      return handleException(e);
    }
  }
  
  /**
   * Displays the number of majors in the specified department.
   *
   * @param deptCode A {@code String} representing the department the user wishes
   *                 to find number of majors for.
   * @return A {@code ResponseEntity} object containing either number of majors for the
   *     specified department and an HTTP 200 response or, an appropriate message
   *     indicating the proper response.
   */
  @GetMapping(value = "/getMajorCountFromDept",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> getMajorCtFromDept(
      @RequestParam("deptCode") String deptCode) {
    try {

      boolean doesDepartmentExists =
          retrieveDepartment(deptCode).getStatusCode() == HttpStatus.OK;

      if (doesDepartmentExists) {
        Map<String, Department> departmentMapping;
        departmentMapping =
            IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
        return new ResponseEntity<>(
            "There are: " + departmentMapping.get(deptCode).getNumberOfMajors()
            + " majors in the department", HttpStatus.OK);
      }
      return new ResponseEntity<>("Department Not Found", HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return handleException(e);
    }
  }
  
  /**
   * Displays the department chair for the specified department.
   *
   * @param deptCode A {@code String} representing the department the user wishes
   *                 to find the department chair of.
   * @return A {@code ResponseEntity} object containing either department chair of the
   *     specified department and an HTTP 200 response or, an appropriate message
   *     indicating the proper response.
   */
  @GetMapping(value = "/idDeptChair",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> identifyDeptChair(
      @RequestParam("deptCode") String deptCode) {
    try {
      boolean doesDepartmentExists =
          retrieveDepartment(deptCode).getStatusCode() == HttpStatus.OK;
      if (doesDepartmentExists) {
        Map<String, Department> departmentMapping;
        departmentMapping =
            IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
        return new ResponseEntity<>(
            departmentMapping.get(deptCode).getDepartmentChair() + " is "
            + "the department chair.", HttpStatus.OK);
      }
      return new ResponseEntity<>("Department Not Found", HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return handleException(e);
    }
  }
  
  /**
   * Displays the location for the specified course.
   *
   * @param deptCode   A {@code String} representing the department the user wishes
   *                   to find the course in.
   * @param courseCode A {@code int} representing the course the user wishes
   *                   to find information about.
   * @return A {@code ResponseEntity} object containing either the location of the
   *     course and an HTTP 200 response or, an appropriate message indicating the
   *     proper response.
   */
  @GetMapping(value = "/findCourseLocation",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> findCourseLocation(
      @RequestParam("deptCode") String deptCode,
      @RequestParam("courseCode") Integer courseCode) {
    
    try {
      boolean doesCourseExists;
      doesCourseExists =
          retrieveCourse(deptCode, courseCode).getStatusCode() == HttpStatus.OK;
      
      if (doesCourseExists) {
        Map<String, Department> departmentMapping;
        departmentMapping =
            IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
        Map<String, Course> coursesMapping;
        coursesMapping =
            departmentMapping.get(deptCode).getCourseSelection();
        
        Course requestedCourse =
            coursesMapping.get(Integer.toString(courseCode));
        return new ResponseEntity<>(
            requestedCourse.getCourseLocation() + " is where the course "
            + "is located.", HttpStatus.OK);
      } else {
        return new ResponseEntity<>("Course Not Found", HttpStatus.NOT_FOUND);
      }
      
    } catch (Exception e) {
      return handleException(e);
    }
  }
  
  /**
   * Displays the instructor for the specified course.
   *
   * @param deptCode   A {@code String} representing the department the user wishes
   *                   to find the course in.
   * @param courseCode A {@code int} representing the course the user wishes
   *                   to find information about.
   * @return A {@code ResponseEntity} object containing either the course instructor and
   *     an HTTP 200 response or, an appropriate message indicating the proper
   *     response.
   */
  @GetMapping(value = "/findCourseInstructor",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> findCourseInstructor(
      @RequestParam("deptCode") String deptCode,
      @RequestParam("courseCode") Integer courseCode) {
    
    try {
      boolean doesCourseExists;
      doesCourseExists =
          retrieveCourse(deptCode, courseCode).getStatusCode() == HttpStatus.OK;
      
      if (doesCourseExists) {
        Map<String, Department> departmentMapping;
        departmentMapping =
            IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
        Map<String, Course> coursesMapping;
        coursesMapping =
            departmentMapping.get(deptCode).getCourseSelection();
        
        Course requestedCourse =
            coursesMapping.get(Integer.toString(courseCode));
        return new ResponseEntity<>(
            requestedCourse.getInstructorName() + " is the instructor for"
            + " the course.", HttpStatus.OK);
      } else {
        return new ResponseEntity<>("Course Not Found", HttpStatus.NOT_FOUND);
      }
      
    } catch (Exception e) {
      return handleException(e);
    }
  }
  
  /**
   * Displays the time the course meets at for the specified course.
   *
   * @param deptCode   A {@code String} representing the department the user wishes
   *                   to find the course in.
   * @param courseCode A {@code int} representing the course the user wishes
   *                   to find information about.
   * @return A {@code ResponseEntity} object containing either the details of the
   *     course timeslot and an HTTP 200 response or, an appropriate message
   *     indicating the proper response.
   */
  @GetMapping(value = "/findCourseTime",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> findCourseTime(
      @RequestParam("deptCode") String deptCode,
      @RequestParam("courseCode") Integer courseCode) {
    try {
      boolean doesCourseExists;
      doesCourseExists =
          retrieveCourse(deptCode, courseCode).getStatusCode() == HttpStatus.OK;
      
      if (doesCourseExists) {
        Map<String, Department> departmentMapping;
        departmentMapping =
            IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
        Map<String, Course> coursesMapping;
        coursesMapping =
            departmentMapping.get(deptCode).getCourseSelection();
        
        Course requestedCourse =
            coursesMapping.get(Integer.toString(courseCode));
        return new ResponseEntity<>("The course meets at: " + requestedCourse.getCourseTimeSlot(),
            HttpStatus.OK);
      } else {
        return new ResponseEntity<>("Course Not Found", HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Returns the appropriate response given user attempted to enroll a student
   * in the department and course code provided.
   *
   * @param courseCode A {@code Integer} representing the course code the user 
   *     is looking for. 
   * @param deptCode A {@code String} representing the department the user is
   *     is looking to enroll a student into.  
   * @return A {@code ResponseEntity} object containing either the details of
   *     the sucessful modification and an HTTP 200 response, HTTP 404 response 
   *     if the course and/or department couldn't be found or an appropriate message
   *     indicating the proper response.
   */
  @PatchMapping(value = "/enrollStudentInCourse", 
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> enrollStudentInCourse(
      @RequestParam("deptCode") String deptCode,
      @RequestParam("courseCode") Integer courseCode) {

    try {
      String localeDeptCode = deptCode.toUpperCase(Locale.ROOT);

      boolean doesDepartmentExists =
          retrieveDepartment(localeDeptCode).getStatusCode() == HttpStatus.OK;
      if (doesDepartmentExists) {
        Map<String, Department> departmentMapping;
        departmentMapping =
            IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
        Map<String, Course> coursesMapping;
        coursesMapping =
            departmentMapping.get(localeDeptCode).getCourseSelection();
        
        boolean doesCourseExists = 
            coursesMapping.containsKey(Integer.toString(courseCode));

        Course requestedCourse = coursesMapping.get(Integer.toString(courseCode));
        
        // if the course exists and is full, do not enroll the student 
        if (doesCourseExists && requestedCourse.isCourseFull()) {

          return new ResponseEntity<>("Course is full; unable to enroll student.", 
                                      HttpStatus.FORBIDDEN);
          
          // if the course exists and has an open seat, enroll the student
        } 
        if (doesCourseExists && !requestedCourse.isCourseFull()) {
          requestedCourse.enrollStudent();
          return new ResponseEntity<>("Student has been enrolled in the course.", 
                                      HttpStatus.OK);

        } 
        if (!doesCourseExists) {
          return new ResponseEntity<>("Course Not Found", HttpStatus.NOT_FOUND);
        }
        
      }
      return new ResponseEntity<>("Department Not Found", HttpStatus.NOT_FOUND);

    } catch (Exception e) {
      return handleException(e);
    }
  }
  
  
  /**
   * Attempts to add a student to the specified department.
   *
   * @param deptCode A {@code String} representing the department.
   * @return A {@code ResponseEntity} object containing an HTTP 200
   *     response with an appropriate message or the proper status
   *     code in tune with what has happened.
   */
  @PatchMapping(value = "/addMajorToDept",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> addMajorToDept(
      @RequestParam("deptCode") String deptCode) {
    try {
      boolean doesDepartmentExists =
          retrieveDepartment(deptCode).getStatusCode() == HttpStatus.OK;
      if (doesDepartmentExists) {
        Map<String, Department> departmentMapping;
        departmentMapping =
            IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
        
        Department specifiedDept = departmentMapping.get(deptCode);
        specifiedDept.addPersonToMajor();
        return new ResponseEntity<>("Attribute was updated successfully.",
            HttpStatus.OK);
      }
      return new ResponseEntity<>("Department Not Found", HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return handleException(e);
    }
  }
  
  /**
   * Attempts to remove a student from the specified department.
   *
   * @param deptCode A {@code String} representing the department.
   * @return A {@code ResponseEntity} object containing an HTTP 200
   *     response with an appropriate message or the proper status
   *     code in tune with what has happened.
   */
  @PatchMapping(value = "/removeMajorFromDept",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> removeMajorFromDept(
      @RequestParam("deptCode") String deptCode) {
    try {
      boolean doesDepartmentExists =
          retrieveDepartment(deptCode).getStatusCode() == HttpStatus.OK;
      if (doesDepartmentExists) {
        Map<String, Department> departmentMapping;
        departmentMapping =
            IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
        
        Department specifiedDept = departmentMapping.get(deptCode);
        specifiedDept.dropPersonFromMajor();
        return new ResponseEntity<>("Attribute was updated or is at minimum",
            HttpStatus.OK);
      }
      return new ResponseEntity<>("Department Not Found", HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return handleException(e);
    }
  }
  
  /**
   * Attempts to drop a student from the specified course.
   *
   * @param deptCode   A {@code String} representing the department.
   * @param courseCode A {@code int} representing the course within the department.
   * @return A {@code ResponseEntity} object containing an HTTP 200
   *     response with an appropriate message or the proper status
   *     code in tune with what has happened.
   */
  @PatchMapping(value = "/dropStudentFromCourse",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> dropStudent(
      @RequestParam("deptCode") String deptCode,
      @RequestParam("courseCode") Integer courseCode) {
    
    try {
      boolean doesCourseExists;
      doesCourseExists =
          retrieveCourse(deptCode, courseCode).getStatusCode() == HttpStatus.OK;
      
      if (doesCourseExists) {
        Map<String, Department> departmentMapping;
        departmentMapping =
            IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
        Map<String, Course> coursesMapping;
        coursesMapping =
            departmentMapping.get(deptCode).getCourseSelection();
        
        Course requestedCourse =
            coursesMapping.get(Integer.toString(courseCode));
        boolean isStudentDropped = requestedCourse.dropStudent();
        
        if (isStudentDropped) {
          return new ResponseEntity<>("Student has been dropped.",
              HttpStatus.OK);
        } else {
          return new ResponseEntity<>("Student has not been dropped.",
              HttpStatus.BAD_REQUEST);
        }
      } else {
        return new ResponseEntity<>("Course Not Found", HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      return handleException(e);
    }
  }
  
  /**
   * Attempts to set the enrollment count for a specific course.
   *
   * @param deptCode   A {@code String} representing the department.
   * @param courseCode A {@code int} representing the course within the department.
   * @param count      A {@code int} representing the number of students within the course.
   * @return A {@code ResponseEntity} object containing an HTTP 200 response with an appropriate
   *     message or the proper status code in tune with what has happened.
   */
  
  @PatchMapping(value = "/setEnrollmentCount",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> setEnrollmentCount(
      @RequestParam("deptCode") String deptCode,
      @RequestParam("courseCode") Integer courseCode,
      @RequestParam("count") int count) {
    
    try {
      boolean doesCourseExists;
      doesCourseExists =
          retrieveCourse(deptCode, courseCode).getStatusCode() == HttpStatus.OK;
      
      if (doesCourseExists) {
        Map<String, Department> departmentMapping;
        departmentMapping =
            IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
        Map<String, Course> coursesMapping;
        coursesMapping =
            departmentMapping.get(deptCode).getCourseSelection();
        
        Course requestedCourse =
            coursesMapping.get(Integer.toString(courseCode));

        Boolean changedStudentCount = requestedCourse.setEnrolledStudentCount(count);
        
        if (changedStudentCount) {
          return new ResponseEntity<>("Attribute was updated successfully.",
              HttpStatus.OK);
        } else {
          return new ResponseEntity<>("Attribute was not updated successfully.",
              HttpStatus.FORBIDDEN);
        }
      } else {
        return new ResponseEntity<>("Course Not Found", HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      return handleException(e);
    }
  }
  
  /**
   * Endpoint for changing the time of a course.
   * This method handles PATCH requests to change the time of a course identified by
   * department code and course code.If the course exists, its time is updated to the provided time.
   *
   * @param deptCode   the code of the department containing the course
   * @param courseCode the code of the course to change the time for
   * @param time       the new time for the course
   * @return a ResponseEntity with a success message if the operation is
   *     successful, or an error message if the course is not found
   */
  @PatchMapping(value = "/changeCourseTime",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> changeCourseTime(
      @RequestParam("deptCode") String deptCode,
      @RequestParam("courseCode") Integer courseCode,
      @RequestParam("time") String time) {

    if (time == null || time.isBlank()) {
      return new ResponseEntity<>("Time cannot be empty", HttpStatus.FORBIDDEN);
    }
    
    try {
      boolean doesCourseExists;
      doesCourseExists =
          retrieveCourse(deptCode, courseCode).getStatusCode() == HttpStatus.OK;
      
      if (doesCourseExists) {
        Map<String, Department> departmentMapping;
        departmentMapping =
            IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
        Map<String, Course> coursesMapping;
        coursesMapping =
            departmentMapping.get(deptCode).getCourseSelection();
        
        Course requestedCourse = 
            coursesMapping.get(Integer.toString(courseCode));
        requestedCourse.reassignTime(time);
        return new ResponseEntity<>("Attribute was updated successfully.",
            HttpStatus.OK);
      } else {
        return new ResponseEntity<>("Course Not Found", HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      return handleException(e);
    }
  }
  
  /**
   * Endpoint for changing the instructor of a course.
   * This method handles PATCH requests to change the instructor of a course identified by
   * department code and course code. If the course exists, its instructor is updated to the
   * provided instructor.
   *
   * @param deptCode   the code of the department containing the course
   * @param courseCode the code of the course to change the instructor for
   * @param teacher    the new instructor for the course
   * @return a ResponseEntity with a success message if the operation is
   *     successful, or an error message if the course is not found
   */
  @PatchMapping(value = "/changeCourseTeacher",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> changeCourseTeacher(
      @RequestParam("deptCode") String deptCode,
      @RequestParam("courseCode") Integer courseCode,
      @RequestParam("teacher") String teacher) {
    
    // check for invalid input 
    if (teacher == null || teacher.isBlank()) {
      return new ResponseEntity<>("Teacher name cannot be empty.", HttpStatus.FORBIDDEN);
    }
    try {
      boolean doesCourseExists;
      doesCourseExists =
          retrieveCourse(deptCode, courseCode).getStatusCode() == HttpStatus.OK;
      
      if (doesCourseExists) {
        Map<String, Department> departmentMapping;
        departmentMapping =
            IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
        Map<String, Course> coursesMapping;
        coursesMapping =
            departmentMapping.get(deptCode).getCourseSelection();
        
        Course requestedCourse =
            coursesMapping.get(Integer.toString(courseCode));
        requestedCourse.reassignInstructor(teacher);
        return new ResponseEntity<>("Attribute was updated successfully.",
            HttpStatus.OK);
      } else {
        return new ResponseEntity<>("Course Not Found", HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      return handleException(e);
    }
  }
  
  /**
   * Attempts to change the location of a already logged course.
   *
   * @param deptCode   A {@code String} representing the department.
   * @param courseCode A {@code int} representing the course within the department.
   * @param location   A {@code String} representing the location in which the course occurs.
   * @return A {@code ResponseEntity} object containing an HTTP 200 response with an appropriate
   *     message or the proper status code in tune with what has happened.
   */
  @PatchMapping(value = "/changeCourseLocation",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> changeCourseLocation(
      @RequestParam("deptCode") String deptCode,
      @RequestParam("courseCode") Integer courseCode,
      @RequestParam("location") String location) {
    
    if (location == null || location.isBlank()) {
      return new ResponseEntity<>("Location cannot be empty.", HttpStatus.FORBIDDEN);
    }
    try {
      boolean doesCourseExists;
      doesCourseExists =
          retrieveCourse(deptCode, courseCode).getStatusCode() == HttpStatus.OK;
      
      if (doesCourseExists) {
        Map<String, Department> departmentMapping;
        departmentMapping =
            IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
        Map<String, Course> coursesMapping;
        coursesMapping =
            departmentMapping.get(deptCode).getCourseSelection();
        
        Course requestedCourse =
            coursesMapping.get(Integer.toString(courseCode));
        requestedCourse.reassignLocation(location);
        return new ResponseEntity<>("Attribute was updated successfully.",
            HttpStatus.OK);
      } else {
        return new ResponseEntity<>("Course Not Found", HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      return handleException(e);
    }
  }
  
  private ResponseEntity<?> handleException(Exception e) {
    e.printStackTrace();
    return new ResponseEntity<>("An error has occurred", HttpStatus.INTERNAL_SERVER_ERROR);
  }
  
  
}
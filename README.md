# COMS-4156-Project
This is the private GitHub repository for the individual miniproject associated with 
COMS 4156 Advanced Software Engineering. All work is individually by Diego Rivas
Lazala (UNI: dwr2118).

# Demo Video
Below you'll get a glimpse into the main deliverable for I3 of this miniproject here: **https://youtu.be/ukxdrQx_Pb0**.
It essentially shows the service launched on a Google Cloud App Engine instance and endpoint testing
using Thunder Client from within VS Code. If there's any issues viewing the video, please let me know through email:
dwr2118@columbia.edu. 

## Building and Running a Local Instance
In order to build and use our service you must install the following (This guide assumes MacOS but the Maven README has instructions for both Windows and Mac):

1. Maven 3.9.5: https://maven.apache.org/download.cgi Download and follow the installation instructions for MacOS
2. JDK 17: This project used JDK 17 for development so that is what we recommend you use: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
3. VSCode: I recommend using VS Code because of the integrated endpoint testing through extensions like Thunder Client: https://code.visualstudio.com/download 
4. You'll need some exposure to working with MacOS terminal and git (used for version control). After
you've familiarized yourself with these technologies, clone the GitHub repo and run 
<code>git clone https://github.com/dwr2118/privrepo.git</code>. You would need to be added as a collaborator
for this repo as it is private so email: dwr2118@columbia.edu if you experience any issues getting 
this repo cloned down to your local. 
5. To build and run the project using Maven: <code>mvn spring-boot:run -Dspring-boot.run.argu
ments="setup"</code> and then you can run the tests via the test files described below. After you've run this command, you should expect to see "".
6. To run the style checker run, <code>mvn checkstyle:check</code> or <code>mvn checkstyle:checkstyle</code> if you wish to generate the report. 
7. If you'd like to run a static bug checker, you can use PMD and run this following command from **within the IndividualProject folder**
to check for bugs without spinning up the service: <code> pmd check -d ./src -R rulesets/java/quickstart.xml -f text </code>. This command allows for the use of the quickstart ruleset within a MacOS environment. 

## Running Tests
My unit tests are located under the directory 'src/test'. To run my project's tests, you'll need
Test Runner for Java from the VS Code Marketplace. You'll then find a potion bottle somewhere on your VS Code
window and you'll hit the Run Tests button as demonstrated in the screenshot below. 
![image](https://github.com/user-attachments/assets/14f000bd-ae31-4fcf-acfe-ae9bace7a24e)

From there, you can left-click any of the test classes present in the src/test directory and click the 
run test button after hovering over the test method you'd like to run. 
![image](https://github.com/user-attachments/assets/fd6f49eb-72b3-4f89-89c1-42633cd88b95)

## Endpoints
Below are all the descriptions of the endpoints that my service provides; including inputs & outputs. 
If you make a crappy request, you'll automatically receive a HTTP 400 Bad Request sent by the server. 

#### GET /, /index, /home
* Redirects to the homepage.
* Returns: A String containing the name of the html file to be loaded.

#### GET /retrieveDepartment
* Returns the details of the specified department.
* Parameters: 
  - `deptCode`: A String representing the department the user wishes to retrieve.
* Returns: A ResponseEntity object containing either the details of the Department and an HTTP 200 response.
If the department the user has requested is not found, the returned response includes a HTTP 404 NOT_FOUND response
with a body message of "Department Not Found". An appropriate message indicating the proper response occurs for errors. 

#### GET /retrieveCourses
* Returns the Spring representation of all the courses with the specific course code. 
* Parameters: 
  - `courseCode`: An Integer representing the course code the user is looking for.
* Returns: A ResponseEntity object containing either the details of the course code within each department and an HTTP 200 response. If the course code is not found within any departments, the response will be an HTTP 404 NOT_FOUND.
with a body message of consisting of the different departments where the course is NOT found. An appropriate message indicating the proper response occurs for errors. 

#### GET /retrieveCourse
* Displays the details of the requested course to the user or displays the proper error message in response to the request.
* Parameters: 
  - `deptCode`: A String representing the department the user wishes to find the course in.
  - `courseCode`: An Integer representing the course the user wishes to retrieve.
* Returns: A ResponseEntity object containing either the details of the course and an HTTP 200. 
If the course code is not found within any departments, the response will be an HTTP 404 NOT_FOUND with 
a body of "Course Not Found" or "Department Not Found" if the deptCode entered is not mapped to an
existing department. An appropriate message indicating the proper response occurs for errors. 

#### GET /isCourseFull
* Displays whether the course has at minimum reached its enrollment capacity.
* Parameters: 
  - `deptCode`: A String representing the department the user wishes to find the course in.
  - `courseCode`: An Integer representing the course the user wishes to retrieve.
* Returns: A ResponseEntity object containing either the requested information and an HTTP 200. 
HTTP 404 NOT_FOUND & "Course Not Found" are sent in the case that the courseCode is not mapped 
to a valid course. HTTP 404 NOT_FOUND & "Department and course are not available options." 
is sent in the case that the deptCode and the course Code are not valid. An appropriate message indicating the proper response occurs for errors. 

#### GET /getMajorCountFromDept
* Displays the number of majors in the specified department.
* Parameters: 
  - `deptCode`: A String representing the department the user wishes to find the number of majors for.
* Returns: A ResponseEntity object containing either the number of majors for the specified department and an HTTP 200 response. An appropriate message indicating the proper response occurs for errors. 

#### GET /idDeptChair
* Displays the department chair for the specified department.
* Parameters: 
  - `deptCode`: A String representing the department the user wishes to find the department chair of.
* Returns: A ResponseEntity object containing either the department chair of the specified department and an HTTP 200 response or an appropriate message indicating the proper response. The returned response includes a HTTP 404 NOT_FOUND response with a body message of "Department Not Found" for an invalid department code entered. 
An appropriate message indicating the proper response occurs for errors. 


#### GET /findCourseLocation
* Displays the location for the specified course.
* Parameters: 
  - `deptCode`: A String representing the department the user wishes to find the course in.
  - `courseCode`: An Integer representing the course the user wishes to find information about.
* Returns: A ResponseEntity object containing either the location of the course and an HTTP 200 response or an appropriate message indicating the proper response. The returned response includes a HTTP 404 NOT_FOUND response with a body message of "Course Not Found" for an invalid course code entered. An appropriate message indicating the proper response occurs for errors. 

#### GET /findCourseInstructor
* Displays the instructor for the specified course.
* Parameters: 
  - `deptCode`: A String representing the department the user wishes to find the course in.
  - `courseCode`: An Integer representing the course the user wishes to find information about.
* Returns: A ResponseEntity object containing either the course instructor and an HTTP 200 response. The returned response includes a HTTP 404 NOT_FOUND response with a body message of "Course Not Found" for an invalid course code entered. An appropriate message indicating the proper response occurs for errors. 

#### GET /findCourseTime
* Displays the time the course meets for the specified course.
* Parameters: 
  - `deptCode`: A String representing the department the user wishes to find the course in.
  - `courseCode`: An Integer representing the course the user wishes to find information about.
* Returns: A ResponseEntity object containing either the details of the course time slot and an HTTP 200. The returned response includes a HTTP 404 NOT_FOUND response with a body message of "Course Not Found" for an invalid course code entered.  An appropriate message indicating the proper response occurs for errors. 

#### PATCH /enrollStudentInCourse
* Returns the appropriate response given a user attempting to enroll a student in the department and course code provided.
* Parameters: 
  - `deptCode`: A String representing the department the user is looking to enroll a student into.
  - `courseCode`: An Integer representing the course code the user is looking for.
* Returns: A ResponseEntity object containing either the details of the successful modification and an HTTP 200 response, an HTTP 404 response with body of "Course Not Found" for an invalid course,
 HTTP 404 request with body of "Department Not Found" for an invalid course, HTTP 403 FORBIDDEN if 
 the course is full with a body of "Course is full; unable to enroll student." and HTTP 200 OK 
 if the student was successfully enrolled. Successful addition of student returns body messsage of:
 "Student has been enrolled in the course." An appropriate message indicating the proper response occurs for errors. 

#### PATCH /addMajorToDept
* Attempts to add a student to the specified department.
* Parameters: 
  - `deptCode`: A String representing the department.
* Returns: A ResponseEntity object containing an HTTP 200 response with an appropriate message.
The returned response includes a HTTP 404 NOT_FOUND response with a body message of "Department Not Found" for an invalid department code entered. An appropriate message indicating the proper response occurs for errors. 

#### PATCH /removeMajorFromDept
* Attempts to remove a student from the specified department.
* Parameters: 
  - `deptCode`: A String representing the department.
* Returns: A ResponseEntity object containing an HTTP 200 response with an appropriate message. 
The returned response includes a HTTP 404 NOT_FOUND response with a body message of "Department Not Found" for an invalid department code entered. An appropriate message indicating the proper response occurs for errors. 

#### PATCH /dropStudentFromCourse
* Attempts to drop a student from the specified course.
* Parameters: 
  - `deptCode`: A String representing the department.
  - `courseCode`: An Integer representing the course within the department.
* Returns: A ResponseEntity object containing an HTTP 200 response with an appropriate message or the proper status code in tune with what has happened. The returned response includes a HTTP 404 NOT_FOUND response with a body message of "Course Not Found" for an invalid course code entered. "Student has been dropped." & HTTP 200 are for valid
decreases in students for a course and "Student has not been dropped." are for invalid decreases in
students within a course. An appropriate message indicating the proper response occurs for errors. 

#### PATCH /setEnrollmentCount
* Attempts to set the enrollment count for a specific course.
* Parameters: 
  - `deptCode`: A String representing the department.
  - `courseCode`: An Integer representing the course within the department.
  - `count`: An Integer representing the number of students within the course.
* Returns: A ResponseEntity object containing an HTTP 200 response with an appropriate message. The returned response includes a HTTP 404 NOT_FOUND response with a body message of "Course Not Found" for an invalid course code entered. 
If the enrollment count was not set properly, HTTP 403 FORBIDDEN is returned and the caller 
is told "Attribute was not updated successfully." An appropriate message indicating the proper response occurs for errors. 

#### PATCH /changeCourseTime
* Endpoint for changing the time of a course. This method handles PATCH requests to change the time of a course identified by department code and course code. If the course exists, its time is updated to the provided time.
* Parameters: 
  - `deptCode`: A String representing the department containing the course.
  - `courseCode`: An Integer representing the course to change the time for.
  - `time`: A String representing the new time for the course.
* Returns: A ResponseEntity with a success message & HTTP 200 if the operation is successful. If the course
is not found, HTTP 404 NOT_FOUND is returned with a message stating "Course Not Found". An appropriate message indicating the proper response occurs for errors. 

#### PATCH /changeCourseTeacher
* Endpoint for changing the instructor of a course. This method handles PATCH requests to change the instructor of a course identified by department code and course code. If the course exists, its instructor is updated to the provided instructor.
* Parameters: 
  - `deptCode`: A String representing the department containing the course.
  - `courseCode`: An Integer representing the course to change the instructor for.
  - `teacher`: A String representing the new instructor for the course.
* Returns: A ResponseEntity with a success message if the operation is successful. If the course is not found, HTTP 404 NOT_FOUND is returned with a message stating "Course Not Found". An appropriate message indicating the proper response occurs for errors. 

#### PATCH /changeCourseLocation
* Attempts to change the location of an already logged course.
* Parameters: 
  - `deptCode`: A String representing the department.
  - `courseCode`: An Integer representing the course within the department.
  - `location`: A String representing the location in which the course occurs.
* Returns: A ResponseEntity object containing an HTTP 200 response with an appropriate message.
If the course is not found, HTTP 404 NOT_FOUND is returned with a message stating "Course Not Found". 
An appropriate message indicating the proper response occurs for errors. 


## Branch Coverage Reporting 
I used JaCoCo to perform branch analysis and to get a better view into the branches that need to get covered within the codebase. Below is a screenshot of the most recent output. 
<img width="1126" alt="image" src="https://github.com/user-attachments/assets/5980128b-64e5-4ad2-8bb1-e2792f75aef5">

## Static Code Analysis
The most recent output given to me by the PMD static bug analysis can be see below. 
![image](https://github.com/user-attachments/assets/d856ffe4-5267-4602-8317-d956bb5289ad)


## Continuous Integration
This repository using GitHub Actions to perform continuous integration and the configs for this workflow are located within the maven.yml file in <code>.github/workflows</code>. These will trigger whenever a new commit to main was conducted and/or there has been a pull request into main created. It just runs <code>mvn test</code> to essentially run the tests described above. Ideally, I would have the runner create a brand new instance of the server for testing but I haven't taken the time out to explore the configurations. 

## Tools used 🧰
This section includes notes on tools and technologies used in building this project, as well as any additional details if applicable.
 
* Maven Package Manager
* GitHub Actions CI
  * This is enabled via the "Actions" tab on GitHub.
  * Currently, this just runs a Maven build to make sure the code builds on branch 'main' whenever there is a push to main or a pull request into main. 
* Checkstyle
  * I use Checkstyle for code reporting. Checkstyle is not part of my current CI pipeline.
  * For running Checkstyle manually, use the commands listed in Building and Running a Local Instance portion of this README. 
* PMD
  * I used PMD to do static analysis of my Java code.
* JUnit
  * JUnit tests get run automatically as part of the CI pipeline.
* JaCoCo
  * I used JaCoCo for generating code coverage reports.
* Thunder Client
  * Thunder Client is used to test our APIs. 




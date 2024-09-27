# COMS-4156-Project
This is the private GitHub repository for the individual miniproject associated with 
COMS 4156 Advanced Software Engineering. All work is individually by Diego Rivas
Lazala (UNI: dwr2118).

video: https://youtu.be/ukxdrQx_Pb0

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

<!-- Our endpoints are listed below in the "Endpoints" section, with brief descriptions of their parameters. For in-depth examples and system-level
tests of them, see the section "Postman Test Documentation" below. -->

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

TODO: put all of the route controller methods/endpoints on here & describe their inputs & outputs 

## Branch Coverage Reporting 
I used JaCoCo to perform branch analysis and to get a better view into the branches that need to get covered within the codebase. Below is a screenshot of the most recent output. 
<img width="1126" alt="image" src="https://github.com/user-attachments/assets/5980128b-64e5-4ad2-8bb1-e2792f75aef5">

## Static Code Analysis
The most recent output given to me by the PMD static bug analysis can be see below. 
![image](https://github.com/user-attachments/assets/d856ffe4-5267-4602-8317-d956bb5289ad)


## Continuous Integration Report
This repository using GitHub Actions to perform continous integration and the configs for this workflow are located within the maven.yml file in <code>.github/workflows</code>. These will trigger whenever a new commit to main was conducted and/or there has been a pull request into main created. It just runs <code>mvn test</code> to essentially run the tests described above. Ideally, I would have the runner create a brand new instance of the server for testing but I haven't taken the time out to explore the configurations. 

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




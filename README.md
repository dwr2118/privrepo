# COMS-4156-Project
This is the private GitHub repository for the individual miniproject associated with 
COMS 4156 Advanced Software Engineering. All work is individually by Diego Rivas
Lazala (UNI: dwr2118).

video: https://youtu.be/ukxdrQx_Pb0

## Building and Running a Local Instance
In order to build and use our service you must install the following (This guide assumes MacOS but the Maven README has instructions for both Windows and Mac):

1. Maven 3.9.5: https://maven.apache.org/download.cgi Download and follow the installation instructions for MacOS
2. JDK 17: This project used JDK 17 for development so that is what we recommend you use: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
3. VSCode: I recommende using VS Code because of the integrated endpoint testing through extensions like ThunderClient: https://code.visualstudio.com/download 
4. You'll need some exposure to working with MacOS terminal and git (used for version control). After
you've familiarized yourself with these technologies, clone the GitHub repo and run 
<code>git clone https://github.com/dwr2118/privrepo.git</code>. You would need to be added as a collaborator
for this repo as it is private so email: dwr2118@columbia.edu if you experience any issues getting 
this repo cloned down to your local. 
5. To build and run the project using Maven: <code>mvn spring-boot:run -Dspring-boot.run.argu
ments="setup"</code> and then you can run the tests via the test files described below. After you've run this command, you should expect to see "".
6. To run the style checker run, <code>mvn checkstyle:check</code> or <code>mvn checkstyle:checkstyle</code> if you wish to generate the report. 

<!-- Our endpoints are listed below in the "Endpoints" section, with brief descriptions of their parameters. For in-depth examples and system-level
tests of them, see the section "Postman Test Documentation" below. -->

Please follow the IA grading rubric while grading my unit tests, bug slaying skills and style B) 

I utilized the quickstart ruleset defined in rulesets/java/quickstart.xml
and, to run the bug finder on macOS, use this command: 

```
pmd check -d ./src -R rulesets/java/quickstart.xml -f text
```


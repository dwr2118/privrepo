package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * This is just testing if we can create a course object and return expected results from each
 * method.
 */
@SpringBootTest
@ContextConfiguration
public class ProjectAppUnitTests {
  
  /**
   * We are setting up the application for testing by creating an instance of it.
   */
  @BeforeAll
  public static void setupAppForTesting() {
    testApp = new IndividualProjectApplication();
  }

  /**
   * We are making sure to put back the ./data.txt file exactly as we have 
   * found it; which is needed for testing of other classes. 
   */
  @AfterAll
  public static void cleanUp() {
    String[] sampleRunTimeArgs = {"setup"};
    testApp.run(sampleRunTimeArgs);
    testApp.myFileDatabase.saveContentsToFile();
  }

  /**
   * Testing if the database setup was conducted properly which is 
   * indicated by a created ./data.txt file and an accessible course. 
   */
  @Test
  public void testRunAppWithSetup() {
    File file = new File(realDBPath);

    if (file.exists()) {
      file.delete();
    }
    String[] sampleSetupArgs = {"setup"};
    testApp.run(sampleSetupArgs);
    
    MyFileDatabase testDatabase = testApp.myFileDatabase;
    Map<String, Department> testMapping = testDatabase.getDepartmentMapping();
    Department compSci = testMapping.get("COMS");
    Map<String, Course> comsCourses = compSci.getCourseSelection();
    Course coms1004 = comsCourses.get("1004");
    String expectedResult = "Adam Cannon";
    assertEquals(expectedResult, coms1004.getInstructorName());
  }

  /**
   * Testing if can still access the database after its been setup.
   */
  @Test
  public void testRunAppWithoutSetup() {
    String[] sampleNonSetupArgs = {"already set"};
    testApp.run(sampleNonSetupArgs);
    MyFileDatabase testDatabase = testApp.myFileDatabase;
    assertNotNull(testDatabase);
  }

  /**
   * Testing if we can override the database with a null one. We then 
   * return the file database to its original value to prevent testing issues
   * in the future. 
   */
  @Test
  public void testOverridingDatabase() {
    MyFileDatabase temp = testApp.myFileDatabase;
    testApp.overrideDatabase(null);
    assertNull(testApp.myFileDatabase);
    testApp.overrideDatabase(temp);
  }

  /**
   * Testing if we can reset the data file after setting our mapping to be null.
   */
  @Test
  public void testDataFileReset() {
    MyFileDatabase testDatabase = testApp.myFileDatabase;
    testDatabase.setMapping(null);
    testApp.resetDataFile();
    assertNotNull(testDatabase.getDepartmentMapping());
  }

  /**
   * Testing if the database was saved to ./data.txt before termination.
   * We can check this by seeing if the file exists. 
   */
  @Test
  public void testSaveUponTermination() {
    File file = new File("./data.txt");
    testApp.saveData = false;
    testApp.onTermination();
    assertTrue(file.exists());
  }
   
  /**
   * The individual project application instance used for testing.
   */
  public static IndividualProjectApplication testApp;
  public static String realDBPath = "./data.txt";
}


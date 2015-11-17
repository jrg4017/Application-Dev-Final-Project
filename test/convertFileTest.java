/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author Chevy
 */
public class convertFileTest {
    
    public convertFileTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        System.out.println("@Before Class");
    }
    
    @AfterClass
    public static void tearDownClass() {
        System.out.println("@After Class");
    }
    
    @Before
    public void setUp() {
        System.out.println("@Before Test");
    }
    
    @After
    public void tearDown() {
        System.out.println("@After Test");
    }

    /**
     * Test to convert .edg or .xml file to DDL (.sql file)
     */
    @Test
    public void testConvert() {
        System.out.println("Convert Files Test");
        boolean convertCheck1 = convertFile("testEdge.edg");
        boolean convertCheck2 = convertFile("testXML.xml");
        boolean convertCheck3 = convertFile("testFile.txt");
        
        // Test Case #1 - Test if it can convert .edg to DDL 
        System.out.println("Test Case #1");
        assertTrue(convertCheck1);
        if(convertCheck1)
        {
            System.out.println("Test Success!");
        }
        else
        {
            System.out.println("Test Fail!");
        }
        
        // Test Case #2 - Test if it can convert .xml to DDL
        System.out.println("Test Case #2");
        assertTrue(convertCheck2);
        if(convertCheck1)
        {
            System.out.println("Test Success!");
        }
        else
        {
            System.out.println("Test Fail!");
        }
        
        // Test Case #3 - Test if input is .xml or .edg then convert to DDL
        System.out.println("Test Case #3");
        assertFalse(convertCheck3);
        if(convertCheck3)
        {
            System.out.println("Test Fail!");
        }
        else
        {
            System.out.println("Test Success!");
        }
    }

    private boolean convertFile(String filename){
        BufferedReader readFile = null;
        FileOutputStream fileOutput = null;
        ObjectOutputStream writeData = null;
        try
        {
            switch (filename.substring(filename.length() - 4, filename.length())) 
            {
                case ".edg":
                {
                    readFile = new BufferedReader(new FileReader("./test/" + filename));
                    fileOutput = new FileOutputStream(new File("./test/convertedEDG.sql"));
                    writeData = new ObjectOutputStream(fileOutput);
                    String line = readFile.readLine();
                    while((line) != null)
                    {
                        writeData.writeUTF(line);
                        line = readFile.readLine();
                    }
                    writeData.close();
                    readFile.close();
                    return true;
                }
                case ".xml":
                {
                    readFile = new BufferedReader(new FileReader("./test/" + filename));
                    fileOutput = new FileOutputStream(new File("./test/convertedXML.sql"));
                    writeData = new ObjectOutputStream(fileOutput);
                    String line = readFile.readLine();
                    while((line) != null)
                    {
                        writeData.writeUTF(line);
                        line = readFile.readLine();;
                    }
                    writeData.close();
                    readFile.close();
                    return true;
                }
                default:
                    return false;
            }
        }
        catch(FileNotFoundException fe)
        {
            System.out.println("File not found.");
            return false;
        }
        catch(IOException ioe)
        {
            System.out.println("Error with the file");
            return false;
        }
    }
}

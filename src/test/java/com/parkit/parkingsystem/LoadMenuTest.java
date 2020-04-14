package com.parkit.parkingsystem;


import com.parkit.parkingsystem.service.InteractiveShell;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class LoadMenuTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();


    @Test
    public void menuTest() throws Exception {
        //GIVEN
        InteractiveShell interactiveShell = new InteractiveShell();
        String input = "a\n4\n3\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        //WHEN
        System.setOut(new PrintStream(outContent));
        InteractiveShell.loadInterface();

        //THEN
        assertTrue(outContent.toString().contains("Error reading input. Please enter valid number for proceeding further"));
        assertTrue(outContent.toString().contains("Unsupported option. Please enter a number corresponding to the provided menu"));
        assertTrue(outContent.toString().contains("Welcome to Parking System!"));
        assertTrue(outContent.toString().contains("Please select an option. Simply enter the number to choose an action"));
        assertTrue(outContent.toString().contains("1 New Vehicle Entering - Allocate Parking Space"));
        assertTrue(outContent.toString().contains("2 Vehicle Exiting - Generate Ticket Price"));
        assertTrue(outContent.toString().contains("3 Shutdown System"));
    }


}

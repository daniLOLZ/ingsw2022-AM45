package it.polimi.ingsw.network;

import it.polimi.ingsw.model.StudentEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

public class BrokerTest {

    MessageBroker broker;

    /**
     * Creates an input stream from the JSON passed as a String
     * @param message the json formatted message
     * @return an input stream containing the message
     */
    public InputStream createJSONFile(String message){
        File tempFile;
        InputStream inputStreamFile;
        OutputStream outputStreamFile;
        try {
            tempFile = File.createTempFile("prefix","suffix");
            inputStreamFile = new FileInputStream(tempFile);
            outputStreamFile = new FileOutputStream(tempFile);
        } catch (IOException e) {
            fail("Can't create temp file or get streams");
            return null;
        }
        try {
            outputStreamFile.write(message.getBytes(StandardCharsets.UTF_8));
            outputStreamFile.flush();
        } catch (IOException e) {
            fail("can't write to temp file");
        }
        return inputStreamFile;
    }

    @BeforeEach
    public void init(){
        broker = new MessageBroker();
    }

    /**
     *  Check if the message broker can deserialize a simple json string representing a message
     *  sent by the user
     */
    @Test
    public void deserializeTest(){
        InputStream stream = createJSONFile("""
                {
                    "COMMAND" : "CONNECTION_REQUEST",
                    "NICKNAME" : "gigio"
                }""");
        try{
            broker.receive(stream);
        }
        catch (IOException e){
            //null
        }
        try {
            broker.waitSyncMessage();
        } catch (InterruptedException e) {
            fail();
        }
        assertEquals(CommandEnum.fromObjectToEnum(broker.readField(NetworkFieldEnum.COMMAND)), CommandEnum.CONNECTION_REQUEST);
        assertEquals(broker.readField(NetworkFieldEnum.NICKNAME), "gigio");
        broker.flushFirstSyncMessage();
    }

    /**
     * Tests whether two messages waiting on the input buffer are correctly separated and translated
     * into two distinct messages
     */
    @Test
    public void twoMessagesQuickSuccession(){
        InputStream stream = createJSONFile("""
                {
                    "COMMAND" : "CONNECTION_REQUEST",
                    "NICKNAME" : "gigio"
                }{
                    "COMMAND" : "CONNECTION_REQUEST",
                    "NICKNAME" : "gigio2"
                }""");
        try{
        broker.receive(stream);
        broker.receive(stream);
        }
        catch (IOException e){
            //null
        }

        try {
            broker.waitSyncMessage();
        } catch (InterruptedException e) {
            fail("No first message to read");
        }
        assertEquals("gigio", ( broker.readField(NetworkFieldEnum.NICKNAME)));
        broker.flushFirstSyncMessage();

        try {
            broker.waitSyncMessage();
        } catch (InterruptedException e) {
            fail("No second message to read");
        }
        assertEquals("gigio2", ( broker.readField(NetworkFieldEnum.NICKNAME)));
        broker.flushFirstSyncMessage();
        //If the messages were correctly parsed, the broker should be able to read two different messages correctly
    }

    /**
     * Tests whether the outgoingMessage is reset after sending
     */
    @Test
    public void emptyOutgoingAfterSending(){
        InputStream in = this.createJSONFile("""
                {
                    "COMMAND" : "CONNECTION_REQUEST",
                     "NICKNAME" : "gigio"
                }
                """);
        OutputStream out = System.out;
        try{
        broker.send(out);
        assertEquals(broker.getOutgoingMessage(), new HashMap<>());
        }
        catch (IOException e){
            //null
        }
    }

    /**
     * Tests whether the broker can handle reading integer arrays
     */
    @Test
    public void readIntArrays() {
        InputStream in = this.createJSONFile("""
                {
                    "COMMAND" : "SELECT_ENTRANCE_STUDENTS",
                    "CHOSEN_ENTRANCE_POSITIONS" : [0,1,2]
                }
                """);
        OutputStream out = System.out;

        int[] realPositions = {0,1,2};
        try {
            broker.receive(in);
        }
        catch (IOException e){
            fail();
        }
        try {
            broker.waitSyncMessage();
        } catch (InterruptedException e) {
            fail("No message gotten");
        }

        int[] parsedArray = ApplicationHelper.getIntArrayFromBrokerField(broker.readField(NetworkFieldEnum.CHOSEN_ENTRANCE_POSITIONS));
        assertArrayEquals(parsedArray, realPositions);
    }

    /**
     * Tests whether the broker can handle reading student enum arrays
     */
    @Test
    public void readStudentEnumArrays() {
        InputStream in = this.createJSONFile("""
                {
                    "COMMAND" : "SELECT_ENTRANCE_STUDENTS",
                    "CHOSEN_STUDENT_COLORS" : [RED,BLUE,GREEN]
                }
                """);
        OutputStream out = System.out;

        StudentEnum[] realColors = {StudentEnum.RED,StudentEnum.BLUE,StudentEnum.GREEN};
        try {
            broker.receive(in);
        }
        catch (IOException e){
            fail();
        }
        try {
            broker.waitSyncMessage();
        } catch (InterruptedException e) {
            fail("No message gotten");
        }

        StudentEnum[] parsedArray = (StudentEnum[]) (broker.readField(NetworkFieldEnum.CHOSEN_STUDENT_COLORS));
        assertArrayEquals(parsedArray, realColors);
    }


}

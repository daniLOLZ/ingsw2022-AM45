package it.polimi.ingsw.network;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class BrokerTest {

    MessageBroker broker;

    public InputStream createJSONFile(String message){
        File tempFile;
        InputStream inputStreamFile;
        OutputStream outputStreamFile;
        try {
            tempFile = File.createTempFile("test","idfk");
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

    @Test
    public void serializeTest(){
        //todo
    }

    @Test
    public void deserializeTest(){

        InputStream stream = createJSONFile("{\n" +
                "    \"COMMAND\" : \"CONNECTION_REQUEST\",\n" +
                "    \"NICKNAME\" : \"gigio\"\n" +
                "}");
        broker.receive(stream);
        assertEquals(CommandEnum.fromObjectToEnum(broker.readField(NetworkFieldEnum.COMMAND)), CommandEnum.CONNECTION_REQUEST);
        assertEquals((String)broker.readField(NetworkFieldEnum.NICKNAME), "gigio");
    }

    /*
    @Test
    public void twoMessagesQuickSuccession(){
        InputStream stream = createJSONFile("{\n" +
                "    \"COMMAND\" : \"CONNECTION_REQUEST\",\n" +
                "    \"NICKNAME\" : \"gigio\"\n" +
                "}"+"\n" +
                "{\n" +
                "    \"COMMAND\" : \"CONNECTION_REQUEST\",\n" +
                "    \"NICKNAME\" : \"gigio2\"\n" +
                "}");
        assertTrue(broker.receive(stream));
        assertTrue(broker.receive(stream));
        assertFalse(broker.receive(stream));
        //If the messages were correctly parsed, the broker should be able to read two different messages correctly
    }
    */
}

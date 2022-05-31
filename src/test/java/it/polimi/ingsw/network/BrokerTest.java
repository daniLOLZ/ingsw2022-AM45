package it.polimi.ingsw.network;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Scanner;

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

    @Test
    public void serializeTest(){
        //todo
    }

    @Test
    public void deserializeTest(){
        InputStream stream = createJSONFile("""
                {
                    "COMMAND" : "CONNECTION_REQUEST",
                    "NICKNAME" : "gigio"
                }""");
        broker.receive(stream);
        try {
            broker.takeIncomingMessage();
        } catch (InterruptedException e) {
            fail();
        }
        //while (!broker.lock());
        assertEquals(CommandEnum.fromObjectToEnum(broker.readField(NetworkFieldEnum.COMMAND)), CommandEnum.CONNECTION_REQUEST);
        assertEquals((String)broker.readField(NetworkFieldEnum.NICKNAME), "gigio");
        //broker.unlock();
        broker.flushFirstMessage();
    }

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
        broker.receive(stream);
        broker.receive(stream);

        try {
            broker.takeIncomingMessage();
        } catch (InterruptedException e) {
            fail("No first message to read");
        }
        assertEquals("gigio", ((String) broker.readField(NetworkFieldEnum.NICKNAME)));
        broker.flushFirstMessage();

        try {
            broker.takeIncomingMessage();
        } catch (InterruptedException e) {
            fail("No second message to read");
        }
        assertEquals("gigio2", ((String) broker.readField(NetworkFieldEnum.NICKNAME)));
        broker.flushFirstMessage();
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
        broker.send(out);
        assertEquals(broker.getOutgoingMessage(), new HashMap<>());
    }


}

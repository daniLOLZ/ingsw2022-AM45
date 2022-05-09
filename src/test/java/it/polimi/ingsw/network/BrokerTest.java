package it.polimi.ingsw.network;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BrokerTest {

    MessageBroker broker;

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
        File tempFile;
        InputStream inputStreamFile;
        OutputStream outputStreamFile;
        try {
            tempFile = File.createTempFile("test","idfk");
            inputStreamFile = new FileInputStream(tempFile);
            outputStreamFile = new FileOutputStream(tempFile);
        } catch (IOException e) {
            assertTrue(false, "Can't create temp file or get streams");
            return;
        }
        try {
            outputStreamFile.write(("{\n" +
                    "    \"COMMAND\" : \"CONNECTION_REQUEST\",\n" +
                    "    \"NICKNAME\" : \"gigio\"\n" +
                    "}").getBytes(StandardCharsets.UTF_8));
            outputStreamFile.flush();
        } catch (IOException e) {
            assertTrue(false, "can't write to temp file");
            return;
        }
        broker.receive(inputStreamFile);
        while (!broker.lock());
        assertEquals(CommandEnum.fromObjectToEnum(broker.readField(NetworkFieldEnum.COMMAND)), CommandEnum.CONNECTION_REQUEST);
        assertEquals((String)broker.readField(NetworkFieldEnum.NICKNAME), "gigio");
        broker.unlock();
    }
}

package it.polimi.ingsw.network;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApplicationHelperTest {

    @Test
    public void badlyFormattedIntArraysTest(){
        assertFalse(ApplicationHelper.isIntArray("[1,2,3"));
        assertFalse(ApplicationHelper.isIntArray("[,]"));
        assertFalse(ApplicationHelper.isIntArray("[1,]"));
        assertFalse(ApplicationHelper.isIntArray("[a]"));
        assertFalse(ApplicationHelper.isIntArray(""));
        assertFalse(ApplicationHelper.isIntArray("[]"));

        assertTrue(ApplicationHelper.isIntArray("[1]"));
        assertTrue(ApplicationHelper.isIntArray("[1,4141]"));
        assertTrue(ApplicationHelper.isIntArray("[1,1,1,1,1,1,1]"));

    }
}

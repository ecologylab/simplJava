package simpl.translation.api;

import static org.junit.Assert.*;

import org.junit.Test;

import simpl.translation.api.SourceAppender;
import simpl.translation.api.SourceAppenderConfiguration;
import simpl.translation.api.SourceCodeAppender;
import simpl.translation.api.SourceEntry;

public class SourceAppenderTest {

	@Test
	public void test() {

		SourceAppenderConfiguration sac = new SourceAppenderConfiguration("\t", "\r\n", "{","}");		
		SourceCodeAppender sca = new SourceCodeAppender();
		sca.append("package");
		sca.append(SourceEntry.BLOCK_BEGIN);
		sca.append("inside");
		sca.append(SourceEntry.BLOCK_BEGIN);
		sca.append("deeper");
		sca.append(SourceEntry.BLOCK_END);
		sca.append(SourceEntry.BLOCK_END);
		sca.append("after");
		
		String result = sca.toSource();
		
		assertEquals("package\r\n{\r\n\tinside\r\n\t{\r\n\t\tdeeper\r\n\t}\r\n}\r\nafter\r\n", result);
	}
	
	@Test
	public void testAppending()
	{
		SourceAppender sca = new SourceCodeAppender();
		assertEquals(0, sca.size());
		sca.append("a");
		assertEquals(1, sca.size());
		sca.append(SourceEntry.BREAK);
		assertEquals(2, sca.size());
		
		SourceAppender sca2 = new SourceCodeAppender().append("1");
		assertEquals(1, sca2.size());
		sca2.append("2").append("3");
		
		assertEquals(3, sca2.size());
		
		sca.append(sca2);
		
		assertEquals(5, sca.size());
		assertEquals(3, sca2.size());
		
		sca2.append("4");
		assertEquals(5, sca.size());
		assertEquals(4, sca2.size());
	}

}

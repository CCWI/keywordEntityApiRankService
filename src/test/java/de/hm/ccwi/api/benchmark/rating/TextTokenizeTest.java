package de.hm.ccwi.api.benchmark.rating;

import java.util.StringTokenizer;

import org.junit.Test;

public class TextTokenizeTest {

	@Test
	public void testTokenizeGoldstandard() {
		String tweet1 = "In today’s digital world, #MobileMatters. Tweet us how mobile devices like smartphones, tablets + all things #IoT are keeping you connected!";
		String tweet2 = "The latest The Des Holmes Daily! Thanks to @kpopper @bradlygreen @Jayenkai #day9 #iot";
		String tweet3 = "#raspberry #tech #pi - Windows 10 for IoT • Re: Raspberry pi zero and W10 for IOT/Visual Studio - - Gus50310 wrot�";
		StringTokenizer st = new StringTokenizer(tweet3, ".,! ()[]+#@");
		while (st.hasMoreElements()) {
			String s = st.nextElement().toString();
			if (s.length() > 1) {
				System.out.println(s);
			}
		}
	}
}
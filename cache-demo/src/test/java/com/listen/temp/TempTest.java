package com.listen.temp;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class TempTest {


	@Test
	public void homePage() throws Exception {
		ArrayList<String> list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		Iterator it = list.iterator();
		for(String i : list){
			System.out.println("first ---- "+i);
			if(i.equals("b")){
				list.remove("b");
			}
		}
		for(String i : list){
			System.out.println("second ---- "+i);
		}
//		if(it.hasNext()){
//			if("a".equals(it.next())){
//				list.remove("a");
//			}
//		}
			
	}
}

package test;

import java.util.ArrayList;
import java.util.List;

import common.math.Tupple;

public class TuppleTest {

	public static void main(String...args) {
		Tupple<Character> tupple = new Tupple<>('a', 'B', 'x');
		System.out.println(tupple.toJson(true));
		System.out.println(tupple.toString());
		System.out.println(tupple.toString(true));
		System.out.println(tupple.getUniqueElements());
		
		Tupple<Integer> t2 = new Tupple<>(100, 200, 300);
		System.out.println(t2.toJson(true));
		System.out.println(t2.toString());
		System.out.println(tupple.getElements());
		
		String[] array = { "Fred", "Cheryl", "Don" };
		Tupple<String> tupple3 = new Tupple<String>(array);
		System.out.println(tupple3.toJson(true));
		array[0] = "Alexander";
		System.out.println(tupple3.toJson(true));
		
		List<Character> clist = new ArrayList<>();
		clist.add('a');
		clist.add('C');
		clist.add('y');
		Tupple<Character> ctupple = new Tupple<>(clist);
		System.out.println(ctupple);
		
		Tupple<Character> ct2 = new Tupple<>(3);
		ct2.add('b');
		ct2.add('D');
		ct2.add('z');
		System.out.println(ct2);	
	}
}

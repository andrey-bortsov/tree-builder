package familytree;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import familytree.proj2.FamilyTree.Node;

public class proj2Test {
	
	proj2 p;
	FileReader file;
	BufferedReader sc;
	char[] pair;

	@Before
	public void setUp() throws Exception {
		p = new proj2();
		 file = new FileReader("E:\\WORKFOLDERS\\CSC_316\\Projects\\P2\\temp\\medium-input.txt");
		 sc = new BufferedReader(file);
		p.readPreorder(sc);
		p.readPostorder(sc);
		for(int i=0; i<15; i++){
			System.out.print(p.pretrav[i]);
		}
		for(int i=0; i<15; i++){
			System.out.print(p.posttrav[i]);
		}
		System.out.print(p.pretrav.length);
		System.out.println(p.posttrav.length);
		p.ft.mainRoot = p.ft.buildTree(15, 0, 0);
		
		pair = p.readQuery(sc);
		System.out.print(pair[0]);


		
		
	}


	@Test
	public void testProj2() throws IOException {
		
		assertTrue(p.ft.mainRoot.getOffspring()[0].getName() == 'H');
	}

}

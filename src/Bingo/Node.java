package Bingo;
import java.util.*;
public class Node {
	int value;
	boolean marked;
	List <Node> neighbors;
	public Node(int value) {
		this.value=value;
		this.marked=false;
		this.neighbors= new ArrayList<>();
	}
	

}

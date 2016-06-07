// package graphe;
import java.io.*;
import java.util.*;
// import graphe.basics.*;
// import graphe.bfs.*;
// import graphe.parameters.*;

public class ShuffleSquare{


    public static void main(String[] args) {
	String square="";
	String word="";
	String schema="";
	if(args.length <= 1){
	    square=args[0];
	    checkWord(square,word);
	    System.out.println(square+" "+word+" ");
	    square=square.toLowerCase();
	    schema=greedyRoot2("",square,"");
	    System.out.println(schema);	    
	    //checkWord(square,word);
	}	
	else
	    for (int i = 0; i < Integer.parseInt(args[1]); i++) {
		word=randomBin(Integer.parseInt(args[0]));
		square=randomShuffle(word,word.toUpperCase(),"");
		checkWord(square,word);
	    }
    }
    
    public static void checkWord(String square, String word) {
	    System.out.println(square+" "+word+" ");
	    square=square.toLowerCase();
	    greedyRoot3("",square,"");
	    // Reverse
	    System.out.println("---REVERSE");		    	    
	     square = new StringBuilder(square).reverse().toString();
	    // schema = greedyRoot("",square,"");
	     greedyRoot3("",square,"");
	     System.out.println("--------------------");		    
	}
    
    //Search the root of a word by a greedy method 
    public static String greedyRoot(String prefix, String sufix, String schema){
    	if(prefix.isEmpty() && sufix.isEmpty()) //DONE
    	    return schema;
    	if(prefix.length()>sufix.length()) // Root not find
    	    return schema+"ERROR"; 
    	if (prefix.isEmpty() || prefix.charAt(0) != sufix.charAt(0))
    	    return greedyRoot(prefix+sufix.charAt(0),sufix.substring(1),schema+"0" );
    	return greedyRoot(prefix.substring(1), sufix.substring(1),schema+"1");	
    }
    
    // Search the root of a word by a semi/greedy method (next first on empty prefix) 
    public static String greedyRoot2(String prefix, String sufix, String schema){	
	if(prefix.isEmpty() && sufix.isEmpty()) //DONE
	    return schema;
	if(prefix.length()>sufix.length()) // Root not find
	    return schema+"ERROR";
	if (prefix.isEmpty()){ //no prefix
	    int next=nextFirstFrom(sufix.substring(0,1),sufix,1); //next block
	    if (next<0) // No next block
		next=1; // ...so only next letter
	    for (int i = 0; i < next; i++) // fix new prefix
		schema+=0;
	    return greedyRoot2(sufix.substring(1,next),sufix.substring(next+1),schema+"1"); // maybe error if no next letter	    	    
	}
	if (prefix.charAt(0) != sufix.charAt(0))
    	    return greedyRoot2(prefix+sufix.charAt(0),sufix.substring(1),schema+"0" );
	return greedyRoot2(prefix.substring(1), sufix.substring(1),schema+"1");
    }

    // Search the root with both, greedy and jump to the next 
    public static void greedyRoot3(String prefix, String sufix, String schema){
	//System.out.println(schema);
	if(prefix.isEmpty() && sufix.isEmpty()){ //DONE
	    System.out.println(schema+" GOOD");
	    return;
	}
	if(prefix.length()>sufix.length()){ // Root not find
	    System.out.println(schema+"ERRORSIZE");
	    return;
	}
	if (prefix.isEmpty()){  //no prefix but sufix
	    greedyRoot3(sufix.substring(0,1),sufix.substring(1),schema+"0"); //put the first of sufix
	    return;
	}
	int next=0;
	next=next(prefix.substring(0,1),sufix); 
	if (next<0){ // No next letter
	    System.out.println(schema+"ERRORNEXT");
	    return;
	}
	else { // next greedy ocurrence
	    for (int i = 0; i < next; i++) // fix new prefix
		schema+=0;
	    greedyRoot3(prefix.substring(1)+sufix.substring(0,next),sufix.substring(next+1),schema+"1");
	}
	next=nextFirstFrom(prefix.substring(0,1),sufix,next);//next block 
	if (next<0){ // No next block
	    System.out.println(schema+"ERRORBLOCK");
	    return;
	}
	else {
	    for (int i = 0; i < next; i++) // fix new prefix
		schema+=0;
	    greedyRoot3(prefix.substring(1)+sufix.substring(0,next),sufix.substring(next+1),schema+"1");		
	}
    }

    // Create a random binary word on the alphabet 'char' 
    public static String randomBin(int length) {
	char[] chars = "ab".toCharArray();
	StringBuilder sb = new StringBuilder();
	Random random = new Random();
	for (int i = 0; i < length; i++) 
	    sb.append(chars[random.nextInt(chars.length)]);
	return sb.toString();
    }

    // Generate a random shuffle product between u and b
    public static String randomShuffle(String u, String v, String result ) {
	Random random = new Random();
	int r=random.nextInt(2);
	if(u.isEmpty() && v.isEmpty())
	    return result;
	else
	    if(u.isEmpty())
		return result+v;
	    else
		if (v.isEmpty())
		    return result+u;
		else
		    if(r==1)
			return randomShuffle(u.substring(1),v,result+u.charAt(0));
		    else
			return randomShuffle(u,v.substring(1),result+v.charAt(0));
    }

    
    // Print in a above/down the schema
    //ex printSchema(bbaaaa,010011):
    //b aa
    // b  aa
    public static void printSchema(String square, String schema) {
	String root1=""; String root2="";
	for (int i = 0; i < schema.length(); i++){ 
	    if(schema.charAt(i)=='0'){
		root1+=square.charAt(i);
		root2+=" ";
	    }
	    else if (schema.charAt(i)=='1'){
		root1+=" ";
		root2+=square.charAt(i);
	    }
	    else
		break;
	}
	System.out.println(root1);
	System.out.println(root2);	
    }

    // Search for the next letter
    public static int next(String letter, String word){	
	return word.indexOf(letter);
    }

    // Search the  next block of 'letter'    
    public static int nextFirst(String letter, String word){	
	int i=0;
	while(i<word.length() && word.charAt(i)== letter.charAt(0) )
	    i++;
	if (i == word.length()) 
	    return -1;
	return word.indexOf(letter,i);
    }

    // Seach the next block of 'letter' no before 'from'  
    public static int nextFirstFrom(String letter, String word, int from){
	if(from<0 || from > word.length())
	    return -1;
	int first=nextFirst(letter, word.substring(from)); 
	if (first>0) 
	    return first+from;
	return -1;
    }
}
// ARCHIVE

// public static String replaceCharAt(String s, int pos, char c) {
// 	StringBuffer buf = new StringBuffer( s );
// 	buf.setCharAt( pos, c );
// 	return buf.toString( );
// }
    
// public static String Root(String motif, String rest, String schema){
// 	if (motif.isEmpty() && rest.isEmpty()) //DONE
// 	    return schema;
// 	if (motif.isEmpty()){ // Substring that is a square
// 	    String letter=rest.substring(0,1);	
// 	    int next=rest.indexOf(letter);
// 	    if(next < 0)
// 		return "ERROR";
// 	    else 
// 	    return Root(letter,rest,schema);
// 	    schema+=letter;
	    
	    
// 	    int step=1;
// 	    //take next round
// 	    int next=nextFirst(rest.substring(0,1), rest);
// 	    if(next>0)
// 		step=next;	    
// 	    motif=rest.substring(0,step);
// 	    rest=rest.substring(step);
// 	    return Root(motif,rest,schema);
// 	}
// 	//System.out.println(motif+" - "+rest+" - "+schema);
// 	motif=motif.substring(1)+rest.substring(0,next);
// 	rest=rest.substring(next+1,rest.length());
// 	return Root(motif,rest,schema);
// }
      

//     public static String restOfMotif(String word, String motif, String rest, String schema){
//     	if (motif.isEmpty()) return rest;
//     	if (word.isEmpty()) return null;
//     	if (word.charAt(0) != motif.charAt(0)){
// 	    schema+="0";
//     	    return restOfMotif(word.substring(1), motif, rest+word.charAt(0),schema);
// 	}
//     	else{
// 	    schema+="1";
// 	    return restOfMotif(word.substring(1), motif.substring(1),rest,schema);
// 	}
//     }
//     public static String greedyRoot(String word, String motif,String square, String schema){	
// 	if (word.isEmpty() && motif.isEmpty()) //DONE
// 	    return square;	
// 	if (motif.isEmpty()) { // Substring that is a square
// 	    //take first letter
// 	    motif=word.substring(0,1);
// 	    word=word.substring(1);
// 	    }
// 	String rest=restOfMotif(word,motif,"","");    
// 	if(rest == null) // No square possible
// 	    return "ERROR";	
// 	word=word.substring(motif.length()+rest.length());
// 	//System.out.println(word+" - "+rest+" - "+square+motif+" "+schema);
// 	return greedyRoot(word, rest, square+motif, schema);
// }

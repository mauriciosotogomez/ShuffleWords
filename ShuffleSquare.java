// package graphe;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

// import graphe.basics.*;
// import graphe.bfs.*;
// import graphe.parameters.*;

public class ShuffleSquare{

    static boolean useFlag=false;
    static boolean flag=true;
    
    public static void main(String[] args) {
	String square="";
	String word="";
	String schema="";
	if (args[args.length-1].equals("-flag")){
	    useFlag=true;
	    args=Arrays.copyOf(args, args.length-1);
	}	
	if(args.length == 1){
	    square=args[0];
	    checkWord(square,word);
	}	
	else
	    for (int i = 0; i < Integer.parseInt(args[1]); i++) {
		word=randomBin(Integer.parseInt(args[0]));
		square=randomShuffle(word,word.toUpperCase(),"");
		checkWord(square,word);
	    }
    }
    
    public static void checkWord(String square, String word) {
	System.out.println(square+" --- "+word+" ");
	square=square.toLowerCase();

	flag=true;
	System.out.println("---GREEDY");
	long startTime = System.nanoTime();
	System.out.println(greedyRoot("",square,""));
	System.out.println("---Total execution time: " +TimeUnit.SECONDS.convert( (System.nanoTime() - startTime), TimeUnit.NANOSECONDS));
	
	// flag=true;
	// System.out.println("---ROOTthenGREEDY");
	// RootThenGreedy("",square,"");
	// System.out.println("---Total execution time: " +TimeUnit.SECONDS.convert( (System.nanoTime() - startTime), TimeUnit.NANOSECONDS));

			
	// startTime = System.nanoTime();
	// System.out.println(semiGreedyRoot("",square,""));
	// System.out.println("---Total execution time: " + (System.nanoTime() - startTime));

	// System.out.println("---GREEDY3");
	// startTime = System.nanoTime();
	// greedyRoot3("",square,"");
      	// System.out.println("---Total execution time: " +TimeUnit.SECONDS.convert( (System.nanoTime() - startTime), TimeUnit.NANOSECONDS));

	flag=true;		
	System.out.println("---ROOT_DEPTH2");
	startTime = System.nanoTime();
	Root(2,"",square,"");
		System.out.println("---Total execution time: " + TimeUnit.SECONDS.convert( (System.nanoTime() - startTime), TimeUnit.NANOSECONDS));

	flag=true;
	System.out.println("---ROOT");
	startTime = System.nanoTime();
	Root("",square,"");
	System.out.println("---Total execution time: " +TimeUnit.SECONDS.convert( (System.nanoTime() - startTime), TimeUnit.NANOSECONDS));

	// System.out.println("---ALT");
	// startTime = System.nanoTime();
	// RootAlt("",square,"","","",true);
	// System.out.println("---Total execution time: " + (System.nanoTime() - startTime));
	    
	System.out.println("---");		    
    }
    
    //Search the root of a word by a greedy method 
    public static String greedyRoot(String prefix, String sufix, String schema){
    	if(prefix.isEmpty() && sufix.isEmpty()) //DONE
    	    return schema+" GOOD";
    	if(prefix.length()>sufix.length()) // Root not find
    	    return schema+" ERROR"; 
    	if (prefix.isEmpty() || prefix.charAt(0) != sufix.charAt(0))
    	    return greedyRoot(prefix+sufix.charAt(0),sufix.substring(1),schema+"0" );
    	return greedyRoot(prefix.substring(1), sufix.substring(1),schema+"1");	
    }

    //Search the root of a word by a greedy method 
    public static String greedyRoot2(String prefix, String sufix, String schema){
    	if(prefix.isEmpty() && sufix.isEmpty()) //DONE
    	    return schema+" GOOD";
	if(prefix.length()>sufix.length()) // Root not find
    	    return schema+" ERROR"; 	
	if (prefix.isEmpty())
	    return greedyRoot2(sufix.substring(0,1),sufix.substring(1),schema+"0" );
        int next=sufix.indexOf(prefix.substring(0,1));
	if (next<0 )
	    return schema+"ERRORnoNEXT";
	for (int i = 0; i < next; i++) // fix new prefix
	    schema+=0;
	//	String zeros = new String(new char[next]).replace("\0", "0");;
	return greedyRoot2(prefix.substring(1)+sufix.substring(0,next), sufix.substring(next+1),schema+"1");	
    }

    // Search the root of a word by a semi/greedy method (next first on empty prefix) 
    public static String semiGreedyRoot(String prefix, String sufix, String schema){	
	if(prefix.isEmpty() && sufix.isEmpty()) //DONE
	    return schema+" GOOD";
	if(prefix.length()>sufix.length()) // Root not find
	    return schema+" ERROR";
	if (prefix.isEmpty()){ //no prefix
	    int next=nextFirstFrom(sufix.substring(0,1),sufix,1); //next block
	    if (next<0) // No next block
		next=1; // ...so only next letter
	    for (int i = 0; i < next; i++) // fix new prefix
		schema+=0;
	    return semiGreedyRoot(sufix.substring(1,next),sufix.substring(next+1),schema+"1"); // maybe error if no next letter	    	    
	}
	if (prefix.charAt(0) != sufix.charAt(0))
    	    return semiGreedyRoot(prefix+sufix.charAt(0),sufix.substring(1),schema+"0" );
	return semiGreedyRoot(prefix.substring(1), sufix.substring(1),schema+"1");
    }
    
    // Search the root with both, greedy and jump to the next 
    public static void greedyRoot3(String prefix, String sufix, String schema){
	//System.out.println(schema);
	if(prefix.isEmpty() && sufix.isEmpty()){ //DONE
	    System.out.println(schema+" GOOD");
	    return;
	}
	if(prefix.length()>sufix.length()){ // Root not find
	    //System.out.println(schema+"ERRORSIZE");
	    return;
	}
	if (prefix.isEmpty()){  //no prefix but sufix
	    greedyRoot3(sufix.substring(0,1),sufix.substring(1),schema+"0"); //put the first of sufix
	    return;
	}
	int next=0;
	next=next(prefix.substring(0,1),sufix); 
	if (next<0){ // No next letter
	    //System.out.println(schema+"ERRORNEXT");
	    return;
	}
	String zeros="";
	for (int i = 0; i < next; i++) // fix new prefix
	    zeros+=0;	   greedyRoot3(prefix.substring(1)+sufix.substring(0,next),sufix.substring(next+1),schema+zeros+"1");	
	next=nextFirstFrom(prefix.substring(0,1),sufix,next);//next block 
	if (next<0){ // No next block
	    //System.out.println(schema+"ERRORBLOCK");
	    return;
	}
	for (int i = 0; i < next; i++) // fix new prefix
	    schema+=0;
	greedyRoot3(prefix.substring(1)+sufix.substring(0,next),sufix.substring(next+1),schema+"1");		
    }

    // Search the root with all, then greedy 
    public static void RootThenGreedy(String prefix, String sufix, String schema){
	// System.out.println(prefix+" - "+sufix);
	if(useFlag)
	    if(flag)
		return;
	if(prefix.isEmpty() && sufix.isEmpty()){ //DONE
	    System.out.println(schema+" GOOD");
	    flag=false;
	    return;
	}
	if(prefix.length()>sufix.length()){ // Root not find
	    // System.out.println(schema+"ERRORSIZE");
	    return;
	}
	if (prefix.isEmpty()){  //no prefix but sufix
	    prefix=sufix.substring(0,1);
	    sufix=sufix.substring(1);
	    schema+=0;
	    //System.out.println("ALL");
	    int next=next(prefix.substring(0,1),sufix);
	    while(next > -1){
		//System.out.println("all-"+next);
		String zeros="";
		for (int i = 0; i < next; i++) // fix new prefix
		    zeros+=0;
		RootThenGreedy(prefix.substring(1)+sufix.substring(0,next), sufix.substring(next+1),schema+zeros+"1");
		next=nextFirstFrom(prefix.substring(0,1),sufix,next);
	    }
	}
	else{
	    //System.out.println("GREEDY");
	    int next=sufix.indexOf(prefix.substring(0,1));
	    if (next<0 )
		return;
	    for (int i = 0; i < next; i++) // fix new prefix
		schema+=0;
	    //	String zeros = new String(new char[next]).replace("\0", "0");;
	    RootThenGreedy(prefix.substring(1)+sufix.substring(0,next), sufix.substring(next+1),schema+"1");		
	} 	
    }

        // Search the root with both, greedy and jump to the next 
    public static void Root(String prefix, String sufix, String schema){
	if(useFlag)
	    if (!flag) 
		return;	
	if(prefix.isEmpty() && sufix.isEmpty()){ //DONE
	    System.out.println(schema+" GOOD");
	    flag=false;
	    return;
	}
	if(prefix.length()>sufix.length()){ // Root not find
	    //	    System.out.println(schema+"ERRORSIZE");
	    return;
	}
	if (prefix.isEmpty()){  //no prefix but sufix
	    Root(sufix.substring(0,1),sufix.substring(1),schema+"0"); //put the first of sufix
	    return;
	}	
	int next=next(prefix.substring(0,1),sufix);
	//	System.out.println(next);
	while(next > -1){
	    String zeros="";
	    for (int i = 0; i < next; i++) // fix new prefix
		zeros+=0;
	    Root(prefix.substring(1)+sufix.substring(0,next), sufix.substring(next+1),schema+zeros+"1");
	    next=nextFirstFrom(prefix.substring(0,1),sufix,next);
	}
    }
    
    // Search the root with both, greedy and jump to the next 
    public static void Root(int deep, String prefix, String sufix, String schema){
	if(useFlag)
	    if (!flag) 
		return;	
	if(prefix.isEmpty() && sufix.isEmpty()){ //DONE
	    System.out.println(schema+" GOOD");
	    flag=false;
	    return;
	}
	if(prefix.length()>sufix.length()){ // Root not find
	    //	    System.out.println(schema+"ERRORSIZE");
	    return;
	}
	if (prefix.isEmpty()){  //no prefix but sufix
	    Root(deep,sufix.substring(0,1),sufix.substring(1),schema+"0"); //put the first of sufix
	    return;
	}	
	int next=next(prefix.substring(0,1),sufix);
	int k=0;
	//	System.out.println(next);
	while(next > -1 && k<deep){
	    k++;
	    String zeros="";
	    for (int i = 0; i < next; i++) // fix new prefix
		zeros+=0;
	    Root(deep, prefix.substring(1)+sufix.substring(0,next), sufix.substring(next+1),schema+zeros+"1");
	    next=nextFirstFrom(prefix.substring(0,1),sufix,next);
	}
    }
    
    //Search the root of a word by a greedy method in both directions 
    public static void RootAlt(String prefix, String word, String sufix, String schema_l, String schema_r,boolean left){
	if(!left)
	    System.out.println(sufix+" - "+reverse(word)+" - "+reverse(prefix)+"(R)");
	else
	    System.out.println(prefix+" - "+word+" - "+reverse(sufix)+"(L)");	   

    	if((prefix.length()>(word.length()+sufix.length())) || (sufix.length()>(word.length()+prefix.length()))  ){ // Root not find
    	    System.out.println(schema_l+schema_r+"ERROR");
	    return;
	}
    	if((prefix+word).equals(reverse(sufix))){ // match
	    String zeros=""; 
	    for (int i = 0; i < word.length(); i++)// fix new prefix
		zeros+=0;	    
	    if(left){
		System.out.println(schema_l+zeros+reverse(inverse(schema_r))+" GOOD");
		return;
	    }
	    System.out.println(schema_r+inverse(zeros+reverse(schema_l))+" GOOD");	    
	    return;
	}
	if (prefix.isEmpty()){
	    RootAlt(sufix,reverse(word.substring(1)),word.substring(0,1),schema_r,schema_l+"0",  !left); //put the first of word
	    return;
	}
	int next=next(prefix.substring(0,1),word);
	//	System.out.println(next);
	while(next > -1){
	    String zeros="";
	    for (int i = 0; i < next; i++) // fix new prefix
		zeros+=0;
	    RootAlt(sufix, reverse(word.substring(next+1)),prefix.substring(1)+word.substring(0,next), schema_r, schema_l+zeros+"1",!left);
	    next=nextFirstFrom(prefix.substring(0,1),word,next);
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

    // Reverse a word
    public static String reverse(String word){	
	return new StringBuilder(word).reverse().toString();
    }

    // Reverse a word
    public static String inverse(String word){
	String inverse="";
	for (int i = 0; i < word.length(); i++) {
	    if (word.charAt(i)=='0') 
		inverse+="1";
	    else
		inverse+="0";
	}
	return inverse;
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

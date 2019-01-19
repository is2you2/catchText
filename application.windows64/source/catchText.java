import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class catchText extends PApplet {

StringList[] tmpFiles=new StringList[2];
StringList result=new StringList();
int var=-1;
String msg="";

public void setup(){
  textAlign(CENTER,CENTER);
  noLoop();
  tmpFiles[0]=new StringList();
  tmpFiles[1]=new StringList();
  println("select original");
  msg="기존 작업폴더를 선택하려면 클릭하세요";
  result.append("*result");
}

public void draw(){
  background(0);
  text(msg,10,10,70,70);
  switch(var){
    case -1:
      msg="업데이트 된 폴더를 선택하려면 클릭하세요";
      break;
    case 0:
      selectFolder("select what you have and click me","getTree");
      println("result 0: ",tmpFiles[0].size());
      println("var: ",var,"select Target");
      break;
    case 1:
      tmpFiles[0].sort();
      selectFolder("targetDir select","getTree");
      msg="클릭하면 시작합니다";
      break;
    case 2:
      tmpFiles[1].sort();
      println("result 1: ",tmpFiles[1].size());
      println(tmpFiles[0].get(0));
      println(tmpFiles[1].get(0));
      msg="끝!\n클릭해주세요";
      break;
    case 3:
    for(int i=tmpFiles[0].size()-1,g=0;i>=g;i--){
      for(int j=tmpFiles[1].size()-1;j>=g;j--){
        if(split(tmpFiles[0].get(i),'/')[split(tmpFiles[0].get(i),'/').length-1]
        .equals(
        split(tmpFiles[1].get(j),'/')[split(tmpFiles[1].get(j),'/').length-1])
        &&split(tmpFiles[0].get(i),'/')[split(tmpFiles[0].get(i),'/').length-1]
        .indexOf("DS_Store")<0){
          println("catch same files: ",i,j);
          println(tmpFiles[0].get(i));
          IntDict origCont=new IntDict(),
            targetCont=new IntDict();
          { // target
            final String[] tmp=loadStrings(tmpFiles[1].get(j));
            for(int a=0,b=tmp.length;a<b;a++){
              targetCont.set(tmp[a],a);
            } targetCont.sortValues();
          } { // original
            final String[] tmp=loadStrings(tmpFiles[0].get(i));
            for(int a=0,b=tmp.length;a<b;a++){
              origCont.set(tmp[a],a);
            } origCont.sortValues();
          }
          for(int k=origCont.size()-1,a=0;k>=a;k--){
            final String[] otmp=origCont.keyArray();
            for(int l=targetCont.size()-1;l>=a;l--){
              final String[] tTmp=targetCont.keyArray();
              if(trim(otmp[k]).equals(trim(tTmp[l]))){
                origCont.remove(otmp[k]);
                targetCont.remove(tTmp[l]);
                println("bang! ",k,l);
                break;
              }
            }
            if(targetCont.size()>0){
              final String[] tmp=targetCont.keyArray();
              for(int l=0,b=tmp.length;l<b;l++){
                if(trim(tmp[l]).indexOf("//")==0||
                trim(tmp[l]).indexOf("<!--")==0){
                  targetCont.remove(tmp[l]);
                }
              }
            }else{
              println("no one alive");
            }
          }
          targetCont.sortValues();
          final String[] tmpKey=targetCont.keyArray();
          if(targetCont.size()>0){
            println("title: "+tmpFiles[1].get(j));
            result.append("\n");
            result.append("##File "+i+" "+j+
            ": "+tmpFiles[1].get(j));
          }
          for(int l=0,b=targetCont.size();l<b;l++){
            result.append(TAB+"Line "+targetCont.get(tmpKey[l])+": "+tmpKey[l]);
          }
          tmpFiles[1].remove(j);
          tmpFiles[0].remove(i);
          break;
        }
      }
    }
    if(tmpFiles[1].size()>0){
      result.append("\nNew Files!");
      for(int i=0,j=tmpFiles[1].size();i<j;i++){
        result.append(tmpFiles[1].get(i));
      }
    }
    redraw();
    break;
    default: println("wtf",var);
      println("getResult: "+result.size());
      saveStrings("result.js",result.array());
      exit();
  }
}

public void getTree(File dir){
  if(dir==null){
    println("getTree null: ",dir);
    exit();
  }else{
    File files=new File(dir.toString());
    File[] tmp=files.listFiles();
    for(int i=0,li=tmp.length;i<li;i++){
      if(tmp[i].isDirectory()){
        getTree(tmp[i]);
      }else{
        tmpFiles[var].append(tmp[i].toString());
      }
    }
  }
}

public void mousePressed(){
  var++;
  println("counter: ",var);
  redraw();
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "catchText" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}

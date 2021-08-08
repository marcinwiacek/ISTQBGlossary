var forReading = 1, forWriting = 2, forAppending = 8;

fs = new ActiveXObject("Scripting.FileSystemObject");
f = fs.GetFile("x3");

is = f.OpenAsTextStream( forReading, 0 );

empty=false;
while( !is.AtEndOfStream ){
   s = is.ReadLine();
   if (s.indexOf("Patrz tak¿e ")!=-1) {
        WScript.Echo(s.substring(0,s.indexOf("Patrz tak¿e ")+12)+"<i>"+s.substring(s.indexOf("Patrz tak¿e ")+12).replace(/,/g,"<i>,<i>")+"</i>");
   } else   if (s.indexOf("Patrz ")!=-1) {
        WScript.Echo(s.substring(0,s.indexOf("Patrz ")+6)+"<i>"+s.substring(s.indexOf("Patrz ")+6).replace(/,/g,"<i>,<i>")+"</i>");
   } else    if (s.indexOf("See also ")!=-1) {
        WScript.Echo(s.substring(0,s.indexOf("See also ")+9)+"<i>"+s.substring(s.indexOf("See also ")+9).replace(/,/g,"<i>,<i>")+"</i>");
   } else   if (s.indexOf("See ")!=-1) {
        WScript.Echo(s.substring(0,s.indexOf("See ")+4)+"<i>"+s.substring(s.indexOf("See ")+4).replace(/,/g,"<i>,<i>")+"</i>");
   } else {
        WScript.Echo(s);
   }  
}

is.Close();

javac App/App.java -d ./dist
xcopy .\assets .\dist\assets\ /s /y
cd dist
jar -cvmf ..\META-INF\MANIFEST.MF App.jar *
cd ..
java -jar ./dist/App.jar
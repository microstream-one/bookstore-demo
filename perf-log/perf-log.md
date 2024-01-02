
only add one api in the original project  
## 0, build the app
  java -jar app to start the application  

## 1, access the api  
http://192.168.1.1:8080/api/book/count  

http://192.168.1.1:8080/api/book/add?count=1000 to add 1000 books 

## 2, check the log
you will find that when the book count is about 15000, the store cost is about 100ms+.
it is very slower than db

![Screenshot](./log1.png?raw=true)
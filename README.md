# **Project Mercurys**

### _Welcome to Mercurys!_

Mercurys is a fun little side-project I'm working on in between my studies. 
It's basically a console-based chat application that uses a simple Socket connection to transmit and receive data.
This is my first real programming project, and I have a lot of hopes for this!

#### Features:

* Uses a custom encryption-decryption algorithm [developed by myself]
* Uses multi-threading to simultaneously read and write messages to and from the server/client
* Works on a LAN connection [as of yet]

### How does one use Mercurys?

Well it's as simple as can be, really. All one needs to do is follow these steps:

_If you are the host:_
1. Download the project and open it in your IDE of choice
2. In the TextServer.java file, set the "hostAddress" variable to your own LAN IP address.
   [That should look something like "192.168._.___"]
3. Compile and run TextServer.java [src/com/mercurys/TextServer.java]
4. Now the server has been initialised and will wait for a client socket to connect
5. Once a client socket has connected to the IP [set by you in step 2], the program will show a confirmatory message
6. Now you can send and receive messages from the user at the other end of the connection!

_If you are the client:_ 
1. Download the project and open it in your IDE of choice
2. Compile and run the TextClient.java file [src/com/mercurys/TextClient.java]
3. Enter the IP address of the host when prompted
4. Once the connection is successful, the program will show a confirmatory message
5. Now you can send and receive messages from the user at the other end of the connection!

Once you have finished with your talks and wish to close the connection, both of the users have to enter "-x-" 
[without the double quotes] to close the connection and stop the program.

#### Future Plans:

I have many new features and upgrades planned for this project, such as:
* Enabling WAN connections
* Upgrading to multi-client chatting, thus making it a sort of chat room
* Enabling media transfer such as images [you can see the work in progress in the "unfinished" folder]
* Adding a GUI and making this a proper chat application

### Thank you for visiting!

# **Project Mercurys**

### _Welcome to Mercurys!_

Mercurys is a fun little side-project I've been working on in between my studies. 
It's basically a console-based chat application that uses a simple Socket connection to transmit and receive data.

#### Features:

* Uses a custom encryption-decryption algorithm [developed by myself]
* Uses multi-threading to simultaneously read and write messages
* Users can send and receive files
* Works on a LAN connection [as of yet]

### How Do You Use Mercurys?

It's quite simple to get the server/client connection up and running:

#### _To start the connection:_

The first step is to download the project and open it in any IDE.

_For hosting the server:_

1. Compile and run the MercurysServer.java file `[project-mercurys/src/com/mercurys/MercurysServer.java]`
2. Enter the device LAN IP address and the server name [it can be anything] when prompted
3. The server has now been initialised, and it will wait for a client to request a connection
4. Once a client socket has connected to the IP entered in step 3, the program should display a confirmatory message

_For connecting to the server:_

1. Compile and run the MercurysClient.java file `[project-mercurys/src/com/mercurys/MercurysClient.java]`
2. Enter the IP address of the host server along with the client's username
3. Once the connection is successful, the program should display a confirmatory message

Now the 2 devices have been successfully connected, and they can send and receive text messages and media files 
while the connection is active.

***To send a message***

Simply type the message and press enter.

***To send a file***

To send any media file, ensure that the file is present locally on the device. The, go to the console and use the 
following format to send the file:

`/<file type> <absolute file path>

The absolute file path should look like this on different operating systems:

* On Windows - `C:\Pictures\Nature\SummerLeaves.jpg`
* On Linux - `/home/xyz/Documents/Sustainability_Report.pdf`
* On macOS - `/Users/xyz/Documents/ProjectDeadlineNotice.pdf`

Note: Currently, the accepted file types are:

* `image` -> for image files
* `pdf` -> for pdf files

The console will display a confirmatory message that the file has been sent if the transfer is successful, 
and it should display the path of the file downloaded for the user on the opposite end.

If the connection is to be closed, both the users must enter `-x-` to stop the program.

#### Why Am I Doing This?

Apart from serving as a productive way of ~~procrastinating~~ taking a break, Mercurys helps me keep in touch with
Java during my otherwise non-programming related studies. Plus, I use this as a way to explore OOP concepts such as
encapsulation and inheritance, while also trying to to effectively implement them in real world scenarios.

#### Plans For The Future:

I have many new features and upgrades planned for this project, such as:

* Enabling WAN connections
* Upgrading to multi-client chatting
* Adding a GUI and making this a proper chat application

## Thank you for visiting!

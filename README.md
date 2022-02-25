# **Project Mercurys**

### _Welcome to Mercurys!_

Mercurys is a fun little side-project I'm working on in between my studies. 
It's basically a console-based chat application that uses a simple Socket connection to transmit and receive data. This
is my first real programming project, and I have a lot of hopes for this!

#### Features:

* Uses a custom encryption-decryption algorithm [developed by myself]
* Uses multi-threading to simultaneously read and write messages
* Users can send and receive image files
* Works on a LAN connection [as of yet]

### How Do You Use Mercurys?

Well it's as simple as can be, really. Here is what you do:

#### _To start the connection:_

The first step is to download the project and open it in your IDE of choice. Then the steps differ slightly based on
whether you are the one who wishes to host the server [the host] or the one who wishes to request a connection to an
already hosted server [the client].

_If you are the host:_

1. Compile and run the MercurysServer.java file `[project-mercurys/src/com/mercurys/MercurysServer.java]`
2. Enter your computer's LAN IP address when prompted [it should look something like 192.168._.\___]
3. The server has now been initialised, and it will wait for a client to request a connection.
4. Once a client socket has connected to the IP [entered by you in step 3], the program will notify you

_If you are the client:_

1. Compile and run the MercurysClient.java file `[project-mercurys/src/com/mercurys/MercurysClient.java]`
2. Enter the IP address of the host you wish to connect to when prompted
3. Once the connection is successful, the program will notify you

Now the 2 computers have been connected, and you can easily send and receive text messages and images while the
connection is active.

***To send a message***

Simply type the message and press enter.

***To send a file***

To send any media file, make sure you have the file downloaded on your computer. Then, go to the image and copy its "
absolute path".

An absolute file path is the complete and unique "name" of a file. More technically, it is the string of characters used
to uniquely identify a file in a device's directory structure. For example:

* On Windows - `C:\Pictures\Nature\SummerLeaves.jpg`
* On Linux - `/home/xyz/Documents/Sustainability_Report.pdf`
* On macOS - `/Users/xyz/Documents/ProjectDeadlineNotice.pdf`

After copying the absolute path, go to the console and use the following format to send the file:

`/<file type> <absolute path>`

Currently, the accepted file types are:

* `image` -> for image files
* `pdf` -> for pdf files

The console will display a confirmatory message that the file has been sent if all goes well.

The console will also display the path of the file downloaded.

Once you have finished with your talks and wish to close the connection, both the users must
enter `-x-`[without the double quotes] to stop the program.

#### Why Am I Doing This?

Many a times I receive questions from friends and family asking how and why do I devote time to this ultimately
meaningless project instead of focusing more on my studies.

Well, apart from serving as a productive way of ~~procrastinating~~ taking a break, this helps me keep in touch with
Java during my otherwise non-programming related studies. Plus, I'm using this as a way to explore OOP concepts such as
encapsulation and inheritance by myself, and learning to effectively implement them in real world scenarios.

#### Plans For The Future:

I have many new features and upgrades planned for this project, such as:

* Enabling WAN connections
* Upgrading to multi-client chatting, thus making it a sort of chat room
* Adding a GUI and making this a proper chat application

## Thank you for visiting!

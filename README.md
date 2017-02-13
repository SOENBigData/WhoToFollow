# WhoToFollow
MapReduce

Who To Follow


=================================

I.  About

II.   Requirements

III. Running Who To Follow

IV . Source Files

V. Results

VI.   Help & Feedback



I.    About:
==============================

Who To Follow is an algorithm that recommends a User X a list of people to follow Fi based on the number of followers X and Fi have in common.
This algorithm is implemented using two map-reduce jobs.
The first  job creates an inverted list of followers,while the other map reduce job filters the existing followers and sorts the resulting recommendations by number of common followed people.

II.   Requirements
=================================

The program is written in Java. It can be run with any Java Runtime Environment (JRE) that is at least version 1.8.
The JRE (or JVM) can be downloaded from:http://java.sun.com/getjava/download.html.
Hadoop Version : 2.7.3
Hadoop Libraries: Include hadoop libraries in the environment variable for mapreduce
The program is implemented using the MapReduce algorithm

III. Running Who To Follow
=================================
Using command line :

Download the whotofollow.jar from the repository https://github.com/SOENBigData/WhoToFollow/WhoToFollow.jar
Once you have the jar,follow the following steps:
Navigate to the directory where your downloaded WhoToFollow.jar is downloaded.
Create an input file in the same directory for the jar 
Execute the command java -jar whotofollow.jar inputFile.txt output1 output2

Using Eclipse:

To run the Who to Follow Project,follow the following steps:
Download the jar file from the repository https://github.com/SOENBigData/WhoToFollow/
Import the jar file in eclipse using the Import -> Archive file -> your jar file

IV. Source Files:
===================================

WhoToFollow/src/main/java/WhoToFollow/pkg/FilterMapper.java
WhoToFollow/src/main/java/WhoToFollow/pkg/FilterReducer.java
WhoToFollow/src/main/java/WhoToFollow/pkg/InvertMapper.java
WhoToFollow/src/main/java/WhoToFollow/pkg/InvertReducer.java
WhoToFollow/src/main/java/WhoToFollow/pkg/WhoToFollow.java

V. Results :
=====================================

Output1 : Contains the output from IncertMapper and InvertReducer
Output2: Contains final output in the following format

X R1(n1) R2(n2) R3(n3) ...
where Ri are the ids of the people recommended to user X and ni is the number of followed
people in common between X and Ri.


VI.   Help & Feedback
=================================
Help
----
If you need help with anything related to Who To Follow that is not yet documented somewhere then you can send an e-mail to the
mailing list: karunsh1@gmail.com,nupur0691@gmail.com
Feedback
--------
General Feedback is very much appreciated. Send an e-mail to
 karunsh1@gmail.com,nupur0691@gmail.com if you want to provide feedback.


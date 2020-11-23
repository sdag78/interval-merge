## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [Questions](#questions)
* [Design-enhancements-to-work-with-large-datasets](#design-enhancements-to-work-with-large-datasets)

## General info
This project is a java program to merge intervals in a list, e.g. [25,30] [2,19] [14, 23] [4,8]. The result should be: [25,30] [2,23]. 

Unit tests are provided. 

The solution is provided with a Dockerfile. The container uses an official openJDK image to build an executable jar file from the source code, and then runs a main method to display a text prompt, and take the text input which can be pasted in. The program processes the data, and displays the result list. It then allows the user to enter further lists, or exit the program by typing "exit".

See the section "Setup", for the docker commands to build and run the docker container directly from the Github repository. 

Alternatively the repository can be checked out using Git, and built and run using JDK version 8. In this case the java compiler is expected to be on the path.

## Lösungsidee

Each interval of the input list is processed against all intervals of the output list, to check whether the input interval overlaps with any intervals in the output list. If there are no overlaps, then the interval is added to the output list. If there is an overlap, then the interval is merged with the overlapping interval in the output list. No optimisations have been made for processing speed or memory usage, however ideas for possible processing and memory optimisations are described in the sections below.


## Technologies
The project is created with:
* Java 8
* Docker

## Setup
To run this project using docker, use the following docker commands:

```
docker build -t sdag-interval-merge:v1 https://github.com/sdag78/interval-merge.git#main  
docker run --rm --name run-sdag-interval-merge -it sdag-interval-merge:v1
```

alternatively use the following scripts to run docker:
build-image-from-github.sh
run-image-from-github.sh

or

build-image-from-local-src.sh 
run-image-from-local-src.sh 


# Questions

## Wie ist die Laufzeit Ihres Programms ?
[How is the processing time of the program?]
Currently the program is implemented in a non-optimised way, it is not optimised for processing time, or for memory usage. The input data and output data are stored in memory. For every element in the input list, every element in the output list must be re-processed. In the worst case, where no intervals are merged, this means that the number of items to be checked is i^2, where i is the number of input intervals.

The algorithm could be optimised for speed, by sorting the input and output data, and limiting iterations when the numeric range of target intervals is being exceeded.
 
## Wie kann die Robustheit sichergestellt werden, vor allem auch mit Hinblick auf sehr große Eingaben ?
[How can the robustness of the program be guaranteed, particularly with regard to very large data inputs?]
The confidence in any software system is ensured by testing. This is most efficiently achieved through unit testing. The ability to process large data inputs requires additional functionality to achieve various optimisations (temp files and their management). To ensure confidence in this functionality, it should be well tested.

Additionally, program robustness is improved by handling expected errors gracefully. The exception handling strategy in the current code is to handle bad input by displaying an error message, and then ask again for input.

## Wie verhält sich der Speicherverbrauch ihres Programms ?
[How is the memory usage of the program?]
Currently in its non optimised state, all inputs are loaded into memory. A second list is built which will be less than or equal to the size of the input list. Thus the memory used will be at least the size of the input plus the output list.

The memory useage of the current code could be improved if elements are removed from the input list as soon as they are added to the output list. In this case memory usage would not increase, and would be roughly equal to the size of the input data, and in practice would reduce as more intervals are merged together. 
 




## Design enhancements to work with large datasets

To work with datasets that are larger than available memory it would be necessary to make design changes to the existing code, to sort the data, and generate the output data in suitably sized temp files, before finally combining them into a single output file.

To process the input data it is not necessary to have all the input data in memory at one time. Through the use of random access (java.io.RandomAccessFile.read) batches of values can be accessed without loading the entire file into memory. 

To allow the program to build an output list whose size exceeds available memory, it is necessary to break up the output list into batches that fit into memory, storing these in separate temp files while processing. This would require managing the size of each temp file, and splitting it when it becomes larger than a specified "maximum memory" parameter. 

These temp files would be most optimally processed if they are sorted, and this also applies to the input data. When the data is sorted, it would allow processing to focus on one particular output file, without having to jump between output files (with associated IO). This would also allow search within a single output file to be broken off, when the values exceed the applicable range.



[![Build Status](https://travis-ci.org/FINRAOS/JTAF-XCore.svg?branch=master)](https://travis-ci.org/FINRAOS/JTAF-XCore)

JTAF-XCore 
==================
[XCore](http://finraos.github.io/JTAF-XCore/) is a framework to define and execute automated tests. It enables automation code development in Java, test script development in XML via domain specific language, and execution & reporting via JUnit.

Go full throttle on testing with XCore ! Get your application Domain Experts to define tests and get your Developers to implement underlying code.

Here is the link to [getting started](http://finraos.github.io/JTAF-XCore/howitworks.html)

Releases
========
>[Release 1.0](https://github.com/FINRAOS/JTAF-XCore/releases/tag/jtaf-xcore-1.0) - 12/23/2014

>Release 1.1 - Coming soon!

Contributing
=============
We encourage contribution from the open source community to help make XCore better. Please refer to the [development](http://finraos.github.io/JTAF-XCore/contribute.html) page for more information on how to contribute to this project including sign off and the [DCO](https://github.com/FINRAOS/JTAF-XCore/blob/master/DCO) agreement.

If you have any questions or discussion topics, please post them on [Google Groups](https://groups.google.com/forum/#!forum/jtaf-xcore).

Building
=========
XCore uses Maven for build. Please install Maven by downloading it [here](http://maven.apache.org/download.cgi).
```sh
# Clone XCore git repo
git clone git://github.com/FINRAOS/JTAF-XCore.git
cd JTAF-XCore

# Run package to compile and create jar
mvn package
```

Running Tests
==============
XCore uses Maven plugins to execute both the unit and integration tests.  You can run all of the tests by executing:
```sh
mvn verify
```
You can run individual tests by executing it from your IDE or through command line.  Please refer [here](http://finraos.github.io/JTAF-XCore/howitworks.html).

Requirements
==============
XCore requires Java SE version 7 or above available [here](http://www.oracle.com/technetwork/java/javase/downloads/index.html)

License Type
=============
JTAF projects including XCore is licensed under [Apache License Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)

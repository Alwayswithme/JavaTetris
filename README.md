## HOWTO run:
open your terminal and install java-1.7 jre

    # cd in the game directory
    $ cd ~/JavaTetris


    # setting CLASSPATH
    $ export CLASSPATH=./bin:./lib/dom4j-1.6.1.jar:./lib/mysql-connector.jar


    #run and enjoy
    $java main.Main

## HOWTO compile:
open your terminal and make sure using javac which
version is 1.7

    # cd in the game directory and make a bin dir
    $ cd ~/JavaTetris;mkdir bin

    # compile everything to the bin directory
    $ javac -d bin -cp lib/dom4j-1.6.1.jar:lib/mysql-connector.jar src/*/*.java src/*/*/*.java

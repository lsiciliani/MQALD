MQALD (Ver. 3.00 - updated 25/01/2021)
====================

Question Answering (QA) over Knowledge Graphs (KG) has the aim of developing a system that is capable of answering a user's question using the information coming from one or multiple Knowledge Graphs, like DBpedia, Wikidata and so on.
This kind of system needs to translate the question of the user, written using natural language, into a query formulated through a data query language that is compliant with the underlying KG.
The translation process is already tricky to solve even when trying to answer simple questions that involve a single triple pattern but becomes very troublesome when trying to cope with questions that require the presence in the final query of modifiers, aggregate functions, and query forms.
The attention over this aspect is growing but has never been thoroughly addressed by the existing literature.
Starting from the latest advances in this field, we want to make a further step towards this direction by giving a comprehensive description of this topic, the main issues revolving around it, and making publicly available a dataset designed to evaluate the performance of a QA system in translating such articulated questions into a specific data query language. 

Setup
--------

1. clone the project from GitHub
2. type "mvn package" into the project folder. Maven creates two jar files into the **target/** subfolder. One jar contains all the dependecies, the other one does not contains dependencies. The **lib/** contains all the jars' libraries.

For running the project, you can go into the **target/** subfolder and run the following commands:
1. for the creation of the output file (see CreateAnswer section for the parameters description)
> java -cp MQALD-1.0-SNAPSHOT-jar-with-dependencies.jar di.uniba.it.mqald.qasystems.CreateAnswer <parameters>
2. for the evaluation (see Eval section for the parameters description)
> java -cp MQALD-1.0-SNAPSHOT-jar-with-dependencies.jar di.uniba.it.mqald.eval.Eval <parameters>

CreateAnswer
---------------

usage: Create the answers JSON file for a specific QA system. Available <br>
       systems are: GAnswer, QAnswer, TeBaQA. [-i <arg>] [-o <arg>] [-s <br>
       <arg>] <br>
 -i <arg>   Input file (questions) <br>
 -o <arg>   Output file (answers) <br>
 -s <arg>   System name (available systems: GAnswer, QAnswer, TeBaQA <br>

Eval
-------

usage: Evaluate system output against the gold standard. [-g <arg>] [-s <br>
       <arg>] [-v] <br>
 -g <arg>   Gold standard file <br>
 -s <arg>   System file <br>
 -v         Verbose <br>

Contacts
-----------
Lucia Siciliani, lucia.siciali@uniba.it
Pierpaolo Basile, pierpaolo.basile@uniba.it

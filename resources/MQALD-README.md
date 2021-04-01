MQALD ver. 4.00 (01/04/2021)
================================

Changes:

* ver. 4.00 fix qald-version field in JSON
* ver. 3.00 query fix in MQALD
* ver. 2.00 query fix in MQALD

Dataset description
----------------------

MQALD is a dataset for the evaluation of question answering systems over knowledge graph.
In particular, the dataset is focused on SPARQL queries conting modifiers.
The SPARQL query language makes available several modifiers: query forms (like ***ASK***), solution sequence modifiers (e.g. **ORDER BY**, **LIMIT**, **OFFSET**), functions (e.g. **FILTER**, **COUNT**, **SUM**, **AVG**, **NOW**, **YEAR**, **MONTH**).
These modifiers can be used alone for simpler queries, but more frequently, they have to be combined to obtain a SPARQL query that is correct and allows to retrieve all the right answers.

MQALD is composed of two sections

1. **novel questions:** 100 questions that have been manually created by human annotators based on DBpedia 2016-10;
2. questions that require modifiers from the QALD dataset, we collect questions with modifiers available in the QALD (http://qald.aksw.org/) dataset over the last three editions (i.e. QALD-9, QALD-8, and QALD-7).

The MQALD dataset is in JSON format and compliant with the QALD dataset structure: each question is serialized as a JSON object which is then stored in a JSON array that contains all the questions.
The only exceptions in the structure of each question is represented by the presence of a further JSON array, named *modifiers*, containing all the modifiers occurring in the SPARQL query (this array is empty if there are no modifiers).

Files description
---------------------

We release several files containing a different subset of queries, in particular:

* **MQALD.json:** contains the novel questions plus the QALD test queries with modifiers
* **MQALD_new_query.json:** contains only the novel questions
* **MQALD-QALD-test-NOMOD.json** contains the novel questions plus all the QALD test queries (with and without modifiers)
* QALD-test-MOD-multilingual.json: contains only the QALD test queries with modifiers
* QALD-test-multilingual.json: contains the QALD test queries
* QALD-test-NOMOD-multilingual.json: contains the QALD test queries without modifiers
* QALD-train-multilingual.json: contains the QALD training queries
* QALD-train-MOD-multilingual.json: contains only the QALD training queries with modifiers
* QALD-train-NOMOD-multilingual.json: contains the QALD training queries without modifiers

Evaluation tool
-------------------

The tool for the evaluation is available on GitHub: https://github.com/lsiciliani/MQALD.

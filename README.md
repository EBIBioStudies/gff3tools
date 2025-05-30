# gff3tools

gff3tools is a Java library for converting EMBL flat files to GFF3 format. 
It uses [sequencetools](https://github.com/enasequence/sequencetools) to read the flat file.

# Compiling
To access the sequencetools library, create the `gradle.properties` file and add your private 
EBI gitlab token in the following format.   

```gitlab_private_token=<token>```

# Conversion Rules and Assumptions

Conversion rules and Assumptions are added to the code under `// Rule: ` and `// Assumption:` comments for now. 

# Building the project
Checkout the project
* Clone the project

```git clone https://github.com/EBIBioStudies/gff3tools.git```
* Change dir

```cd gff3tools```

* Build the project 

```./gradlew clean build``` 

The jar file will be found in /build/lib/gff3tools*.jar. You can use this jar to run the converter.


# Running ff to gff3 converter
```java -cp ./gff3tools-1.0.jar uk.ac.ebi.embl.converter.cli.FFToGff3CLI -in OZ026791.ff -out OZ026791.gff3```

# Running gff3 to ff converter
```java -cp ./gff3tools-1.0.jar uk.ac.ebi.embl.converter.cli.Gff3ToFFCLI -in OZ026791.gff3 -out OZ026791.ff```

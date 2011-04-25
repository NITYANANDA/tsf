Talend Service Factory Container - powered by Karaf
===========================================================================

The Talend Service Factory Container (aka TSF Container) is a lightweight
OSGi container powered by Apache Karaf.  It packs current versions of 
Equinox, Camel and CXF. The Container already contains everything needed to 
CXF services.  Still it is very compatible to the pure Karaf distribution.


Starting TSF Container
======================

The start scripts are in the container/bin directory. If you loaded the 
.zip version you will see Windows starters, for the .tar.gz version you 
will see Unix/Linux starters.

Scripts (use .bat extension for Windows):

 - TSF : Launch the container in foreground with the TSF (Karaf) console
 - karaf : same as TSF
 - server:  Start TSF Container in background:
 - client <command> : Connect to running Container and start shell 
   command <command>
 
At this point the examples can be run. Please refer to the README in 
each example for further instructions


Short introduction to TSF and Karaf
===========================

Make sure to read the Karaf getting started:
http://karaf.apache.org/manual/2.2.0/quick-start.html

> list

   Shows the list of installed bundles. -s will show the bundle symbolic
   name instead of the description
   
> start <id>

   Start the bundle with the given id

> stop <id>

   Stop the bundle with the given id

> features:listurl

   Shows the features.xml files that are available. These define which 
   features can be installed

> features:install <feature>

   Install a given feature. A feature contains bundles, config and 
   references to other features. They are an easy way to do complex 
   installations with one command

> ls

   Show the available OSGi services
   

Hints
====

Use <tab> for quick entry completion, it is available for most commands.

Use "| grep -i <searchstring>" to filter interesting information from long
lists.

TSF Container (Karaf) is very silent. Make sure to have a tail -f on the 
log running to not miss exceptions and warnings.

The container will keep its state after restarts. So to really reset the 
container delete the data directory.

Interested how a features.xml at 
mvn:org.apache.karaf.assemblies.features/standard/2.2.0/xml/features 
looks? Try "cat mvn:org.apache.karaf.assemblies.features/standard/2.2.0/xml/features"

Use the environment variable KARAF_DEBUG=true to prepare the container for 
a remote debugging session. Connect to it using the Eclipse "Remote Java 
Application" starter.


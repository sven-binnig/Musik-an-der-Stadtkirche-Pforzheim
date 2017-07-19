#!/bin/sh
mcP=; # classpath variable
for lib in lib/*.jar; do
    echo $lib
    mcP=$mcP:$lib
done
java -cp $mcP de.biware.pf.stadtkirche.musik.calendartools.ui.MainStage

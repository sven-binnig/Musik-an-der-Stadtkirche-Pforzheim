@echo off
cls
cd lib
for %%d in (dir *.jar) do call ..\cpappend.bat %%d
echo %mcp%
start /b javaw -cp %mcp% de.biware.pf.stadtkirche.musik.calendartools.ui.MainStage

